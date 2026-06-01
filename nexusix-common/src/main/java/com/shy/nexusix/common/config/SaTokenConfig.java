package com.shy.nexusix.common.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * Sa-Token 配置类 - 集成Redis、JWT、FastJSON2
 * </p>
 * <p>
 * 主要功能：
 * 1. 配置Sa-Token全局过滤器，实现统一的认证授权
 * 2. 配置Sa-Token数据持久层，使用Redis存储会话数据
 * 3. 配置JWT逻辑（如果使用JWT模式）
 * 4. 设置跨域请求支持
 * </p>
 *
 * @author shy
 * @since 2026-05-07
 */
@Configuration
public class SaTokenConfig {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 注册 Sa-Token 全局过滤器
     *
     * 过滤器的作用：
     * 1. 拦截所有请求（除了排除的路径）
     * 2. 执行认证逻辑
     * 3. 处理认证异常
     * 4. 设置跨域请求头
     */
//    @Bean
//    public SaServletFilter saServletFilter() {
//        return new SaServletFilter()
//                // 拦截所有请求路径，但排除静态资源
//                .addInclude("/**").addExclude("/favicon.ico")
//
//                // 认证函数: 每次请求执行
//                // 作用：检查用户是否已登录，未登录则抛出异常
//                .setAuth(obj -> {
//                    // 检查请求路径是否需要登录验证
//                    // 如果不是认证相关的路径，则需要检查登录状态
//                    if (!SaHolder.getRequest().getRequestPath().startsWith("/auth")) {
//                        // 检查当前用户是否已登录
//                        StpUtil.checkLogin();
//                    }
//                })
//
//                // 异常处理函数：每次认证函数发生异常时执行此函数
//                // 作用：统一处理认证失败的情况，返回统一格式的错误信息
//                .setError(e -> {
//                    // 打印异常日志
//                    System.out.println("---------- sa全局异常 ");
//                    System.out.println("异常:: " + e.getMessage());
//
//                    // 返回统一格式的错误结果
//                    return SaResult.error(e.getMessage());
//                })
//
//                // 前置函数：在每次认证函数之前执行
//                // 作用：设置跨域请求头，处理OPTIONS预检请求
//                .setBeforeAuth(obj -> {
//                    // ---------- 设置跨域请求头 ----------
//                    // 允许所有域名访问
//                    SaHolder.getResponse()
//                            .setHeader("Access-Control-Allow-Origin", "*")
//                            // 允许的HTTP方法
//                            .setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
//                            // 预检请求的有效期（秒）
//                            .setHeader("Access-Control-Max-Age", "3600")
//                            // 允许携带的请求头
//                            .setHeader("Access-Control-Allow-Headers", "*");
//
//                    // 如果是预检请求（OPTIONS方法），则立即返回200状态码
//                    // 预检请求是浏览器为了安全考虑，先发送一个OPTIONS请求询问服务器是否允许跨域请求
//                    if (SaHolder.getRequest().getMethod().equalsIgnoreCase("OPTIONS")) {
//                        SaHolder.getResponse().setStatus(HttpServletResponse.SC_OK);
//                        // OPTIONS请求不需要继续处理后续逻辑
//                        return;
//                    }
//                });
//    }

    /**
     * Sa-Token 整合 JWT (Simple模式)
     * Simple模式: Token为JWT格式, 会话数据存储在Redis中
     * 优势: Token本身可无状态验证签名, Redis存储支持主动踢下线
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

}
