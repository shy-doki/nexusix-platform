# test_all 用户完整测试方案

## 📋 测试目标

测试 `test_all` 用户登录接口返回的数据完整性和准确性，包括：
- 租户信息
- 部门信息
- 角色信息
- 权限信息（启用/禁用状态）
- 字段权限（按租户分组）
- 禁用权限详情（按级别分类）

---

## 🎯 测试用户信息

**用户：** test_all (ID: 7301)

**归属关系：**
- 租户A（主租户）：3个部门，3个角色
- 租户B（辅助租户）：3个部门，3个角色
- 租户C（辅助租户）：3个部门，3个角色

---

## 📊 测试矩阵

| 测试编号 | 测试场景 | 修改对象 | 修改级别 | 预期影响范围 |
|---------|---------|---------|---------|------------|
| TC-01 | 基线测试（全部启用） | 无 | - | 所有权限启用 |
| TC-02 | 租户级禁用 | 租户A的权限策略 | TENANT | 影响租户A所有用户 |
| TC-03 | 部门级禁用 | 租户A研发部的权限策略 | DEPT | 影响租户A研发部用户 |
| TC-04 | 角色级禁用 | 租户A管理员的权限策略 | ROLE | 影响租户A管理员角色 |
| TC-05 | 用户级禁用 | test_all用户的权限策略 | USER | 仅影响test_all |
| TC-06 | 多级禁用组合 | 同时禁用多个级别 | 混合 | 测试优先级和去重 |
| TC-07 | 跨租户测试 | 同时修改多个租户 | 混合 | 测试租户隔离 |
| TC-08 | 字段权限禁用 | 禁用字段权限策略 | 混合 | 字段权限消失 |

---

## 🧪 详细测试用例

### TC-01：基线测试（全部启用）

**目的：** 验证初始状态所有权限都正常启用

**前置条件：** 确保所有策略状态为 `ACTIVE`

**验证SQL：**
```sql
-- 检查是否有禁用的策略
SELECT pp.id, pp.policy_code, pp.perm_id, pp.target_type, pp.target_id, pp.status
FROM sys_perm_policy pp
WHERE pp.status != 'ACTIVE'
  AND pp.is_deleted = 'NOT_DELETED';

-- 预期结果：0行（所有策略都是ACTIVE）
```

**执行操作：**
1. 使用 test_all/password 登录
2. 获取返回结果

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A"}
      ],
      "disabled": []
    },
    "fieldPermissionByTenant": {
      "TENANT_A": {
        "query": {
          "sys_user": {"operable": ["email", "user_name", "phone"]},
          "product": {"operable": ["name", "price"]}
        },
        "update": {
          "sys_user": {"operable": ["user_name"]},
          "product": {"operable": ["price"]}
        }
      },
      "TENANT_B": {
        "query": {
          "sys_log": {"operable": ["level", "content"]},
          "biz_order": {"operable": ["amount", "order_no"]},
          "salary": {"operable": ["amount"]}
        },
        "update": {
          "biz_order": {"operable": ["amount"]},
          "salary": {"operable": ["amount"]}
        }
      },
      "TENANT_C": {
        "query": {
          "biz_order": {"operable": ["amount", "order_no"]},
          "salary": {"operable": ["amount"]}
        },
        "update": {
          "biz_order": {"operable": ["amount"]}
        }
      }
    },
    "disabledDetailByTenant": {
      "TENANT_A": {"system": [], "tenant": [], "dept": [], "role": [], "user": []},
      "TENANT_B": {"system": [], "tenant": [], "dept": [], "role": [], "user": []},
      "TENANT_C": {"system": [], "tenant": [], "dept": [], "role": [], "user": []}
    }
  }
}
```

**验证点：**
- ✅ current.enabled 包含租户A的4个权限
- ✅ current.disabled 为空
- ✅ fieldPermissionByTenant 包含3个租户的字段权限
- ✅ disabledDetailByTenant 所有列表都为空

---

### TC-02：租户级禁用测试

**目的：** 验证租户级权限策略禁用后的影响

**修改SQL：**
```sql
-- 禁用租户A的DATA_USER权限（策略80002）
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 80002 
  AND target_type = 'TENANT' 
  AND target_id = 7001;

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用（确保缓存清除）
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A"}
      ],
      "disabled": [
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A", "permPolicyStatus": "INACTIVE"}
      ]
    },
    "fieldPermissionByTenant": {
      "TENANT_A": {
        "query": {
          "product": {"operable": ["name", "price"]}
          // ❌ sys_user 的字段权限消失
        },
        "update": {
          "product": {"operable": ["price"]}
          // ❌ sys_user 的字段权限消失
        }
      }
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "system": [],
        "tenant": ["DATA_USER"],  // ✅ 出现在tenant级别
        "dept": [],
        "role": [],
        "user": []
      }
    }
  }
}
```

**验证点：**
- ✅ DATA_USER 从 enabled 移到 disabled
- ✅ sys_user 的字段权限消失
- ✅ disabledDetailByTenant.TENANT_A.tenant 包含 ["DATA_USER"]
- ✅ 其他租户不受影响

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 80002;
COMMIT;
```

---

### TC-03：部门级禁用测试

**目的：** 验证部门级权限策略禁用后的影响

**修改SQL：**
```sql
-- 禁用租户A研发部的DATA_USER权限（策略81001）
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 81001 
  AND target_type = 'DEPT' 
  AND target_id = 75001;  -- 研发部(租户A)

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A"}
      ],
      "disabled": [
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A", "permPolicyStatus": "INACTIVE"}
      ]
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "system": [],
        "tenant": [],
        "dept": ["DATA_USER"],  // ✅ 出现在dept级别
        "role": [],
        "user": []
      }
    }
  }
}
```

**验证点：**
- ✅ DATA_USER 出现在 disabled 列表
- ✅ disabledDetailByTenant.TENANT_A.dept 包含 ["DATA_USER"]
- ✅ 字段权限受影响

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 81001;
COMMIT;
```

---

### TC-04：角色级禁用测试

**目的：** 验证角色级权限策略禁用后的影响

**修改SQL：**
```sql
-- 禁用租户A管理员的DATA_USER权限（策略82001）
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 82001 
  AND target_type = 'ROLE' 
  AND target_id = 76001;  -- 管理员(租户A)

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A"}
      ],
      "disabled": [
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A", "permPolicyStatus": "INACTIVE"}
      ]
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "system": [],
        "tenant": [],
        "dept": [],
        "role": ["DATA_USER"],  // ✅ 出现在role级别
        "user": []
      }
    }
  }
}
```

**验证点：**
- ✅ DATA_USER 出现在 disabled 列表
- ✅ disabledDetailByTenant.TENANT_A.role 包含 ["DATA_USER"]

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 82001;
COMMIT;
```

---

### TC-05：用户级禁用测试

**目的：** 验证用户级权限策略禁用后的影响

**修改SQL：**
```sql
-- 禁用test_all用户的MENU_REPORT权限（策略83003）
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 83003 
  AND target_type = 'USER' 
  AND target_id = 77001;  -- test_all(租户A)

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"}
      ],
      "disabled": [
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A", "permPolicyStatus": "INACTIVE"}
      ]
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "system": [],
        "tenant": [],
        "dept": [],
        "role": [],
        "user": ["MENU_REPORT"]  // ✅ 出现在user级别
      }
    }
  }
}
```

**验证点：**
- ✅ MENU_REPORT 从 enabled 移到 disabled
- ✅ disabledDetailByTenant.TENANT_A.user 包含 ["MENU_REPORT"]
- ✅ 其他权限不受影响

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 83003;
COMMIT;
```

---

### TC-06：多级禁用组合测试

**目的：** 验证同一权限在多个级别同时禁用时的行为

**修改SQL：**
```sql
-- 同时禁用DATA_USER的租户级、部门级、角色级策略
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id IN (80002, 81001, 82001)  -- TENANT、DEPT、ROLE级
  AND perm_id = 80002  -- DATA_USER权限
  AND is_deleted = 'NOT_DELETED';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "disabled": [
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A", "permPolicyStatus": "INACTIVE"}
      ]
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "system": [],
        "tenant": ["DATA_USER"],  // ✅ 租户级
        "dept": ["DATA_USER"],    // ✅ 部门级
        "role": ["DATA_USER"],    // ✅ 角色级
        "user": []
      }
    }
  }
}
```

**验证点：**
- ✅ DATA_USER 出现在 disabled 列表（仅出现一次，已去重）
- ✅ disabledDetailByTenant 的 tenant/dept/role 都包含 ["DATA_USER"]
- ✅ 测试去重逻辑正常

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id IN (80002, 81001, 82001);
COMMIT;
```

---

### TC-07：跨租户测试

**目的：** 验证不同租户的禁用权限相互隔离

**修改SQL：**
```sql
-- 租户A：禁用DATA_USER
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 80002 
  AND target_type = 'TENANT' 
  AND target_id = 7001;

-- 租户B：禁用DATA_ORDER
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 80006 
  AND target_type = 'TENANT' 
  AND target_id = 7002;

-- 租户C：禁用MENU_SYSTEM
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 80009 
  AND target_type = 'TENANT' 
  AND target_id = 7003;

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "current": {
      "enabled": [
        {"permCode": "DATA_PRODUCT", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_DASHBOARD", "tenantCode": "TENANT_A"},
        {"permCode": "MENU_REPORT", "tenantCode": "TENANT_A"}
      ],
      "disabled": [
        {"permCode": "DATA_USER", "tenantCode": "TENANT_A"}
      ]
    },
    "all": [
      {
        "tenantCode": "TENANT_A",
        "permissions": [
          {"permCode": "DATA_PRODUCT"},
          {"permCode": "DATA_USER", "permPolicyStatus": "INACTIVE"},
          {"permCode": "MENU_DASHBOARD"},
          {"permCode": "MENU_REPORT"}
        ]
      },
      {
        "tenantCode": "TENANT_B",
        "permissions": [
          {"permCode": "DATA_LOG"},
          {"permCode": "DATA_ORDER", "permPolicyStatus": "INACTIVE"},
          {"permCode": "MENU_BUSINESS"},
          {"permCode": "MENU_MONITOR"}
        ]
      },
      {
        "tenantCode": "TENANT_C",
        "permissions": [
          {"permCode": "MENU_REPORT"},
          {"permCode": "MENU_SYSTEM", "permPolicyStatus": "INACTIVE"}
        ]
      }
    ],
    "disabledDetailByTenant": {
      "TENANT_A": {
        "tenant": ["DATA_USER"],  // ✅ 租户A禁用
        "dept": [], "role": [], "user": []
      },
      "TENANT_B": {
        "tenant": ["DATA_ORDER"],  // ✅ 租户B禁用
        "dept": [], "role": [], "user": []
      },
      "TENANT_C": {
        "tenant": ["MENU_SYSTEM"],  // ✅ 租户C禁用
        "dept": [], "role": [], "user": []
      }
    }
  }
}
```

**验证点：**
- ✅ 每个租户的禁用权限相互独立
- ✅ current 只显示当前租户（TENANT_A）的禁用权限
- ✅ disabledDetailByTenant 每个租户都有独立的禁用列表
- ✅ 租户隔离正常

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id IN (80002, 80006, 80009);
COMMIT;
```

---

### TC-08：字段权限禁用测试

**目的：** 验证禁用字段权限策略后字段权限的变化

**修改SQL：**
```sql
-- 禁用租户A的部门级字段权限（策略81002，phone字段）
UPDATE sys_perm_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 81002 
  AND target_type = 'DEPT' 
  AND target_id = 75001
  AND table_name = 'sys_user';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "permInfo": {
    "fieldPermissionByTenant": {
      "TENANT_A": {
        "query": {
          "sys_user": {
            "operable": ["email", "user_name"],  // ❌ phone 消失
            "inoperable": ["phone"]  // ✅ phone 变为不可操作
          },
          "product": {
            "operable": ["name", "price"]
          }
        },
        "update": {
          "sys_user": {"operable": ["user_name"]},
          "product": {"operable": ["price"]}
        }
      }
    },
    "disabledDetailByTenant": {
      "TENANT_A": {
        "dept": ["DATA_USER"]  // ✅ 部门级禁用
      }
    }
  }
}
```

**验证点：**
- ✅ sys_user.phone 从 operable 移到 inoperable
- ✅ 其他字段权限不受影响
- ✅ disabledDetailByTenant 正确记录

**恢复SQL：**
```sql
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 81002;
COMMIT;
```

---

## 📝 测试执行记录表

| 测试编号 | 执行日期 | 执行人 | 执行结果 | 问题描述 | 备注 |
|---------|---------|-------|---------|---------|------|
| TC-01 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-02 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-03 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-04 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-05 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-06 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-07 | | | ⬜ PASS / ⬜ FAIL | | |
| TC-08 | | | ⬜ PASS / ⬜ FAIL | | |

---

## 🔍 通用验证SQL

### 1. 查看当前所有禁用的策略

```sql
SELECT 
    pp.id,
    pp.policy_code,
    p.perm_code,
    pp.target_type,
    pp.status,
    CASE pp.target_type
        WHEN 'TENANT' THEN t.tenant_code
        WHEN 'DEPT' THEN d.dept_code
        WHEN 'ROLE' THEN r.role_code
        WHEN 'USER' THEN u.user_code
    END AS target_code
FROM sys_perm_policy pp
LEFT JOIN sys_perm p ON p.id = pp.perm_id
LEFT JOIN sys_tenant t ON t.id = pp.target_id AND pp.target_type = 'TENANT'
LEFT JOIN sys_dept d ON d.id = pp.target_id AND pp.target_type = 'DEPT'
LEFT JOIN sys_role r ON r.id = pp.target_id AND pp.target_type = 'ROLE'
LEFT JOIN sys_user u ON u.id = pp.target_id AND pp.target_type = 'USER'
WHERE pp.status = 'INACTIVE'
  AND pp.is_deleted = 'NOT_DELETED'
ORDER BY pp.target_type, pp.id;
```

### 2. 查看test_all用户的所有权限策略

```sql
SELECT 
    p.perm_code,
    p.perm_name,
    t.tenant_code,
    pp.target_type,
    pp.status,
    pp.table_name,
    pp.field_operation
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
INNER JOIN sys_perm_policy pp ON pp.target_id = up_tenant.id AND pp.target_type = 'USER'
INNER JOIN sys_perm p ON p.id = pp.perm_id
WHERE up_tenant.user_id = 7301
  AND up_tenant.target_type = 'TENANT'
  AND up_tenant.is_deleted = 'NOT_DELETED'
ORDER BY t.tenant_code, p.perm_code;
```

### 3. 一键恢复所有策略为ACTIVE

```sql
-- ⚠️ 谨慎使用：恢复所有策略为启用状态
UPDATE sys_perm_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE status = 'INACTIVE'
  AND is_deleted = 'NOT_DELETED';

COMMIT;
```

---

## 🎯 测试成功标准

### 整体通过标准
- ✅ 所有8个测试用例PASS
- ✅ 禁用权限正确显示在 disabled 列表
- ✅ disabledDetailByTenant 正确分类
- ✅ 字段权限正确处理禁用状态
- ✅ 租户隔离正常
- ✅ 无异常日志

### 单个用例通过标准
- ✅ 返回的JSON结构完整
- ✅ enabled/disabled 列表准确
- ✅ disabledDetailByTenant 各级别列表准确
- ✅ fieldPermissionByTenant 准确
- ✅ 数据与预期100%一致

---

## 📌 注意事项

1. **测试前备份数据库**
   ```bash
   pg_dump -U postgres -d nexusix > backup_before_test.sql
   ```

2. **每个测试后恢复初始状态**
   - 执行对应的恢复SQL
   - 或重新导入备份

3. **重启应用**
   - 每次修改数据库后需要重启应用
   - 确保缓存清除

4. **记录测试结果**
   - 保存每次测试的完整返回JSON
   - 记录与预期不符的地方
   - 截图或日志保存

5. **问题排查**
   - 如果结果不符，先检查SQL是否执行成功
   - 检查应用日志是否有异常
   - 验证test_all用户的归属关系是否正确

---

## 🚀 快速测试脚本

```sql
-- 开始测试前：备份当前状态
CREATE TEMP TABLE temp_perm_policy_backup AS 
SELECT * FROM sys_perm_policy WHERE id IN (80002, 81001, 82001, 83003, 80006, 80009, 81002);

-- 测试完成后：恢复
UPDATE sys_perm_policy pp
SET status = backup.status,
    update_by = backup.update_by,
    update_at = backup.update_at
FROM temp_perm_policy_backup backup
WHERE pp.id = backup.id;

DROP TABLE temp_perm_policy_backup;
COMMIT;
```

---

**测试准备完成！请按照测试用例逐个执行测试！** 🧪
