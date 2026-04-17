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
 * <p>
 * 数据权限切面 - 数据范围过滤控制组件
 * </p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li>拦截带有@DataScope注解的方法，实现数据权限过滤</li>
 *   <li>支持多种数据权限范围：全部数据、本部门及子部门、本部门、仅本人、自定义</li>
 *   <li>通过ThreadLocal存储数据权限上下文，供MyBatis拦截器使用</li>
 *   <li>支持自定义表别名和权限字段名</li>
 *   <li>自动生成数据权限SQL片段</li>
 * </ul>
 *
 * <h3>设计目的：</h3>
 * <p>
 * 实现细粒度的数据权限控制，确保用户只能访问其权限范围内的数据。
 * 通过注解驱动的方式，使数据权限配置更加灵活，支持不同业务场景的权限需求。
 * 配合MyBatis拦截器使用，在SQL执行前自动注入权限过滤条件。
 * </p>
 *
 * <h3>适用场景：</h3>
 * <ul>
 *   <li>多租户系统的数据隔离</li>
 *   <li>组织架构下的数据权限控制</li>
 *   <li>敏感数据的访问控制</li>
 *   <li>业务数据的范围过滤</li>
 * </ul>
 *
 * <h3>核心实现逻辑：</h3>
 * <ol>
 *   <li>通过注解切入点匹配所有带有@DataScope注解的方法</li>
 *   <li>解析注解配置，获取表别名、权限字段等信息</li>
 *   <li>根据数据权限范围类型生成对应的SQL片段</li>
 *   <li>将权限上下文存入ThreadLocal供MyBatis拦截器读取</li>
 *   <li>方法执行完成后清理ThreadLocal资源</li>
 * </ol>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 本部门及子部门数据权限
 * &#64;DataScope(deptAlias = "d", userAlias = "u", scopeType = DataScopeType.DEPT_AND_CHILD)
 * public List&lt;User&gt; selectUserList() { ... }
 *
 * // 仅本人数据权限
 * &#64;DataScope(deptAlias = "d", userAlias = "u", scopeType = DataScopeType.SELF_ONLY)
 * public List&lt;Order&gt; selectMyOrders() { ... }
 * </pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>需要配合MyBatis拦截器使用，拦截器通过getDataScopeContext()获取权限上下文</li>
 *   <li>SQL片段中的参数占位符（如#{currentDeptId}）需要由拦截器注入实际值</li>
 *   <li>确保在方法执行完成后清理ThreadLocal资源，避免内存泄漏</li>
 *   <li>数据权限范围需要与用户权限表配合使用</li>
 * </ul>
 *
 * @author shy
 * @since 2026-04-07
 * @see com.shy.nexusix.common.annotation.DataScope
 * @see com.shy.nexusix.common.annotation.DataScope.DataScopeType
 * @see DataScopeContext
 */
@Aspect
@Component
public class DataScopeAspect {

    private static final Logger log = LoggerFactory.getLogger(DataScopeAspect.class);

    private static final String POINTCUT_DATA_SCOPE = "@annotation(com.shy.nexusix.common.annotation.DataScope)";

    /**
     * 数据权限上下文存储
     * <p>
     * 使用ThreadLocal存储数据权限上下文，确保每个线程有独立的上下文副本。
     * MyBatis拦截器通过getDataScopeContext()方法获取当前线程的权限上下文。
     * </p>
     */
    private static final ThreadLocal<DataScopeContext> DATA_SCOPE_CONTEXT = new ThreadLocal<>();

    /**
     * <p>
     * 定义数据权限切入点
     * </p>
     *
     * <h4>切入点表达式说明：</h4>
     * <pre>
     * @annotation(com.shy.nexusix.common.annotation.DataScope)
     * </pre>
     * <ul>
     *   <li><b>@annotation</b>: 表示匹配带有指定注解的方法</li>
     *   <li><b>com.shy.nexusix.common.annotation.DataScope</b>: 指定要匹配的注解全限定名</li>
     * </ul>
     *
     * <h4>作用范围：</h4>
     * <p>
     * 该切入点会匹配项目中所有带有@DataScope注解的方法，无论其所在的包路径、类名或方法名。
     * 通常用于Mapper层或Service层的查询方法上，配合MyBatis拦截器实现数据权限过滤。
     * </p>
     *
     * <h4>匹配条件：</h4>
     * <p>
     * 只要方法上标注了@DataScope注解，该方法就会被切面拦截处理。
     * </p>
     */
    @Pointcut(POINTCUT_DATA_SCOPE)
    public void dataScopePointcut() {
    }

    /**
     * <p>
     * 前置通知 - 在目标方法执行前设置数据权限上下文
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当带有@DataScope注解的方法被调用时，在目标方法执行之前立即执行此通知。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>检查注解是否启用（enabled属性）</li>
     *   <li>解析@DataScope注解，提取表别名、权限字段等配置</li>
     *   <li>根据数据权限范围类型生成SQL片段</li>
     *   <li>创建DataScopeContext对象并存入ThreadLocal</li>
     *   <li>输出调试日志</li>
     * </ol>
     *
     * @param joinPoint 连接点对象，包含目标方法的相关信息
     *                  <ul>
     *                    <li>joinPoint.getSignature(): 获取方法签名，用于提取注解信息</li>
     *                  </ul>
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
        // enabled属性允许在特定情况下临时禁用数据权限过滤
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

        // 将上下文存入ThreadLocal，供MyBatis拦截器读取
        // 使用ThreadLocal确保多线程环境下数据隔离
        DATA_SCOPE_CONTEXT.set(context);

        // 输出调试日志，便于开发调试
        log.debug("[数据权限] 别名: dept={}, user={} | 权限字段: deptId={}, userId={} | 范围: {}",
                dataScope.deptAlias(), dataScope.userAlias(),
                dataScope.deptIdField(), dataScope.userIdField(),
                dataScope.scopeType().getDescription());
    }

    /**
     * <p>
     * 后置通知 - 在目标方法执行后清理数据权限上下文
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标方法执行完成（无论正常返回还是抛出异常）后执行此通知。
     * 确保ThreadLocal资源被正确清理，避免内存泄漏。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>从ThreadLocal中移除数据权限上下文</li>
     * </ol>
     */
    @After("dataScopePointcut()")
    public void doAfter() {
        // 清理ThreadLocal资源，防止内存泄漏
        // 在线程池环境下，线程会被复用，如果不清理会导致数据残留
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>
     * 异常通知 - 在目标方法抛出异常后清理数据权限上下文
     * </p>
     *
     * <h4>触发时机：</h4>
     * <p>
     * 当目标方法执行过程中抛出异常时执行此通知。
     * 确保异常情况下ThreadLocal资源也能被正确清理。
     * </p>
     *
     * <h4>执行逻辑：</h4>
     * <ol>
     *   <li>从ThreadLocal中移除数据权限上下文</li>
     * </ol>
     */
    @AfterThrowing("dataScopePointcut()")
    public void doAfterThrowing() {
        // 清理ThreadLocal资源，防止内存泄漏
        // 异常情况下也需要清理，确保资源释放
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>
     * 获取当前线程的数据权限上下文
     * </p>
     *
     * <h4>使用场景：</h4>
     * <p>
     * MyBatis拦截器通过此方法获取当前请求的数据权限上下文，
     * 用于在SQL执行前注入数据权限过滤条件。
     * </p>
     *
     * @return 数据权限上下文对象，如果不存在则返回null
     */
    public static DataScopeContext getDataScopeContext() {
        // 从ThreadLocal获取当前线程的数据权限上下文
        return DATA_SCOPE_CONTEXT.get();
    }

    /**
     * <p>
     * 清理当前线程的数据权限上下文
     * </p>
     *
     * <h4>使用场景：</h4>
     * <p>
     * 在特殊情况下手动清理数据权限上下文，通常不需要手动调用，
     * 切面会自动处理资源清理。
     * </p>
     */
    public static void clearDataScopeContext() {
        // 手动清理ThreadLocal资源
        DATA_SCOPE_CONTEXT.remove();
    }

    /**
     * <p>
     * 构建数据权限SQL片段
     * </p>
     *
     * <h4>SQL片段生成规则：</h4>
     * <ul>
     *   <li><b>ALL</b>: 返回空字符串，不添加任何过滤条件</li>
     *   <li><b>DEPT_AND_CHILD</b>: 生成部门及子部门过滤SQL</li>
     *   <li><b>DEPT_ONLY</b>: 生成本部门过滤SQL</li>
     *   <li><b>SELF_ONLY</b>: 生成仅本人过滤SQL</li>
     *   <li><b>CUSTOM</b>: 生成自定义角色部门过滤SQL</li>
     *   <li><b>AUTO</b>: 自动判断，默认添加1=1条件</li>
     * </ul>
     *
     * <h4>参数占位符说明：</h4>
     * <p>
     * SQL片段中使用的参数占位符（如#{currentDeptId}）需要由MyBatis拦截器
     * 在执行前注入实际的用户权限值。
     * </p>
     *
     * @param dataScope 数据权限注解配置
     * @return 数据权限SQL片段字符串
     */
    private String buildDataScopeSql(DataScope dataScope) {
        // 创建SQL片段构建器
        StringBuilder sql = new StringBuilder();

        // 从注解中提取配置参数
        String deptAlias = dataScope.deptAlias();
        String userAlias = dataScope.userAlias();
        String deptIdField = dataScope.deptIdField();
        String userIdField = dataScope.userIdField();

        // 根据数据权限范围类型生成不同的SQL片段
        switch (dataScope.scopeType()) {
            case ALL:
                // 全部数据权限：不添加任何过滤条件
                // 适用于超级管理员等需要查看所有数据的场景
                return "";

            case DEPT_AND_CHILD:
                // 本部门及子部门数据权限
                // SQL逻辑：查询部门ID等于当前用户部门ID，或者当前用户部门ID在其祖先路径中
                // FIND_IN_SET函数用于查找当前部门ID是否在ancestors字段中
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" IN (SELECT dept_id FROM sys_dept WHERE dept_id = #{currentDeptId} OR FIND_IN_SET(#{currentDeptId}, ancestors))");
                break;

            case DEPT_ONLY:
                // 本部门数据权限
                // SQL逻辑：只查询部门ID等于当前用户部门ID的数据
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" = #{currentDeptId}");
                break;

            case SELF_ONLY:
                // 仅本人数据权限
                // SQL逻辑：只查询用户ID等于当前用户ID的数据
                sql.append(" OR ").append(userAlias).append(".").append(userIdField)
                   .append(" = #{currentUserId}");
                break;

            case CUSTOM:
                // 自定义数据权限
                // SQL逻辑：根据用户角色关联的部门进行过滤
                // 适用于复杂的权限配置场景
                sql.append(" OR ").append(deptAlias).append(".").append(deptIdField)
                   .append(" IN (SELECT dept_id FROM sys_role_dept WHERE role_id = #{currentRoleId})");
                break;

            case AUTO:
            default:
                // 自动判断：默认添加1=1条件
                // 实际权限由MyBatis拦截器根据用户权限动态决定
                sql.append(" OR 1=1 ");
                break;
        }

        // 处理SQL片段格式：移除开头的" OR "，并用括号包围
        // 这样可以将条件作为一个整体与其他条件组合
        if (sql.length() > 0 && sql.charAt(0) == ' ' && sql.charAt(1) == 'O' && sql.charAt(2) == 'R') {
            // 删除开头的" OR "（4个字符）
            sql.delete(0, 4);
            // 在开头插入左括号
            sql.insert(0, "(");
            // 在末尾添加右括号
            sql.append(")");
        }

        return sql.toString();
    }

    /**
     * <p>
     * 数据权限上下文 - 存储数据权限过滤所需的所有信息
     * </p>
     *
     * <h4>主要字段：</h4>
     * <ul>
     *   <li><b>deptAlias</b>: 部门表在SQL中的别名</li>
     *   <li><b>userAlias</b>: 用户表在SQL中的别名</li>
     *   <li><b>deptIdField</b>: 部门ID字段名</li>
     *   <li><b>userIdField</b>: 用户ID字段名</li>
     *   <li><b>scopeType</b>: 数据权限范围类型</li>
     *   <li><b>sqlSegment</b>: 生成的SQL片段</li>
     *   <li><b>params</b>: 额外的参数存储</li>
     * </ul>
     *
     * <h4>使用方式：</h4>
     * <p>
     * MyBatis拦截器通过getDataScopeContext()获取此上下文对象，
     * 然后读取sqlSegment字段获取权限过滤SQL片段，
     * 并将params中的参数注入到SQL执行上下文中。
     * </p>
     */
    public static class DataScopeContext {
        /** 部门表在SQL中的别名 */
        private String deptAlias;

        /** 用户表在SQL中的别名 */
        private String userAlias;

        /** 部门ID字段名 */
        private String deptIdField;

        /** 用户ID字段名 */
        private String userIdField;

        /** 数据权限范围类型 */
        private DataScope.DataScopeType scopeType;

        /** 生成的SQL片段 */
        private String sqlSegment;

        /**
         * 额外的参数存储
         * <p>
         * 使用ConcurrentHashMap确保多线程环境下的安全性。
         * MyBatis拦截器可以从这里获取需要注入的参数值。
         * </p>
         */
        private Map<String, Object> params = new ConcurrentHashMap<>();

        /**
         * 获取部门表在SQL中的别名
         *
         * @return 部门表别名
         */
        public String getDeptAlias() {
            return deptAlias;
        }

        /**
         * 设置部门表在SQL中的别名
         *
         * @param deptAlias 部门表别名
         */
        public void setDeptAlias(String deptAlias) {
            this.deptAlias = deptAlias;
        }

        /**
         * 获取用户表在SQL中的别名
         *
         * @return 用户表别名
         */
        public String getUserAlias() {
            return userAlias;
        }

        /**
         * 设置用户表在SQL中的别名
         *
         * @param userAlias 用户表别名
         */
        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
        }

        /**
         * 获取部门ID字段名
         *
         * @return 部门ID字段名
         */
        public String getDeptIdField() {
            return deptIdField;
        }

        /**
         * 设置部门ID字段名
         *
         * @param deptIdField 部门ID字段名
         */
        public void setDeptIdField(String deptIdField) {
            this.deptIdField = deptIdField;
        }

        /**
         * 获取用户ID字段名
         *
         * @return 用户ID字段名
         */
        public String getUserIdField() {
            return userIdField;
        }

        /**
         * 设置用户ID字段名
         *
         * @param userIdField 用户ID字段名
         */
        public void setUserIdField(String userIdField) {
            this.userIdField = userIdField;
        }

        /**
         * 获取数据权限范围类型
         *
         * @return 数据权限范围类型
         */
        public DataScope.DataScopeType getScopeType() {
            return scopeType;
        }

        /**
         * 设置数据权限范围类型
         *
         * @param scopeType 数据权限范围类型
         */
        public void setScopeType(DataScope.DataScopeType scopeType) {
            this.scopeType = scopeType;
        }

        /**
         * 获取生成的SQL片段
         * <p>
         * 该SQL片段用于数据权限过滤，会被MyBatis拦截器注入到原始SQL中。
         * </p>
         *
         * @return SQL片段
         */
        public String getSqlSegment() {
            return sqlSegment;
        }

        /**
         * 设置生成的SQL片段
         *
         * @param sqlSegment SQL片段
         */
        public void setSqlSegment(String sqlSegment) {
            this.sqlSegment = sqlSegment;
        }

        /**
         * 获取额外的参数存储
         * <p>
         * MyBatis拦截器可以从这里获取需要注入到SQL执行上下文中的参数值。
         * </p>
         *
         * @return 参数字典
         */
        public Map<String, Object> getParams() {
            return params;
        }

        /**
         * 设置额外的参数存储
         *
         * @param params 参数字典
         */
        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        /**
         * 添加额外参数
         *
         * @param key 参数名
         * @param value 参数值
         */
        public void addParam(String key, Object value) {
            this.params.put(key, value);
        }

        /**
         * 获取额外参数
         *
         * @param key 参数名
         * @return 参数值
         */
        public Object getParam(String key) {
            return this.params.get(key);
        }
    }
}
