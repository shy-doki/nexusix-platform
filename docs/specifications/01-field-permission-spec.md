# 字段级权限实现规格文档

## 1. 概述

### 1.1 功能定义
字段级权限：根据用户的字段权限，动态控制数据库查询、插入、更新时可以操作的字段。

### 1.2 适用场景
- **查询接口**：控制返回哪些字段
- **新增接口**：控制可以填写哪些字段
- **更新接口**：控制可以修改哪些字段

---

## 2. 数据结构设计

### 2.1 字段权限存储结构

**UserContextDTO.FieldPermission 结构：**

```java
public class FieldPermission {
    /** 查询操作的字段权限，key=表名 */
    private Map<String, TableFieldPermission> query;
    
    /** 创建操作的字段权限，key=表名 */
    private Map<String, TableFieldPermission> create;
    
    /** 更新操作的字段权限，key=表名 */
    private Map<String, TableFieldPermission> update;
}

public class TableFieldPermission {
    /** 可操作字段列表（策略ACTIVE时配置） */
    private List<String> operable = new ArrayList<>();
    
    /** 不可操作字段列表（策略非ACTIVE时配置） */
    private List<String> inoperable = new ArrayList<>();
}
```

**JSON 示例：**

```json
{
  "query": {
    "sys_user": {
      "operable": ["user_name", "email", "phone", "status"],
      "inoperable": ["password", "salary"]
    }
  },
  "create": {
    "sys_user": {
      "operable": ["user_name", "email", "password"],
      "inoperable": ["status", "salary"]
    }
  },
  "update": {
    "sys_user": {
      "operable": ["email", "phone", "status"],
      "inoperable": ["user_name", "password"]
    }
  }
}
```

---

## 3. 查询接口实现

### 3.1 方案一：MapStruct 条件映射（推荐）

#### 3.1.1 Converter 实现

```java
@Mapper(componentModel = "spring")
public interface SysUserConverter {
    
    /**
     * Entity 转 VO（带字段权限过滤）
     */
    default SysUserVO toVO(SysUser entity, List<String> operableFields) {
        if (entity == null) {
            return null;
        }
        
        SysUserVO vo = new SysUserVO();
        
        // 根据字段权限条件映射
        if (hasFieldPermission(operableFields, "user_name")) {
            vo.setUserName(entity.getUserName());
        }
        if (hasFieldPermission(operableFields, "email")) {
            vo.setEmail(entity.getEmail());
        }
        if (hasFieldPermission(operableFields, "phone")) {
            vo.setPhone(entity.getPhone());
        }
        if (hasFieldPermission(operableFields, "status")) {
            vo.setStatus(entity.getStatus());
        }
        
        return vo;
    }
    
    /**
     * 批量转换
     */
    default List<SysUserVO> toVOList(List<SysUser> entityList, List<String> operableFields) {
        return entityList.stream()
            .map(entity -> toVO(entity, operableFields))
            .collect(Collectors.toList());
    }
    
    /**
     * 检查是否有字段权限
     */
    default boolean hasFieldPermission(List<String> operableFields, String fieldName) {
        return operableFields != null && operableFields.contains(fieldName);
    }
}
```

#### 3.1.2 Service 实现

```java
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysUserConverter sysUserConverter;
    
    @Override
    public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
        
        // 2. 执行查询
        List<SysUser> userList = sysUserMapper.selectList(wrapper);
        
        // 3. 获取字段权限
        List<String> operableFields = getQueryOperableFields("sys_user");
        
        // 4. 转换并过滤字段
        return sysUserConverter.toVOList(userList, operableFields);
    }
    
    /**
     * 获取查询操作的可操作字段
     */
    private List<String> getQueryOperableFields(String tableName) {
        UserContextDTO userContext = UserContext.getUserContext();
        UserContextDTO.FieldPermission fieldPerm = 
            userContext.getPermissions().getFieldPermission();
        
        UserContextDTO.TableFieldPermission tableFieldPerm = 
            fieldPerm.getQuery().get(tableName);
        
        if (tableFieldPerm == null || tableFieldPerm.getOperable() == null 
            || tableFieldPerm.getOperable().isEmpty()) {
            return null;  // null表示无限制，返回所有字段
        }
        
        return tableFieldPerm.getOperable();
    }
}
```

---

### 3.2 方案二：MyBatis-Plus 动态 SELECT

#### 3.2.1 Service 实现

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // 1. 获取字段权限
    List<String> operableFields = getQueryOperableFields("sys_user");
    
    // 2. 构建动态查询
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    
    // 如果有字段权限限制
    if (operableFields != null && !operableFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> operableFields.contains(field.getColumn())
        );
    }
    
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    // 3. 执行查询（只查询有权限的字段）
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // 4. 转换为VO
    return sysUserConverter.toVOList(userList);
}
```

#### 3.2.2 生成的SQL

```sql
-- 有字段权限限制
SELECT user_name, email, phone, status 
FROM sys_user 
WHERE is_deleted = 'NOT_DELETED';

-- 没有字段权限限制
SELECT * 
FROM sys_user 
WHERE is_deleted = 'NOT_DELETED';
```

---

### 3.3 推荐方案：组合使用

```java
@Override
public List<SysUserVO> queryUserList(SysUserQueryRTO queryRTO) {
    // 获取字段权限
    List<String> operableFields = getQueryOperableFields("sys_user");
    
    // 方案二：动态SELECT（数据库层过滤）
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    if (operableFields != null && !operableFields.isEmpty()) {
        wrapper.select(SysUser.class, 
            field -> operableFields.contains(field.getColumn())
        );
    }
    wrapper.eq(SysUser::getIsDeleted, "NOT_DELETED");
    
    List<SysUser> userList = sysUserMapper.selectList(wrapper);
    
    // 方案一：MapStruct 转换（双重保险）
    return sysUserConverter.toVOList(userList, operableFields);
}
```

**优点：**
- ✅ 数据库层过滤（减少数据传输）
- ✅ 应用层二次过滤（双重保险）
- ✅ 性能最优

---

## 4. 新增接口实现

### 4.1 字段权限校验

#### 4.1.1 Controller 实现

```java
@RestController
@RequestMapping("/api/users")
public class SysUserController {
    
    @PostMapping
    public ApiResponse createUser(@RequestBody SysUserAddRTO addRTO) {
        // 1. 获取create字段权限
        List<String> operableFields = getCreateOperableFields("sys_user");
        
        // 2. 校验字段权限
        validateCreateFields(addRTO, operableFields);
        
        // 3. 通过校验后，执行创建
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
        // ... 检查其他字段
    }
    
    private List<String> getCreateOperableFields(String tableName) {
        UserContextDTO userContext = UserContext.getUserContext();
        UserContextDTO.FieldPermission fieldPerm = 
            userContext.getPermissions().getFieldPermission();
        
        UserContextDTO.TableFieldPermission tableFieldPerm = 
            fieldPerm.getCreate().get(tableName);
        
        return tableFieldPerm != null ? tableFieldPerm.getOperable() : null;
    }
}
```

---

## 5. 更新接口实现

### 5.1 差异检测 + 校验

#### 5.1.1 Controller 实现

```java
@PutMapping("/{id}")
public ApiResponse updateUser(@PathVariable Long id, @RequestBody SysUserUpdateRTO updateRTO) {
    // 1. 获取update字段权限
    List<String> operableFields = getUpdateOperableFields("sys_user");
    
    // 2. 校验字段权限
    validateUpdateFields(id, updateRTO, operableFields);
    
    // 3. 通过校验后，执行更新
    sysUserService.update(id, updateRTO);
    
    return ApiResponse.success();
}

/**
 * 校验更新字段权限
 */
private void validateUpdateFields(Long userId, SysUserUpdateRTO updateRTO, List<String> operableFields) {
    // 如果没有字段权限限制，允许所有字段
    if (operableFields == null) {
        return;
    }
    
    // 查询原始数据
    SysUser oldEntity = sysUserMapper.selectById(userId);
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
    
    // ... 检查其他字段
}
```

---

## 6. 通用工具类

### 6.1 FieldPermissionHelper

```java
public class FieldPermissionHelper {
    
    /**
     * 获取字段权限
     */
    public static List<String> getOperableFields(String tableName, String operation) {
        UserContextDTO userContext = UserContext.getUserContext();
        UserContextDTO.FieldPermission fieldPerm = 
            userContext.getPermissions().getFieldPermission();
        
        Map<String, UserContextDTO.TableFieldPermission> operationMap;
        switch (operation.toUpperCase()) {
            case "QUERY":
                operationMap = fieldPerm.getQuery();
                break;
            case "CREATE":
                operationMap = fieldPerm.getCreate();
                break;
            case "UPDATE":
                operationMap = fieldPerm.getUpdate();
                break;
            default:
                return null;
        }
        
        UserContextDTO.TableFieldPermission tableFieldPerm = operationMap.get(tableName);
        return tableFieldPerm != null ? tableFieldPerm.getOperable() : null;
    }
    
    /**
     * 检查是否有字段权限
     */
    public static boolean hasFieldPermission(String tableName, String operation, String fieldName) {
        List<String> operableFields = getOperableFields(tableName, operation);
        return operableFields == null || operableFields.contains(fieldName);
    }
}
```

---

## 7. 测试用例

### 7.1 查询接口测试

```java
@Test
public void testQueryWithFieldPermission() {
    // 模拟用户权限：只能查看 user_name, email
    mockFieldPermission("sys_user", "QUERY", Arrays.asList("user_name", "email"));
    
    // 查询用户列表
    List<SysUserVO> result = sysUserService.queryUserList(new SysUserQueryRTO());
    
    // 验证返回字段
    assertNotNull(result.get(0).getUserName());
    assertNotNull(result.get(0).getEmail());
    assertNull(result.get(0).getPhone());  // 无权限字段为null
    assertNull(result.get(0).getPassword());  // 无权限字段为null
}
```

### 7.2 新增接口测试

```java
@Test
public void testCreateWithoutPermission() {
    // 模拟用户权限：只能设置 user_name, email
    mockFieldPermission("sys_user", "CREATE", Arrays.asList("user_name", "email"));
    
    // 尝试设置不允许的字段
    SysUserAddRTO addRTO = new SysUserAddRTO();
    addRTO.setUserName("test");
    addRTO.setEmail("test@example.com");
    addRTO.setStatus("ENABLED");  // 试图设置不允许的字段
    
    // 预期抛出异常
    assertThrows(BusinessException.class, () -> {
        sysUserService.save(addRTO);
    });
}
```

---

## 8. 性能优化建议

### 8.1 缓存字段权限

```java
// 在Service中缓存字段权限，避免重复获取
private List<String> cachedOperableFields;

private List<String> getQueryOperableFields(String tableName) {
    if (cachedOperableFields == null) {
        UserContextDTO userContext = UserContext.getUserContext();
        // ... 获取字段权限
        cachedOperableFields = ...;
    }
    return cachedOperableFields;
}
```

### 8.2 避免反射

- ✅ 使用 MapStruct 编译期生成代码
- ✅ 使用 MyBatis-Plus 动态 SELECT
- ❌ 避免运行时反射遍历字段

---

## 9. 注意事项

### 9.1 安全性

- ✅ 字段权限是**白名单**机制（只返回有权限的字段）
- ✅ 默认策略：无配置 = 无限制（管理员）
- ✅ 双重过滤：数据库层 + 应用层

### 9.2 兼容性

- ✅ null 表示无字段限制
- ✅ 空列表 [] 表示无任何字段权限
- ✅ 向后兼容：老接口无需修改，默认返回所有字段

### 9.3 维护性

- ✅ 每个实体一个 Converter
- ✅ 字段映射逻辑集中管理
- ✅ 易于测试和调试

---

## 10. 实施计划

### 10.1 阶段一：基础设施

- [ ] 创建 FieldPermissionHelper 工具类
- [ ] 修改 AuthServiceImpl.buildPermissionInfo() 方法
- [ ] 测试字段权限数据结构

### 10.2 阶段二：查询接口

- [ ] 为每个实体创建带字段过滤的 Converter
- [ ] 修改 Service 查询方法，应用字段权限
- [ ] 编写单元测试

### 10.3 阶段三：新增/更新接口

- [ ] 在 Controller 添加字段权限校验
- [ ] 编写校验逻辑
- [ ] 编写集成测试

### 10.4 阶段四：性能优化

- [ ] 添加字段权限缓存
- [ ] 优化 SQL 查询
- [ ] 性能测试
