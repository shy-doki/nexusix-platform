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
 * <p>Sa-Token配置类 - JWT Simple模式与全局过滤器</p>
 *
 * @author shy
 */
@Configuration
public class SaTokenConfig {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

//    /**
//     * Sa-Token全局过滤器（暂未启用）
//     * <p>功能：拦截请求执行认证、处理异常、设置跨域头</p>
//     */
//    @Bean
//    public SaServletFilter saServletFilter() {
//        return new SaServletFilter()
//                .addInclude("/**").addExclude("/favicon.ico")
//                .setAuth(obj -> {
//                    if (!SaHolder.getRequest().getRequestPath().startsWith("/auth")) {
//                        StpUtil.checkLogin();
//                    }
//                })
//                .setError(e -> SaResult.error(e.getMessage()))
//                .setBeforeAuth(obj -> {
//                    SaHolder.getResponse()
//                            .setHeader("Access-Control-Allow-Origin", "*")
//                            .setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
//                            .setHeader("Access-Control-Max-Age", "3600")
//                            .setHeader("Access-Control-Allow-Headers", "*");
//                    if (SaHolder.getRequest().getMethod().equalsIgnoreCase("OPTIONS")) {
//                        SaHolder.getResponse().setStatus(HttpServletResponse.SC_OK);
//                        return;
//                    }
//                });
//    }

    /**
     * 配置Sa-Token整合JWT（Simple模式）
     * <p>Token为JWT格式，会话数据存储在Redis中，支持无状态验签与主动踢下线</p>
     *
     * @return StpLogicJwtForSimple实例
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

}
