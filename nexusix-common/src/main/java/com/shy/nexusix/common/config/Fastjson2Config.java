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
 * <p>Fastjson2全局配置，替换默认HttpMessageConverter</p>
 *
 * @author shy
 */
@Configuration
public class Fastjson2Config implements WebMvcConfigurer {

    /**
     * <p>配置Fastjson2为默认消息转换器</p>
     *
     * @param converters 消息转换器列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清除所有默认转换器（避免 Jackson 残留）
        converters.clear();

        // 创建 Fastjson2 消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 配置 Fastjson2
        FastJsonConfig config = new FastJsonConfig();

        // 序列化特性
        config.setWriterFeatures(
                JSONWriter.Feature.PrettyFormat
                // 空布尔值输出 false
                // JSONWriter.Feature.WriteNullBooleanAsFalse,
                // 输出 null 值
                // JSONWriter.Feature.WriteMapNullValue,
                // 空字符串输出 ""
                // JSONWriter.Feature.WriteNullStringAsEmpty,
                // 空数字输出 0
                // JSONWriter.Feature.WriteNullNumberAsZero,
                // 空列表输出 []
                // JSONWriter.Feature.WriteNullListAsEmpty,
        );

        // 反序列化特性
        config.setReaderFeatures(
                // 智能匹配字段
                JSONReader.Feature.SupportSmartMatch,
                // 忽略null值字段
                JSONReader.Feature.IgnoreSetNullValue
        );

        // 默认日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

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

        // 手动注册 FastJSON2 转换器（推荐：显式控制，确保唯一）
        // fastjson2-extension-spring6 会自动注册，但手动注册可确保优先级和配置生效
        converters.add(converter);
    }

}
