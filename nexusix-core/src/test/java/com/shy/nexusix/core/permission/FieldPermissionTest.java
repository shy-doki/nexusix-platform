package com.shy.nexusix.core.permission;

import com.shy.nexusix.common.enums.SecurityLevel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段权限功能测试类 - 直接运行 main 方法即可执行测试
 * </p>
 * <p>
 * 测试覆盖：
 * 1. 超级管理员：可见所有字段
 * 2. 管理员：可见 PUBLIC + INTERNAL 字段
 * 3. 普通用户：仅可见 PUBLIC 字段
 * 4. 默认规则：isDeleted 等敏感字段自动标记为 CONFIDENTIAL
 * 5. 例外配置：手动覆盖字段安全级别
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
public class FieldPermissionTest {

    /**
     * 测试运行入口
     */
    public static void main(String[] args) throws Exception {
        printHeader();

        // 创建测试实例
        TestFieldPermissionConfig config = new TestFieldPermissionConfig();

        // 加载测试实体（模拟 SysTenant）
        Class<?> testEntity = TestSysTenant.class;

        // 测试1：扫描字段安全级别
        testScanFieldLevels(config, testEntity);

        // 测试2：超级管理员可见字段
        testSuperAdmin(config, testEntity);

        // 测试3：管理员可见字段
        testAdmin(config, testEntity);

        // 测试4：普通用户可见字段
        testNormalUser(config, testEntity);

        // 测试5：例外配置
        testExceptionConfig(config, testEntity);

        // 测试6：默认规则推导
        testDefaultRules(config, testEntity);

        printFooter();
    }

    /**
     * 测试1：扫描字段安全级别
     */
    private static void testScanFieldLevels(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试1：扫描实体字段安全级别");

        Map<String, SecurityLevel> levels = config.getFieldSecurityLevels(entity);

        System.out.println("字段安全级别扫描结果：");
        System.out.println("+-------------------------+----------------+");
        System.out.println("| 字段名                   | 安全级别        |");
        System.out.println("+-------------------------+----------------+");

        for (Map.Entry<String, SecurityLevel> entry : levels.entrySet()) {
            System.out.printf("| %-25s| %-15s|\n", entry.getKey(), entry.getValue().name());
        }
        System.out.println("+-------------------------+----------------+");
        System.out.println("共扫描 " + levels.size() + " 个字段\n");
    }

    /**
     * 测试2：超级管理员可见字段
     */
    private static void testSuperAdmin(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试2：超级管理员可见字段（应返回null，表示不限制）");

        config.setUserRole("超级管理员");
        List<String> fields = config.getVisibleFields(entity);

        System.out.println("用户角色：超级管理员");
        System.out.println("安全级别：CONFIDENTIAL");
        System.out.println("可见字段数：" + (fields == null ? "无限制（所有字段）" : fields.size()));
        System.out.println("测试结果：" + (fields == null ? "✅ 通过" : "❌ 失败（应返回null）"));
        System.out.println();
    }

    /**
     * 测试3：管理员可见字段
     */
    private static void testAdmin(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试3：管理员可见字段（应包含 PUBLIC + INTERNAL 字段）");

        config.setUserRole("管理员");
        List<String> fields = config.getVisibleFields(entity);

        System.out.println("用户角色：管理员");
        System.out.println("安全级别：INTERNAL");
        System.out.println("可见字段数：" + (fields != null ? fields.size() : "无限制"));
        System.out.println("可见字段列表：");

        if (fields != null) {
            for (String field : fields) {
                System.out.println("  - " + field);
            }
        }

        boolean isDeletedVisible = fields != null && fields.contains("isDeleted");
        System.out.println("isDeleted 可见：" + isDeletedVisible + "（期望：false）");
        System.out.println("测试结果：" + (!isDeletedVisible ? "✅ 通过" : "❌ 失败（管理员不可见 isDeleted）"));
        System.out.println();
    }

    /**
     * 测试4：普通用户可见字段
     */
    private static void testNormalUser(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试4：普通用户可见字段（仅 PUBLIC 字段）");

        config.setUserRole("普通用户");
        List<String> fields = config.getVisibleFields(entity);

        System.out.println("用户角色：普通用户");
        System.out.println("安全级别：PUBLIC");
        System.out.println("可见字段数：" + (fields != null ? fields.size() : "无限制"));
        System.out.println("可见字段列表：");

        if (fields != null) {
            for (String field : fields) {
                System.out.println("  - " + field);
            }
        }

        boolean tenantCodeVisible = fields != null && fields.contains("tenantCode");
        boolean isDeletedVisible = fields != null && fields.contains("isDeleted");

        System.out.println("tenantCode 可见：" + tenantCodeVisible + "（期望：false）");
        System.out.println("isDeleted 可见：" + isDeletedVisible + "（期望：false）");
        System.out.println("测试结果：" + (!tenantCodeVisible && !isDeletedVisible ? "✅ 通过" : "❌ 失败"));
        System.out.println();
    }

    /**
     * 测试5：例外配置
     */
    private static void testExceptionConfig(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试5：例外配置（手动覆盖字段安全级别）");

        // 使用 TestSysTenantNoAnnotation 实体（无注解，全部走默认规则）
        config.setUserRole("普通用户");

        // 注册例外：让普通用户可见 contactPhone（默认PUBLIC）
        config.registerException(TestSysTenantNoAnnotation.class, "contactPhone", SecurityLevel.CONFIDENTIAL);
        config.invalidateCache(TestSysTenantNoAnnotation.class);

        List<String> fields = config.getVisibleFields(TestSysTenantNoAnnotation.class);

        System.out.println("实体：TestSysTenantNoAnnotation（无注解）");
        System.out.println("用户角色：普通用户（PUBLIC）");
        System.out.println("例外配置：将 contactPhone 的安全级别从 PUBLIC 提升为 CONFIDENTIAL");
        System.out.println("contactPhone 可见：" + (fields != null && fields.contains("contactPhone")));
        System.out.println("测试结果：" + (fields != null && !fields.contains("contactPhone") ? "✅ 通过" : "❌ 失败"));
        System.out.println();
    }

    /**
     * 测试6：默认规则推导
     */
    private static void testDefaultRules(TestFieldPermissionConfig config, Class<?> entity) {
        printTestHeader("测试6：默认规则推导（无注解字段的自动安全级别）");

        Map<String, SecurityLevel> levels = config.getFieldSecurityLevels(entity);

        String[] defaultFields = {"isDeleted"};

        boolean allPassed = true;
        for (String fieldName : defaultFields) {
            SecurityLevel level = levels.get(fieldName);
            boolean isConfidential = level == SecurityLevel.CONFIDENTIAL;
            System.out.println(fieldName + " 安全级别：" + level + "（期望：CONFIDENTIAL）");
            if (!isConfidential) {
                allPassed = false;
            }
        }

        System.out.println("测试结果：" + (allPassed ? "✅ 通过" : "❌ 失败"));
        System.out.println();
    }

    /**
     * 打印测试头
     */
    private static void printHeader() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          字段权限功能测试 (Field Permission Test)        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("测试实体类：TestSysTenant（模拟 SysTenant）");
        System.out.println("测试时间：" + LocalDateTime.now());
        System.out.println("\n");
    }

    /**
     * 打印测试尾
     */
    private static void printFooter() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                     测试完成                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }

    /**
     * 打印测试用例头
     */
    private static void printTestHeader(String title) {
        System.out.println("─────────────────────────────────────────────────────────");
        System.out.println("▶ " + title);
        System.out.println("─────────────────────────────────────────────────────────");
    }

    /**
     * 测试用字段权限配置（独立于 Spring）
     */
    static class TestFieldPermissionConfig {

        private String currentRole;
        private final Map<String, Map<String, SecurityLevel>> fieldSecurityCache = new ConcurrentHashMap<>();
        private final Map<String, Map<String, SecurityLevel>> exceptionConfig = new ConcurrentHashMap<>();

        public void setUserRole(String role) {
            this.currentRole = role;
        }

        public List<String> getVisibleFields(Class<?> entityClass) {
            SecurityLevel userLevel = getUserSecurityLevel();

            if (userLevel == SecurityLevel.CONFIDENTIAL) {
                return null;
            }

            Map<String, SecurityLevel> fieldLevels = getFieldSecurityLevels(entityClass);

            return fieldLevels.entrySet().stream()
                    .filter(entry -> entry.getValue().ordinal() <= userLevel.ordinal())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        public Map<String, SecurityLevel> getFieldSecurityLevels(Class<?> entityClass) {
            return fieldSecurityCache.computeIfAbsent(
                    entityClass.getName(),
                    name -> scanFieldSecurityLevels(entityClass)
            );
        }

        public void registerException(Class<?> entityClass, String fieldName, SecurityLevel level) {
            exceptionConfig.computeIfAbsent(entityClass.getName(), k -> new HashMap<>())
                    .put(fieldName, level);
            fieldSecurityCache.remove(entityClass.getName());
        }

        public void invalidateCache(Class<?> entityClass) {
            fieldSecurityCache.remove(entityClass.getName());
        }

        private Map<String, SecurityLevel> scanFieldSecurityLevels(Class<?> entityClass) {
            Map<String, SecurityLevel> levels = new LinkedHashMap<>();
            Class<?> currentClass = entityClass;

            while (currentClass != null && currentClass != Object.class) {
                for (Field field : currentClass.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    String fieldName = field.getName();

                    com.shy.nexusix.common.annotation.FieldSecurityLevel annotation =
                            field.getAnnotation(com.shy.nexusix.common.annotation.FieldSecurityLevel.class);
                    if (annotation != null) {
                        levels.put(fieldName, annotation.value());
                        continue;
                    }

                    Map<String, SecurityLevel> entityExceptions = exceptionConfig.get(entityClass.getName());
                    if (entityExceptions != null && entityExceptions.containsKey(fieldName)) {
                        levels.put(fieldName, entityExceptions.get(fieldName));
                        continue;
                    }

                    SecurityLevel inferred = inferSecurityLevel(fieldName);
                    levels.put(fieldName, inferred);
                }

                currentClass = currentClass.getSuperclass();
            }

            return levels;
        }

        private SecurityLevel inferSecurityLevel(String fieldName) {
            String name = fieldName.toLowerCase();

            if (name.contains("deleted") || name.contains("password") || name.contains("pwd")
                    || name.contains("salt") || name.contains("secret") || name.contains("token")
                    || name.contains("credential")) {
                return SecurityLevel.CONFIDENTIAL;
            }

            return SecurityLevel.PUBLIC;
        }

        private SecurityLevel getUserSecurityLevel() {
            if (currentRole == null) {
                return SecurityLevel.PUBLIC;
            }

            if ("超级管理员".equals(currentRole)) {
                return SecurityLevel.CONFIDENTIAL;
            }

            if ("管理员".equals(currentRole)) {
                return SecurityLevel.INTERNAL;
            }

            return SecurityLevel.PUBLIC;
        }
    }

    /**
     * 测试实体（模拟 SysTenant）
     */
    static class TestSysTenant {

        @com.shy.nexusix.common.annotation.FieldSecurityLevel(SecurityLevel.PUBLIC)
        private String id;

        @com.shy.nexusix.common.annotation.FieldSecurityLevel(SecurityLevel.PUBLIC)
        private String tenantName;

        @com.shy.nexusix.common.annotation.FieldSecurityLevel(SecurityLevel.INTERNAL)
        private String tenantCode;

        @com.shy.nexusix.common.annotation.FieldSecurityLevel(SecurityLevel.PUBLIC)
        private String status;

        private String isDeleted;

        private String createTime;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        public String getTenantCode() { return tenantCode; }
        public void setTenantCode(String tenantCode) { this.tenantCode = tenantCode; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getIsDeleted() { return isDeleted; }
        public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
    }

    /**
     * 测试实体（无注解，全部走默认规则）
     */
    static class TestSysTenantNoAnnotation {

        private String id;

        private String tenantName;

        private String tenantCode;

        private String contactPhone;

        private String status;

        private String isDeleted;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTenantName() { return tenantName; }
        public void setTenantName(String tenantName) { this.tenantName = tenantName; }
        public String getTenantCode() { return tenantCode; }
        public void setTenantCode(String tenantCode) { this.tenantCode = tenantCode; }
        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getIsDeleted() { return isDeleted; }
        public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
    }

}
