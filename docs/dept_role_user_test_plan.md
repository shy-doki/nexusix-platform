# 部门、角色、用户策略状态测试方案

## 📋 测试范围

测试 `test_all` 用户的：
- **租户策略**的启用/禁用状态
- **部门策略**的启用/禁用状态  
- **角色策略**的启用/禁用状态
- **用户策略**（租户/部门/角色绑定）的启用/禁用状态

---

## 🎯 测试用户数据概览

### test_all 用户归属关系

| 租户 | 用户策略ID | 部门归属 | 部门策略ID | 角色归属 | 角色策略ID |
|-----|-----------|---------|-----------|---------|-----------|
| **TENANT_A** | 77001 | DEPT_RD(主) | 75001 | ROLE_ADMIN | 76001 |
| | | DEPT_MKT | 75002 | ROLE_EMPLOYEE | 76002 |
| | | DEPT_SALES | 75007 | ROLE_AUDITOR | 76007 |
| **TENANT_B** | 77002 | DEPT_MKT(主) | 75003 | ROLE_ADMIN | 76003 |
| | | DEPT_SALES | 75004 | ROLE_EMPLOYEE | 76004 |
| | | DEPT_RD | 75008 | ROLE_AUDITOR | 76008 |
| **TENANT_C** | 77003 | DEPT_SALES(主) | 75006 | ROLE_ADMIN | 76005 |
| | | DEPT_RD | 75005 | ROLE_AUDITOR | 76006 |
| | | DEPT_MKT | 75010 | ROLE_EMPLOYEE | 76009 |

---

## 🧪 测试用例

### TC-09：租户策略禁用测试

**目的：** 验证用户与租户的绑定策略禁用后，该租户的所有数据不可见

**修改SQL：**
```sql
-- 禁用test_all与租户B的绑定（用户策略77002）
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77002 
  AND user_id = 7301
  AND target_type = 'TENANT' 
  AND target_id = 7002;

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "tenantInfo": {
    "current": [
      {"tenantCode": "TENANT_A", "status": "ACTIVE", "isPrimary": true}
    ],
    "all": [
      {"tenantCode": "TENANT_A", "status": "ACTIVE", "isPrimary": true},
      {"tenantCode": "TENANT_B", "status": "INACTIVE", "isPrimary": false},  // ❌ 状态变为INACTIVE
      {"tenantCode": "TENANT_C", "status": "ACTIVE", "isPrimary": false}
    ],
    "valid": [
      {"tenantCode": "TENANT_A", "status": "ACTIVE"},
      {"tenantCode": "TENANT_C", "status": "ACTIVE"}
    ],
    "invalid": [
      {"tenantCode": "TENANT_B", "status": "INACTIVE"}  // ✅ 移到invalid
    ]
  },
  "deptInfo": {
    "all": [
      {"tenantCode": "TENANT_A", "depts": [...]},
      // ❌ 租户B的部门消失或标记为无效
      {"tenantCode": "TENANT_C", "depts": [...]}
    ]
  },
  "roleInfo": {
    "all": [
      {"tenantCode": "TENANT_A", "roles": [...]},
      // ❌ 租户B的角色消失或标记为无效
      {"tenantCode": "TENANT_C", "roles": [...]}
    ]
  },
  "permInfo": {
    "all": [
      {"tenantCode": "TENANT_A", "permissions": [...]},
      // ❌ 租户B的权限消失或标记为无效
      {"tenantCode": "TENANT_C", "permissions": [...]}
    ],
    "fieldPermissionByTenant": {
      "TENANT_A": {...},
      // ❌ 租户B的字段权限消失
      "TENANT_C": {...}
    }
  }
}
```

**验证点：**
- ✅ 租户B出现在 invalid 列表
- ✅ 租户B的部门不在 valid 列表
- ✅ 租户B的角色不在 valid 列表
- ✅ 租户B的权限不在 valid 列表
- ✅ 租户B的字段权限不返回或为空
- ✅ 租户A和C不受影响

**恢复SQL：**
```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77002;
COMMIT;
```

---

### TC-10：部门策略禁用测试

**目的：** 验证用户与部门的绑定策略禁用后，该部门数据的变化

**修改SQL：**
```sql
-- 禁用test_all在租户A的研发部绑定（用户策略77101）
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77101 
  AND user_id = 77001  -- test_all的租户A用户ID
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
  "deptInfo": {
    "current": {
      "enabled": [
        {"deptCode": "DEPT_MKT", "isPrimary": false, "userPolicyStatus": "ACTIVE"},
        {"deptCode": "DEPT_SALES", "isPrimary": false, "userPolicyStatus": "ACTIVE"}
      ],
      "disabled": [
        {"deptCode": "DEPT_RD", "isPrimary": true, "userPolicyStatus": "INACTIVE"}  // ✅ 主部门被禁用
      ]
    },
    "all": [
      {
        "tenantCode": "TENANT_A",
        "depts": [
          {"deptCode": "DEPT_MKT", "userPolicyStatus": "ACTIVE"},
          {"deptCode": "DEPT_RD", "userPolicyStatus": "INACTIVE"},  // ❌ 状态变为INACTIVE
          {"deptCode": "DEPT_SALES", "userPolicyStatus": "ACTIVE"}
        ]
      }
    ],
    "valid": [
      {
        "tenantCode": "TENANT_A",
        "depts": [
          {"deptCode": "DEPT_MKT"},
          {"deptCode": "DEPT_SALES"}
          // ❌ DEPT_RD 不在valid中
        ]
      }
    ],
    "invalid": [
      {
        "tenantCode": "TENANT_A",
        "depts": [
          {"deptCode": "DEPT_RD"}  // ✅ 移到invalid
        ]
      }
    ]
  }
}
```

**验证点：**
- ✅ DEPT_RD 从 current.enabled 移到 current.disabled
- ✅ DEPT_RD 出现在 invalid 列表
- ✅ DEPT_RD 不在 valid 列表
- ✅ userPolicyStatus 为 INACTIVE
- ✅ 其他部门不受影响
- ⚠️ 注意：主部门被禁用可能影响数据权限范围

**恢复SQL：**
```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77101;
COMMIT;
```

---

### TC-11：角色策略禁用测试

**目的：** 验证用户与角色的绑定策略禁用后，角色权限的变化

**修改SQL：**
```sql
-- 禁用test_all在租户A的管理员角色绑定（用户策略77201）
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77201 
  AND user_id = 77001  -- test_all的租户A用户ID
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
  "roleInfo": {
    "current": {
      "enabled": [
        {"roleCode": "ROLE_AUDITOR", "dataScope": "ALL"},
        {"roleCode": "ROLE_EMPLOYEE", "dataScope": "SELF"}
      ],
      "disabled": [
        {"roleCode": "ROLE_ADMIN", "dataScope": "ALL"}  // ✅ 管理员角色被禁用
      ]
    },
    "all": [
      {
        "tenantCode": "TENANT_A",
        "roles": [
          {"roleCode": "ROLE_ADMIN", "rolePolicyStatus": "INACTIVE"},  // ❌ 状态变为INACTIVE
          {"roleCode": "ROLE_AUDITOR", "rolePolicyStatus": "ACTIVE"},
          {"roleCode": "ROLE_EMPLOYEE", "rolePolicyStatus": "ACTIVE"}
        ]
      }
    ],
    "valid": [
      {
        "tenantCode": "TENANT_A",
        "roles": [
          {"roleCode": "ROLE_AUDITOR"},
          {"roleCode": "ROLE_EMPLOYEE"}
          // ❌ ROLE_ADMIN 不在valid中
        ]
      }
    ],
    "invalid": [
      {
        "tenantCode": "TENANT_A",
        "roles": [
          {"roleCode": "ROLE_ADMIN"}  // ✅ 移到invalid
        ]
      }
    ]
  },
  "permInfo": {
    "current": {
      "enabled": [
        // ❌ 可能减少了管理员角色的权限
      ]
    }
  }
}
```

**验证点：**
- ✅ ROLE_ADMIN 从 current.enabled 移到 current.disabled
- ✅ ROLE_ADMIN 出现在 invalid 列表
- ✅ ROLE_ADMIN 不在 valid 列表
- ✅ rolePolicyStatus 为 INACTIVE
- ⚠️ 注意：管理员角色权限可能影响权限列表
- ✅ 其他角色不受影响

**恢复SQL：**
```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77201;
COMMIT;
```

---

### TC-12：租户本身禁用测试

**目的：** 验证租户主表状态为DISABLED时的影响

**修改SQL：**
```sql
-- 禁用租户C
UPDATE sys_tenant 
SET status = 'DISABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7003 
  AND tenant_code = 'TENANT_C';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "tenantInfo": {
    "current": [
      {"tenantCode": "TENANT_A", "status": "ACTIVE"}
    ],
    "all": [
      {"tenantCode": "TENANT_A", "status": "ACTIVE"},
      {"tenantCode": "TENANT_B", "status": "ACTIVE"},
      {"tenantCode": "TENANT_C", "status": "DISABLED"}  // ❌ 租户本身被禁用
    ],
    "valid": [
      {"tenantCode": "TENANT_A"},
      {"tenantCode": "TENANT_B"}
      // ❌ 租户C不在valid中
    ],
    "invalid": [
      {"tenantCode": "TENANT_C"}  // ✅ 移到invalid
    ]
  },
  "deptInfo": {
    "all": [
      {"tenantCode": "TENANT_A", "depts": [...]},
      {"tenantCode": "TENANT_B", "depts": [...]},
      {"tenantCode": "TENANT_C", "depts": [...]}  // ⚠️ 可能仍存在但标记为无效
    ],
    "valid": [
      {"tenantCode": "TENANT_A"},
      {"tenantCode": "TENANT_B"}
      // ❌ 租户C的部门不在valid中
    ]
  },
  "roleInfo": {
    "valid": [
      {"tenantCode": "TENANT_A"},
      {"tenantCode": "TENANT_B"}
      // ❌ 租户C的角色不在valid中
    ]
  },
  "permInfo": {
    "valid": [
      {"tenantCode": "TENANT_A"},
      {"tenantCode": "TENANT_B"}
      // ❌ 租户C的权限不在valid中
    ]
  }
}
```

**验证点：**
- ✅ 租户C出现在 invalid 列表
- ✅ 租户C的所有关联数据（部门、角色、权限）都不在valid中
- ✅ 租户C不能作为当前租户
- ✅ 租户A和B不受影响

**恢复SQL：**
```sql
UPDATE sys_tenant 
SET status = 'ENABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7003;
COMMIT;
```

---

### TC-13：部门本身禁用测试

**目的：** 验证部门主表状态为DISABLED时的影响

**修改SQL：**
```sql
-- 禁用租户A的研发部
UPDATE sys_dept 
SET status = 'DISABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7101 
  AND dept_code = 'DEPT_RD';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "deptInfo": {
    "current": {
      "enabled": [
        {"deptCode": "DEPT_MKT"},
        {"deptCode": "DEPT_SALES"}
      ],
      "disabled": []  // ⚠️ 可能不显示，因为部门本身禁用
    },
    "all": [
      {
        "tenantCode": "TENANT_A",
        "depts": [
          {"deptCode": "DEPT_MKT"},
          {"deptCode": "DEPT_SALES"}
          // ❌ DEPT_RD 可能完全不返回
        ]
      }
    ]
  }
}
```

**验证点：**
- ✅ DEPT_RD 不在返回结果中（因为SQL查询已过滤）
- ✅ 其他租户的DEPT_RD仍然存在
- ✅ 租户A的其他部门不受影响

**恢复SQL：**
```sql
UPDATE sys_dept 
SET status = 'ENABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7101;
COMMIT;
```

---

### TC-14：角色本身禁用测试

**目的：** 验证角色主表状态为DISABLED时的影响

**修改SQL：**
```sql
-- 禁用管理员角色
UPDATE sys_role 
SET status = 'DISABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7001 
  AND role_code = 'ROLE_ADMIN';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "roleInfo": {
    "current": {
      "enabled": [
        {"roleCode": "ROLE_AUDITOR"},
        {"roleCode": "ROLE_EMPLOYEE"}
        // ❌ ROLE_ADMIN 不返回
      ]
    },
    "all": [
      {
        "tenantCode": "TENANT_A",
        "roles": [
          {"roleCode": "ROLE_AUDITOR"},
          {"roleCode": "ROLE_EMPLOYEE"}
          // ❌ ROLE_ADMIN 不返回
        ]
      }
    ]
  }
}
```

**验证点：**
- ✅ ROLE_ADMIN 不在任何返回结果中
- ✅ 所有租户的ROLE_ADMIN都不返回
- ✅ 其他角色不受影响

**恢复SQL：**
```sql
UPDATE sys_role 
SET status = 'ENABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7001;
COMMIT;
```

---

### TC-15：用户本身禁用测试

**目的：** 验证用户主表状态为DISABLED时无法登录

**修改SQL：**
```sql
-- 禁用test_all用户
UPDATE sys_user 
SET status = 'DISABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7301 
  AND user_name = 'test_all';

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "code": 400,
  "msg": "用户已被禁用",
  "success": false
}
```

**验证点：**
- ✅ 无法登录
- ✅ 返回明确的错误信息
- ✅ 不返回用户上下文数据

**恢复SQL：**
```sql
UPDATE sys_user 
SET status = 'ENABLED', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 7301;
COMMIT;
```

---

### TC-16：组合禁用测试（租户策略+部门策略）

**目的：** 验证同时禁用租户策略和部门策略的影响

**修改SQL：**
```sql
-- 禁用租户A的用户策略
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77001 
  AND user_id = 7301
  AND target_type = 'TENANT' 
  AND target_id = 7001;

-- 禁用租户A研发部的用户策略
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id = 77101 
  AND user_id = 77001
  AND target_type = 'DEPT' 
  AND target_id = 75001;

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "tenantInfo": {
    "current": [
      {"tenantCode": "TENANT_B"}  // ✅ 自动切换到租户B（因为A被禁用）
    ],
    "invalid": [
      {"tenantCode": "TENANT_A"}  // ✅ 租户A在invalid中
    ]
  },
  "deptInfo": {
    "current": {
      // ✅ 显示租户B的部门
    }
  },
  "roleInfo": {
    "current": {
      // ✅ 显示租户B的角色
    }
  }
}
```

**验证点：**
- ✅ 当前租户自动切换到有效租户
- ✅ 租户A的所有数据不可用
- ✅ 租户B和C正常显示

**恢复SQL：**
```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE id IN (77001, 77101);
COMMIT;
```

---

### TC-17：所有租户都禁用测试

**目的：** 验证所有租户策略都禁用时的行为

**修改SQL：**
```sql
-- 禁用test_all的所有租户策略
UPDATE sys_user_policy 
SET status = 'INACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE user_id = 7301
  AND target_type = 'TENANT'
  AND id IN (77001, 77002, 77003);

COMMIT;
```

**执行操作：**
1. 执行上述SQL
2. 重启应用
3. 使用 test_all/password 登录

**预期结果：**

```json
{
  "code": 400,
  "msg": "用户未关联有效租户",
  "success": false
}
```

**验证点：**
- ✅ 无法登录
- ✅ 返回明确的错误信息
- ✅ 提示用户未关联有效租户

**恢复SQL：**
```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE user_id = 7301 AND target_type = 'TENANT';
COMMIT;
```

---

## 📝 测试执行记录表

| 测试编号 | 测试场景 | 执行日期 | 执行结果 | 问题描述 |
|---------|---------|---------|---------|---------|
| TC-09 | 租户策略禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-10 | 部门策略禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-11 | 角色策略禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-12 | 租户本身禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-13 | 部门本身禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-14 | 角色本身禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-15 | 用户本身禁用 | | ⬜ PASS / ⬜ FAIL | |
| TC-16 | 组合禁用测试 | | ⬜ PASS / ⬜ FAIL | |
| TC-17 | 所有租户禁用 | | ⬜ PASS / ⬜ FAIL | |

---

## 🔍 通用验证SQL

### 查看test_all的所有用户策略

```sql
SELECT 
    up.id,
    up.policy_code,
    up.target_type,
    CASE up.target_type
        WHEN 'TENANT' THEN t.tenant_code
        WHEN 'DEPT' THEN CONCAT(td.tenant_code, '-', d.dept_code)
        WHEN 'ROLE' THEN CONCAT(tr.tenant_code, '-', r.role_code)
    END AS target,
    up.status,
    up.is_primary
FROM sys_user_policy up
LEFT JOIN sys_tenant t ON t.id = up.target_id AND up.target_type = 'TENANT'
LEFT JOIN sys_tenant_policy tp_dept ON tp_dept.id = up.target_id AND up.target_type = 'DEPT'
LEFT JOIN sys_dept d ON d.id = tp_dept.source_id
LEFT JOIN sys_tenant td ON td.id = tp_dept.tenant_id
LEFT JOIN sys_tenant_policy tp_role ON tp_role.id = up.target_id AND up.target_type = 'ROLE'
LEFT JOIN sys_role r ON r.id = tp_role.source_id
LEFT JOIN sys_tenant tr ON tr.id = tp_role.tenant_id
WHERE up.user_id = 7301
  AND up.is_deleted = 'NOT_DELETED'
ORDER BY up.target_type, up.id;
```

### 查看所有禁用的用户策略

```sql
SELECT 
    up.id,
    u.user_name,
    up.policy_code,
    up.target_type,
    up.status
FROM sys_user_policy up
INNER JOIN sys_user u ON u.id = up.user_id
WHERE up.status = 'INACTIVE'
  AND up.is_deleted = 'NOT_DELETED'
ORDER BY u.user_name, up.target_type;
```

### 一键恢复所有用户策略

```sql
UPDATE sys_user_policy 
SET status = 'ACTIVE', 
    update_by = 1, 
    update_at = CURRENT_TIMESTAMP
WHERE user_id = 7301
  AND status = 'INACTIVE'
  AND is_deleted = 'NOT_DELETED';

COMMIT;
```

---

## 🎯 测试成功标准

### 策略级禁用（TC-09~TC-11）
- ✅ 对应数据从 enabled 移到 disabled
- ✅ 对应数据从 valid 移到 invalid
- ✅ status 字段正确显示为 INACTIVE
- ✅ 其他数据不受影响

### 主表禁用（TC-12~TC-15）
- ✅ 禁用的数据不在返回结果中
- ✅ 用户/租户禁用时无法登录并返回明确错误
- ✅ 其他数据不受影响

### 组合测试（TC-16~TC-17）
- ✅ 多个禁用同时生效
- ✅ 当前租户自动切换逻辑正常
- ✅ 所有租户禁用时无法登录

---

## 📌 注意事项

1. **测试顺序建议：**
   - 先测试单一禁用（TC-09~TC-15）
   - 再测试组合禁用（TC-16~TC-17）

2. **恢复数据：**
   - 每个测试后必须恢复
   - 使用提供的恢复SQL

3. **重启应用：**
   - 每次修改数据库后重启
   - 确保缓存清除

4. **登录失败的处理：**
   - TC-15和TC-17预期无法登录
   - 验证错误信息是否准确

5. **主租户切换：**
   - TC-16可能触发主租户自动切换
   - 验证切换逻辑是否正确

---

**补充测试方案完成！结合之前的TC-01~TC-08，共17个完整测试用例！** 🎉
