package com.shy.nexusix.common.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>Web配置类 - 跨域、静态资源映射、拦截器注册</p>
 *
 * @author shy
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置CORS跨域，允许前端localhost:8080访问后端API
     *
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源路径映射
     *
     * @param registry 资源处理注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:/upload/");
    }

//    /**
//     * Sa-Token登录拦截器（暂未启用）
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
//                .addPathPatterns(
//                        "/tenant/**",
//                        "/sys-tenant-subscription/**",
//                        "/sys-user/**",
//                        "/sys-role/**",
//                        "/sys-permission/**",
//                        "/sys-permission-policy/**",
//                        "/sys-user-token/**",
//                        "/sys-user-role-rel/**",
//                        "/sys-user-tenant-rel/**"
//                )
//                .excludePathPatterns(
//                        "/auth/login",
//                        "/auth/register",
//                        "/doc.html#/**",
//                        "/favicon.ico"
//                );
//    }

}
