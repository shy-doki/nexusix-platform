package com.shy.nexusix.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * <p>
 * Redis配置类 - 序列化策略与缓存管理
 * </p>
 * <p>
 * Key使用StringRedisSerializer（可读性好，便于Redis客户端查看）
 * Value使用Fastjson2序列化（与项目全局JSON框架统一，避免引入Jackson依赖冲突）
 * Sa-Token会话数据使用sa-token-fastjson2序列化，与业务RedisTemplate互不干扰
 * </p>
 *
 * @author shy
 * @since 2026-05-07
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 自定义RedisTemplate序列化策略
     * Key: String序列化
     * Value: Fastjson2序列化（带类型信息，反序列化时自动还原为原始类型）
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // Key序列化：String
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value序列化：Fastjson2（与项目全局JSON框架统一）
        RedisSerializer<Object> fastjson2Serializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object object) {
                if (object == null) {
                    return new byte[0];
                }
                return JSON.toJSONBytes(object, JSONWriter.Feature.WriteClassName);
            }

            @Override
            public Object deserialize(byte[] bytes) {
                if (bytes == null || bytes.length == 0) {
                    return null;
                }
                return JSON.parseObject(bytes, Object.class, JSONReader.Feature.SupportAutoType);
            }
        };
        template.setValueSerializer(fastjson2Serializer);
        template.setHashValueSerializer(fastjson2Serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 缓存管理器（用于Spring Cache注解 @Cacheable 等）
     * 默认缓存2小时，不缓存null值
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

}
