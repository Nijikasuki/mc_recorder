package com.dy.mcrecorder.mc_recorder.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import com.dy.mcrecorder.mc_recorder.service.ResonatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

// 三层架构的【Web 层】：只管 HTTP（收请求、定 URL、返响应转 JSON）
// 业务逻辑不写这里，转交给 Service。当前链路：Controller → Service → Mapper → DB
@RestController
@RequestMapping("/api/resonators")
@Tag(name = "2. 共鸣者管理", description = "角色 CRUD —— 多用户隔离，每人只能看/管自己的")
public class ResonatorController {

    // 依赖 Service（不再直接碰 Mapper）。Spring 启动时把 ResonatorService Bean 注进来
    private final ResonatorService service;

    public ResonatorController(ResonatorService service) {
        this.service = service;
    }

    // GET /api/resonators —— 查列表（无路径参数、无 body）
    @GetMapping
    @Operation(summary = "查询我的角色列表", description = "返回当前登录用户的全部角色（走 Redis 缓存，5 分钟 TTL）")
    public Result<List<Resonator>> list(@AuthenticationPrincipal Long userId) {
        return Result.success(service.findAll(userId));
    }

    // GET /api/resonators/{id} —— 查详情
    // @PathVariable：数据在 URL 路径里
    @GetMapping("/{id}")
    @Operation(summary = "查询某个角色", description = "按 id 查；不存在或不归你 → 404")
    public Result<Resonator> getOne(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        Resonator r = service.findById(id,userId);
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
    @Operation(summary = "新增角色", description = "owner_id 自动填为当前登录用户，请求里不用传也不能传")
    public Result<Resonator> create(@Valid @RequestBody Resonator resonator, @AuthenticationPrincipal Long userId) {
        resonator.setOwnerId(userId);
        return Result.success(service.create(resonator));
    }

    // PUT /api/resonators/{id} —— 编辑
    // 两个数据来源同时用：id 在 URL 路径，新数据在 body
    @PutMapping("/{id}")
    @Operation(summary = "编辑角色", description = "只能改自己的；改不存在的或别人的 → 404")
    public Result<Resonator> update(@Valid @RequestBody Resonator resonator, @PathVariable Long id, @AuthenticationPrincipal Long userId) {
        resonator.setId(id);
        resonator.setOwnerId(userId);
        boolean ok = service.update(resonator);
        return ok ? Result.success(resonator) : Result.fail(404,"未成功更新");
    }

    // DELETE /api/resonators/{id} —— 删除
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "只能删自己的；删不存在的或别人的 → 404")
    public Result<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Long userId) {
        boolean ok = service.delete(id,userId);
        return ok ? Result.success():Result.fail(404,"未成功删除");
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询我的角色列表", description = "pageNum 默认 1，pageSize 默认 10")
    public Result<Page<Resonator>> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @AuthenticationPrincipal Long userId) {
        return Result.success(service.findPage(pageNum,pageSize,userId));
    }
}
