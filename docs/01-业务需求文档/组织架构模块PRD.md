# NexusIX-Platform 模块功能设计PRD文档

**文档版本**: v2.0  
**创建日期**: 2026-06-15  
**基于**: nexusix-tenant 模块实现方案  

---

## 1. nexusix-org (组织架构模块)

### 1.1 模块概述

**模块职责**: 部门管理、岗位管理、组织架构树形展示

**核心表**:
- `sys_dept` - 部门表
- `sys_post` - 岗位表（可选）

**数据库设计**:

```sql
CREATE TABLE sys_dept (
    id BIGINT PRIMARY KEY,
    dept_code VARCHAR(100) NOT NULL UNIQUE,
    dept_name VARCHAR(100) NOT NULL,
    dept_desc VARCHAR(200),
    parent_id BIGINT NOT NULL,
    path VARCHAR(1000) NOT NULL,
    level INT NOT NULL,
    has_children BOOLEAN DEFAULT FALSE,
    leader_user_id BIGINT,
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ENABLED',
    disable_reason VARCHAR(200),
    create_tenant BIGINT NOT NULL,
    create_dept BIGINT NOT NULL,
    create_role BIGINT NOT NULL,
    create_by BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_by BIGINT,
    update_at TIMESTAMP,
    is_deleted VARCHAR(20) DEFAULT 'NOT_DELETED',
    deleted_at TIMESTAMP
);
```

### 1.2 目录结构

```
nexusix-org/src/main/java/com/shy/nexusix/org/
├── controller/
│   ├── SysDeptController.java          # 部门管理控制器
│   └── SysPostController.java          # 岗位管理控制器
├── converter/
│   ├── SysDeptConverter.java           # 部门转换器
│   └── SysPostConverter.java           # 岗位转换器
├── dto/
│   └── (内部传输对象)
├── entity/
│   ├── SysDept.java                    # 部门实体
│   └── SysPost.java                    # 岗位实体
├── mapper/
│   ├── SysDeptMapper.java              # 部门Mapper
│   ├── SysDeptMapper.xml               # 部门SQL映射
│   ├── SysPostMapper.java              # 岗位Mapper
│   └── SysPostMapper.xml               # 岗位SQL映射
├── rto/
│   ├── SysDeptAddRTO.java              # 新增部门请求
│   ├── SysDeptUpdateRTO.java           # 更新部门请求
│   ├── SysDeptQueryRTO.java            # 查询部门请求
│   ├── SysDeptMoveRTO.java             # 移动部门请求
│   ├── SysPostAddRTO.java              # 新增岗位请求
│   ├── SysPostUpdateRTO.java           # 更新岗位请求
│   └── SysPostQueryRTO.java            # 查询岗位请求
├── service/
│   ├── ISysDeptService.java            # 部门服务接口
│   ├── ISysPostService.java            # 岗位服务接口
│   └── impl/
│       ├── SysDeptServiceImpl.java     # 部门服务实现
│       └── SysPostServiceImpl.java     # 岗位服务实现
└── vo/
    ├── SysDeptCommonVO.java            # 部门通用VO
    ├── SysDeptDetailVO.java            # 部门详情VO
    ├── SysDeptTreeVO.java              # 部门树形VO
    ├── SysPostCommonVO.java            # 岗位通用VO
    └── SysPostDetailVO.java            # 岗位详情VO
```

### 1.3 SysDeptController (部门管理控制器)

#### 1.3.1 功能接口列表

| 接口方法 | 请求方式 | 路径 | 功能说明 | 权限编码 |
|---------|---------|------|---------|---------|
| `queryDeptList()` | GET | `/dept/list` | 查询部门列表（平铺） | DEPT:VIEW |
| `queryDeptPage()` | GET | `/dept/page` | 分页查询部门 | DEPT:VIEW |
| `queryDeptTree()` | GET | `/dept/tree/list` | 查询部门树形结构 | DEPT:VIEW |
| `queryDeptDetail()` | GET | `/dept/detail/{code}` | 查询部门详情 | DEPT:VIEW |
| `querySubDepts()` | GET | `/dept/children/{code}` | 查询子部门列表 | DEPT:VIEW |
| `addDept()` | POST | `/dept/add` | 新增部门 | DEPT:ADD |
| `updateDept()` | PUT | `/dept/update` | 更新部门信息 | DEPT:EDIT |
| `deleteDept()` | DELETE | `/dept/delete/{code}` | 删除部门（逻辑删除） | DEPT:DELETE |
| `moveDept()` | PUT | `/dept/move` | 移动部门到新父部门 | DEPT:MOVE |
| `queryDeptMembers()` | GET | `/dept/{code}/members` | 查询部门成员列表 | DEPT:VIEW_MEMBERS |
| `batchAddMembers()` | POST | `/dept/{code}/members/batch` | 批量添加部门成员 | DEPT:MANAGE_MEMBERS |
| `batchRemoveMembers()` | DELETE | `/dept/{code}/members/batch` | 批量移除部门成员 | DEPT:MANAGE_MEMBERS |

#### 1.3.2 核心接口实现逻辑

**接口1: queryDeptTree() - 查询部门树形结构**

```java
@GetMapping("/tree/list")
@Operation(summary = "查询部门树形结构")
@SaCheckPermission("DEPT:VIEW")
public ApiResponse queryDeptTree(@Valid SysDeptQueryRTO param) {
    // 1. 获取当前用户租户ID
    Long tenantId = TenantContext.getCurrentTenantId();
    
    // 2. 查询该租户下所有部门（使用物化路径排序）
    List<SysDept> deptList = deptMapper.selectList(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getCreateTenant, tenantId)
            .eq(SysDept::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
            .orderByAsc(SysDept::getPath)
    );
    
    // 3. 构建树形结构
    List<SysDeptTreeVO> treeList = buildDeptTree(deptList);
    
    // 4. 字段权限过滤
    List<SysDeptTreeVO> filteredList = applyFieldPermissionFilter(treeList);
    
    return ApiResponse.success(filteredList);
}

/**
 * 构建部门树形结构
 */
private List<SysDeptTreeVO> buildDeptTree(List<SysDept> deptList) {
    // 1. 转换为TreeVO并建立映射
    Map<String, SysDeptTreeVO> codeToTreeMap = new HashMap<>();
    Map<Long, String> idToCodeMap = new HashMap<>();
    
    for (SysDept dept : deptList) {
        SysDeptTreeVO treeVO = deptConverter.toTreeVO(dept);
        codeToTreeMap.put(dept.getDeptCode(), treeVO);
        idToCodeMap.put(dept.getId(), dept.getDeptCode());
    }
    
    // 2. 构建父子关系
    List<SysDeptTreeVO> rootList = new ArrayList<>();
    for (SysDept dept : deptList) {
        SysDeptTreeVO treeVO = codeToTreeMap.get(dept.getDeptCode());
        
        if (dept.getParentId() == 0) {
            // 根部门
            rootList.add(treeVO);
        } else {
            // 子部门，找到父部门并添加到children
            String parentCode = idToCodeMap.get(dept.getParentId());
            SysDeptTreeVO parent = codeToTreeMap.get(parentCode);
            if (parent != null) {
                parent.getChildren().add(treeVO);
            }
        }
    }
    
    return rootList;
}
```

**接口2: moveDept() - 移动部门**

```java
@PutMapping("/move")
@Operation(summary = "移动部门")
@SaCheckPermission("DEPT:MOVE")
@Transactional(rollbackFor = Exception.class)
public ApiResponse moveDept(@Valid @RequestBody SysDeptMoveRTO param) {
    // 1. 查询源部门
    SysDept sourceDept = deptMapper.selectOne(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptCode, param.getSourceDeptCode())
            .eq(SysDept::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    if (sourceDept == null) {
        throw new BusinessException("源部门不存在");
    }
    
    // 2. 查询目标父部门
    SysDept targetParent = deptMapper.selectOne(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptCode, param.getTargetParentCode())
            .eq(SysDept::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    if (targetParent == null) {
        throw new BusinessException("目标父部门不存在");
    }
    
    // 3. 校验不能移动到自己的子部门
    if (targetParent.getPath().startsWith(sourceDept.getPath() + "/")) {
        throw new BusinessException("不能移动到自己的子部门");
    }
    
    // 4. 计算新的path和level
    String oldPath = sourceDept.getPath();
    String newPath = targetParent.getPath() + "/" + sourceDept.getId();
    int levelDiff = targetParent.getLevel() + 1 - sourceDept.getLevel();
    
    // 5. 查询所有子孙部门
    List<SysDept> subDepts = deptMapper.selectList(
        new LambdaQueryWrapper<SysDept>()
            .likeRight(SysDept::getPath, oldPath + "/")
            .eq(SysDept::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    // 6. 批量更新子孙部门的path和level
    for (SysDept subDept : subDepts) {
        String updatedPath = subDept.getPath().replace(oldPath, newPath);
        subDept.setPath(updatedPath);
        subDept.setLevel(subDept.getLevel() + levelDiff);
        deptMapper.updateById(subDept);
    }
    
    // 7. 更新源部门
    sourceDept.setParentId(targetParent.getId());
    sourceDept.setPath(newPath);
    sourceDept.setLevel(targetParent.getLevel() + 1);
    deptMapper.updateById(sourceDept);
    
    // 8. 更新原父部门和新父部门的has_children标记
    updateHasChildrenFlag(sourceDept.getParentId());
    updateHasChildrenFlag(targetParent.getId());
    
    return ApiResponse.success("部门移动成功");
}

/**
 * 更新部门的has_children标记
 */
private void updateHasChildrenFlag(Long deptId) {
    if (deptId == 0) return;
    
    long childCount = deptMapper.selectCount(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getParentId, deptId)
            .eq(SysDept::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
    );
    
    SysDept dept = deptMapper.selectById(deptId);
    dept.setHasChildren(childCount > 0);
    deptMapper.updateById(dept);
}
```

**接口3: batchAddMembers() - 批量添加部门成员**

```java
@PostMapping("/{code}/members/batch")
@Operation(summary = "批量添加部门成员")
@SaCheckPermission("DEPT:MANAGE_MEMBERS")
@Transactional(rollbackFor = Exception.class)
public ApiResponse batchAddMembers(
    @PathVariable String code,
    @Valid @RequestBody SysDeptBatchMembersRTO param) {
    
    // 1. 查询部门
    SysDept dept = deptMapper.selectOne(
        new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptCode, code)
    );
    
    if (dept == null) {
        throw new BusinessException("部门不存在");
    }
    
    // 2. 查询用户
    List<SysUser> users = userMapper.selectList(
        new LambdaQueryWrapper<SysUser>()
            .in(SysUser::getUserCode, param.getUserCodes())
    );
    
    if (users.size() != param.getUserCodes().size()) {
        throw new BusinessException("部分用户不存在");
    }
    
    // 3. 批量创建用户策略
    Long tenantId = TenantContext.getCurrentTenantId();
    for (SysUser user : users) {
        // 检查是否已存在
        Long existCount = userPolicyMapper.selectCount(
            new LambdaQueryWrapper<SysUserPolicy>()
                .eq(SysUserPolicy::getUserId, user.getId())
                .eq(SysUserPolicy::getTenantId, tenantId)
                .eq(SysUserPolicy::getDeptId, dept.getId())
                .eq(SysUserPolicy::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED)
        );
        
        if (existCount > 0) {
            continue; // 已存在，跳过
        }
        
        // 创建用户策略
        SysUserPolicy userPolicy = new SysUserPolicy();
        userPolicy.setUserId(user.getId());
        userPolicy.setTenantId(tenantId);
        userPolicy.setDeptId(dept.getId());
        userPolicy.setRoleId(param.getDefaultRoleId()); // 可选的默认角色
        userPolicy.setIsPrimary(false);
        userPolicy.setStatus("ACTIVE");
        userPolicyMapper.insert(userPolicy);
    }
    
    return ApiResponse.success("批量添加成员成功");
}
```

### 1.4 RTO (请求传输对象)

#### SysDeptAddRTO.java

```java
package com.shy.nexusix.org.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDeptAddRTO {
    
    @NotBlank(message = "部门编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{3,50}$", message = "部门编码格式错误：只能包含大写字母、数字、下划线，长度3-50")
    private String deptCode;
    
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 100, message = "部门名称不能超过100字符")
    private String deptName;
    
    @Length(max = 200, message = "部门描述不能超过200字符")
    private String deptDesc;
    
    @NotBlank(message = "父部门编码不能为空")
    private String parentCode;
    
    private Long leaderUserId;  // 部门负责人ID（可选）
    
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;
}
```

#### SysDeptUpdateRTO.java

```java
package com.shy.nexusix.org.rto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SysDeptUpdateRTO {
    
    @NotBlank(message = "部门编码不能为空")
    private String deptCode;
    
    @Length(max = 100, message = "部门名称不能超过100字符")
    private String deptName;
    
    @Length(max = 200, message = "部门描述不能超过200字符")
    private String deptDesc;
    
    private Long leaderUserId;
    
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;
    
    private String status;  // ENABLED/DISABLED
}
```

#### SysDeptMoveRTO.java

```java
package com.shy.nexusix.org.rto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysDeptMoveRTO {
    
    @NotBlank(message = "源部门编码不能为空")
    private String sourceDeptCode;
    
    @NotBlank(message = "目标父部门编码不能为空")
    private String targetParentCode;
}
```

#### SysDeptQueryRTO.java

```java
package com.shy.nexusix.org.rto;

import lombok.Data;

@Data
public class SysDeptQueryRTO {
    
    private String deptName;      // 部门名称（模糊查询）
    private String status;        // 状态
    private String leaderUserName; // 负责人姓名（模糊查询）
}
```

#### SysDeptBatchMembersRTO.java

```java
package com.shy.nexusix.org.rto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SysDeptBatchMembersRTO {
    
    @NotEmpty(message = "用户编码列表不能为空")
    private List<String> userCodes;
    
    private Long defaultRoleId;  // 可选的默认角色ID
}
```

### 1.5 VO (视图对象)

#### SysDeptCommonVO.java

```java
package com.shy.nexusix.org.vo;

import lombok.Data;

@Data
public class SysDeptCommonVO {
    
    private String deptCode;
    private String deptName;
    private String deptDesc;
    private String parentCode;
    private String parentName;
    private Integer level;
    private String leaderUserName;
    private String leaderUserCode;
    private Integer sortOrder;
    private String status;
    private Integer memberCount;    // 成员数量
    private String createTime;
    private String updateTime;
}
```

#### SysDeptDetailVO.java

```java
package com.shy.nexusix.org.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysDeptDetailVO extends SysDeptCommonVO {
    
    private String path;
    private Boolean hasChildren;
    private String disableReason;
    
    // 部门负责人详细信息
    private LeaderInfo leader;
    
    // 部门成员列表（前10个）
    private List<MemberInfo> members;
    
    // 扩展属性
    private Map<String, Object> extAttributes;
    
    // 审计信息
    private String createByName;
    private String updateByName;
    
    @Data
    public static class LeaderInfo {
        private Long userId;
        private String userCode;
        private String userName;
        private String nickName;
        private String email;
        private String phone;
    }
    
    @Data
    public static class MemberInfo {
        private Long userId;
        private String userCode;
        private String userName;
        private String nickName;
        private String roleName;
        private String status;
    }
}
```

#### SysDeptTreeVO.java

```java
package com.shy.nexusix.org.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysDeptTreeVO {
    
    private String deptCode;
    private String deptName;
    private String deptDesc;
    private String parentCode;
    private String path;
    private Integer level;
    private Boolean hasChildren;
    private String leaderUserName;
    private Long leaderUserId;
    private Integer sortOrder;
    private String status;
    private Integer memberCount;
    private String createTime;
    
    // 子部门列表
    private List<SysDeptTreeVO> children = new ArrayList<>();
}
```

### 1.6 Converter (转换器)

```java
package com.shy.nexusix.org.converter;

import com.shy.nexusix.org.entity.SysDept;
import com.shy.nexusix.org.rto.SysDeptAddRTO;
import com.shy.nexusix.org.vo.SysDeptCommonVO;
import com.shy.nexusix.org.vo.SysDeptDetailVO;
import com.shy.nexusix.org.vo.SysDeptTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SysDeptConverter {
    
    @Named("toCommonVO")
    @Mapping(source = "createAt", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "updateAt", target = "updateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SysDeptCommonVO toCommonVO(SysDept entity);
    
    @Named("toDetailVO")
    @Mapping(source = "createAt", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "updateAt", target = "updateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SysDeptDetailVO toDetailVO(SysDept entity);
    
    @Named("toTreeVO")
    @Mapping(source = "createAt", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    SysDeptTreeVO toTreeVO(SysDept entity);
    
    @Named("toEntityFromAdd")
    SysDept toEntity(SysDeptAddRTO rto);
}
```

### 1.7 Service (服务层)

#### ISysDeptService.java

```java
package com.shy.nexusix.org.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.org.entity.SysDept;
import com.shy.nexusix.org.rto.*;
import com.shy.nexusix.org.vo.*;

import java.util.List;

public interface ISysDeptService extends IService<SysDept> {
    
    /**
     * 查询部门列表
     */
    List<SysDeptCommonVO> queryDeptList(SysDeptQueryRTO param);
    
    /**
     * 分页查询部门
     */
    IPage<SysDeptCommonVO> queryDeptPage(PageCommonRTO page, SysDeptQueryRTO param);
    
    /**
     * 查询部门树形结构
     */
    List<SysDeptTreeVO> queryDeptTree(SysDeptQueryRTO param);
    
    /**
     * 查询部门详情
     */
    SysDeptDetailVO queryDeptDetail(String deptCode);
    
    /**
     * 查询子部门列表
     */
    List<SysDeptCommonVO> querySubDepts(String parentCode);
    
    /**
     * 新增部门
     */
    Integer addDept(SysDeptAddRTO param);
    
    /**
     * 更新部门
     */
    Integer updateDept(SysDeptUpdateRTO param);
    
    /**
     * 删除部门
     */
    Integer deleteDept(String deptCode);
    
    /**
     * 移动部门
     */
    Integer moveDept(SysDeptMoveRTO param);
    
    /**
     * 查询部门成员
     */
    List<SysDeptDetailVO.MemberInfo> queryDeptMembers(String deptCode);
    
    /**
     * 批量添加部门成员
     */
    Integer batchAddMembers(String deptCode, SysDeptBatchMembersRTO param);
    
    /**
     * 批量移除部门成员
     */
    Integer batchRemoveMembers(String deptCode, List<String> userCodes);
}
```

### 1.8 Mapper (数据访问层)

#### SysDeptMapper.java

```java
package com.shy.nexusix.org.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shy.nexusix.org.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    
    /**
     * 查询部门及其成员数量
     */
    List<SysDept> queryDeptWithMemberCount(@Param("tenantId") Long tenantId);
    
    /**
     * 查询部门成员
     */
    List<MemberDTO> queryDeptMembers(@Param("deptId") Long deptId);
}
```

#### SysDeptMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.shy.nexusix.org.mapper.SysDeptMapper">
    
    <!-- 查询部门及其成员数量 -->
    <select id="queryDeptWithMemberCount" resultType="com.shy.nexusix.org.entity.SysDept">
        SELECT 
            d.*,
            COUNT(up.id) as member_count
        FROM sys_dept d
        LEFT JOIN sys_user_policy up ON d.id = up.dept_id 
            AND up.is_deleted = 'NOT_DELETED'
        WHERE d.create_tenant = #{tenantId}
            AND d.is_deleted = 'NOT_DELETED'
        GROUP BY d.id
        ORDER BY d.path
    </select>
    
    <!-- 查询部门成员 -->
    <select id="queryDeptMembers" resultType="com.shy.nexusix.org.dto.MemberDTO">
        SELECT 
            u.id as user_id,
            u.user_code,
            u.user_name,
            u.nick_name,
            r.role_name,
            up.status
        FROM sys_user_policy up
        INNER JOIN sys_user u ON up.user_id = u.id
        LEFT JOIN sys_role r ON up.role_id = r.id
        WHERE up.dept_id = #{deptId}
            AND up.is_deleted = 'NOT_DELETED'
            AND u.is_deleted = 'NOT_DELETED'
        ORDER BY up.create_at DESC
    </select>
    
</mapper>
```

---

**文档未完待续...**

本文档详细定义了组织架构模块的完整实现方案，包括：
- ✅ 完整的目录结构
- ✅ 12个核心接口定义
- ✅ 详细的实现逻辑（含代码示例）
- ✅ 5个RTO请求对象
- ✅ 3个VO视图对象
- ✅ MapStruct转换器
- ✅ Service服务层接口
- ✅ MyBatis Mapper映射

下一部分将继续完成其他模块的设计文档。
