package com.shy.nexusix.tenant.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.common.rto.TimeRangeCommonRTO;
import com.shy.nexusix.core.context.UserContext;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.core.entity.dto.UserContextDTO.FieldPermission;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TableFieldPermission;
import com.shy.nexusix.core.entity.dto.UserContextDTO.TenantItem;
import com.shy.nexusix.tenant.converter.SysTenantConverter;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
import com.shy.nexusix.tenant.rto.SysTenantRegisterRTO;
import com.shy.nexusix.tenant.rto.SysTenantReviewRTO;
import com.shy.nexusix.tenant.rto.SysTenantSwitchRTO;
import com.shy.nexusix.tenant.rto.SysTenantUpdateRTO;
import com.shy.nexusix.tenant.service.ISysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import com.shy.nexusix.tenant.vo.SysTenantDetailVO;
import com.shy.nexusix.tenant.vo.SysTenantTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>租户信息服务实现类</p>
 *
 * @author shy
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    /**
     * <p>获取查询操作的可操作字段</p>
     *
     * @return 可操作字段列表，null表示无限制
     */
    private List<String> getQueryOperableFields() {
        // 直接从UserContext获取字段权限，不使用独立工具类
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            return null;
        }

        FieldPermission fieldPerm =
            UserContext.getCurrentPerm();

        if (fieldPerm == null || fieldPerm.getQuery() == null) {
            return null;
        }

        TableFieldPermission tableFieldPerm =
            fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;  // null表示无限制
        }

        return tableFieldPerm.getOperable();
    }

    /**
     * <p>获取指定操作类型的字段权限</p>
     *
     * @param operationType 操作类型："query"、"create"、"update"
     * @return 可操作字段列表，null表示无权限或无限制
     * @throws BusinessException 用户上下文为空或操作类型不支持时抛出
     */
    private List<String> getTableFieldPermission(String operationType) {
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            throw new BusinessException("无法获取用户上下文");
        }

        FieldPermission fieldPerm = UserContext.getCurrentPerm();
        if (fieldPerm == null) {
            return null;
        }

        TableFieldPermission tableFieldPerm = null;
        if ("query".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getQuery() != null) {
                tableFieldPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);
            }
        } else if ("create".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getCreate() != null) {
                tableFieldPerm = fieldPerm.getCreate().get(GlobalConstant.Table.TENANT);
            }
        } else if ("update".equalsIgnoreCase(operationType)) {
            if (fieldPerm.getUpdate() != null) {
                tableFieldPerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);
            }
        } else {
            throw new BusinessException("不支持的操作类型: " + operationType);
        }

        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null) {
            return null;
        }

        return tableFieldPerm.getOperable();
    }

    /**
     * <p>查询租户列表</p>
     *
     * @return 租户通用VO列表，封装用户有权查看的租户信息
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantCommonVO> queryTenantList() {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysTenant.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        List<SysTenant> tenantList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表，同时完成状态码到描述的转换
        return sysTenantConverter.entityListToCommonVoList(tenantList);

    }

    /**
     * <p>分页查询租户列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的租户通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenantPage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysTenant.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        IPage<SysTenant> entityPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysTenantCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysTenantConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询租户树形结构</p>
     *
     * @return 租户树形VO列表，包含完整的层级关系
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {

        // 获取查询操作的字段权限
        List<String> visibleFields = getTableFieldPermission("query");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 构建查询字段集合 合并用户可见字段和树形查询业务必要字段
        // 创建新集合 不修改原始visibleFields，避免污染Session缓存中的权限数据 该集合是为了确保业务执行正确性
        Set<String> queryFields = new HashSet<>(visibleFields);
        queryFields.addAll(GlobalConstant.FieldPerm.TREE_MANDATORY_FIELDS);

        // 查询所有未删除的租户 选择用户有权限查看的列 + 业务必要字段
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysTenant::getPath);
        List<SysTenant> allTenants = this.list(wrapper);

        // 通过 MapStruct 转换器将实体列表转换为 TreeVO 列表
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.entityListToTreeVoList(allTenants);

        // 构建ID到tenantCode的映射 用于通过parentCode(父租户ID)查找父租户的tenantCode
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysTenant tenant : allTenants) {
            idToCodeMap.put(String.valueOf(tenant.getId()), tenant.getTenantCode());
        }

        // 构建tenantCode到TreeVO的映射 用于O(1)时间查找父节点
        Map<String, SysTenantTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysTenantTreeVO treeVO : treeVOList) {
            treeVO.setChildTenant(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getTenantCode(), treeVO);
        }

        // 构建树形结构 遍历所有TreeVO 将子节点挂载到对应的父节点上
        List<SysTenantTreeVO> rootList = new ArrayList<>();
        for (SysTenantTreeVO treeVO : treeVOList) {
            String parentCode = treeVO.getParentCode();
            if (parentCode == null || "0".equals(parentCode)) {
                // 无父租户或父租户ID为0 作为根节点
                rootList.add(treeVO);
            } else {
                // 通过parentCode(父租户ID)查找父租户的tenantCode 再通过tenantCode查找父TreeVO
                String parentTenantCode = idToCodeMap.get(parentCode);
                if (parentTenantCode != null) {
                    SysTenantTreeVO parent = codeToTreeVOMap.get(parentTenantCode);
                    if (parent != null) {
                        parent.getChildTenant().add(treeVO);
                    }
                } else {
                    // 父租户不存在时作为根节点处理
                    rootList.add(treeVO);
                }
            }
        }

        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        sysTenantConverter.filterTreeVoListByVisibleFields(rootList, visibleFields);
        return rootList;

    }

    /**
     * <p>分页查询租户树形结构</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的租户树形VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询字段集合：合并用户可见字段和树形查询业务必要字段
        // 创建新集合，不修改原始visibleFields，避免污染Session缓存中的权限数据
        Set<String> queryFieldSet = new HashSet<>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            queryFieldSet.addAll(visibleFields);
        }
        queryFieldSet.addAll(GlobalConstant.FieldPerm.TREE_MANDATORY_FIELDS);
        Set<String> queryFields = queryFieldSet;

        // 先查询根节点总数用于分页
        LambdaQueryWrapper<SysTenant> rootCountWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysTenant::getParentId).or().eq(SysTenant::getParentId, 0L));
        long rootTotal = this.count(rootCountWrapper);

        // 分页查询根节点 选择用户有权限查看的列 + 业务必要字段
        LambdaQueryWrapper<SysTenant> rootWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysTenant::getParentId).or().eq(SysTenant::getParentId, "0"))
                .orderByAsc(SysTenant::getPath);
        IPage<SysTenant> rootPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), rootWrapper);

        // 收集所有根节点的path前缀 用于一次性查询所有子节点
        // 归一化path：移除末尾"/" 避免与后续拼接的"/"产生双斜杠导致LIKE匹配失败
        List<String> rootPaths = new ArrayList<>();
        List<Long> rootIds = new ArrayList<>();
        for (SysTenant root : rootPage.getRecords()) {
            if (root.getPath() != null && !root.getPath().isEmpty()) {
                rootPaths.add(normalizePathPrefix(root.getPath()));
            }
            rootIds.add(root.getId());
        }

        // 查询所有子节点 如果没有根节点则跳过
        List<SysTenant> allChildren = new ArrayList<>();
        if (!rootPaths.isEmpty()) {
            LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                    .select(SysTenant.class, entity -> queryFields.contains(entity.getProperty()))
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .notIn(SysTenant::getId, rootIds)
                    .and(w -> {
                        // 利用path前缀匹配查询所有子节点 减少多次查询
                        // path已归一化（移除末尾"/"），拼接"/"后可正确匹配子节点路径
                        for (int i = 0; i < rootPaths.size(); i++) {
                            String pathPrefix = rootPaths.get(i);
                            if (i == 0) {
                                w.likeRight(SysTenant::getPath, pathPrefix + "/");
                            } else {
                                w.or().likeRight(SysTenant::getPath, pathPrefix + "/");
                            }
                        }
                    })
                    .orderByAsc(SysTenant::getPath);
            allChildren = this.list(childWrapper);
        }

        // 合并根节点和子节点 并构建ID到tenantCode的映射
        List<SysTenant> allTenants = new ArrayList<>(rootPage.getRecords());
        allTenants.addAll(allChildren);
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysTenant tenant : allTenants) {
            idToCodeMap.put(String.valueOf(tenant.getId()), tenant.getTenantCode());
        }

        // 转换为TreeVO列表
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.entityListToTreeVoList(allTenants);
        Map<String, SysTenantTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysTenantTreeVO treeVO : treeVOList) {
            treeVO.setChildTenant(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getTenantCode(), treeVO);
        }

        // 构建树形结构
        List<SysTenantTreeVO> rootList = new ArrayList<>();
        for (SysTenantTreeVO treeVO : treeVOList) {
            String parentCode = treeVO.getParentCode();
            if (parentCode == null || "0".equals(parentCode)) {
                rootList.add(treeVO);
            } else {
                String parentTenantCode = idToCodeMap.get(parentCode);
                if (parentTenantCode != null) {
                    SysTenantTreeVO parent = codeToTreeVOMap.get(parentTenantCode);
                    if (parent != null) {
                        parent.getChildTenant().add(treeVO);
                    } else {
                        // 父租户不在当前结果集中时作为根节点处理
                        rootList.add(treeVO);
                    }
                } else {
                    // 父租户不存在时作为根节点处理
                    rootList.add(treeVO);
                }
            }
        }

        // 构建分页返回结果
        IPage<SysTenantTreeVO> voPage = new Page<>(rootPage.getCurrent(), rootPage.getSize(), rootTotal);
        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        sysTenantConverter.filterTreeVoListByVisibleFields(rootList, visibleFields);
        voPage.setRecords(rootList);
        return voPage;

    }

    /**
     * <p>查询指定租户的树形结构</p>
     *
     * @param id 租户ID，用于定位要查询的租户节点
     * @return 以指定租户为根的树形结构
     * @throws BusinessException 租户不存在时抛出业务异常
     */
    @Override
    public SysTenantTreeVO queryTenantTree(String id) {

        // 安全校验 租户ID合法性
        if (id == null || id.trim().isEmpty()) {
            throw new BusinessException("租户ID不能为空");
        }

        // 获取查询操作的字段权限
        List<String> visibleFields = getTableFieldPermission("query");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 构建查询字段集合：合并用户可见字段和树形查询业务必要字段
        // 创建新集合，不修改原始visibleFields，避免污染Session缓存中的权限数据
        Set<String> queryFields = new HashSet<>(visibleFields);
        queryFields.addAll(GlobalConstant.FieldPerm.TREE_MANDATORY_FIELDS);

        // 查询指定租户
        LambdaQueryWrapper<SysTenant> targetWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant targetTenant = this.getOne(targetWrapper);
        if (targetTenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 利用path前缀匹配查询所有子节点 一次查询获取整棵子树
        // 归一化path：移除末尾"/" 避免与后续拼接的"/"产生双斜杠导致LIKE匹配失败
        String targetPathPrefix = normalizePathPrefix(targetTenant.getPath());
        LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.eq(SysTenant::getId, id)
                        .or()
                        .likeRight(SysTenant::getPath, targetPathPrefix + "/"))
                .orderByAsc(SysTenant::getPath);
        List<SysTenant> subTreeTenants = this.list(childWrapper);

        // 构建ID到tenantCode的映射
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysTenant tenant : subTreeTenants) {
            idToCodeMap.put(String.valueOf(tenant.getId()), tenant.getTenantCode());
        }

        // 转换为TreeVO列表
        List<SysTenantTreeVO> treeVOList = sysTenantConverter.entityListToTreeVoList(subTreeTenants);
        Map<String, SysTenantTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysTenantTreeVO treeVO : treeVOList) {
            treeVO.setChildTenant(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getTenantCode(), treeVO);
        }

        // 构建树形结构 统一处理所有节点的父子关系
        SysTenantTreeVO rootNode = null;
        for (SysTenantTreeVO treeVO : treeVOList) {
            // 记录根节点（指定租户自身）
            if (treeVO.getTenantCode().equals(targetTenant.getTenantCode())) {
                rootNode = treeVO;
            }
            String parentCode = treeVO.getParentCode();
            // 仅当parentCode有效且不为"0"时，通过parentCode查找父TreeVO并挂载
            if (parentCode != null && !"0".equals(parentCode)) {
                String parentTenantCode = idToCodeMap.get(parentCode);
                if (parentTenantCode != null) {
                    SysTenantTreeVO parent = codeToTreeVOMap.get(parentTenantCode);
                    if (parent != null) {
                        parent.getChildTenant().add(treeVO);
                    }
                }
            }
        }

        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        if (rootNode != null) {
            sysTenantConverter.filterTreeVoByVisibleFields(rootNode, visibleFields);
        }
        return rootNode;

    }

    /**
     * <p>条件查询租户列表</p>
     *
     * @param queryParam 查询条件，包含租户编码、名称、类型、状态等筛选条件
     * @return 满足条件的租户分页列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysTenant.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 租户编码精确匹配
        if (queryParam.getTenantCode() != null && !queryParam.getTenantCode().isEmpty()) {
            wrapper.eq(SysTenant::getTenantCode, queryParam.getTenantCode());
        }
        // 租户名称模糊匹配
        if (queryParam.getTenantName() != null && !queryParam.getTenantName().isEmpty()) {
            wrapper.like(SysTenant::getTenantName, queryParam.getTenantName());
        }
        // 租户类型精确匹配
        if (queryParam.getTenantType() != null && !queryParam.getTenantType().isEmpty()) {
            wrapper.eq(SysTenant::getTenantType, queryParam.getTenantType());
        }
        // 联系人姓名模糊匹配
        if (queryParam.getContactName() != null && !queryParam.getContactName().isEmpty()) {
            wrapper.like(SysTenant::getContactName, queryParam.getContactName());
        }
        // 联系人电话精确匹配
        if (queryParam.getContactPhone() != null && !queryParam.getContactPhone().isEmpty()) {
            wrapper.eq(SysTenant::getContactPhone, queryParam.getContactPhone());
        }
        // 状态精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysTenant::getStatus, queryParam.getStatus());
        }
        // 服务过期时间范围查询
        TimeRangeCommonRTO expireTimeRange = queryParam.getExpireTimeRange();
        if (expireTimeRange != null) {
            if (expireTimeRange.getStartTime() != null) {
                wrapper.ge(SysTenant::getExpireTime, expireTimeRange.getStartTime());
            }
            if (expireTimeRange.getEndTime() != null) {
                wrapper.le(SysTenant::getExpireTime, expireTimeRange.getEndTime());
            }
        }
        // 创建时间范围查询
        TimeRangeCommonRTO createTimeRange = queryParam.getCreateTimeRange();
        if (createTimeRange != null) {
            if (createTimeRange.getStartTime() != null) {
                wrapper.ge(SysTenant::getCreateAt, createTimeRange.getStartTime());
            }
            if (createTimeRange.getEndTime() != null) {
                wrapper.le(SysTenant::getCreateAt, createTimeRange.getEndTime());
            }
        }
        // 更新时间范围查询
        TimeRangeCommonRTO updateTimeRange = queryParam.getUpdateTimeRange();
        if (updateTimeRange != null) {
            if (updateTimeRange.getStartTime() != null) {
                wrapper.ge(SysTenant::getUpdateAt, updateTimeRange.getStartTime());
            }
            if (updateTimeRange.getEndTime() != null) {
                wrapper.le(SysTenant::getUpdateAt, updateTimeRange.getEndTime());
            }
        }

        // 执行分页查询
        IPage<SysTenant> entityPage = this.page(new Page<>(queryParam.getPageNum(), queryParam.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysTenantCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysTenantConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询租户详情</p>
     *
     * @param tenantCode 租户编码，用于定位唯一租户
     * @return 租户详情VO，包含完整的租户信息
     * @throws BusinessException 租户不存在时抛出业务异常
     */
    @Override
    public SysTenantDetailVO queryTenantDetail(String tenantCode) {

        // 参数校验 租户编码不能为空
        if (tenantCode == null || tenantCode.trim().isEmpty()) {
            throw new BusinessException("租户编码不能为空");
        }

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 根据租户编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysTenant.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysTenant::getTenantCode, tenantCode)
               .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysTenant tenant = this.getOne(wrapper);
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysTenantConverter.toDetailVO(tenant);

    }

    /**
     * <p>新增租户</p>
     *
     * @param addParam 新增租户信息
     * @return 新增结果行数
     * @throws BusinessException 租户编码已存在、父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addTenant(SysTenantAddRTO addParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增租户");
        }

        // 校验租户编码唯一性
        LambdaQueryWrapper<SysTenant> codeCheckWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, addParam.getTenantCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(codeCheckWrapper) > 0) {
            throw new BusinessException("租户编码已存在");
        }

        // 查询父租户信息 用于计算path和校验父租户存在性及状态
        SysTenant parentTenant = null;
        if (addParam.getParentCode() != null && !"0".equals(addParam.getParentCode())) {
            parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, addParam.getParentCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (parentTenant == null) {
                throw new BusinessException("父租户不存在");
            }
            // 校验父租户状态：停用或已过期的父租户下不允许新增子租户
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已停用，无法新增子租户");
            }
            if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已过期，无法新增子租户");
            }
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysTenant entity = sysTenantConverter.toEntityFromAdd(addParam);

        // 计算物化路径 如果RTO中未指定path则自动计算
        if (entity.getPath() == null || entity.getPath().isEmpty()) {
            if (parentTenant != null) {
                // 子租户路径 = 父租户路径 + / + 当前租户编码
                entity.setPath(parentTenant.getPath() + "/" + addParam.getTenantCode());
            } else {
                // 根租户路径 = 自身租户编码
                entity.setPath(addParam.getTenantCode());
            }
        }

        // 新增租户默认无子租户
        entity.setHasChildren(false);

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(LocalDateTime.now());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            }
        } else {
            // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值，忽略前端传递的审核字段参数
            entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setCreateAt(LocalDateTime.now());
            entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能设置有权限的字段
        if (!visibleFields.contains("tenantName")) entity.setTenantName(null);
        if (!visibleFields.contains("tenantType")) entity.setTenantType(null);
        if (!visibleFields.contains("tenantDesc")) entity.setTenantDesc(null);
        if (!visibleFields.contains("contactName")) entity.setContactName(null);
        if (!visibleFields.contains("contactPhone")) entity.setContactPhone(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("expireTime")) entity.setExpireTime(null);
        if (!visibleFields.contains("packageId")) entity.setPackageId(null);
        if (!visibleFields.contains("packageName")) entity.setPackageName(null);
        if (!visibleFields.contains("extAttributes")) entity.setExtAttributes(null);

        // 保存租户信息
        this.save(entity);

        // 更新父租户的hasChildren标记
        if (parentTenant != null && !Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, parentTenant.getId())
                    .set(SysTenant::getHasChildren, true));
        }
        return 1;

    }

    /**
     * <p>修改租户</p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果行数
     * @throws BusinessException 租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改租户");
        }

        // 查询待更新的租户 确保租户存在且未删除
        SysTenant existingTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, updateParam.getTenantCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (existingTenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysTenant entity = sysTenantConverter.toEntityFromUpdate(updateParam);

        // 设置实体ID用于更新条件
        entity.setId(existingTenant.getId());

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(existingTenant.getCreateBy());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(existingTenant.getCreateAt());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            // isDeleted和deletedAt未填写时保留原值
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(existingTenant.getIsDeleted());
            }
        } else {
            // 非超级管理员：严格禁止修改审核字段，系统自动填充更新人信息和更新时间，保留原创建信息
            entity.setCreateBy(null);
            entity.setCreateAt(null);
            entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(null);
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能更新有权限的字段
        // 将不可见字段设为null MyBatis-Plus更新时将跳过null字段
        if (!visibleFields.contains("tenantName")) entity.setTenantName(null);
        if (!visibleFields.contains("tenantType")) entity.setTenantType(null);
        if (!visibleFields.contains("tenantDesc")) entity.setTenantDesc(null);
        if (!visibleFields.contains("tenantLogoUrl")) entity.setTenantLogoUrl(null);
        if (!visibleFields.contains("contactName")) entity.setContactName(null);
        if (!visibleFields.contains("contactPhone")) entity.setContactPhone(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("expireTime")) entity.setExpireTime(null);
        if (!visibleFields.contains("packageId")) entity.setPackageId(null);
        if (!visibleFields.contains("packageName")) entity.setPackageName(null);
        if (!visibleFields.contains("extAttributes")) entity.setExtAttributes(null);

        // 租户编码和path不可修改 清除这些字段
        entity.setTenantCode(null);
        entity.setPath(null);
        entity.setId(existingTenant.getId());

        // 执行更新操作 使用updateById仅更新非null字段
        this.updateById(entity);
        return 1;

    }

    /**
     * <p>更新租户状态</p>
     *
     * @param id 租户ID
     * @param status 目标状态（ENABLED/DISABLED/EXPIRED）
     * @return 更新结果行数
     * @throws BusinessException 租户不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updateTenantStatus(String id, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改租户状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.TenantStatus.isValidCode(status)) {
            throw new BusinessException("无效的租户状态");
        }

        // 查询待更新状态的租户
        SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 校验租户是否已处于目标状态
        if (status.equals(tenant.getStatus())) {
            throw new BusinessException("租户已处于该状态，无需重复操作");
        }

        // 校验状态转换是否合法
        if (!GlobalEnum.TenantStatus.isValidTransition(tenant.getStatus(), status)) {
            throw new BusinessException("不允许从" + GlobalEnum.TenantStatus.getByCode(tenant.getStatus()).getDesc()
                    + "状态转换为" + GlobalEnum.TenantStatus.getByCode(status).getDesc() + "状态");
        }

        // 启用租户时校验父租户状态 若父租户处于停用状态，则不允许启用子租户
        if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(status)
                && tenant.getParentId() != null
                && !Long.valueOf(0L).equals(tenant.getParentId())) {
            SysTenant parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, tenant.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (parentTenant != null
                    && GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已停用，无法启用子租户");
            }
        }

        // 更新当前租户状态 审核字段updateBy和updateAt由系统自动设置
        // 停用时记录禁用原因为管理员主动停用，启用时清除禁用原因
        LambdaUpdateWrapper<SysTenant> updateWrapper = new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysTenant::getStatus, status)
                .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysTenant::getUpdateAt, LocalDateTime.now());
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status)) {
            updateWrapper.set(SysTenant::getDisableReason, "ADMIN_DISABLE");
        } else if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(status)) {
            updateWrapper.set(SysTenant::getDisableReason, null);
        }
        this.update(updateWrapper);

        int updatedCount = 1;

        // 如果是停用操作 利用path前缀匹配级联停用所有子租户
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status) && tenant.getPath() != null) {
            LambdaUpdateWrapper<SysTenant> childUpdateWrapper = new LambdaUpdateWrapper<SysTenant>()
                    .likeRight(SysTenant::getPath, normalizePathPrefix(tenant.getPath()) + "/")
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysTenant::getStatus, GlobalEnum.TenantStatus.DISABLED.getCode())
                    .set(SysTenant::getStatus, status)
                    .set(SysTenant::getDisableReason, "PARENT_CASCADE:" + id)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now());
            updatedCount += this.update(childUpdateWrapper) ? 1 : 0;
        }
        return updatedCount;

    }

    /**
     * <p>删除租户</p>
     *
     * @param id 租户ID
     * @return 删除结果行数
     * @throws BusinessException 租户不存在、存在子租户或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deleteTenant(String id) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除租户");
        }

        // 查询待删除的租户
        SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 校验是否存在子租户 利用path前缀匹配查询
        if (tenant.getPath() != null) {
            long childCount = this.count(new LambdaQueryWrapper<SysTenant>()
                    .likeRight(SysTenant::getPath, normalizePathPrefix(tenant.getPath()) + "/")
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (childCount > 0) {
                throw new BusinessException("该租户下存在子租户，无法删除");
            }
        }

        // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
        this.update(new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysTenant::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysTenant::getDeletedAt, LocalDateTime.now())
                .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysTenant::getUpdateAt, LocalDateTime.now()));

        // 更新父租户的hasChildren标记 检查父租户是否还有其他子租户
        if (tenant.getParentId() != null && !Long.valueOf(0L).equals(tenant.getParentId())) {
            long siblingCount = this.count(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getParentId, tenant.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (siblingCount == 0) {
                this.update(new LambdaUpdateWrapper<SysTenant>()
                        .eq(SysTenant::getId, tenant.getParentId())
                        .set(SysTenant::getHasChildren, false));
            }
        }
        return 1;

    }

    /**
     * <p>批量新增租户</p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 成功新增的租户数量
     * @throws BusinessException 任一租户编码重复、父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增租户");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 通过 MapStruct 转换器批量将RTO列表转换为实体列表
        List<SysTenant> entityList = sysTenantConverter.addRTOListToEntityList(addParamList);

        // 批量内重复编码检查 同一批次中租户编码不得重复
        Set<String> batchCodeSet = new HashSet<>();
        for (SysTenant entity : entityList) {
            if (!batchCodeSet.add(entity.getTenantCode())) {
                throw new BusinessException("批量新增中存在重复的租户编码: " + entity.getTenantCode());
            }
        }

        // 遍历处理每个租户实体 校验编码唯一性、计算path、设置默认值、清除不可操作字段
        for (SysTenant entity : entityList) {
            // 校验租户编码唯一性
            long codeCount = this.count(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, entity.getTenantCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (codeCount > 0) {
                throw new BusinessException("租户编码已存在: " + entity.getTenantCode());
            }

            // 查询父租户信息 用于计算path
            if (entity.getParentId() != null && !"0".equals(entity.getParentId())) {
                SysTenant parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getId, entity.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (parentTenant == null) {
                    throw new BusinessException("父租户不存在: " + entity.getParentId());
                }
                // 校验父租户状态 停用或已过期的父租户下不允许新增子租户
                if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                    throw new BusinessException("父租户已停用，无法新增子租户: " + entity.getTenantCode());
                }
                if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(parentTenant.getStatus())) {
                    throw new BusinessException("父租户已过期，无法新增子租户: " + entity.getTenantCode());
                }
                // 计算物化路径
                if (entity.getPath() == null || entity.getPath().isEmpty()) {
                    entity.setPath(parentTenant.getPath() + "/" + entity.getTenantCode());
                }
                // 更新父租户的hasChildren标记
                if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
                    this.update(new LambdaUpdateWrapper<SysTenant>()
                            .eq(SysTenant::getId, parentTenant.getId())
                            .set(SysTenant::getHasChildren, true));
                }
            } else {
                // 根租户路径 = 自身租户编码
                if (entity.getPath() == null || entity.getPath().isEmpty()) {
                    entity.setPath(entity.getTenantCode());
                }
            }

            // 设置默认值
            entity.setHasChildren(false);

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(LocalDateTime.now());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                }
            } else {
                // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值
                entity.setCreateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setCreateAt(LocalDateTime.now());
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("tenantName")) entity.setTenantName(null);
            if (!visibleFields.contains("tenantType")) entity.setTenantType(null);
            if (!visibleFields.contains("tenantDesc")) entity.setTenantDesc(null);
            if (!visibleFields.contains("contactName")) entity.setContactName(null);
            if (!visibleFields.contains("contactPhone")) entity.setContactPhone(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("expireTime")) entity.setExpireTime(null);
            if (!visibleFields.contains("packageId")) entity.setPackageId(null);
            if (!visibleFields.contains("packageName")) entity.setPackageName(null);
            if (!visibleFields.contains("extAttributes")) entity.setExtAttributes(null);
        }

        // 批量保存所有租户
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改租户</p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 成功修改的租户数量
     * @throws BusinessException 任一租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改租户");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 遍历处理每个租户更新
        for (SysTenantUpdateRTO updateParam : updateParamList) {
            // 查询待更新的租户 确保租户存在且未删除
            SysTenant existingTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, updateParam.getTenantCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existingTenant == null) {
                throw new BusinessException("租户不存在: " + updateParam.getTenantCode());
            }

            // 通过 MapStruct 转换器将RTO转换为实体
            SysTenant entity = sysTenantConverter.toEntityFromUpdate(updateParam);
            entity.setId(existingTenant.getId());

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(existingTenant.getCreateBy());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(existingTenant.getCreateAt());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(existingTenant.getIsDeleted());
                }
            } else {
                // 非超级管理员：严格禁止修改审核字段，系统自动填充更新人信息和更新时间
                entity.setCreateBy(null);
                entity.setCreateAt(null);
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(null);
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("tenantName")) entity.setTenantName(null);
            if (!visibleFields.contains("tenantType")) entity.setTenantType(null);
            if (!visibleFields.contains("tenantDesc")) entity.setTenantDesc(null);
            if (!visibleFields.contains("tenantLogoUrl")) entity.setTenantLogoUrl(null);
            if (!visibleFields.contains("contactName")) entity.setContactName(null);
            if (!visibleFields.contains("contactPhone")) entity.setContactPhone(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("expireTime")) entity.setExpireTime(null);
            if (!visibleFields.contains("packageId")) entity.setPackageId(null);
            if (!visibleFields.contains("packageName")) entity.setPackageName(null);
            if (!visibleFields.contains("extAttributes")) entity.setExtAttributes(null);

            // 租户编码和path不可修改
            entity.setTenantCode(null);
            entity.setPath(null);

            // 执行更新
            this.updateById(entity);
        }
        return updateParamList.size();

    }

    /**
     * <p>批量更新租户状态</p>
     *
     * @param ids 租户ID集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenantStatus(List<String> ids, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改租户状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.TenantStatus.isValidCode(status)) {
            throw new BusinessException("无效的租户状态");
        }

        int totalUpdated = 0;

        // 遍历每个租户ID 更新状态
        for (String id : ids) {
            // 查询租户信息
            SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, id)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (tenant == null) {
                continue;
            }

            // 校验租户是否已处于目标状态
            if (status.equals(tenant.getStatus())) {
                continue;
            }

            // 校验状态转换是否合法
            if (!GlobalEnum.TenantStatus.isValidTransition(tenant.getStatus(), status)) {
                continue;
            }

            // 启用租户时校验父租户状态：若父租户处于停用状态，则不允许启用子租户
            if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(status)
                    && tenant.getParentId() != null
                    && !"0".equals(tenant.getParentId())) {
                SysTenant parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getId, tenant.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (parentTenant != null
                        && GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                    throw new BusinessException("父租户已停用，无法启用子租户: " + tenant.getTenantName());
                }
            }

            // 更新当前租户状态 审核字段updateBy和updateAt由系统自动设置
            // 停用时记录禁用原因，启用时清除禁用原因
            LambdaUpdateWrapper<SysTenant> batchUpdateWrapper = new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, id)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysTenant::getStatus, status)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now());
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysTenant::getDisableReason, "ADMIN_DISABLE");
            } else if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysTenant::getDisableReason, null);
            }
            this.update(batchUpdateWrapper);
            totalUpdated++;

            // 如果是停用操作 利用path前缀匹配级联停用所有子租户
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status) && tenant.getPath() != null) {
                this.update(new LambdaUpdateWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, normalizePathPrefix(tenant.getPath()) + "/")
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .ne(SysTenant::getStatus, GlobalEnum.TenantStatus.DISABLED.getCode())
                        .set(SysTenant::getStatus, status)
                        .set(SysTenant::getDisableReason, "PARENT_CASCADE:" + id)
                        .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                        .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            }
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除租户</p>
     *
     * @param ids 租户ID集合
     * @return 删除结果行数
     * @throws BusinessException 存在子租户或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteTenant(List<String> ids) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除租户");
        }

        int totalDeleted = 0;

        // 遍历每个租户ID 执行逻辑删除
        for (String id : ids) {
            // 查询待删除的租户
            SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, id)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (tenant == null) {
                continue;
            }

            // 校验是否存在子租户
            if (tenant.getPath() != null) {
                long childCount = this.count(new LambdaQueryWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, normalizePathPrefix(tenant.getPath()) + "/")
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (childCount > 0) {
                    throw new BusinessException("租户[" + tenant.getTenantName() + "]下存在子租户，无法删除");
                }
            }

            // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, id)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysTenant::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysTenant::getDeletedAt, LocalDateTime.now())
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            totalDeleted++;

            // 更新父租户的hasChildren标记
            if (tenant.getParentId() != null && !Long.valueOf(0L).equals(tenant.getParentId())) {
                long siblingCount = this.count(new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getParentId, tenant.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (siblingCount == 0) {
                    this.update(new LambdaUpdateWrapper<SysTenant>()
                            .eq(SysTenant::getId, tenant.getParentId())
                            .set(SysTenant::getHasChildren, false));
                }
            }
        }
        return totalDeleted;

    }

    /**
     * <p>分配子租户</p>
     *
     * @param assignParam 子租户分配参数，包含父租户编码和子租户编码列表
     * @return 更新子租户行数
     * @throws BusinessException 父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignSubTenant(SysTenantAssignRTO assignParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("parentId") || !visibleFields.contains("path")) {
            throw new BusinessException("无权修改租户层级关系字段");
        }

        // 查询父租户信息
        SysTenant parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (parentTenant == null) {
            throw new BusinessException("父租户不存在");
        }

        // 校验父租户状态：停用或已过期的父租户下不允许分配子租户
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
            throw new BusinessException("父租户已停用，无法分配子租户");
        }
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(parentTenant.getStatus())) {
            throw new BusinessException("父租户已过期，无法分配子租户");
        }

        int updatedCount = 0;

        // 遍历子租户编码列表 更新每个子租户的父租户信息和路径
        for (String subTenantCode : assignParam.getSubCode()) {
            // 自身分配检查 父租户不能分配给自己
            if (parentTenant.getTenantCode().equals(subTenantCode)) {
                throw new BusinessException("不能将租户分配给自己");
            }

            // 查询子租户
            SysTenant subTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, subTenantCode)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (subTenant == null) {
                throw new BusinessException("子租户不存在: " + subTenantCode);
            }

            // 重复分配检查 检查子租户是否已分配在该父租户下
            if (parentTenant.getId().equals(subTenant.getParentId())) {
                throw new BusinessException("子租户已分配在该父租户下: " + subTenantCode);
            }

            // 保存旧路径前缀 用于批量更新子租户的path
            String oldPathPrefix = subTenant.getPath();
            // 计算新路径 = 父租户路径 + / + 子租户编码
            String newPath = parentTenant.getPath() + "/" + subTenant.getTenantCode();

            // 更新子租户的父租户信息和路径 审核字段updateBy和updateAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, subTenant.getId())
                    .set(SysTenant::getParentId, parentTenant.getId())
                    .set(SysTenant::getParentName, parentTenant.getTenantName())
                    .set(SysTenant::getPath, newPath)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            updatedCount++;

            // 利用path前缀匹配更新所有子孙节点的路径 替换旧路径前缀为新路径前缀
            if (oldPathPrefix != null) {
                List<SysTenant> descendants = this.list(new LambdaQueryWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, normalizePathPrefix(oldPathPrefix) + "/")
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                for (SysTenant descendant : descendants) {
                    String newDescendantPath = newPath + descendant.getPath().substring(oldPathPrefix.length());
                    this.update(new LambdaUpdateWrapper<SysTenant>()
                            .eq(SysTenant::getId, descendant.getId())
                            .set(SysTenant::getPath, newDescendantPath)
                            .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                            .set(SysTenant::getUpdateAt, LocalDateTime.now()));
                }
            }
        }

        // 更新父租户的hasChildren标记
        if (!Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, parentTenant.getId())
                    .set(SysTenant::getHasChildren, true));
        }
        return updatedCount;

    }

    /**
     * <p>分配父租户</p>
     *
     * @param assignParam 父租户分配参数，包含新父租户编码和待移动子租户编码列表
     * @return 更新子租户行数
     * @throws BusinessException 新父租户不存在、形成环状结构或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignParentTenant(SysTenantAssignRTO assignParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("parentId") || !visibleFields.contains("path")) {
            throw new BusinessException("无权修改租户层级关系字段");
        }

        // 查询新父租户信息
        SysTenant newParent = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, assignParam.getParentCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (newParent == null) {
            throw new BusinessException("新父租户不存在");
        }

        // 校验新父租户状态 停用或已过期的父租户下不允许分配子租户
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(newParent.getStatus())) {
            throw new BusinessException("新父租户已停用，无法分配子租户");
        }
        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(newParent.getStatus())) {
            throw new BusinessException("新父租户已过期，无法分配子租户");
        }

        int updatedCount = 0;

        // 遍历子租户编码列表 执行层级调整
        for (String subTenantCode : assignParam.getSubCode()) {
            // 自身分配检查：不能将租户移动到自身节点下
            if (newParent.getTenantCode().equals(subTenantCode)) {
                throw new BusinessException("不能将租户移动到自身节点下");
            }

            // 查询待移动的子租户
            SysTenant subTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getTenantCode, subTenantCode)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (subTenant == null) {
                throw new BusinessException("子租户不存在: " + subTenantCode);
            }

            // 利用path属性进行环状结构检测 新父租户的path包含当前租户的tenantCode则说明新父租户是当前租户的后代
            // 使用精确匹配避免短编码误匹配（如编码"A"匹配到"GRP_A"的路径）
            if (newParent.getPath() != null && subTenant.getTenantCode() != null
                    && (newParent.getPath().contains("/" + subTenant.getTenantCode() + "/")
                    || newParent.getPath().endsWith("/" + subTenant.getTenantCode()))) {
                throw new BusinessException("不能将租户移动到自身子节点下，会形成环状结构");
            }

            // 保存旧路径前缀 用于批量更新子孙节点的path
            String oldPathPrefix = subTenant.getPath();
            // 计算新路径 = 新父租户路径 + / + 子租户编码
            String newPath = newParent.getPath() + "/" + subTenant.getTenantCode();

            // 更新当前子租户的父租户信息和路径 审核字段updateBy和updateAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, subTenant.getId())
                    .set(SysTenant::getParentId, newParent.getId())
                    .set(SysTenant::getParentName, newParent.getTenantName())
                    .set(SysTenant::getPath, newPath)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            updatedCount++;

            // 利用path前缀匹配更新所有子孙节点的路径 替换旧路径前缀为新路径前缀
            if (oldPathPrefix != null) {
                List<SysTenant> descendants = this.list(new LambdaQueryWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, normalizePathPrefix(oldPathPrefix) + "/")
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                for (SysTenant descendant : descendants) {
                    String newDescendantPath = newPath + descendant.getPath().substring(oldPathPrefix.length());
                    this.update(new LambdaUpdateWrapper<SysTenant>()
                            .eq(SysTenant::getId, descendant.getId())
                            .set(SysTenant::getPath, newDescendantPath)
                            .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                            .set(SysTenant::getUpdateAt, LocalDateTime.now()));
                }
            }

            // 更新旧父租户的hasChildren标记 检查旧父租户是否还有其他子租户
            if (subTenant.getParentId() != null && !"0".equals(subTenant.getParentId())) {
                long oldSiblingCount = this.count(new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getParentId, subTenant.getParentId())
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (oldSiblingCount == 0) {
                    this.update(new LambdaUpdateWrapper<SysTenant>()
                            .eq(SysTenant::getId, subTenant.getParentId())
                            .set(SysTenant::getHasChildren, false));
                }
            }
        }

        // 更新新父租户的hasChildren标记
        if (!Boolean.TRUE.equals(newParent.getHasChildren())) {
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, newParent.getId())
                    .set(SysTenant::getHasChildren, true));
        }
        return updatedCount;

    }

    /**
     * <p>归一化物化路径前缀，移除末尾的"/"</p>
     *
     * @param path 原始物化路径
     * @return 移除末尾"/"后的路径
     */
    private String normalizePathPrefix(String path) {
        if (path != null && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * <p>租户自助注册</p>
     *
     * @param registerParam 注册信息
     * @return 新增结果行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer registerTenant(SysTenantRegisterRTO registerParam) {

        // 自动生成租户编码：REG_ + 联系人电话后4位 + 时间戳后6位
        String tenantCode = "REG_" + registerParam.getContactPhone().substring(7)
                + String.valueOf(System.currentTimeMillis()).substring(7);

        // 校验租户编码唯一性
        if (this.count(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, tenantCode)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())) > 0) {
            throw new BusinessException("租户编码冲突，请重试");
        }

        // 查询父租户信息（可选）
        SysTenant parentTenant = null;
        if (registerParam.getParentCode() != null && !"0".equals(registerParam.getParentCode())) {
            parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, registerParam.getParentCode())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (parentTenant == null) {
                throw new BusinessException("父租户不存在");
            }
            // 校验父租户状态
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已停用，无法在其下注册子租户");
            }
            if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已过期，无法在其下注册子租户");
            }
            if (GlobalEnum.TenantStatus.PENDING.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户待审核，无法在其下注册子租户");
            }
        }

        // 构建租户实体
        SysTenant entity = new SysTenant();
        entity.setTenantCode(tenantCode);
        entity.setTenantName(registerParam.getTenantName());
        entity.setTenantType(registerParam.getTenantType());
        entity.setContactName(registerParam.getContactName());
        entity.setContactPhone(registerParam.getContactPhone());
        entity.setExtAttributes(registerParam.getExtAttributes());

        // 注册租户状态为PENDING（待审核）
        entity.setStatus(GlobalEnum.TenantStatus.PENDING.getCode());

        // 设置父租户信息
        if (parentTenant != null) {
            entity.setParentId(parentTenant.getId());
            entity.setParentName(parentTenant.getTenantName());
            entity.setPath(parentTenant.getPath() + "/" + tenantCode);
        } else {
            entity.setParentId(0L);
            entity.setParentName(null);
            entity.setPath(tenantCode);
        }

        // 默认值
        entity.setHasChildren(false);
        entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        entity.setCreateBy(0L); // 注册时暂无用户ID，使用0
        entity.setCreateAt(LocalDateTime.now());
        entity.setUpdateBy(0L); // 注册时暂无用户ID，使用0
        entity.setUpdateAt(LocalDateTime.now());

        this.save(entity);

        // 更新父租户hasChildren标记
        if (parentTenant != null && !Boolean.TRUE.equals(parentTenant.getHasChildren())) {
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, parentTenant.getId())
                    .set(SysTenant::getHasChildren, true)
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
        }

        return 1;
    }

    /**
     * <p>审核租户注册</p>
     *
     * @param reviewParam 审核信息
     * @return 审核结果行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer reviewTenant(SysTenantReviewRTO reviewParam) {

        // 查询待审核的租户
        SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getId, reviewParam.getTenantId())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (tenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 校验租户当前状态必须为PENDING
        if (!GlobalEnum.TenantStatus.PENDING.getCode().equals(tenant.getStatus())) {
            throw new BusinessException("仅待审核状态的租户可进行审核操作");
        }

        // 审核通过：PENDING → ENABLED
        if (Boolean.TRUE.equals(reviewParam.getApproved())) {
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, reviewParam.getTenantId())
                    .set(SysTenant::getStatus, GlobalEnum.TenantStatus.ENABLED.getCode())
                    .set(SysTenant::getDisableReason, null)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
        } else {
            // 审核拒绝：PENDING → DISABLED
            String reason = reviewParam.getReviewRemark();
            if (reason == null || reason.trim().isEmpty()) {
                reason = "REVIEW_REJECTED";
            }
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, reviewParam.getTenantId())
                    .set(SysTenant::getStatus, GlobalEnum.TenantStatus.DISABLED.getCode())
                    .set(SysTenant::getDisableReason, "REVIEW_REJECTED:" + reason)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
        }

        return 1;
    }

    /**
     * <p>切换租户</p>
     *
     * @param switchParam 切换参数
     * @return 切换后的租户信息
     */
    @Override
    public SysTenantCommonVO switchTenant(SysTenantSwitchRTO switchParam) {

        // 获取当前用户上下文
        UserContextDTO userContext = UserContext.getUserContext();
        if (userContext == null) {
            throw new BusinessException("无法获取当前用户上下文");
        }

        // 从缓存的有效租户列表中查找目标租户
        TenantItem targetTenantItem = null;
        if (userContext.getTenantInfo().getValid() != null) {
            for (TenantItem item : userContext.getTenantInfo().getValid()) {
                if (switchParam.getTargetTenantCode().equals(item.getTenantCode())) {
                    targetTenantItem = item;
                    break;
                }
            }
        }

        // 目标租户不在有效租户列表中，检查是否在无效租户列表中
        if (targetTenantItem == null) {
            if (userContext.getTenantInfo().getInvalid() != null) {
                for (TenantItem item : userContext.getTenantInfo().getInvalid()) {
                    if (switchParam.getTargetTenantCode().equals(item.getTenantCode())) {
                        // 目标租户在无效租户列表中，根据状态给出具体提示
                        String status = item.getStatus();
                        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status)) {
                            throw new BusinessException("目标租户已停用，无法切换");
                        }
                        if (GlobalEnum.TenantStatus.EXPIRED.getCode().equals(status)) {
                            throw new BusinessException("目标租户已过期，无法切换");
                        }
                        if (GlobalEnum.TenantStatus.PENDING.getCode().equals(status)) {
                            throw new BusinessException("目标租户待审核，无法切换");
                        }
                        throw new BusinessException("目标租户状态异常，无法切换");
                    }
                }
            }
            // 目标租户不在用户的任何租户列表中
            throw new BusinessException("当前用户不属于目标租户，无法切换");
        }

        // 更新Session中的当前租户信息
        List<TenantItem> currentTenantList = new ArrayList<>();
        TenantItem currentTenantItem = new TenantItem();
        currentTenantItem.setTenantCode(targetTenantItem.getTenantCode());
        currentTenantItem.setTenantName(targetTenantItem.getTenantName());
        currentTenantItem.setStatus(targetTenantItem.getStatus());
        currentTenantItem.setIsPrimary(targetTenantItem.getIsPrimary());
        currentTenantList.add(currentTenantItem);

        userContext.getTenantInfo().setCurrent(currentTenantList);

        // 将更新后的上下文写回Session
        StpUtil.getSession().set(GlobalConstant.Session.USER_CONTEXT, userContext);

        // 查询目标租户完整信息用于返回
        SysTenant targetTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantCode, switchParam.getTargetTenantCode())
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        return sysTenantConverter.toCommonVO(targetTenant);

    }

}
