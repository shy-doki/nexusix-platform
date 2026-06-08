package com.shy.nexusix.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import com.shy.nexusix.common.exception.BusinessException;
import jakarta.validation.Valid;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 租户信息表 - 存储租户基础信息，支持无限层级 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * <p>
     * 查询租户列表
     * </p>
     * <p>
     * 返回所有租户的平铺列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    List<SysTenantCommonVO> queryTenantList();

    /**
     * <p>
     * 分页查询租户列表
     * </p>
     * <p>
     * 返回分页后的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page);

    /**
     * <p>
     * 查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    List<SysTenantTreeVO> queryTenantTreeList();

    /**
     * <p>
     * 分页查询租户树形结构
     * </p>
     * <p>
     * 返回所有租户的层级树形结构
     * 需要登录并具备租户查看权限才能访问。
     * </p>
     *
     * @param page 分页参数
     * @return 分页后的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限
     * @author shy
     * @since 2026-04-19
     */
    IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page);

    /**
     * <p>
     * 查询指定租户的树形结构
     * </p>
     * <p>
     * 查询系统中所有租户的层级关系，并构建成树形结构返回。
     * 返回的租户编码会自动进行脱敏处理（保留前3位和后3位，中间用星号替换）。
     * </p>
     *
     * @param id 租户Id，用于定位要查询的租户节点
     * @return 租户树形结构列表，每个节点包含租户名称、脱敏后的租户编码、父租户ID、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当数据库查询失败或数据异常时抛出
     * @author shy
     * @since 2026-04-19
     */
    SysTenantTreeVO queryTenantTree(String id);

    /**
     * <p>
     * 条件查询\筛选租户列表
     * </p>
     * <p>
     * 返回满足条件的租户列表，租户编码会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户条件查询权限才能访问。
     * </p>
     *
     * @param queryParam 查询条件
     * @return 满足条件的租户列表，包含租户名称、脱敏后的租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam);

    /**
     * <p>
     * 查询租户详情
     * </p>
     * <p>
     * 返回指定租户的详情信息，租户敏感会自动进行脱敏处理（保留前3位和后3位）。
     * 需要登录并具备租户详情查询权限才能访问。
     * </p>
     *
     * @param tenantCode 租户编码
     * @return 租户详情信息，包含租户名称、租户编码、联系人、状态等信息
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或查询失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    SysTenantDetailVO queryTenantDetail(String tenantCode);

    /**
     * <p>
     * 新增租户
     * </p>
     * <p>
     * 新增租户信息，需要登录并具备租户新增权限才能访问。
     * </p>
     *
     * @param addParam 新增租户信息
     * @return 新增租户的ID
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限或新增失败时抛出
     * @author shy
     * @since 2026-04-19
     */
    Integer addTenant(SysTenantAddRTO addParam);

    /**
     * <p>
     * 修改租户
     * </p>
     * <p>
     * 修改租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    Integer updateTenant(SysTenantUpdateRTO updateParam);

    /**
     * <p>
     * 更新租户状态
     * </p>
     * <p>
     * 更新指定租户的状态（正常/冻结），冻结后租户下所有用户无法登录。
     * 需要登录并具备租户修改权限才能访问。
     * </p>
     *
     * @param id 租户ID
     * @param status 租户状态（正常/冻结）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-05
     */
    Integer updateTenantStatus(String id, String status);

    /**
     * <p>
     * 删除租户
     * </p>
     * <p>
     * 删除指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param id 租户ID
     * @return 删除结果：true-成功，false-失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    Integer deleteTenant(@Valid String id);

    /**
     * <p>
     * 批量新增租户
     * </p>
     * <p>
     * 批量新增多个租户信息，需要登录并具备租户新增权限才能访问。
     * 批量操作支持事务回滚，任一租户新增失败则全部失败。
     * </p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 成功新增的租户ID集合
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数校验失败或新增失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    Integer batchAddTenant(List<SysTenantAddRTO> addParamList);

    /**
     * <p>
     * 批量修改租户
     * </p>
     * <p>
     * 批量修改多个租户信息，需要登录并具备租户修改权限才能访问。
     * 仅允许修改指定租户的有效配置信息，不允许修改租户唯一标识。
     * </p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 修改结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或修改失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList);

    /**
     * <p>
     * 批量更新租户状态
     * </p>
     * <p>
     * 批量更新多个指定租户的状态（正常/冻结），冻结后租户下所有用户无法登录。
     * 批量操作支持事务回滚，任一租户更新失败则全部失败。
     * 需要登录并具备租户修改权限才能访问。
     * </p>
     *
     * @param ids 租户ID集合
     * @param status 租户状态（正常/冻结）
     * @return 更新结果行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或更新失败时抛出
     * @author shy
     * @since 2026-05-05
     */
    Integer batchUpdateTenantStatus(List<String> ids, String status);

    /**
     * <p>
     * 批量删除租户
     * </p>
     * <p>
     * 批量删除多个指定租户信息，需要登录并具备租户删除权限才能访问。
     * 删除操作不可逆，删除后租户相关数据将同步清理。
     * </p>
     *
     * @param ids 租户ID集合
     * @return 删除结果：true-全部成功，false-部分/全部失败
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、租户不存在或删除失败时抛出
     * @author shy
     * @since 2026-04-20
     */
    Integer batchDeleteTenant(List<String> ids);

    /**
     * <p>
     * 分配子租户
     * </p>
     * <p>
     * 为指定父租户分配一个新的子租户，自动处理层级关系和ancestors字段更新。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 子租户分配参数
     * @return 更新子租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、父租户不存在或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    Integer assignSubTenant(SysTenantAssignRTO assignParam);

    /**
     * <p>
     * 分配父租户
     * </p>
     * <p>
     * 为指定租户分配一个新的父租户，处理层级关系调整及数据关联更新。
     * 会进行循环层级验证，避免形成环状结构。
     * 需要登录并具备租户分配权限才能访问。
     * </p>
     *
     * @param assignParam 父租户分配参数
     * @return 更新子租户行数
     * @throws com.shy.nexusix.common.exception.BusinessException 当用户无权限、参数非法或分配失败时抛出
     * @author shy
     * @since 2026-05-04
     */
    Integer assignParentTenant(SysTenantAssignRTO assignParam);

    // ==================== 字段权限过滤辅助方法 ====================

    /**
     * DB列名到VO字段名的映射关系
     */
    Map<String, List<String>> DB_COLUMN_TO_VO_FIELDS = Map.ofEntries(
            Map.entry("tenant_code", List.of("tenantCode")),
            Map.entry("tenant_name", List.of("tenantName")),
            Map.entry("tenant_type", List.of("tenantType")),
            Map.entry("parent_name", List.of("parentName")),
            Map.entry("contact_name", List.of("contactName")),
            Map.entry("contact_phone", List.of("contactPhone")),
            Map.entry("status", List.of("status")),
            Map.entry("expire_time", List.of("expireTime")),
            Map.entry("has_children", List.of("hasChildren")),
            Map.entry("package_name", List.of("packageName")),
            Map.entry("create_by", List.of("createByName", "createByCode")),
            Map.entry("create_at", List.of("createTime")),
            Map.entry("update_by", List.of("updateByName", "updateByCode")),
            Map.entry("update_at", List.of("updateTime")),
            Map.entry("is_deleted", List.of("isDeleted")),
            Map.entry("deleted_at", List.of("deleteTime")),
            Map.entry("tenant_desc", List.of("tenantDesc")),
            Map.entry("tenant_logo_url", List.of("tenantLogoUrl")),
            Map.entry("path", List.of("path")),
            Map.entry("ext_attributes", List.of("extAttributes")),
            Map.entry("parent_id", List.of("parentCode"))
    );

    /**
     * 树形查询业务必需的数据库字段
     */
    Set<String> TREE_MANDATORY_FIELDS = Set.of("id", "tenant_code", "parent_id", "path");

    /**
     * 构建查询字段集合：合并用户可见字段和业务必要字段
     */
    default Set<String> buildQueryFieldSet(List<String> visibleFields, Set<String> mandatoryFields) {
        Set<String> queryFields = new HashSet<>(visibleFields);
        if (mandatoryFields != null) {
            queryFields.addAll(mandatoryFields);
        }
        return queryFields;
    }

    /**
     * 根据用户可操作字段列表过滤VO对象
     */
    default void filterVoByVisibleFields(Object vo, List<String> visibleFields) {
        if (vo == null || visibleFields == null) return;

        Set<String> allowedVoFields = new HashSet<>();
        for (String dbColumn : visibleFields) {
            List<String> voFields = DB_COLUMN_TO_VO_FIELDS.get(dbColumn);
            if (voFields != null) allowedVoFields.addAll(voFields);
        }

        try {
            Class<?> clazz = vo.getClass();
            while (clazz != null && clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    String fieldName = field.getName();
                    if ("childTenant".equals(fieldName) || "serialVersionUID".equals(fieldName)) continue;
                    if (!allowedVoFields.contains(fieldName)) {
                        field.setAccessible(true);
                        if (!field.getType().isPrimitive()) field.set(vo, null);
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            throw new BusinessException("字段权限过滤异常");
        }
    }

    /**
     * 递归过滤树形VO及其所有子节点
     */
    default void filterTreeVoByVisibleFields(SysTenantTreeVO treeVO, List<String> visibleFields) {
        filterVoByVisibleFields(treeVO, visibleFields);
        if (treeVO.getChildTenant() != null) {
            for (SysTenantTreeVO child : treeVO.getChildTenant()) {
                filterTreeVoByVisibleFields(child, visibleFields);
            }
        }
    }

    /**
     * 批量过滤树形VO列表
     */
    default void filterTreeVoListByVisibleFields(List<SysTenantTreeVO> treeVOList, List<String> visibleFields) {
        if (treeVOList == null) return;
        for (SysTenantTreeVO treeVO : treeVOList) {
            filterTreeVoByVisibleFields(treeVO, visibleFields);
        }
    }

    /**
     * 归一化物化路径前缀：移除末尾的"/"
     * <p>数据库中path格式为 /1/2/3/（带前导和末尾斜杠），
     * 后续使用 likeRight(path, prefix + "/") 查询子节点时，
     * 如果path以"/"结尾则会产生双斜杠 /1/2/3// 导致LIKE匹配失败。
     * 归一化后 prefix = /1/2/3，拼接后为 /1/2/3/ 可正确匹配子节点。</p>
     */
    default String normalizePathPrefix(String path) {
        if (path != null && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

}
