CREATE TABLE sys_tenant (
    -- 主键（系统租户ID）
                            id              BIGINT        NOT NULL PRIMARY KEY,

    -- 租户基本信息
                            tenant_code     VARCHAR(50)   NOT NULL,
                            tenant_name     VARCHAR(100)  NOT NULL,
                            tenant_type     VARCHAR(50)   NOT NULL,
                            tenant_desc     VARCHAR(500)  DEFAULT NULL,
                            tenant_logo_url VARCHAR(500)  DEFAULT NULL,

    -- 层级结构
                            parent_id       BIGINT        NOT NULL,
                            path            VARCHAR(1000) NOT NULL,
                            level           INT           NOT NULL,
                            has_children    BOOLEAN       NOT NULL,

    -- 联系信息
                            contact_name    VARCHAR(50)   NOT NULL,
                            contact_phone   VARCHAR(20)   NOT NULL,
                            contact_email   VARCHAR(100)  DEFAULT NULL,

    -- 服务配置
                            expire_time     TIMESTAMP(6)  NOT NULL,
                            package_id      BIGINT        NOT NULL,

    -- 扩展属性
                            ext_attributes  JSONB         DEFAULT '{}',

    -- 状态管理
                            status          VARCHAR(20)   NOT NULL,
                            disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                            create_tenant   BIGINT        NOT NULL,
                            create_dept     BIGINT        NOT NULL,
                            create_role     BIGINT        NOT NULL,
                            create_by       BIGINT        NOT NULL,
                            create_at       TIMESTAMP(6)  NOT NULL,
                            update_by       BIGINT        NOT NULL,
                            update_at       TIMESTAMP(6)  NOT NULL,
                            is_deleted      VARCHAR(20)   NOT NULL,
                            deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE UNIQUE INDEX uk_tenant_code ON sys_tenant(tenant_code, is_deleted);
-- CREATE INDEX idx_tenant_parent ON sys_tenant(parent_id, status);
-- CREATE INDEX idx_tenant_path ON sys_tenant(path);
-- CREATE INDEX idx_tenant_status ON sys_tenant(status, is_deleted);

COMMENT ON TABLE sys_tenant IS '租户表';
COMMENT ON COLUMN sys_tenant.id IS '主键ID';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_type IS '租户类型';
COMMENT ON COLUMN sys_tenant.tenant_desc IS '租户描述';
COMMENT ON COLUMN sys_tenant.tenant_logo_url IS '租户LOGO路径';
COMMENT ON COLUMN sys_tenant.parent_id IS '父租户ID';
COMMENT ON COLUMN sys_tenant.path IS '层级路径';
COMMENT ON COLUMN sys_tenant.level IS '层级深度';
COMMENT ON COLUMN sys_tenant.has_children IS '是否存在子租户';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.contact_email IS '联系人邮箱';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_id IS '套餐ID';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性';
COMMENT ON COLUMN sys_tenant.status IS '状态';
COMMENT ON COLUMN sys_tenant.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_tenant.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_tenant.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_tenant.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_tenant.create_at IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_tenant.update_at IS '更新时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_tenant.deleted_at IS '删除时间';

CREATE TABLE sys_dept (
    -- 主键（系统部门ID）
                          id              BIGINT        NOT NULL PRIMARY KEY,

    -- 部门基本信息
                          dept_code       VARCHAR(50)   NOT NULL,
                          dept_name       VARCHAR(100)  NOT NULL,
                          dept_desc       VARCHAR(200)  DEFAULT NULL,

    -- 层级结构
                          parent_id       BIGINT        NOT NULL,
                          path            VARCHAR(1000) NOT NULL,
                          level           INT           NOT NULL,
                          has_children    BOOLEAN       NOT NULL,

    -- 管理信息
                          leader_id       BIGINT        NOT NULL,

    -- 状态管理
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE INDEX idx_dept_code ON sys_dept(dept_code, is_deleted);
-- CREATE INDEX idx_dept_parent ON sys_dept(parent_id, status);
-- CREATE INDEX idx_dept_path ON sys_dept(path);
-- CREATE INDEX idx_dept_status ON sys_dept(status, is_deleted);
-- CREATE INDEX idx_dept_leader ON sys_dept(leader_id);

COMMENT ON TABLE sys_dept IS '部门表';
COMMENT ON COLUMN sys_dept.id IS '主键ID';
COMMENT ON COLUMN sys_dept.dept_code IS '部门编码';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.dept_desc IS '部门描述';
COMMENT ON COLUMN sys_dept.parent_id IS '父部门ID';
COMMENT ON COLUMN sys_dept.path IS '层级路径';
COMMENT ON COLUMN sys_dept.level IS '层级深度';
COMMENT ON COLUMN sys_dept.has_children IS '是否存在子部门';
COMMENT ON COLUMN sys_dept.leader_id IS '部门负责人用户ID';
COMMENT ON COLUMN sys_dept.status IS '状态';
COMMENT ON COLUMN sys_dept.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_dept.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_dept.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_dept.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_dept.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_dept.create_at IS '创建时间';
COMMENT ON COLUMN sys_dept.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_dept.update_at IS '更新时间';
COMMENT ON COLUMN sys_dept.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_dept.deleted_at IS '删除时间';

CREATE TABLE sys_role (
    -- 主键（系统角色ID）
                          id              BIGINT        NOT NULL PRIMARY KEY,

    -- 角色基本信息
                          role_code       VARCHAR(100)  NOT NULL,
                          role_name       VARCHAR(100)  NOT NULL,
                          role_desc       VARCHAR(200)  DEFAULT NULL,

    -- 状态管理
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE UNIQUE INDEX uk_role_code ON sys_role(role_code, is_deleted);
-- CREATE INDEX idx_role_type ON sys_role(role_type, status);
-- CREATE INDEX idx_role_level ON sys_role(role_level, status);
-- CREATE INDEX idx_role_status ON sys_role(status, is_deleted);

COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.status IS '状态';
COMMENT ON COLUMN sys_role.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_role.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_role.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_role.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_role.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_role.create_at IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_role.update_at IS '更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_role.deleted_at IS '删除时间';

CREATE TABLE sys_user (
    -- 主键（系统用户ID）
                          id              BIGINT        NOT NULL PRIMARY KEY,

    -- 用户基本信息
                          user_code       VARCHAR(50)   NOT NULL,
                          user_name       VARCHAR(50)   NOT NULL,
                          nick_name       VARCHAR(50)   NOT NULL,
                          real_name       VARCHAR(50)   NOT NULL,

    -- 联系方式
                          email           VARCHAR(100)  NOT NULL,
                          phone           VARCHAR(20)   NOT NULL,

    -- 认证信息
                          password        VARCHAR(200)  NOT NULL,

    -- 个人信息
                          avatar_url      VARCHAR(500)  DEFAULT NULL,
                          gender          VARCHAR(10)   NOT NULL,
                          birthday        DATE          DEFAULT NULL,

    -- 状态管理
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,
                          last_login_at   TIMESTAMP(6)  DEFAULT NULL,
                          last_login_ip   VARCHAR(50)   DEFAULT NULL,

    -- 审计字段
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE UNIQUE INDEX uk_user_code ON sys_user(user_code, is_deleted);
-- CREATE UNIQUE INDEX uk_user_name ON sys_user(user_name, is_deleted);
-- CREATE UNIQUE INDEX uk_user_email ON sys_user(email, is_deleted);
-- CREATE UNIQUE INDEX uk_user_phone ON sys_user(phone, is_deleted);
-- CREATE INDEX idx_user_status ON sys_user(status, is_deleted);

COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.user_code IS '用户编码';
COMMENT ON COLUMN sys_user.user_name IS '用户名';
COMMENT ON COLUMN sys_user.nick_name IS '昵称';
COMMENT ON COLUMN sys_user.real_name IS '真实姓名';
COMMENT ON COLUMN sys_user.email IS '电子邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号码';
COMMENT ON COLUMN sys_user.password IS '密码哈希值';
COMMENT ON COLUMN sys_user.avatar_url IS '头像URL';
COMMENT ON COLUMN sys_user.gender IS '性别';
COMMENT ON COLUMN sys_user.birthday IS '出生日期';
COMMENT ON COLUMN sys_user.status IS '状态';
COMMENT ON COLUMN sys_user.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_user.last_login_at IS '最后登录时间';
COMMENT ON COLUMN sys_user.last_login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_user.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_user.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_user.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_user.create_at IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_user.update_at IS '更新时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_user.deleted_at IS '删除时间';

CREATE TABLE sys_perm (
    -- 主键（系统权限ID）
                          id              BIGINT        NOT NULL PRIMARY KEY,

    -- 权限基本信息
                          perm_code       VARCHAR(100)  NOT NULL,
                          perm_name       VARCHAR(100)  NOT NULL,
                          perm_desc       VARCHAR(200)  DEFAULT NULL,
                          perm_type       VARCHAR(20)   NOT NULL,

    -- 层级结构（用于菜单权限）
                          parent_id       BIGINT        NOT NULL,
                          path            VARCHAR(1000) NOT NULL,
                          level           INT           NOT NULL,

    -- 权限资源定位
                          resource_type   VARCHAR(50)   DEFAULT NULL,
                          resource_path   VARCHAR(200)  DEFAULT NULL,
                          resource_method VARCHAR(20)   DEFAULT NULL,

    -- 显示配置（菜单权限专用）
                          icon            VARCHAR(100)  DEFAULT NULL,
                          sort_order      INT           NOT NULL,
                          is_visible      BOOLEAN       NOT NULL,

    -- 状态管理
                          status          VARCHAR(20)   NOT NULL,
                          disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                          create_tenant   BIGINT        NOT NULL,
                          create_dept     BIGINT        NOT NULL,
                          create_role     BIGINT        NOT NULL,
                          create_by       BIGINT        NOT NULL,
                          create_at       TIMESTAMP(6)  NOT NULL,
                          update_by       BIGINT        NOT NULL,
                          update_at       TIMESTAMP(6)  NOT NULL,
                          is_deleted      VARCHAR(20)   NOT NULL,
                          deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE UNIQUE INDEX uk_perm_code ON sys_perm(perm_code, is_deleted);
-- CREATE INDEX idx_perm_type ON sys_perm(perm_type, status);
-- CREATE INDEX idx_perm_parent ON sys_perm(parent_id, status);
-- CREATE INDEX idx_perm_path ON sys_perm(path);
-- CREATE INDEX idx_perm_resource ON sys_perm(resource_type, resource_path);
-- CREATE INDEX idx_perm_status ON sys_perm(status, is_deleted);

COMMENT ON TABLE sys_perm IS '权限表';
COMMENT ON COLUMN sys_perm.id IS '主键ID';
COMMENT ON COLUMN sys_perm.perm_code IS '权限编码';
COMMENT ON COLUMN sys_perm.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm.perm_desc IS '权限描述';
COMMENT ON COLUMN sys_perm.perm_type IS '权限类型';
COMMENT ON COLUMN sys_perm.parent_id IS '父权限ID，';
COMMENT ON COLUMN sys_perm.path IS '权限层级路径';
COMMENT ON COLUMN sys_perm.level IS '层级深度';
COMMENT ON COLUMN sys_perm.resource_type IS '资源类型';
COMMENT ON COLUMN sys_perm.resource_path IS '资源路径';
COMMENT ON COLUMN sys_perm.resource_method IS '资源方法';
COMMENT ON COLUMN sys_perm.icon IS '图标';
COMMENT ON COLUMN sys_perm.sort_order IS '排序序号';
COMMENT ON COLUMN sys_perm.is_visible IS '是否可见';
COMMENT ON COLUMN sys_perm.status IS '状态';
COMMENT ON COLUMN sys_perm.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_perm.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_perm.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_perm.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_perm.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_perm.create_at IS '创建时间';
COMMENT ON COLUMN sys_perm.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_perm.update_at IS '更新时间';
COMMENT ON COLUMN sys_perm.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_perm.deleted_at IS '删除时间';

CREATE TABLE sys_tenant_policy (
    -- 主键（同时作为租户级ID：租户部门ID/租户角色ID）
                                   id              BIGINT        NOT NULL PRIMARY KEY,

    -- 策略基本信息
                                   policy_code     VARCHAR(100)  NOT NULL,
                                   policy_name     VARCHAR(100)  NOT NULL,

    -- 绑定关系
                                   source_type     VARCHAR(20)   NOT NULL,
                                   source_id       BIGINT        NOT NULL,
                                   tenant_id       BIGINT        NOT NULL,

    -- 状态管理
                                   status          VARCHAR(20)   NOT NULL,
                                   disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                                   create_tenant   BIGINT        NOT NULL,
                                   create_dept     BIGINT        NOT NULL,
                                   create_role     BIGINT        NOT NULL,
                                   create_by       BIGINT        NOT NULL,
                                   create_at       TIMESTAMP(6)  NOT NULL,
                                   update_by       BIGINT        NOT NULL,
                                   update_at       TIMESTAMP(6)  NOT NULL,
                                   is_deleted      VARCHAR(20)   NOT NULL,
                                   deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE UNIQUE INDEX uk_tenant_policy_source ON sys_tenant_policy(source_type, source_id, tenant_id, is_deleted);
-- CREATE INDEX idx_tenant_policy_tenant ON sys_tenant_policy(tenant_id, status);
-- CREATE INDEX idx_tenant_policy_status ON sys_tenant_policy(status, is_deleted);

COMMENT ON TABLE sys_tenant_policy IS '租户策略表';
COMMENT ON COLUMN sys_tenant_policy.id IS '主键ID';
COMMENT ON COLUMN sys_tenant_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_tenant_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_tenant_policy.source_type IS '源实体类型';
COMMENT ON COLUMN sys_tenant_policy.source_id IS '系统实体ID';
COMMENT ON COLUMN sys_tenant_policy.tenant_id IS '目标租户ID';
COMMENT ON COLUMN sys_tenant_policy.status IS '策略状态';
COMMENT ON COLUMN sys_tenant_policy.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_tenant_policy.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_tenant_policy.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_tenant_policy.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_tenant_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_tenant_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_tenant_policy.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_tenant_policy.update_at IS '更新时间';
COMMENT ON COLUMN sys_tenant_policy.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_tenant_policy.deleted_at IS '删除时间';

CREATE TABLE sys_user_policy (
    -- 主键（绑定租户时作为租户用户ID）
                                 id              BIGINT        NOT NULL PRIMARY KEY,

    -- 策略基本信息
                                 policy_code     VARCHAR(100)  NOT NULL,
                                 policy_name     VARCHAR(100)  NOT NULL,

    -- 绑定关系
                                 user_id         BIGINT        NOT NULL,
                                 target_type     VARCHAR(20)   NOT NULL,
                                 target_id       BIGINT        NOT NULL,

    -- 关系附加数据
                                 is_primary      BOOLEAN       NOT NULL,

    -- 状态管理
                                 status          VARCHAR(20)   NOT NULL,
                                 disable_reason  VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                                 create_tenant   BIGINT        NOT NULL,
                                 create_dept     BIGINT        NOT NULL,
                                 create_role     BIGINT        NOT NULL,
                                 create_by       BIGINT        NOT NULL,
                                 create_at       TIMESTAMP(6)  NOT NULL,
                                 update_by       BIGINT        NOT NULL,
                                 update_at       TIMESTAMP(6)  NOT NULL,
                                 is_deleted      VARCHAR(20)   NOT NULL,
                                 deleted_at      TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE INDEX idx_user_policy_user ON sys_user_policy(user_id, target_type, status);
-- CREATE INDEX idx_user_policy_target ON sys_user_policy(target_type, target_id, status);
-- CREATE INDEX idx_user_policy_status ON sys_user_policy(status, is_deleted);

COMMENT ON TABLE sys_user_policy IS '用户策略表';
COMMENT ON COLUMN sys_user_policy.id IS '主键ID';
COMMENT ON COLUMN sys_user_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_user_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_user_policy.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_user_policy.target_id IS '目标ID';
COMMENT ON COLUMN sys_user_policy.is_primary IS '是否为主租户/主部门';
COMMENT ON COLUMN sys_user_policy.status IS '策略状态';
COMMENT ON COLUMN sys_user_policy.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_user_policy.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_user_policy.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_user_policy.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_user_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_user_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_user_policy.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_user_policy.update_at IS '更新时间';
COMMENT ON COLUMN sys_user_policy.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_user_policy.deleted_at IS '删除时间';

CREATE TABLE sys_perm_policy (
    -- 主键（系统权限授予租户时作为租户权限ID）
                                 id                BIGINT        NOT NULL PRIMARY KEY,

    -- 策略基本信息
                                 policy_code       VARCHAR(100)  NOT NULL,
                                 policy_name       VARCHAR(100)  NOT NULL,

    -- 绑定关系
                                 perm_id           BIGINT        NOT NULL,
                                 target_type       VARCHAR(20)   NOT NULL,
                                 target_id         BIGINT        NOT NULL,

    -- 权限配置
                                 data_scope        VARCHAR(20)   DEFAULT NULL,
                                 table_name        VARCHAR(100)  DEFAULT NULL,
                                 field_operation JSONB         DEFAULT '{}',

    -- 状态管理
                                 status            VARCHAR(20)   NOT NULL,
                                 disable_reason    VARCHAR(200)  DEFAULT NULL,

    -- 审计字段
                                 create_tenant     BIGINT        NOT NULL,
                                 create_dept       BIGINT        NOT NULL,
                                 create_role       BIGINT        NOT NULL,
                                 create_by         BIGINT        NOT NULL,
                                 create_at         TIMESTAMP(6)  NOT NULL,
                                 update_by         BIGINT        NOT NULL,
                                 update_at         TIMESTAMP(6)  NOT NULL,
                                 is_deleted        VARCHAR(20)   NOT NULL,
                                 deleted_at        TIMESTAMP(6)  DEFAULT NULL
);

-- 索引
-- CREATE INDEX idx_perm_policy_perm ON sys_perm_policy(perm_id, target_type, status);
-- CREATE INDEX idx_perm_policy_target ON sys_perm_policy(target_type, target_id, status);
-- CREATE INDEX idx_perm_policy_status ON sys_perm_policy(status, is_deleted);

COMMENT ON TABLE sys_perm_policy IS '权限策略表';
COMMENT ON COLUMN sys_perm_policy.id IS '主键ID';
COMMENT ON COLUMN sys_perm_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_perm_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_perm_policy.perm_id IS '权限ID';
COMMENT ON COLUMN sys_perm_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_perm_policy.target_id IS '目标ID';
COMMENT ON COLUMN sys_perm_policy.data_scope IS '行级权限';
COMMENT ON COLUMN sys_perm_policy.table_name IS '关联的数据表名';
COMMENT ON COLUMN sys_perm_policy.field_operation IS '字段级权限配置';
COMMENT ON COLUMN sys_perm_policy.status IS '策略状态';
COMMENT ON COLUMN sys_perm_policy.disable_reason IS '禁用原因';
COMMENT ON COLUMN sys_perm_policy.create_tenant IS '创建租户ID';
COMMENT ON COLUMN sys_perm_policy.create_dept IS '创建部门ID';
COMMENT ON COLUMN sys_perm_policy.create_role IS '创建角色ID';
COMMENT ON COLUMN sys_perm_policy.create_by IS '创建人用户ID';
COMMENT ON COLUMN sys_perm_policy.create_at IS '创建时间';
COMMENT ON COLUMN sys_perm_policy.update_by IS '更新人用户ID';
COMMENT ON COLUMN sys_perm_policy.update_at IS '更新时间';
COMMENT ON COLUMN sys_perm_policy.is_deleted IS '逻辑删除状态';
COMMENT ON COLUMN sys_perm_policy.deleted_at IS '删除时间';

-- 根租户：万象集团
INSERT INTO sys_tenant VALUES (101, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团', 'https://logo.example.com/wanxiang.png', 0, '/101', 1, TRUE, '张总', '13800000001', 'zhangzong@wanxiang.com', '2030-12-31 23:59:59', 1, '{"industry":"综合","region":"全国"}', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 根租户：鼎新集团
INSERT INTO sys_tenant VALUES (102, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dingxin.png', 0, '/102', 1, TRUE, '丁总', '13800000002', 'dingzong@dingxin.com', '2032-06-30 23:59:59', 2, '{"industry":"制造业","region":"全国"}', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 根租户：海纳集团
INSERT INTO sys_tenant VALUES (103, 'HAINA', '海纳集团', 'ENTERPRISE', '多元化投资集团', 'https://logo.example.com/haina.png', 0, '/103', 1, FALSE, '海总', '13800000003', 'haizong@haina.com', '2031-12-31 23:59:59', 1, '{"industry":"投资","region":"全国"}', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：华东分公司（万象集团）
INSERT INTO sys_tenant VALUES (111, 'WX_EAST', '万象-华东分公司', 'BRANCH', '负责华东区域业务', 'https://logo.example.com/wx_east.png', 101, '/101/111', 2, TRUE, '李经理', '13800000011', 'lijingli@wanxiang.com', '2030-12-31 23:59:59', 1, '{"region":"华东"}', 'ENABLED', NULL, 101, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-02-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：华南分公司（万象集团）
INSERT INTO sys_tenant VALUES (112, 'WX_SOUTH', '万象-华南分公司', 'BRANCH', '负责华南区域业务', 'https://logo.example.com/wx_south.png', 101, '/101/112', 2, FALSE, '陈经理', '13800000012', 'chenjingli@wanxiang.com', '2030-12-31 23:59:59', 1, '{"region":"华南"}', 'ENABLED', NULL, 101, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-02-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：华北分公司（万象集团）
INSERT INTO sys_tenant VALUES (113, 'WX_NORTH', '万象-华北分公司', 'BRANCH', '负责华北区域业务', 'https://logo.example.com/wx_north.png', 101, '/101/113', 2, FALSE, '武经理', '13800000013', 'wujingli@wanxiang.com', '2030-12-31 23:59:59', 1, '{"region":"华北"}', 'DISABLED', '业务调整暂停', 101, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-06-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：上海办事处（华东分公司）
INSERT INTO sys_tenant VALUES (121, 'WX_EAST_SH', '万象-华东-上海办事处', 'OFFICE', '上海地区业务中心', 'https://logo.example.com/wx_sh.png', 111, '/101/111/121', 3, FALSE, '王主管', '13800000021', 'wangzhuguan@wanxiang.com', '2030-12-31 23:59:59', 1, '{"city":"上海"}', 'ENABLED', NULL, 111, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：南京办事处（华东分公司）
INSERT INTO sys_tenant VALUES (122, 'WX_EAST_NJ', '万象-华东-南京办事处', 'OFFICE', '南京地区业务中心', 'https://logo.example.com/wx_nj.png', 111, '/101/111/122', 3, FALSE, '赵主管', '13800000022', 'zhaozhuguan@wanxiang.com', '2030-12-31 23:59:59', 1, '{"city":"南京"}', 'ENABLED', NULL, 111, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：华中分公司（鼎新集团）
INSERT INTO sys_tenant VALUES (201, 'DX_CENTRAL', '鼎新-华中分公司', 'BRANCH', '负责华中区域制造业务', 'https://logo.example.com/dx_central.png', 102, '/102/201', 2, FALSE, '马经理', '13800000201', 'majingli@dingxin.com', '2032-06-30 23:59:59', 2, '{"region":"华中"}', 'ENABLED', NULL, 102, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-02-01 08:00:00', 'NOT_DELETED', NULL);

-- 子租户：武汉工厂（华中分公司）
INSERT INTO sys_tenant VALUES (211, 'DX_CENTRAL_WH', '鼎新-华中-武汉工厂', 'OFFICE', '武汉生产制造基地', 'https://logo.example.com/dx_wh.png', 201, '/102/201/211', 3, FALSE, '董主管', '13800000211', 'dongzhuguan@dingxin.com', '2032-06-30 23:59:59', 2, '{"city":"武汉"}', 'ENABLED', NULL, 201, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);

-- 一级部门
INSERT INTO sys_dept VALUES (301, 'DEPT_FIN', '财务部', '财务管理部门', 0, '/301', 1, TRUE, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (302, 'DEPT_RD', '研发部', '产品研发部门', 0, '/302', 1, TRUE, 2, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (303, 'DEPT_SALES', '销售部', '市场销售部门', 0, '/303', 1, TRUE, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (304, 'DEPT_HR', '人力资源部', '人力资源管理', 0, '/304', 1, FALSE, 4, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (305, 'DEPT_IT', '信息技术部', 'IT支持与系统维护', 0, '/305', 1, FALSE, 5, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (306, 'DEPT_MKT', '市场部', '市场营销推广', 0, '/306', 1, FALSE, 6, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (307, 'DEPT_PROD', '生产部', '生产制造管理', 0, '/307', 1, TRUE, 7, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (308, 'DEPT_QC', '质量部', '质量控制与检验', 0, '/308', 1, FALSE, 8, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (309, 'DEPT_LOG', '物流部', '物流与供应链管理', 0, '/309', 1, FALSE, 9, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (310, 'DEPT_CS', '客服部', '客户服务支持', 0, '/310', 1, FALSE, 10, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级部门（财务部下属）
INSERT INTO sys_dept VALUES (311, 'DEPT_FIN_ACC', '会计组', '日常会计核算', 301, '/301/311', 2, FALSE, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (312, 'DEPT_FIN_TAX', '税务组', '税务筹划与申报', 301, '/301/312', 2, FALSE, 2, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (313, 'DEPT_FIN_AUDIT', '审计组', '内部审计监督', 301, '/301/313', 2, FALSE, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级部门（研发部下属）
INSERT INTO sys_dept VALUES (321, 'DEPT_RD_FE', '前端开发组', '前端技术研发', 302, '/302/321', 2, FALSE, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (322, 'DEPT_RD_BE', '后端开发组', '后端技术研发', 302, '/302/322', 2, FALSE, 2, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (323, 'DEPT_RD_TEST', '测试组', '软件测试与质量保障', 302, '/302/323', 2, FALSE, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (324, 'DEPT_RD_UI', 'UI设计组', '用户界面设计', 302, '/302/324', 2, FALSE, 4, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级部门（销售部下属）
INSERT INTO sys_dept VALUES (331, 'DEPT_SALES_NORTH', '华北销售组', '华北区域销售', 303, '/303/331', 2, FALSE, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (332, 'DEPT_SALES_SOUTH', '华南销售组', '华南区域销售', 303, '/303/332', 2, FALSE, 2, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (333, 'DEPT_SALES_EAST', '华东销售组', '华东区域销售', 303, '/303/333', 2, FALSE, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级部门（生产部下属）
INSERT INTO sys_dept VALUES (371, 'DEPT_PROD_LINE1', '生产一线', '第一生产线', 307, '/307/371', 2, FALSE, 1, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (372, 'DEPT_PROD_LINE2', '生产二线', '第二生产线', 307, '/307/372', 2, FALSE, 2, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (373, 'DEPT_PROD_WH', '生产仓储组', '生产物料仓储', 307, '/307/373', 2, FALSE, 3, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 虚拟部门
INSERT INTO sys_dept VALUES (391, 'DEPT_PROJ_A', '项目A组', '临时项目A团队', 0, '/391', 1, FALSE, 20, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (392, 'DEPT_PROJ_B', '项目B组', '临时项目B团队', 0, '/392', 1, FALSE, 21, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 已禁用的部门
INSERT INTO sys_dept VALUES (381, 'DEPT_OLD', '已撤销部门', '历史遗留部门', 0, '/381', 1, FALSE, 99, 'DISABLED', '部门合并已撤销', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-05-01 08:00:00', 'NOT_DELETED', NULL);

-- 补充到30条
INSERT INTO sys_dept VALUES (341, 'DEPT_LEGAL', '法务部', '法律事务管理', 0, '/341', 1, FALSE, 11, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (342, 'DEPT_ADMIN', '行政部', '行政后勤管理', 0, '/342', 1, FALSE, 12, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_dept VALUES (343, 'DEPT_BD', '商务拓展部', '商务合作与拓展', 0, '/343', 1, FALSE, 13, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 系统级角色
INSERT INTO sys_role VALUES (401, 'SUPER_ADMIN', '超级管理员', '系统最高权限', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (402, 'SYS_ADMIN', '系统管理员', '系统配置管理', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (403, 'SYS_AUDITOR', '系统审计员', '系统审计监督', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 租户级角色
INSERT INTO sys_role VALUES (411, 'TENANT_ADMIN', '租户管理员', '租户级管理员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (412, 'TENANT_OPERATOR', '租户运营', '租户运营人员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (413, 'TENANT_VIEWER', '租户查看员', '租户只读权限', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 部门级角色
INSERT INTO sys_role VALUES (421, 'DEPT_MANAGER', '部门经理', '部门管理者', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (422, 'DEPT_LEADER', '部门主管', '部门负责人', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (423, 'DEPT_SUPERVISOR', '部门组长', '部门小组长', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 用户级角色
INSERT INTO sys_role VALUES (431, 'EMPLOYEE', '普通员工', '普通员工角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (432, 'INTERN', '实习生', '实习员工角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (433, 'CONTRACTOR', '外包人员', '外包员工角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 职能角色
INSERT INTO sys_role VALUES (441, 'FINANCE_STAFF', '财务专员', '财务人员角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (442, 'HR_STAFF', 'HR专员', '人力资源专员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (443, 'IT_STAFF', 'IT专员', 'IT技术支持', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (444, 'SALES_STAFF', '销售专员', '销售人员角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (445, 'RD_ENGINEER', '研发工程师', '研发技术人员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (446, 'PROD_WORKER', '生产员工', '生产线工人', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (447, 'QC_INSPECTOR', '质检员', '质量检验人员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (448, 'CS_AGENT', '客服专员', '客户服务人员', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 特殊角色
INSERT INTO sys_role VALUES (451, 'PROJECT_MANAGER', '项目经理', '项目管理角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (452, 'DATA_ANALYST', '数据分析师', '数据分析角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (453, 'SECURITY_OFFICER', '安全专员', '信息安全角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (454, 'REPORT_VIEWER', '报表查看员', '只能查看报表', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (455, 'GUEST', '访客', '临时访客角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 已禁用角色
INSERT INTO sys_role VALUES (461, 'OLD_ROLE', '已废弃角色', '历史遗留角色', 'DISABLED', '角色已废弃', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-05-01 08:00:00', 'NOT_DELETED', NULL);

-- 补充到30条
INSERT INTO sys_role VALUES (456, 'TRAINER', '培训师', '员工培训角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (457, 'PURCHASER', '采购专员', '采购管理角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_role VALUES (458, 'WAREHOUSE_KEEPER', '仓库管理员', '仓储管理角色', 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 超级管理员
INSERT INTO sys_user VALUES (1, 'U001', 'admin', '超管', '系统管理员', 'admin@example.com', '13800000001', '$2a$10$encrypted_password', '/avatar/admin.png', 'MALE', '1985-01-01', 'ENABLED', NULL, '2025-06-12 10:30:00', '192.168.1.100', 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-06-12 10:30:00', 'NOT_DELETED', NULL);

-- 万象集团用户
INSERT INTO sys_user VALUES (2, 'U002', 'zhangsan', '张三', '张三', 'zhangsan@wanxiang.com', '13800000002', '$2a$10$encrypted_password', '/avatar/zhangsan.png', 'MALE', '1990-03-15', 'ENABLED', NULL, '2025-06-12 09:00:00', '192.168.1.101', 0, 0, 0, 1, '2025-01-05 08:00:00', 1, '2025-06-12 09:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (3, 'U003', 'lisi', '李四', '李四', 'lisi@wanxiang.com', '13800000003', '$2a$10$encrypted_password', '/avatar/lisi.png', 'MALE', '1988-06-20', 'ENABLED', NULL, '2025-06-11 16:00:00', '192.168.1.102', 0, 0, 0, 1, '2025-01-05 08:00:00', 1, '2025-06-11 16:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (4, 'U004', 'wangwu', '王五', '王五', 'wangwu@wanxiang.com', '13800000004', '$2a$10$encrypted_password', '/avatar/wangwu.png', 'FEMALE', '1992-09-10', 'ENABLED', NULL, '2025-06-12 08:30:00', '192.168.1.103', 0, 0, 0, 1, '2025-01-05 08:00:00', 1, '2025-06-12 08:30:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (5, 'U005', 'zhaoliu', '赵六', '赵六', 'zhaoliu@wanxiang.com', '13800000005', '$2a$10$encrypted_password', '/avatar/zhaoliu.png', 'MALE', '1995-11-25', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-01-05 08:00:00', 1, '2025-01-05 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (6, 'U006', 'sunqi', '孙七', '孙七', 'sunqi@wanxiang.com', '13800000006', '$2a$10$encrypted_password', '/avatar/sunqi.png', 'FEMALE', '1993-04-18', 'ENABLED', NULL, '2025-06-10 14:20:00', '192.168.1.104', 0, 0, 0, 1, '2025-01-06 08:00:00', 1, '2025-06-10 14:20:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (7, 'U007', 'zhouba', '周八', '周八', 'zhouba@wanxiang.com', '13800000007', '$2a$10$encrypted_password', '/avatar/zhouba.png', 'MALE', '1991-07-30', 'ENABLED', NULL, '2025-06-12 07:45:00', '192.168.1.105', 0, 0, 0, 1, '2025-01-06 08:00:00', 1, '2025-06-12 07:45:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (8, 'U008', 'wujiu', '吴九', '吴九', 'wujiu@wanxiang.com', '13800000008', '$2a$10$encrypted_password', '/avatar/wujiu.png', 'MALE', '1994-12-05', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-01-06 08:00:00', 1, '2025-01-06 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (9, 'U009', 'zhengshi', '郑十', '郑十', 'zhengshi@wanxiang.com', '13800000009', '$2a$10$encrypted_password', '/avatar/zhengshi.png', 'FEMALE', '1996-02-14', 'DISABLED', '已离职', NULL, NULL, 0, 0, 0, 1, '2025-01-06 08:00:00', 1, '2025-05-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (10, 'U010', 'qianyi', '钱一', '钱一', 'qianyi@wanxiang.com', '13800000010', '$2a$10$encrypted_password', '/avatar/qianyi.png', 'MALE', '1989-08-22', 'ENABLED', NULL, '2025-06-11 18:00:00', '192.168.1.106', 0, 0, 0, 1, '2025-01-07 08:00:00', 1, '2025-06-11 18:00:00', 'NOT_DELETED', NULL);

-- 鼎新集团用户
INSERT INTO sys_user VALUES (11, 'U011', 'chener', '陈二', '陈二', 'chener@dingxin.com', '13800000011', '$2a$10$encrypted_password', '/avatar/chener.png', 'MALE', '1987-05-10', 'ENABLED', NULL, '2025-06-12 09:15:00', '192.168.2.101', 0, 0, 0, 1, '2025-01-08 08:00:00', 1, '2025-06-12 09:15:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (12, 'U012', 'fengsan', '冯三', '冯三', 'fengsan@dingxin.com', '13800000012', '$2a$10$encrypted_password', '/avatar/fengsan.png', 'MALE', '1990-11-20', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-01-08 08:00:00', 1, '2025-01-08 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (13, 'U013', 'chusi', '楚四', '楚四', 'chusi@dingxin.com', '13800000013', '$2a$10$encrypted_password', '/avatar/chusi.png', 'FEMALE', '1992-06-30', 'ENABLED', NULL, '2025-06-11 15:30:00', '192.168.2.102', 0, 0, 0, 1, '2025-01-08 08:00:00', 1, '2025-06-11 15:30:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (14, 'U014', 'weiwu', '魏五', '魏五', 'weiwu@dingxin.com', '13800000014', '$2a$10$encrypted_password', '/avatar/weiwu.png', 'MALE', '1993-09-18', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-01-09 08:00:00', 1, '2025-01-09 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (15, 'U015', 'jianglu', '蒋六', '蒋六', 'jianglu@dingxin.com', '13800000015', '$2a$10$encrypted_password', '/avatar/jianglu.png', 'FEMALE', '1991-03-25', 'ENABLED', NULL, '2025-06-10 11:00:00', '192.168.2.103', 0, 0, 0, 1, '2025-01-09 08:00:00', 1, '2025-06-10 11:00:00', 'NOT_DELETED', NULL);

-- 海纳集团用户
INSERT INTO sys_user VALUES (16, 'U016', 'shenqi', '沈七', '沈七', 'shenqi@haina.com', '13800000016', '$2a$10$encrypted_password', '/avatar/shenqi.png', 'MALE', '1988-12-12', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (17, 'U017', 'hanba', '韩八', '韩八', 'hanba@haina.com', '13800000017', '$2a$10$encrypted_password', '/avatar/hanba.png', 'MALE', '1994-07-08', 'ENABLED', NULL, '2025-06-11 13:20:00', '192.168.3.101', 0, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-06-11 13:20:00', 'NOT_DELETED', NULL);

-- 跨租户用户
INSERT INTO sys_user VALUES (18, 'U018', 'yangjiu', '杨九', '杨九', 'yangjiu@example.com', '13800000018', '$2a$10$encrypted_password', '/avatar/yangjiu.png', 'MALE', '1990-10-15', 'ENABLED', NULL, '2025-06-12 08:00:00', '192.168.1.107', 0, 0, 0, 1, '2025-01-11 08:00:00', 1, '2025-06-12 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (19, 'U019', 'zhushi', '朱十', '朱十', 'zhushi@example.com', '13800000019', '$2a$10$encrypted_password', '/avatar/zhushi.png', 'FEMALE', '1992-05-20', 'ENABLED', NULL, '2025-06-11 17:30:00', '192.168.1.108', 0, 0, 0, 1, '2025-01-11 08:00:00', 1, '2025-06-11 17:30:00', 'NOT_DELETED', NULL);

-- 实习生/外包
INSERT INTO sys_user VALUES (20, 'U020', 'intern01', '实习生A', '实习生A', 'intern01@wanxiang.com', '13800000020', '$2a$10$encrypted_password', '/avatar/intern01.png', 'MALE', '2000-01-01', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (21, 'U021', 'intern02', '实习生B', '实习生B', 'intern02@wanxiang.com', '13800000021', '$2a$10$encrypted_password', '/avatar/intern02.png', 'FEMALE', '2001-06-15', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (22, 'U022', 'contractor01', '外包C', '外包C', 'contractor01@external.com', '13800000022', '$2a$10$encrypted_password', '/avatar/contractor01.png', 'MALE', '1995-08-20', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-04-01 08:00:00', 1, '2025-04-01 08:00:00', 'NOT_DELETED', NULL);

-- 补充到30条
INSERT INTO sys_user VALUES (23, 'U023', 'user23', '用户23', '用户23', 'user23@wanxiang.com', '13800000023', '$2a$10$encrypted_password', '/avatar/user23.png', 'MALE', '1989-03-10', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-02-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (24, 'U024', 'user24', '用户24', '用户24', 'user24@wanxiang.com', '13800000024', '$2a$10$encrypted_password', '/avatar/user24.png', 'FEMALE', '1991-07-22', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-01 08:00:00', 1, '2025-02-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (25, 'U025', 'user25', '用户25', '用户25', 'user25@dingxin.com', '13800000025', '$2a$10$encrypted_password', '/avatar/user25.png', 'MALE', '1993-11-05', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-02 08:00:00', 1, '2025-02-02 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (26, 'U026', 'user26', '用户26', '用户26', 'user26@dingxin.com', '13800000026', '$2a$10$encrypted_password', '/avatar/user26.png', 'FEMALE', '1990-04-18', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-02 08:00:00', 1, '2025-02-02 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (27, 'U027', 'user27', '用户27', '用户27', 'user27@haina.com', '13800000027', '$2a$10$encrypted_password', '/avatar/user27.png', 'MALE', '1988-09-30', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-03 08:00:00', 1, '2025-02-03 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (28, 'U028', 'user28', '用户28', '用户28', 'user28@wanxiang.com', '13800000028', '$2a$10$encrypted_password', '/avatar/user28.png', 'MALE', '1992-12-25', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-04 08:00:00', 1, '2025-02-04 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (29, 'U029', 'user29', '用户29', '用户29', 'user29@wanxiang.com', '13800000029', '$2a$10$encrypted_password', '/avatar/user29.png', 'FEMALE', '1994-05-12', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-04 08:00:00', 1, '2025-02-04 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user VALUES (30, 'U030', 'user30', '用户30', '用户30', 'user30@dingxin.com', '13800000030', '$2a$10$encrypted_password', '/avatar/user30.png', 'MALE', '1987-08-08', 'ENABLED', NULL, NULL, NULL, 0, 0, 0, 1, '2025-02-05 08:00:00', 1, '2025-02-05 08:00:00', 'NOT_DELETED', NULL);

-- 一级菜单权限
INSERT INTO sys_perm VALUES (501, 'PERM_SYSTEM', '系统管理', '系统管理菜单', 'MENU', 0, '/501', 1, 'URL', '/system', NULL, 'system', 1, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (502, 'PERM_BUSINESS', '业务管理', '业务管理菜单', 'MENU', 0, '/502', 1, 'URL', '/business', NULL, 'business', 2, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (503, 'PERM_REPORT', '报表中心', '报表查看菜单', 'MENU', 0, '/503', 1, 'URL', '/report', NULL, 'chart', 3, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (504, 'PERM_MONITOR', '系统监控', '系统监控菜单', 'MENU', 0, '/504', 1, 'URL', '/monitor', NULL, 'monitor', 4, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级菜单权限（系统管理下）
INSERT INTO sys_perm VALUES (511, 'PERM_USER_MANAGE', '用户管理', '用户信息管理', 'MENU', 501, '/501/511', 2, 'URL', '/system/user', NULL, 'user', 1, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (512, 'PERM_ROLE_MANAGE', '角色管理', '角色权限管理', 'MENU', 501, '/501/512', 2, 'URL', '/system/role', NULL, 'role', 2, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (513, 'PERM_DEPT_MANAGE', '部门管理', '部门组织管理', 'MENU', 501, '/501/513', 2, 'URL', '/system/dept', NULL, 'dept', 3, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (514, 'PERM_TENANT_MANAGE', '租户管理', '租户信息管理', 'MENU', 501, '/501/514', 2, 'URL', '/system/tenant', NULL, 'tenant', 4, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (515, 'PERM_MENU_MANAGE', '菜单管理', '菜单权限管理', 'MENU', 501, '/501/515', 2, 'URL', '/system/menu', NULL, 'menu', 5, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 按钮权限（用户管理下）
INSERT INTO sys_perm VALUES (521, 'PERM_USER_VIEW', '用户查看', '查看用户信息', 'BUTTON', 511, '/501/511/521', 3, 'METHOD', '/api/user/list', 'GET', NULL, 1, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (522, 'PERM_USER_ADD', '用户新增', '新增用户', 'BUTTON', 511, '/501/511/522', 3, 'METHOD', '/api/user/create', 'POST', NULL, 2, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (523, 'PERM_USER_EDIT', '用户编辑', '编辑用户信息', 'BUTTON', 511, '/501/511/523', 3, 'METHOD', '/api/user/update', 'PUT', NULL, 3, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (524, 'PERM_USER_DELETE', '用户删除', '删除用户', 'BUTTON', 511, '/501/511/524', 3, 'METHOD', '/api/user/delete', 'DELETE', NULL, 4, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (525, 'PERM_USER_EXPORT', '用户导出', '导出用户数据', 'BUTTON', 511, '/501/511/525', 3, 'METHOD', '/api/user/export', 'POST', NULL, 5, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (526, 'PERM_USER_IMPORT', '用户导入', '导入用户数据', 'BUTTON', 511, '/501/511/526', 3, 'METHOD', '/api/user/import', 'POST', NULL, 6, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (527, 'PERM_USER_RESET_PWD', '重置密码', '重置用户密码', 'BUTTON', 511, '/501/511/527', 3, 'METHOD', '/api/user/reset-password', 'POST', NULL, 7, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 按钮权限（角色管理下）
INSERT INTO sys_perm VALUES (531, 'PERM_ROLE_VIEW', '角色查看', '查看角色信息', 'BUTTON', 512, '/501/512/531', 3, 'METHOD', '/api/role/list', 'GET', NULL, 1, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (532, 'PERM_ROLE_ADD', '角色新增', '新增角色', 'BUTTON', 512, '/501/512/532', 3, 'METHOD', '/api/role/create', 'POST', NULL, 2, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (533, 'PERM_ROLE_EDIT', '角色编辑', '编辑角色信息', 'BUTTON', 512, '/501/512/533', 3, 'METHOD', '/api/role/update', 'PUT', NULL, 3, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (534, 'PERM_ROLE_DELETE', '角色删除', '删除角色', 'BUTTON', 512, '/501/512/534', 3, 'METHOD', '/api/role/delete', 'DELETE', NULL, 4, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (535, 'PERM_ROLE_AUTH', '角色授权', '配置角色权限', 'BUTTON', 512, '/501/512/535', 3, 'METHOD', '/api/role/grant', 'POST', NULL, 5, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 二级菜单权限（业务管理下）
INSERT INTO sys_perm VALUES (541, 'PERM_ORDER_MANAGE', '订单管理', '订单信息管理', 'MENU', 502, '/502/541', 2, 'URL', '/business/order', NULL, 'order', 1, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (542, 'PERM_PRODUCT_MANAGE', '产品管理', '产品信息管理', 'MENU', 502, '/502/542', 2, 'URL', '/business/product', NULL, 'product', 2, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (543, 'PERM_CUSTOMER_MANAGE', '客户管理', '客户信息管理', 'MENU', 502, '/502/543', 2, 'URL', '/business/customer', NULL, 'customer', 3, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (544, 'PERM_SUPPLIER_MANAGE', '供应商管理', '供应商信息管理', 'MENU', 502, '/502/544', 2, 'URL', '/business/supplier', NULL, 'supplier', 4, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 报表权限
INSERT INTO sys_perm VALUES (551, 'PERM_SALES_REPORT', '销售报表', '销售数据报表', 'MENU', 503, '/503/551', 2, 'URL', '/report/sales', NULL, 'chart-line', 1, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (552, 'PERM_FINANCE_REPORT', '财务报表', '财务数据报表', 'MENU', 503, '/503/552', 2, 'URL', '/report/finance', NULL, 'chart-bar', 2, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (553, 'PERM_PROD_REPORT', '生产报表', '生产数据报表', 'MENU', 503, '/503/553', 2, 'URL', '/report/production', NULL, 'chart-pie', 3, TRUE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 数据权限
INSERT INTO sys_perm VALUES (561, 'PERM_DATA_USER', '用户数据权限', '用户表数据访问', 'DATA', 0, '/561', 1, 'TABLE', 'sys_user', NULL, NULL, 1, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (562, 'PERM_DATA_ORDER', '订单数据权限', '订单表数据访问', 'DATA', 0, '/562', 1, 'TABLE', 'biz_order', NULL, NULL, 2, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm VALUES (563, 'PERM_DATA_SALARY', '薪资数据权限', '薪资表数据访问', 'DATA', 0, '/563', 1, 'TABLE', 'hr_salary', NULL, NULL, 3, FALSE, 'ENABLED', NULL, 0, 0, 0, 1, '2025-01-01 08:00:00', 1, '2025-01-01 08:00:00', 'NOT_DELETED', NULL);

-- 场景1：财务部绑定到各租户
INSERT INTO sys_tenant_policy VALUES (2001, 'TP_DEPT_001', '财务部→万象集团', 'DEPT', 301, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2002, 'TP_DEPT_002', '财务部→华东分公司', 'DEPT', 301, 111, 'ACTIVE', NULL, 111, 0, 0, 1, '2025-02-10 08:00:00', 1, '2025-02-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2003, 'TP_DEPT_003', '财务部→鼎新集团', 'DEPT', 301, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);

-- 场景2：研发部绑定到各租户
INSERT INTO sys_tenant_policy VALUES (2004, 'TP_DEPT_004', '研发部→万象集团', 'DEPT', 302, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2005, 'TP_DEPT_005', '研发部→华东分公司', 'DEPT', 302, 111, 'ACTIVE', NULL, 111, 0, 0, 1, '2025-02-10 08:00:00', 1, '2025-02-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2006, 'TP_DEPT_006', '研发部→上海办事处', 'DEPT', 302, 121, 'ACTIVE', NULL, 121, 0, 0, 1, '2025-03-10 08:00:00', 1, '2025-03-10 08:00:00', 'NOT_DELETED', NULL);

-- 场景3：销售部绑定到各租户
INSERT INTO sys_tenant_policy VALUES (2007, 'TP_DEPT_007', '销售部→万象集团', 'DEPT', 303, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2008, 'TP_DEPT_008', '销售部→华南分公司', 'DEPT', 303, 112, 'ACTIVE', NULL, 112, 0, 0, 1, '2025-02-10 08:00:00', 1, '2025-02-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2009, 'TP_DEPT_009', '销售部→鼎新集团', 'DEPT', 303, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);

-- 场景4：其他部门绑定
INSERT INTO sys_tenant_policy VALUES (2010, 'TP_DEPT_010', '人力资源部→万象集团', 'DEPT', 304, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2011, 'TP_DEPT_011', 'IT部→万象集团', 'DEPT', 305, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2012, 'TP_DEPT_012', '生产部→鼎新集团', 'DEPT', 307, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2013, 'TP_DEPT_013', '质量部→鼎新集团', 'DEPT', 308, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2014, 'TP_DEPT_014', '客服部→海纳集团', 'DEPT', 310, 103, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (2015, 'TP_DEPT_015', '生产部→武汉工厂', 'DEPT', 307, 211, 'ACTIVE', NULL, 211, 0, 0, 1, '2025-03-10 08:00:00', 1, '2025-03-10 08:00:00', 'NOT_DELETED', NULL);

-- 场景5：角色绑定到租户
INSERT INTO sys_tenant_policy VALUES (3001, 'TP_ROLE_001', '超级管理员→万象集团', 'ROLE', 401, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3002, 'TP_ROLE_002', '租户管理员→万象集团', 'ROLE', 411, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3003, 'TP_ROLE_003', '部门经理→万象集团', 'ROLE', 421, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3004, 'TP_ROLE_004', '普通员工→万象集团', 'ROLE', 431, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3005, 'TP_ROLE_005', '财务专员→万象集团', 'ROLE', 441, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3006, 'TP_ROLE_006', '销售专员→万象集团', 'ROLE', 444, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3007, 'TP_ROLE_007', '研发工程师→万象集团', 'ROLE', 445, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3008, 'TP_ROLE_008', '租户管理员→鼎新集团', 'ROLE', 411, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3009, 'TP_ROLE_009', '部门经理→鼎新集团', 'ROLE', 421, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3010, 'TP_ROLE_010', '普通员工→鼎新集团', 'ROLE', 431, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3011, 'TP_ROLE_011', '生产员工→鼎新集团', 'ROLE', 446, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3012, 'TP_ROLE_012', '质检员→鼎新集团', 'ROLE', 447, 102, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3013, 'TP_ROLE_013', '租户管理员→海纳集团', 'ROLE', 411, 103, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3014, 'TP_ROLE_014', '普通员工→海纳集团', 'ROLE', 431, 103, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-10 08:00:00', 1, '2025-01-10 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_tenant_policy VALUES (3015, 'TP_ROLE_015', '实习生→万象集团', 'ROLE', 432, 101, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-03-10 08:00:00', 1, '2025-03-10 08:00:00', 'NOT_DELETED', NULL);

-- 张三(U002)的绑定
INSERT INTO sys_user_policy VALUES (4001, 'UP_001', '张三→万象集团', 2, 'TENANT', 101, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-15 08:00:00', 1, '2025-01-15 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4002, 'UP_002', '张三→财务部', 4001, 'DEPT', 2001, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-15 08:00:00', 1, '2025-01-15 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4003, 'UP_003', '张三→财务专员角色', 4001, 'ROLE', 3005, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-15 08:00:00', 1, '2025-01-15 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4004, 'UP_004', '张三→鼎新集团', 2, 'TENANT', 102, FALSE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4005, 'UP_005', '张三→财务部(鼎新)', 4004, 'DEPT', 2003, TRUE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4006, 'UP_006', '张三→部门经理角色(鼎新)', 4004, 'ROLE', 3009, FALSE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 李四(U003)的绑定
INSERT INTO sys_user_policy VALUES (4007, 'UP_007', '李四→万象集团', 3, 'TENANT', 101, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-16 08:00:00', 1, '2025-01-16 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4008, 'UP_008', '李四→研发部', 4007, 'DEPT', 2004, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-16 08:00:00', 1, '2025-01-16 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4009, 'UP_009', '李四→研发工程师', 4007, 'ROLE', 3007, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-16 08:00:00', 1, '2025-01-16 08:00:00', 'NOT_DELETED', NULL);

-- 王五(U004)的绑定
INSERT INTO sys_user_policy VALUES (4010, 'UP_010', '王五→万象集团', 4, 'TENANT', 101, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-17 08:00:00', 1, '2025-01-17 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4011, 'UP_011', '王五→销售部', 4010, 'DEPT', 2007, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-17 08:00:00', 1, '2025-01-17 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4012, 'UP_012', '王五→销售专员', 4010, 'ROLE', 3006, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-17 08:00:00', 1, '2025-01-17 08:00:00', 'NOT_DELETED', NULL);

-- 赵六(U005)的绑定
INSERT INTO sys_user_policy VALUES (4013, 'UP_013', '赵六→华东分公司', 5, 'TENANT', 111, TRUE, 'ACTIVE', NULL, 111, 0, 0, 1, '2025-02-15 08:00:00', 1, '2025-02-15 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4014, 'UP_014', '赵六→研发部(华东)', 4013, 'DEPT', 2005, TRUE, 'ACTIVE', NULL, 111, 0, 0, 1, '2025-02-15 08:00:00', 1, '2025-02-15 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4015, 'UP_015', '赵六→普通员工', 4013, 'ROLE', 3004, FALSE, 'ACTIVE', NULL, 111, 0, 0, 1, '2025-02-15 08:00:00', 1, '2025-02-15 08:00:00', 'NOT_DELETED', NULL);

-- 陈二(U011)的绑定
INSERT INTO sys_user_policy VALUES (4016, 'UP_016', '陈二→鼎新集团', 11, 'TENANT', 102, TRUE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-18 08:00:00', 1, '2025-01-18 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4017, 'UP_017', '陈二→生产部', 4016, 'DEPT', 2012, TRUE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-18 08:00:00', 1, '2025-01-18 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4018, 'UP_018', '陈二→部门经理', 4016, 'ROLE', 3009, FALSE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-18 08:00:00', 1, '2025-01-18 08:00:00', 'NOT_DELETED', NULL);

-- 冯三(U012)的绑定
INSERT INTO sys_user_policy VALUES (4019, 'UP_019', '冯三→鼎新集团', 12, 'TENANT', 102, TRUE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-19 08:00:00', 1, '2025-01-19 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4020, 'UP_020', '冯三→质量部', 4019, 'DEPT', 2013, TRUE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-19 08:00:00', 1, '2025-01-19 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4021, 'UP_021', '冯三→质检员', 4019, 'ROLE', 3012, FALSE, 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-19 08:00:00', 1, '2025-01-19 08:00:00', 'NOT_DELETED', NULL);

-- 杨九(U018)：跨租户用户
INSERT INTO sys_user_policy VALUES (4022, 'UP_022', '杨九→万象集团', 18, 'TENANT', 101, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-21 08:00:00', 1, '2025-01-21 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4023, 'UP_023', '杨九→研发部(万象)', 4022, 'DEPT', 2004, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-21 08:00:00', 1, '2025-01-21 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4024, 'UP_024', '杨九→研发工程师(万象)', 4022, 'ROLE', 3007, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-21 08:00:00', 1, '2025-01-21 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4025, 'UP_025', '杨九→海纳集团', 18, 'TENANT', 103, FALSE, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-22 08:00:00', 1, '2025-01-22 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4026, 'UP_026', '杨九→客服部(海纳)', 4025, 'DEPT', 2014, TRUE, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-22 08:00:00', 1, '2025-01-22 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4027, 'UP_027', '杨九→普通员工(海纳)', 4025, 'ROLE', 3014, FALSE, 'ACTIVE', NULL, 103, 0, 0, 1, '2025-01-22 08:00:00', 1, '2025-01-22 08:00:00', 'NOT_DELETED', NULL);

-- 实习生(U020)的绑定
INSERT INTO sys_user_policy VALUES (4028, 'UP_028', '实习生A→万象集团', 20, 'TENANT', 101, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4029, 'UP_029', '实习生A→研发部', 4028, 'DEPT', 2004, TRUE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_user_policy VALUES (4030, 'UP_030', '实习生A→实习生角色', 4028, 'ROLE', 3015, FALSE, 'ACTIVE', NULL, 101, 0, 0, 1, '2025-03-01 08:00:00', 1, '2025-03-01 08:00:00', 'NOT_DELETED', NULL);

-- 场景1：用户管理权限(511)授予万象集团
INSERT INTO sys_perm_policy VALUES (5001, 'PP_001', '用户管理权限→万象集团', 511, 'TENANT', 101, 'ALL', 'sys_user', '{"user_name":["READ","CREATE","UPDATE"],"email":["READ","CREATE","UPDATE"],"phone":["READ","CREATE","UPDATE"],"password":["CREATE","UPDATE"],"status":["READ","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景2：用户查看权限(521)授予万象集团
INSERT INTO sys_perm_policy VALUES (5002, 'PP_002', '用户查看权限→万象集团', 521, 'TENANT', 101, 'ALL', 'sys_user', '{"user_name":["READ"],"email":["READ"],"phone":["READ"],"nick_name":["READ"],"status":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景3：用户新增权限(522)授予万象集团
INSERT INTO sys_perm_policy VALUES (5003, 'PP_003', '用户新增权限→万象集团', 522, 'TENANT', 101, 'ALL', 'sys_user', '{"user_name":["CREATE"],"email":["CREATE"],"phone":["CREATE"],"password":["CREATE"],"nick_name":["CREATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景4：用户编辑权限(523)授予万象集团
INSERT INTO sys_perm_policy VALUES (5004, 'PP_004', '用户编辑权限→万象集团', 523, 'TENANT', 101, 'ALL', 'sys_user', '{"user_name":["UPDATE"],"email":["UPDATE"],"phone":["UPDATE"],"nick_name":["UPDATE"],"status":["UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景5：用户删除权限(524)授予万象集团
INSERT INTO sys_perm_policy VALUES (5005, 'PP_005', '用户删除权限→万象集团', 524, 'TENANT', 101, 'ALL', NULL, '{}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景6：订单管理权限(541)授予万象集团
INSERT INTO sys_perm_policy VALUES (5006, 'PP_006', '订单管理权限→万象集团', 541, 'TENANT', 101, 'ALL', 'biz_order', '{"order_no":["READ","CREATE","UPDATE"],"customer_name":["READ"],"amount":["READ","CREATE","UPDATE"],"status":["READ","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景7：报表查看权限(551)授予万象集团
INSERT INTO sys_perm_policy VALUES (5007, 'PP_007', '销售报表权限→万象集团', 551, 'TENANT', 101, 'ALL', NULL, '{}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景8：系统权限授予鼎新集团
INSERT INTO sys_perm_policy VALUES (5008, 'PP_008', '用户管理权限→鼎新集团', 511, 'TENANT', 102, 'ALL', 'sys_user', '{"user_name":["READ","CREATE","UPDATE"],"email":["READ"],"phone":["READ"],"status":["READ","UPDATE"]}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm_policy VALUES (5009, 'PP_009', '生产报表权限→鼎新集团', 553, 'TENANT', 102, 'ALL', NULL, '{}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-20 08:00:00', 1, '2025-01-20 08:00:00', 'NOT_DELETED', NULL);

-- 场景9：租户权限(5002-用户查看)→租户角色-普通员工(3004)
INSERT INTO sys_perm_policy VALUES (5010, 'PP_010', '用户查看→普通员工角色', 5002, 'ROLE', 3004, 'SELF', 'sys_user', '{"user_name":["READ"],"email":["READ"],"phone":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景10：租户权限(5001-用户管理)→租户角色-租户管理员(3002)
INSERT INTO sys_perm_policy VALUES (5011, 'PP_011', '用户管理→租户管理员', 5001, 'ROLE', 3002, 'ALL', 'sys_user', '{"user_name":["READ","CREATE","UPDATE","DELETE"],"email":["READ","CREATE","UPDATE"],"phone":["READ","CREATE","UPDATE"],"password":["CREATE","UPDATE"],"status":["READ","UPDATE","DELETE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景11：租户权限(5003-用户新增)→租户角色-部门经理(3003)
INSERT INTO sys_perm_policy VALUES (5012, 'PP_012', '用户新增→部门经理', 5003, 'ROLE', 3003, 'DEPT_AND_SUB', 'sys_user', '{"user_name":["CREATE"],"email":["CREATE"],"phone":["CREATE"],"password":["CREATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景12：租户权限(5004-用户编辑)→租户角色-部门经理(3003)
INSERT INTO sys_perm_policy VALUES (5013, 'PP_013', '用户编辑→部门经理', 5004, 'ROLE', 3003, 'DEPT_AND_SUB', 'sys_user', '{"email":["UPDATE"],"phone":["UPDATE"],"status":["UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景13：租户权限(5006-订单管理)→租户角色-销售专员(3006)
INSERT INTO sys_perm_policy VALUES (5014, 'PP_014', '订单管理→销售专员', 5006, 'ROLE', 3006, 'SELF', 'biz_order', '{"order_no":["READ","CREATE","UPDATE"],"customer_name":["READ"],"amount":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景14：租户权限(5007-销售报表)→租户角色-部门经理(3003)
INSERT INTO sys_perm_policy VALUES (5015, 'PP_015', '销售报表→部门经理', 5007, 'ROLE', 3003, 'DEPT_AND_SUB', NULL, '{}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景15：租户权限(5005-用户删除)→租户部门-财务部(2001)
INSERT INTO sys_perm_policy VALUES (5016, 'PP_016', '用户删除→财务部', 5005, 'DEPT', 2001, 'DEPT', NULL, '{}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-26 08:00:00', 1, '2025-01-26 08:00:00', 'NOT_DELETED', NULL);

-- 场景16：租户权限(5002-用户查看)→租户部门-研发部(2004)
INSERT INTO sys_perm_policy VALUES (5017, 'PP_017', '用户查看→研发部', 5002, 'DEPT', 2004, 'DEPT', 'sys_user', '{"user_name":["READ"],"email":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-26 08:00:00', 1, '2025-01-26 08:00:00', 'NOT_DELETED', NULL);

-- 场景17：租户权限(5006-订单管理)→租户部门-销售部(2007)
INSERT INTO sys_perm_policy VALUES (5018, 'PP_018', '订单管理→销售部', 5006, 'DEPT', 2007, 'DEPT', 'biz_order', '{"order_no":["READ","CREATE","UPDATE"],"customer_name":["READ","CREATE","UPDATE"],"amount":["READ","CREATE","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-26 08:00:00', 1, '2025-01-26 08:00:00', 'NOT_DELETED', NULL);

-- 场景18：租户权限(5001-用户管理)→租户用户-张三(4001)
INSERT INTO sys_perm_policy VALUES (5019, 'PP_019', '用户管理→张三(特殊授权)', 5001, 'USER', 4001, 'DEPT', 'sys_user', '{"user_name":["READ","UPDATE"],"email":["READ","UPDATE"],"status":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-27 08:00:00', 1, '2025-01-27 08:00:00', 'NOT_DELETED', NULL);

-- 场景19：租户权限(5007-销售报表)→租户用户-王五(4010)
INSERT INTO sys_perm_policy VALUES (5020, 'PP_020', '销售报表→王五(特殊授权)', 5007, 'USER', 4010, 'SELF', NULL, '{}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-27 08:00:00', 1, '2025-01-27 08:00:00', 'NOT_DELETED', NULL);

-- 场景20：租户权限(5008-用户管理)→租户角色-租户管理员(3008)
INSERT INTO sys_perm_policy VALUES (5021, 'PP_021', '用户管理→租户管理员(鼎新)', 5008, 'ROLE', 3008, 'ALL', 'sys_user', '{"user_name":["READ","CREATE","UPDATE","DELETE"],"email":["READ","CREATE","UPDATE"],"status":["READ","UPDATE"]}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景21：租户权限(5008-用户管理)→租户角色-部门经理(3009)
INSERT INTO sys_perm_policy VALUES (5022, 'PP_022', '用户管理→部门经理(鼎新)', 5008, 'ROLE', 3009, 'DEPT_AND_SUB', 'sys_user', '{"user_name":["READ","CREATE","UPDATE"],"email":["READ"],"status":["READ","UPDATE"]}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景22：租户权限(5009-生产报表)→租户角色-生产员工(3011)
INSERT INTO sys_perm_policy VALUES (5023, 'PP_023', '生产报表→生产员工(鼎新)', 5009, 'ROLE', 3011, 'DEPT', NULL, '{}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-01-25 08:00:00', 'NOT_DELETED', NULL);

-- 场景23：租户权限(5008-用户管理)→租户部门-生产部(2012)
INSERT INTO sys_perm_policy VALUES (5024, 'PP_024', '用户管理→生产部(鼎新)', 5008, 'DEPT', 2012, 'DEPT', 'sys_user', '{"user_name":["READ"],"phone":["READ"],"status":["READ"]}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-26 08:00:00', 1, '2025-01-26 08:00:00', 'NOT_DELETED', NULL);

-- 场景24：租户权限(5009-生产报表)→租户部门-质量部(2013)
INSERT INTO sys_perm_policy VALUES (5025, 'PP_025', '生产报表→质量部(鼎新)', 5009, 'DEPT', 2013, 'DEPT', NULL, '{}', 'ACTIVE', NULL, 102, 0, 0, 1, '2025-01-26 08:00:00', 1, '2025-01-26 08:00:00', 'NOT_DELETED', NULL);

-- 场景25：已禁用的权限授予（测试级联禁用）
INSERT INTO sys_perm_policy VALUES (5026, 'PP_026', '用户查看→已禁用角色', 5002, 'ROLE', 3004, 'SELF', 'sys_user', '{"user_name":["READ"]}', 'DISABLED', '父权限已禁用', 101, 0, 0, 1, '2025-01-25 08:00:00', 1, '2025-05-01 08:00:00', 'NOT_DELETED', NULL);

-- 场景26：数据权限授予
INSERT INTO sys_perm_policy VALUES (5027, 'PP_027', '用户数据权限→财务专员', 561, 'TENANT', 101, 'ALL', 'sys_user', '{"user_name":["READ"],"email":["READ"],"phone":["READ"],"salary":["READ","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-28 08:00:00', 1, '2025-01-28 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm_policy VALUES (5028, 'PP_028', '订单数据权限→万象集团', 562, 'TENANT', 101, 'ALL', 'biz_order', '{"order_no":["READ","CREATE","UPDATE"],"amount":["READ","CREATE","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-28 08:00:00', 1, '2025-01-28 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm_policy VALUES (5029, 'PP_029', '薪资数据权限→财务部', 563, 'TENANT', 101, 'DEPT', 'hr_salary', '{"employee_name":["READ"],"base_salary":["READ","UPDATE"],"bonus":["READ","UPDATE"],"total":["READ"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-28 08:00:00', 1, '2025-01-28 08:00:00', 'NOT_DELETED', NULL);
INSERT INTO sys_perm_policy VALUES (5030, 'PP_030', '薪资数据权限→HR专员', 5029, 'ROLE', 3005, 'DEPT', 'hr_salary', '{"employee_name":["READ"],"base_salary":["READ","CREATE","UPDATE"],"bonus":["READ","UPDATE"]}', 'ACTIVE', NULL, 101, 0, 0, 1, '2025-01-29 08:00:00', 1, '2025-01-29 08:00:00', 'NOT_DELETED', NULL);