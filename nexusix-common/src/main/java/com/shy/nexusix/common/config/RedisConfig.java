package com.shy.nexusix.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
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
     * RedisTemplate配置，使用FastJSON2序列化
     * 作用：提供Redis的基本操作接口
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate实例
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(factory);

        // 配置key的序列化器：使用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 配置hash key的序列化器：使用StringRedisSerializer
        template.setHashKeySerializer(new StringRedisSerializer());

        // 配置value的序列化器：使用FastJSON2
        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(fastJson2JsonRedisSerializer);
        template.setHashValueSerializer(fastJson2JsonRedisSerializer);

        // 初始化
        template.afterPropertiesSet();
        return template;
    }

    /**
     * StringRedisTemplate配置
     * 作用：专门处理String类型的Redis操作
     * @param factory Redis连接工厂
     * @return 配置好的StringRedisTemplate实例
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        // 创建StringRedisTemplate实例
        StringRedisTemplate template = new StringRedisTemplate();
        // 设置连接工厂
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * 缓存管理器配置，使用FastJSON2序列化
     * 作用：管理应用中的缓存，支持@Cacheable等注解
     * @param factory Redis连接工厂
     * @return 配置好的CacheManager实例
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 创建FastJSON2序列化器
        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer =
                new FastJson2JsonRedisSerializer<>(Object.class);

        // 创建Redis缓存配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 设置默认过期时间为1小时
                // 配置key的序列化方式：使用StringRedisSerializer
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 配置value的序列化方式：使用FastJSON2序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJson2JsonRedisSerializer))
                .disableCachingNullValues(); // 不缓存空值

        // 构建RedisCacheManager
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config) // 应用默认配置
                .build();
    }

}
