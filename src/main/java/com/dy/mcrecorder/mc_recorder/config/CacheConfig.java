package com.dy.mcrecorder.mc_recorder.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

// 缓存配置：开启 Spring 缓存抽象 + 配 Redis 当后端 + 设 TTL/序列化器
@Configuration
@EnableCaching   // ⭐ 打开"声明式缓存"。没这个注解，@Cacheable/@CacheEvict 全部失效
public class CacheConfig {

    // @Bean 方法的参数 Spring 会自动从容器注入；Spring Boot 已给你造好 Jackson 3 的 ObjectMapper
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          ObjectMapper objectMapper) {
        // 缓存的默认配置：5 分钟过期 + key 用纯字符串 + value 用 JSON
        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                // 全局 TTL：和你之前 CACHE_TTL 一样的 5 分钟
                .entryTtl(Duration.ofMinutes(5))
                // key 序列化：纯字符串（默认是二进制，redis-cli 看不懂）
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                // value 序列化：JSON（默认是 JDK 序列化，redis-cli 里是乱码）
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults)
                .build();
    }
}
