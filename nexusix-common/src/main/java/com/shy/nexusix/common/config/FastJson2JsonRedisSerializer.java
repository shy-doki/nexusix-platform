package com.shy.nexusix.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * FastJSON2 Redis序列化器
 * 作用：将Java对象序列化为Redis可存储的字节数组，或将字节数组反序列化为Java对象
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    // 定义默认字符集为UTF-8
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 要序列化的对象类型
    private Class<T> clazz;

    /**
     * 构造函数
     * @param clazz 要序列化的对象类型
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化方法：将Java对象转换为字节数组
     * @param value 要序列化的对象
     * @return 序列化后的字节数组
     * @throws SerializationException 序列化异常
     */
    @Override
    public byte[] serialize(T value) throws SerializationException {
        // 如果对象为空，返回空字节数组
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
        ).getBytes(DEFAULT_CHARSET); // 使用UTF-8编码转换为字节数组
    }

    /**
     * 反序列化方法：将字节数组转换为Java对象
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
        // 将JSON字符串解析为指定类型的对象，启用自动类型支持
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }
}
