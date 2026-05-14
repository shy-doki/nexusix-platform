package com.shy.nexusix.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Fastjson2 全局配置
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Configuration
public class Fastjson2Config implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建自定义 Fastjson2 消息转换器
        FilteredFastJsonHttpMessageConverter converter = new FilteredFastJsonHttpMessageConverter();

        // 配置 Fastjson2
        FastJsonConfig config = new FastJsonConfig();

        // 设置序列化特性
        config.setWriterFeatures(
                // 空列表输出 []
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 空数字输出 0
                JSONWriter.Feature.WriteNullNumberAsZero,
                // 空布尔值输出 false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                // 美化输出
                JSONWriter.Feature.PrettyFormat
        );

        // 设置反序列化特性
        config.setReaderFeatures(
                // 对读取到的字符串值做去空格处理
                JSONReader.Feature.TrimString,
                // 忽略输入为null的字段
                JSONReader.Feature.IgnoreSetNullValue
        );

        // 设置默认日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 配置消息转换器
        converter.setFastJsonConfig(config);

        // 设置全局属性预过滤器
        converter.setPropertyPreFilter(new PropertyValueFilter());

        // 设置支持的媒体类型
        List<MediaType> mediaTypes = new ArrayList<>();
        // 添加 application/json 类型（标准 JSON 格式）
        mediaTypes.add(MediaType.APPLICATION_JSON);
        // 添加 application/json;charset=UTF-8 类型（带字符集的 JSON 格式）
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        // 添加 text/plain 类型（纯文本格式）
        mediaTypes.add(MediaType.TEXT_PLAIN);
        // 添加 text/html 类型（HTML 格式）
        mediaTypes.add(MediaType.TEXT_HTML);
        // 将支持的媒体类型列表设置到转换器中
        converter.setSupportedMediaTypes(mediaTypes);

        // 设置字符集 确保中文等多字节字符正确处理
        converter.setDefaultCharset(StandardCharsets.UTF_8);

        // 将 Fastjson2 消息转换器添加到列表开头（优先级最高）
        converters.add(0, converter);
    }

    /**
     * <p>
     * 支持全局属性预过滤的 Fastjson2 HTTP 消息转换器
     * </p>
     *
     * @author shy
     * @since 2026-05-14
     */
    public static class FilteredFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

        private PropertyPreFilter propertyPreFilter;

        public void setPropertyPreFilter(PropertyPreFilter propertyPreFilter) {
            this.propertyPreFilter = propertyPreFilter;
        }

        @Override
        protected void writeInternal(Object object, org.springframework.http.HttpOutputMessage outputMessage) throws IOException {
            String json;
            if (propertyPreFilter != null) {
                json = JSON.toJSONString(object, propertyPreFilter);
            } else {
                json = JSON.toJSONString(object);
            }
            OutputStream out = outputMessage.getBody();
            out.write(json.getBytes(getDefaultCharset()));
            out.flush();
        }

    }

    /**
     * <p>
     * Fastjson2 全局属性预过滤器
     * </p>
     *
     * <p>
     * 作用: 在属性序列化前进行值过滤，以下类型的属性值将被过滤掉，不包含在序列化结果中：
     * 1. null 值
     * 2. 空字符串 ""
     * 3. 仅包含空格的字符串（如 " "、"  " 等）
     * </p>
     *
     * @author shy
     * @since 2026-05-14
     */
    public static class PropertyValueFilter implements PropertyPreFilter {

        @Override
        public boolean process(JSONWriter jsonWriter, Object object, String name) {
            if (object == null) {
                return true;
            }

            try {
                Class<?> objectClass = object.getClass();
                java.lang.reflect.Field field = objectClass.getDeclaredField(name);
                field.setAccessible(true);
                Object value = field.get(object);

                if (value == null) {
                    return false;
                }

                if (value instanceof String && ((String) value).isBlank()) {
                    return false;
                }

                return true;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 无法获取字段时，保持默认序列化行为
                return true;
            }
        }

    }

}
