-- 诊断李四权限查询问题
-- 用户ID: 3 (lisi)

-- Step 1: 检查用户基本信息
SELECT '=== Step 1: 用户基本信息 ===' as step;
SELECT id, user_name, status, is_deleted
FROM sys_user
WHERE id = 3;

-- Step 2: 检查用户的租户绑定
SELECT '=== Step 2: 用户→租户绑定 ===' as step;
SELECT id, user_id, target_type, target_id, status, is_deleted
FROM sys_user_policy
WHERE user_id = 3 AND target_type = 'TENANT';

-- Step 3: 检查用户的部门绑定
SELECT '=== Step 3: 用户→部门绑定 ===' as step;
SELECT up.id, up.user_id, up.target_type, up.target_id, up.status, up.is_deleted
FROM sys_user_policy up_tenant
INNER JOIN sys_user_policy up ON up.user_id = up_tenant.id
WHERE up_tenant.user_id = 3
  AND up_tenant.target_type = 'TENANT'
  AND up.target_type = 'DEPT';

-- Step 4: 检查租户策略表（部门→租户绑定）
SELECT '=== Step 4: 租户策略（部门→租户）===' as step;
SELECT tp.id, tp.source_type, tp.source_id, tp.tenant_id, tp.status, tp.is_deleted
FROM sys_user_policy up_tenant
INNER JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
INNER JOIN sys_tenant_policy tp ON tp.id = up_dept.target_id
WHERE up_tenant.user_id = 3
  AND up_tenant.target_type = 'TENANT'
  AND up_dept.target_type = 'DEPT';

-- Step 5: 检查部门信息
SELECT '=== Step 5: 部门信息 ===' as step;
SELECT d.id, d.dept_code, d.dept_name, d.status, d.is_deleted
FROM sys_user_policy up_tenant
INNER JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
INNER JOIN sys_tenant_policy tp ON tp.id = up_dept.target_id
INNER JOIN sys_dept d ON d.id = tp.source_id
WHERE up_tenant.user_id = 3
  AND up_tenant.target_type = 'TENANT'
  AND up_dept.target_type = 'DEPT';

-- Step 6: 检查权限策略（授予部门的权限）
SELECT '=== Step 6: 权限策略（授予部门）===' as step;
SELECT pp.id, pp.target_type, pp.target_id, pp.perm_id, pp.status, pp.is_deleted
FROM sys_perm_policy pp
WHERE pp.target_type = 'DEPT'
  AND pp.target_id = 2004;

-- Step 7: 检查权限信息
SELECT '=== Step 7: 权限信息 ===' as step;
SELECT p.id, p.perm_code, p.perm_name, p.status, p.is_deleted
FROM sys_perm p
WHERE p.id = 521;

-- Step 8: 完整查询（部门权限）
SELECT '=== Step 8: 完整查询结果 ===' as step;
SELECT
    p.perm_code,
    p.perm_name,
    t.tenant_code,
    t.tenant_name,
    pp.status as perm_policy_status,
    pp.target_type,
    pp.data_scope
FROM sys_user_policy up_tenant
INNER JOIN sys_tenant t ON t.id = up_tenant.target_id
    AND up_tenant.target_type = 'TENANT'
    AND t.status = 'ENABLED'
    AND t.is_deleted = 'NOT_DELETED'
INNER JOIN sys_user_policy up_dept ON up_dept.user_id = up_tenant.id
    AND up_dept.target_type = 'DEPT'
    AND up_dept.status = 'ACTIVE'
    AND up_dept.is_deleted = 'NOT_DELETED'
INNER JOIN sys_tenant_policy tp_dept ON tp_dept.id = up_dept.target_id
    AND tp_dept.source_type = 'DEPT'
    AND tp_dept.tenant_id = t.id
    AND tp_dept.status = 'ACTIVE'
    AND tp_dept.is_deleted = 'NOT_DELETED'
INNER JOIN sys_dept d ON d.id = tp_dept.source_id
    AND d.status = 'ENABLED'
    AND d.is_deleted = 'NOT_DELETED'
INNER JOIN sys_perm_policy pp ON pp.target_type = 'DEPT'
    AND pp.target_id = tp_dept.id
    AND pp.status = 'ACTIVE'
    AND pp.is_deleted = 'NOT_DELETED'
INNER JOIN sys_perm p ON p.id = pp.perm_id
    AND p.status = 'ENABLED'
    AND p.is_deleted = 'NOT_DELETED'
WHERE up_tenant.user_id = 3
  AND up_tenant.status = 'ACTIVE'
  AND up_tenant.is_deleted = 'NOT_DELETED';
