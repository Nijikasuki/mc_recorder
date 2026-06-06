package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.config.KurobbsProperties;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.KurobbsResponse;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.KurobbsRole;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.RoleDataPayload;
import com.dy.mcrecorder.mc_recorder.entity.KuroToken;
import com.dy.mcrecorder.mc_recorder.mapper.GameTokenMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class KurobbsApiClient {
    private final KurobbsProperties props;
    private final GameTokenMapper tokenMapper;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KurobbsApiClient(KurobbsProperties props, GameTokenMapper tokenMapper, ObjectMapper objectMapper) {
        this.props = props;
        this.tokenMapper = tokenMapper;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    /**
     * 查用户的鸣潮所有角色
     */

    public List<KurobbsRole> getRoles(Long userId) {
        // 1. 从 DB 读这个用户的 token
        KuroToken t = tokenMapper.selectById(userId);
        if(t == null){
            throw new RuntimeException("用户未绑定库街区, 请先去绑定页面操作");
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
            throw new RuntimeException("库街区返回为空");
        }

        // 4. 处理 token 过期
        if (resp.isTokenExpired()) {
            tokenMapper.deleteById(userId);
            throw new RuntimeException("库街区 token 已失效, 请重新绑定");
        }

        if (!resp.isOk()) {
            throw new RuntimeException("库街区请求失败: " + resp.msg());
        }

        // 5. 解析嵌套 data 字符串 → RoleDataPayload
        try {
            RoleDataPayload payload = objectMapper.readValue(resp.data(), RoleDataPayload.class);
            return payload.roleList();
        } catch (Exception e) {
            throw new RuntimeException("解析库街区返回失败: " + e.getMessage(), e);
        }
    }
}
