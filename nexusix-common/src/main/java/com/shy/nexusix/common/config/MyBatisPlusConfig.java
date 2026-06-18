package com.shy.nexusix.common.config;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * <p>MyBatisPlus配置类，配置分页、乐观锁、防SQL注入等插件</p>
 *
 * @author shy
 */
@Configuration
public class MyBatisPlusConfig {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisPlusConfig.class);

    /**
     * <p>配置MyBatis-Plus拦截器插件链</p>
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        logger.info("开始配置MyBatis-Plus拦截器");

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户插件（暂未启用）
//        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
//        tenantInterceptor.setTenantLineHandler(new com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler() {
//            @Override
//            public String getTenantIdColumn() {
//                return "tenant_id";
//            }
//
//            @Override
//            public boolean ignoreTable(String tableName) {
//                List<String> ignoreTables = Arrays.asList(
//                        "sys_tenant", "prod_package", "prod_package_quota", "sys_user",
//                        "sys_permission", "sys_permission_policy", "sys_user_role_rel",
//                        "sys_user_group_rel", "sys_menu", "sys_dict_item", "sys_user_tenant_rel",
//                        "sys_role", "sys_tenant_security", "sys_login_log", "sys_oper_log",
//                        "sys_perm_policy", "sys_role_policy", "sys_user_perm_rel"
//                );
//                return ignoreTables.contains(tableName);
//            }
//
//            @Override
//            public Expression getTenantId() {
//                if (!StpUtil.isLogin()) {
//                    return new LongValue(1L);
//                }
//                SaSession session = StpUtil.getSession(false);
//                if (session == null) {
//                    return new LongValue(1L);
//                }
//                Object tenantIdObj = session.get("tenantId");
//                if (tenantIdObj == null) {
//                    return new LongValue(1L);
//                }
//                long tenantId = parseLongSafely(tenantIdObj);
//                return new LongValue(tenantId);
//            }
//        });
//        interceptor.addInnerInterceptor(tenantInterceptor);
        logger.info("多租户插件配置完成");

        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.POSTGRE_SQL);
        paginationInterceptor.setOverflow(true);
        paginationInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInterceptor);
        logger.info("分页插件配置完成");

        // 乐观锁插件
        OptimisticLockerInnerInterceptor optimisticLockerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInterceptor);
        logger.info("乐观锁插件配置完成");

        // 防SQL注入攻击插件，拦截没有WHERE条件的UPDATE/DELETE
        BlockAttackInnerInterceptor blockAttackInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInterceptor);
        logger.info("防SQL注入插件配置完成");

        logger.info("MyBatis-Plus拦截器配置完成");
        return interceptor;
    }

    /**
     * <p>安全解析Long值，解析失败返回默认值1</p>
     *
     * @param value 待解析的值
     * @return 解析后的Long值
     */
    private long parseLongSafely(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            logger.warn("租户ID解析失败，使用默认值: 1, value={}", value);
            return 1L;
        }
    }

}
