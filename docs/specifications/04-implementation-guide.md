# 字段权限与数据范围实施指南

## 📋 概述

本文档提供完整的实施指南，说明如何在新的 Service 中应用字段权限和数据范围控制。

---

## 🎯 适用场景

本指南适用于所有需要实现以下功能的 Service：
- ✅ 查询接口需要字段权限过滤
- ✅ 查询接口需要数据范围控制（ALL/DEPT_AND_SUB/DEPT/SELF）
- ✅ 新增接口需要字段权限校验
- ✅ 更新接口需要字段权限校验

---

## 📦 前置条件

### 1. 工具类已就绪
- ✅ `FieldPermissionHelper` - 字段权限工具类
- ✅ `DataScopeHelper` - 数据范围工具类

### 2. 数据库表字段要求

**字段权限所需字段：**
- 无特殊要求（所有表都支持）

**数据范围所需字段：**
| 字段 | 类型 | 数据范围 | 必需程度 |
|-----|------|---------|---------|
| tenant_id | VARCHAR/BIGINT | ALL | 必需 |
| dept_id | VARCHAR/BIGINT | DEPT | DEPT范围必需 |
| dept_path | VARCHAR | DEPT_AND_SUB | DEPT_AND_SUB范围必需 |
| create_by | BIGINT | SELF | SELF范围必需 |

---

## 🚀 实施步骤

### 步骤1：在 Service 中添加私有方法

```java
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
    implements ISysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysUserConverter sysUserConverter;
    
    /**
     * 获取查询操作的可操作字段
     */
    private List<String> getQueryOperableFields() {
        return FieldPermissionHelper.getQueryOperableFields(GlobalConstant.Table.USER);
    }
    
    /**
     * 获取新增操作的可操作字段
     */
    private List<String> getCreateOperableFields() {
        return FieldPermissionHelper.getCreateOperableFields(GlobalConstant.Table.USER);
    }
    
    /**
     * 获取更新操作的可操作字段
     */
    private List<String> getUpdateOperableFields() {
        return FieldPermissionHelper.getUpdateOperableFields(GlobalConstant.Table.USER);
    }
}
```

---

### 步骤2：实现查询接口（仅字段权限）

**适用场景：** 不需要数据范围控制的查询接口

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // 1. 获取字段权限
    List<String> visibleFields = getQueryOperableFields();
    
    // 2. 构建查询条件
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    
    // 3. 应用字段权限（动态SELECT）
    if (visibleFields != null && !visibleFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> visibleFields.contains(field.getColumn())
        );
    }
    
    // 4. 添加业务查询条件
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    if (StringUtils.isNotBlank(queryRTO.getUserName())) {
        wrapper.like(SysUser::getUserName, queryRTO.getUserName());
    }
    
    // 5. 执行查询
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // 6. 转换VO（二次字段过滤）
    return sysUserConverter.toVOList(userList, visibleFields);
}
```

---

### 步骤3：实现查询接口（字段权限 + 数据范围）

**适用场景：** 需要同时控制字段权限和数据范围的查询接口

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // 1. 获取字段权限
    List<String> visibleFields = getQueryOperableFields();
    
    // 2. 构建查询条件
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    // 3. 租户隔离（强制）
    UserContextDTO userContext = UserContext.getUserContext();
    wrapper.eq(SysUser::getTenantId, 
        userContext.getCurrentTenant().getTenantCode());
    
    // 4. 应用数据范围
    String dataScope = DataScopeHelper.getDataScopeForPermission("PERM_USER_VIEW");
    applyDataScope(wrapper, dataScope);
    
    // 5. 添加业务查询条件
    if (StringUtils.isNotBlank(queryRTO.getUserName())) {
        wrapper.like(SysUser::getUserName, queryRTO.getUserName());
    }
    if (StringUtils.isNotBlank(queryRTO.getStatus())) {
        wrapper.eq(SysUser::getStatus, queryRTO.getStatus());
    }
    
    // 6. 应用字段权限（动态SELECT）
    if (visibleFields != null && !visibleFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> visibleFields.contains(field.getColumn())
        );
    }
    
    // 7. 执行查询
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // 8. 转换VO
    return sysUserConverter.toVOList(userList, visibleFields);
}

/**
 * 应用数据范围
 */
private void applyDataScope(LambdaQueryWrapper<SysUser> wrapper, String dataScope) {
    if ("ALL".equals(dataScope)) {
        // 不添加额外条件，可以查看全租户数据
        return;
    }
    
    if ("DEPT_AND_SUB".equals(dataScope)) {
        // 本部门及下级部门
        String deptPath = DataScopeHelper.getCurrentUserDeptPath();
        wrapper.likeRight(SysUser::getDeptPath, deptPath);
        return;
    }
    
    if ("DEPT".equals(dataScope)) {
        // 仅本部门
        String deptId = DataScopeHelper.getCurrentUserDeptId();
        wrapper.eq(SysUser::getDeptId, deptId);
        return;
    }
    
    if ("SELF".equals(dataScope)) {
        // 仅本人创建的或本人的数据
        Long userId = DataScopeHelper.getCurrentUserId();
        // 优先使用 create_by 字段
        wrapper.eq(SysUser::getCreateBy, userId);
        // 如果没有 create_by，使用 id
        // wrapper.eq(SysUser::getId, userId);
        return;
    }
}
```

---

### 步骤4：实现分页查询接口

```java
@Override
public IPage<SysUserVO> queryUserPage(PageCommonRTO pageParam) {
    // 1. 获取字段权限
    List<String> visibleFields = getQueryOperableFields();
    
    // 2. 构建查询条件
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    // 3. 租户隔离
    UserContextDTO userContext = UserContext.getUserContext();
    wrapper.eq(SysUser::getTenantId, 
        userContext.getCurrentTenant().getTenantCode());
    
    // 4. 应用数据范围
    String dataScope = DataScopeHelper.getDataScopeForPermission("PERM_USER_VIEW");
    applyDataScope(wrapper, dataScope);
    
    // 5. 应用字段权限
    if (visibleFields != null && !visibleFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> visibleFields.contains(field.getColumn())
        );
    }
    
    // 6. 执行分页查询
    IPage<SysUser> entityPage = this.page(
        new Page<>(pageParam.getPageNum(), pageParam.getPageSize()), 
        wrapper
    );
    
    // 7. 构建VO分页对象
    IPage<SysUserVO> voPage = new Page<>(
        entityPage.getCurrent(), 
        entityPage.getSize(), 
        entityPage.getTotal()
    );
    voPage.setRecords(sysUserConverter.toVOList(entityPage.getRecords(), visibleFields));
    
    return voPage;
}
```

---

### 步骤5：在 Controller 实现新增接口字段校验

```java
@RestController
@RequestMapping("/api/users")
public class SysUserController {
    
    @Autowired
    private ISysUserService sysUserService;
    
    @PostMapping
    public ApiResponse<Void> createUser(@RequestBody SysUserAddRTO addRTO) {
        // 1. 获取create字段权限
        List<String> operableFields = FieldPermissionHelper.getCreateOperableFields("sys_user");
        
        // 2. 校验字段权限
        validateCreateFields(addRTO, operableFields);
        
        // 3. 执行创建
        sysUserService.save(addRTO);
        
        return ApiResponse.success();
    }
    
    /**
     * 校验新增字段权限
     */
    private void validateCreateFields(SysUserAddRTO addRTO, List<String> operableFields) {
        // 如果没有字段权限限制，允许所有字段
        if (operableFields == null) {
            return;
        }
        
        // 检查每个字段
        if (addRTO.getStatus() != null && !operableFields.contains("status")) {
            throw new BusinessException("无权设置字段：status");
        }
        
        if (addRTO.getSalary() != null && !operableFields.contains("salary")) {
            throw new BusinessException("无权设置字段：salary");
        }
        
        if (addRTO.getRoleId() != null && !operableFields.contains("role_id")) {
            throw new BusinessException("无权设置字段：role_id");
        }
        
        // ... 检查其他敏感字段
    }
}
```

---

### 步骤6：在 Controller 实现更新接口字段校验

```java
@PutMapping("/{id}")
public ApiResponse<Void> updateUser(
    @PathVariable Long id, 
    @RequestBody SysUserUpdateRTO updateRTO) {
    
    // 1. 获取update字段权限
    List<String> operableFields = FieldPermissionHelper.getUpdateOperableFields("sys_user");
    
    // 2. 校验字段权限
    validateUpdateFields(id, updateRTO, operableFields);
    
    // 3. 执行更新
    sysUserService.updateById(id, updateRTO);
    
    return ApiResponse.success();
}

/**
 * 校验更新字段权限
 */
private void validateUpdateFields(
    Long userId, 
    SysUserUpdateRTO updateRTO, 
    List<String> operableFields) {
    
    // 如果没有字段权限限制，允许所有字段
    if (operableFields == null) {
        return;
    }
    
    // 查询原始数据
    SysUser oldEntity = sysUserService.getById(userId);
    if (oldEntity == null) {
        throw new BusinessException("用户不存在");
    }
    
    // 检测字段变更
    if (updateRTO.getUserName() != null 
        && !updateRTO.getUserName().equals(oldEntity.getUserName())) {
        // user_name被修改了
        if (!operableFields.contains("user_name")) {
            throw new BusinessException("无权更新字段：user_name");
        }
    }
    
    if (updateRTO.getSalary() != null 
        && !updateRTO.getSalary().equals(oldEntity.getSalary())) {
        // salary被修改了
        if (!operableFields.contains("salary")) {
            throw new BusinessException("无权更新字段：salary");
        }
    }
    
    if (updateRTO.getStatus() != null 
        && !updateRTO.getStatus().equals(oldEntity.getStatus())) {
        // status被修改了
        if (!operableFields.contains("status")) {
            throw new BusinessException("无权更新字段：status");
        }
    }
    
    // ... 检查其他字段
}
```

---

## 📝 完整示例

### Service 完整实现

```java
package com.shy.nexusix.iam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.common.helper.DataScopeHelper;
import com.shy.nexusix.common.helper.FieldPermissionHelper;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.core.context.UserContext;
import com.shy.nexusix.core.entity.dto.UserContextDTO;
import com.shy.nexusix.iam.converter.SysUserConverter;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.mapper.SysUserMapper;
import com.shy.nexusix.iam.rto.SysUserQueryRTO;
import com.shy.nexusix.iam.service.ISysUserService;
import com.shy.nexusix.iam.vo.SysUserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 *
 * @author NexusIX
 * @since 2026-06-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
    implements ISysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysUserConverter sysUserConverter;
    
    /**
     * 获取查询操作的可操作字段
     */
    private List<String> getQueryOperableFields() {
        return FieldPermissionHelper.getQueryOperableFields(GlobalConstant.Table.USER);
    }
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 1. 获取字段权限
        List<String> visibleFields = getQueryOperableFields();
        
        // 2. 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 3. 租户隔离
        UserContextDTO userContext = UserContext.getUserContext();
        wrapper.eq(SysUser::getTenantId, 
            userContext.getCurrentTenant().getTenantCode());
        
        // 4. 应用数据范围
        String dataScope = DataScopeHelper.getDataScopeForPermission("PERM_USER_VIEW");
        applyDataScope(wrapper, dataScope);
        
        // 5. 添加业务查询条件
        if (StringUtils.isNotBlank(queryRTO.getUserName())) {
            wrapper.like(SysUser::getUserName, queryRTO.getUserName());
        }
        if (StringUtils.isNotBlank(queryRTO.getStatus())) {
            wrapper.eq(SysUser::getStatus, queryRTO.getStatus());
        }
        
        // 6. 应用字段权限
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, 
                field -> visibleFields.contains(field.getColumn())
            );
        }
        
        // 7. 执行查询
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        
        // 8. 转换VO
        return sysUserConverter.toVOList(userList, visibleFields);
    }
    
    @Override
    public IPage<SysUserVO> queryUserPage(PageCommonRTO pageParam) {
        // 1. 获取字段权限
        List<String> visibleFields = getQueryOperableFields();
        
        // 2. 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 3. 租户隔离
        UserContextDTO userContext = UserContext.getUserContext();
        wrapper.eq(SysUser::getTenantId, 
            userContext.getCurrentTenant().getTenantCode());
        
        // 4. 应用数据范围
        String dataScope = DataScopeHelper.getDataScopeForPermission("PERM_USER_VIEW");
        applyDataScope(wrapper, dataScope);
        
        // 5. 应用字段权限
        if (visibleFields != null && !visibleFields.isEmpty()) {
            wrapper.select(SysUser.class, 
                field -> visibleFields.contains(field.getColumn())
            );
        }
        
        // 6. 执行分页查询
        IPage<SysUser> entityPage = this.page(
            new Page<>(pageParam.getPageNum(), pageParam.getPageSize()), 
            wrapper
        );
        
        // 7. 构建VO分页对象
        IPage<SysUserVO> voPage = new Page<>(
            entityPage.getCurrent(), 
            entityPage.getSize(), 
            entityPage.getTotal()
        );
        voPage.setRecords(
            sysUserConverter.toVOList(entityPage.getRecords(), visibleFields)
        );
        
        return voPage;
    }
    
    /**
     * 应用数据范围
     */
    private void applyDataScope(LambdaQueryWrapper<SysUser> wrapper, String dataScope) {
        if ("ALL".equals(dataScope)) {
            return;
        }
        
        if ("DEPT_AND_SUB".equals(dataScope)) {
            String deptPath = DataScopeHelper.getCurrentUserDeptPath();
            wrapper.likeRight(SysUser::getDeptPath, deptPath);
            return;
        }
        
        if ("DEPT".equals(dataScope)) {
            String deptId = DataScopeHelper.getCurrentUserDeptId();
            wrapper.eq(SysUser::getDeptId, deptId);
            return;
        }
        
        if ("SELF".equals(dataScope)) {
            Long userId = DataScopeHelper.getCurrentUserId();
            wrapper.eq(SysUser::getCreateBy, userId);
            return;
        }
    }
}
```

---

## ✅ 检查清单

实施完成后，请确认以下内容：

### Service 层
- [ ] 添加了 `getQueryOperableFields()` 私有方法
- [ ] 查询方法应用了字段权限（动态SELECT）
- [ ] 查询方法应用了数据范围（如需要）
- [ ] 查询方法添加了租户隔离（如需要）
- [ ] VO转换时传递了 visibleFields 参数

### Controller 层
- [ ] 新增接口添加了字段权限校验
- [ ] 更新接口添加了差异检测+字段权限校验
- [ ] 校验失败时抛出清晰的业务异常

### 数据库
- [ ] 确认表有必需的字段（tenant_id, dept_path, dept_id, create_by）
- [ ] 确认相关索引已创建

---

## 🔍 常见问题

### Q1: 字段权限返回 null 怎么办？
**A:** null 表示无字段限制，应该返回所有字段。代码中使用：
```java
if (visibleFields != null && !visibleFields.isEmpty()) {
    // 应用字段权限
}
// 否则查询所有字段
```

### Q2: 用户没有部门怎么办？
**A:** DEPT 和 DEPT_AND_SUB 范围会抛出 BusinessException。需要在应用数据范围前检查：
```java
if ("DEPT".equals(dataScope) || "DEPT_AND_SUB".equals(dataScope)) {
    // 检查用户是否有部门
    List<UserContextDTO.DeptItem> depts = userContext.getDepts().getCurrent();
    if (depts == null || depts.isEmpty()) {
        throw new BusinessException("当前用户未分配部门，无法查询");
    }
}
```

### Q3: 如何测试字段权限是否生效？
**A:** 查看生成的SQL：
```sql
-- 有字段权限
SELECT user_name, email, phone FROM sys_user WHERE ...

-- 无字段权限
SELECT * FROM sys_user WHERE ...
```

### Q4: 数据范围优先级是什么？
**A:** 用户通过多个角色获得同一权限时，取**最宽松**的数据范围：
ALL > DEPT_AND_SUB > DEPT > SELF

---

## 📚 参考文档

- [字段级权限实现规格](./01-field-permission-spec.md)
- [数据可见范围实现规格](./02-data-scope-spec.md)
- [总体架构文档](./00-overview-spec.md)

---

**文档版本：** v1.0  
**创建日期：** 2026-06-12  
**最后更新：** 2026-06-12  
**适用范围：** 所有需要字段权限和数据范围控制的 Service
