package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
@RequestMapping("/api/test/redis")
public class RedisTestController {
    private final StringRedisTemplate redisTemplate;
    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{key}")
    public Result<String> get(@PathVariable String key){
        return Result.success(redisTemplate.opsForValue().get(key));
    }

    @PostMapping("/{key}")
    public Result<Void> set(@PathVariable String key,@RequestParam String value ){
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(30));
        return  Result.success();
    }

    @DeleteMapping("/{key}")
    public Result<Void> delete(@PathVariable String key){
        redisTemplate.delete(key);
        return  Result.success();
    }
}
