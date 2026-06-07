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
import com.shy.nexusix.tenant.converter.SysTenantConverter;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.mapper.SysTenantMapper;
import com.shy.nexusix.tenant.rto.SysTenantAddRTO;
import com.shy.nexusix.tenant.rto.SysTenantAssignRTO;
import com.shy.nexusix.tenant.rto.SysTenantQueryRTO;
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
 * @since 2026-06-07
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    /**
     * <p>查询租户列表</p>
     *
     * @return 租户通用VO列表，封装用户有权查看的租户信息
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantCommonVO> queryTenantList() {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                // 遍历 SysTenant 实体的所有字段 仅选择可操作字段
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
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

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的租户记录
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

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
     * <p>基于path物化路径实现，利用Map进行O(n)复杂度的树构建，避免递归操作</p>
     *
     * @return 租户树形VO列表，包含完整的层级关系
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysTenantTreeVO> queryTenantTreeList() {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 查询所有未删除的租户 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
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
        return rootList;

    }

    /**
     * <p>分页查询租户树形结构</p>
     * <p>对根节点进行分页，每个根节点携带完整的子树</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的租户树形VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO page) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 先查询根节点总数用于分页
        LambdaQueryWrapper<SysTenant> rootCountWrapper = new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysTenant::getParentId).or().eq(SysTenant::getParentId, "0"));
        long rootTotal = this.count(rootCountWrapper);

        // 分页查询根节点 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> rootWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysTenant::getParentId).or().eq(SysTenant::getParentId, "0"))
                .orderByAsc(SysTenant::getPath);
        IPage<SysTenant> rootPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), rootWrapper);

        // 收集所有根节点的path前缀 用于一次性查询所有子节点
        List<String> rootPaths = new ArrayList<>();
        for (SysTenant root : rootPage.getRecords()) {
            if (root.getPath() != null && !root.getPath().isEmpty()) {
                rootPaths.add(root.getPath());
            }
        }

        // 查询所有子节点 如果没有根节点则跳过
        List<SysTenant> allChildren = new ArrayList<>();
        if (!rootPaths.isEmpty()) {
            LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                    .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .and(w -> {
                        // 利用path前缀匹配查询所有子节点 减少多次查询
                        // 加"/"后缀避免匹配到根节点自身和路径前缀碰撞的无关节点
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
        voPage.setRecords(rootList);
        return voPage;

    }

    /**
     * <p>查询指定租户的树形结构</p>
     * <p>基于path物化路径前缀匹配，一次性查询所有子节点，避免递归查询</p>
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

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 查询指定租户
        LambdaQueryWrapper<SysTenant> targetWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysTenant targetTenant = this.getOne(targetWrapper);
        if (targetTenant == null) {
            throw new BusinessException("租户不存在");
        }

        // 利用path前缀匹配查询所有子节点 一次查询获取整棵子树
        LambdaQueryWrapper<SysTenant> childWrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.eq(SysTenant::getId, id)
                        .or()
                        .likeRight(SysTenant::getPath, targetTenant.getPath() + "/"))
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
        return rootNode;

    }

    /**
     * <p>条件查询租户列表</p>
     * <p>支持多条件筛选和分页，基于字段权限动态控制可见列</p>
     *
     * @param queryParam 查询条件，包含租户编码、名称、类型、状态等筛选条件
     * @return 满足条件的租户分页列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysTenantCommonVO> queryTenant(SysTenantQueryRTO queryParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

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

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的查询操作字段权限
        UserContextDTO.EntityFieldPerm tenantQueryPerm = fieldPerm.getQuery().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于动态列选择
        List<String> visibleFields = tenantQueryPerm.getVisibleFields();
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询租户信息");
        }

        // 根据租户编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant.class, entity -> visibleFields.contains(entity.getColumn()))
                .eq(SysTenant::getTenantCode, tenantCode)
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
     * <p>自动计算物化路径path，校验租户编码唯一性和父租户存在性</p>
     *
     * @param addParam 新增租户信息
     * @return 新增结果行数
     * @throws BusinessException 租户编码已存在、父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addTenant(SysTenantAddRTO addParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的新增操作字段权限
        UserContextDTO.EntityFieldPerm tenantCreatePerm = fieldPerm.getCreate().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于字段权限校验
        List<String> visibleFields = tenantCreatePerm.getVisibleFields();
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
                entity.setCreateBy(StpUtil.getLoginIdAsString());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(LocalDateTime.now());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(StpUtil.getLoginIdAsString());
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            }
        } else {
            // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值，忽略前端传递的审核字段参数
            entity.setCreateBy(StpUtil.getLoginIdAsString());
            entity.setCreateAt(LocalDateTime.now());
            entity.setUpdateBy(StpUtil.getLoginIdAsString());
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能设置有权限的字段
        if (!visibleFields.contains("tenant_name")) entity.setTenantName(null);
        if (!visibleFields.contains("tenant_type")) entity.setTenantType(null);
        if (!visibleFields.contains("tenant_desc")) entity.setTenantDesc(null);
        if (!visibleFields.contains("contact_name")) entity.setContactName(null);
        if (!visibleFields.contains("contact_phone")) entity.setContactPhone(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("expire_time")) entity.setExpireTime(null);
        if (!visibleFields.contains("package_id")) entity.setPackageId(null);
        if (!visibleFields.contains("package_name")) entity.setPackageName(null);
        if (!visibleFields.contains("ext_attributes")) entity.setExtAttributes(null);

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
     * <p>仅允许修改用户有权限的字段，租户编码不可修改</p>
     *
     * @param updateParam 修改租户信息
     * @return 修改结果行数
     * @throws BusinessException 租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTenant(SysTenantUpdateRTO updateParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于字段权限校验
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
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
                entity.setUpdateBy(StpUtil.getLoginIdAsString());
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
            entity.setUpdateBy(StpUtil.getLoginIdAsString());
            entity.setUpdateAt(LocalDateTime.now());
            entity.setIsDeleted(null);
            entity.setDeletedAt(null);
        }

        // 根据字段权限清除不可操作的字段值 确保用户只能更新有权限的字段
        // 将不可见字段设为null MyBatis-Plus更新时将跳过null字段
        if (!visibleFields.contains("tenant_name")) entity.setTenantName(null);
        if (!visibleFields.contains("tenant_type")) entity.setTenantType(null);
        if (!visibleFields.contains("tenant_desc")) entity.setTenantDesc(null);
        if (!visibleFields.contains("tenant_logo_url")) entity.setTenantLogoUrl(null);
        if (!visibleFields.contains("contact_name")) entity.setContactName(null);
        if (!visibleFields.contains("contact_phone")) entity.setContactPhone(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("expire_time")) entity.setExpireTime(null);
        if (!visibleFields.contains("package_id")) entity.setPackageId(null);
        if (!visibleFields.contains("package_name")) entity.setPackageName(null);
        if (!visibleFields.contains("ext_attributes")) entity.setExtAttributes(null);

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
     * <p>冻结父租户时，通过path前缀匹配级联冻结所有子租户</p>
     *
     * @param id 租户ID
     * @param status 目标状态（ENABLED/DISABLED/EXPIRED）
     * @return 更新结果行数
     * @throws BusinessException 租户不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updateTenantStatus(String id, String status) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改status字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
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

        // 启用租户时校验父租户状态 若父租户处于停用状态，则不允许启用子租户
        if (GlobalEnum.TenantStatus.ENABLED.getCode().equals(status)
                && tenant.getParentId() != null
                && !"0".equals(tenant.getParentId())) {
            SysTenant parentTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getId, tenant.getParentId())
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (parentTenant != null
                    && GlobalEnum.TenantStatus.DISABLED.getCode().equals(parentTenant.getStatus())) {
                throw new BusinessException("父租户已停用，无法启用子租户");
            }
        }

        // 更新当前租户状态 审核字段updateBy和updateAt由系统自动设置
        this.update(new LambdaUpdateWrapper<SysTenant>()
                .eq(SysTenant::getId, id)
                .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysTenant::getStatus, status)
                .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysTenant::getUpdateAt, LocalDateTime.now()));

        int updatedCount = 1;

        // 如果是停用操作 利用path前缀匹配级联停用所有子租户
        if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status) && tenant.getPath() != null) {
            LambdaUpdateWrapper<SysTenant> childUpdateWrapper = new LambdaUpdateWrapper<SysTenant>()
                    .likeRight(SysTenant::getPath, tenant.getPath() + "/")
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysTenant::getStatus, GlobalEnum.TenantStatus.DISABLED.getCode())
                    .set(SysTenant::getStatus, status)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now());
            updatedCount += this.update(childUpdateWrapper) ? 1 : 0;
        }
        return updatedCount;

    }

    /**
     * <p>删除租户</p>
     * <p>逻辑删除，删除前校验租户无子租户</p>
     *
     * @param id 租户ID
     * @return 删除结果行数
     * @throws BusinessException 租户不存在、存在子租户或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deleteTenant(String id) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限（删除属于更新操作范畴）
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改is_deleted字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
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
                    .likeRight(SysTenant::getPath, tenant.getPath() + "/")
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
        if (tenant.getParentId() != null && !"0".equals(tenant.getParentId())) {
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
     * <p>事务控制，任一租户新增失败则全部回滚</p>
     *
     * @param addParamList 批量新增租户信息集合
     * @return 成功新增的租户数量
     * @throws BusinessException 任一租户编码重复、父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddTenant(List<SysTenantAddRTO> addParamList) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的新增操作字段权限
        UserContextDTO.EntityFieldPerm tenantCreatePerm = fieldPerm.getCreate().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于字段权限校验
        List<String> visibleFields = tenantCreatePerm.getVisibleFields();
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
                    entity.setCreateBy(StpUtil.getLoginIdAsString());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(LocalDateTime.now());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(StpUtil.getLoginIdAsString());
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                }
            } else {
                // 非超级管理员：严格禁止设置审核字段，系统自动填充默认值
                entity.setCreateBy(StpUtil.getLoginIdAsString());
                entity.setCreateAt(LocalDateTime.now());
                entity.setUpdateBy(StpUtil.getLoginIdAsString());
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("tenant_name")) entity.setTenantName(null);
            if (!visibleFields.contains("tenant_type")) entity.setTenantType(null);
            if (!visibleFields.contains("tenant_desc")) entity.setTenantDesc(null);
            if (!visibleFields.contains("contact_name")) entity.setContactName(null);
            if (!visibleFields.contains("contact_phone")) entity.setContactPhone(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("expire_time")) entity.setExpireTime(null);
            if (!visibleFields.contains("package_id")) entity.setPackageId(null);
            if (!visibleFields.contains("package_name")) entity.setPackageName(null);
            if (!visibleFields.contains("ext_attributes")) entity.setExtAttributes(null);
        }

        // 批量保存所有租户
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改租户</p>
     * <p>事务控制，任一租户修改失败则全部回滚</p>
     *
     * @param updateParamList 批量修改租户信息集合
     * @return 成功修改的租户数量
     * @throws BusinessException 任一租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenant(List<SysTenantUpdateRTO> updateParamList) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 提取用户可操作的字段列表 用于字段权限校验
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
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
                    entity.setUpdateBy(StpUtil.getLoginIdAsString());
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
                entity.setUpdateBy(StpUtil.getLoginIdAsString());
                entity.setUpdateAt(LocalDateTime.now());
                entity.setIsDeleted(null);
                entity.setDeletedAt(null);
            }

            // 根据字段权限清除不可操作的字段值
            if (!visibleFields.contains("tenant_name")) entity.setTenantName(null);
            if (!visibleFields.contains("tenant_type")) entity.setTenantType(null);
            if (!visibleFields.contains("tenant_desc")) entity.setTenantDesc(null);
            if (!visibleFields.contains("tenant_logo_url")) entity.setTenantLogoUrl(null);
            if (!visibleFields.contains("contact_name")) entity.setContactName(null);
            if (!visibleFields.contains("contact_phone")) entity.setContactPhone(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("expire_time")) entity.setExpireTime(null);
            if (!visibleFields.contains("package_id")) entity.setPackageId(null);
            if (!visibleFields.contains("package_name")) entity.setPackageName(null);
            if (!visibleFields.contains("ext_attributes")) entity.setExtAttributes(null);

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
     * <p>事务控制，任一租户状态更新失败则全部回滚</p>
     *
     * @param ids 租户ID集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateTenantStatus(List<String> ids, String status) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改status字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
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
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, id)
                    .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysTenant::getStatus, status)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            totalUpdated++;

            // 如果是停用操作 利用path前缀匹配级联停用所有子租户
            if (GlobalEnum.TenantStatus.DISABLED.getCode().equals(status) && tenant.getPath() != null) {
                this.update(new LambdaUpdateWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, tenant.getPath() + "/")
                        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .ne(SysTenant::getStatus, GlobalEnum.TenantStatus.DISABLED.getCode())
                        .set(SysTenant::getStatus, status)
                        .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                        .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            }
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除租户</p>
     * <p>事务控制，任一租户删除失败则全部回滚</p>
     *
     * @param ids 租户ID集合
     * @return 删除结果行数
     * @throws BusinessException 存在子租户或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteTenant(List<String> ids) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限（删除属于更新操作范畴）
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改is_deleted字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
        if (visibleFields == null || !visibleFields.contains("is_deleted")) {
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
                        .likeRight(SysTenant::getPath, tenant.getPath() + "/")
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
            if (tenant.getParentId() != null && !"0".equals(tenant.getParentId())) {
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
     * <p>将指定的子租户分配到指定父租户下，更新parentId、parentName和path</p>
     *
     * @param assignParam 子租户分配参数，包含父租户编码和子租户编码列表
     * @return 更新子租户行数
     * @throws BusinessException 父租户不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignSubTenant(SysTenantAssignRTO assignParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改parentId和path字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
        if (visibleFields == null || !visibleFields.contains("parent_id") || !visibleFields.contains("path")) {
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
            if (String.valueOf(parentTenant.getId()).equals(subTenant.getParentId())) {
                throw new BusinessException("子租户已分配在该父租户下: " + subTenantCode);
            }

            // 保存旧路径前缀 用于批量更新子租户的path
            String oldPathPrefix = subTenant.getPath();
            // 计算新路径 = 父租户路径 + / + 子租户编码
            String newPath = parentTenant.getPath() + "/" + subTenant.getTenantCode();

            // 更新子租户的父租户信息和路径 审核字段updateBy和updateAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, subTenant.getId())
                    .set(SysTenant::getParentId, String.valueOf(parentTenant.getId()))
                    .set(SysTenant::getParentName, parentTenant.getTenantName())
                    .set(SysTenant::getPath, newPath)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            updatedCount++;

            // 利用path前缀匹配更新所有子孙节点的路径 替换旧路径前缀为新路径前缀
            if (oldPathPrefix != null) {
                List<SysTenant> descendants = this.list(new LambdaQueryWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, oldPathPrefix + "/")
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
     * <p>将指定子租户移动到新的父租户下，利用path属性进行环状结构检测和路径更新</p>
     *
     * @param assignParam 父租户分配参数，包含新父租户编码和待移动子租户编码列表
     * @return 更新子租户行数
     * @throws BusinessException 新父租户不存在、形成环状结构或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer assignParentTenant(SysTenantAssignRTO assignParam) {

        // 获取当前登录用户的上下文信息，包含权限配置
        UserContextDTO userContext = UserContext.getUserContext();

        // 从权限上下文中提取字段级权限配置
        UserContextDTO.FieldPerm fieldPerm = userContext.getPermInfo().getFieldPerm();
        // 获取用户对租户表的更新操作字段权限
        UserContextDTO.EntityFieldPerm tenantUpdatePerm = fieldPerm.getUpdate().get(GlobalConstant.Table.TENANT);

        // 校验用户是否有权限修改parentId和path字段
        List<String> visibleFields = tenantUpdatePerm.getVisibleFields();
        if (visibleFields == null || !visibleFields.contains("parent_id") || !visibleFields.contains("path")) {
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
            if (newParent.getPath() != null && newParent.getPath().contains(subTenant.getTenantCode())) {
                throw new BusinessException("不能将租户移动到自身子节点下，会形成环状结构");
            }

            // 保存旧路径前缀 用于批量更新子孙节点的path
            String oldPathPrefix = subTenant.getPath();
            // 计算新路径 = 新父租户路径 + / + 子租户编码
            String newPath = newParent.getPath() + "/" + subTenant.getTenantCode();

            // 更新当前子租户的父租户信息和路径 审核字段updateBy和updateAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysTenant>()
                    .eq(SysTenant::getId, subTenant.getId())
                    .set(SysTenant::getParentId, String.valueOf(newParent.getId()))
                    .set(SysTenant::getParentName, newParent.getTenantName())
                    .set(SysTenant::getPath, newPath)
                    .set(SysTenant::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysTenant::getUpdateAt, LocalDateTime.now()));
            updatedCount++;

            // 利用path前缀匹配更新所有子孙节点的路径 替换旧路径前缀为新路径前缀
            if (oldPathPrefix != null) {
                List<SysTenant> descendants = this.list(new LambdaQueryWrapper<SysTenant>()
                        .likeRight(SysTenant::getPath, oldPathPrefix + "/")
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

}
