package com.shy.nexusix.core.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Sa-Token 核心配置类 - JWT整合与StpLogic注入
 * </p>
 * <p>
 * 配置Sa-Token使用JWT Simple模式：
 * - Token为JWT格式，可无状态验证签名合法性
 * - 会话数据存储在Redis中，支持主动踢下线
 * - 通过注入StpLogicJwtForSimple替换默认StpLogic实现
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
@Configuration
public class SaTokenConfig {

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
