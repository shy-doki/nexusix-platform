# 租户树服务优化规格文档

## 1. 概述

### 1.1 优化目标
- 解决租户树查询的N+1问题
- 充分利用path、level、hasChildren字段
- 提升查询性能100倍以上

### 1.2 核心问题
当前实现使用递归查询构建树形结构，存在严重的性能问题：
- 查询根节点：1次SQL
- 递归查询每个节点的子节点：N次SQL
- 总计：1 + N 次SQL

---

## 2. 数据表结构

### 2.1 关键字段说明

| 字段 | 类型 | 说明 | 用途 |
|-----|------|------|------|
| id | BIGINT | 租户ID | 主键 |
| tenant_code | VARCHAR(50) | 租户编码 | 业务主键 |
| parent_id | BIGINT | 父租户ID | 树形结构关联 |
| path | VARCHAR(500) | 祖先路径 | `/101/201/301` 格式，用于快速查询子树 |
| level | INT | 层级深度 | 根节点=0，子节点递增，用于排序和深度限制 |
| has_children | BOOLEAN | 是否有子节点 | 快速判断，避免COUNT查询 |

### 2.2 path 字段详解

**格式规则：**
```
根节点：/101
一级子节点：/101/201
二级子节点：/101/201/301
```

**关键特性：**
- ✅ 包含完整祖先路径
- ✅ 支持前缀匹配（LIKE）
- ✅ 可以快速判断祖先关系
- ✅ 避免递归查询

**应用场景：**
```sql
-- 查询节点101的所有子孙
SELECT * FROM sys_tenant 
WHERE path LIKE '/101/%' 
ORDER BY level, path;

-- 检测环路：节点201是否是节点101的子孙
SELECT COUNT(*) FROM sys_tenant 
WHERE id = 201 AND path LIKE '/101/%';
```

---

## 3. 核心优化原则

### 3.1 原则一：一次查询 + 内存构建树

**优化前（N+1问题）：**
```java
// ❌ 错误：递归查询
public List<TreeVO> buildTree(Long parentId) {
    // 查询子节点
    List<Tenant> children = query("parent_id = ?", parentId);  // 1次SQL
    
    List<TreeVO> result = new ArrayList<>();
    for (Tenant child : children) {
        TreeVO vo = convert(child);
        vo.setChildren(buildTree(child.getId()));  // N次递归SQL
        result.add(vo);
    }
    return result;
}
```

**优化后：**
```java
// ✅ 正确：一次查询 + 内存构建
public List<TreeVO> buildTree() {
    // 1次SQL查询所有节点
    List<Tenant> allTenants = query("ORDER BY level, path");
    
    // 内存构建树（O(n)复杂度）
    return buildTreeInMemory(allTenants);
}
```

---

### 3.2 原则二：充分利用 path 字段

**应用场景：**

1. **查询子树**
```sql
SELECT * FROM sys_tenant 
WHERE path LIKE '/101/%'  -- path前缀匹配
ORDER BY level, path;
```

2. **环路检测**
```sql
-- 检查：节点201移动到节点101下是否形成环路
SELECT * FROM sys_tenant 
WHERE id = 201 
  AND path LIKE '/101/%';
-- 如果查到记录，说明201是101的子孙，不能移动！
```

3. **批量更新子孙路径**
```sql
-- 节点从 /101/201 移动到 /102/202
-- 更新所有子孙的路径
UPDATE sys_tenant 
SET path = REPLACE(path, '/101/201/', '/102/202/')
WHERE path LIKE '/101/201/%';
```

---

### 3.3 原则三：充分利用 level 字段

**应用场景：**

1. **排序（保证父节点在子节点之前）**
```sql
SELECT * FROM sys_tenant 
ORDER BY level ASC, path ASC;
```

2. **深度限制查询**
```sql
-- 只查询3层以内的租户
SELECT * FROM sys_tenant 
WHERE level <= 3;
```

3. **层级统计**
```sql
SELECT level, COUNT(*) as count
FROM sys_tenant
GROUP BY level;
```

---

### 3.4 原则四：充分利用 hasChildren 字段

**应用场景：**

1. **快速判断是否有子节点**
```java
// ✅ 正确
if (Boolean.TRUE.equals(tenant.getHasChildren())) {
    throw new BusinessException("存在子租户，无法删除");
}

// ❌ 错误（多一次COUNT查询）
long count = count("parent_id = ?", tenant.getId());
if (count > 0) {
    throw new BusinessException("存在子租户，无法删除");
}
```

2. **前端展开/收起图标**
```json
{
  "tenantName": "万象集团",
  "hasChildren": true,  // 前端显示展开图标
  "children": []  // 初始未加载子节点
}
```

**维护时机：**
- 添加子租户时：更新父节点 `hasChildren = true`
- 删除子租户时：检查父节点是否还有其他子节点
- 移动节点时：更新旧父节点和新父节点

---

## 4. 核心方法实现

### 4.1 通用树构建方法

```java
/**
 * 内存中构建树形结构（O(n)复杂度）
 * 
 * @param allTenants 所有租户节点（必须按 level, path 排序）
 * @return 根节点列表
 */
private List<SysTenantTreeVO> buildTreeInMemory(List<SysTenant> allTenants) {
    // 存储所有节点（id -> TreeVO）
    Map<Long, SysTenantTreeVO> nodeMap = new HashMap<>(allTenants.size());
    
    // 根节点列表
    List<SysTenantTreeVO> rootList = new ArrayList<>();
    
    // 第一次遍历：创建所有节点
    for (SysTenant tenant : allTenants) {
        SysTenantTreeVO treeVO = sysTenantConverter.toTreeVO(tenant);
        treeVO.setChildTenant(new ArrayList<>());  // 初始化子节点列表
        nodeMap.put(tenant.getId(), treeVO);
    }
    
    // 第二次遍历：建立父子关系
    for (SysTenant tenant : allTenants) {
        SysTenantTreeVO currentNode = nodeMap.get(tenant.getId());
        
        // 判断是否为根节点
        if (tenant.getParentId() == null || tenant.getParentId() == 0L) {
            rootList.add(currentNode);
        } else {
            // 找到父节点并添加到父节点的子列表
            SysTenantTreeVO parentNode = nodeMap.get(tenant.getParentId());
            if (parentNode != null) {
                parentNode.getChildTenant().add(currentNode);
            } else {
                // 父节点不存在，当作根节点（数据异常兜底）
                rootList.add(currentNode);
            }
        }
    }
    
    return rootList;
}
```

**时间复杂度：** O(2n) = O(n)
**空间复杂度：** O(n)

---

### 4.2 路径规范化方法

```java
/**
 * 规范化路径前缀（去除尾部斜杠）
 * 
 * @param path 原始路径
 * @return 规范化后的路径
 */
private String normalizePathPrefix(String path) {
    if (path == null || path.isEmpty()) {
        return "";
    }
    // 去除尾部斜杠
    return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
}
```

---

## 5. 接口优化实现

### 5.1 queryTenantTree（租户树查询）

#### 5.1.1 优化前代码

```java
@Override
public List<SysTenantTreeVO> queryTenantTree() {
    // 查询根节点 1次SQL
    List<SysTenant> roots = this.list(new LambdaQueryWrapper<SysTenant>()
        .isNull(SysTenant::getParentId)
        .or()
        .eq(SysTenant::getParentId, 0L)
    );
    
    List<SysTenantTreeVO> result = new ArrayList<>();
    
    // 递归查询每个根节点的子树 N次SQL
    for (SysTenant root : roots) {
        SysTenantTreeVO treeVO = sysTenantConverter.toTreeVO(root);
        treeVO.setChildTenant(queryChildren(root.getId()));  // 递归查询
        result.add(treeVO);
    }
    
    return result;
}

// 递归查询子节点
private List<SysTenantTreeVO> queryChildren(Long parentId) {
    List<SysTenant> children = this.list(new LambdaQueryWrapper<SysTenant>()
        .eq(SysTenant::getParentId, parentId)
    );
    
    List<SysTenantTreeVO> result = new ArrayList<>();
    for (SysTenant child : children) {
        SysTenantTreeVO treeVO = sysTenantConverter.toTreeVO(child);
        treeVO.setChildTenant(queryChildren(child.getId()));  // 继续递归
        result.add(treeVO);
    }
    
    return result;
}
```

**性能：** 100个节点 = 101次SQL

---

#### 5.1.2 优化后代码

```java
@Override
public List<SysTenantTreeVO> queryTenantTree() {
    // 一次性查询所有租户（按层级和路径排序）
    List<SysTenant> allTenants = this.list(new LambdaQueryWrapper<SysTenant>()
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        .orderByAsc(SysTenant::getLevel)     // 先按层级排序
        .orderByAsc(SysTenant::getPath)      // 再按路径排序
    );
    
    // 内存构建树
    List<SysTenantTreeVO> rootList = buildTreeInMemory(allTenants);
    
    // 字段权限过滤
    List<String> visibleFields = getQueryOperableFields();
    sysTenantConverter.filterTreeVoListByVisibleFields(rootList, visibleFields);
    
    return rootList;
}
```

**性能：** 100个节点 = 1次SQL → **性能提升100倍！**

---

### 5.2 querySubTree（查询子树）

#### 5.2.1 优化后代码

```java
@Override
public SysTenantTreeVO querySubTree(String tenantCode) {
    // 查询目标租户
    SysTenant rootTenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
        .eq(SysTenant::getTenantCode, tenantCode)
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
    );
    
    if (rootTenant == null) {
        throw new BusinessException("租户不存在");
    }
    
    // 利用 path 前缀匹配，一次性查询所有子孙节点
    String pathPrefix = normalizePathPrefix(rootTenant.getPath());
    List<SysTenant> descendants = this.list(new LambdaQueryWrapper<SysTenant>()
        .likeRight(SysTenant::getPath, pathPrefix + "/")  // path LIKE '/101/%'
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        .orderByAsc(SysTenant::getLevel)
        .orderByAsc(SysTenant::getPath)
    );
    
    // 合并根节点和子孙节点
    List<SysTenant> allNodes = new ArrayList<>();
    allNodes.add(rootTenant);
    allNodes.addAll(descendants);
    
    // 内存构建树
    List<SysTenantTreeVO> treeList = buildTreeInMemory(allNodes);
    
    // 字段权限过滤
    List<String> visibleFields = getQueryOperableFields();
    if (!treeList.isEmpty()) {
        sysTenantConverter.filterTreeVoByVisibleFields(treeList.get(0), visibleFields);
    }
    
    return treeList.isEmpty() ? null : treeList.get(0);
}
```

**性能：** 100个子孙 = 2次SQL（1次查根 + 1次查所有子孙）→ **性能提升50倍！**

---

### 5.3 queryTenantTreePage（树形分页）

#### 5.3.1 优化后代码

```java
@Override
public IPage<SysTenantTreeVO> queryTenantTreePage(PageCommonRTO pageParam) {
    // 第1步：分页查询根节点
    Page<SysTenant> rootPage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
    LambdaQueryWrapper<SysTenant> rootWrapper = new LambdaQueryWrapper<SysTenant>()
        .and(w -> w.isNull(SysTenant::getParentId).or().eq(SysTenant::getParentId, 0L))
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
        .orderByAsc(SysTenant::getPath);
    
    IPage<SysTenant> rootResult = this.page(rootPage, rootWrapper);
    List<SysTenant> rootList = rootResult.getRecords();
    
    if (rootList.isEmpty()) {
        return new Page<>(pageParam.getPageNum(), pageParam.getPageSize(), 0);
    }
    
    // 第2步：收集所有根节点的 path
    List<String> rootPaths = rootList.stream()
        .map(SysTenant::getPath)
        .collect(Collectors.toList());
    
    // 第3步：批量查询所有根节点的子孙节点
    LambdaQueryWrapper<SysTenant> descendantsWrapper = new LambdaQueryWrapper<>();
    descendantsWrapper.eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode());
    
    // 构建 OR 条件：path LIKE '/101/%' OR path LIKE '/102/%' OR ...
    descendantsWrapper.and(w -> {
        for (int i = 0; i < rootPaths.size(); i++) {
            String pathPrefix = normalizePathPrefix(rootPaths.get(i));
            if (i == 0) {
                w.likeRight(SysTenant::getPath, pathPrefix + "/");
            } else {
                w.or().likeRight(SysTenant::getPath, pathPrefix + "/");
            }
        }
    });
    
    descendantsWrapper.orderByAsc(SysTenant::getLevel).orderByAsc(SysTenant::getPath);
    List<SysTenant> allDescendants = this.list(descendantsWrapper);
    
    // 第4步：合并根节点和子孙节点
    List<SysTenant> allNodes = new ArrayList<>(rootList);
    allNodes.addAll(allDescendants);
    
    // 第5步：内存构建树
    List<SysTenantTreeVO> treeList = buildTreeInMemory(allNodes);
    
    // 第6步：字段权限过滤
    List<String> visibleFields = getQueryOperableFields();
    sysTenantConverter.filterTreeVoListByVisibleFields(treeList, visibleFields);
    
    // 第7步：构建分页结果
    IPage<SysTenantTreeVO> result = new Page<>(
        rootResult.getCurrent(), 
        rootResult.getSize(), 
        rootResult.getTotal()
    );
    result.setRecords(treeList);
    
    return result;
}
```

**性能：** 10个根节点、每个100子节点 = 2次SQL（1次分页查根 + 1次批量查所有子孙）→ **性能提升500倍！**

---

### 5.4 deleteTenant（删除租户优化）

#### 5.4.1 优化后代码

```java
@Override
public void deleteTenant(String tenantCode) {
    // 查询租户
    SysTenant tenant = this.getOne(new LambdaQueryWrapper<SysTenant>()
        .eq(SysTenant::getTenantCode, tenantCode)
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
    );
    
    if (tenant == null) {
        throw new BusinessException("租户不存在");
    }
    
    // ✅ 使用 hasChildren 字段判断（无需COUNT查询）
    if (Boolean.TRUE.equals(tenant.getHasChildren())) {
        throw new BusinessException("存在子租户，无法删除");
    }
    
    // 逻辑删除
    this.update(new LambdaUpdateWrapper<SysTenant>()
        .eq(SysTenant::getId, tenant.getId())
        .set(SysTenant::getIsDeleted, GlobalEnum.Deleted.DELETED.getCode())
        .set(SysTenant::getDeletedAt, LocalDateTime.now())
    );
    
    // 更新父租户的 hasChildren 标记
    if (tenant.getParentId() != null && tenant.getParentId() != 0L) {
        updateParentHasChildren(tenant.getParentId());
    }
}

/**
 * 更新父租户的 hasChildren 标记
 */
private void updateParentHasChildren(Long parentId) {
    // 检查父节点是否还有其他子节点
    long childCount = this.count(new LambdaQueryWrapper<SysTenant>()
        .eq(SysTenant::getParentId, parentId)
        .eq(SysTenant::getIsDeleted, GlobalEnum.Deleted.NOT_DELETED.getCode())
    );
    
    // 更新 hasChildren 标记
    this.update(new LambdaUpdateWrapper<SysTenant>()
        .eq(SysTenant::getId, parentId)
        .set(SysTenant::getHasChildren, childCount > 0)
    );
}
```

**性能提升：** 减少1次COUNT查询

---

## 6. 数据库索引优化

### 6.1 必需索引

```sql
-- 1. path 前缀匹配索引（最重要！）
CREATE INDEX idx_sys_tenant_path ON sys_tenant(path);

-- 2. parent_id 索引（查询直接子节点）
CREATE INDEX idx_sys_tenant_parent_id ON sys_tenant(parent_id);

-- 3. 复合索引（常用查询条件）
CREATE INDEX idx_sys_tenant_deleted_level ON sys_tenant(is_deleted, level);

-- 4. 租户编码唯一索引
CREATE UNIQUE INDEX uk_sys_tenant_code ON sys_tenant(tenant_code, is_deleted);

-- 5. 复合索引（树查询优化）
CREATE INDEX idx_sys_tenant_deleted_level_path ON sys_tenant(is_deleted, level, path);
```

### 6.2 索引分析

```sql
-- 查看索引使用情况
EXPLAIN SELECT * FROM sys_tenant 
WHERE is_deleted = 'NOT_DELETED' 
  AND path LIKE '/101/%' 
ORDER BY level, path;
```

**预期结果：**
- type: range
- key: idx_sys_tenant_deleted_level_path
- rows: 实际子孙数量

---

## 7. 性能测试

### 7.1 测试场景

| 场景 | 节点数 | 层级 | 优化前SQL | 优化后SQL | 性能提升 |
|-----|--------|------|----------|----------|---------|
| 小型树 | 100 | 3层 | 101次 | 1次 | 100倍 |
| 中型树 | 1000 | 5层 | 1001次 | 1次 | 1000倍 |
| 大型树 | 10000 | 7层 | 10001次 | 1次 | 10000倍 |
| 分页查询 | 10根×100子 | 3层 | 1011次 | 2次 | 500倍 |

### 7.2 测试代码

```java
@Test
public void testQueryPerformance() {
    // 准备测试数据：1000个租户，5层树
    prepareTestData(1000, 5);
    
    // 测试优化前
    long start1 = System.currentTimeMillis();
    List<TreeVO> result1 = queryTenantTreeOld();  // 旧方法
    long time1 = System.currentTimeMillis() - start1;
    
    // 测试优化后
    long start2 = System.currentTimeMillis();
    List<TreeVO> result2 = queryTenantTree();  // 新方法
    long time2 = System.currentTimeMillis() - start2;
    
    // 输出结果
    System.out.println("优化前耗时：" + time1 + "ms");
    System.out.println("优化后耗时：" + time2 + "ms");
    System.out.println("性能提升：" + (time1 / time2) + "倍");
}
```

---

## 8. 注意事项

### 8.1 数据一致性

**path 字段维护：**
- 新增节点时：path = 父path + "/" + 自己的ID
- 移动节点时：批量更新所有子孙的path
- 删除节点时：不需要更新path（逻辑删除）

**hasChildren 字段维护：**
- 添加子节点时：更新父节点 hasChildren = true
- 删除子节点时：检查并更新父节点 hasChildren
- 移动节点时：更新旧父节点和新父节点

### 8.2 环路检测

```java
/**
 * 检测是否会形成环路
 */
private void checkCircularReference(Long nodeId, Long newParentId) {
    // 查询新父节点
    SysTenant newParent = this.getById(newParentId);
    if (newParent == null) {
        throw new BusinessException("新父节点不存在");
    }
    
    // 查询当前节点
    SysTenant currentNode = this.getById(nodeId);
    if (currentNode == null) {
        throw new BusinessException("当前节点不存在");
    }
    
    // 检查：新父节点的path是否以当前节点的path开头
    String currentPath = normalizePathPrefix(currentNode.getPath());
    if (newParent.getPath().startsWith(currentPath + "/")) {
        throw new BusinessException("不能移动到自己的子孙节点下，会形成环路");
    }
}
```

### 8.3 边界情况

- **空树**：返回空列表，不抛异常
- **孤儿节点**：parent_id存在但父节点不存在 → 当作根节点
- **深度过大**：建议限制最大深度（如10层）

---

## 9. 实施计划

### 9.1 阶段一：数据库准备

- [ ] 检查path、level、hasChildren字段是否存在
- [ ] 创建必需索引
- [ ] 数据迁移（如果path字段为空）
- [ ] 验证数据一致性

### 9.2 阶段二：代码重构

- [ ] 实现 buildTreeInMemory() 通用方法
- [ ] 重构 queryTenantTree()
- [ ] 重构 querySubTree()
- [ ] 重构 queryTenantTreePage()
- [ ] 优化 deleteTenant()

### 9.3 阶段三：测试验证

- [ ] 单元测试
- [ ] 集成测试
- [ ] 性能测试
- [ ] 边界测试

### 9.4 阶段四：上线发布

- [ ] 代码审查
- [ ] 灰度发布
- [ ] 性能监控
- [ ] 回滚预案
