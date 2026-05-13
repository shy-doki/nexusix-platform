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
        /**
         * 未删除状态
         */
        NOT_DELETED("ACTIVE", "未删除"),

        /**
         * 已删除状态
         */
        DELETED("DELETED", "已删除");

        /**
         * 枚举编码
         */
        @EnumValue
        @JSONField(value = true)
        private final String code;

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
        Deleted(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 获取枚举编码
         *
         * @return 枚举编码
         */
        @Override
        public String getCode() {
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
         * @return 枚举对象
         * @throws com.shy.nexusix.common.exception.BusinessException 当编码无效时抛出
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
         * @param desc 枚举描述
         * @return 枚举对象
         * @throws com.shy.nexusix.common.exception.BusinessException 当描述无效时抛出
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
         * 根据名称获取枚举
         *
         * @param name 枚举名称
         * @return 枚举对象
         * @throws com.shy.nexusix.common.exception.BusinessException 当名称无效时抛出
         */
        public static Deleted getByName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new BusinessException("删除标记枚举名称不能为空");
            }

            try {
                return valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("无效的删除标记枚举名称: ");
            }
        }

        /**
         * <p>
         * 智能解析枚举值
         * </p>
         * <p>
         * 支持多种输入格式自动转换为 Deleted 枚举:
         * <ul>
         *   <li>字符串编码: "ACTIVE", "DELETED" → 对应枚举</li>
         *   <li>中文描述: "未删除", "已删除" → 对应枚举</li>
         *   <li>枚举名称: "NOT_DELETED", "DELETED" → 对应枚举</li>
         *   <li>枚举对象: Deleted.NOT_DELETED → 直接返回</li>
         * </ul>
         * </p>
         *
         * @param value 待解析的值
         * @return 枚举对象
         * @throws com.shy.nexusix.common.exception.BusinessException 当无法解析时抛出
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

            // 尝试作为编码匹配
            Deleted byCode = getByCode(strValue);
            if (byCode != null) {
                return byCode;
            }

            // 尝试作为描述匹配
            Deleted byDesc = getByDesc(strValue);
            if (byDesc != null) {
                return byDesc;
            }

            // 尝试作为枚举名称匹配
            Deleted byName = getByName(strValue);
            if (byName != null) {
                return byName;
            }
            throw new BusinessException("无法解析的删除标记值: " + value);
        }

        /**
         * 判断编码是否有效
         *
         * @param code 枚举编码
         * @return true-有效，false-无效
         */
        public static boolean isValidCode(String code) {
            return getByCode(code) != null;
        }
    }

}
