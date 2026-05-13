-- =============================================================================
-- 数据库变更脚本 - 角色策略表及角色表优化
-- 创建日期: 2026-05-13
-- 说明: 支持角色级联禁用功能，与权限策略保持一致
-- =============================================================================

-- =============================================================================
-- 1. 新增角色策略控制表
-- =============================================================================

CREATE TABLE sys_role_policy (
    id                  BIGINT          NOT NULL,
    target_type         SMALLINT        NOT NULL,
    target_id           BIGINT          NOT NULL,
    target_name         VARCHAR(100)    NOT NULL,
    role_id             BIGINT          NOT NULL,
    role_name           VARCHAR(50)     NOT NULL,
    action              SMALLINT        NOT NULL,
    priority            INT             DEFAULT 0,
    inheritance_enabled BOOLEAN         DEFAULT TRUE,
    create_by           BIGINT          DEFAULT 0,
    create_by_name      VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
    update_by_name      VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_role_policy IS '角色策略控制表 - 实现角色级联禁用及策略继承';
COMMENT ON COLUMN sys_role_policy.id IS '主键 ID (雪花算法)';
COMMENT ON COLUMN sys_role_policy.target_type IS '目标类型 (1-系统 2-租户 3-用户)';
COMMENT ON COLUMN sys_role_policy.target_id IS '目标 ID (对应租户/用户 ID)';
COMMENT ON COLUMN sys_role_policy.target_name IS '目标名称 (对应租户/用户名称)';
COMMENT ON COLUMN sys_role_policy.role_id IS '关联角色 ID';
COMMENT ON COLUMN sys_role_policy.role_name IS '关联角色名称';
COMMENT ON COLUMN sys_role_policy.action IS '动作 (1-允许 2-拒绝)';
COMMENT ON COLUMN sys_role_policy.priority IS '优先级 (数字越大优先级越高)';
COMMENT ON COLUMN sys_role_policy.inheritance_enabled IS '是否向下继承';
COMMENT ON COLUMN sys_role_policy.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_role_policy.create_by_name IS '创建人名称(新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_role_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_policy.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_role_policy.update_by_name IS '更新人名称(新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_role_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_policy.is_deleted IS '逻辑删除 (0-正常 1-删除)';

-- 创建索引优化查询性能
CREATE INDEX idx_role_policy_target ON sys_role_policy(target_type, target_id, is_deleted);
CREATE INDEX idx_role_policy_role ON sys_role_policy(role_id, is_deleted);
CREATE INDEX idx_role_policy_action ON sys_role_policy(action, is_deleted);

-- =============================================================================
-- 2. sys_role 表优化 - 新增缺失字段
-- =============================================================================

-- 新增排序字段
ALTER TABLE sys_role
ADD COLUMN sort_order INT DEFAULT 0;

COMMENT ON COLUMN sys_role.sort_order IS '排序顺序 (数字越小越靠前)';

-- 新增描述字段
ALTER TABLE sys_role
ADD COLUMN description VARCHAR(500) DEFAULT '';

COMMENT ON COLUMN sys_role.description IS '角色描述 (说明角色用途和职责范围)';

-- =============================================================================
-- 3. sys_user_role_rel 表优化 - 支持临时角色和有效期控制
-- =============================================================================

-- 新增生效时间字段
ALTER TABLE sys_user_role_rel
ADD COLUMN effective_time TIMESTAMP DEFAULT NULL;

COMMENT ON COLUMN sys_user_role_rel.effective_time IS '角色生效时间 (NULL表示立即生效)';

-- 新增失效时间字段
ALTER TABLE sys_user_role_rel
ADD COLUMN expire_time TIMESTAMP DEFAULT NULL;

COMMENT ON COLUMN sys_user_role_rel.expire_time IS '角色失效时间 (NULL表示永久有效)';

-- 创建索引优化有效期查询
CREATE INDEX idx_user_role_rel_time ON sys_user_role_rel(effective_time, expire_time, is_deleted);

-- =============================================================================
-- 4. sys_dept 表优化 - 补充冗余字段保持一致性
-- =============================================================================

-- 新增负责人名称冗余字段
ALTER TABLE sys_dept
ADD COLUMN leader_name VARCHAR(100) DEFAULT NULL;

COMMENT ON COLUMN sys_dept.leader_name IS '部门负责人名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';

-- =============================================================================
-- 5. 初始化数据 - 系统级角色策略示例 (可选)
-- =============================================================================

-- 示例: 系统级拒绝某角色的策略记录
-- INSERT INTO sys_role_policy (id, target_type, target_id, target_name, role_id, role_name, action, priority, inheritance_enabled, create_by, create_by_name, update_by, update_by_name)
-- VALUES (1001, 1, 0, '系统级', 1234567890, '某角色名称', 2, 100, TRUE, 0, 'system', 0, 'system');

-- =============================================================================
-- 6. 数据迁移注意事项
-- =============================================================================

-- 注意:
-- 1. sys_role 新增字段不影响现有数据，使用默认值
-- 2. sys_user_role_rel 新增时间字段为 NULL，不影响现有角色关联
-- 3. sys_role_policy 为新增表，初始为空，需要业务逻辑逐步填充
-- 4. 建议在低峰期执行此脚本
-- 5. 执行前务必备份数据库

-- 验证脚本执行结果:
-- SELECT COUNT(*) FROM sys_role_policy;  -- 应返回 0 (新表)
-- SELECT column_name FROM information_schema.columns WHERE table_name='sys_role' AND column_name IN ('sort_order', 'description');
-- SELECT column_name FROM information_schema.columns WHERE table_name='sys_user_role_rel' AND column_name IN ('effective_time', 'expire_time');
