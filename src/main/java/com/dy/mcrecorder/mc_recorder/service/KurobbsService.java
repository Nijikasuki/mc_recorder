package com.dy.mcrecorder.mc_recorder.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dy.mcrecorder.mc_recorder.config.KurobbsProperties;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.KurobbsResponse;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.KurobbsRole;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.RoleDataPayload;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.SyncResult;
import com.dy.mcrecorder.mc_recorder.entity.KuroToken;
import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.exception.BizException;
import com.dy.mcrecorder.mc_recorder.mapper.GameTokenMapper;
import com.dy.mcrecorder.mc_recorder.mapper.ResonatorMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KurobbsService {
    private final KurobbsProperties props;
    private final GameTokenMapper tokenMapper;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ResonatorMapper resonatorMapper;

    public KurobbsService(KurobbsProperties props, GameTokenMapper tokenMapper, ObjectMapper objectMapper,ResonatorMapper resonatorMapper) {
        this.props = props;
        this.tokenMapper = tokenMapper;
        this.objectMapper = objectMapper;
        this.resonatorMapper =  resonatorMapper;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public void bind(KuroToken kuroToken) {
        KuroToken existing = tokenMapper.selectById(kuroToken.getUserId());
        if (existing == null) {
            tokenMapper.insert(kuroToken);
        } else {
            tokenMapper.updateById(kuroToken);   // 直接覆盖
        }
    }

    public List<KurobbsRole> getRoles(Long userId) {
        // 1. 从 DB 读这个用户的 token
        KuroToken t = tokenMapper.selectById(userId);
        if(t == null){
            throw new BizException("用户未绑定库街区, 请先去绑定页面操作");
        }
        String body = "gameId=" + props.getGameId()
                + "&roleId=" + t.getGameRoleId()
                + "&serverId=" + t.getServerId()
                + "&channelId=" + props.getChannelId()
                + "&countryCode=" + props.getCountryCode();

        KurobbsResponse resp = restClient.post()
                .uri("/aki/roleBox/akiBox/roleData")
                .header("b-at", t.getBatToken())
                .header("did", props.getDid())
                .header("source", props.getSource())
                .header("Origin", props.getOrigin())
                .header("User-Agent", props.getUserAgent())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KurobbsResponse.class);

        if (resp == null) {
            throw new BizException("库街区返回为空");
        }

        // 4. 处理 token 过期
        if (resp.isTokenExpired()) {
            tokenMapper.deleteById(userId);
            throw new BizException("库街区 token 已失效, 请重新绑定");
        }

        if (!resp.isOk()) {
            throw new BizException(502,"库街区请求失败: " + resp.msg());
        }

        // 5. 解析嵌套 data 字符串 → RoleDataPayload
        try {
            RoleDataPayload payload = objectMapper.readValue(resp.data(), RoleDataPayload.class);
            return payload.roleList();
        } catch (Exception e) {
            throw new RuntimeException("解析库街区返回失败: " + e.getMessage(), e);
        }
    }


    // ⭐ @CacheEvict: 同步成功后清掉 Redis 里这个用户的 resonator 缓存
    //    否则 ResonatorService.findAll 会一直返旧的(空)缓存
    @CacheEvict(value = "resonators", key = "#userId")
    public SyncResult syncToResonator(Long userId) {
        // 步骤 1: 从库街区拿最新角色 (复用 getRoles)
        List<KurobbsRole> kurobbsRoles = getRoles(userId);

        // 步骤 2: 删除该用户当前 source='kurobbs' 的所有旧数据
        LambdaQueryWrapper<Resonator> q = new LambdaQueryWrapper<>();
        q.eq(Resonator::getOwnerId, userId).eq(Resonator::getSource, "kurobbs");
        resonatorMapper.delete(q);

        // 步骤 3: 一条一条 INSERT 新数据
        LocalDateTime now = LocalDateTime.now();
        for (KurobbsRole kr : kurobbsRoles) {
            Resonator r = toResonator(userId, kr, now);
            resonatorMapper.insert(r);
        }
        return new SyncResult(kurobbsRoles.size(), now);
    }

    private Resonator toResonator(Long userId, KurobbsRole kr, LocalDateTime syncedAt) {
        Resonator r = new Resonator();

        // 用户归属
        r.setOwnerId(userId);

        // 业务核心
        r.setName(kr.roleName());
        r.setElement(kr.attributeName());
        r.setStar(kr.starLevel());
        r.setLevel(kr.level());
        r.setResonanceChain(kr.chainUnlockNum());

        // 扩展字段
        r.setBreach(kr.breach());
        r.setIsMainRole(kr.isMainRole());
        r.setWeaponTypeName(kr.weaponTypeName());
        r.setRoleIconUrl(kr.roleIconUrl());
        r.setRolePicUrl(kr.rolePicUrl());

        // 同步元数据
        r.setKuroRoleId(kr.roleId());
        r.setSource("kurobbs");
        r.setSyncedAt(syncedAt);

        // 皮肤 (有就填)
        if (kr.roleSkin() != null) {
            r.setSkinName(kr.roleSkin().skinName());
            r.setSkinPicUrl(kr.roleSkin().picUrl());
        }

        return r;
    }
}
