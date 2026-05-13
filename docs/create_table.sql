-- =============================================================================
-- 1. 核心租户域 (Tenant Core)
-- =============================================================================

-- 租户信息表 已改
CREATE TABLE sys_tenant (
    id                  BIGINT          NOT NULL,
    tenant_name         VARCHAR(100)    NOT NULL,
    tenant_code         VARCHAR(50)     NOT NULL,
    parent_id           BIGINT          DEFAULT 0,
	parent_name			VARCHAR(100)    DEFAULT NULL,
    ancestors           VARCHAR(500)    DEFAULT '',
    contact_name        VARCHAR(50)     DEFAULT '',
    contact_phone       VARCHAR(20)     DEFAULT '',
    status              SMALLINT        DEFAULT 1,
    expire_time         TIMESTAMP       DEFAULT NULL,
    package_id          BIGINT          DEFAULT 0,
	package_name		VARCHAR(100)    DEFAULT NULL,
    ext_attributes      JSONB           DEFAULT '{}'::jsonb,
    has_children        BOOLEAN         DEFAULT false,
    create_by           BIGINT          DEFAULT 0,
	create_by_name		VARCHAR(100)    DEFAULT NULL,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_tenant IS '租户信息表 - 存储租户基础信息，支持无限层级';
COMMENT ON COLUMN sys_tenant.id IS '主键 ID (雪花算法)';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户唯一编码';
COMMENT ON COLUMN sys_tenant.parent_id IS '父租户 ID (0 为根租户)';
COMMENT ON COLUMN sys_tenant.ancestors IS '祖级列表 (物化路径，如 0,100,200)';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.status IS '状态 (1-正常 0-冻结)';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_id IS '当前主套餐 ID';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性 (JSONB，存储行业特定配置)';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_tenant.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_tenant.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除 (0-正常 1-删除)';
COMMENT ON COLUMN sys_tenant.parent_name IS '父租户名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.package_name IS '套餐名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.create_by_name IS '创建人名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.update_by_name IS '更新人名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.has_children IS '是否有子租户 (true/false)';

-- 租户套餐订阅表
CREATE TABLE sys_tenant_subscription (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    tenant_name         VARCHAR(100)    NOT NULL,
    package_id          BIGINT          NOT NULL,
    package_name        VARCHAR(100)    NOT NULL,
    subscription_type   SMALLINT        DEFAULT 1,
    start_time          TIMESTAMP       NOT NULL,
    end_time            TIMESTAMP       NOT NULL,
    status              SMALLINT        DEFAULT 1,
    is_auto_renew       BOOLEAN         DEFAULT FALSE,
    source_type         SMALLINT        DEFAULT 1,
    parent_grant_id     BIGINT          DEFAULT 0,
    parent_tenant_name  VARCHAR(100)    NOT NULL,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_tenant_subscription IS '租户套餐订阅表 - 记录租户购买的套餐及订阅状态';
COMMENT ON COLUMN sys_tenant_subscription.id IS '主键 ID';
COMMENT ON COLUMN sys_tenant_subscription.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_tenant_subscription.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant_subscription.package_id IS '套餐产品 ID';
COMMENT ON COLUMN sys_tenant_subscription.package_name IS '套餐产品名称';
COMMENT ON COLUMN sys_tenant_subscription.subscription_type IS '订阅类型 (1-自购 2-父租户分配)';
COMMENT ON COLUMN sys_tenant_subscription.start_time IS '订阅开始时间';
COMMENT ON COLUMN sys_tenant_subscription.end_time IS '订阅结束时间';
COMMENT ON COLUMN sys_tenant_subscription.status IS '状态 (1-生效 0-过期)';
COMMENT ON COLUMN sys_tenant_subscription.is_auto_renew IS '是否自动续费';
COMMENT ON COLUMN sys_tenant_subscription.source_type IS '来源类型';
COMMENT ON COLUMN sys_tenant_subscription.parent_grant_id IS '父租户分配记录 ID';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant_subscription.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_tenant_subscription.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant_subscription.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_subscription.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_tenant_subscription.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant_subscription.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant_subscription.is_deleted IS '逻辑删除';

-- =============================================================================
-- 2. 计费与配额域 (Billing & Quota)
-- =============================================================================

-- 产品套餐定义表
CREATE TABLE prod_package (
    id                  BIGINT          NOT NULL,
    package_name        VARCHAR(100)    NOT NULL,
    package_code        VARCHAR(50)     NOT NULL,
    description         VARCHAR(500)    DEFAULT '',
    price               DECIMAL(10,2)   DEFAULT 0.00,
    cycle_type          SMALLINT        DEFAULT 1,
    cycle_value         INT             DEFAULT 1,
    status              SMALLINT        DEFAULT 1,
    sort_order          INT             DEFAULT 0,
    ext_config          JSONB           DEFAULT '{}'::jsonb,
    create_by           BIGINT          DEFAULT 0,
    update_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE prod_package IS '产品套餐定义表 - 定义可售卖的套餐模板';
COMMENT ON COLUMN prod_package.id IS '主键 ID';
COMMENT ON COLUMN prod_package.package_name IS '套餐名称';
COMMENT ON COLUMN prod_package.package_code IS '套餐编码';
COMMENT ON COLUMN prod_package.description IS '套餐描述';
COMMENT ON COLUMN prod_package.price IS '价格';
COMMENT ON COLUMN prod_package.cycle_type IS '周期类型 (1-天 2-月 3-年)';
COMMENT ON COLUMN prod_package.cycle_value IS '周期数值';
COMMENT ON COLUMN prod_package.status IS '状态 (1-上架 0-下架)';
COMMENT ON COLUMN prod_package.sort_order IS '排序';
COMMENT ON COLUMN prod_package.ext_config IS '扩展配置 (JSONB，存储套餐功能开关)';
COMMENT ON COLUMN prod_package.create_by IS '创建人 ID';
COMMENT ON COLUMN prod_package.update_by IS '更新人 ID';
COMMENT ON COLUMN prod_package.create_time IS '创建时间';
COMMENT ON COLUMN prod_package.update_time IS '更新时间';
COMMENT ON COLUMN prod_package.is_deleted IS '逻辑删除';

-- 套餐配额模板表
CREATE TABLE prod_package_quota (
    id                  BIGINT          NOT NULL,
    package_id          BIGINT          NOT NULL,
    resource_code       VARCHAR(50)     NOT NULL,
    resource_name       VARCHAR(100)    NOT NULL,
    quota_value         BIGINT          NOT NULL,
    unit                VARCHAR(20)     DEFAULT '',
    is_allow_overage    BOOLEAN         DEFAULT FALSE,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE prod_package_quota IS '套餐配额模板表 - 定义套餐包含的资源配额';
COMMENT ON COLUMN prod_package_quota.id IS '主键 ID';
COMMENT ON COLUMN prod_package_quota.package_id IS '关联套餐 ID';
COMMENT ON COLUMN prod_package_quota.resource_code IS '资源类型代码 (如 TOKENS, STORAGE)';
COMMENT ON COLUMN prod_package_quota.resource_name IS '资源名称';
COMMENT ON COLUMN prod_package_quota.quota_value IS '配额数值';
COMMENT ON COLUMN prod_package_quota.unit IS '单位';
COMMENT ON COLUMN prod_package_quota.is_allow_overage IS '是否允许超额使用';
COMMENT ON COLUMN prod_package_quota.create_time IS '创建时间';
COMMENT ON COLUMN prod_package_quota.is_deleted IS '逻辑删除';

-- 租户配额调整表
CREATE TABLE sys_tenant_quota_adjustment (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    resource_code       VARCHAR(50)     NOT NULL,
    adjust_value        BIGINT          NOT NULL,
    adjust_type         SMALLINT        DEFAULT 1,
    reason              VARCHAR(500)    DEFAULT '',
    effective_time      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    expire_time         TIMESTAMP       DEFAULT NULL,
    operator_id         BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_tenant_quota_adjustment IS '租户配额调整表 - 记录套餐外的配额增减 (加油包/补偿)';
COMMENT ON COLUMN sys_tenant_quota_adjustment.id IS '主键 ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.resource_code IS '资源类型代码';
COMMENT ON COLUMN sys_tenant_quota_adjustment.adjust_value IS '调整数值 (正增负减)';
COMMENT ON COLUMN sys_tenant_quota_adjustment.adjust_type IS '调整类型 (1-购买加油包 2-补偿 3-惩罚)';
COMMENT ON COLUMN sys_tenant_quota_adjustment.reason IS '调整原因';
COMMENT ON COLUMN sys_tenant_quota_adjustment.effective_time IS '生效时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.expire_time IS '过期时间 (NULL 为永久)';
COMMENT ON COLUMN sys_tenant_quota_adjustment.operator_id IS '操作人 ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.is_deleted IS '逻辑删除';

-- 资源使用计量表
CREATE TABLE sys_resource_usage (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    resource_code       VARCHAR(50)     NOT NULL,
    usage_value         BIGINT          NOT NULL,
    business_type       VARCHAR(50)     DEFAULT '',
    business_id         BIGINT          DEFAULT 0,
    usage_time          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    remark              VARCHAR(500)    DEFAULT '',
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_resource_usage IS '资源使用计量表 - 记录租户资源消耗流水';
COMMENT ON COLUMN sys_resource_usage.id IS '主键 ID';
COMMENT ON COLUMN sys_resource_usage.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_resource_usage.resource_code IS '资源类型代码';
COMMENT ON COLUMN sys_resource_usage.usage_value IS '消耗数值';
COMMENT ON COLUMN sys_resource_usage.business_type IS '业务类型';
COMMENT ON COLUMN sys_resource_usage.business_id IS '关联业务 ID';
COMMENT ON COLUMN sys_resource_usage.usage_time IS '消耗时间';
COMMENT ON COLUMN sys_resource_usage.remark IS '备注';

-- 订单表
CREATE TABLE bill_order (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    order_no            VARCHAR(64)     NOT NULL,
    product_type        SMALLINT        DEFAULT 1,
    product_id          BIGINT          DEFAULT 0,
    total_amount        DECIMAL(10,2)   NOT NULL,
    pay_amount          DECIMAL(10,2)   DEFAULT 0.00,
    status              SMALLINT        DEFAULT 0,
    pay_time            TIMESTAMP       DEFAULT NULL,
    pay_channel         VARCHAR(50)     DEFAULT '',
    order_items         JSONB           DEFAULT '[]'::jsonb,
    ext_attributes      JSONB           DEFAULT '{}'::jsonb,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE bill_order IS '订单表 - 记录租户购买订单';
COMMENT ON COLUMN bill_order.id IS '主键 ID';
COMMENT ON COLUMN bill_order.tenant_id IS '租户 ID';
COMMENT ON COLUMN bill_order.order_no IS '订单号';
COMMENT ON COLUMN bill_order.product_type IS '产品类型 (1-套餐 2-配额包)';
COMMENT ON COLUMN bill_order.product_id IS '产品 ID';
COMMENT ON COLUMN bill_order.total_amount IS '订单总金额';
COMMENT ON COLUMN bill_order.pay_amount IS '实际支付金额';
COMMENT ON COLUMN bill_order.status IS '订单状态 (0-未付 1-已付 2-取消)';
COMMENT ON COLUMN bill_order.pay_time IS '支付时间';
COMMENT ON COLUMN bill_order.pay_channel IS '支付渠道';
COMMENT ON COLUMN bill_order.order_items IS '订单明细 (JSONB，商品/服务列表)';
COMMENT ON COLUMN bill_order.ext_attributes IS '行业扩展属性 (JSONB，存储行业特有订单字段)';
COMMENT ON COLUMN bill_order.create_by IS '创建人 ID';
COMMENT ON COLUMN bill_order.create_time IS '创建时间';
COMMENT ON COLUMN bill_order.update_time IS '更新时间';
COMMENT ON COLUMN bill_order.is_deleted IS '逻辑删除';

-- 发票管理表
CREATE TABLE bill_invoice (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    order_id            BIGINT          NOT NULL,
    invoice_no          VARCHAR(64)     DEFAULT '',
    invoice_type        SMALLINT        DEFAULT 1,
    invoice_title       VARCHAR(200)    NOT NULL,
    tax_id              VARCHAR(50)     NOT NULL,
    amount              DECIMAL(10,2)   NOT NULL,
    status              SMALLINT        DEFAULT 0,
    invoice_url         VARCHAR(500)    DEFAULT '',
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE bill_invoice IS '发票管理表 - 记录租户发票申请与开具';
COMMENT ON COLUMN bill_invoice.id IS '主键 ID';
COMMENT ON COLUMN bill_invoice.tenant_id IS '租户 ID';
COMMENT ON COLUMN bill_invoice.order_id IS '关联订单 ID';
COMMENT ON COLUMN bill_invoice.invoice_no IS '发票号码';
COMMENT ON COLUMN bill_invoice.invoice_type IS '发票类型 (1-普票 2-专票 3-电子发票)';
COMMENT ON COLUMN bill_invoice.invoice_title IS '发票抬头';
COMMENT ON COLUMN bill_invoice.tax_id IS '税号';
COMMENT ON COLUMN bill_invoice.amount IS '发票金额';
COMMENT ON COLUMN bill_invoice.status IS '状态 (0-待开具 1-已开具 2-已邮寄 3-已作废)';
COMMENT ON COLUMN bill_invoice.invoice_url IS '发票文件 URL';
COMMENT ON COLUMN bill_invoice.create_by IS '创建人 ID';
COMMENT ON COLUMN bill_invoice.create_time IS '创建时间';
COMMENT ON COLUMN bill_invoice.update_time IS '更新时间';
COMMENT ON COLUMN bill_invoice.is_deleted IS '逻辑删除';

-- =============================================================================
-- 3. 身份与访问控制域 (IAM & Permission)
-- =============================================================================

-- 用户基础表
CREATE TABLE sys_user (
    id                  BIGINT          NOT NULL,
    username            VARCHAR(50)     NOT NULL,
    password            VARCHAR(100)    NOT NULL,
    nickname            VARCHAR(50)     DEFAULT '',
    email               VARCHAR(100)    DEFAULT '',
    phone               VARCHAR(20)     DEFAULT '',
    avatar              VARCHAR(255)    DEFAULT '',
    status              SMALLINT        DEFAULT 1,
    login_ip            VARCHAR(50)     DEFAULT '',
    login_date          TIMESTAMP       DEFAULT NULL,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user IS '用户基础表 - 存储全局用户信息 (不区分租户)';
COMMENT ON COLUMN sys_user.id IS '主键 ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '加密密码';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.status IS '全局状态 (1-正常 0-禁用)';
COMMENT ON COLUMN sys_user.login_ip IS '最后登录 IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登录时间';
COMMENT ON COLUMN sys_user.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_user.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_user.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除';

-- 用户 - 租户关联表
CREATE TABLE sys_user_tenant_rel (
    id                  BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    dept_id             BIGINT          DEFAULT 0,
    is_admin            BOOLEAN         DEFAULT FALSE,
    join_time           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_default          SMALLINT        DEFAULT 0,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_tenant_rel IS '用户 - 租户关联表 - 实现用户与多租户绑定';
COMMENT ON COLUMN sys_user_tenant_rel.id IS '主键 ID';
COMMENT ON COLUMN sys_user_tenant_rel.user_id IS '用户 ID';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_user_tenant_rel.dept_id IS '主部门 ID (必须属于当前租户)';
COMMENT ON COLUMN sys_user_tenant_rel.is_admin IS '是否租户管理员(解决一个人在 A 公司是管理员、在 B 公司是普通员工，is_admin不等于角色)';
COMMENT ON COLUMN sys_user_tenant_rel.join_time IS '加入时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_default IS '是否默认租户 (解决一个用户加入多个租户，默认一个租户)';
COMMENT ON COLUMN sys_user_tenant_rel.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_tenant_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_tenant_rel.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_tenant_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_deleted IS '逻辑删除';

-- 角色表 已改
CREATE TABLE sys_role (
    id                  BIGINT          NOT NULL,
    role_name           VARCHAR(50)     NOT NULL,
    role_code           VARCHAR(50)     NOT NULL,
    role_level          SMALLINT        DEFAULT 2,
    tenant_id           BIGINT          DEFAULT 0,
    tenant_name         VARCHAR(100)     NOT NULL,
    data_scope          SMALLINT        DEFAULT 1,
    status              SMALLINT        DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_role IS '角色表 - 定义系统/租户/用户级角色';
COMMENT ON COLUMN sys_role.id IS '主键 ID';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.role_level IS '角色层级 (1-系统 2-租户 3-用户)';
COMMENT ON COLUMN sys_role.tenant_id IS '所属租户 ID (系统级为 0)';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围 (1-全部 2-本部门 3-本人 4-自定义)';
COMMENT ON COLUMN sys_role.status IS '状态 (1-正常 0-禁用)';
COMMENT ON COLUMN sys_role.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除';
COMMENT ON COLUMN sys_role.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_role.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_role.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role.update_by_name IS '更新人名称';

-- 权限/资源表 已改
CREATE TABLE sys_permission (
    id                  BIGINT          NOT NULL,
    perm_name           VARCHAR(50)     NOT NULL,
    perm_code           VARCHAR(100)    NOT NULL,
    perm_type           SMALLINT        DEFAULT 1,
    parent_id           BIGINT          DEFAULT 0,
    parent_name         VARCHAR(50)     NOT NULL,
    path                VARCHAR(200)    DEFAULT '',
    status              SMALLINT        DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_permission IS '权限/资源表 - 定义系统所有可授权资源';
COMMENT ON COLUMN sys_permission.id IS '主键 ID';
COMMENT ON COLUMN sys_permission.perm_name IS '权限名称';
COMMENT ON COLUMN sys_permission.perm_code IS '权限标识 (如 system:user:add)';
COMMENT ON COLUMN sys_permission.perm_type IS '类型 (1-菜单 2-按钮 3-接口 4-数据字段)';
COMMENT ON COLUMN sys_permission.parent_id IS '父权限 ID';
COMMENT ON COLUMN sys_permission.parent_name IS '父权限名称';
COMMENT ON COLUMN sys_permission.path IS '资源路径';
COMMENT ON COLUMN sys_permission.status IS '状态 (1-正常 0-禁用)';
COMMENT ON COLUMN sys_permission.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_permission.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_permission.create_time IS '创建时间';
COMMENT ON COLUMN sys_permission.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_permission.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_permission.update_time IS '更新时间';
COMMENT ON COLUMN sys_permission.is_deleted IS '逻辑删除';

-- 权限策略控制表 已改
CREATE TABLE sys_permission_policy (
    id                  BIGINT          NOT NULL,
    target_type         SMALLINT        NOT NULL,
    target_id           BIGINT          NOT NULL,
    target_name         VARCHAR(100)     NOT NULL,
    permission_id       BIGINT          NOT NULL,
    perm_name           VARCHAR(50)     NOT NULL,
    action              SMALLINT        NOT NULL,
    priority            INT             DEFAULT 0,
    inheritance_enabled BOOLEAN         DEFAULT TRUE,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_permission_policy IS '权限策略控制表 - 实现四层权限及禁用继承逻辑';
COMMENT ON COLUMN sys_permission_policy.id IS '主键 ID';
COMMENT ON COLUMN sys_permission_policy.target_type IS '目标类型 (1-系统 2-租户 3-角色 4-用户)';
COMMENT ON COLUMN sys_permission_policy.target_id IS '目标 ID (对应租户/角色/用户 ID)';
COMMENT ON COLUMN sys_permission_policy.target_name IS '目标名称 (对应租户/角色/用户名称)';
COMMENT ON COLUMN sys_permission_policy.permission_id IS '关联权限 ID';
COMMENT ON COLUMN sys_permission_policy.perm_name IS '关联权限名称';
COMMENT ON COLUMN sys_permission_policy.action IS '动作 (1-允许 2-拒绝)';
COMMENT ON COLUMN sys_permission_policy.priority IS '优先级 (数字越大优先级越高)';
COMMENT ON COLUMN sys_permission_policy.inheritance_enabled IS '是否向下继承';
COMMENT ON COLUMN sys_permission_policy.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_permission_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_permission_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_permission_policy.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_permission_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_permission_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_permission_policy.is_deleted IS '逻辑删除';

-- 用户角色关联表
CREATE TABLE sys_user_role_rel (
    id                  BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    role_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    create_by           BIGINT          DEFAULT 0,
    create_by_name		VARCHAR(100)    DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_by           BIGINT          DEFAULT 0,
	update_by_name		VARCHAR(100)    DEFAULT NULL,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_role_rel IS '用户角色关联表 - 用户与角色的绑定关系';
COMMENT ON COLUMN sys_user_role_rel.id IS '主键 ID';
COMMENT ON COLUMN sys_user_role_rel.user_id IS '用户 ID';
COMMENT ON COLUMN sys_user_role_rel.role_id IS '角色 ID';
COMMENT ON COLUMN sys_user_role_rel.tenant_id IS '租户 ID (角色必须属于此租户)';
COMMENT ON COLUMN sys_user_role_rel.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_user_role_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_role_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role_rel.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_user_role_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_role_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_role_rel.is_deleted IS '逻辑删除';

-- 用户 Token 记录表
CREATE TABLE sys_user_token (
    id                  BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    user_name           VARCHAR(50)     NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    tenant_name         VARCHAR(50)     NOT NULL,
    token               VARCHAR(255)    NOT NULL,
    device_info         VARCHAR(200)    DEFAULT '',
    login_ip            VARCHAR(50)     DEFAULT '',
    login_time          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    expire_time         TIMESTAMP       NOT NULL,
    status              SMALLINT        DEFAULT 1,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_token IS '用户 Token 记录表 - 用于多端登录管理和强制下线';
COMMENT ON COLUMN sys_user_token.id IS '主键 ID';
COMMENT ON COLUMN sys_user_token.user_id IS '用户 ID';
COMMENT ON COLUMN sys_user_token.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_token.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_user_token.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_token.token IS '登录令牌';
COMMENT ON COLUMN sys_user_token.device_info IS '设备信息';
COMMENT ON COLUMN sys_user_token.login_ip IS '最后登录 IP';
COMMENT ON COLUMN sys_user_token.login_time IS '登录时间';
COMMENT ON COLUMN sys_user_token.expire_time IS '过期时间';
COMMENT ON COLUMN sys_user_token.status IS '状态 (1-有效 0-失效)';
COMMENT ON COLUMN sys_user_token.is_deleted IS '逻辑删除';

-- =============================================================================
-- 4. 组织架构域 (Organization)
-- =============================================================================

-- 部门表
CREATE TABLE sys_dept (
    id                  BIGINT          NOT NULL,
    dept_name           VARCHAR(50)     NOT NULL,
    parent_id           BIGINT          DEFAULT 0,
    ancestors           VARCHAR(500)    DEFAULT '',
    tenant_id           BIGINT          NOT NULL,
    leader_id           BIGINT          DEFAULT 0,
    phone               VARCHAR(20)     DEFAULT '',
    sort_order          INT             DEFAULT 0,
    status              SMALLINT        DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_dept IS '部门表 - 存储租户组织架构，支持无限层级';
COMMENT ON COLUMN sys_dept.id IS '主键 ID';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.parent_id IS '父部门 ID';
COMMENT ON COLUMN sys_dept.ancestors IS '祖级列表 (物化路径)';
COMMENT ON COLUMN sys_dept.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_dept.leader_id IS '部门负责人 ID';
COMMENT ON COLUMN sys_dept.phone IS '部门电话';
COMMENT ON COLUMN sys_dept.sort_order IS '排序';
COMMENT ON COLUMN sys_dept.status IS '状态 (1-正常 0-停用)';
COMMENT ON COLUMN sys_dept.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_dept.update_time IS '更新时间';
COMMENT ON COLUMN sys_dept.is_deleted IS '逻辑删除';

-- 岗位表
CREATE TABLE sys_post (
    id                  BIGINT          NOT NULL,
    post_name           VARCHAR(50)     NOT NULL,
    post_code           VARCHAR(50)     NOT NULL,
    dept_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    sort_order          INT             DEFAULT 0,
    status              SMALLINT        DEFAULT 1,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_post IS '岗位表 - 部门下的具体职位';
COMMENT ON COLUMN sys_post.id IS '主键 ID';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN sys_post.dept_id IS '所属部门 ID';
COMMENT ON COLUMN sys_post.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_post.sort_order IS '排序';
COMMENT ON COLUMN sys_post.status IS '状态 (1-正常 0-停用)';
COMMENT ON COLUMN sys_post.create_time IS '创建时间';
COMMENT ON COLUMN sys_post.is_deleted IS '逻辑删除';

-- 用户组表
CREATE TABLE sys_user_group (
    id                  BIGINT          NOT NULL,
    group_name          VARCHAR(50)     NOT NULL,
    group_type          SMALLINT        DEFAULT 1,
    tenant_id           BIGINT          NOT NULL,
    description         VARCHAR(200)    DEFAULT '',
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_group IS '用户组表 - 虚拟组，用于跨部门权限分配';
COMMENT ON COLUMN sys_user_group.id IS '主键 ID';
COMMENT ON COLUMN sys_user_group.group_name IS '组名称';
COMMENT ON COLUMN sys_user_group.group_type IS '组类型 (1-静态 2-动态)';
COMMENT ON COLUMN sys_user_group.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_user_group.description IS '描述';
COMMENT ON COLUMN sys_user_group.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_user_group.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_group.is_deleted IS '逻辑删除';

-- 用户组成员表
CREATE TABLE sys_user_group_rel (
    id                  BIGINT          NOT NULL,
    group_id            BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_group_rel IS '用户组成员表 - 用户与用户组的关联';
COMMENT ON COLUMN sys_user_group_rel.id IS '主键 ID';
COMMENT ON COLUMN sys_user_group_rel.group_id IS '用户组 ID';
COMMENT ON COLUMN sys_user_group_rel.user_id IS '用户 ID';
COMMENT ON COLUMN sys_user_group_rel.create_time IS '创建时间';

-- 角色数据权限关联表（本质上是当角色的数据范围 = 自定义部门时，能看哪些部门的数据）
CREATE TABLE sys_role_dept_rel (
    id                  BIGINT          NOT NULL,
    role_id             BIGINT          NOT NULL,
    dept_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_role_dept_rel IS '角色数据权限关联表 - 角色自定义数据范围时关联的部门';
COMMENT ON COLUMN sys_role_dept_rel.id IS '主键 ID';
COMMENT ON COLUMN sys_role_dept_rel.role_id IS '角色 ID';
COMMENT ON COLUMN sys_role_dept_rel.dept_id IS '部门 ID（针对数据可见度为自定义时解决如何知道该用户能看哪些部门）';
COMMENT ON COLUMN sys_role_dept_rel.tenant_id IS '租户 ID（这里解决用户每次登录，只能选择一个租户进入，进入租户 A，就只能看到租户 A 的数据，进入租户 B，就只能看到租户 B 的数据，绝对不允许在租户 A 里看到租户 B 的部门 / 角色 / 权限）';
COMMENT ON COLUMN sys_role_dept_rel.create_time IS '创建时间';

-- =============================================================================
-- 5. 动态配置域 (Dynamic Configuration)
-- =============================================================================

-- 动态表单配置表
CREATE TABLE sys_form_config (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    biz_type            VARCHAR(50)     NOT NULL,
    form_name           VARCHAR(100)    NOT NULL,
    form_schema         JSONB           NOT NULL,
    status              SMALLINT        DEFAULT 1,
    version             INT             DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_form_config IS '动态表单配置表 - 实现不同行业表单字段动态配置';
COMMENT ON COLUMN sys_form_config.id IS '主键 ID';
COMMENT ON COLUMN sys_form_config.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_form_config.biz_type IS '业务类型 (如：education_order, restaurant_order)';
COMMENT ON COLUMN sys_form_config.form_name IS '表单名称';
COMMENT ON COLUMN sys_form_config.form_schema IS '表单结构配置 (JSONB，定义字段类型/校验/选项)';
COMMENT ON COLUMN sys_form_config.status IS '状态 (1-启用 0-停用)';
COMMENT ON COLUMN sys_form_config.version IS '版本号 (支持表单版本管理)';
COMMENT ON COLUMN sys_form_config.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_form_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_form_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_form_config.is_deleted IS '逻辑删除';

-- 动态数据源配置表
CREATE TABLE sys_datasource_config (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    datasource_code     VARCHAR(50)     NOT NULL,
    datasource_name     VARCHAR(100)    NOT NULL,
    datasource_type     SMALLINT        NOT NULL,
    datasource_config   JSONB           NOT NULL,
    cache_enabled       BOOLEAN         DEFAULT TRUE,
    cache_expire        INT             DEFAULT 300,
    status              SMALLINT        DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_datasource_config IS '动态数据源配置表 - 实现下拉框数据来源动态配置';
COMMENT ON COLUMN sys_datasource_config.id IS '主键 ID';
COMMENT ON COLUMN sys_datasource_config.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_datasource_config.datasource_code IS '数据源编码 (如：dish_list, part_list)';
COMMENT ON COLUMN sys_datasource_config.datasource_name IS '数据源名称';
COMMENT ON COLUMN sys_datasource_config.datasource_type IS '数据源类型 (1-业务表 2-API 接口 3-字典 4-SQL 查询)';
COMMENT ON COLUMN sys_datasource_config.datasource_config IS '数据源配置 (JSONB，定义表名/字段/条件)';
COMMENT ON COLUMN sys_datasource_config.cache_enabled IS '是否启用缓存';
COMMENT ON COLUMN sys_datasource_config.cache_expire IS '缓存过期时间 (秒)';
COMMENT ON COLUMN sys_datasource_config.status IS '状态 (1-启用 0-停用)';
COMMENT ON COLUMN sys_datasource_config.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_datasource_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_datasource_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_datasource_config.is_deleted IS '逻辑删除';

-- 打印模板配置表
CREATE TABLE sys_print_template (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    template_code       VARCHAR(50)     NOT NULL,
    template_name       VARCHAR(100)    NOT NULL,
    biz_type            VARCHAR(50)     NOT NULL,
    template_type       SMALLINT        NOT NULL,
    template_content    TEXT            NOT NULL,
    template_config     JSONB           DEFAULT '{}'::jsonb,
    paper_size          VARCHAR(20)     DEFAULT 'A4',
    orientation         VARCHAR(10)     DEFAULT 'portrait',
    status              SMALLINT        DEFAULT 1,
    is_default          BOOLEAN         DEFAULT FALSE,
    version             INT             DEFAULT 1,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_print_template IS '打印模板配置表 - 实现不同行业打印/PDF 模板动态配置';
COMMENT ON COLUMN sys_print_template.id IS '主键 ID';
COMMENT ON COLUMN sys_print_template.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_print_template.template_code IS '模板编码 (如：education_contract, restaurant_receipt)';
COMMENT ON COLUMN sys_print_template.template_name IS '模板名称';
COMMENT ON COLUMN sys_print_template.biz_type IS '业务类型';
COMMENT ON COLUMN sys_print_template.template_type IS '模板类型 (1-HTML 2-Markdown 3-JSON 配置)';
COMMENT ON COLUMN sys_print_template.template_content IS '模板内容 (HTML/模板引擎语法)';
COMMENT ON COLUMN sys_print_template.template_config IS '模板配置 (JSONB，定义变量/条件显示)';
COMMENT ON COLUMN sys_print_template.paper_size IS '纸张大小 (A4/A5/80mm 热敏纸等)';
COMMENT ON COLUMN sys_print_template.orientation IS '纸张方向 (portrait/landscape)';
COMMENT ON COLUMN sys_print_template.status IS '状态 (1-启用 0-停用)';
COMMENT ON COLUMN sys_print_template.is_default IS '是否默认模板';
COMMENT ON COLUMN sys_print_template.version IS '版本号';
COMMENT ON COLUMN sys_print_template.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_print_template.create_time IS '创建时间';
COMMENT ON COLUMN sys_print_template.update_time IS '更新时间';
COMMENT ON COLUMN sys_print_template.is_deleted IS '逻辑删除';

-- =============================================================================
-- 6. 系统支撑域 (System Support)
-- =============================================================================

-- 菜单表
CREATE TABLE sys_menu (
    id                  BIGINT          NOT NULL,
    menu_name           VARCHAR(50)     NOT NULL,
    menu_type           SMALLINT        DEFAULT 1,
    parent_id           BIGINT          DEFAULT 0,
    path                VARCHAR(200)    DEFAULT '',
    component           VARCHAR(200)    DEFAULT '',
    perm_code           VARCHAR(100)    DEFAULT '',
    visible             BOOLEAN         DEFAULT TRUE,
    status              SMALLINT        DEFAULT 1,
    sort_order          INT             DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_menu IS '菜单表 - 前端导航与按钮权限映射';
COMMENT ON COLUMN sys_menu.id IS '主键 ID';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.menu_type IS '类型 (1-目录 2-菜单 3-按钮)';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单 ID';
COMMENT ON COLUMN sys_menu.path IS '路由地址';
COMMENT ON COLUMN sys_menu.component IS '组件路径';
COMMENT ON COLUMN sys_menu.perm_code IS '权限标识';
COMMENT ON COLUMN sys_menu.visible IS '是否可见';
COMMENT ON COLUMN sys_menu.status IS '状态 (1-正常 0-停用)';
COMMENT ON COLUMN sys_menu.sort_order IS '排序';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.is_deleted IS '逻辑删除';

-- 字典类型表
CREATE TABLE sys_dict (
    id                  BIGINT          NOT NULL,
    dict_name           VARCHAR(100)    NOT NULL,
    dict_type           VARCHAR(100)    NOT NULL,
    tenant_id           BIGINT          DEFAULT 0,
    status              SMALLINT        DEFAULT 1,
    remark              VARCHAR(500)    DEFAULT '',
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_dict IS '字典类型表 - 定义系统常量类型';
COMMENT ON COLUMN sys_dict.id IS '主键 ID';
COMMENT ON COLUMN sys_dict.dict_name IS '字典名称';
COMMENT ON COLUMN sys_dict.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict.tenant_id IS '所属租户 (0 为系统字典)';
COMMENT ON COLUMN sys_dict.status IS '状态 (1-正常 0-停用)';
COMMENT ON COLUMN sys_dict.remark IS '备注';
COMMENT ON COLUMN sys_dict.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict.is_deleted IS '逻辑删除';

-- 字典数据表
CREATE TABLE sys_dict_item (
    id                  BIGINT          NOT NULL,
    dict_id             BIGINT          NOT NULL,
    dict_label          VARCHAR(100)    NOT NULL,
    dict_value          VARCHAR(100)    NOT NULL,
    sort_order          INT             DEFAULT 0,
    status              SMALLINT        DEFAULT 1,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_dict_item IS '字典数据表 - 字典具体键值对';
COMMENT ON COLUMN sys_dict_item.id IS '主键 ID';
COMMENT ON COLUMN sys_dict_item.dict_id IS '关联字典类型 ID';
COMMENT ON COLUMN sys_dict_item.dict_label IS '字典标签';
COMMENT ON COLUMN sys_dict_item.dict_value IS '字典值';
COMMENT ON COLUMN sys_dict_item.sort_order IS '排序';
COMMENT ON COLUMN sys_dict_item.status IS '状态 (1-正常 0-停用)';
COMMENT ON COLUMN sys_dict_item.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_item.is_deleted IS '逻辑删除';

-- 文件资源表
CREATE TABLE sys_file (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    tenant_name         VARCHAR(150)    NOT NULL,
    file_name           VARCHAR(255)    NOT NULL,
    original_name       VARCHAR(255)    NOT NULL,
    file_path           VARCHAR(500)    NOT NULL,
    file_url            VARCHAR(500)    NOT NULL,
    file_size           BIGINT          NOT NULL,
    file_type           VARCHAR(50)     NOT NULL,
    mime_type           VARCHAR(50)     NOT NULL,
    biz_type            VARCHAR(50)     NOT NULL,
    upload_by           BIGINT          NOT NULL,
    upload_name         VARCHAR(50)     NOT NULL,
    upload_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_file IS '文件资源表 - 存储上传的文件信息';
COMMENT ON COLUMN sys_file.id IS '主键 ID';
COMMENT ON COLUMN sys_file.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_file.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_file.file_name IS '文件名称';
COMMENT ON COLUMN sys_file.original_name IS '原始文件名';
COMMENT ON COLUMN sys_file.file_path IS '文件存储路径';
COMMENT ON COLUMN sys_file.file_url IS '文件访问 URL';
COMMENT ON COLUMN sys_file.file_size IS '文件大小 (字节)';
COMMENT ON COLUMN sys_file.file_type IS '文件类型';
COMMENT ON COLUMN sys_file.mime_type IS 'MIME类型';
COMMENT ON COLUMN sys_file.biz_type IS '业务类型分类';
COMMENT ON COLUMN sys_file.upload_by IS '上传人ID';
COMMENT ON COLUMN sys_file.upload_name IS '上传人名称';
COMMENT ON COLUMN sys_file.upload_time IS '上传时间';
COMMENT ON COLUMN sys_file.is_deleted IS '逻辑删除';

-- 通知公告表
CREATE TABLE sys_notice (
    id                  BIGINT          NOT NULL,
    notice_title        VARCHAR(100)    NOT NULL,
    notice_type         SMALLINT        DEFAULT 1,
    notice_content      TEXT            DEFAULT NULL,
    status              SMALLINT        DEFAULT 1,
    target_type         SMALLINT        DEFAULT 1,
    target_ids          VARCHAR(1000)   DEFAULT '',
    tenant_id           BIGINT          DEFAULT 0,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_notice IS '通知公告表 - 系统公告与站内信';
COMMENT ON COLUMN sys_notice.id IS '主键 ID';
COMMENT ON COLUMN sys_notice.notice_title IS '公告标题';
COMMENT ON COLUMN sys_notice.notice_type IS '类型 (1-公告 2-通知)';
COMMENT ON COLUMN sys_notice.notice_content IS '公告内容';
COMMENT ON COLUMN sys_notice.status IS '状态 (1-发布 0-下架)';
COMMENT ON COLUMN sys_notice.target_type IS '目标类型 (1-全员 2-指定租户 3-指定用户)';
COMMENT ON COLUMN sys_notice.target_ids IS '目标 ID 集合 (逗号分隔)';
COMMENT ON COLUMN sys_notice.tenant_id IS '所属租户 (0 为系统公告)';
COMMENT ON COLUMN sys_notice.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_notice.create_time IS '创建时间';
COMMENT ON COLUMN sys_notice.is_deleted IS '逻辑删除';

-- 用户公告阅读状态表
CREATE TABLE sys_notice_user_rel (
    id                  BIGINT          NOT NULL,
    notice_id           BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    read_status         SMALLINT        DEFAULT 0,
    read_time           TIMESTAMP       DEFAULT NULL,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_notice_user_rel IS '用户公告阅读状态表 - 记录用户公告已读/未读状态';
COMMENT ON COLUMN sys_notice_user_rel.id IS '主键 ID';
COMMENT ON COLUMN sys_notice_user_rel.notice_id IS '公告 ID';
COMMENT ON COLUMN sys_notice_user_rel.user_id IS '用户 ID';
COMMENT ON COLUMN sys_notice_user_rel.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_notice_user_rel.read_status IS '阅读状态 (0-未读 1-已读)';
COMMENT ON COLUMN sys_notice_user_rel.read_time IS '阅读时间';
COMMENT ON COLUMN sys_notice_user_rel.create_time IS '创建时间';

-- 操作日志表
CREATE TABLE sys_oper_log (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          DEFAULT 0,
    module              VARCHAR(50)     DEFAULT '',
    business_type       SMALLINT        DEFAULT 0,
    method              VARCHAR(100)    DEFAULT '',
    request_method      VARCHAR(10)     DEFAULT '',
    operator_name       VARCHAR(50)     DEFAULT '',
    operator_id         BIGINT          DEFAULT 0,
    dept_name           VARCHAR(50)     DEFAULT '',
    oper_url            VARCHAR(255)    DEFAULT '',
    oper_ip             VARCHAR(128)    DEFAULT '',
    oper_location       VARCHAR(255)    DEFAULT '',
    oper_param          VARCHAR(2000)   DEFAULT '',
    json_result         VARCHAR(2000)   DEFAULT '',
    status              SMALLINT        DEFAULT 1,
    error_msg           VARCHAR(2000)   DEFAULT '',
    oper_time           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_oper_log IS '操作日志表 - 审计用户操作行为';
COMMENT ON COLUMN sys_oper_log.id IS '主键 ID';
COMMENT ON COLUMN sys_oper_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_oper_log.module IS '操作模块';
COMMENT ON COLUMN sys_oper_log.business_type IS '业务类型';
COMMENT ON COLUMN sys_oper_log.method IS '请求方法';
COMMENT ON COLUMN sys_oper_log.request_method IS '请求方式 (GET/POST)';
COMMENT ON COLUMN sys_oper_log.operator_name IS '操作人员姓名';
COMMENT ON COLUMN sys_oper_log.operator_id IS '操作人员 ID';
COMMENT ON COLUMN sys_oper_log.dept_name IS '部门名称';
COMMENT ON COLUMN sys_oper_log.oper_url IS '请求 URL';
COMMENT ON COLUMN sys_oper_log.oper_ip IS '操作 IP';
COMMENT ON COLUMN sys_oper_log.oper_location IS '操作地点';
COMMENT ON COLUMN sys_oper_log.oper_param IS '请求参数';
COMMENT ON COLUMN sys_oper_log.json_result IS '返回结果';
COMMENT ON COLUMN sys_oper_log.status IS '操作状态 (1-正常 0-失败)';
COMMENT ON COLUMN sys_oper_log.error_msg IS '错误消息';
COMMENT ON COLUMN sys_oper_log.oper_time IS '操作时间';

-- 登录日志表
CREATE TABLE sys_login_log (
    id                  BIGINT          NOT NULL,
    user_id             BIGINT          DEFAULT 0,
    username            VARCHAR(50)     DEFAULT '',
    tenant_id           BIGINT          DEFAULT 0,
    ip_address          VARCHAR(128)    DEFAULT '',
    login_location      VARCHAR(255)    DEFAULT '',
    browser             VARCHAR(50)     DEFAULT '',
    os                  VARCHAR(50)     DEFAULT '',
    status              SMALLINT        DEFAULT 1,
    msg                 VARCHAR(255)    DEFAULT '',
    login_time          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_login_log IS '登录日志表 - 记录用户登录信息';
COMMENT ON COLUMN sys_login_log.id IS '主键 ID';
COMMENT ON COLUMN sys_login_log.user_id IS '用户 ID';
COMMENT ON COLUMN sys_login_log.username IS '用户名';
COMMENT ON COLUMN sys_login_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_login_log.ip_address IS '登录 IP';
COMMENT ON COLUMN sys_login_log.login_location IS '登录地点';
COMMENT ON COLUMN sys_login_log.browser IS '浏览器';
COMMENT ON COLUMN sys_login_log.os IS '操作系统';
COMMENT ON COLUMN sys_login_log.status IS '状态 (1-成功 0-失败)';
COMMENT ON COLUMN sys_login_log.msg IS '提示消息';
COMMENT ON COLUMN sys_login_log.login_time IS '登录时间';

-- 数据变更审计表
CREATE TABLE sys_data_audit_log (
    id                  BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    table_name          VARCHAR(50)     NOT NULL,
    record_id           BIGINT          NOT NULL,
    operator_id         BIGINT          NOT NULL,
    operate_type        SMALLINT        DEFAULT 1,
    old_value           JSONB           DEFAULT '{}'::jsonb,
    new_value           JSONB           DEFAULT '{}'::jsonb,
    operate_time        TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_data_audit_log IS '数据变更审计表 - 记录数据字段变更详情';
COMMENT ON COLUMN sys_data_audit_log.id IS '主键 ID';
COMMENT ON COLUMN sys_data_audit_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_data_audit_log.table_name IS '表名';
COMMENT ON COLUMN sys_data_audit_log.record_id IS '记录 ID';
COMMENT ON COLUMN sys_data_audit_log.operator_id IS '操作人 ID';
COMMENT ON COLUMN sys_data_audit_log.operate_type IS '操作类型 (1-更新 2-删除)';
COMMENT ON COLUMN sys_data_audit_log.old_value IS '修改前数据 (JSONB)';
COMMENT ON COLUMN sys_data_audit_log.new_value IS '修改后数据 (JSONB)';
COMMENT ON COLUMN sys_data_audit_log.operate_time IS '操作时间';

-- =============================================================================
-- 7. 消息通知中心 (Notification Center) - 精简版
-- =============================================================================

-- 消息模板表
CREATE TABLE sys_message_template (
    id                  BIGINT          NOT NULL,
    template_code       VARCHAR(100)    NOT NULL,
    template_name       VARCHAR(100)    NOT NULL,
    tenant_id           BIGINT          DEFAULT 0,
    biz_type            VARCHAR(50)     DEFAULT '',
    message_type        SMALLINT        DEFAULT 1,
    template_title      VARCHAR(200)    DEFAULT '',
    template_content    TEXT            NOT NULL,
    template_example    JSONB           DEFAULT '{}'::jsonb,
    variables           JSONB           DEFAULT '[]'::jsonb,
    status              SMALLINT        DEFAULT 1,
    version             INT             DEFAULT 1,
    language            VARCHAR(20)     DEFAULT 'zh-CN',
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_message_template IS '消息模板表 - 定义各类消息的内容模板';
COMMENT ON COLUMN sys_message_template.id IS '主键 ID';
COMMENT ON COLUMN sys_message_template.template_code IS '模板编码 (如：ORDER_PAID, PACKAGE_EXPIRE_WARNING)';
COMMENT ON COLUMN sys_message_template.template_name IS '模板名称';
COMMENT ON COLUMN sys_message_template.tenant_id IS '所属租户 ID (0 为系统模板)';
COMMENT ON COLUMN sys_message_template.biz_type IS '业务类型 (如：order, subscription, system)';
COMMENT ON COLUMN sys_message_template.message_type IS '消息类型 (1-通知 2-营销 3-验证 4-提醒)';
COMMENT ON COLUMN sys_message_template.template_title IS '模板标题';
COMMENT ON COLUMN sys_message_template.template_content IS '模板内容 (支持变量占位符，如 ${userName})';
COMMENT ON COLUMN sys_message_template.template_example IS '示例数据 (JSONB，用于测试预览)';
COMMENT ON COLUMN sys_message_template.variables IS '变量定义 (JSONB，定义变量名/类型/必填)';
COMMENT ON COLUMN sys_message_template.status IS '状态 (1-启用 0-停用)';
COMMENT ON COLUMN sys_message_template.version IS '版本号';
COMMENT ON COLUMN sys_message_template.language IS '语言 (zh-CN/en-US 等)';
COMMENT ON COLUMN sys_message_template.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_message_template.create_time IS '创建时间';
COMMENT ON COLUMN sys_message_template.update_time IS '更新时间';
COMMENT ON COLUMN sys_message_template.is_deleted IS '逻辑删除';

-- 用户消息订阅配置表
-- CREATE TABLE sys_user_subscription (
--     id                  BIGINT          NOT NULL,
--     user_id             BIGINT          NOT NULL,
--     tenant_id           BIGINT          NOT NULL,
--     subscription_type   SMALLINT        NOT NULL,
--     enabled_types       JSONB           DEFAULT '{"system":true,"billing":true,"approval":false}'::jsonb,
--     frequency           SMALLINT        DEFAULT 1,
--     quiet_start         TIME            DEFAULT '22:00:00',
--     quiet_end           TIME            DEFAULT '08:00:00',
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_user_subscription IS '用户消息订阅配置表 - 用户可自定义接收哪些类型的消息';
-- COMMENT ON COLUMN sys_user_subscription.id IS '主键 ID';
-- COMMENT ON COLUMN sys_user_subscription.user_id IS '用户 ID';
-- COMMENT ON COLUMN sys_user_subscription.tenant_id IS '租户 ID';
-- COMMENT ON COLUMN sys_user_subscription.subscription_type IS '订阅类型 (1-系统通知 2-营销消息 3-账单提醒 4-审批通知)';
-- COMMENT ON COLUMN sys_user_subscription.enabled_types IS '启用的消息类型 (JSONB)';
-- COMMENT ON COLUMN sys_user_subscription.frequency IS '发送频率 (1-实时 2-每小时汇总 3-每日汇总)';
-- COMMENT ON COLUMN sys_user_subscription.quiet_start IS '免打扰开始时间';
-- COMMENT ON COLUMN sys_user_subscription.quiet_end IS '免打扰结束时间';
-- COMMENT ON COLUMN sys_user_subscription.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_user_subscription.update_time IS '更新时间';

-- 站内信收件箱表
CREATE TABLE sys_inbox_message (
    id                  BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    tenant_id           BIGINT          NOT NULL,
    message_type        SMALLINT        DEFAULT 1,
    title               VARCHAR(200)    NOT NULL,
    content             TEXT            NOT NULL,
    priority            SMALLINT        DEFAULT 1,
    is_read             BOOLEAN         DEFAULT FALSE,
    read_time           TIMESTAMP       DEFAULT NULL,
    is_archived         BOOLEAN         DEFAULT FALSE,
    archived_time       TIMESTAMP       DEFAULT NULL,
    expire_time         TIMESTAMP       DEFAULT NULL,
    action_url          VARCHAR(500)    DEFAULT '',
    action_text         VARCHAR(50)     DEFAULT '',
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_inbox_message IS '站内信收件箱表 - 用户个人消息 inbox';
COMMENT ON COLUMN sys_inbox_message.id IS '主键 ID';
COMMENT ON COLUMN sys_inbox_message.user_id IS '用户 ID';
COMMENT ON COLUMN sys_inbox_message.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_inbox_message.message_type IS '消息类型 (1-系统通知 2-审批通知 3-账单提醒 4-营销活动)';
COMMENT ON COLUMN sys_inbox_message.title IS '消息标题';
COMMENT ON COLUMN sys_inbox_message.content IS '消息内容';
COMMENT ON COLUMN sys_inbox_message.priority IS '优先级 (1-普通 2-重要 3-紧急)';
COMMENT ON COLUMN sys_inbox_message.is_read IS '是否已读';
COMMENT ON COLUMN sys_inbox_message.read_time IS '阅读时间';
COMMENT ON COLUMN sys_inbox_message.is_archived IS '是否已归档';
COMMENT ON COLUMN sys_inbox_message.archived_time IS '归档时间';
COMMENT ON COLUMN sys_inbox_message.expire_time IS '过期时间 (NULL 为永不过期)';
COMMENT ON COLUMN sys_inbox_message.action_url IS '操作按钮 URL';
COMMENT ON COLUMN sys_inbox_message.action_text IS '操作按钮文案';
COMMENT ON COLUMN sys_inbox_message.create_time IS '创建时间';

-- 定时消息任务表
CREATE TABLE sys_message_schedule (
    id                  BIGINT          NOT NULL,
    task_name           VARCHAR(100)    NOT NULL,
    tenant_id           BIGINT          DEFAULT 0,
    template_id         BIGINT          NOT NULL,
    target_type         SMALLINT        NOT NULL,
    target_ids          VARCHAR(1000)   DEFAULT '',
    trigger_type        SMALLINT        DEFAULT 1,
    trigger_condition   JSONB           DEFAULT '{}'::jsonb,
    execute_time        TIMESTAMP       NOT NULL,
    repeat_rule         JSONB           DEFAULT '{}'::jsonb,
    status              SMALLINT        DEFAULT 0,
    executed_count      INT             DEFAULT 0,
    last_execute_time   TIMESTAMP       DEFAULT NULL,
    create_by           BIGINT          DEFAULT 0,
    create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_deleted          SMALLINT        DEFAULT 0,
    PRIMARY KEY (id)
);
COMMENT ON TABLE sys_message_schedule IS '定时消息任务表 - 预约发送或周期性发送的消息';
COMMENT ON COLUMN sys_message_schedule.id IS '主键 ID';
COMMENT ON COLUMN sys_message_schedule.task_name IS '任务名称';
COMMENT ON COLUMN sys_message_schedule.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_message_schedule.template_id IS '消息模板 ID';
COMMENT ON COLUMN sys_message_schedule.target_type IS '目标类型 (1-指定用户 2-指定租户 3-符合条件的所有用户)';
COMMENT ON COLUMN sys_message_schedule.target_ids IS '目标 ID 集合 (逗号分隔)';
COMMENT ON COLUMN sys_message_schedule.trigger_type IS '触发类型 (1-单次定时 2-周期循环 3-事件触发)';
COMMENT ON COLUMN sys_message_schedule.trigger_condition IS '触发条件 (JSONB，定义时间/事件规则)';
COMMENT ON COLUMN sys_message_schedule.execute_time IS '计划执行时间';
COMMENT ON COLUMN sys_message_schedule.repeat_rule IS '重复规则 (JSONB，如{"type":"daily","interval":1})';
COMMENT ON COLUMN sys_message_schedule.status IS '状态 (0-待执行 1-执行中 2-已完成 3-已取消)';
COMMENT ON COLUMN sys_message_schedule.executed_count IS '已执行次数';
COMMENT ON COLUMN sys_message_schedule.last_execute_time IS '最后执行时间';
COMMENT ON COLUMN sys_message_schedule.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_message_schedule.create_time IS '创建时间';
COMMENT ON COLUMN sys_message_schedule.update_time IS '更新时间';
COMMENT ON COLUMN sys_message_schedule.is_deleted IS '逻辑删除';

-- 租户表增加冗余字段
ALTER TABLE sys_tenant
ADD COLUMN parent_name VARCHAR(100) DEFAULT NULL,
ADD COLUMN package_name VARCHAR(100) DEFAULT NULL,
ADD COLUMN create_by_name VARCHAR(100) DEFAULT NULL,
ADD COLUMN update_by_name VARCHAR(100) DEFAULT NULL;

COMMENT ON COLUMN sys_tenant.parent_name IS '父租户名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.package_name IS '套餐名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.create_by_name IS '创建人名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';
COMMENT ON COLUMN sys_tenant.update_by_name IS '更新人名称(冗余字段,用于查询优化,新增、更新、删除操作需要同步该字段)';


SELECT * FROM sys_file;