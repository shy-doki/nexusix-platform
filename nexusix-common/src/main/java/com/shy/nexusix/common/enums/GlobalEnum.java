package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>全局统一枚举类，整合所有业务相关的枚举类型</p>
 * <p>所有内部枚举实现BaseEnum接口，使用@EnumValue标记数据库存储字段，@JSONField支持JSON序列化</p>
 *
 * @author shy
 */
public final class GlobalEnum {

    private GlobalEnum() {
    }

    /**
     * 删除标记枚举
     */
    public enum Deleted implements BaseEnum {
        // 未删除
        NOT_DELETED("NOT_DELETED", "未删除"),
        // 已删除
        DELETED("DELETED", "已删除");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        Deleted(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static Deleted getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("删除标记编码不能为空");
            }
            String trimmedCode = code.trim();
            for (Deleted e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的删除标记编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static Deleted getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("删除标记描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (Deleted e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的删除标记描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static Deleted getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("删除标记枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的删除标记枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static Deleted parse(Object value) {
            if (value == null) {
                throw new BusinessException("删除标记值不能为空");
            }
            if (value instanceof Deleted) {
                return (Deleted) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("删除标记值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 权限状态枚举
     */
    public enum PermStatus implements BaseEnum {
        // 启用
        ENABLED("ENABLED", "启用"),
        // 禁用
        DISABLED("DISABLED", "禁用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static PermStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static PermStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static PermStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static PermStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限状态值不能为空");
            }
            if (value instanceof PermStatus) {
                return (PermStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 权限策略状态枚举
     */
    public enum PermPolicyStatus implements BaseEnum {
        // 生效
        ACTIVE("ACTIVE", "生效"),
        // 禁用
        DISABLED("DISABLED", "禁用"),
        // 系统级禁用
        DISABLED_SYSTEM_LEVEL("DISABLED_SYSTEM_LEVEL", "系统级禁用"),
        // 租户级禁用
        DISABLED_TENANT_LEVEL("DISABLED_TENANT_LEVEL", "租户级禁用"),
        // 角色级禁用
        DISABLED_ROLE_LEVEL("DISABLED_ROLE_LEVEL", "角色级禁用"),
        // 用户级禁用
        DISABLED_USER_LEVEL("DISABLED_USER_LEVEL", "用户级禁用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static PermPolicyStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限策略状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static PermPolicyStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限策略状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static PermPolicyStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限策略状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限策略状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static PermPolicyStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限策略状态值不能为空");
            }
            if (value instanceof PermPolicyStatus) {
                return (PermPolicyStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限策略状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 权限策略目标类型枚举
     */
    public enum PermPolicyTargetType implements BaseEnum {
        // 租户
        TENANT("TENANT", "租户"),
        // 角色
        ROLE("ROLE", "角色"),
        // 用户
        USER("USER", "用户");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyTargetType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static PermPolicyTargetType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyTargetType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略目标类型编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static PermPolicyTargetType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyTargetType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限策略目标类型描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static PermPolicyTargetType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限策略目标类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限策略目标类型枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static PermPolicyTargetType parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限策略目标类型值不能为空");
            }
            if (value instanceof PermPolicyTargetType) {
                return (PermPolicyTargetType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限策略目标类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 权限策略字段访问类型枚举
     */
    public enum PermPolicyAccessType implements BaseEnum {
        // 查询
        QUERY("QUERY", "查询"),
        // 新增
        CREATE("CREATE", "新增"),
        // 更新
        UPDATE("UPDATE", "更新");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermPolicyAccessType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static PermPolicyAccessType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("字段访问类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermPolicyAccessType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字段访问类型编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static PermPolicyAccessType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("字段访问类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermPolicyAccessType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字段访问类型描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static PermPolicyAccessType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("字段访问类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的字段访问类型枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static PermPolicyAccessType parse(Object value) {
            if (value == null) {
                throw new BusinessException("字段访问类型值不能为空");
            }
            if (value instanceof PermPolicyAccessType) {
                return (PermPolicyAccessType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("字段访问类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 租户状态枚举
     */
    public enum TenantStatus implements BaseEnum {
        // 待审核
        PENDING("PENDING", "待审核"),
        // 启用
        ENABLED("ENABLED", "启用"),
        // 停用
        DISABLED("DISABLED", "停用"),
        // 过期
        EXPIRED("EXPIRED", "过期");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        TenantStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static TenantStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("租户状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (TenantStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的租户状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static TenantStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("租户状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (TenantStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的租户状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static TenantStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("租户状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的租户状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static TenantStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("租户状态值不能为空");
            }
            if (value instanceof TenantStatus) {
                return (TenantStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("租户状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }

        /**
         * 校验状态转换是否合法
         * <p>PENDING→ENABLED/DISABLED, ENABLED→DISABLED/EXPIRED, DISABLED→ENABLED, EXPIRED→ENABLED</p>
         *
         * @param currentStatus 当前状态编码
         * @param targetStatus  目标状态编码
         * @return 是否为合法的状态转换
         */
        public static boolean isValidTransition(String currentStatus, String targetStatus) {
            if (currentStatus == null || targetStatus == null) return false;
            if (currentStatus.equals(targetStatus)) return false;
            if (PENDING.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus) || DISABLED.getCode().equals(targetStatus);
            }
            if (ENABLED.getCode().equals(currentStatus)) {
                return DISABLED.getCode().equals(targetStatus) || EXPIRED.getCode().equals(targetStatus);
            }
            if (DISABLED.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus);
            }
            if (EXPIRED.getCode().equals(currentStatus)) {
                return ENABLED.getCode().equals(targetStatus);
            }
            return false;
        }
    }

    /**
     * 订阅状态枚举
     */
    public enum SubscriptionStatus implements BaseEnum {
        // 生效中
        ACTIVE("ACTIVE", "生效中"),
        // 已过期
        EXPIRED("EXPIRED", "已过期"),
        // 已取消
        CANCELLED("CANCELLED", "已取消"),
        // 待生效
        PENDING("PENDING", "待生效");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        SubscriptionStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static SubscriptionStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("订阅状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (SubscriptionStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static SubscriptionStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("订阅状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (SubscriptionStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static SubscriptionStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("订阅状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的订阅状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static SubscriptionStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("订阅状态值不能为空");
            }
            if (value instanceof SubscriptionStatus) {
                return (SubscriptionStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("订阅状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 订阅类型枚举
     */
    public enum SubscriptionType implements BaseEnum {
        // 新订阅
        NEW("NEW", "新订阅"),
        // 续费
        RENEWAL("RENEWAL", "续费"),
        // 升级
        UPGRADE("UPGRADE", "升级"),
        // 降级
        DOWNGRADE("DOWNGRADE", "降级");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        SubscriptionType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static SubscriptionType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("订阅类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (SubscriptionType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅类型编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static SubscriptionType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("订阅类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (SubscriptionType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订阅类型描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static SubscriptionType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("订阅类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的订阅类型枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static SubscriptionType parse(Object value) {
            if (value == null) {
                throw new BusinessException("订阅类型值不能为空");
            }
            if (value instanceof SubscriptionType) {
                return (SubscriptionType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("订阅类型值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 用户默认租户枚举
     */
    public enum DefaultTenant implements BaseEnum {
        // 默认
        DEFAULT(true, "默认"),
        // 次默认
        SECONDARY_DEFAULT(false, "次默认");

        @EnumValue
        @JSONField(value = true)
        private final Boolean code;
        private final String desc;

        DefaultTenant(Boolean code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code != null ? code.toString() : null;
        }

        /**
         * 获取Boolean类型的编码
         *
         * @return Boolean编码
         */
        public Boolean getBooleanCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据Boolean编码获取枚举
         *
         * @param code Boolean编码
         * @return 枚举实例
         */
        public static DefaultTenant getByCode(Boolean code) {
            if (code == null) {
                throw new BusinessException("用户默认租户编码不能为空");
            }
            for (DefaultTenant e : values()) {
                if (e.getBooleanCode().equals(code)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户默认租户编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static DefaultTenant getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("用户默认租户描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DefaultTenant e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户默认租户描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static DefaultTenant getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("用户默认租户枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的用户默认租户枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按名称、描述、Boolean编码转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static DefaultTenant parse(Object value) {
            if (value == null) {
                throw new BusinessException("用户默认租户值不能为空");
            }
            if (value instanceof DefaultTenant) {
                return (DefaultTenant) value;
            }
            if (value instanceof Boolean) {
                return getByCode((Boolean) value);
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("用户默认租户值不能为空字符串");
            }
            try {
                return getByName(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    Boolean boolValue = Boolean.parseBoolean(strValue);
                    return getByCode(boolValue);
                }
            }
        }

        /**
         * 判断Boolean编码是否有效
         *
         * @param code Boolean编码
         * @return 是否有效
         */
        public static boolean isValidCode(Boolean code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 用户状态枚举
     */
    public enum UserStatus implements BaseEnum {
        // 启用
        ENABLED("ENABLED", "启用"),
        // 禁用
        DISABLED("DISABLED", "禁用"),
        // 锁定
        LOCKED("LOCKED", "锁定");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        UserStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static UserStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("用户状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (UserStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static UserStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("用户状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (UserStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static UserStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("用户状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的用户状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static UserStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("用户状态值不能为空");
            }
            if (value instanceof UserStatus) {
                return (UserStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("用户状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

    /**
     * 角色状态枚举
     */
    public enum RoleStatus implements BaseEnum {
        // 启用
        ENABLED("ENABLED", "启用"),
        // 禁用
        DISABLED("DISABLED", "禁用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        RoleStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 编码
         * @return 枚举实例
         */
        public static RoleStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("角色状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (RoleStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的角色状态编码");
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 描述
         * @return 枚举实例
         */
        public static RoleStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("角色状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (RoleStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的角色状态描述");
        }

        /**
         * 根据枚举名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举实例
         */
        public static RoleStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("角色状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的角色状态枚举名称");
            }
        }

        /**
         * 通用解析，依次尝试按编码、描述、名称转换
         *
         * @param value 值
         * @return 枚举实例
         */
        public static RoleStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("角色状态值不能为空");
            }
            if (value instanceof RoleStatus) {
                return (RoleStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("角色状态值不能为空字符串");
            }
            try {
                return getByCode(strValue);
            } catch (BusinessException e) {
                try {
                    return getByDesc(strValue);
                } catch (BusinessException ex) {
                    return getByName(strValue);
                }
            }
        }

        /**
         * 判断编码是否有效
         *
         * @param code 编码
         * @return 是否有效
         */
        public static boolean isValidCode(String code) {
            try {
                getByCode(code);
                return true;
            } catch (BusinessException e) {
                return false;
            }
        }
    }

}
