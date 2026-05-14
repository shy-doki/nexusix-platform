package com.shy.nexusix.common.enums;

/**
 * <p>
 * 字段安全级别枚举 - 定义字段可见性的三个层级
 * </p>
 * <p>
 * 层级关系（由低到高）：PUBLIC → INTERNAL → CONFIDENTIAL
 * <ul>
 *   <li>PUBLIC：所有角色可见</li>
 *   <li>INTERNAL：管理员及以上角色可见</li>
 *   <li>CONFIDENTIAL：仅超级管理员可见</li>
 * </ul>
 * </p>
 *
 * @author shy
 * @since 2026-05-14
 */
public enum SecurityLevel {

    /**
     * 公开 - 所有角色可见
     */
    PUBLIC,

    /**
     * 内部 - 管理员及以上角色可见
     */
    INTERNAL,

    /**
     * 机密 - 仅超级管理员可见
     */
    CONFIDENTIAL

}
