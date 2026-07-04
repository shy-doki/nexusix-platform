package com.shy.nexusix.iam.service.impl;

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
import com.shy.nexusix.iam.converter.SysPermConverter;
import com.shy.nexusix.iam.entity.SysPerm;
import com.shy.nexusix.iam.mapper.SysPermMapper;
import com.shy.nexusix.iam.rto.SysPermAddRTO;
import com.shy.nexusix.iam.rto.SysPermQueryRTO;
import com.shy.nexusix.iam.rto.SysPermUpdateRTO;
import com.shy.nexusix.iam.service.ISysPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.iam.vo.SysPermCommonVO;
import com.shy.nexusix.iam.vo.SysPermDetailVO;
import com.shy.nexusix.iam.vo.SysPermTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>系统权限服务实现类</p>
 *
 * @author shy
 */
@Service
public class SysPermServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements ISysPermService {

    @Autowired
    private SysPermConverter sysPermConverter;

    /**
     * <p>获取查询操作的可操作字段</p>
     *
     * @return 可操作字段列表，null表示无限制
     */
    private List<String> getQueryOperableFields() {
        // 暂无IAM字段权限配置，返回null表示无限制
        return null;
    }

    /**
     * <p>获取指定操作类型的字段权限</p>
     *
     * @param operationType 操作类型："query"、"create"、"update"
     * @return 可操作字段列表，null表示无权限或无限制
     * @throws BusinessException 用户上下文为空或操作类型不支持时抛出
     */
    private List<String> getTableFieldPermission(String operationType) {
        // 暂无IAM字段权限配置，返回null表示无限制
        return null;
    }

    /**
     * <p>查询权限列表</p>
     *
     * @return 权限通用VO列表，封装用户有权查看的权限信息
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysPermCommonVO> queryPermList() {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询条件 仅选择用户有权限查看的列，并排除已删除的权限记录
        LambdaQueryWrapper<SysPerm> wrapper = new LambdaQueryWrapper<SysPerm>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysPerm.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        List<SysPerm> permList = this.list(wrapper);
        // 通过 MapStruct 转换器将实体列表转换为 VO 列表
        return sysPermConverter.entityListToCommonVoList(permList);

    }

    /**
     * <p>分页查询权限列表</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的权限通用VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysPermCommonVO> queryPermPage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建分页查询条件 仅选择用户有权限查看的列，并排除已删除的权限记录
        LambdaQueryWrapper<SysPerm> wrapper = new LambdaQueryWrapper<SysPerm>();

        // 如果有字段级权限限制，则只选择可操作字段
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysPerm.class, entity -> visibleFields.contains(entity.getProperty()));
        }

        wrapper.eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 执行分页查询
        IPage<SysPerm> entityPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysPermCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysPermConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询权限树形结构</p>
     *
     * @return 权限树形VO列表，包含完整的层级关系
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public List<SysPermTreeVO> queryPermTreeList() {

        // 获取查询操作的字段权限
        List<String> visibleFields = getTableFieldPermission("query");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询权限信息");
        }

        // 构建查询字段集合 合并用户可见字段和树形查询业务必要字段
        // 创建新集合 不修改原始visibleFields，避免污染Session缓存中的权限数据
        Set<String> queryFields = new HashSet<>(visibleFields);
        queryFields.addAll(GlobalConstant.FieldPerm.PERM_TREE_MANDATORY_FIELDS);

        // 查询所有未删除的权限 选择用户有权限查看的列 + 业务必要字段
        LambdaQueryWrapper<SysPerm> wrapper = new LambdaQueryWrapper<SysPerm>()
                .select(SysPerm.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .orderByAsc(SysPerm::getPath);
        List<SysPerm> allPerms = this.list(wrapper);

        // 通过 MapStruct 转换器将实体列表转换为 TreeVO 列表
        List<SysPermTreeVO> treeVOList = sysPermConverter.entityListToTreeVoList(allPerms);

        // 构建ID到permCode的映射 用于通过parentCode(父权限ID)查找父权限的permCode
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysPerm perm : allPerms) {
            idToCodeMap.put(String.valueOf(perm.getId()), perm.getPermCode());
        }

        // 构建permCode到TreeVO的映射 用于O(1)时间查找父节点
        Map<String, SysPermTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysPermTreeVO treeVO : treeVOList) {
            treeVO.setChildPerm(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getPermCode(), treeVO);
        }

        // 构建树形结构 遍历所有TreeVO 将子节点挂载到对应的父节点上
        List<SysPermTreeVO> rootList = new ArrayList<>();
        for (SysPermTreeVO treeVO : treeVOList) {
            String parentCode = treeVO.getParentCode();
            if (parentCode == null || "0".equals(parentCode)) {
                // 无父权限或父权限ID为0 作为根节点
                rootList.add(treeVO);
            } else {
                // 通过parentCode(父权限ID)查找父权限的permCode 再通过permCode查找父TreeVO
                String parentPermCode = idToCodeMap.get(parentCode);
                if (parentPermCode != null) {
                    SysPermTreeVO parent = codeToTreeVOMap.get(parentPermCode);
                    if (parent != null) {
                        parent.getChildPerm().add(treeVO);
                    }
                } else {
                    // 父权限不存在时作为根节点处理
                    rootList.add(treeVO);
                }
            }
        }

        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        sysPermConverter.filterTreeVoListByVisibleFields(rootList, visibleFields);
        return rootList;

    }

    /**
     * <p>分页查询权限树形结构</p>
     *
     * @param page 分页参数，包含页码和每页数量
     * @return 分页后的权限树形VO列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysPermTreeVO> queryPermTreePage(PageCommonRTO page) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建查询字段集合：合并用户可见字段和树形查询业务必要字段
        // 创建新集合，不修改原始visibleFields，避免污染Session缓存中的权限数据
        Set<String> queryFieldSet = new HashSet<>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            queryFieldSet.addAll(visibleFields);
        }
        queryFieldSet.addAll(GlobalConstant.FieldPerm.PERM_TREE_MANDATORY_FIELDS);
        Set<String> queryFields = queryFieldSet;

        // 先查询根节点总数用于分页
        LambdaQueryWrapper<SysPerm> rootCountWrapper = new LambdaQueryWrapper<SysPerm>()
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysPerm::getParentId).or().eq(SysPerm::getParentId, 0L));
        long rootTotal = this.count(rootCountWrapper);

        // 分页查询根节点 选择用户有权限查看的列 + 业务必要字段
        LambdaQueryWrapper<SysPerm> rootWrapper = new LambdaQueryWrapper<SysPerm>()
                .select(SysPerm.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.isNull(SysPerm::getParentId).or().eq(SysPerm::getParentId, "0"))
                .orderByAsc(SysPerm::getPath);
        IPage<SysPerm> rootPage = this.page(new Page<>(page.getPageNum(), page.getPageSize()), rootWrapper);

        // 收集所有根节点的path前缀 用于一次性查询所有子节点
        // 归一化path：移除末尾"/" 避免与后续拼接的"/"产生双斜杠导致LIKE匹配失败
        List<String> rootPaths = new ArrayList<>();
        List<Long> rootIds = new ArrayList<>();
        for (SysPerm root : rootPage.getRecords()) {
            if (root.getPath() != null && !root.getPath().isEmpty()) {
                rootPaths.add(normalizePathPrefix(root.getPath()));
            }
            rootIds.add(root.getId());
        }

        // 查询所有子节点 如果没有根节点则跳过
        List<SysPerm> allChildren = new ArrayList<>();
        if (!rootPaths.isEmpty()) {
            LambdaQueryWrapper<SysPerm> childWrapper = new LambdaQueryWrapper<SysPerm>()
                    .select(SysPerm.class, entity -> queryFields.contains(entity.getProperty()))
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .notIn(SysPerm::getId, rootIds)
                    .and(w -> {
                        // 利用path前缀匹配查询所有子节点 减少多次查询
                        for (int i = 0; i < rootPaths.size(); i++) {
                            String pathPrefix = rootPaths.get(i);
                            if (i == 0) {
                                w.likeRight(SysPerm::getPath, pathPrefix + "/");
                            } else {
                                w.or().likeRight(SysPerm::getPath, pathPrefix + "/");
                            }
                        }
                    })
                    .orderByAsc(SysPerm::getPath);
            allChildren = this.list(childWrapper);
        }

        // 合并根节点和子节点 并构建ID到permCode的映射
        List<SysPerm> allPerms = new ArrayList<>(rootPage.getRecords());
        allPerms.addAll(allChildren);
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysPerm perm : allPerms) {
            idToCodeMap.put(String.valueOf(perm.getId()), perm.getPermCode());
        }

        // 转换为TreeVO列表
        List<SysPermTreeVO> treeVOList = sysPermConverter.entityListToTreeVoList(allPerms);
        Map<String, SysPermTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysPermTreeVO treeVO : treeVOList) {
            treeVO.setChildPerm(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getPermCode(), treeVO);
        }

        // 构建树形结构
        List<SysPermTreeVO> rootList = new ArrayList<>();
        for (SysPermTreeVO treeVO : treeVOList) {
            String parentCode = treeVO.getParentCode();
            if (parentCode == null || "0".equals(parentCode)) {
                rootList.add(treeVO);
            } else {
                String parentPermCode = idToCodeMap.get(parentCode);
                if (parentPermCode != null) {
                    SysPermTreeVO parent = codeToTreeVOMap.get(parentPermCode);
                    if (parent != null) {
                        parent.getChildPerm().add(treeVO);
                    } else {
                        // 父权限不在当前结果集中时作为根节点处理
                        rootList.add(treeVO);
                    }
                } else {
                    // 父权限不存在时作为根节点处理
                    rootList.add(treeVO);
                }
            }
        }

        // 构建分页返回结果
        IPage<SysPermTreeVO> voPage = new Page<>(rootPage.getCurrent(), rootPage.getSize(), rootTotal);
        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        sysPermConverter.filterTreeVoListByVisibleFields(rootList, visibleFields);
        voPage.setRecords(rootList);
        return voPage;

    }

    /**
     * <p>查询指定权限的树形结构</p>
     *
     * @param id 权限ID，用于定位要查询的权限节点
     * @return 以指定权限为根的树形结构
     * @throws BusinessException 权限不存在时抛出业务异常
     */
    @Override
    public SysPermTreeVO queryPermTree(String id) {

        // 安全校验 权限ID合法性
        if (id == null || id.trim().isEmpty()) {
            throw new BusinessException("权限ID不能为空");
        }

        // 获取查询操作的字段权限
        List<String> visibleFields = getTableFieldPermission("query");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权查询权限信息");
        }

        // 构建查询字段集合：合并用户可见字段和树形查询业务必要字段
        // 创建新集合，不修改原始visibleFields，避免污染Session缓存中的权限数据
        Set<String> queryFields = new HashSet<>(visibleFields);
        queryFields.addAll(GlobalConstant.FieldPerm.PERM_TREE_MANDATORY_FIELDS);

        // 查询指定权限
        LambdaQueryWrapper<SysPerm> targetWrapper = new LambdaQueryWrapper<SysPerm>()
                .select(SysPerm.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysPerm::getId, id)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        SysPerm targetPerm = this.getOne(targetWrapper);
        if (targetPerm == null) {
            throw new BusinessException("权限不存在");
        }

        // 利用path前缀匹配查询所有子节点 一次查询获取整棵子树
        // 归一化path：移除末尾"/" 避免与后续拼接的"/"产生双斜杠导致LIKE匹配失败
        String targetPathPrefix = normalizePathPrefix(targetPerm.getPath());
        LambdaQueryWrapper<SysPerm> childWrapper = new LambdaQueryWrapper<SysPerm>()
                .select(SysPerm.class, entity -> queryFields.contains(entity.getProperty()))
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .and(w -> w.eq(SysPerm::getId, id)
                        .or()
                        .likeRight(SysPerm::getPath, targetPathPrefix + "/"))
                .orderByAsc(SysPerm::getPath);
        List<SysPerm> subTreePerms = this.list(childWrapper);

        // 构建ID到permCode的映射
        Map<String, String> idToCodeMap = new HashMap<>();
        for (SysPerm perm : subTreePerms) {
            idToCodeMap.put(String.valueOf(perm.getId()), perm.getPermCode());
        }

        // 转换为TreeVO列表
        List<SysPermTreeVO> treeVOList = sysPermConverter.entityListToTreeVoList(subTreePerms);
        Map<String, SysPermTreeVO> codeToTreeVOMap = new HashMap<>();
        for (SysPermTreeVO treeVO : treeVOList) {
            treeVO.setChildPerm(new ArrayList<>());
            codeToTreeVOMap.put(treeVO.getPermCode(), treeVO);
        }

        // 构建树形结构 统一处理所有节点的父子关系
        SysPermTreeVO rootNode = null;
        for (SysPermTreeVO treeVO : treeVOList) {
            // 记录根节点（指定权限自身）
            if (treeVO.getPermCode().equals(targetPerm.getPermCode())) {
                rootNode = treeVO;
            }
            String parentCode = treeVO.getParentCode();
            // 仅当parentCode有效且不为"0"时，通过parentCode查找父TreeVO并挂载
            if (parentCode != null && !"0".equals(parentCode)) {
                String parentPermCode = idToCodeMap.get(parentCode);
                if (parentPermCode != null) {
                    SysPermTreeVO parent = codeToTreeVOMap.get(parentPermCode);
                    if (parent != null) {
                        parent.getChildPerm().add(treeVO);
                    }
                }
            }
        }

        // 根据用户可操作字段过滤返回数据，仅返回有权限的字段
        if (rootNode != null) {
            sysPermConverter.filterTreeVoByVisibleFields(rootNode, visibleFields);
        }
        return rootNode;

    }

    /**
     * <p>条件查询权限列表</p>
     *
     * @param queryParam 查询条件，包含权限编码、名称、类型、状态等筛选条件
     * @return 满足条件的权限分页列表
     * @throws BusinessException 用户上下文为空、字段权限配置异常时抛出业务异常
     */
    @Override
    public IPage<SysPermCommonVO> queryPerm(SysPermQueryRTO queryParam) {

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 构建条件查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysPerm> wrapper = new LambdaQueryWrapper<SysPerm>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysPerm.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        // 权限编码模糊匹配
        if (queryParam.getPermCode() != null && !queryParam.getPermCode().isEmpty()) {
            wrapper.like(SysPerm::getPermCode, queryParam.getPermCode());
        }
        // 权限名称模糊匹配
        if (queryParam.getPermName() != null && !queryParam.getPermName().isEmpty()) {
            wrapper.like(SysPerm::getPermName, queryParam.getPermName());
        }
        // 权限类型精确匹配
        if (queryParam.getPermType() != null && !queryParam.getPermType().isEmpty()) {
            wrapper.eq(SysPerm::getPermType, queryParam.getPermType());
        }
        // 资源类型精确匹配
        if (queryParam.getResourceType() != null && !queryParam.getResourceType().isEmpty()) {
            wrapper.eq(SysPerm::getResourceType, queryParam.getResourceType());
        }
        // 资源方法精确匹配
        if (queryParam.getResourceMethod() != null && !queryParam.getResourceMethod().isEmpty()) {
            wrapper.eq(SysPerm::getResourceMethod, queryParam.getResourceMethod());
        }
        // 状态精确匹配
        if (queryParam.getStatus() != null && !queryParam.getStatus().isEmpty()) {
            wrapper.eq(SysPerm::getStatus, queryParam.getStatus());
        }
        // 创建时间范围查询
        TimeRangeCommonRTO createTimeRange = queryParam.getCreateTimeRange();
        if (createTimeRange != null) {
            if (createTimeRange.getStartTime() != null) {
                wrapper.ge(SysPerm::getCreateAt, createTimeRange.getStartTime());
            }
            if (createTimeRange.getEndTime() != null) {
                wrapper.le(SysPerm::getCreateAt, createTimeRange.getEndTime());
            }
        }
        // 更新时间范围查询
        TimeRangeCommonRTO updateTimeRange = queryParam.getUpdateTimeRange();
        if (updateTimeRange != null) {
            if (updateTimeRange.getStartTime() != null) {
                wrapper.ge(SysPerm::getUpdateAt, updateTimeRange.getStartTime());
            }
            if (updateTimeRange.getEndTime() != null) {
                wrapper.le(SysPerm::getUpdateAt, updateTimeRange.getEndTime());
            }
        }

        // 执行分页查询
        IPage<SysPerm> entityPage = this.page(new Page<>(queryParam.getPageNum(), queryParam.getPageSize()), wrapper);

        // 构建VO分页对象 保留原始分页信息
        IPage<SysPermCommonVO> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        // 通过 MapStruct 转换器将实体分页记录转换为 VO 列表
        voPage.setRecords(sysPermConverter.entityListToCommonVoList(entityPage.getRecords()));
        return voPage;

    }

    /**
     * <p>查询权限详情</p>
     *
     * @param permCode 权限编码，用于定位唯一权限
     * @return 权限详情VO，包含完整的权限信息
     * @throws BusinessException 权限不存在时抛出业务异常
     */
    @Override
    public SysPermDetailVO queryPermDetail(String permCode) {

        // 参数校验 权限编码不能为空
        if (permCode == null || permCode.trim().isEmpty()) {
            throw new BusinessException("权限编码不能为空");
        }

        // 使用工具类获取字段权限
        List<String> visibleFields = getQueryOperableFields();

        // 根据权限编码查询 仅选择用户有权限查看的列
        LambdaQueryWrapper<SysPerm> wrapper = new LambdaQueryWrapper<SysPerm>();
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysPerm.class, entity -> visibleFields.contains(entity.getProperty()));
        }
        wrapper.eq(SysPerm::getPermCode, permCode)
               .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());

        SysPerm perm = this.getOne(wrapper);
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }

        // 通过 MapStruct 转换器将实体转换为详情VO
        return sysPermConverter.toDetailVO(perm);

    }

    /**
     * <p>新增权限</p>
     *
     * @param addParam 新增权限信息
     * @return 新增结果行数
     * @throws BusinessException 权限编码已存在、父权限不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addPerm(SysPermAddRTO addParam) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增权限");
        }

        // 校验权限编码唯一性
        LambdaQueryWrapper<SysPerm> codeCheckWrapper = new LambdaQueryWrapper<SysPerm>()
                .eq(SysPerm::getPermCode, addParam.getPermCode())
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
        if (this.count(codeCheckWrapper) > 0) {
            throw new BusinessException("权限编码已存在");
        }

        // 查询父权限信息 用于计算path和校验父权限存在性
        SysPerm parentPerm = null;
        if (addParam.getParentCode() != null && !"0".equals(addParam.getParentCode())) {
            parentPerm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                    .eq(SysPerm::getId, addParam.getParentCode())
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (parentPerm == null) {
                throw new BusinessException("父权限不存在");
            }
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysPerm entity = sysPermConverter.toEntityFromAdd(addParam);

        // 计算物化路径 如果RTO中未指定path则自动计算
        if (entity.getPath() == null || entity.getPath().isEmpty()) {
            if (parentPerm != null) {
                // 子权限路径 = 父权限路径 + / + 当前权限编码
                entity.setPath(parentPerm.getPath() + "/" + addParam.getPermCode());
            } else {
                // 根权限路径 = 自身权限编码
                entity.setPath(addParam.getPermCode());
            }
        }

        // 计算层级深度
        if (entity.getLevel() == null) {
            if (parentPerm != null) {
                entity.setLevel(parentPerm.getLevel() + 1);
            } else {
                entity.setLevel(1);
            }
        }

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
        if (!visibleFields.contains("permName")) entity.setPermName(null);
        if (!visibleFields.contains("permDesc")) entity.setPermDesc(null);
        if (!visibleFields.contains("permType")) entity.setPermType(null);
        if (!visibleFields.contains("resourceType")) entity.setResourceType(null);
        if (!visibleFields.contains("resourcePath")) entity.setResourcePath(null);
        if (!visibleFields.contains("resourceMethod")) entity.setResourceMethod(null);
        if (!visibleFields.contains("icon")) entity.setIcon(null);
        if (!visibleFields.contains("sortOrder")) entity.setSortOrder(null);
        if (!visibleFields.contains("isVisible")) entity.setIsVisible(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

        // 保存权限信息
        this.save(entity);
        return 1;

    }

    /**
     * <p>修改权限</p>
     *
     * @param updateParam 修改权限信息
     * @return 修改结果行数
     * @throws BusinessException 权限不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePerm(SysPermUpdateRTO updateParam) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改权限");
        }

        // 查询待更新的权限 确保权限存在且未删除
        SysPerm existingPerm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                .eq(SysPerm::getPermCode, updateParam.getPermCode())
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (existingPerm == null) {
            throw new BusinessException("权限不存在");
        }

        // 通过 MapStruct 转换器将RTO转换为实体
        SysPerm entity = sysPermConverter.toEntityFromUpdate(updateParam);

        // 设置实体ID用于更新条件
        entity.setId(existingPerm.getId());

        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);
        if (isSuperAdmin) {
            // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
            if (entity.getCreateBy() == null) {
                entity.setCreateBy(existingPerm.getCreateBy());
            }
            if (entity.getCreateAt() == null) {
                entity.setCreateAt(existingPerm.getCreateAt());
            }
            if (entity.getUpdateBy() == null) {
                entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
            }
            if (entity.getUpdateAt() == null) {
                entity.setUpdateAt(LocalDateTime.now());
            }
            // isDeleted未填写时保留原值
            if (entity.getIsDeleted() == null) {
                entity.setIsDeleted(existingPerm.getIsDeleted());
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
        if (!visibleFields.contains("permName")) entity.setPermName(null);
        if (!visibleFields.contains("permDesc")) entity.setPermDesc(null);
        if (!visibleFields.contains("permType")) entity.setPermType(null);
        if (!visibleFields.contains("resourceType")) entity.setResourceType(null);
        if (!visibleFields.contains("resourcePath")) entity.setResourcePath(null);
        if (!visibleFields.contains("resourceMethod")) entity.setResourceMethod(null);
        if (!visibleFields.contains("icon")) entity.setIcon(null);
        if (!visibleFields.contains("sortOrder")) entity.setSortOrder(null);
        if (!visibleFields.contains("isVisible")) entity.setIsVisible(null);
        if (!visibleFields.contains("status")) entity.setStatus(null);
        if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

        // 权限编码和path不可修改 清除这些字段
        entity.setPermCode(null);
        entity.setPath(null);
        entity.setId(existingPerm.getId());

        // 执行更新操作 使用updateById仅更新非null字段
        this.updateById(entity);
        return 1;

    }

    /**
     * <p>更新权限状态</p>
     *
     * @param id 权限ID
     * @param status 目标状态（ENABLED/DISABLED）
     * @return 更新结果行数
     * @throws BusinessException 权限不存在或状态无效时抛出业务异常
     */
    @Override
    public Integer updatePermStatus(String id, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改权限状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.PermStatus.isValidCode(status)) {
            throw new BusinessException("无效的权限状态");
        }

        // 查询待更新状态的权限
        SysPerm perm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                .eq(SysPerm::getId, id)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }

        // 校验权限是否已处于目标状态
        if (status.equals(perm.getStatus())) {
            throw new BusinessException("权限已处于该状态，无需重复操作");
        }

        // 更新当前权限状态 审核字段updateBy和updateAt由系统自动设置
        // 停用时记录禁用原因，启用时清除禁用原因
        LambdaUpdateWrapper<SysPerm> updateWrapper = new LambdaUpdateWrapper<SysPerm>()
                .eq(SysPerm::getId, id)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysPerm::getStatus, status)
                .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysPerm::getUpdateAt, LocalDateTime.now());
        if (GlobalEnum.PermStatus.DISABLED.getCode().equals(status)) {
            updateWrapper.set(SysPerm::getDisableReason, "ADMIN_DISABLE");
        } else if (GlobalEnum.PermStatus.ENABLED.getCode().equals(status)) {
            updateWrapper.set(SysPerm::getDisableReason, null);
        }
        this.update(updateWrapper);

        int updatedCount = 1;

        // 如果是停用操作 利用path前缀匹配级联停用所有子权限
        if (GlobalEnum.PermStatus.DISABLED.getCode().equals(status) && perm.getPath() != null) {
            LambdaUpdateWrapper<SysPerm> childUpdateWrapper = new LambdaUpdateWrapper<SysPerm>()
                    .likeRight(SysPerm::getPath, normalizePathPrefix(perm.getPath()) + "/")
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .ne(SysPerm::getStatus, GlobalEnum.PermStatus.DISABLED.getCode())
                    .set(SysPerm::getStatus, status)
                    .set(SysPerm::getDisableReason, "PARENT_CASCADE:" + id)
                    .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysPerm::getUpdateAt, LocalDateTime.now());
            updatedCount += this.update(childUpdateWrapper) ? 1 : 0;
        }
        return updatedCount;

    }

    /**
     * <p>删除权限</p>
     *
     * @param id 权限ID
     * @return 删除结果行数
     * @throws BusinessException 权限不存在、存在子权限或字段权限不足时抛出业务异常
     */
    @Override
    public Integer deletePerm(String id) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除权限");
        }

        // 查询待删除的权限
        SysPerm perm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                .eq(SysPerm::getId, id)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
        if (perm == null) {
            throw new BusinessException("权限不存在");
        }

        // 校验是否存在子权限 利用path前缀匹配查询
        if (perm.getPath() != null) {
            long childCount = this.count(new LambdaQueryWrapper<SysPerm>()
                    .likeRight(SysPerm::getPath, normalizePathPrefix(perm.getPath()) + "/")
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (childCount > 0) {
                throw new BusinessException("该权限下存在子权限，无法删除");
            }
        }

        // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
        this.update(new LambdaUpdateWrapper<SysPerm>()
                .eq(SysPerm::getId, id)
                .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                .set(SysPerm::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                .set(SysPerm::getDeletedAt, LocalDateTime.now())
                .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                .set(SysPerm::getUpdateAt, LocalDateTime.now()));
        return 1;

    }

    /**
     * <p>批量新增权限</p>
     *
     * @param addParamList 批量新增权限信息集合
     * @return 成功新增的权限数量
     * @throws BusinessException 任一权限编码重复、父权限不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAddPerm(List<SysPermAddRTO> addParamList) {

        // 获取创建操作的字段权限
        List<String> visibleFields = getTableFieldPermission("create");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权新增权限");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 通过 MapStruct 转换器批量将RTO列表转换为实体列表
        List<SysPerm> entityList = sysPermConverter.addRTOListToEntityList(addParamList);

        // 批量内重复编码检查 同一批次中权限编码不得重复
        Set<String> batchCodeSet = new HashSet<>();
        for (SysPerm entity : entityList) {
            if (!batchCodeSet.add(entity.getPermCode())) {
                throw new BusinessException("批量新增中存在重复的权限编码: " + entity.getPermCode());
            }
        }

        // 遍历处理每个权限实体 校验编码唯一性、计算path、设置默认值、清除不可操作字段
        for (SysPerm entity : entityList) {
            // 校验权限编码唯一性
            long codeCount = this.count(new LambdaQueryWrapper<SysPerm>()
                    .eq(SysPerm::getPermCode, entity.getPermCode())
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (codeCount > 0) {
                throw new BusinessException("权限编码已存在: " + entity.getPermCode());
            }

            // 查询父权限信息 用于计算path
            if (entity.getParentId() != null && !"0".equals(String.valueOf(entity.getParentId()))) {
                SysPerm parentPerm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                        .eq(SysPerm::getId, entity.getParentId())
                        .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (parentPerm == null) {
                    throw new BusinessException("父权限不存在: " + entity.getParentId());
                }
                // 计算物化路径
                if (entity.getPath() == null || entity.getPath().isEmpty()) {
                    entity.setPath(parentPerm.getPath() + "/" + entity.getPermCode());
                }
                // 计算层级深度
                if (entity.getLevel() == null) {
                    entity.setLevel(parentPerm.getLevel() + 1);
                }
            } else {
                // 根权限路径 = 自身权限编码
                if (entity.getPath() == null || entity.getPath().isEmpty()) {
                    entity.setPath(entity.getPermCode());
                }
                // 根权限层级深度为1
                if (entity.getLevel() == null) {
                    entity.setLevel(1);
                }
            }

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
            if (!visibleFields.contains("permName")) entity.setPermName(null);
            if (!visibleFields.contains("permDesc")) entity.setPermDesc(null);
            if (!visibleFields.contains("permType")) entity.setPermType(null);
            if (!visibleFields.contains("resourceType")) entity.setResourceType(null);
            if (!visibleFields.contains("resourcePath")) entity.setResourcePath(null);
            if (!visibleFields.contains("resourceMethod")) entity.setResourceMethod(null);
            if (!visibleFields.contains("icon")) entity.setIcon(null);
            if (!visibleFields.contains("sortOrder")) entity.setSortOrder(null);
            if (!visibleFields.contains("isVisible")) entity.setIsVisible(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);
        }

        // 批量保存所有权限
        this.saveBatch(entityList);
        return entityList.size();

    }

    /**
     * <p>批量修改权限</p>
     *
     * @param updateParamList 批量修改权限信息集合
     * @return 成功修改的权限数量
     * @throws BusinessException 任一权限不存在或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePerm(List<SysPermUpdateRTO> updateParamList) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || visibleFields.isEmpty()) {
            throw new BusinessException("无权修改权限");
        }

        // 审核字段权限控制 通过Sa-Token判断当前用户是否为超级管理员
        boolean isSuperAdmin = StpUtil.hasRole(GlobalConstant.Role.SUPER_ADMIN_ROLE);

        // 遍历处理每个权限更新
        for (SysPermUpdateRTO updateParam : updateParamList) {
            // 查询待更新的权限 确保权限存在且未删除
            SysPerm existingPerm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                    .eq(SysPerm::getPermCode, updateParam.getPermCode())
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (existingPerm == null) {
                throw new BusinessException("权限不存在: " + updateParam.getPermCode());
            }

            // 通过 MapStruct 转换器将RTO转换为实体
            SysPerm entity = sysPermConverter.toEntityFromUpdate(updateParam);
            entity.setId(existingPerm.getId());

            // 审核字段权限控制
            if (isSuperAdmin) {
                // 超级管理员：若明确填写了审核字段值则以填写值为准，若未填写则保留原值或自动应用默认值
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(existingPerm.getCreateBy());
                }
                if (entity.getCreateAt() == null) {
                    entity.setCreateAt(existingPerm.getCreateAt());
                }
                if (entity.getUpdateBy() == null) {
                    entity.setUpdateBy(Long.valueOf(StpUtil.getLoginIdAsString()));
                }
                if (entity.getUpdateAt() == null) {
                    entity.setUpdateAt(LocalDateTime.now());
                }
                if (entity.getIsDeleted() == null) {
                    entity.setIsDeleted(existingPerm.getIsDeleted());
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
            if (!visibleFields.contains("permName")) entity.setPermName(null);
            if (!visibleFields.contains("permDesc")) entity.setPermDesc(null);
            if (!visibleFields.contains("permType")) entity.setPermType(null);
            if (!visibleFields.contains("resourceType")) entity.setResourceType(null);
            if (!visibleFields.contains("resourcePath")) entity.setResourcePath(null);
            if (!visibleFields.contains("resourceMethod")) entity.setResourceMethod(null);
            if (!visibleFields.contains("icon")) entity.setIcon(null);
            if (!visibleFields.contains("sortOrder")) entity.setSortOrder(null);
            if (!visibleFields.contains("isVisible")) entity.setIsVisible(null);
            if (!visibleFields.contains("status")) entity.setStatus(null);
            if (!visibleFields.contains("disableReason")) entity.setDisableReason(null);

            // 权限编码和path不可修改
            entity.setPermCode(null);
            entity.setPath(null);

            // 执行更新
            this.updateById(entity);
        }
        return updateParamList.size();

    }

    /**
     * <p>批量更新权限状态</p>
     *
     * @param ids 权限ID集合
     * @param status 目标状态
     * @return 更新结果行数
     * @throws BusinessException 状态无效或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdatePermStatus(List<String> ids, String status) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("status")) {
            throw new BusinessException("无权修改权限状态字段");
        }

        // 校验状态值合法性
        if (!GlobalEnum.PermStatus.isValidCode(status)) {
            throw new BusinessException("无效的权限状态");
        }

        int totalUpdated = 0;

        // 遍历每个权限ID 更新状态
        for (String id : ids) {
            // 查询权限信息
            SysPerm perm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                    .eq(SysPerm::getId, id)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (perm == null) {
                continue;
            }

            // 校验权限是否已处于目标状态
            if (status.equals(perm.getStatus())) {
                continue;
            }

            // 更新当前权限状态 审核字段updateBy和updateAt由系统自动设置
            // 停用时记录禁用原因，启用时清除禁用原因
            LambdaUpdateWrapper<SysPerm> batchUpdateWrapper = new LambdaUpdateWrapper<SysPerm>()
                    .eq(SysPerm::getId, id)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysPerm::getStatus, status)
                    .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysPerm::getUpdateAt, LocalDateTime.now());
            if (GlobalEnum.PermStatus.DISABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysPerm::getDisableReason, "ADMIN_DISABLE");
            } else if (GlobalEnum.PermStatus.ENABLED.getCode().equals(status)) {
                batchUpdateWrapper.set(SysPerm::getDisableReason, null);
            }
            this.update(batchUpdateWrapper);
            totalUpdated++;

            // 如果是停用操作 利用path前缀匹配级联停用所有子权限
            if (GlobalEnum.PermStatus.DISABLED.getCode().equals(status) && perm.getPath() != null) {
                this.update(new LambdaUpdateWrapper<SysPerm>()
                        .likeRight(SysPerm::getPath, normalizePathPrefix(perm.getPath()) + "/")
                        .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                        .ne(SysPerm::getStatus, GlobalEnum.PermStatus.DISABLED.getCode())
                        .set(SysPerm::getStatus, status)
                        .set(SysPerm::getDisableReason, "PARENT_CASCADE:" + id)
                        .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                        .set(SysPerm::getUpdateAt, LocalDateTime.now()));
            }
        }
        return totalUpdated;

    }

    /**
     * <p>批量删除权限</p>
     *
     * @param ids 权限ID集合
     * @return 删除结果行数
     * @throws BusinessException 存在子权限或字段权限不足时抛出业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeletePerm(List<String> ids) {

        // 获取更新操作的字段权限
        List<String> visibleFields = getTableFieldPermission("update");
        if (visibleFields == null || !visibleFields.contains("isDeleted")) {
            throw new BusinessException("无权删除权限");
        }

        int totalDeleted = 0;

        // 遍历每个权限ID 执行逻辑删除
        for (String id : ids) {
            // 查询待删除的权限
            SysPerm perm = this.getOne(new LambdaQueryWrapper<SysPerm>()
                    .eq(SysPerm::getId, id)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
            if (perm == null) {
                continue;
            }

            // 校验是否存在子权限
            if (perm.getPath() != null) {
                long childCount = this.count(new LambdaQueryWrapper<SysPerm>()
                        .likeRight(SysPerm::getPath, normalizePathPrefix(perm.getPath()) + "/")
                        .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode()));
                if (childCount > 0) {
                    throw new BusinessException("权限[" + perm.getPermName() + "]下存在子权限，无法删除");
                }
            }

            // 执行逻辑删除 审核字段isDeleted和deletedAt由系统自动设置
            this.update(new LambdaUpdateWrapper<SysPerm>()
                    .eq(SysPerm::getId, id)
                    .eq(SysPerm::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
                    .set(SysPerm::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
                    .set(SysPerm::getDeletedAt, LocalDateTime.now())
                    .set(SysPerm::getUpdateBy, StpUtil.getLoginIdAsString())
                    .set(SysPerm::getUpdateAt, LocalDateTime.now()));
            totalDeleted++;
        }
        return totalDeleted;

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

}
