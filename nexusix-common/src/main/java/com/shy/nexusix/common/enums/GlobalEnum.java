package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;

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
     * 通用状态枚举
     * </p>
     * <p>
     * 用于表示系统中通用的启用/禁用状态，适用于租户、用户、角色、菜单等实体的状态字段
     * </p>
     */
    public enum Status implements BaseEnum {
        /**
         * 启用/正常状态
         */
        ENABLE(1, "启用"),

        /**
         * 禁用/冻结状态
         */
        DISABLE(0, "禁用");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static Status getByCode(Integer code) {
            if (code == null) return null;
            for (Status e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }

        /**
         * 根据描述获取枚举
         */
        public static Status getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (Status e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static Status getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 Status 枚举:
         * <ul>
         *   <li>数字类型: 1, 0 → ENABLE, DISABLE</li>
         *   <li>字符串数字: "1", "0" → ENABLE, DISABLE</li>
         *   <li>中文描述: "启用", "禁用" → ENABLE, DISABLE</li>
         *   <li>枚举名称: "ENABLE", "DISABLE" → 对应枚举</li>
         *   <li>枚举对象: Status.ENABLE → 直接返回</li>
         * </ul>
         * </p>
         */
        public static Status parse(Object value) {
            if (value == null) return null;

            if (value instanceof Status) {
                return (Status) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            Status byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }
    }

    /**
     * <p>
     * 租户状态枚举
     * </p>
     * <p>
     * 用于表示租户的状态，正常状态的租户可以正常使用系统，冻结状态的租户下所有用户无法登录
     * </p>
     */
    public enum TenantStatus implements BaseEnum {
        /**
         * 正常状态
         */
        NORMAL(1, "正常"),

        /**
         * 冻结状态
         */
        FROZEN(0, "冻结");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        TenantStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static TenantStatus getByCode(Integer code) {
            if (code == null) return null;
            for (TenantStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 根据描述获取枚举
         */
        public static TenantStatus getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (TenantStatus e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static TenantStatus getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 TenantStatus 枚举:
         * <ul>
         *   <li>数字类型: 1, 0 → NORMAL, FROZEN</li>
         *   <li>字符串数字: "1", "0" → NORMAL, FROZEN</li>
         *   <li>中文描述: "正常", "冻结" → NORMAL, FROZEN</li>
         *   <li>枚举名称: "NORMAL", "FROZEN" → 对应枚举</li>
         *   <li>枚举对象: TenantStatus.NORMAL → 直接返回</li>
         * </ul>
         * </p>
         */
        public static TenantStatus parse(Object value) {
            if (value == null) return null;

            if (value instanceof TenantStatus) {
                return (TenantStatus) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            TenantStatus byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 套餐状态枚举
     * </p>
     * <p>
     * 用于表示套餐产品的上架/下架状态，上架的套餐可供租户订阅，下架的套餐不可供租户订阅
     * </p>
     */
    public enum PackageStatus implements BaseEnum {
        /**
         * 上架状态
         */
        ON_SHELF(1, "上架"),

        /**
         * 下架状态
         */
        OFF_SHELF(0, "下架");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        PackageStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static PackageStatus getByCode(Integer code) {
            if (code == null) return null;
            for (PackageStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 订单状态枚举
     * </p>
     * <p>
     * 用于表示订单的支付状态，包括未支付、已支付、已取消三种状态
     * </p>
     */
    public enum OrderStatus implements BaseEnum {
        /**
         * 未支付状态
         */
        UNPAID(0, "未支付"),

        /**
         * 已支付状态
         */
        PAID(1, "已支付"),

        /**
         * 已取消状态
         */
        CANCELLED(2, "已取消");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        OrderStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static OrderStatus getByCode(Integer code) {
            if (code == null) return null;
            for (OrderStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 发票状态枚举
     * </p>
     * <p>
     * 用于表示发票的开具状态，包括待开具、已开具、已邮寄、已作废四种状态
     * </p>
     */
    public enum InvoiceStatus implements BaseEnum {
        /**
         * 待开具状态
         */
        PENDING(0, "待开具"),

        /**
         * 已开具状态
         */
        ISSUED(1, "已开具"),

        /**
         * 已邮寄状态
         */
        MAILED(2, "已邮寄"),

        /**
         * 已作废状态
         */
        CANCELLED(3, "已作废");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        InvoiceStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static InvoiceStatus getByCode(Integer code) {
            if (code == null) return null;
            for (InvoiceStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 订阅状态枚举
     * </p>
     * <p>
     * 用于表示租户订阅套餐的状态，生效状态的订阅可以正常使用，过期状态的订阅无法使用
     * </p>
     */
    public enum SubscriptionStatus implements BaseEnum {
        /**
         * 生效状态
         */
        ACTIVE(1, "生效"),

        /**
         * 过期状态
         */
        EXPIRED(0, "过期");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        SubscriptionStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static SubscriptionStatus getByCode(Integer code) {
            if (code == null) return null;
            for (SubscriptionStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 消息类型枚举
     * </p>
     * <p>
     * 用于表示消息模板的消息类型，包括通知、营销、验证、提醒四种类型
     * </p>
     */
    public enum MessageType implements BaseEnum {
        /**
         * 通知类型
         */
        NOTIFICATION(1, "通知"),

        /**
         * 营销类型
         */
        MARKETING(2, "营销"),

        /**
         * 验证类型
         */
        VERIFICATION(3, "验证"),

        /**
         * 提醒类型
         */
        REMINDER(4, "提醒");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        MessageType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static MessageType getByCode(Integer code) {
            if (code == null) return null;
            for (MessageType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 菜单类型枚举
     * </p>
     * <p>
     * 用于表示菜单的类型，包括目录、菜单、按钮三种类型
     * </p>
     */
    public enum MenuType implements BaseEnum {
        /**
         * 目录类型
         */
        DIRECTORY(1, "目录"),

        /**
         * 菜单类型
         */
        MENU(2, "菜单"),

        /**
         * 按钮类型
         */
        BUTTON(3, "按钮");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        MenuType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static MenuType getByCode(Integer code) {
            if (code == null) return null;
            for (MenuType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 公告类型枚举
     * </p>
     * <p>
     * 用于表示公告的类型，包括公告、通知两种类型
     * </p>
     */
    public enum NoticeType implements BaseEnum {
        /**
         * 公告类型
         */
        ANNOUNCEMENT(1, "公告"),

        /**
         * 通知类型
         */
        NOTIFICATION(2, "通知");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        NoticeType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static NoticeType getByCode(Integer code) {
            if (code == null) return null;
            for (NoticeType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 登录状态枚举
     * </p>
     * <p>
     * 用于表示登录日志的登录结果，包括成功、失败两种状态
     * </p>
     */
    public enum LoginStatus implements BaseEnum {
        /**
         * 登录失败
         */
        FAIL(0, "失败"),

        /**
         * 登录成功
         */
        SUCCESS(1, "成功");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        LoginStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static LoginStatus getByCode(Integer code) {
            if (code == null) return null;
            for (LoginStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 数据审计操作类型枚举
     * </p>
     * <p>
     * 用于表示数据变更审计的操作类型，包括更新、删除两种类型
     * </p>
     */
    public enum AuditType implements BaseEnum {
        /**
         * 更新操作
         */
        UPDATE(1, "更新"),

        /**
         * 删除操作
         */
        DELETE(2, "删除");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        AuditType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static AuditType getByCode(Integer code) {
            if (code == null) return null;
            for (AuditType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 数据源类型枚举
     * </p>
     * <p>
     * 用于表示动态数据源的类型，包括业务表、API接口、字典、SQL查询四种类型
     * </p>
     */
    public enum DatasourceType implements BaseEnum {
        /**
         * 业务表类型
         */
        TABLE(1, "业务表"),

        /**
         * API接口类型
         */
        API(2, "API接口"),

        /**
         * 字典类型
         */
        DICT(3, "字典"),

        /**
         * SQL查询类型
         */
        SQL(4, "SQL查询");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        DatasourceType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static DatasourceType getByCode(Integer code) {
            if (code == null) return null;
            for (DatasourceType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 打印模板类型枚举
     * </p>
     * <p>
     * 用于表示打印模板的类型，包括HTML、Markdown、JSON配置三种类型
     * </p>
     */
    public enum TemplateType implements BaseEnum {
        /**
         * HTML类型
         */
        HTML(1, "HTML"),

        /**
         * Markdown类型
         */
        MARKDOWN(2, "Markdown"),

        /**
         * JSON配置类型
         */
        JSON(3, "JSON配置");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        TemplateType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static TemplateType getByCode(Integer code) {
            if (code == null) return null;
            for (TemplateType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 用户组类型枚举
     * </p>
     * <p>
     * 用于表示用户组的类型，包括静态组、动态组两种类型。静态组需手动添加成员，动态组根据规则自动匹配成员
     * </p>
     */
    public enum GroupType implements BaseEnum {
        /**
         * 静态组类型
         */
        STATIC(1, "静态组"),

        /**
         * 动态组类型
         */
        DYNAMIC(2, "动态组");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        GroupType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static GroupType getByCode(Integer code) {
            if (code == null) return null;
            for (GroupType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 密码复杂度枚举
     * </p>
     * <p>
     * 用于表示租户安全策略的密码复杂度要求，包括无要求、字母+数字、字母+数字+特殊字符三种级别
     * </p>
     */
    public enum PwdComplexity implements BaseEnum {
        /**
         * 无要求
         */
        NONE(0, "无要求"),

        /**
         * 字母+数字
         */
        LETTER_NUMBER(1, "字母+数字"),

        /**
         * 字母+数字+特殊字符
         */
        STRONG(2, "字母+数字+特殊字符");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        PwdComplexity(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static PwdComplexity getByCode(Integer code) {
            if (code == null) return null;
            for (PwdComplexity e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * Token状态枚举
     * </p>
     * <p>
     * 用于表示用户Token的状态，有效状态的Token可以正常使用，无效状态的Token无法使用（强制下线时将Token置为无效）
     * </p>
     */
    public enum TokenStatus implements BaseEnum {
        /**
         * 无效状态
         */
        INVALID(0, "无效"),

        /**
         * 有效状态
         */
        VALID(1, "有效");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        TokenStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static TokenStatus getByCode(Integer code) {
            if (code == null) return null;
            for (TokenStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 周期类型枚举
     * </p>
     * <p>
     * 用于表示套餐订阅的周期类型，包括天、月、年三种类型
     * </p>
     */
    public enum CycleType implements BaseEnum {
        /**
         * 天
         */
        DAY(1, "天"),

        /**
         * 月
         */
        MONTH(2, "月"),

        /**
         * 年
         */
        YEAR(3, "年");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        CycleType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static CycleType getByCode(Integer code) {
            if (code == null) return null;
            for (CycleType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 订阅类型枚举
     * </p>
     * <p>
     * 用于表示租户订阅套餐的类型，包括自购、父租户分配两种类型
     * </p>
     */
    public enum SubscriptionType implements BaseEnum {
        /**
         * 自购类型
         */
        SELF_PURCHASE(1, "自购"),

        /**
         * 父租户分配类型
         */
        PARENT_GRANT(2, "父租户分配");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        SubscriptionType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static SubscriptionType getByCode(Integer code) {
            if (code == null) return null;
            for (SubscriptionType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 产品类型枚举
     * </p>
     * <p>
     * 用于表示订单的产品类型，包括套餐、配额包两种类型
     * </p>
     */
    public enum ProductType implements BaseEnum {
        /**
         * 套餐类型
         */
        PACKAGE(1, "套餐"),

        /**
         * 配额包类型
         */
        QUOTA_PACKAGE(2, "配额包");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        ProductType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static ProductType getByCode(Integer code) {
            if (code == null) return null;
            for (ProductType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 配额调整类型枚举
     * </p>
     * <p>
     * 用于表示租户配额动态调整的类型，包括购买加油包、补偿、惩罚三种类型
     * </p>
     */
    public enum AdjustType implements BaseEnum {
        /**
         * 购买加油包
         */
        PURCHASE(1, "购买加油包"),

        /**
         * 补偿
         */
        COMPENSATION(2, "补偿"),

        /**
         * 惩罚
         */
        PENALTY(3, "惩罚");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        AdjustType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static AdjustType getByCode(Integer code) {
            if (code == null) return null;
            for (AdjustType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 目标类型枚举
     * </p>
     * <p>
     * 用于表示权限策略、消息任务、公告等的目标类型，包括系统、租户、角色、用户、全员等类型
     * </p>
     */
    public enum TargetType implements BaseEnum {
        /**
         * 系统级
         */
        SYSTEM(1, "系统"),

        /**
         * 租户级
         */
        TENANT(2, "租户"),

        /**
         * 角色级
         */
        ROLE(3, "角色"),

        /**
         * 用户级
         */
        USER(4, "用户"),

        /**
         * 全员
         */
        ALL(5, "全员");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        TargetType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static TargetType getByCode(Integer code) {
            if (code == null) return null;
            for (TargetType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }

        /**
         * 根据描述获取枚举
         */
        public static TargetType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (TargetType e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static TargetType getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 TargetType 枚举:
         * <ul>
         *   <li>数字类型: 1, 2, 3, 4, 5 → SYSTEM, TENANT, ROLE, USER, ALL</li>
         *   <li>字符串数字: "1", "2" → SYSTEM, TENANT</li>
         *   <li>中文描述: "系统", "租户" → SYSTEM, TENANT</li>
         *   <li>枚举名称: "SYSTEM", "TENANT" → 对应枚举</li>
         *   <li>枚举对象: TargetType.SYSTEM → 直接返回</li>
         * </ul>
         * </p>
         */
        public static TargetType parse(Object value) {
            if (value == null) return null;

            if (value instanceof TargetType) {
                return (TargetType) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            TargetType byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }
    }

    /**
     * <p>
     * 策略动作枚举
     * </p>
     * <p>
     * 用于表示权限策略的动作类型，包括允许、拒绝两种类型
     * </p>
     */
    public enum Action implements BaseEnum {
        /**
         * 允许动作
         */
        ALLOW(1, "允许"),

        /**
         * 拒绝动作
         */
        DENY(2, "拒绝");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        Action(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static Action getByCode(Integer code) {
            if (code == null) return null;
            for (Action e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }

        /**
         * 根据描述获取枚举
         */
        public static Action getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (Action e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static Action getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 Action 枚举:
         * <ul>
         *   <li>数字类型: 1, 2 → ALLOW, DENY</li>
         *   <li>字符串数字: "1", "2" → ALLOW, DENY</li>
         *   <li>中文描述: "允许", "拒绝" → ALLOW, DENY</li>
         *   <li>枚举名称: "ALLOW", "DENY" → 对应枚举</li>
         *   <li>枚举对象: Action.ALLOW → 直接返回</li>
         * </ul>
         * </p>
         */
        public static Action parse(Object value) {
            if (value == null) return null;

            if (value instanceof Action) {
                return (Action) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            Action byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }
    }

    /**
     * <p>
     * 权限类型枚举
     * </p>
     * <p>
     * 用于表示权限的类型，包括菜单、按钮、接口、数据字段四种类型
     * </p>
     */
    public enum PermType implements BaseEnum {
        /**
         * 菜单类型
         */
        MENU(1, "菜单"),

        /**
         * 按钮类型
         */
        BUTTON(2, "按钮"),

        /**
         * 接口类型
         */
        API(3, "接口"),

        /**
         * 数据字段类型
         */
        DATA_FIELD(4, "数据字段");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        PermType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static PermType getByCode(Integer code) {
            if (code == null) return null;
            for (PermType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 根据描述获取枚举
         */
        public static PermType getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (PermType e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static PermType getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 PermType 枚举:
         * <ul>
         *   <li>数字类型: 1, 2, 3, 4 → MENU, BUTTON, API, DATA_FIELD</li>
         *   <li>字符串数字: "1", "2" → MENU, BUTTON</li>
         *   <li>中文描述: "菜单", "按钮" → MENU, BUTTON</li>
         *   <li>枚举名称: "MENU", "BUTTON" → 对应枚举</li>
         *   <li>枚举对象: PermType.MENU → 直接返回</li>
         * </ul>
         * </p>
         */
        public static PermType parse(Object value) {
            if (value == null) return null;

            if (value instanceof PermType) {
                return (PermType) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            PermType byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 角色层级枚举
     * </p>
     * <p>
     * 用于表示角色的层级，包括系统级、租户级、用户级三种层级。系统级角色对所有租户可见，租户级角色仅在所属租户内可见，用户级角色为临时角色
     * </p>
     */
    public enum RoleLevel implements BaseEnum {
        /**
         * 系统级
         */
        SYSTEM(1, "系统级"),

        /**
         * 租户级
         */
        TENANT(2, "租户级"),

        /**
         * 用户级
         */
        USER(3, "用户级");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        RoleLevel(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static RoleLevel getByCode(Integer code) {
            if (code == null) return null;
            for (RoleLevel e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }

        /**
         * 根据描述获取枚举
         */
        public static RoleLevel getByDesc(String desc) {
            if (desc == null || desc.trim().isEmpty()) return null;
            for (RoleLevel e : values()) {
                if (e.getDesc().equals(desc.trim())) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         */
        public static RoleLevel getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 RoleLevel 枚举:
         * <ul>
         *   <li>数字类型: 1, 2, 3 → SYSTEM, TENANT, USER</li>
         *   <li>字符串数字: "1", "2" → SYSTEM, TENANT</li>
         *   <li>中文描述: "系统级", "租户级" → SYSTEM, TENANT</li>
         *   <li>枚举名称: "SYSTEM", "TENANT" → 对应枚举</li>
         *   <li>枚举对象: RoleLevel.SYSTEM → 直接返回</li>
         * </ul>
         * </p>
         */
        public static RoleLevel parse(Object value) {
            if (value == null) return null;

            if (value instanceof RoleLevel) {
                return (RoleLevel) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            RoleLevel byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }
    }

    /**
     * <p>
     * 数据范围枚举
     * </p>
     * <p>
     * 用于表示角色的数据权限范围，包括全部、本部门、本人、自定义四种范围
     * </p>
     */
    public enum DataScope implements BaseEnum {
        /**
         * 全部数据
         */
        ALL(1, "全部"),

        /**
         * 本部门数据
         */
        DEPT(2, "本部门"),

        /**
         * 本人数据
         */
        SELF(3, "本人"),

        /**
         * 自定义数据
         */
        CUSTOM(4, "自定义");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        DataScope(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static DataScope getByCode(Integer code) {
            if (code == null) return null;
            for (DataScope e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 触发类型枚举
     * </p>
     * <p>
     * 用于表示定时消息任务的触发类型，包括单次定时、周期循环、事件触发三种类型
     * </p>
     */
    public enum TriggerType implements BaseEnum {
        /**
         * 单次定时
         */
        ONCE(1, "单次定时"),

        /**
         * 周期循环
         */
        PERIODIC(2, "周期循环"),

        /**
         * 事件触发
         */
        EVENT(3, "事件触发");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        TriggerType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static TriggerType getByCode(Integer code) {
            if (code == null) return null;
            for (TriggerType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 定时任务状态枚举
     * </p>
     * <p>
     * 用于表示定时消息任务的状态，包括待执行、执行中、已完成、已取消四种状态
     * </p>
     */
    public enum ScheduleStatus implements BaseEnum {
        /**
         * 待执行状态
         */
        PENDING(0, "待执行"),

        /**
         * 执行中状态
         */
        EXECUTING(1, "执行中"),

        /**
         * 已完成状态
         */
        COMPLETED(2, "已完成"),

        /**
         * 已取消状态
         */
        CANCELLED(3, "已取消");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        ScheduleStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static ScheduleStatus getByCode(Integer code) {
            if (code == null) return null;
            for (ScheduleStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 发票类型枚举
     * </p>
     * <p>
     * 用于表示发票的类型，包括普通发票、专用发票、电子发票三种类型
     * </p>
     */
    public enum InvoiceType implements BaseEnum {
        /**
         * 普通发票
         */
        ORDINARY(1, "普通发票"),

        /**
         * 专用发票
         */
        SPECIAL(2, "专用发票"),

        /**
         * 电子发票
         */
        ELECTRONIC(3, "电子发票");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        InvoiceType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static InvoiceType getByCode(Integer code) {
            if (code == null) return null;
            for (InvoiceType e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 阅读状态枚举
     * </p>
     * <p>
     * 用于表示公告、消息等的阅读状态，包括未读、已读两种状态
     * </p>
     */
    public enum ReadStatus implements BaseEnum {
        /**
         * 未读状态
         */
        UNREAD(0, "未读"),

        /**
         * 已读状态
         */
        READ(1, "已读");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        ReadStatus(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static ReadStatus getByCode(Integer code) {
            if (code == null) return null;
            for (ReadStatus e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 删除标记枚举
     * </p>
     * <p>
     * 用于表示数据的逻辑删除状态，包括未删除、已删除两种状态
     * </p>
     */
    public enum Deleted implements BaseEnum {
        /**
         * 未删除状态
         */
        NOT_DELETED(0, "未删除"),

        /**
         * 已删除状态
         */
        DELETED(1, "已删除");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        Deleted(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static Deleted getByCode(Integer code) {
            if (code == null) return null;
            for (Deleted e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 根据描述获取枚举
         *
         * @param desc 枚举描述
         * @return 枚举对象，如果不存在则返回null
         */
        public static Deleted getByDesc(String desc) {
            if (desc == null) return null;
            for (Deleted e : values()) {
                if (e.getDesc().equals(desc)) return e;
            }
            return null;
        }

        /**
         * 根据名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举对象，如果不存在则返回null
         */
        public static Deleted getByName(String name) {
            if (name == null || name.trim().isEmpty()) return null;
            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 Deleted 枚举:
         * <ul>
         *   <li>数字类型: 0, 1 → NOT_DELETED, DELETED</li>
         *   <li>字符串数字: "0", "1" → NOT_DELETED, DELETED</li>
         *   <li>中文描述: "未删除", "已删除" → NOT_DELETED, DELETED</li>
         *   <li>枚举名称: "NOT_DELETED", "DELETED" → 对应枚举</li>
         *   <li>枚举对象: Deleted.NOT_DELETED → 直接返回</li>
         * </ul>
         * </p>
         */
        public static Deleted parse(Object value) {
            if (value == null) return null;

            if (value instanceof Deleted) {
                return (Deleted) value;
            }

            if (value instanceof Number) {
                return getByCode(((Number) value).intValue());
            }

            String strValue = value.toString().trim();

            try {
                int code = Integer.parseInt(strValue);
                return getByCode(code);
            } catch (NumberFormatException ignored) {

            }

            Deleted byDesc = getByDesc(strValue);
            if (byDesc != null) return byDesc;

            return getByName(strValue);
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }

    /**
     * <p>
     * 布尔标记枚举
     * </p>
     * <p>
     * 用于表示布尔类型字段，包括是、否两种状态。适用于是否管理员、是否自动续费等布尔类型字段
     * </p>
     */
    public enum Boolean implements BaseEnum {
        /**
         * 否
         */
        NO(0, "否"),

        /**
         * 是
         */
        YES(1, "是");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final Integer code;

        /**
         * 枚举描述
         */
        private final String desc;

        /**
         * 构造函数
         *
         * @param code 枚举编码
         * @param desc 枚举描述
         */
        Boolean(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public Integer getCode() {
            return code;
        }

        /**
         * 获取枚举描述
         *
         * @return 枚举描述
         */
        @Override
        public String getDesc() {
            return desc;
        }

        /**
         * 根据编码获取枚举
         *
         * @param code 枚举编码
         * @return 枚举对象，如果不存在则返回null
         */
        public static Boolean getByCode(Integer code) {
            if (code == null) return null;
            for (Boolean e : values()) {
                if (e.getCode().equals(code)) return e;
            }
            return null;
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(Integer code) {
            return getByCode(code) != null;
        }
    }
}
