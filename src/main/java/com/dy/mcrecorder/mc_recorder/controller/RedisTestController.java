package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/api/test/redis")
@Tag(name = "测试 - Redis Playground", description = "感受 RedisTemplate API 的玩具接口；功能完成后将删")
public class RedisTestController {
    private final StringRedisTemplate redisTemplate;
    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{key}")
    @Operation(summary = "取 Redis 值", description = "key 不存在 → data 为 null")
    public Result<String> get(@PathVariable String key){
        return Result.success(redisTemplate.opsForValue().get(key));
    }

    @PostMapping("/{key}")
    @Operation(summary = "设 Redis 值（30 秒 TTL）", description = "value 通过 URL 查询参数 ?value=xxx 传入")
    public Result<Void> set(@PathVariable String key,@RequestParam String value ){
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(30));
        return  Result.success();
    }

    @DeleteMapping("/{key}")
    @Operation(summary = "删 Redis key")
    public Result<Void> delete(@PathVariable String key){
        redisTemplate.delete(key);
        return  Result.success();
    }
}
