package com.shy.nexusix.core.permission;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * 字段权限自动配置类 - 注册拦截器到MyBatis-Plus拦截器链
 * </p>
 * <p>
 * 设计说明：
 * 由于模块层次结构约束（nexusix-common 不能依赖 nexusix-core），
 * 字段权限拦截器不能直接在 MyBatisPlusConfig 中注册。
 * 本配置类使用 SmartInitializingSingleton 接口，在所有Bean初始化完成后，
 * 通过反射将字段权限拦截器插入到MyBatis-Plus拦截器链的指定位置。
 * </p>
 * <p>
 * 拦截器执行顺序：
 * <ol>
 *   <li>TenantLineInnerInterceptor（租户过滤，索引0）</li>
 *   <li>FieldPermissionInterceptor（字段裁剪，索引1）← 本配置注册</li>
 *   <li>PaginationInnerInterceptor（分页，索引2）</li>
 *   <li>OptimisticLockerInnerInterceptor（乐观锁，索引3）</li>
 *   <li>BlockAttackInnerInterceptor（防SQL注入，索引4）</li>
 * </ol>
 * </p>
 * <p>
 * 为什么使用反射：
 * MyBatis-Plus的 MybatisPlusInterceptor.setInterceptors() 方法会替换整个列表，
 * 而不是追加。为了在已配置的拦截器链中插入新拦截器，需要通过反射访问内部列表。
 * 这是当前架构下的最优解，既保持了模块边界，又实现了拦截器的动态注册。
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
@Configuration
public class FieldPermissionAutoConfiguration implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(FieldPermissionAutoConfiguration.class);

    private static final String INTERCEPTORS_FIELD_NAME = "interceptors";

    private final MybatisPlusInterceptor mybatisPlusInterceptor;
    private final ObjectProvider<FieldPermissionInterceptor> interceptorProvider;

    /**
     * 构造函数注入依赖
     *
     * @param mybatisPlusInterceptor MyBatis-Plus拦截器（由nexusix-common模块提供）
     * @param interceptorProvider    字段权限拦截器的延迟加载提供者
     */
    public FieldPermissionAutoConfiguration(
            MybatisPlusInterceptor mybatisPlusInterceptor,
            ObjectProvider<FieldPermissionInterceptor> interceptorProvider) {
        this.mybatisPlusInterceptor = mybatisPlusInterceptor;
        this.interceptorProvider = interceptorProvider;
        log.info("[字段权限] 字段权限自动配置类初始化完成");
    }

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
     * 在所有单例Bean初始化完成后，将字段权限拦截器注册到MyBatis-Plus拦截器链
     * <p>
     * 使用SmartInitializingSingleton接口的原因：
     * 1. 确保MybatisPlusInterceptor Bean已完全初始化
     * 2. 确保FieldPermissionInterceptor Bean已创建完成
     * 3. 避免Spring循环依赖问题
     * 4. 提供确定的Bean初始化时机
     * </p>
     */
    @Override
    public void afterSingletonsInstantiated() {
        log.info("[字段权限] 开始注册字段权限拦截器到MyBatis-Plus拦截器链");

        try {
            // 获取拦截器实例（此时已完成初始化）
            FieldPermissionInterceptor interceptor = interceptorProvider.getObject();
            if (interceptor == null) {
                log.error("[字段权限] 字段权限拦截器实例为空，注册失败");
                return;
            }

            // 通过反射访问MybatisPlusInterceptor内部的interceptors列表
            Field interceptorsField = MybatisPlusInterceptor.class.getDeclaredField(INTERCEPTORS_FIELD_NAME);
            interceptorsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<InnerInterceptor> interceptorList = (List<InnerInterceptor>) interceptorsField.get(mybatisPlusInterceptor);

            if (interceptorList == null) {
                log.error("[字段权限] MyBatis-Plus拦截器链为空，注册失败");
                return;
            }

            // 在索引1的位置插入字段权限拦截器（租户过滤之后，分页之前）
            interceptorList.add(1, interceptor);
            log.info("[字段权限] 字段权限拦截器已成功注册到MyBatis-Plus拦截器链（位置：索引1，租户过滤之后）");
            log.info("[字段权限] 当前拦截器链顺序：租户过滤 → 字段裁剪 → 分页 → 乐观锁 → 防SQL注入");

        } catch (NoSuchFieldException e) {
            // MyBatis-Plus内部字段名变更，通常是版本升级导致
            log.error("[字段权限] 注册失败：MyBatis-Plus内部字段 '{}' 不存在，请检查版本兼容性", INTERCEPTORS_FIELD_NAME, e);
            throw new RuntimeException("[字段权限] MyBatis-Plus版本不兼容，无法注册字段权限拦截器", e);
        } catch (IllegalAccessException e) {
            // 安全策略阻止反射访问，通常不会发生
            log.error("[字段权限] 注册失败：无法访问MyBatis-Plus拦截器内部字段，请检查安全策略配置", e);
            throw new RuntimeException("[字段权限] 反射访问被拒绝，无法注册字段权限拦截器", e);
        } catch (Exception e) {
            // 其他未知异常
            log.error("[字段权限] 注册字段权限拦截器时发生未知异常", e);
            throw new RuntimeException("[字段权限] 注册字段权限拦截器失败", e);
        }
    }

}
