package com.shy.nexusix.core.config;

import cn.dev33.satoken.dao.SaTokenDao;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.shy.nexusix.common.constant.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义 Sa-Token Redis 存储 DAO
 * </p>
 * <p>
 * 接管 Sa-Token 的 Key 规则、Value 序列化、数据结构形态。
 * 使用项目全局 Redis 配置（StringRedisTemplate），通过 Key 前缀实现命名空间隔离。
 * String 类型操作直接使用 StringRedisTemplate，Object 类型操作使用 Fastjson2 序列化（与项目全局 JSON 框架统一）。
 * Redis 连接的获取与释放由 StringRedisTemplate 内部连接池自动管理，无需手动干预。
 * </p>
 *
 * @author shy
 * @since 2026-05-09
 */
@Component
@Primary
public class CustomSaTokenDao implements SaTokenDao {

    /**
     * Sa-Token Redis 键前缀
     * <p>完整格式：nexusix:satoken:{saTokenOriginalKey}</p>
     * <p>使用场景：所有 Sa-Token 相关数据的 Redis 键均以此前缀开头，与业务数据隔离</p>
     */
    private static final String SA_TOKEN_KEY_PREFIX = GlobalConstant.RedisKey.SA_TOKEN_PREFIX;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // -------------------- String 操作 --------------------

    /**
     * <p>
     * 获取Value，如无返空
     * </p>
     *
     * @param key 键名
     * @return 对应的Value值，不存在则返回null
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(SA_TOKEN_KEY_PREFIX + key);
    }

    /**
     * <p>
     * 写入Value，并设定存活时间（单位：秒）
     * </p>
     *
     * @param key 键名
     * @param value 值
     * @param timeout 存活时间（秒），-1表示永不过期
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void set(String key, String value, long timeout) {
        if (timeout == -1) {
            stringRedisTemplate.opsForValue().set(SA_TOKEN_KEY_PREFIX + key, value);
        } else {
            stringRedisTemplate.opsForValue().set(SA_TOKEN_KEY_PREFIX + key, value, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * <p>
     * 修改Value
     * </p>
     * <p>
     * 更新键对应的值，同时保留原有的过期时间
     * </p>
     *
     * @param key 键名
     * @param value 新值
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void update(String key, String value) {
        // 获取当前键的剩余存活时间
        String redisKey = SA_TOKEN_KEY_PREFIX + key;
        Long ttl = stringRedisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        // 写入新值
        stringRedisTemplate.opsForValue().set(redisKey, value);

        // 恢复原有的过期时间
        if (ttl != null && ttl > 0) {
            stringRedisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
        }
    }

    /**
     * <p>
     * 删除Value
     * </p>
     *
     * @param key 键名
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(SA_TOKEN_KEY_PREFIX + key);
    }

    /**
     * <p>
     * 获取Value的剩余存活时间（单位：秒）
     * </p>
     *
     * @param key 键名
     * @return 剩余存活时间（秒），-1表示永不过期，-2表示键不存在
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public long getTimeout(String key) {
        Long ttl = stringRedisTemplate.getExpire(SA_TOKEN_KEY_PREFIX + key, TimeUnit.SECONDS);
        return ttl != null ? ttl : -2;
    }

    /**
     * <p>
     * 修改Value的剩余存活时间（单位：秒）
     * </p>
     *
     * @param key 键名
     * @param timeout 新的存活时间（秒），-1表示永不过期
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void updateTimeout(String key, long timeout) {
        String redisKey = SA_TOKEN_KEY_PREFIX + key;
        if (timeout == -1) {
            // 移除过期时间，设为永不过期
            stringRedisTemplate.persist(redisKey);
        } else {
            stringRedisTemplate.expire(redisKey, timeout, TimeUnit.SECONDS);
        }
    }

    // -------------------- Object 操作 --------------------

    /**
     * <p>
     * 获取Object，如无返空
     * </p>
     * <p>
     * 使用 Fastjson2 反序列化（SupportAutoType），与项目全局 JSON 框架统一
     * </p>
     *
     * @param key 键名
     * @return 对应的Object对象，不存在则返回null
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public Object getObject(String key) {
        String json = stringRedisTemplate.opsForValue().get(SA_TOKEN_KEY_PREFIX + key);
        if (json == null) {
            return null;
        }
        return JSON.parseObject(json, Object.class, JSONReader.Feature.SupportAutoType);
    }

    /**
     * <p>
     * 写入Object，并设定存活时间（单位：秒）
     * </p>
     * <p>
     * 使用 Fastjson2 序列化（WriteClassName），保留类型信息以便反序列化时还原为原始类型
     * </p>
     *
     * @param key 键名
     * @param object 对象
     * @param timeout 存活时间（秒），-1表示永不过期
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void setObject(String key, Object object, long timeout) {
        // 使用 Fastjson2 序列化，保留类型信息
        String json = JSON.toJSONString(object, JSONWriter.Feature.WriteClassName);
        if (timeout == -1) {
            stringRedisTemplate.opsForValue().set(SA_TOKEN_KEY_PREFIX + key, json);
        } else {
            stringRedisTemplate.opsForValue().set(SA_TOKEN_KEY_PREFIX + key, json, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * <p>
     * 更新Object
     * </p>
     * <p>
     * 更新键对应的对象，同时保留原有的过期时间
     * </p>
     *
     * @param key 键名
     * @param object 新对象
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void updateObject(String key, Object object) {
        // 获取当前键的剩余存活时间
        String redisKey = SA_TOKEN_KEY_PREFIX + key;
        Long ttl = stringRedisTemplate.getExpire(redisKey, TimeUnit.SECONDS);

        // 使用 Fastjson2 序列化新对象
        String json = JSON.toJSONString(object, JSONWriter.Feature.WriteClassName);
        stringRedisTemplate.opsForValue().set(redisKey, json);

        // 恢复原有的过期时间
        if (ttl != null && ttl > 0) {
            stringRedisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
        }
    }

    /**
     * <p>
     * 删除Object
     * </p>
     *
     * @param key 键名
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void deleteObject(String key) {
        stringRedisTemplate.delete(SA_TOKEN_KEY_PREFIX + key);
    }

    /**
     * <p>
     * 获取Object的剩余存活时间（单位：秒）
     * </p>
     *
     * @param key 键名
     * @return 剩余存活时间（秒），-1表示永不过期，-2表示键不存在
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public long getObjectTimeout(String key) {
        Long ttl = stringRedisTemplate.getExpire(SA_TOKEN_KEY_PREFIX + key, TimeUnit.SECONDS);
        return ttl != null ? ttl : -2;
    }

    /**
     * <p>
     * 修改Object的剩余存活时间（单位：秒）
     * </p>
     *
     * @param key 键名
     * @param timeout 新的存活时间（秒），-1表示永不过期
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public void updateObjectTimeout(String key, long timeout) {
        String redisKey = SA_TOKEN_KEY_PREFIX + key;
        if (timeout == -1) {
            // 移除过期时间，设为永不过期
            stringRedisTemplate.persist(redisKey);
        } else {
            stringRedisTemplate.expire(redisKey, timeout, TimeUnit.SECONDS);
        }
    }

    // -------------------- 数据搜索 --------------------

    /**
     * <p>
     * 搜索数据
     * </p>
     * <p>
     * 根据前缀和关键字搜索匹配的键名，支持分页和排序。
     * 搜索结果会移除自定义前缀，返回 Sa-Token 原始键名格式。
     * </p>
     *
     * @param prefix 键名前缀
     * @param keyword 搜索关键字
     * @param start 起始位置
     * @param size 返回数量
     * @param sortType 排序方式：true-正序，false-倒序
     * @return 匹配的键名列表
     * @author shy
     * @since 2026-05-09
     */
    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        // 构建搜索模式：自定义前缀 + Sa-Token前缀 + 关键字通配符
        String pattern = SA_TOKEN_KEY_PREFIX + prefix + "*" + keyword + "*";

        // 执行模糊匹配查询
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        // 移除自定义前缀，还原为 Sa-Token 原始键名
        List<String> keyList = keys.stream()
                .map(k -> k.substring(SA_TOKEN_KEY_PREFIX.length()))
                .collect(Collectors.toList());

        // 排序处理
        if (sortType) {
            Collections.sort(keyList);
        } else {
            Collections.sort(keyList, Collections.reverseOrder());
        }

        // 分页截取
        int fromIndex = Math.min(start, keyList.size());
        int toIndex = Math.min(start + size, keyList.size());
        return new ArrayList<>(keyList.subList(fromIndex, toIndex));
    }

}
