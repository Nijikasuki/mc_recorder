package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.service.ResonatorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

// 三层架构的【Web 层】：只管 HTTP（收请求、定 URL、返响应转 JSON）
// 业务逻辑不写这里，转交给 Service。当前链路：Controller → Service → Mapper → DB
@RestController
@RequestMapping("/api/resonators")   // 类级 URL 前缀，下面所有方法都以它开头
public class ResonatorController {

    // 依赖 Service（不再直接碰 Mapper）。Spring 启动时把 ResonatorService Bean 注进来
    private final ResonatorService service;

    @Autowired   // 单构造方法时可省略，显式写出便于理解"依赖注入"
    public ResonatorController(ResonatorService service) {
        this.service = service;
    }

    // GET /api/resonators —— 查列表（无路径参数、无 body）
    @GetMapping
    public Result<List<Resonator>> list() {
        return Result.success(service.findAll());
    }

    // GET /api/resonators/{id} —— 查详情
    // @PathVariable：数据在 URL 路径里
    @GetMapping("/{id}")
    public Result<Resonator> getOne(@PathVariable Long id) {
        Resonator r = service.findById(id);
        if (r == null) {
            return Result.fail(404,"角色不存在");
        }
        else{
            return Result.success(r);
        }
    }
    // POST /api/resonators —— 新增
    // @RequestBody：数据在请求体(body)里，JSON 反序列化成对象
    @PostMapping
    public Result<Resonator> create(@Valid @RequestBody Resonator resonator) {
        return Result.success(service.create(resonator));
    }

    // PUT /api/resonators/{id} —— 编辑
    // 两个数据来源同时用：id 在 URL 路径，新数据在 body
    @PutMapping("/{id}")
    public Result<Resonator> update(@Valid @RequestBody Resonator resonator, @PathVariable Long id) {
        resonator.setId(id);
        boolean ok = service.update(resonator);
        return ok ? Result.success(resonator) : Result.fail(404,"未成功更新");
    }

    // DELETE /api/resonators/{id} —— 删除
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean ok = service.delete(id);
        return ok ? Result.success():Result.fail(404,"未成功删除");
    }
}
