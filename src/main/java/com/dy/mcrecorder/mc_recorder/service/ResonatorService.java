package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.mapper.ResonatorMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service：和 @RestController/@Mapper 同类——声明这是个 Spring Bean（业务逻辑层）
// 启动时被扫描、注册进容器，可被 Controller 构造注入
@Service
public class ResonatorService {

    // 和 Controller 注入 Mapper 一个套路：Service 注入 Mapper
    // 现在 Controller 不再直接碰 Mapper，改成「Controller → Service → Mapper」
    private final ResonatorMapper mapper;

    public ResonatorService(ResonatorMapper mapper) {
        this.mapper = mapper;
    }

    // 目前这些方法多数只是「转发」给 Mapper —— 这是预期的。
    // Service 当前的价值是「占好业务逻辑的位置」，
    // 等做数据校验/复杂规则时，逻辑就往这些方法里填（比如 update 前先查存在性）。
    @Cacheable(value = "resonators",key = "#userId")
    public List<Resonator> findAll(Long userId) {
//        String key = cacheKey(userId);
//        String cached = redisTemplate.opsForValue().get(key);
//        if (cached != null) {
//            return objectMapper.readValue(cached, new TypeReference<List<Resonator>>() {});
//        }
//        List<Resonator> list = mapper.findAll(userId);
//
//        String json = objectMapper.writeValueAsString(list);
//        redisTemplate.opsForValue().set(key,json, CACHE_TTL);
//        return list;
        return mapper.findAll(userId);
    }

    public Resonator findById(Long id, Long userId) {
        return mapper.findById(id,userId);
    }
    @CacheEvict(value = "resonators", key = "#resonator.ownerId")
    public Resonator create(Resonator resonator) {
        mapper.insert(resonator);
//        redisTemplate.delete(cacheKey(resonator.getOwnerId()));
        return resonator;
    }
    @CacheEvict(value = "resonators", key = "#resonator.ownerId")
    public boolean update(Resonator resonator) {
        int affectedRows = mapper.update(resonator);
//        if (affectedRows > 0) {
//            redisTemplate.delete(cacheKey(resonator.getOwnerId()));   // ← 加这行（只在真改到了才清）
//        }
        return affectedRows > 0;
    }
    @CacheEvict(value = "resonators", key = "#userId")
    public boolean delete(Long id, Long userId) {
        int affectedRows = mapper.delete(id,userId);
//        if (affectedRows > 0) {
//            redisTemplate.delete(cacheKey(userId));   // ← 加这行（只在真改到了才清）关于"affectedRows > 0 才清".注解版不再有这个微优化——@CacheEvict 默认方法成功执行后就清，不看返回值。对学习项目无所谓，少一行判断更干净。
//        }
        return affectedRows > 0;
    }
}
