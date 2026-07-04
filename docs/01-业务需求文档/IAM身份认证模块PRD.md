# NexusIX-Platform IAM模块功能补充设计

**文档版本**: v2.0  
**创建日期**: 2026-06-15  
**基于**: nexusix-tenant 模块实现方案  

---

## 2. nexusix-iam (身份认证模块) - 功能补充

### 2.1 模块概述

IAM模块已有基础实现，本文档补充用户管理、角色管理、权限管理的完整接口设计。

**已实现**:
- ✅ AuthController - 登录认证
- ✅ 用户上下文构建逻辑
- ✅ 基础实体和Mapper

**需补充**:
- 用户管理完整CRUD接口
- 角色管理完整CRUD接口
- 权限管理完整CRUD接口
- 用户策略管理接口
- 权限策略管理接口

---

## 2.2 SysUserController (用户管理控制器) - 补充

### 2.2.1 完整接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryUserList()` | GET | `/user/list` | 查询用户列表 | USER:VIEW |
| `queryUserPage()` | GET | `/user/page` | 分页查询用户 | USER:VIEW |
| `queryUserDetail()` | GET | `/user/detail/{code}` | 查询用户详情 | USER:VIEW |
| `addUser()` | POST | `/user/add` | 新增用户 | USER:ADD |
| `updateUser()` | PUT | `/user/update` | 更新用户信息 | USER:EDIT |
| `deleteUser()` | DELETE | `/user/delete/{code}` | 删除用户（逻辑删除） | USER:DELETE |
| `resetPassword()` | POST | `/user/reset-password` | 重置用户密码 | USER:RESET_PWD |
| `changeStatus()` | PUT | `/user/change-status` | 启用/禁用/锁定用户 | USER:CHANGE_STATUS |
| `exportUsers()` | POST | `/user/export` | 导出用户数据（Excel） | USER:EXPORT |
| `importUsers()` | POST | `/user/import` | 导入用户数据（Excel） | USER:IMPORT |
| `queryUserRoles()` | GET | `/user/{code}/roles` | 查询用户角色列表 | USER:VIEW |
| `assignRoles()` | POST | `/user/{code}/roles` | 为用户分配角色 | USER:ASSIGN_ROLES |
| `queryUserPerms()` | GET | `/user/{code}/permissions` | 查询用户权限 | USER:VIEW_PERMS |
| `queryUserDepts()` | GET | `/user/{code}/depts` | 查询用户部门 | USER:VIEW |
| `updateProfile()` | PUT | `/user/profile` | 更新个人信息 | - |
| `updateAvatar()` | POST | `/user/avatar` | 更新头像 | - |
| `changePassword()` | POST | `/user/change-password` | 修改密码 | - |

### 2.2.2 核心接口实现

**接口1: addUser() - 新增用户**

```java
@PostMapping("/add")
@Operation(summary = "新增用户")
@SaCheckPermission("USER:ADD")
@Transactional(rollbackFor = Exception.class)
public ApiResponse addUser(@Valid @RequestBody SysUserAddRTO param) {
    
    // 1. 校验用户名唯一性
    Long existCount = userMapper.selectCount(
        new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUserName, param.getUserName())
            .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    if (existCount > 0) {
        throw new BusinessException("用户名已存在");
    }
    
    // 2. 校验用户编码唯一性
    existCount = userMapper.selectCount(
        new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUserCode, param.getUserCode())
            .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    if (existCount > 0) {
        throw new BusinessException("用户编码已存在");
    }
    
    // 3. 校验邮箱唯一性（如果提供）
    if (StringUtils.isNotBlank(param.getEmail())) {
        existCount = userMapper.selectCount(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, param.getEmail())
                .eq(SysUser::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
        );
        
        if (existCount > 0) {
            throw new BusinessException("邮箱已被使用");
        }
    }
    
    // 4. 转换实体并加密密码
    SysUser user = userConverter.toEntity(param);
    String encryptedPassword = BCryptUtil.encrypt(param.getPassword());
    user.setPassword(encryptedPassword);
    user.setStatus("ENABLED");
    
    // 5. 插入用户
    userMapper.insert(user);
    
    // 6. 创建用户策略（关联部门和角色）
    Long tenantId = TenantContext.getCurrentTenantId();
    
    // 查询部门
    SysDept dept = deptMapper.selectOne(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptCode, param.getDeptCode())
    );
    
    if (dept == null) {
        throw new BusinessException("部门不存在");
    }
    
    // 查询角色
    List<SysRole> roles = roleMapper.selectList(
        new LambdaQueryWrapper<SysRole>()
            .in(SysRole::getRoleCode, param.getRoleCodes())
    );
    
    if (roles.size() != param.getRoleCodes().size()) {
        throw new BusinessException("部分角色不存在");
    }
    
    // 创建用户策略
    for (SysRole role : roles) {
        SysUserPolicy userPolicy = new SysUserPolicy();
        userPolicy.setUserId(user.getId());
        userPolicy.setTenantId(tenantId);
        userPolicy.setDeptId(dept.getId());
        userPolicy.setRoleId(role.getId());
        userPolicy.setIsPrimary(roles.indexOf(role) == 0); // 第一个角色为主角色
        userPolicy.setStatus("ACTIVE");
        userPolicyMapper.insert(userPolicy);
    }
    
    return ApiResponse.success("用户创建成功", user.getId());
}
```

**接口2: exportUsers() - 导出用户数据**

```java
@PostMapping("/export")
@Operation(summary = "导出用户数据")
@SaCheckPermission("USER:EXPORT")
public void exportUsers(
    @Valid @RequestBody SysUserQueryRTO param,
    HttpServletResponse response) throws IOException {
    
    // 1. 查询用户数据
    List<SysUser> userList = queryUsers(param);
    
    // 2. 转换为VO
    List<SysUserExportVO> exportList = userList.stream()
        .map(userConverter::toExportVO)
        .collect(Collectors.toList());
    
    // 3. 使用FastExcel导出
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Content-Disposition", 
        "attachment;filename=" + URLEncoder.encode("用户数据_" + 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
        ".xlsx", "UTF-8"));
    
    try (OutputStream out = response.getOutputStream()) {
        ExcelWriter.create()
            .sheet("用户列表")
            .write(exportList)
            .to(out);
    }
}
```

**接口3: importUsers() - 导入用户数据**

```java
@PostMapping("/import")
@Operation(summary = "导入用户数据")
@SaCheckPermission("USER:IMPORT")
@Transactional(rollbackFor = Exception.class)
public ApiResponse importUsers(@RequestParam("file") MultipartFile file) throws IOException {
    
    if (file.isEmpty()) {
        throw new BusinessException("上传文件为空");
    }
    
    // 1. 解析Excel
    List<SysUserImportVO> importList = ExcelReader.read(file.getInputStream())
        .sheet(0)
        .to(SysUserImportVO.class);
    
    if (importList.isEmpty()) {
        throw new BusinessException("Excel中无有效数据");
    }
    
    // 2. 数据校验
    List<String> errors = new ArrayList<>();
    for (int i = 0; i < importList.size(); i++) {
        SysUserImportVO vo = importList.get(i);
        int row = i + 2; // Excel行号（从第2行开始，第1行是表头）
        
        // 校验必填字段
        if (StringUtils.isBlank(vo.getUserCode())) {
            errors.add("第" + row + "行：用户编码不能为空");
        }
        if (StringUtils.isBlank(vo.getUserName())) {
            errors.add("第" + row + "行：用户名不能为空");
        }
        if (StringUtils.isBlank(vo.getPassword())) {
            errors.add("第" + row + "行：密码不能为空");
        }
        
        // 校验格式
        if (StringUtils.isNotBlank(vo.getEmail()) && 
            !vo.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            errors.add("第" + row + "行：邮箱格式错误");
        }
        
        if (StringUtils.isNotBlank(vo.getPhone()) && 
            !vo.getPhone().matches("^1[3-9]\\d{9}$")) {
            errors.add("第" + row + "行：手机号格式错误");
        }
    }
    
    if (!errors.isEmpty()) {
        return ApiResponse.error(400, "数据校验失败", errors);
    }
    
    // 3. 批量导入
    int successCount = 0;
    List<String> failedList = new ArrayList<>();
    
    for (int i = 0; i < importList.size(); i++) {
        SysUserImportVO vo = importList.get(i);
        int row = i + 2;
        
        try {
            // 转换为AddRTO
            SysUserAddRTO addRTO = new SysUserAddRTO();
            BeanUtils.copyProperties(vo, addRTO);
            
            // 调用添加方法
            addUser(addRTO);
            successCount++;
            
        } catch (Exception e) {
            failedList.add("第" + row + "行：" + e.getMessage());
        }
    }
    
    // 4. 返回结果
    Map<String, Object> result = new HashMap<>();
    result.put("totalCount", importList.size());
    result.put("successCount", successCount);
    result.put("failedCount", failedList.size());
    result.put("failedList", failedList);
    
    return ApiResponse.success("导入完成", result);
}
```

### 2.2.3 RTO设计

#### SysUserAddRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysUserAddRTO {
    
    @NotBlank(message = "用户编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{3,50}$", message = "用户编码格式错误")
    private String userCode;
    
    @NotBlank(message = "用户名不能为空")
    @Length(min = 3, max = 50, message = "用户名长度3-50字符")
    private String userName;
    
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度6-20字符")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", 
             message = "密码必须包含大小写字母和数字")
    private String password;
    
    @Length(max = 100, message = "昵称不能超过100字符")
    private String nickName;
    
    @Length(max = 100, message = "真实姓名不能超过100字符")
    private String realName;
    
    @Email(message = "邮箱格式错误")
    @Length(max = 100, message = "邮箱不能超过100字符")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;
    
    @Pattern(regexp = "^(MALE|FEMALE|UNKNOWN)$", message = "性别值错误")
    private String gender;
    
    @NotBlank(message = "部门编码不能为空")
    private String deptCode;
    
    @NotEmpty(message = "角色列表不能为空")
    @Size(min = 1, max = 10, message = "角色数量1-10个")
    private List<String> roleCodes;
}
```

#### SysUserUpdateRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class SysUserUpdateRTO {
    
    @NotBlank(message = "用户编码不能为空")
    private String userCode;
    
    @Length(max = 100, message = "昵称不能超过100字符")
    private String nickName;
    
    @Length(max = 100, message = "真实姓名不能超过100字符")
    private String realName;
    
    @Email(message = "邮箱格式错误")
    @Length(max = 100, message = "邮箱不能超过100字符")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;
    
    @Pattern(regexp = "^(MALE|FEMALE|UNKNOWN)$", message = "性别值错误")
    private String gender;
    
    private String deptCode;
    
    private List<String> roleCodes;
}
```

#### SysUserResetPasswordRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysUserResetPasswordRTO {
    
    @NotBlank(message = "用户编码不能为空")
    private String userCode;
    
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度6-20字符")
    private String newPassword;
}
```

#### SysUserChangePasswordRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysUserChangePasswordRTO {
    
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度6-20字符")
    private String newPassword;
    
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
```

#### SysUserQueryRTO.java

```java
package com.shy.nexusix.iam.rto;

import lombok.Data;

@Data
public class SysUserQueryRTO {
    
    private String userName;       // 用户名（模糊查询）
    private String nickName;       // 昵称（模糊查询）
    private String email;          // 邮箱
    private String phone;          // 手机号
    private String deptCode;       // 部门编码
    private String roleCode;       // 角色编码
    private String status;         // 状态
    private String gender;         // 性别
    private String createTimeStart; // 创建时间开始
    private String createTimeEnd;   // 创建时间结束
}
```

### 2.2.4 VO设计

#### SysUserCommonVO.java

```java
package com.shy.nexusix.iam.vo;

import lombok.Data;

@Data
public class SysUserCommonVO {
    
    private String userCode;
    private String userName;
    private String nickName;
    private String realName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String gender;
    private String genderDesc;     // 性别描述
    private String deptName;
    private String deptCode;
    private String status;
    private String statusDesc;     // 状态描述
    private String lastLoginTime;
    private String lastLoginIp;
    private String createTime;
    private String updateTime;
}
```

#### SysUserDetailVO.java

```java
package com.shy.nexusix.iam.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysUserDetailVO extends SysUserCommonVO {
    
    private String birthday;
    private String disableReason;
    
    // 部门信息列表
    private List<DeptInfo> depts;
    
    // 角色信息列表
    private List<RoleInfo> roles;
    
    // 权限编码列表（已去重）
    private List<String> permissions;
    
    // 扩展属性
    private Map<String, Object> extAttributes;
    
    // 审计信息
    private String createByName;
    private String updateByName;
    
    @Data
    public static class DeptInfo {
        private String deptCode;
        private String deptName;
        private Boolean isPrimary;      // 是否主部门
        private String policyStatus;    // 策略状态
    }
    
    @Data
    public static class RoleInfo {
        private String roleCode;
        private String roleName;
        private String dataScope;
        private Boolean isPrimary;      // 是否主角色
        private String policyStatus;    // 策略状态
    }
}
```

#### SysUserExportVO.java

```java
package com.shy.nexusix.iam.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysUserExportVO {
    
    @ExcelProperty(value = "用户编码", index = 0)
    private String userCode;
    
    @ExcelProperty(value = "用户名", index = 1)
    private String userName;
    
    @ExcelProperty(value = "昵称", index = 2)
    private String nickName;
    
    @ExcelProperty(value = "真实姓名", index = 3)
    private String realName;
    
    @ExcelProperty(value = "邮箱", index = 4)
    private String email;
    
    @ExcelProperty(value = "手机号", index = 5)
    private String phone;
    
    @ExcelProperty(value = "性别", index = 6)
    private String genderDesc;
    
    @ExcelProperty(value = "部门", index = 7)
    private String deptName;
    
    @ExcelProperty(value = "角色", index = 8)
    private String roleNames;  // 多个角色用逗号分隔
    
    @ExcelProperty(value = "状态", index = 9)
    private String statusDesc;
    
    @ExcelProperty(value = "最后登录时间", index = 10)
    private String lastLoginTime;
    
    @ExcelProperty(value = "创建时间", index = 11)
    private String createTime;
}
```

#### SysUserImportVO.java

```java
package com.shy.nexusix.iam.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysUserImportVO {
    
    @ExcelProperty(value = "用户编码*", index = 0)
    private String userCode;
    
    @ExcelProperty(value = "用户名*", index = 1)
    private String userName;
    
    @ExcelProperty(value = "密码*", index = 2)
    private String password;
    
    @ExcelProperty(value = "昵称", index = 3)
    private String nickName;
    
    @ExcelProperty(value = "真实姓名", index = 4)
    private String realName;
    
    @ExcelProperty(value = "邮箱", index = 5)
    private String email;
    
    @ExcelProperty(value = "手机号", index = 6)
    private String phone;
    
    @ExcelProperty(value = "性别(MALE/FEMALE/UNKNOWN)", index = 7)
    private String gender;
    
    @ExcelProperty(value = "部门编码*", index = 8)
    private String deptCode;
    
    @ExcelProperty(value = "角色编码*（多个用逗号分隔）", index = 9)
    private String roleCodesStr;
    
    // 将角色编码字符串转换为列表
    public List<String> getRoleCodes() {
        if (StringUtils.isBlank(roleCodesStr)) {
            return Collections.emptyList();
        }
        return Arrays.asList(roleCodesStr.split(","));
    }
}
```

---

## 2.3 SysRoleController (角色管理控制器)

### 2.3.1 接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryRoleList()` | GET | `/role/list` | 查询角色列表 | ROLE:VIEW |
| `queryRolePage()` | GET | `/role/page` | 分页查询角色 | ROLE:VIEW |
| `queryRoleDetail()` | GET | `/role/detail/{code}` | 查询角色详情 | ROLE:VIEW |
| `addRole()` | POST | `/role/add` | 新增角色 | ROLE:ADD |
| `updateRole()` | PUT | `/role/update` | 更新角色 | ROLE:EDIT |
| `deleteRole()` | DELETE | `/role/delete/{code}` | 删除角色 | ROLE:DELETE |
| `grantPermissions()` | POST | `/role/{code}/permissions` | 为角色授予权限 | ROLE:GRANT_PERMS |
| `revokePermissions()` | DELETE | `/role/{code}/permissions` | 撤销角色权限 | ROLE:REVOKE_PERMS |
| `queryRolePerms()` | GET | `/role/{code}/permissions` | 查询角色权限 | ROLE:VIEW_PERMS |
| `queryRoleUsers()` | GET | `/role/{code}/users` | 查询拥有该角色的用户 | ROLE:VIEW_USERS |
| `changeStatus()` | PUT | `/role/change-status` | 启用/禁用角色 | ROLE:CHANGE_STATUS |
| `copyRole()` | POST | `/role/copy` | 复制角色（含权限） | ROLE:COPY |

### 2.3.2 核心接口实现

**接口: grantPermissions() - 为角色授予权限**

```java
@PostMapping("/{code}/permissions")
@Operation(summary = "为角色授予权限")
@SaCheckPermission("ROLE:GRANT_PERMS")
@Transactional(rollbackFor = Exception.class)
public ApiResponse grantPermissions(
    @PathVariable String code,
    @Valid @RequestBody SysRoleGrantPermRTO param) {
    
    // 1. 查询角色
    SysRole role = roleMapper.selectOne(
        new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getRoleCode, code)
    );
    
    if (role == null) {
        throw new BusinessException("角色不存在");
    }
    
    // 2. 查询权限
    List<String> permCodes = param.getPermissions().stream()
        .map(SysRoleGrantPermRTO.PermissionItem::getPermCode)
        .collect(Collectors.toList());
    
    List<SysPerm> perms = permMapper.selectList(
        new LambdaQueryWrapper<SysPerm>()
            .in(SysPerm::getPermCode, permCodes)
    );
    
    if (perms.size() != permCodes.size()) {
        throw new BusinessException("部分权限不存在");
    }
    
    // 3. 删除旧的权限策略
    permPolicyMapper.delete(
        new LambdaQueryWrapper<SysPermPolicy>()
            .eq(SysPermPolicy::getRoleId, role.getId())
    );
    
    // 4. 创建新的权限策略
    for (SysRoleGrantPermRTO.PermissionItem item : param.getPermissions()) {
        SysPerm perm = perms.stream()
            .filter(p -> p.getPermCode().equals(item.getPermCode()))
            .findFirst()
            .orElse(null);
        
        if (perm == null) continue;
        
        SysPermPolicy permPolicy = new SysPermPolicy();
        permPolicy.setRoleId(role.getId());
        permPolicy.setPermId(perm.getId());
        permPolicy.setStatus("ACTIVE");
        
        // 字段级权限（可选）
        if (item.getFieldPermissions() != null && !item.getFieldPermissions().isEmpty()) {
            String fieldPermJson = JSON.toJSONString(item.getFieldPermissions());
            permPolicy.setFieldPermissions(fieldPermJson);
        }
        
        permPolicyMapper.insert(permPolicy);
    }
    
    return ApiResponse.success("权限授予成功");
}
```

### 2.3.3 RTO设计

#### SysRoleAddRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysRoleAddRTO {
    
    @NotBlank(message = "角色编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{3,50}$", message = "角色编码格式错误")
    private String roleCode;
    
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 100, message = "角色名称不能超过100字符")
    private String roleName;
    
    @Length(max = 200, message = "角色描述不能超过200字符")
    private String roleDesc;
    
    @NotBlank(message = "数据权限范围不能为空")
    @Pattern(regexp = "^(SELF|DEPT|DEPT_AND_SUB|ALL)$", message = "数据权限范围值错误")
    private String dataScope;
    
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;
}
```

#### SysRoleGrantPermRTO.java

```java
package com.shy.nexusix.iam.rto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysRoleGrantPermRTO {
    
    @NotEmpty(message = "权限列表不能为空")
    @Valid
    private List<PermissionItem> permissions;
    
    @Data
    public static class PermissionItem {
        
        @NotBlank(message = "权限编码不能为空")
        private String permCode;
        
        /**
         * 字段级权限配置（可选）
         * 格式：{"user_name": ["READ", "UPDATE"], "email": ["READ"]}
         */
        private Map<String, List<String>> fieldPermissions;
    }
}
```

---

**文档未完待续...**

本文档详细定义了IAM模块的补充实现方案，包括：
- ✅ 用户管理17个接口
- ✅ 角色管理12个接口
- ✅ 详细的实现逻辑（含代码示例）
- ✅ 完整的RTO/VO设计
- ✅ Excel导入导出功能

下一部分将继续完成权限管理、系统配置等模块的设计文档。
