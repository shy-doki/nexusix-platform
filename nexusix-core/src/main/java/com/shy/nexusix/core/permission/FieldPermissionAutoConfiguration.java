package com.shy.nexusix.core.permission;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 字段权限自动配置类 - 注册拦截器到MyBatis-Plus拦截器链
 * </p>
 * <p>
 * 拦截器执行顺序：
 * <ol>
 *   <li>TenantLineInnerInterceptor（租户过滤）</li>
 *   <li>FieldPermissionInterceptor（字段裁剪）← 本配置注册</li>
 *   <li>PaginationInnerInterceptor（分页）</li>
 *   <li>OptimisticLockerInnerInterceptor（乐观锁）</li>
 *   <li>BlockAttackInnerInterceptor（防SQL注入）</li>
 * </ol>
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
@Configuration
public class FieldPermissionAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FieldPermissionAutoConfiguration.class);

    /**
     * 创建字段权限拦截器Bean
     *
     * @param fieldPermissionConfig 字段权限配置引擎
     * @return 字段权限拦截器实例
     */
    @Bean
    public FieldPermissionInterceptor fieldPermissionInterceptor(FieldPermissionConfig fieldPermissionConfig) {
        log.info("[字段权限] 创建字段权限拦截器");
        return new FieldPermissionInterceptor(fieldPermissionConfig);
    }

    /**
     * 将字段权限拦截器注册到MyBatis-Plus拦截器链
     * 插入位置：租户拦截器之后、分页拦截器之前（索引1）
     *
     * @param mybatisPlusInterceptor      MyBatis-Plus拦截器实例
     * @param fieldPermissionInterceptor  字段权限拦截器实例
     */
    @Autowired
    public void registerFieldPermissionInterceptor(MybatisPlusInterceptor mybatisPlusInterceptor,
                                                    FieldPermissionInterceptor fieldPermissionInterceptor) {
        // 在租户拦截器（索引0）之后插入，分页拦截器之前
        mybatisPlusInterceptor.getInterceptors().add(1, fieldPermissionInterceptor);
        log.info("[字段权限] 字段权限拦截器已注册到MyBatis-Plus拦截器链（位置：索引1）");
    }

}
