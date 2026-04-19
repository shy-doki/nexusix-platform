package com.shy.nexusix.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 跨域配置、静态资源映射、拦截器注册
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置跨域资源共享 (CORS)
     * 允许前端应用从不同域名访问后端 API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许的源，生产环境应限制具体域名
                .allowedOrigins("http://localhost:8080")
                // 允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 暴露的响应头
                .exposedHeaders("Authorization")
                // 是否允许携带凭证（如 cookies）
                .allowCredentials(true)
                // 预检请求的有效期（秒）
                .maxAge(3600);
    }

    /**
     * 配置静态资源访问路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 classpath 下的静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // 映射上传文件目录
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:/upload/");
    }

    /**
     * 注册拦截器
     * 用于请求拦截和处理
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO 待注册自定义拦截器
        // 例如：登录拦截器、日志拦截器等
        // 示例：
        // registry.addInterceptor(new LoginInterceptor())
        //         .addPathPatterns("/api/**")
        //         .excludePathPatterns("/api/auth/login");
    }

}
