package com.shy.nexusix.core.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class SaTokenRedisSerializerConfig {

    @Configuration
    static class SaTokenRedisTemplatePostProcessor implements BeanPostProcessor {

        private final StringRedisSerializer stringSerializer = new StringRedisSerializer();

        private final RedisSerializer<Object> fastjson2Serializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object object) {
                if (object == null) {
                    return new byte[0];
                }
                return JSON.toJSONBytes(object,
                        JSONWriter.Feature.WriteClassName,
                        JSONWriter.Feature.NotWriteHashMapArrayListClassName,
                        JSONWriter.Feature.NotWriteRootClassName
                );
            }

            @Override
            public Object deserialize(byte[] bytes) {
                if (bytes == null || bytes.length == 0) {
                    return null;
                }
                return JSON.parseObject(bytes, Object.class, JSONReader.Feature.SupportAutoType);
            }
        };

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof RedisTemplate && beanName.equals("saTokenRedisTemplate")) {
                RedisTemplate<String, Object> template = (RedisTemplate<String, Object>) bean;
                template.setKeySerializer(stringSerializer);
                template.setHashKeySerializer(stringSerializer);
                template.setValueSerializer(fastjson2Serializer);
                template.setHashValueSerializer(fastjson2Serializer);
                template.afterPropertiesSet();
            }
            return bean;
        }
    }

}
