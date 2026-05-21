package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.mapper.ResonatorMapper;
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

    public List<Resonator> findAll() {
        return mapper.findAll();
    }

    public Resonator findById(Long id) {
        return mapper.findById(id);
    }

    public Resonator create(Resonator resonator) {
        mapper.insert(resonator);
        return resonator;
    }

    public boolean update(Resonator resonator) {
        int affectedRows = mapper.update(resonator);
        return affectedRows > 0;
    }

    public boolean delete(Long id) {
        int affectedRows = mapper.delete(id);
        return affectedRows > 0;
    }
}
