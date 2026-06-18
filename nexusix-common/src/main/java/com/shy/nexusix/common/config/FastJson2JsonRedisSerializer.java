package com.shy.nexusix.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>FastJSON2 Redis序列化器，将Java对象与Redis字节数组互转</p>
 *
 * @author shy
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    // 定义默认字符集为UTF-8
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 要序列化的对象类型
    private Class<T> clazz;

    /**
     * <p>构造函数</p>
     *
     * @param clazz 要序列化的对象类型
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * <p>将Java对象序列化为字节数组</p>
     *
     * @param value 要序列化的对象
     * @return 序列化后的字节数组
     * @throws SerializationException 序列化异常
     */
    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }
        // 将对象转换为JSON字符串，并指定序列化特性
        return JSON.toJSONString(value,
                // 写入类名信息，便于反序列化时确定具体类型
                JSONWriter.Feature.WriteClassName,
                // 序列化时保留null值字段
                JSONWriter.Feature.WriteMapNullValue,
                // 格式化输出，便于调试查看
                JSONWriter.Feature.PrettyFormat
        ).getBytes(DEFAULT_CHARSET);
    }

    /**
     * <p>将字节数组反序列化为Java对象</p>
     *
     * @param bytes 字节数组
     * @return 反序列化后的对象
     * @throws SerializationException 反序列化异常
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        // 如果字节数组为空或长度为0，返回null
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        // 将字节数组转换为字符串
        String str = new String(bytes, DEFAULT_CHARSET);
        // 启用自动类型支持
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }
}
