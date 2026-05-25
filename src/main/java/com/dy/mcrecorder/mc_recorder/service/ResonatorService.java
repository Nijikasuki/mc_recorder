package com.dy.mcrecorder.mc_recorder.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        LambdaQueryWrapper<Resonator> q = new LambdaQueryWrapper<>();
        q.eq(Resonator::getOwnerId,userId);
        return mapper.selectList(q);
    }

    public Resonator findById(Long id, Long userId) {
        LambdaQueryWrapper<Resonator> q = new LambdaQueryWrapper<>();
        q.eq(Resonator::getId,id).eq(Resonator::getOwnerId,userId);
        return mapper.selectOne(q);
    }

    @CacheEvict(value = "resonators", key = "#resonator.ownerId")
    public Resonator create(Resonator resonator) {
        mapper.insert(resonator);
        return resonator;
    }

    @CacheEvict(value = "resonators", key = "#resonator.ownerId")
    public boolean update(Resonator resonator) {
        LambdaUpdateWrapper<Resonator> u = new LambdaUpdateWrapper<>();
        u.eq(Resonator::getId,resonator.getId()).eq(Resonator::getOwnerId,resonator.getOwnerId());
        int affectedRows = mapper.update(resonator,u);
        return affectedRows > 0;
    }
    @CacheEvict(value = "resonators", key = "#userId")
    public boolean delete(Long id, Long userId) {
        LambdaQueryWrapper<Resonator> q = new LambdaQueryWrapper<>();
        q.eq(Resonator::getId,id).eq(Resonator::getOwnerId,userId);
        int affectedRows = mapper.delete(q);
        return affectedRows > 0;
    }

    public Page<Resonator> findPage(int pageNo, int pageSize, Long userId) {
        Page<Resonator> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Resonator> q = new LambdaQueryWrapper<>();
        q.eq(Resonator::getOwnerId,userId);
        return mapper.selectPage(page,q);
    }
}
