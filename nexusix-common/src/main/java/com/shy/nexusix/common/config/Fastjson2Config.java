package com.shy.nexusix.common.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        // 创建 Fastjson2 消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 配置 Fastjson2
        FastJsonConfig config = new FastJsonConfig();

        // 设置序列化特性
        config.setWriterFeatures(
                // 空列表输出 []
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 空字符串输出 ""
                // JSONWriter.Feature.WriteNullStringAsEmpty,
                // 空数字输出 0
                JSONWriter.Feature.WriteNullNumberAsZero,
                // 空布尔值输出 false
                // JSONWriter.Feature.WriteNullBooleanAsFalse,
                // 输出 null 值
                // JSONWriter.Feature.WriteMapNullValue,
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

}
