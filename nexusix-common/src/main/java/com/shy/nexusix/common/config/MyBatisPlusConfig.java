package com.shy.nexusix.common.config;

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
 * <p>
 * MyBatisPlus配置类 核心、全局配置见application.yml
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Configuration
public class MyBatisPlusConfig {

    // 创建静态日志记录器，用于记录配置过程中的日志信息
    private static final Logger logger = LoggerFactory.getLogger(MyBatisPlusConfig.class);

    /**
     * 配置 MyBatis-Plus 插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        logger.info("开始配置MyBatis-Plus拦截器");

        // 创建MyBatis-Plus拦截器实例，用于管理和执行多个内部拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 创建多租户插件拦截器实例，用于实现数据的多租户隔离
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        // 设置租户ID处理器，定义如何获取租户ID和哪些表需要忽略租户过滤
        tenantInterceptor.setTenantLineHandler(new com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler() {
            @Override
            public String getTenantIdColumn() {
                // 租户ID字段名
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // 定义需要忽略租户过滤的表名列表，这些表通常是系统级别的公共表
                List<String> ignoreTables = Arrays.asList(
                        "prod_package",
                        "prod_package_quota",
                        "sys_user",
                        "sys_permission",
                        "sys_permission_policy",
                        "sys_user_group_rel",
                        "sys_menu",
                        "sys_dict_item"
                );
                // 判断当前表是否在忽略列表中
                boolean ignore = ignoreTables.contains(tableName);
                if (ignore) {
                    logger.debug("租户过滤忽略表: {}", tableName);
                }
                // 返回是否需要忽略该表的租户过滤
                return ignore;
            }

            @Override
            public Expression getTenantId() {
                // TODO 暂时返回默认租户ID 1 后续结合Sa-Token实现获取租户ID
                long tenantId = 1L;
                logger.debug("当前租户ID: {}", tenantId);
                // 将租户ID包装为JSqlParser的LongValue表达式对象并返回
                return new LongValue(tenantId);
            }
        });
        // 将配置好的多租户拦截器添加到拦截器链中
        interceptor.addInnerInterceptor(tenantInterceptor);
        logger.info("多租户插件配置完成");

        // 创建分页插件拦截器实例，用于支持SQL分页查询
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型为POSTGRESQL，以便生成正确的分页SQL语句
        paginationInterceptor.setDbType(DbType.POSTGRE_SQL);
        // 启用页码溢出处理，当请求页码超过总页数时自动返回最后一页的数据
        paginationInterceptor.setOverflow(true);
        // 设置单页最大记录数限制，防止一次性查询过多数据导致性能问题
        paginationInterceptor.setMaxLimit(500L);
        // 将配置好的分页拦截器添加到拦截器链中
        interceptor.addInnerInterceptor(paginationInterceptor);
        logger.info("分页插件配置完成");

        // 创建乐观锁插件拦截器实例，用于支持基于版本号的乐观锁机制 [多人同时修改同一条数据导致覆盖 靠版本号字段 version 实现 需要在实体类中加入@Version 并且有Version字段]
        OptimisticLockerInnerInterceptor optimisticLockerInterceptor = new OptimisticLockerInnerInterceptor();
        // 将配置好的乐观锁拦截器添加到拦截器链中
        interceptor.addInnerInterceptor(optimisticLockerInterceptor);
        logger.info("乐观锁插件配置完成");

        // 创建防SQL注入攻击插件拦截器实例，用于拦截恶意的全表更新和删除操作 [拦截没有 WHERE 条件的 UPDATE / DELETE 语句，直接抛异常，不让执行]
        BlockAttackInnerInterceptor blockAttackInterceptor = new BlockAttackInnerInterceptor();
        // 将配置好的防SQL注入拦截器添加到拦截器链中
        interceptor.addInnerInterceptor(blockAttackInterceptor);
        logger.info("防SQL注入插件配置完成");
        
        logger.info("MyBatis-Plus拦截器配置完成");
        // 返回配置好的拦截器实例，由Spring容器管理
        return interceptor;
    }

}