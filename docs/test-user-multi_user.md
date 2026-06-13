# 登录接口测试用户 - multi_user

## 🎯 推荐测试用户：`multi_user`

这是一个**多租户用户**，权限场景非常丰富，适合全面测试登录接口和权限加载功能。

---

## 📋 用户基本信息

| 字段 | 值 | 说明 |
|-----|-----|------|
| **用户ID** | 30 | 系统用户ID |
| **用户编码** | U030 | 用户唯一编码 |
| **用户名** | **multi_user** | 登录用户名 ⭐ |
| **密码** | **multi123** | 登录密码 ⭐ |
| **昵称** | 多租户用户 | 显示名称 |
| **邮箱** | multi@example.com | 联系邮箱 |
| **手机** | 13800000030 | 联系电话 |

---

## 🏢 租户绑定信息（5个租户）

### **1. 万象集团（主租户）- ENABLED**
- **租户编码：** WANXIANG (tenant_id=100)
- **策略ID：** 1028 (UP_028)
- **状态：** ✅ ENABLED（可用）
- **是否默认：** ✅ true（默认租户）
- **加入时间：** 2026-01-01
- **角色：** 待查询

### **2. 鼎新集团（附加租户）- ENABLED**
- **租户编码：** DINGXIN (tenant_id=200)
- **策略ID：** 1029 (UP_029)
- **状态：** ✅ ENABLED（可用）
- **是否默认：** ❌ false（附加租户）
- **加入时间：** 2026-02-01
- **角色：** 待查询

### **3. 星辰科技（附加租户）- ENABLED**
- **租户编码：** XINGCHEN (tenant_id=300)
- **策略ID：** 1030 (UP_030)
- **状态：** ✅ ENABLED（可用）
- **是否默认：** ❌ false（附加租户）
- **加入时间：** 2026-03-01
- **角色：** 待查询

### **4. 天翔物流（失效租户）- DISABLED**
- **租户编码：** TIANXIANG (tenant_id=500)
- **策略ID：** 1031 (UP_031)
- **状态：** ❌ DISABLED（已禁用）
- **是否默认：** ❌ false
- **禁用原因：** "租户已停用，级联禁用"
- **加入时间：** 2026-05-01

### **5. 鼎新东北（待激活租户）- PENDING**
- **租户编码：** DINGXIN_NORTHEAST (tenant_id=230)
- **策略ID：** 1032 (UP_032)
- **状态：** ⏳ PENDING（待激活）
- **是否默认：** ❌ false
- **待激活原因：** "租户待激活，权限暂不可用"
- **加入时间：** 2026-06-01

---

## 🎭 权限场景覆盖

### ✅ **已覆盖的场景**

1. **多租户切换**
   - 用户绑定了 5 个租户
   - 可以测试租户切换功能
   - 默认租户是"万象集团"

2. **租户状态分类**
   - ✅ 有效租户（ENABLED）：3个
   - ❌ 失效租户（DISABLED）：1个
   - ⏳ 待激活租户（PENDING）：1个

3. **多角色权限**
   - 在不同租户下有不同的角色
   - 可以测试角色权限合并逻辑

4. **字段权限**
   - 不同角色可能有不同的字段权限
   - 可以测试字段权限合并逻辑（DISABLED优先）

5. **数据范围**
   - 不同角色可能有不同的数据范围
   - 可以测试数据范围合并逻辑（取最宽松）

---

## 🧪 测试步骤

### **步骤1：准备测试数据**

确保数据库已导入 `docs/test-data.sql` 文件：

```sql
-- 连接到数据库
psql -U postgres -d nexusix

-- 导入测试数据
\i D:/PojectHub/NexusIX-Platform/docs/test-data.sql
```

---

### **步骤2：发起登录请求**

**接口地址：** `POST /api/auth/login`

**请求体：**
```json
{
  "username": "multi_user",
  "password": "multi123"
}
```

**cURL 示例：**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "multi_user",
    "password": "multi123"
  }'
```

---

### **步骤3：验证返回结果**

#### **3.1 验证租户分组（tenants）**

**预期结果：**
```json
{
  "tenants": {
    "current": [
      {
        "tenantCode": "WANXIANG",
        "tenantName": "万象集团",
        "status": "ACTIVE",
        "expireTime": "2026-12-31 23:59:59"
      }
    ],
    "valid": [
      {
        "tenantCode": "WANXIANG",
        "tenantName": "万象集团",
        "status": "ACTIVE"
      },
      {
        "tenantCode": "DINGXIN",
        "tenantName": "鼎新集团",
        "status": "ACTIVE"
      },
      {
        "tenantCode": "XINGCHEN",
        "tenantName": "星辰科技",
        "status": "ACTIVE"
      }
    ],
    "invalid": [
      {
        "tenantCode": "TIANXIANG",
        "tenantName": "天翔物流",
        "status": "DISABLED"
      },
      {
        "tenantCode": "DINGXIN_NORTHEAST",
        "tenantName": "鼎新东北",
        "status": "PENDING"
      }
    ]
  }
}
```

**验证点：**
- ✅ `current` 数组应该只有1个租户（万象集团，is_default=true）
- ✅ `valid` 数组应该有3个租户（ENABLED状态）
- ✅ `invalid` 数组应该有2个租户（DISABLED和PENDING状态）

---

#### **3.2 验证角色分组（roles）**

**预期结果：**
```json
{
  "roles": {
    "current": [
      {
        "roleCode": "XXX",
        "dataScope": "SELF",
        "tenantCode": "WANXIANG",
        "tenantName": "万象集团"
      }
    ],
    "all": [...],  // 所有租户的所有角色
    "valid": [...],  // 状态为ACTIVE的角色
    "invalid": [...]  // 状态非ACTIVE的角色
  }
}
```

**验证点：**
- ✅ `current` 数组只包含当前租户（万象集团）的角色
- ✅ `all` 数组包含用户在所有租户下的角色
- ✅ 每个角色都有 `dataScope` 字段

---

#### **3.3 验证部门分组（depts）**

**预期结果：**
```json
{
  "depts": {
    "current": [
      {
        "deptCode": "XXX",
        "deptName": "XXX部门",
        "path": "/100/...",
        "level": 2,
        "tenantCode": "WANXIANG"
      }
    ],
    "all": [...],
    "valid": [...],
    "invalid": [...]
  }
}
```

**验证点：**
- ✅ 部门信息按租户分组
- ✅ 包含部门路径（path）和层级（level）

---

#### **3.4 验证权限信息（permissions）**

**预期结果：**
```json
{
  "permissions": {
    "all": ["PERM_USER_VIEW", "PERM_TENANT_VIEW", ...],
    "valid": ["PERM_USER_VIEW", ...],
    "invalid": [],
    "fieldPermission": {
      "query": {
        "sys_user": {
          "operable": ["user_name", "email", "phone"],
          "inoperable": ["password", "salary"]
        },
        "sys_tenant": {
          "operable": ["tenant_name", "tenant_code"],
          "inoperable": []
        }
      },
      "create": {
        "sys_user": {
          "operable": ["user_name", "email"],
          "inoperable": ["status", "is_admin"]
        }
      },
      "update": {
        "sys_user": {
          "operable": ["email", "phone"],
          "inoperable": ["user_name", "password"]
        }
      }
    }
  }
}
```

**验证点：**
- ✅ `fieldPermission` 按操作类型分组（query/create/update）
- ✅ 每个操作类型下按表名分组
- ✅ 每个表的字段分为 `operable` 和 `inoperable`
- ✅ 验证**字段权限黑名单机制**：如果某个字段在任何一个角色中是 DISABLED，应该在 `inoperable` 中

---

## 🐛 可能遇到的问题

### **问题1：字段权限为空**

**原因：** 权限策略表（sys_perm_policy）中没有字段权限数据

**解决：** 检查 `sys_perm_policy` 表的 `field_operation` 字段是否有JSON数据

**示例数据：**
```json
{
  "user_name": ["READ", "CREATE"],
  "email": ["READ", "UPDATE"],
  "password": ["CREATE"]
}
```

---

### **问题2：数据范围都是 SELF**

**原因：** SQL查询中 `dataScope` 是硬编码的 `'SELF'`

**解决：** 后续需要从角色配置或权限策略中查询真实的数据范围

**临时方案：** 手动修改 `sys_role` 表添加 `data_scope` 字段

---

### **问题3：租户/角色/部门为空**

**原因：** 用户策略或关联数据有问题

**排查步骤：**
1. 检查 `sys_user_policy` 表是否有 user_id=30 的记录
2. 检查 `sys_role_policy` 表是否有对应的角色绑定
3. 检查 LEFT JOIN 查询是否正确

---

## 📊 预期测试结果总结

| 维度 | 预期数量/状态 | 说明 |
|-----|-------------|------|
| 绑定租户总数 | 5个 | 包含所有状态的租户 |
| 有效租户 | 3个 | ENABLED状态 |
| 失效租户 | 2个 | DISABLED+PENDING状态 |
| 当前租户 | 1个 | 万象集团（默认租户） |
| 角色数量 | ≥1个/租户 | 不同租户可能有不同角色 |
| 字段权限表数 | ≥1个 | 至少有一个表的字段权限 |
| 权限码数量 | ≥3个 | 基础查看权限 |

---

## 🚀 开始测试

使用以下登录信息进行测试：

```
用户名：multi_user
密码：multi123
```

祝测试顺利！如有问题请查看上述"可能遇到的问题"章节。
