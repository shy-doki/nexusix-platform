package com.shy.nexusix.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 字段权限实体 - 用于存储用户在各个表上的字段操作权限
 * </p>
 * <p>
 * 数据结构：{操作类型: {表名: [字段集合]}}
 * 例如：{query: {sys_tenant: ["id", "tenant_name"]}, update: {...}}
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
public class ColumnPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询操作的字段权限
     * key: 表名（如：sys_tenant）
     * value: 可查询的字段集合
     */
    private Map<String, Set<String>> query;

    /**
     * 创建操作的字段权限
     * key: 表名（如：sys_tenant）
     * value: 可创建的字段集合
     */
    private Map<String, Set<String>> create;

    /**
     * 更新操作的字段权限
     * key: 表名（如：sys_tenant）
     * value: 可更新的字段集合
     */
    private Map<String, Set<String>> update;

}
