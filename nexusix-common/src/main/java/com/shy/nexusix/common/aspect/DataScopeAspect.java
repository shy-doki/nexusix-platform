package com.shy.nexusix.common.aspect;

import com.shy.nexusix.common.annotation.DataScope;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>数据权限切面，拦截@DataScope注解方法并注入数据权限过滤条件</p>
 *
 * @author shy
 */
@Aspect
@Component
public class DataScopeAspect {

    private static final Logger log = LoggerFactory.getLogger(DataScopeAspect.class);

    private static final String POINTCUT_DATA_SCOPE = "@annotation(com.shy.nexusix.common.annotation.DataScope)";

    /**
     * <p>数据权限上下文，使用ThreadLocal存储供MyBatis拦截器读取</p>
     */
    private static final ThreadLocal<DataScopeContext> DATA_SCOPE_CONTEXT = new ThreadLocal<>();

    /**
     * <p>数据权限切入点</p>
     */
    @Pointcut(POINTCUT_DATA_SCOPE)
    public void dataScopePointcut() {
    }

    /**
     * <p>前置通知，解析注解配置并设置数据权限上下文</p>
     *
     * @param joinPoint 连接点
     */
    @Before("dataScopePointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取方法签名并转换为MethodSignature类型，用于提取方法对象和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法对象，用于读取方法上的@DataScope注解
        Method method = signature.getMethod();

        // 获取方法上的@DataScope注解实例
        DataScope dataScope = method.getAnnotation(DataScope.class);

        // 检查注解是否启用
        if (!dataScope.enabled()) {
            return;
        }

        // 创建数据权限上下文对象，用于存储所有权限相关信息
        DataScopeContext context = new DataScopeContext();

        // 从注解中提取部门表别名，用于SQL拼接
        // 例如：SELECT * FROM sys_user u LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
        // 其中d就是部门表别名
        context.setDeptAlias(dataScope.deptAlias());

        // 从注解中提取用户表别名，用于SQL拼接
        // 例如：SELECT * FROM sys_user u WHERE u.user_id = ?
        // 其中u就是用户表别名
        context.setUserAlias(dataScope.userAlias());

        // 从注解中提取部门ID字段名，用于权限过滤条件
        // 通常是dept_id，用于关联部门表
        context.setDeptIdField(dataScope.deptIdField());

        // 从注解中提取用户ID字段名，用于权限过滤条件
        // 通常是user_id，用于标识数据所有者
        context.setUserIdField(dataScope.userIdField());

        // 从注解中提取数据权限范围类型
        // 决定了生成什么样的SQL过滤条件
        context.setScopeType(dataScope.scopeType());

        // 根据权限范围类型生成SQL片段
        // 这个SQL片段将被MyBatis拦截器注入到原SQL中
        context.setSqlSegment(buildDataScopeSql(dataScope));

        // 存入ThreadLocal供MyBatis拦截器读取
        DATA_SCOPE_CONTEXT.set(context);

        log.debug("[数据权限] 别名: dept={}, user={} | 权限字段: deptId={}, userId={} | 范围: {}",
                dataScope.deptAlias(), dataScope.userAlias(),
                dataScope.deptIdField(), dataScope.userIdField(),
                dataScope.scopeType().getDescription());
    }

    /**
     * <p>后置通知，清理数据权限上下文</p>
     */
    @After("dataScopePointcut()")
    public void doAfter() {
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>异常通知，清理数据权限上下文</p>
     */
    @AfterThrowing("dataScopePointcut()")
    public void doAfterThrowing() {
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>获取当前线程的数据权限上下文</p>
     *
     * @return 数据权限上下文，不存在则返回null
     */
    public static DataScopeContext getDataScopeContext() {
        return DATA_SCOPE_CONTEXT.get();
    }

    /**
     * <p>清理当前线程的数据权限上下文</p>
     */
    public static void clearDataScopeContext() {
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>构建数据权限SQL片段</p>
     *
     * @param dataScope 数据权限注解配置
     * @return 数据权限SQL片段
     */
    private String buildDataScopeSql(DataScope dataScope) {
        StringBuilder sql = new StringBuilder();
        String deptAlias = dataScope.deptAlias();
        String userAlias = dataScope.userAlias();
        String deptIdField = dataScope.deptIdField();
        String userIdField = dataScope.userIdField();

        // 根据权限范围类型生成不同的SQL片段
        switch (dataScope.scopeType()) {
            case ALL:
                // 全部数据权限：不添加过滤条件
                return "";

            case DEPT_AND_CHILD:
                // 本部门及子部门
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" IN (SELECT dept_id FROM sys_dept WHERE dept_id = #{currentDeptId} OR FIND_IN_SET(#{currentDeptId}, ancestors))");
                break;

            case DEPT_ONLY:
                // 本部门
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" = #{currentDeptId}");
                break;

            case SELF_ONLY:
                // 仅本人
                sql.append(" OR ").append(userAlias).append(".").append(userIdField)
                   .append(" = #{currentUserId}");
                break;

            case CUSTOM:
                // 自定义权限
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" IN (SELECT dept_id FROM sys_role_dept WHERE role_id = #{currentRoleId})");
                break;

            case AUTO:
            default:
                // 自动判断
                sql.append(" OR 1=1 ");
                break;
        }

        // 处理SQL片段格式：移除开头的" OR "，用括号包围
        if (sql.length() > 0 && sql.charAt(0) == ' ' && sql.charAt(1) == 'O' && sql.charAt(2) == 'R') {
            sql.delete(0, 4);
            sql.insert(0, "(");
            sql.append(")");
        }

        return sql.toString();
    }

    /**
     * <p>数据权限上下文，存储数据权限过滤所需信息</p>
     */
    public static class DataScopeContext {
        /** 部门表别名 */
        private String deptAlias;
        /** 用户表别名 */
        private String userAlias;
        /** 部门ID字段名 */
        private String deptIdField;
        /** 用户ID字段名 */
        private String userIdField;
        /** 数据权限范围类型 */
        private DataScope.DataScopeType scopeType;
        /** 生成的SQL片段 */
        private String sqlSegment;
        /** 额外参数存储 */
        private Map<String, Object> params = new ConcurrentHashMap<>();

        /**
         * <p>获取部门表别名</p>
         *
         * @return 部门表别名
         */
        public String getDeptAlias() {
            return deptAlias;
        }

        /**
         * <p>设置部门表别名</p>
         *
         * @param deptAlias 部门表别名
         */
        public void setDeptAlias(String deptAlias) {
            this.deptAlias = deptAlias;
        }

        /**
         * <p>获取用户表别名</p>
         *
         * @return 用户表别名
         */
        public String getUserAlias() {
            return userAlias;
        }

        /**
         * <p>设置用户表别名</p>
         *
         * @param userAlias 用户表别名
         */
        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
        }

        /**
         * <p>获取部门ID字段名</p>
         *
         * @return 部门ID字段名
         */
        public String getDeptIdField() {
            return deptIdField;
        }

        /**
         * <p>设置部门ID字段名</p>
         *
         * @param deptIdField 部门ID字段名
         */
        public void setDeptIdField(String deptIdField) {
            this.deptIdField = deptIdField;
        }

        /**
         * <p>获取用户ID字段名</p>
         *
         * @return 用户ID字段名
         */
        public String getUserIdField() {
            return userIdField;
        }

        /**
         * <p>设置用户ID字段名</p>
         *
         * @param userIdField 用户ID字段名
         */
        public void setUserIdField(String userIdField) {
            this.userIdField = userIdField;
        }

        /**
         * <p>获取数据权限范围类型</p>
         *
         * @return 数据权限范围类型
         */
        public DataScope.DataScopeType getScopeType() {
            return scopeType;
        }

        /**
         * <p>设置数据权限范围类型</p>
         *
         * @param scopeType 数据权限范围类型
         */
        public void setScopeType(DataScope.DataScopeType scopeType) {
            this.scopeType = scopeType;
        }

        /**
         * <p>获取生成的SQL片段</p>
         *
         * @return SQL片段
         */
        public String getSqlSegment() {
            return sqlSegment;
        }

        /**
         * <p>设置生成的SQL片段</p>
         *
         * @param sqlSegment SQL片段
         */
        public void setSqlSegment(String sqlSegment) {
            this.sqlSegment = sqlSegment;
        }

        /**
         * <p>获取额外参数存储</p>
         *
         * @return 参数字典
         */
        public Map<String, Object> getParams() {
            return params;
        }

        /**
         * <p>设置额外参数存储</p>
         *
         * @param params 参数字典
         */
        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        /**
         * <p>添加额外参数</p>
         *
         * @param key 参数名
         * @param value 参数值
         */
        public void addParam(String key, Object value) {
            this.params.put(key, value);
        }

        /**
         * <p>获取额外参数</p>
         *
         * @param key 参数名
         * @return 参数值
         */
        public Object getParam(String key) {
            return this.params.get(key);
        }
    }
}
