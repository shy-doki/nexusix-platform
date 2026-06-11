-- =============================================
-- 1. 租户表
-- =============================================
CREATE TABLE sys_tenant (
                            id              BIGINT        NOT NULL PRIMARY KEY,
                            tenant_code     VARCHAR(50)   NOT NULL,
                            tenant_name     VARCHAR(100)  NOT NULL,
                            tenant_type     VARCHAR(50)   NOT NULL,
                            tenant_desc     VARCHAR(500)  DEFAULT NULL,
                            tenant_logo_url VARCHAR(500)  DEFAULT NULL,
                            parent_id       BIGINT        NOT NULL,
                            path            VARCHAR(1000) NOT NULL,
                            contact_name    VARCHAR(50)   NOT NULL,
                            contact_phone   VARCHAR(20)   NOT NULL,
                            expire_time     TIMESTAMP(6)  NOT NULL,
                            package_id      BIGINT        NOT NULL,
                            ext_attributes  JSONB         DEFAULT '{}',
                            has_children    BOOLEAN       NOT NULL,
                            status          VARCHAR(20)   NOT NULL,
                            disable_reason  VARCHAR(200)  DEFAULT NULL,
                            create_tenant   BIGINT        NOT NULL,
                            create_dept     BIGINT        NOT NULL,
                            create_role     BIGINT        NOT NULL,
                            create_by       BIGINT        NOT NULL,
                            create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            update_by       BIGINT        NOT NULL,
                            update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            is_deleted      VARCHAR(20)   NOT NULL,
                            deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_tenant IS '租户表';
COMMENT ON COLUMN sys_tenant.id IS '主键ID';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_type IS '租户类型';
COMMENT ON COLUMN sys_tenant.tenant_desc IS '租户描述';
COMMENT ON COLUMN sys_tenant.tenant_logo_url IS '租户LOGO';
COMMENT ON COLUMN sys_tenant.parent_id IS '父租户ID';
COMMENT ON COLUMN sys_tenant.path IS '租户层级路径';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_id IS '主套餐ID';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性';
COMMENT ON COLUMN sys_tenant.has_children IS '是否存在子租户';
COMMENT ON COLUMN sys_tenant.status IS '实体状态';
COMMENT ON COLUMN sys_tenant.disable_reason IS '实体禁用原因';
COMMENT ON COLUMN sys_tenant.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_tenant.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_tenant.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_tenant.create_at IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_tenant.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_tenant.deleted_at IS '删除时间';

-- =============================================
-- 2. 部门表（实体，可选层级，数据权限控制）
-- =============================================
CREATE TABLE sys_dept (
                          id              BIGINT       NOT NULL PRIMARY KEY,
                          dept_code       VARCHAR(50)  NOT NULL,
                          dept_name       VARCHAR(100) NOT NULL,
                          dept_desc       VARCHAR(200) NOT NULL,
                          parent_id       BIGINT       NOT NULL,
                          path            VARCHAR(1000) NOT NULL,
                          tenant_id       BIGINT       NOT NULL,
                          leader_id       BIGINT       NOT NULL,
                          status          VARCHAR(20)  NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,
                          create_tenant   BIGINT       NOT NULL,
                          create_dept     BIGINT       NOT NULL,
                          create_role     BIGINT       NOT NULL,
                          create_by       BIGINT       NOT NULL,
                          create_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_by       BIGINT       NOT NULL,
                          update_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          is_deleted      VARCHAR(20)  NOT NULL,
                          deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_dept IS '部门表';
COMMENT ON COLUMN sys_dept.id IS '主键ID';
COMMENT ON COLUMN sys_dept.dept_code IS '部门编码';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.dept_desc IS '部门描述';
COMMENT ON COLUMN sys_dept.parent_id IS '父部门ID';
COMMENT ON COLUMN sys_dept.path IS '部门层级路径';
COMMENT ON COLUMN sys_dept.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_dept.leader_id IS '部门负责人用户ID';
COMMENT ON COLUMN sys_dept.status IS '实体状态';
COMMENT ON COLUMN sys_dept.disable_reason IS '实体禁用原因';
COMMENT ON COLUMN sys_dept.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_dept.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_dept.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_dept.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_dept.create_at IS '创建时间';
COMMENT ON COLUMN sys_dept.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_dept.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_dept.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_dept.deleted_at IS '删除时间';

-- =============================================
-- 3. 角色表（实体，权限的集合）
-- =============================================
CREATE TABLE sys_role (
                          id              BIGINT       NOT NULL PRIMARY KEY,
                          role_code       VARCHAR(100) NOT NULL,
                          role_name       VARCHAR(100) NOT NULL,
                          role_desc       VARCHAR(200) NOT NULL,
                          role_level      VARCHAR(20)  NOT NULL,
                          tenant_id       BIGINT       NOT NULL,
                          data_scope      VARCHAR(20)  NOT NULL,
                          status          VARCHAR(20)  NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,
                          create_tenant   BIGINT       NOT NULL,
                          create_dept     BIGINT       NOT NULL,
                          create_role     BIGINT       NOT NULL,
                          create_by       BIGINT       NOT NULL,
                          create_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_by       BIGINT       NOT NULL,
                          update_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          is_deleted      VARCHAR(20)  NOT NULL,
                          deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.role_level IS '角色层级';
COMMENT ON COLUMN sys_role.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_role.data_scope IS '数据权限范围';
COMMENT ON COLUMN sys_role.status IS '实体状态';
COMMENT ON COLUMN sys_role.disable_reason IS '实体禁用原因';
COMMENT ON COLUMN sys_role.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_role.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_role.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_role.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_role.create_at IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_role.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_role.deleted_at IS '删除时间';

-- =============================================
-- 4. 用户表（实体）
-- =============================================
CREATE TABLE sys_user (
                          id              BIGINT        NOT NULL PRIMARY KEY,
                          user_code       VARCHAR(50)   NOT NULL,
                          user_name       VARCHAR(50)   NOT NULL,
                          nick_name       VARCHAR(50)   NOT NULL,
                          email           VARCHAR(100)  NOT NULL,
                          phone           VARCHAR(20)   NOT NULL,
                          password        VARCHAR(200)  NOT NULL,
                          avatar_url      VARCHAR(500)  NOT NULL,
                          gender          VARCHAR(10)   NOT NULL DEFAULT 'UNKNOWN',
                          birthday        DATE          NOT NULL,
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.user_code IS '用户编码';
COMMENT ON COLUMN sys_user.user_name IS '用户名';
COMMENT ON COLUMN sys_user.nick_name IS '昵称';
COMMENT ON COLUMN sys_user.email IS '电子邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号码';
COMMENT ON COLUMN sys_user.password IS '密码哈希值';
COMMENT ON COLUMN sys_user.avatar_url IS '头像URL';
COMMENT ON COLUMN sys_user.gender IS '性别';
COMMENT ON COLUMN sys_user.birthday IS '出生日期';
COMMENT ON COLUMN sys_user.status IS '实体状态';
COMMENT ON COLUMN sys_user.disable_reason IS '实体禁用原因';
COMMENT ON COLUMN sys_user.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_user.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_user.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_user.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_user.create_at IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_user.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除标记：';
COMMENT ON COLUMN sys_user.deleted_at IS '删除时间';

-- =============================================
-- 5. 权限表（实体，系统级全局共享）
-- =============================================
CREATE TABLE sys_perm (
                          id              BIGINT        NOT NULL PRIMARY KEY,
                          perm_code       VARCHAR(100)  NOT NULL,
                          perm_name       VARCHAR(100)  NOT NULL,
                          perm_desc       VARCHAR(200)  NOT NULL,
                          perm_type       VARCHAR(20)   NOT NULL,
                          parent_id       BIGINT        NOT NULL,
                          path            VARCHAR(1000) NOT NULL,
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_perm IS '权限表';
COMMENT ON COLUMN sys_perm.id IS '主键ID';
COMMENT ON COLUMN sys_perm.perm_code IS '权限编码';
COMMENT ON COLUMN sys_perm.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm.perm_desc IS '权限描述';
COMMENT ON COLUMN sys_perm.perm_type IS '权限类型';
COMMENT ON COLUMN sys_perm.parent_id IS '父权限ID';
COMMENT ON COLUMN sys_perm.path IS '权限层级路径';
COMMENT ON COLUMN sys_perm.status IS '实体状态';
COMMENT ON COLUMN sys_perm.disable_reason IS '实体禁用原因';
COMMENT ON COLUMN sys_perm.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_perm.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_perm.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_perm.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_perm.create_at IS '创建时间';
COMMENT ON COLUMN sys_perm.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_perm.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_perm.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_perm.deleted_at IS '删除时间';

-- =============================================
-- 6. 租户策略表（关系：用户 ↔ 租户）
-- =============================================
CREATE TABLE sys_tenant_policy (
                                   id              BIGINT        NOT NULL PRIMARY KEY,
                                   policy_code     VARCHAR(100)  NOT NULL,
                                   policy_name     VARCHAR(100)  NOT NULL,
                                   user_id         BIGINT        NOT NULL,
                                   target_type     VARCHAR(20)   NOT NULL DEFAULT 'TENANT',
                                   target_id       BIGINT        NOT NULL,
                                   is_primary      BOOLEAN       NOT NULL,
                                   join_time       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   status          VARCHAR(20)   NOT NULL,
                                   disable_reason  VARCHAR(200)  DEFAULT NULL,
                                   create_tenant   BIGINT        NOT NULL,
                                   create_dept     BIGINT        NOT NULL,
                                   create_role     BIGINT        NOT NULL,
                                   create_by       BIGINT        NOT NULL,
                                   create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   update_by       BIGINT        NOT NULL,
                                   update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted      VARCHAR(20)   NOT NULL,
                                   deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_tenant_policy IS '租户策略表' ;
COMMENT ON COLUMN sys_tenant_policy.id IS '主键ID';
COMMENT ON COLUMN sys_tenant_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_tenant_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_tenant_policy.user_id IS '用户ID';
COMMENT ON COLUMN sys_tenant_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_tenant_policy.target_id IS '目标租户ID';
COMMENT ON COLUMN sys_tenant_policy.is_primary IS '是否为主租户';
COMMENT ON COLUMN sys_tenant_policy.join_time IS '加入时间';
COMMENT ON COLUMN sys_tenant_policy.status IS '关系状态';
COMMENT ON COLUMN sys_tenant_policy.disable_reason IS '关系禁用原因';
COMMENT ON COLUMN sys_tenant_policy.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_tenant_policy.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_tenant_policy.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_tenant_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_tenant_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_tenant_policy.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_tenant_policy.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_tenant_policy.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_tenant_policy.deleted_at IS '删除时间';

-- =============================================
-- 7. 部门策略表（关系：用户 ↔ 部门）
-- =============================================
CREATE TABLE sys_dept_policy (
                                 id              BIGINT        NOT NULL PRIMARY KEY,
                                 policy_code     VARCHAR(100)  NOT NULL,
                                 policy_name     VARCHAR(100)  NOT NULL,
                                 user_id         BIGINT        NOT NULL,
                                 target_type     VARCHAR(20)   NOT NULL,
                                 target_id       BIGINT        NOT NULL,
                                 is_primary      BOOLEAN       NOT NULL,
                                 join_time       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 status          VARCHAR(20)   NOT NULL,
                                 disable_reason  VARCHAR(200)  DEFAULT NULL,
                                 create_tenant   BIGINT        NOT NULL,
                                 create_dept     BIGINT        NOT NULL,
                                 create_role     BIGINT        NOT NULL,
                                 create_by       BIGINT        NOT NULL,
                                 create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 update_by       BIGINT        NOT NULL,
                                 update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted      VARCHAR(20)   NOT NULL,
                                 deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_dept_policy IS '部门策略表';
COMMENT ON COLUMN sys_dept_policy.id IS '主键ID';
COMMENT ON COLUMN sys_dept_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_dept_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_dept_policy.user_id IS '用户ID';
COMMENT ON COLUMN sys_dept_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_dept_policy.target_id IS '目标部门ID';
COMMENT ON COLUMN sys_dept_policy.is_primary IS '是否为主部门';
COMMENT ON COLUMN sys_dept_policy.join_time IS '加入时间';
COMMENT ON COLUMN sys_dept_policy.status IS '关系状态';
COMMENT ON COLUMN sys_dept_policy.disable_reason IS '关系禁用原因';
COMMENT ON COLUMN sys_dept_policy.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_dept_policy.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_dept_policy.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_dept_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_dept_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_dept_policy.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_dept_policy.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_dept_policy.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_dept_policy.deleted_at IS '删除时间';

-- =============================================
-- 8. 角色策略表（关系：角色 → 用户/部门）
-- =============================================
CREATE TABLE sys_role_policy (
                                 id              BIGINT        NOT NULL PRIMARY KEY,
                                 policy_code     VARCHAR(100)  NOT NULL,
                                 policy_name     VARCHAR(100)  NOT NULL,
                                 role_id         BIGINT        NOT NULL,
                                 target_type     VARCHAR(20)   NOT NULL,
                                 target_id       BIGINT        NOT NULL,
                                 status          VARCHAR(20)   NOT NULL,
                                 disable_reason  VARCHAR(200)  DEFAULT NULL,
                                 create_tenant   BIGINT        NOT NULL,
                                 create_dept     BIGINT        NOT NULL,
                                 create_role     BIGINT        NOT NULL,
                                 create_by       BIGINT        NOT NULL,
                                 create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 update_by       BIGINT        NOT NULL,
                                 update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted      VARCHAR(20)   NOT NULL,
                                 deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_role_policy IS '角色策略表';
COMMENT ON COLUMN sys_role_policy.id IS '主键ID';
COMMENT ON COLUMN sys_role_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_role_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_role_policy.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_role_policy.target_id IS '目标实体ID';
COMMENT ON COLUMN sys_role_policy.status IS '关系状态：ACTIVE';
COMMENT ON COLUMN sys_role_policy.disable_reason IS '关系禁用原因';
COMMENT ON COLUMN sys_role_policy.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_role_policy.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_role_policy.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_role_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_role_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_role_policy.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_role_policy.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_role_policy.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_role_policy.deleted_at IS '删除时间';

-- =============================================
-- 9. 权限策略表（关系：权限 → 角色/用户/租户）
-- =============================================
CREATE TABLE sys_perm_policy (
                                 id              BIGINT        NOT NULL PRIMARY KEY,
                                 policy_code     VARCHAR(100)  NOT NULL,
                                 policy_name     VARCHAR(100)  NOT NULL,
                                 perm_id         BIGINT        NOT NULL,
                                 target_type     VARCHAR(20)   NOT NULL,
                                 target_id       BIGINT        NOT NULL,
                                 status          VARCHAR(20)   NOT NULL,
                                 disable_reason  VARCHAR(200)  DEFAULT NULL,
                                 create_tenant   BIGINT        NOT NULL,
                                 create_dept     BIGINT        NOT NULL,
                                 create_role     BIGINT        NOT NULL,
                                 create_by       BIGINT        NOT NULL,
                                 create_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 update_by       BIGINT        NOT NULL,
                                 update_at       TIMESTAMP(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted      VARCHAR(20)   NOT NULL,
                                 deleted_at      TIMESTAMP(6)
);

COMMENT ON TABLE sys_perm_policy IS '权限策略表';
COMMENT ON COLUMN sys_perm_policy.id IS '主键ID';
COMMENT ON COLUMN sys_perm_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_perm_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_perm_policy.perm_id IS '权限ID';
COMMENT ON COLUMN sys_perm_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_perm_policy.target_id IS '目标实体ID';
COMMENT ON COLUMN sys_perm_policy.status IS '关系状态：';
COMMENT ON COLUMN sys_perm_policy.disable_reason IS '关系禁用原因';
COMMENT ON COLUMN sys_perm_policy.create_tenant IS '创建时所属租户ID';
COMMENT ON COLUMN sys_perm_policy.create_dept IS '创建时所属部门ID';
COMMENT ON COLUMN sys_perm_policy.create_role IS '创建时使用角色ID';
COMMENT ON COLUMN sys_perm_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_perm_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_perm_policy.update_by IS '最后更新人用户ID';
COMMENT ON COLUMN sys_perm_policy.update_at IS '最后更新时间';
COMMENT ON COLUMN sys_perm_policy.is_deleted IS '逻辑删除标记';
COMMENT ON COLUMN sys_perm_policy.deleted_at IS '删除时间';


-- =============================================
-- 租户表数据 (10条)
-- =============================================
INSERT INTO sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, contact_name, contact_phone, expire_time, package_id, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                                                                                                                               (101, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团，覆盖华东、华南、华北等区域', 'https://logo.example.com/wx.png', 0, '/101', '张总', '13800001001', '2030-12-31 23:59:59', 1, '{"scale":5000, "region":"全国", "industry":"综合"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (102, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dx.png', 0, '/102', '丁总', '13800002001', '2032-06-30 23:59:59', 2, '{"scale":3000, "region":"全国", "industry":"制造业"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (111, 'WX_EAST', '万象集团-华东分公司', 'BRANCH', '负责华东区域业务运营', 'https://logo.example.com/wx_east.png', 101, '/101/111', '李经理', '13800001002', '2030-12-31 23:59:59', 1, '{"scale":1200, "region":"华东", "industry":"综合"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (112, 'WX_SOUTH', '万象集团-华南分公司', 'BRANCH', '负责华南区域业务运营', 'https://logo.example.com/wx_south.png', 101, '/101/112', '陈经理', '13800001003', '2030-12-31 23:59:59', 1, '{"scale":1000, "region":"华南", "industry":"综合"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (113, 'WX_NORTH', '万象集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/wx_north.png', 101, '/101/113', '武经理', '13800001004', '2030-12-31 23:59:59', 1, '{"scale":800, "region":"华北", "industry":"综合"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (121, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'OFFICE', '上海地区业务运营中心', 'https://logo.example.com/wx_sh.png', 111, '/101/111/121', '王主管', '13800001005', '2030-12-31 23:59:59', 1, '{"scale":300, "region":"上海", "industry":"综合"}', FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (122, 'WX_EAST_NJ', '万象集团-华东-南京办事处', 'OFFICE', '南京地区业务运营中心（已停用）', 'https://logo.example.com/wx_nj.png', 111, '/101/111/122', '赵主管', '13800001006', '2030-12-31 23:59:59', 1, '{"scale":50, "region":"南京", "industry":"综合"}', FALSE, 'DISABLED', '业务调整，合并至上海办事处', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-06-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (201, 'DX_CENTRAL', '鼎新集团-华中分公司', 'BRANCH', '负责华中区域业务运营', 'https://logo.example.com/dx_central.png', 102, '/102/201', '马经理', '13800002002', '2032-06-30 23:59:59', 2, '{"scale":800, "region":"华中", "industry":"制造业"}', TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (202, 'DX_NORTH', '鼎新集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/dx_north.png', 102, '/102/202', '韩经理', '13800002003', '2032-06-30 23:59:59', 2, '{"scale":600, "region":"华北", "industry":"制造业"}', FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                                                                                                               (211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'OFFICE', '武汉地区业务运营中心', 'https://logo.example.com/dx_wh.png', 201, '/102/201/211', '董主管', '13800002004', '2032-06-30 23:59:59', 2, '{"scale":300, "region":"武汉", "industry":"制造业"}', FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 部门表数据 (20条)
-- =============================================
INSERT INTO sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, tenant_id, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                           (301, 'DEPT_ZJB', '总经办', '万象集团总经理办公室', 0, '/301', 101, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (302, 'DEPT_CW', '财务部', '万象集团财务管理中心', 0, '/302', 101, 10, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (303, 'DEPT_HR', '人力资源部', '万象集团人力资源管理', 0, '/303', 101, 13, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (304, 'DEPT_IT', '信息技术部', '万象集团信息技术与系统支持', 0, '/304', 101, 18, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (305, 'DEPT_SJ', '审计部', '万象集团内部审计监督', 0, '/305', 101, 17, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (306, 'DEPT_ZHGL', '综合管理部', '华东分公司综合行政管理', 301, '/301/306', 111, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (307, 'DEPT_YW1', '业务一部', '华东分公司核心业务部门', 301, '/301/307', 111, 4, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (308, 'DEPT_XS', '销售部', '华南分公司销售业务管理', 302, '/302/308', 112, 7, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (309, 'DEPT_KF', '客服部', '华南分公司客户服务支持', 302, '/302/309', 112, 8, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (310, 'DEPT_YY', '运营部', '华北分公司运营管理', 303, '/303/310', 113, 11, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (311, 'DEPT_SC', '生产管理部', '鼎新集团生产调度与管理', 0, '/311', 102, 19, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (312, 'DEPT_ZL', '质量部', '鼎新集团质量检验与控制', 0, '/312', 102, 20, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (313, 'DEPT_CG', '采购部', '鼎新集团物资采购管理', 0, '/313', 102, 21, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (314, 'DEPT_YF1', '研发一部', '鼎新集团华中研发', 311, '/311/314', 201, 22, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (315, 'DEPT_SC2', '市场部', '鼎新集团华北市场推广', 312, '/312/315', 202, 23, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (316, 'DEPT_ZX', '咨询部', '万象集团华东-上海办事处咨询业务', 306, '/301/306/316', 121, 5, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (317, 'DEPT_BG', '办公室', '万象集团华东-南京办事处办公室（已停用）', 306, '/301/306/317', 122, 6, 'DISABLED', '办事处已停用', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-06-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (318, 'DEPT_WL', '物流部', '鼎新集团华中-武汉物流管理', 311, '/311/314/318', 211, 24, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (319, 'DEPT_KFZX', '客服中心', '万象集团集团级客服中心', 0, '/319', 101, 14, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                           (320, 'DEPT_FZ', '发展部', '鼎新集团战略发展部', 0, '/320', 102, 25, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 角色表数据 (15条)
-- =============================================
INSERT INTO sys_role (id, role_code, role_name, role_desc, role_level, tenant_id, data_scope, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                       (401, 'SUPER_ADMIN', '超级管理员', '系统最高权限管理员', 'SYSTEM', 101, 'ALL', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (402, 'TENANT_ADMIN', '租户管理员', '万象集团租户级管理员', 'TENANT', 101, 'ALL', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (403, 'DEPT_MANAGER', '部门管理员', '万象集团部门级管理员', 'DEPT', 101, 'DEPT_AND_SUB', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (404, 'EMPLOYEE', '普通员工', '万象集团普通员工', 'USER', 101, 'SELF', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (405, 'FINANCE', '财务专员', '万象集团财务人员', 'TENANT', 101, 'DEPT', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (406, 'AUDITOR', '审计员', '万象集团审计专员', 'TENANT', 101, 'ALL', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (407, 'BRANCH_ADMIN', '分公司管理员', '华东分公司管理员', 'BRANCH', 111, 'DEPT_AND_SUB', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (408, 'OFFICE_MANAGER', '办事处主管', '上海办事处主管', 'OFFICE', 121, 'DEPT', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (409, 'SALES', '销售人员', '华南分公司销售专员', 'USER', 112, 'SELF', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (410, 'DX_TENANT_ADMIN', '鼎新管理员', '鼎新集团管理员', 'TENANT', 102, 'ALL', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (411, 'DX_DEPT_MANAGER', '鼎新部门管理员', '鼎新集团部门管理员', 'DEPT', 102, 'DEPT_AND_SUB', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (412, 'DX_EMPLOYEE', '鼎新普通员工', '鼎新集团普通员工', 'USER', 102, 'SELF', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (413, 'DX_AUDITOR', '鼎新审计员', '鼎新集团审计专员', 'TENANT', 102, 'ALL', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (414, 'DX_RD_MANAGER', '研发经理', '鼎新华中研发部经理', 'DEPT', 201, 'DEPT_AND_SUB', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                       (415, 'READONLY', '只读用户', '万象集团只读权限用户', 'TENANT', 101, 'ALL', 'DISABLED', '临时停用', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 用户表数据 (30条)
-- =============================================
INSERT INTO sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                                          (1, 'U001', 'admin', '系统管理员', 'admin@wanxiang.com', '13800000001', '$2a$10$encrypted', 'avatar/admin.png', 'MALE', '1990-01-01', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (2, 'U002', 'zhang_zong', '张总', 'zhangzong@wanxiang.com', '13800000002', '$2a$10$encrypted', 'avatar/zhang.png', 'MALE', '1985-03-15', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (3, 'U003', 'li_jingli', '李经理', 'lijingli@wanxiang.com', '13800000003', '$2a$10$encrypted', 'avatar/li.png', 'MALE', '1988-06-20', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (4, 'U004', 'wang_zhuguan', '王主管', 'wangzhuguan@wanxiang.com', '13800000004', '$2a$10$encrypted', 'avatar/wang.png', 'FEMALE', '1992-09-10', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (5, 'U005', 'zhang_san', '张三', 'zhangsan@wanxiang.com', '13800000005', '$2a$10$encrypted', 'avatar/zhangsan.png', 'MALE', '1995-12-01', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (6, 'U006', 'li_si', '李四', 'lisi@wanxiang.com', '13800000006', '$2a$10$encrypted', 'avatar/lisi.png', 'MALE', '1993-04-18', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (7, 'U007', 'wang_wu', '王五', 'wangwu@wanxiang.com', '13800000007', '$2a$10$encrypted', 'avatar/wangwu.png', 'MALE', '1991-07-25', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (8, 'U008', 'zhao_liu', '赵六', 'zhaoliu@wanxiang.com', '13800000008', '$2a$10$encrypted', 'avatar/zhaoliu.png', 'FEMALE', '1994-11-30', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (9, 'U009', 'sun_qi', '孙七', 'sunqi@wanxiang.com', '13800000009', '$2a$10$encrypted', 'avatar/sunqi.png', 'MALE', '1996-02-14', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (10, 'U010', 'zhou_ba', '周八', 'zhouba@wanxiang.com', '13800000010', '$2a$10$encrypted', 'avatar/zhouba.png', 'MALE', '1989-08-08', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (11, 'U011', 'wu_jiu', '吴九', 'wujiu@wanxiang.com', '13800000011', '$2a$10$encrypted', 'avatar/wujiu.png', 'MALE', '1987-05-22', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (12, 'U012', 'zheng_shi', '郑十', 'zhengshi@wanxiang.com', '13800000012', '$2a$10$encrypted', 'avatar/zhengshi.png', 'FEMALE', '1997-01-15', 'DISABLED', '离职', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (13, 'U013', 'qian_yi', '钱一', 'qianyi@wanxiang.com', '13800000013', '$2a$10$encrypted', 'avatar/qianyi.png', 'FEMALE', '1990-10-05', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (14, 'U014', 'chen_er', '陈二', 'chener@wanxiang.com', '13800000014', '$2a$10$encrypted', 'avatar/chener.png', 'MALE', '1994-03-28', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (15, 'U015', 'feng_san', '冯三', 'fengsan@wanxiang.com', '13800000015', '$2a$10$encrypted', 'avatar/fengsan.png', 'MALE', '1986-12-12', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (16, 'U016', 'chu_si', '楚四', 'chusi@haina.com', '13800000016', '$2a$10$encrypted', 'avatar/chusi.png', 'FEMALE', '1992-06-30', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (17, 'U017', 'huang_audit', '黄审计', 'huangaudit@wanxiang.com', '13800000017', '$2a$10$encrypted', 'avatar/huang.png', 'MALE', '1988-09-18', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (18, 'U018', 'xu_readonly', '徐只读', 'xureadonly@wanxiang.com', '13800000018', '$2a$10$encrypted', 'avatar/xu.png', 'MALE', '1993-11-22', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (19, 'U019', 'dingxin_ceo', '丁总', 'dingxin@dingxin.com', '13800002001', '$2a$10$encrypted', 'avatar/dingxin.png', 'MALE', '1980-01-01', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (20, 'U020', 'dong_manager', '董经理', 'dong@dingxin.com', '13800002002', '$2a$10$encrypted', 'avatar/dong.png', 'MALE', '1984-04-20', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (21, 'U021', 'guo_staff', '郭员工', 'guo@dingxin.com', '13800002003', '$2a$10$encrypted', 'avatar/guo.png', 'FEMALE', '1990-10-10', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (22, 'U022', 'yan_dev', '严研发', 'yan@dingxin.com', '13800002004', '$2a$10$encrypted', 'avatar/yan.png', 'MALE', '1995-05-15', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (23, 'U023', 'cai_market', '蔡市场', 'cai@dingxin.com', '13800002005', '$2a$10$encrypted', 'avatar/cai.png', 'FEMALE', '1993-08-08', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (24, 'U024', 'han_overseas', '韩海外', 'han@dingxin.com', '13800002006', '$2a$10$encrypted', 'avatar/han.png', 'MALE', '1987-02-14', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (25, 'U025', 'hai_manager', '海经理', 'hai@haina.com', '13800004001', '$2a$10$encrypted', 'avatar/hai.png', 'MALE', '1982-11-11', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (26, 'U026', 'lu_edu', '陆教育', 'lu@haina.com', '13800004002', '$2a$10$encrypted', 'avatar/lu.png', 'FEMALE', '1991-07-20', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (27, 'U027', 'qiao_med', '乔医疗', 'qiao@haina.com', '13800004003', '$2a$10$encrypted', 'avatar/qiao.png', 'MALE', '1989-09-09', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (28, 'U028', 'tian_logistics', '田物流', 'tian@tianxiang.com', '13800005001', '$2a$10$encrypted', 'avatar/tian.png', 'MALE', '1985-05-20', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (29, 'U029', 'multi_user', '多租户用户', 'multiuser@test.com', '13900000001', '$2a$10$encrypted', 'avatar/multi.png', 'MALE', '1995-05-05', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                          (30, 'U030', 'test_user', '测试用户', 'testuser@test.com', '13900000002', '$2a$10$encrypted', 'avatar/test.png', 'FEMALE', '2000-01-01', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 权限表数据 (20条，树形结构)
-- =============================================
INSERT INTO sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                (501, 'SYSTEM', '系统管理', '系统基础管理', 'MENU', 0, '/501', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (502, 'TENANT_MANAGE', '租户管理', '租户信息管理', 'MENU', 501, '/501/502', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (503, 'USER_MANAGE', '用户管理', '用户信息管理', 'MENU', 501, '/501/503', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (504, 'ROLE_MANAGE', '角色管理', '角色信息管理', 'MENU', 501, '/501/504', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (505, 'PERM_MANAGE', '权限管理', '权限信息管理', 'MENU', 501, '/501/505', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (506, 'DEPT_MANAGE', '部门管理', '部门信息管理', 'MENU', 501, '/501/506', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (507, 'FINANCE', '财务管理', '财务数据管理', 'MENU', 0, '/507', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (508, 'AUDIT', '审计管理', '审计监督权限', 'MENU', 0, '/508', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (509, 'DATA_VIEW', '数据查看', '基础数据查看权限', 'BUTTON', 501, '/501/509', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (510, 'DATA_EDIT', '数据编辑', '基础数据编辑权限', 'BUTTON', 501, '/501/510', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (511, 'DATA_DELETE', '数据删除', '基础数据删除权限', 'BUTTON', 501, '/501/511', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (512, 'EXPORT', '数据导出', '基础数据导出权限', 'BUTTON', 501, '/501/512', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (513, 'REPORT_VIEW', '报表查看', '报表数据查看权限', 'BUTTON', 507, '/507/513', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (514, 'ORDER_MANAGE', '订单管理', '订单业务管理', 'MENU', 0, '/514', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (515, 'PRODUCT_MANAGE', '产品管理', '产品信息管理', 'MENU', 0, '/515', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (516, 'CUSTOMER_MANAGE', '客户管理', '客户信息管理', 'MENU', 0, '/516', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (517, 'SUPPLIER_MANAGE', '供应商管理', '供应商信息管理', 'MENU', 515, '/515/517', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (518, 'WAREHOUSE_MANAGE', '仓储管理', '仓储信息管理', 'MENU', 0, '/518', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (519, 'LOGISTICS_MANAGE', '物流管理', '物流配送管理', 'MENU', 518, '/518/519', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                (520, 'DEV_MANAGE', '研发管理', '研发项目管理', 'MENU', 0, '/520', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 租户策略表数据 (30条)
-- =============================================
INSERT INTO sys_tenant_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                                              (601, 'TP001', 'admin主万象', 1, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (602, 'TP002', '张总主万象', 2, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (603, 'TP003', '李经理主华东', 3, 'TENANT', 111, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (604, 'TP004', '王主管主上海', 4, 'TENANT', 121, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (605, 'TP005', '张三主上海', 5, 'TENANT', 121, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (606, 'TP006', '李四主华南', 6, 'TENANT', 112, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (607, 'TP007', '王五主华南', 7, 'TENANT', 112, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (608, 'TP008', '赵六主华南', 8, 'TENANT', 112, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (609, 'TP009', '孙七主华东', 9, 'TENANT', 111, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (610, 'TP010', '周八主万象', 10, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (611, 'TP011', '吴九主华北', 11, 'TENANT', 113, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (612, 'TP012', '郑十已禁用万象', 12, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'DISABLED', '用户离职', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (613, 'TP013', '钱一主万象', 13, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (614, 'TP014', '陈二主华南', 14, 'TENANT', 112, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (615, 'TP015', '冯三主万象', 15, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (616, 'TP016', '楚四主鼎新', 16, 'TENANT', 102, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (617, 'TP017', '黄审计主万象', 17, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (618, 'TP018', '徐只读主万象', 18, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (619, 'TP019', '丁总主鼎新', 19, 'TENANT', 102, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (620, 'TP020', '董经理主华中', 20, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (621, 'TP021', '郭员工主华北', 21, 'TENANT', 202, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (622, 'TP022', '严研发主武汉', 22, 'TENANT', 211, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (623, 'TP023', '蔡市场主华北', 23, 'TENANT', 202, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (624, 'TP024', '韩海外主华中', 24, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (625, 'TP025', '海经理主海纳', 25, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (626, 'TP026', '陆教育主海纳', 26, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (627, 'TP027', '乔医疗主海纳', 27, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (628, 'TP028', '田物流主天翔', 28, 'TENANT', 201, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (629, 'TP029', '多租户主万象', 29, 'TENANT', 101, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                              (630, 'TP030', '多租户兼鼎新', 29, 'TENANT', 102, FALSE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 部门策略表数据 (30条)
-- =============================================
INSERT INTO sys_dept_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                                            (701, 'DP001', 'admin总经办', 1, 'DEPT', 301, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (702, 'DP002', '张总总经办', 2, 'DEPT', 301, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (703, 'DP003', '李经理综合管理部', 3, 'DEPT', 306, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (704, 'DP004', '王主管咨询部', 4, 'DEPT', 316, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (705, 'DP005', '张三咨询部', 5, 'DEPT', 316, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (706, 'DP006', '李四销售部', 6, 'DEPT', 308, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (707, 'DP007', '王五销售部', 7, 'DEPT', 308, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (708, 'DP008', '赵六客服部', 8, 'DEPT', 309, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (709, 'DP009', '孙七业务一部', 9, 'DEPT', 307, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (710, 'DP010', '周八财务部', 10, 'DEPT', 302, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (711, 'DP011', '吴九运营部', 11, 'DEPT', 310, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (712, 'DP012', '郑十总经办已禁用', 12, 'DEPT', 301, TRUE, '2025-01-01 08:00:00', 'DISABLED', '用户离职', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (713, 'DP013', '钱一人力资源部', 13, 'DEPT', 303, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (714, 'DP014', '陈二客服部', 14, 'DEPT', 319, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (715, 'DP015', '冯三审计部', 15, 'DEPT', 305, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (716, 'DP016', '楚四生产管理部', 16, 'DEPT', 311, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (717, 'DP017', '黄审计审计部', 17, 'DEPT', 305, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (718, 'DP018', '徐只读信息技术部', 18, 'DEPT', 304, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (719, 'DP019', '丁总生产管理部', 19, 'DEPT', 311, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (720, 'DP020', '董经理研发一部', 20, 'DEPT', 314, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (721, 'DP021', '郭员工市场部', 21, 'DEPT', 315, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (722, 'DP022', '严研发研发一部', 22, 'DEPT', 314, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (723, 'DP023', '蔡市场市场部', 23, 'DEPT', 315, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (724, 'DP024', '韩海外研发一部', 24, 'DEPT', 314, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (725, 'DP025', '海经理生产管理部', 25, 'DEPT', 311, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (726, 'DP026', '陆教育研发一部', 26, 'DEPT', 314, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (727, 'DP027', '乔医疗质量部', 27, 'DEPT', 312, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (728, 'DP028', '田物流仓储管理', 28, 'DEPT', 518, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (729, 'DP029', '多租户主部门总经办', 29, 'DEPT', 301, TRUE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                                            (730, 'DP030', '多租户兼部门生产管理', 29, 'DEPT', 311, FALSE, '2025-01-01 08:00:00', 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 角色策略表数据 (30条)
-- =============================================
INSERT INTO sys_role_policy (id, policy_code, policy_name, role_id, target_type, target_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                     (801, 'RP001', 'admin超级管理员', 401, 'USER', 1, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (802, 'RP002', '张总租户管理员', 402, 'USER', 2, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (803, 'RP003', '李经理分公司管理员', 407, 'USER', 3, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (804, 'RP004', '王主管办事处主管', 408, 'USER', 4, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (805, 'RP005', '张三普通员工万象', 404, 'USER', 5, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (806, 'RP006', '李四销售人员', 409, 'USER', 6, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (807, 'RP007', '王五销售人员', 409, 'USER', 7, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (808, 'RP008', '赵六客服部员工', 404, 'USER', 8, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (809, 'RP009', '孙七普通员工华东', 404, 'USER', 9, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (810, 'RP010', '周八财务专员', 405, 'USER', 10, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (811, 'RP011', '吴九分公司管理员华北', 407, 'USER', 11, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (812, 'RP012', '钱一人事专员', 405, 'USER', 13, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (813, 'RP013', '陈二客服中心员工', 404, 'USER', 14, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (814, 'RP014', '冯三审计员', 406, 'USER', 15, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (815, 'RP015', '楚四鼎新管理员', 410, 'USER', 16, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (816, 'RP016', '黄审计审计员', 406, 'USER', 17, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (817, 'RP017', '徐只读只读角色', 415, 'USER', 18, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (818, 'RP018', '丁总鼎新管理员', 410, 'USER', 19, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (819, 'RP019', '董经理研发经理', 414, 'USER', 20, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (820, 'RP020', '郭员工普通员工鼎新', 412, 'USER', 21, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (821, 'RP021', '严研发普通员工', 412, 'USER', 22, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (822, 'RP022', '蔡市场普通员工', 412, 'USER', 23, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (823, 'RP023', '韩海外普通员工', 412, 'USER', 24, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (824, 'RP024', '角色授予部门示例：财务部授财务专员', 405, 'DEPT', 302, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (825, 'RP025', '销售部授销售角色', 409, 'DEPT', 308, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (826, 'RP026', '多租户用户万象普通员工', 404, 'USER', 29, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (827, 'RP027', '多租户用户鼎新普通员工', 412, 'USER', 29, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (828, 'RP028', '测试用户万象普通员工', 404, 'USER', 30, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (829, 'RP029', '测试用户鼎新普通员工', 412, 'USER', 30, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (830, 'RP030', 'admin鼎新管理员', 410, 'USER', 1, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- =============================================
-- 权限策略表数据 (30条)
-- =============================================
INSERT INTO sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
                                                                                                                                                                                                                                     (901, 'PP001', '超级管理员所有系统管理', 501, 'ROLE', 401, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (902, 'PP002', '超级管理员用户管理', 503, 'ROLE', 401, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (903, 'PP003', '超级管理员数据查看', 509, 'ROLE', 401, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (904, 'PP004', '租户管理员租户管理', 502, 'ROLE', 402, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (905, 'PP005', '租户管理员用户管理', 503, 'ROLE', 402, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (906, 'PP006', '财务专员财务管理', 507, 'ROLE', 405, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (907, 'PP007', '审计员审计管理', 508, 'ROLE', 406, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (908, 'PP008', '普通员工数据查看', 509, 'ROLE', 404, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (909, 'PP009', '销售人员客户管理', 516, 'ROLE', 409, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (910, 'PP010', '鼎新管理员产品管理', 515, 'ROLE', 410, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (911, 'PP011', '鼎新管理员用户管理', 503, 'ROLE', 410, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (912, 'PP012', '鼎新普通员工数据查看', 509, 'ROLE', 412, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (913, 'PP013', '只读用户数据查看', 509, 'ROLE', 415, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (914, 'PP014', '直接用户admin系统配置', 501, 'USER', 1, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (915, 'PP015', '直接用户张总导出', 512, 'USER', 2, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (916, 'PP016', '租户万象默认权限系统管理', 501, 'TENANT', 101, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (917, 'PP017', '租户鼎新默认权限生产管理', 515, 'TENANT', 102, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (918, 'PP018', '分公司管理员部门管理', 506, 'ROLE', 407, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (919, 'PP019', '办事处主管数据查看', 509, 'ROLE', 408, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (920, 'PP020', '鼎新审计员审计管理', 508, 'ROLE', 413, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (921, 'PP021', '研发经理研发管理', 520, 'ROLE', 414, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (922, 'PP022', '部门财务部授予财务管理权限', 507, 'DEPT', 302, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (923, 'PP023', '部门销售部授予客户管理权限', 516, 'DEPT', 308, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (924, 'PP024', '多租户用户直接授予数据查看', 509, 'USER', 29, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (925, 'PP025', '测试用户数据编辑', 510, 'USER', 30, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (926, 'PP026', '租户万象默认订单管理', 514, 'TENANT', 101, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (927, 'PP027', '鼎新审计员审计管理授权', 508, 'ROLE', 413, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (928, 'PP028', '研发管理授予研发一部部门', 520, 'DEPT', 314, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (929, 'PP029', '只读用户报表查看', 513, 'ROLE', 415, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL),
                                                                                                                                                                                                                                     (930, 'PP030', '超级管理员数据编辑', 510, 'ROLE', 401, 'ACTIVE', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);