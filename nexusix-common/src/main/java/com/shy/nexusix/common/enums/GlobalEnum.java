package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>
 * 全局统一枚举类
 * </p>
 * <p>
 * 该类整合了系统中所有业务相关的枚举类型，采用内部枚举类的方式进行组织和管理。
 * 每个内部枚举类代表一种特定类型的枚举，确保枚举值的统一编码和规范管理。
 * </p>
 * <p>
 * <b>设计说明：</b>
 * <ul>
 *   <li>所有内部枚举类都实现 BaseEnum 接口，保证统一的规范</li>
 *   <li>使用 @EnumValue 注解标记存储到数据库的字段（code字段）</li>
 *   <li>使用 @JSONField(value = true) 注解支持 JSON 序列化</li>
 *   <li>每个内部枚举类都提供 getByCode() 和 isValidCode() 方法</li>
 * </ul>
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public final class GlobalEnum {

    private GlobalEnum() {
    }

    /**
     * <p>
     * 删除标记枚举
     * </p>
     * <p>
     * 用于表示数据的逻辑删除状态。
     * 数据库存储值为 String 类型："ACTIVE" (未删除) 或 "DELETED" (已删除)。
     * </p>
     */
    public enum Deleted implements BaseEnum {
        NOT_DELETED("ACTIVE", "未删除"),
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
     * <p>
     * 租户状态枚举
     * </p>
     * <p>
     * 用于表示租户的服务状态。
     * 数据库字段: sys_tenant.status (VARCHAR)
     * </p>
     */
    public enum TenantStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用"),
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
     * <p>
     * 用户状态枚举
     * </p>
     * <p>
     * 用于表示用户的全局状态。
     * 数据库字段: sys_user.status (VARCHAR)
     * </p>
     */
    public enum UserStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

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
     * <p>
     * 订阅状态枚举
     * </p>
     * <p>
     * 用于表示租户套餐订阅的状态。
     * 数据库字段: sys_tenant_subscription.status (VARCHAR)
     * </p>
     */
    public enum SubscriptionStatus implements BaseEnum {
        ACTIVE("ACTIVE", "生效中"),
        EXPIRED("EXPIRED", "已过期"),
        CANCELLED("CANCELLED", "已取消"),
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
     * <p>
     * 订阅类型枚举
     * </p>
     * <p>
     * 用于表示订阅的来源类型。
     * 数据库字段: sys_tenant_subscription.subscription_type, source_type (VARCHAR)
     * </p>
     */
    public enum SubscriptionType implements BaseEnum {
        NEW("NEW", "新订阅"),
        RENEWAL("RENEWAL", "续费"),
        UPGRADE("UPGRADE", "升级"),
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
     * <p>
     * 套餐状态枚举
     * </p>
     * <p>
     * 用于表示产品套餐的状态。
     * 数据库字段: prod_package.status (VARCHAR)
     * </p>
     */
    public enum PackageStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PackageStatus(String code, String desc) {
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

        public static PackageStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("套餐状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PackageStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的套餐状态编码");
        }

        public static PackageStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("套餐状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PackageStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的套餐状态描述");
        }

        public static PackageStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("套餐状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的套餐状态枚举名称");
            }
        }

        public static PackageStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("套餐状态值不能为空");
            }
            if (value instanceof PackageStatus) {
                return (PackageStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("套餐状态值不能为空字符串");
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
     * <p>
     * 套餐周期类型枚举
     * </p>
     * <p>
     * 用于表示套餐的计费周期类型。
     * 数据库字段: prod_package.cycle_type (VARCHAR)
     * </p>
     */
    public enum PackageCycleType implements BaseEnum {
        DAY("DAY", "按天"),
        WEEK("WEEK", "按周"),
        MONTH("MONTH", "按月"),
        YEAR("YEAR", "按年");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PackageCycleType(String code, String desc) {
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

        public static PackageCycleType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("套餐周期类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PackageCycleType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的套餐周期类型编码");
        }

        public static PackageCycleType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("套餐周期类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PackageCycleType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的套餐周期类型描述");
        }

        public static PackageCycleType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("套餐周期类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的套餐周期类型枚举名称");
            }
        }

        public static PackageCycleType parse(Object value) {
            if (value == null) {
                throw new BusinessException("套餐周期类型值不能为空");
            }
            if (value instanceof PackageCycleType) {
                return (PackageCycleType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("套餐周期类型值不能为空字符串");
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
     * <p>
     * 配额调整类型枚举
     * </p>
     * <p>
     * 用于表示租户配额调整的类型。
     * 数据库字段: sys_tenant_quota_adjustment.adjust_type (VARCHAR)
     * </p>
     */
    public enum AdjustType implements BaseEnum {
        INCREASE("INCREASE", "增加"),
        DECREASE("DECREASE", "减少");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        AdjustType(String code, String desc) {
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

        public static AdjustType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("调整类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (AdjustType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的调整类型编码");
        }

        public static AdjustType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("调整类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (AdjustType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的调整类型描述");
        }

        public static AdjustType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("调整类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的调整类型枚举名称");
            }
        }

        public static AdjustType parse(Object value) {
            if (value == null) {
                throw new BusinessException("调整类型值不能为空");
            }
            if (value instanceof AdjustType) {
                return (AdjustType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("调整类型值不能为空字符串");
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
     * <p>
     * 订单状态枚举
     * </p>
     * <p>
     * 用于表示订单的生命周期状态。
     * 数据库字段: bill_order.status (VARCHAR)
     * </p>
     */
    public enum OrderStatus implements BaseEnum {
        PENDING("PENDING", "待支付"),
        PAID("PAID", "已支付"),
        SHIPPED("SHIPPED", "已发货"),
        COMPLETED("COMPLETED", "已完成"),
        CANCELLED("CANCELLED", "已取消"),
        REFUNDED("REFUNDED", "已退款");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        OrderStatus(String code, String desc) {
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

        public static OrderStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("订单状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (OrderStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订单状态编码");
        }

        public static OrderStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("订单状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (OrderStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的订单状态描述");
        }

        public static OrderStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("订单状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的订单状态枚举名称");
            }
        }

        public static OrderStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("订单状态值不能为空");
            }
            if (value instanceof OrderStatus) {
                return (OrderStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("订单状态值不能为空字符串");
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
     * <p>
     * 发票状态枚举
     * </p>
     * <p>
     * 用于表示发票的状态。
     * 数据库字段: bill_invoice.status (VARCHAR)
     * </p>
     */
    public enum InvoiceStatus implements BaseEnum {
        PENDING("PENDING", "待开具"),
        ISSUED("ISSUED", "已开具"),
        CANCELLED("CANCELLED", "已取消");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        InvoiceStatus(String code, String desc) {
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

        public static InvoiceStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("发票状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (InvoiceStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的发票状态编码");
        }

        public static InvoiceStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("发票状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (InvoiceStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的发票状态描述");
        }

        public static InvoiceStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("发票状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的发票状态枚举名称");
            }
        }

        public static InvoiceStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("发票状态值不能为空");
            }
            if (value instanceof InvoiceStatus) {
                return (InvoiceStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("发票状态值不能为空字符串");
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
     * <p>
     * 发票类型枚举
     * </p>
     * <p>
     * 用于表示发票的类型。
     * 数据库字段: bill_invoice.invoice_type (VARCHAR)
     * </p>
     */
    public enum InvoiceType implements BaseEnum {
        NORMAL("NORMAL", "普通发票"),
        SPECIAL("SPECIAL", "专用发票"),
        ELECTRONIC("ELECTRONIC", "电子发票");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        InvoiceType(String code, String desc) {
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

        public static InvoiceType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("发票类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (InvoiceType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的发票类型编码");
        }

        public static InvoiceType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("发票类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (InvoiceType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的发票类型描述");
        }

        public static InvoiceType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("发票类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的发票类型枚举名称");
            }
        }

        public static InvoiceType parse(Object value) {
            if (value == null) {
                throw new BusinessException("发票类型值不能为空");
            }
            if (value instanceof InvoiceType) {
                return (InvoiceType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("发票类型值不能为空字符串");
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
     * <p>
     * 角色状态枚举
     * </p>
     * <p>
     * 用于表示角色的状态。
     * 数据库字段: sys_role.status (VARCHAR)
     * </p>
     */
    public enum RoleStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

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
     * <p>
     * 数据范围枚举
     * </p>
     * <p>
     * 用于表示角色的数据权限范围。
     * 数据库字段: sys_role.data_scope (VARCHAR)
     * </p>
     */
    public enum DataScope implements BaseEnum {
        ALL("ALL", "全部数据"),
        DEPT("DEPT", "本部门数据"),
        DEPT_AND_CHILDREN("DEPT_AND_CHILDREN", "本部门及下级"),
        SELF("SELF", "仅本人数据"),
        CUSTOM("CUSTOM", "自定义");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        DataScope(String code, String desc) {
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

        public static DataScope getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("数据范围编码不能为空");
            }
            String trimmedCode = code.trim();
            for (DataScope e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据范围编码");
        }

        public static DataScope getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("数据范围描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DataScope e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据范围描述");
        }

        public static DataScope getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("数据范围枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的数据范围枚举名称");
            }
        }

        public static DataScope parse(Object value) {
            if (value == null) {
                throw new BusinessException("数据范围值不能为空");
            }
            if (value instanceof DataScope) {
                return (DataScope) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("数据范围值不能为空字符串");
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
     * <p>
     * 权限状态枚举
     * </p>
     * <p>
     * 用于表示权限/资源的状态。
     * 数据库字段: sys_permission.status (VARCHAR)
     * </p>
     */
    public enum PermissionStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermissionStatus(String code, String desc) {
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

        public static PermissionStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermissionStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态编码");
        }

        public static PermissionStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermissionStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限状态描述");
        }

        public static PermissionStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限状态枚举名称");
            }
        }

        public static PermissionStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限状态值不能为空");
            }
            if (value instanceof PermissionStatus) {
                return (PermissionStatus) value;
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
     * <p>
     * 权限类型枚举
     * </p>
     * <p>
     * 用于表示权限/资源的类型。
     * 数据库字段: sys_permission.perm_type (VARCHAR)
     * </p>
     */
    public enum PermType implements BaseEnum {
        MENU("MENU", "菜单"),
        BUTTON("BUTTON", "按钮"),
        API("API", "API接口");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PermType(String code, String desc) {
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

        public static PermType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("权限类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PermType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限类型编码");
        }

        public static PermType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("权限类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PermType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的权限类型描述");
        }

        public static PermType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("权限类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的权限类型枚举名称");
            }
        }

        public static PermType parse(Object value) {
            if (value == null) {
                throw new BusinessException("权限类型值不能为空");
            }
            if (value instanceof PermType) {
                return (PermType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("权限类型值不能为空字符串");
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
     * <p>
     * 策略动作枚举
     * </p>
     * <p>
     * 用于表示角色/权限策略的动作。
     * 数据库字段: sys_role_policy.action, sys_permission_policy.action (VARCHAR)
     * </p>
     */
    public enum PolicyAction implements BaseEnum {
        ALLOW("ALLOW", "允许"),
        DENY("DENY", "拒绝");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PolicyAction(String code, String desc) {
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

        public static PolicyAction getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("策略动作编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PolicyAction e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的策略动作编码");
        }

        public static PolicyAction getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("策略动作描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PolicyAction e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的策略动作描述");
        }

        public static PolicyAction getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("策略动作枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的策略动作枚举名称");
            }
        }

        public static PolicyAction parse(Object value) {
            if (value == null) {
                throw new BusinessException("策略动作值不能为空");
            }
            if (value instanceof PolicyAction) {
                return (PolicyAction) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("策略动作值不能为空字符串");
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
     * <p>
     * 部门状态枚举
     * </p>
     * <p>
     * 用于表示部门的状态。
     * 数据库字段: sys_dept.status (VARCHAR)
     * </p>
     */
    public enum DeptStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        DeptStatus(String code, String desc) {
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

        public static DeptStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("部门状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (DeptStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的部门状态编码");
        }

        public static DeptStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("部门状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DeptStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的部门状态描述");
        }

        public static DeptStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("部门状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的部门状态枚举名称");
            }
        }

        public static DeptStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("部门状态值不能为空");
            }
            if (value instanceof DeptStatus) {
                return (DeptStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("部门状态值不能为空字符串");
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
     * <p>
     * 岗位状态枚举
     * </p>
     * <p>
     * 用于表示岗位的状态。
     * 数据库字段: sys_post.status (VARCHAR)
     * </p>
     */
    public enum PostStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PostStatus(String code, String desc) {
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

        public static PostStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("岗位状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PostStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的岗位状态编码");
        }

        public static PostStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("岗位状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PostStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的岗位状态描述");
        }

        public static PostStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("岗位状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的岗位状态枚举名称");
            }
        }

        public static PostStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("岗位状态值不能为空");
            }
            if (value instanceof PostStatus) {
                return (PostStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("岗位状态值不能为空字符串");
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
     * <p>
     * 用户组状态枚举
     * </p>
     * <p>
     * 用于表示用户组的状态。
     * 数据库字段: sys_user_group.status (VARCHAR)
     * </p>
     */
    public enum GroupStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        GroupStatus(String code, String desc) {
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

        public static GroupStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("用户组状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (GroupStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户组状态编码");
        }

        public static GroupStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("用户组状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (GroupStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的用户组状态描述");
        }

        public static GroupStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("用户组状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的用户组状态枚举名称");
            }
        }

        public static GroupStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("用户组状态值不能为空");
            }
            if (value instanceof GroupStatus) {
                return (GroupStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("用户组状态值不能为空字符串");
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
     * <p>
     * 表单配置状态枚举
     * </p>
     * <p>
     * 用于表示动态表单配置的状态。
     * 数据库字段: sys_form_config.status (VARCHAR)
     * </p>
     */
    public enum FormConfigStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        FormConfigStatus(String code, String desc) {
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

        public static FormConfigStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("表单配置状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (FormConfigStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的表单配置状态编码");
        }

        public static FormConfigStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("表单配置状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (FormConfigStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的表单配置状态描述");
        }

        public static FormConfigStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("表单配置状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的表单配置状态枚举名称");
            }
        }

        public static FormConfigStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("表单配置状态值不能为空");
            }
            if (value instanceof FormConfigStatus) {
                return (FormConfigStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("表单配置状态值不能为空字符串");
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
     * <p>
     * 数据源状态枚举
     * </p>
     * <p>
     * 用于表示动态数据源配置的状态。
     * 数据库字段: sys_datasource_config.status (VARCHAR)
     * </p>
     */
    public enum DatasourceStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        DatasourceStatus(String code, String desc) {
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

        public static DatasourceStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("数据源状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (DatasourceStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据源状态编码");
        }

        public static DatasourceStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("数据源状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DatasourceStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据源状态描述");
        }

        public static DatasourceStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("数据源状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的数据源状态枚举名称");
            }
        }

        public static DatasourceStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("数据源状态值不能为空");
            }
            if (value instanceof DatasourceStatus) {
                return (DatasourceStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("数据源状态值不能为空字符串");
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
     * <p>
     * 数据源类型枚举
     * </p>
     * <p>
     * 用于表示动态数据源的类型。
     * 数据库字段: sys_datasource_config.datasource_type (VARCHAR)
     * </p>
     */
    public enum DatasourceType implements BaseEnum {
        BUSINESS_TABLE("BUSINESS_TABLE", "业务表"),
        API_INTERFACE("API_INTERFACE", "API接口"),
        DICT("DICT", "字典"),
        SQL_QUERY("SQL_QUERY", "SQL查询");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        DatasourceType(String code, String desc) {
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

        public static DatasourceType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("数据源类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (DatasourceType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据源类型编码");
        }

        public static DatasourceType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("数据源类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DatasourceType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的数据源类型描述");
        }

        public static DatasourceType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("数据源类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的数据源类型枚举名称");
            }
        }

        public static DatasourceType parse(Object value) {
            if (value == null) {
                throw new BusinessException("数据源类型值不能为空");
            }
            if (value instanceof DatasourceType) {
                return (DatasourceType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("数据源类型值不能为空字符串");
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
     * <p>
     * 打印模板状态枚举
     * </p>
     * <p>
     * 用于表示打印模板的状态。
     * 数据库字段: sys_print_template.status (VARCHAR)
     * </p>
     */
    public enum PrintTemplateStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PrintTemplateStatus(String code, String desc) {
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

        public static PrintTemplateStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("打印模板状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PrintTemplateStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的打印模板状态编码");
        }

        public static PrintTemplateStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("打印模板状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PrintTemplateStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的打印模板状态描述");
        }

        public static PrintTemplateStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("打印模板状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的打印模板状态枚举名称");
            }
        }

        public static PrintTemplateStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("打印模板状态值不能为空");
            }
            if (value instanceof PrintTemplateStatus) {
                return (PrintTemplateStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("打印模板状态值不能为空字符串");
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
     * <p>
     * 打印模板类型枚举
     * </p>
     * <p>
     * 用于表示打印模板的类型。
     * 数据库字段: sys_print_template.template_type (VARCHAR)
     * </p>
     */
    public enum PrintTemplateType implements BaseEnum {
        HTML("HTML", "HTML模板"),
        JSON_CONFIG("JSON_CONFIG", "JSON配置");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        PrintTemplateType(String code, String desc) {
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

        public static PrintTemplateType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("打印模板类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (PrintTemplateType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的打印模板类型编码");
        }

        public static PrintTemplateType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("打印模板类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (PrintTemplateType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的打印模板类型描述");
        }

        public static PrintTemplateType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("打印模板类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的打印模板类型枚举名称");
            }
        }

        public static PrintTemplateType parse(Object value) {
            if (value == null) {
                throw new BusinessException("打印模板类型值不能为空");
            }
            if (value instanceof PrintTemplateType) {
                return (PrintTemplateType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("打印模板类型值不能为空字符串");
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
     * <p>
     * 字典状态枚举
     * </p>
     * <p>
     * 用于表示字典类型的状态。
     * 数据库字段: sys_dict.status, sys_dict_item.status (VARCHAR)
     * </p>
     */
    public enum DictStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        DictStatus(String code, String desc) {
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

        public static DictStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("字典状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (DictStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字典状态编码");
        }

        public static DictStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("字典状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (DictStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的字典状态描述");
        }

        public static DictStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("字典状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的字典状态枚举名称");
            }
        }

        public static DictStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("字典状态值不能为空");
            }
            if (value instanceof DictStatus) {
                return (DictStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("字典状态值不能为空字符串");
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
     * <p>
     * Token状态枚举
     * </p>
     * <p>
     * 用于表示用户Token的状态。
     * 数据库字段: sys_user_token.status (VARCHAR)
     * </p>
     */
    public enum TokenStatus implements BaseEnum {
        VALID("VALID", "有效"),
        EXPIRED("EXPIRED", "已过期"),
        REVOKED("REVOKED", "已撤销");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        TokenStatus(String code, String desc) {
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

        public static TokenStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("Token状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (TokenStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的Token状态编码");
        }

        public static TokenStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("Token状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (TokenStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的Token状态描述");
        }

        public static TokenStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("Token状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的Token状态枚举名称");
            }
        }

        public static TokenStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("Token状态值不能为空");
            }
            if (value instanceof TokenStatus) {
                return (TokenStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("Token状态值不能为空字符串");
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
     * <p>
     * 公告状态枚举
     * </p>
     * <p>
     * 用于表示通知公告的状态。
     * 数据库字段: sys_notice.status (VARCHAR)
     * </p>
     */
    public enum NoticeStatus implements BaseEnum {
        PUBLISHED("PUBLISHED", "已发布"),
        UNPUBLISHED("UNPUBLISHED", "已下架");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        NoticeStatus(String code, String desc) {
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

        public static NoticeStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("公告状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (NoticeStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的公告状态编码");
        }

        public static NoticeStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("公告状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (NoticeStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的公告状态描述");
        }

        public static NoticeStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("公告状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的公告状态枚举名称");
            }
        }

        public static NoticeStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("公告状态值不能为空");
            }
            if (value instanceof NoticeStatus) {
                return (NoticeStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("公告状态值不能为空字符串");
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
     * <p>
     * 公告类型枚举
     * </p>
     * <p>
     * 用于表示公告的类型。
     * 数据库字段: sys_notice.notice_type (VARCHAR)
     * </p>
     */
    public enum NoticeType implements BaseEnum {
        ANNOUNCEMENT("ANNOUNCEMENT", "公告"),
        NOTIFICATION("NOTIFICATION", "通知");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        NoticeType(String code, String desc) {
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

        public static NoticeType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("公告类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (NoticeType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的公告类型编码");
        }

        public static NoticeType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("公告类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (NoticeType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的公告类型描述");
        }

        public static NoticeType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("公告类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的公告类型枚举名称");
            }
        }

        public static NoticeType parse(Object value) {
            if (value == null) {
                throw new BusinessException("公告类型值不能为空");
            }
            if (value instanceof NoticeType) {
                return (NoticeType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("公告类型值不能为空字符串");
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
     * <p>
     * 目标类型枚举
     * </p>
     * <p>
     * 用于表示公告/消息的目标类型。
     * 数据库字段: sys_notice.target_type, sys_message_schedule.target_type (VARCHAR)
     * </p>
     */
    public enum TargetType implements BaseEnum {
        ALL("ALL", "全员"),
        TENANT("TENANT", "指定租户"),
        USER("USER", "指定用户"),
        SPECIFIC_USER("SPECIFIC_USER", "指定用户"),
        SPECIFIC_TENANT("SPECIFIC_TENANT", "指定租户"),
        QUALIFIED_USERS("QUALIFIED_USERS", "符合条件的所有用户");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        TargetType(String code, String desc) {
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

        public static TargetType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("目标类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (TargetType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的目标类型编码");
        }

        public static TargetType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("目标类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (TargetType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的目标类型描述");
        }

        public static TargetType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("目标类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的目标类型枚举名称");
            }
        }

        public static TargetType parse(Object value) {
            if (value == null) {
                throw new BusinessException("目标类型值不能为空");
            }
            if (value instanceof TargetType) {
                return (TargetType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("目标类型值不能为空字符串");
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
     * <p>
     * 阅读状态枚举
     * </p>
     * <p>
     * 用于表示公告阅读状态。
     * 数据库字段: sys_notice_user_rel.read_status (VARCHAR)
     * </p>
     */
    public enum ReadStatus implements BaseEnum {
        UNREAD("UNREAD", "未读"),
        READ("READ", "已读");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        ReadStatus(String code, String desc) {
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

        public static ReadStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("阅读状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (ReadStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的阅读状态编码");
        }

        public static ReadStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("阅读状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (ReadStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的阅读状态描述");
        }

        public static ReadStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("阅读状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的阅读状态枚举名称");
            }
        }

        public static ReadStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("阅读状态值不能为空");
            }
            if (value instanceof ReadStatus) {
                return (ReadStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("阅读状态值不能为空字符串");
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
     * <p>
     * 操作日志状态枚举
     * </p>
     * <p>
     * 用于表示操作日志的状态。
     * 数据库字段: sys_oper_log.status (VARCHAR)
     * </p>
     */
    public enum OperLogStatus implements BaseEnum {
        SUCCESS("SUCCESS", "成功"),
        FAIL("FAIL", "失败");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        OperLogStatus(String code, String desc) {
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

        public static OperLogStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("操作日志状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (OperLogStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的操作日志状态编码");
        }

        public static OperLogStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("操作日志状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (OperLogStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的操作日志状态描述");
        }

        public static OperLogStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("操作日志状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的操作日志状态枚举名称");
            }
        }

        public static OperLogStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("操作日志状态值不能为空");
            }
            if (value instanceof OperLogStatus) {
                return (OperLogStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("操作日志状态值不能为空字符串");
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
     * <p>
     * 登录日志状态枚举
     * </p>
     * <p>
     * 用于表示登录日志的状态。
     * 数据库字段: sys_login_log.status (VARCHAR)
     * </p>
     */
    public enum LoginLogStatus implements BaseEnum {
        SUCCESS("SUCCESS", "成功"),
        FAIL("FAIL", "失败");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        LoginLogStatus(String code, String desc) {
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

        public static LoginLogStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("登录日志状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (LoginLogStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的登录日志状态编码");
        }

        public static LoginLogStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("登录日志状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (LoginLogStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的登录日志状态描述");
        }

        public static LoginLogStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("登录日志状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的登录日志状态枚举名称");
            }
        }

        public static LoginLogStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("登录日志状态值不能为空");
            }
            if (value instanceof LoginLogStatus) {
                return (LoginLogStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("登录日志状态值不能为空字符串");
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
     * <p>
     * 操作类型枚举
     * </p>
     * <p>
     * 用于表示数据审计的操作类型。
     * 数据库字段: sys_data_audit_log.operate_type (VARCHAR)
     * </p>
     */
    public enum OperateType implements BaseEnum {
        INSERT("INSERT", "新增"),
        UPDATE("UPDATE", "更新"),
        DELETE("DELETE", "删除");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        OperateType(String code, String desc) {
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

        public static OperateType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("操作类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (OperateType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的操作类型编码");
        }

        public static OperateType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("操作类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (OperateType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的操作类型描述");
        }

        public static OperateType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("操作类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的操作类型枚举名称");
            }
        }

        public static OperateType parse(Object value) {
            if (value == null) {
                throw new BusinessException("操作类型值不能为空");
            }
            if (value instanceof OperateType) {
                return (OperateType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("操作类型值不能为空字符串");
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
     * <p>
     * 业务类型枚举
     * </p>
     * <p>
     * 用于表示操作日志的业务类型。
     * 数据库字段: sys_oper_log.business_type (VARCHAR)
     * </p>
     */
    public enum BusinessType implements BaseEnum {
        INSERT("INSERT", "新增"),
        UPDATE("UPDATE", "修改"),
        DELETE("DELETE", "删除"),
        GRANT("GRANT", "授权"),
        EXPORT("EXPORT", "导出"),
        IMPORT("IMPORT", "导入"),
        OTHER("OTHER", "其他");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        BusinessType(String code, String desc) {
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

        public static BusinessType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("业务类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (BusinessType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的业务类型编码");
        }

        public static BusinessType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("业务类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (BusinessType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的业务类型描述");
        }

        public static BusinessType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("业务类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的业务类型枚举名称");
            }
        }

        public static BusinessType parse(Object value) {
            if (value == null) {
                throw new BusinessException("业务类型值不能为空");
            }
            if (value instanceof BusinessType) {
                return (BusinessType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("业务类型值不能为空字符串");
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
     * <p>
     * 消息模板状态枚举
     * </p>
     * <p>
     * 用于表示消息模板的状态。
     * 数据库字段: sys_message_template.status (VARCHAR)
     * </p>
     */
    public enum MessageTemplateStatus implements BaseEnum {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "停用");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        MessageTemplateStatus(String code, String desc) {
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

        public static MessageTemplateStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("消息模板状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (MessageTemplateStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息模板状态编码");
        }

        public static MessageTemplateStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("消息模板状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (MessageTemplateStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息模板状态描述");
        }

        public static MessageTemplateStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("消息模板状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的消息模板状态枚举名称");
            }
        }

        public static MessageTemplateStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("消息模板状态值不能为空");
            }
            if (value instanceof MessageTemplateStatus) {
                return (MessageTemplateStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("消息模板状态值不能为空字符串");
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
     * <p>
     * 消息类型枚举
     * </p>
     * <p>
     * 用于表示消息模板的类型。
     * 数据库字段: sys_message_template.message_type, sys_inbox_message.message_type (VARCHAR)
     * </p>
     */
    public enum MessageType implements BaseEnum {
        NOTIFICATION("NOTIFICATION", "通知"),
        MARKETING("MARKETING", "营销"),
        VERIFICATION("VERIFICATION", "验证"),
        REMINDER("REMINDER", "提醒"),
        SYSTEM_NOTIFICATION("SYSTEM_NOTIFICATION", "系统通知"),
        APPROVAL_NOTIFICATION("APPROVAL_NOTIFICATION", "审批通知"),
        BILLING_REMINDER("BILLING_REMINDER", "账单提醒"),
        MARKETING_ACTIVITY("MARKETING_ACTIVITY", "营销活动");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        MessageType(String code, String desc) {
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

        public static MessageType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("消息类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (MessageType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息类型编码");
        }

        public static MessageType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("消息类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (MessageType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息类型描述");
        }

        public static MessageType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("消息类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的消息类型枚举名称");
            }
        }

        public static MessageType parse(Object value) {
            if (value == null) {
                throw new BusinessException("消息类型值不能为空");
            }
            if (value instanceof MessageType) {
                return (MessageType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("消息类型值不能为空字符串");
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
     * <p>
     * 消息优先级枚举
     * </p>
     * <p>
     * 用于表示站内信的优先级。
     * 数据库字段: sys_inbox_message.priority (VARCHAR)
     * </p>
     */
    public enum MessagePriority implements BaseEnum {
        NORMAL("NORMAL", "普通"),
        IMPORTANT("IMPORTANT", "重要"),
        URGENT("URGENT", "紧急");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        MessagePriority(String code, String desc) {
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

        public static MessagePriority getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("消息优先级编码不能为空");
            }
            String trimmedCode = code.trim();
            for (MessagePriority e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息优先级编码");
        }

        public static MessagePriority getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("消息优先级描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (MessagePriority e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息优先级描述");
        }

        public static MessagePriority getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("消息优先级枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的消息优先级枚举名称");
            }
        }

        public static MessagePriority parse(Object value) {
            if (value == null) {
                throw new BusinessException("消息优先级值不能为空");
            }
            if (value instanceof MessagePriority) {
                return (MessagePriority) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("消息优先级值不能为空字符串");
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
     * <p>
     * 消息调度状态枚举
     * </p>
     * <p>
     * 用于表示定时消息任务的状态。
     * 数据库字段: sys_message_schedule.status (VARCHAR)
     * </p>
     */
    public enum MessageScheduleStatus implements BaseEnum {
        PENDING("PENDING", "待执行"),
        EXECUTING("EXECUTING", "执行中"),
        COMPLETED("COMPLETED", "已完成"),
        CANCELLED("CANCELLED", "已取消");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        MessageScheduleStatus(String code, String desc) {
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

        public static MessageScheduleStatus getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("消息调度状态编码不能为空");
            }
            String trimmedCode = code.trim();
            for (MessageScheduleStatus e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息调度状态编码");
        }

        public static MessageScheduleStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("消息调度状态描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (MessageScheduleStatus e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的消息调度状态描述");
        }

        public static MessageScheduleStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("消息调度状态枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的消息调度状态枚举名称");
            }
        }

        public static MessageScheduleStatus parse(Object value) {
            if (value == null) {
                throw new BusinessException("消息调度状态值不能为空");
            }
            if (value instanceof MessageScheduleStatus) {
                return (MessageScheduleStatus) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("消息调度状态值不能为空字符串");
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
     * <p>
     * 触发类型枚举
     * </p>
     * <p>
     * 用于表示定时消息任务的触发类型。
     * 数据库字段: sys_message_schedule.trigger_type (VARCHAR)
     * </p>
     */
    public enum TriggerType implements BaseEnum {
        ONCE("ONCE", "单次定时"),
        PERIODIC("PERIODIC", "周期循环"),
        EVENT_TRIGGERED("EVENT_TRIGGERED", "事件触发");

        @EnumValue
        @JSONField(value = true)
        private final String code;
        private final String desc;

        TriggerType(String code, String desc) {
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

        public static TriggerType getByCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new BusinessException("触发类型编码不能为空");
            }
            String trimmedCode = code.trim();
            for (TriggerType e : values()) {
                if (e.getCode().equals(trimmedCode)) {
                    return e;
                }
            }
            throw new BusinessException("无效的触发类型编码");
        }

        public static TriggerType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) {
                throw new BusinessException("触发类型描述不能为空");
            }
            String trimmedDesc = desc.trim();
            for (TriggerType e : values()) {
                if (e.getDesc().equals(trimmedDesc)) {
                    return e;
                }
            }
            throw new BusinessException("无效的触发类型描述");
        }

        public static TriggerType getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("触发类型枚举名称不能为空");
            }
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的触发类型枚举名称");
            }
        }

        public static TriggerType parse(Object value) {
            if (value == null) {
                throw new BusinessException("触发类型值不能为空");
            }
            if (value instanceof TriggerType) {
                return (TriggerType) value;
            }
            String strValue = value.toString().trim();
            if (strValue.isEmpty()) {
                throw new BusinessException("触发类型值不能为空字符串");
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
