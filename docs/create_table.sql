-- =============================================================================
-- 1. 核心租户域 (Tenant Core)
-- =============================================================================
-- 租户信息表
CREATE TABLE sys_tenant (
                            id VARCHAR ( 200 ) NOT NULL,
                            tenant_name VARCHAR ( 50 ) NOT NULL,
                            tenant_type VARCHAR ( 50 ) NOT NULL,
                            tenant_logo_url VARCHAR ( 200 ) DEFAULT NULL,
                            tenant_desc VARCHAR ( 200 ) DEFAULT NULL,
                            tenant_code VARCHAR ( 50 ) NOT NULL,
                            parent_id VARCHAR ( 200 ) DEFAULT NULL,
                            parent_name VARCHAR ( 50 ) DEFAULT NULL,
                            ancestors VARCHAR ( 500 ) DEFAULT NULL,
                            contact_name VARCHAR ( 50 ) DEFAULT NULL,
                            contact_phone VARCHAR ( 20 ) DEFAULT NULL,
                            STATUS VARCHAR ( 50 ) NOT NULL,
                            expire_time TIMESTAMP DEFAULT NULL,
                            package_id VARCHAR ( 200 ) DEFAULT NULL,
                            package_name VARCHAR ( 50 ) DEFAULT NULL,
                            ext_attributes JSONB DEFAULT '{}' :: jsonb,
                            has_children BOOLEAN DEFAULT FALSE,
                            create_by VARCHAR ( 200 ) DEFAULT NULL,
                            create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            update_by VARCHAR ( 200 ) DEFAULT NULL,
                            update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                            PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_tenant IS '租户信息表 - 存储租户基础信息，支持无限层级';
COMMENT ON COLUMN sys_tenant.id IS '主键ID (雪花算法)';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_type IS '租户类型';
COMMENT ON COLUMN sys_tenant.tenant_logo_url IS '租户logo';
COMMENT ON COLUMN sys_tenant.tenant_desc IS '租户描述';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户唯一编码';
COMMENT ON COLUMN sys_tenant.parent_id IS '父租户ID';
COMMENT ON COLUMN sys_tenant.ancestors IS '祖级列表 (物化路径，如 0,100,200)';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.STATUS IS '状态';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_id IS '当前主套餐ID';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性(JSONB，存储行业特定配置)';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人ID';
COMMENT ON COLUMN sys_tenant.update_by IS '更新人ID';
COMMENT ON COLUMN sys_tenant.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除';
COMMENT ON COLUMN sys_tenant.parent_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant.package_name IS '套餐名称';
COMMENT ON COLUMN sys_tenant.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant.has_children IS '是否有子租户';
-- 租户套餐订阅表
CREATE TABLE sys_tenant_subscription (
                                         id VARCHAR ( 200 ) NOT NULL,
                                         tenant_id VARCHAR ( 200 ) NOT NULL,
                                         tenant_name VARCHAR ( 100 ) NOT NULL,
                                         package_id VARCHAR ( 200 ) NOT NULL,
                                         package_name VARCHAR ( 100 ) NOT NULL,
                                         subscription_type VARCHAR ( 50 ) NOT NULL,
                                         start_time TIMESTAMP NOT NULL,
                                         end_time TIMESTAMP NOT NULL,
                                         STATUS VARCHAR ( 50 ) NOT NULL,
                                         is_auto_renew BOOLEAN DEFAULT FALSE,
                                         source_type VARCHAR ( 50 ) NOT NULL,
                                         parent_grant_id VARCHAR ( 200 ) DEFAULT NULL,
                                         parent_tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                                         create_by VARCHAR ( 200 ) DEFAULT NULL,
                                         create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                         update_by VARCHAR ( 200 ) DEFAULT NULL,
                                         update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                         PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_tenant_subscription IS '租户套餐订阅表 - 记录租户购买的套餐及订阅状态';
COMMENT ON COLUMN sys_tenant_subscription.id IS '主键ID (雪花算法)';
COMMENT ON COLUMN sys_tenant_subscription.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_tenant_subscription.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant_subscription.package_id IS '套餐产品ID';
COMMENT ON COLUMN sys_tenant_subscription.package_name IS '套餐产品名称';
COMMENT ON COLUMN sys_tenant_subscription.subscription_type IS '订阅类型';
COMMENT ON COLUMN sys_tenant_subscription.start_time IS '订阅开始时间';
COMMENT ON COLUMN sys_tenant_subscription.end_time IS '订阅结束时间';
COMMENT ON COLUMN sys_tenant_subscription.STATUS IS '状态';
COMMENT ON COLUMN sys_tenant_subscription.is_auto_renew IS '是否自动续费';
COMMENT ON COLUMN sys_tenant_subscription.source_type IS '来源类型';
COMMENT ON COLUMN sys_tenant_subscription.parent_grant_id IS '父租户分配记录 ID';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant_subscription.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_tenant_subscription.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant_subscription.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_tenant_subscription.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant_subscription.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_subscription.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant_subscription.is_deleted IS '逻辑删除';
-- =============================================================================
-- 2. 计费与配额域 (Billing & Quota)
-- =============================================================================
-- 产品套餐定义表
CREATE TABLE prod_package (
                              id VARCHAR ( 200 ) NOT NULL,
                              package_name VARCHAR ( 100 ) NOT NULL,
                              package_code VARCHAR ( 50 ) NOT NULL,
                              packe_desc VARCHAR ( 500 ) DEFAULT NULL,
                              price DECIMAL ( 10, 2 ) NOT NULL,
                              cycle_type VARCHAR ( 50 ) DEFAULT 'DAY',
                              cycle_value INT NOT NULL,
                              STATUS VARCHAR ( 50 ) NOT NULL,
                              sort_order INT DEFAULT 0,
                              ext_config JSONB DEFAULT '{}' :: jsonb,
                              create_by VARCHAR ( 200 ) DEFAULT NULL,
                              create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              update_by VARCHAR ( 200 ) DEFAULT NULL,
                              update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                              PRIMARY KEY ( id )
);
COMMENT ON TABLE prod_package IS '产品套餐定义表 - 定义可售卖的套餐模板';
COMMENT ON COLUMN prod_package.id IS '主键ID(雪花算法)';
COMMENT ON COLUMN prod_package.package_name IS '套餐名称';
COMMENT ON COLUMN prod_package.package_code IS '套餐编码';
COMMENT ON COLUMN prod_package.packe_desc IS '套餐描述';
COMMENT ON COLUMN prod_package.price IS '价格';
COMMENT ON COLUMN prod_package.cycle_type IS '周期类型';
COMMENT ON COLUMN prod_package.cycle_value IS '周期数值';
COMMENT ON COLUMN prod_package.STATUS IS '状态';
COMMENT ON COLUMN prod_package.sort_order IS '排序';
COMMENT ON COLUMN prod_package.ext_config IS '扩展配置(JSONB，存储套餐功能开关)';
COMMENT ON COLUMN prod_package.create_by IS '创建人ID';
COMMENT ON COLUMN prod_package.create_by_name IS '创建人名称';
COMMENT ON COLUMN prod_package.update_by IS '更新人ID';
COMMENT ON COLUMN prod_package.update_by_name IS '更新人名称';
COMMENT ON COLUMN prod_package.create_time IS '创建时间';
COMMENT ON COLUMN prod_package.update_time IS '更新时间';
COMMENT ON COLUMN prod_package.is_deleted IS '逻辑删除';-- 套餐配额模板表
CREATE TABLE prod_package_quota (
                                    id VARCHAR ( 200 ) NOT NULL,
                                    package_id VARCHAR ( 200 ) NOT NULL,
                                    resource_code VARCHAR ( 50 ) NOT NULL,
                                    resource_name VARCHAR ( 100 ) NOT NULL,
                                    quota_value BIGINT NOT NULL,
                                    unit VARCHAR ( 20 ) NOT NULL,
                                    is_allow_overage BOOLEAN DEFAULT FALSE,
                                    create_by VARCHAR ( 200 ) DEFAULT NULL,
                                    create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    update_by VARCHAR ( 200 ) DEFAULT NULL,
                                    update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                    PRIMARY KEY ( id )
);
COMMENT ON TABLE prod_package_quota IS '套餐配额模板表 - 定义套餐包含的资源配额';
COMMENT ON COLUMN prod_package_quota.id IS '主键ID';
COMMENT ON COLUMN prod_package_quota.package_id IS '关联套餐ID';
COMMENT ON COLUMN prod_package_quota.resource_code IS '资源类型代码';
COMMENT ON COLUMN prod_package_quota.resource_name IS '资源名称';
COMMENT ON COLUMN prod_package_quota.quota_value IS '配额数值';
COMMENT ON COLUMN prod_package_quota.unit IS '单位';
COMMENT ON COLUMN prod_package_quota.is_allow_overage IS '是否允许超额使用';
COMMENT ON COLUMN prod_package_quota.create_by IS '创建人ID';
COMMENT ON COLUMN prod_package_quota.create_by_name IS '创建人名称';
COMMENT ON COLUMN prod_package_quota.update_by IS '更新人ID';
COMMENT ON COLUMN prod_package_quota.update_by_name IS '更新人名称';
COMMENT ON COLUMN prod_package_quota.create_time IS '创建时间';
COMMENT ON COLUMN prod_package_quota.update_time IS '更新时间';
COMMENT ON COLUMN prod_package_quota.is_deleted IS '逻辑删除';
-- 租户配额调整表
CREATE TABLE sys_tenant_quota_adjustment (
                                             id VARCHAR ( 200 ) NOT NULL,
                                             tenant_id VARCHAR ( 200 ) NOT NULL,
                                             tenant_name VARCHAR ( 100 ) NOT NULL,
                                             resource_code VARCHAR ( 50 ) NOT NULL,
                                             adjust_value BIGINT NOT NULL,
                                             adjust_type VARCHAR ( 50 ) NOT NULL,
                                             reason VARCHAR ( 500 ) DEFAULT '',
                                             effective_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             expire_time TIMESTAMP DEFAULT NULL,
                                             create_by VARCHAR ( 200 ) DEFAULT NULL,
                                             create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                             update_by VARCHAR ( 200 ) DEFAULT NULL,
                                             update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                             create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                             PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_tenant_quota_adjustment IS '租户配额调整表-记录套餐外的配额增减 (加油包/补偿)';
COMMENT ON COLUMN sys_tenant_quota_adjustment.id IS '主键ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant_quota_adjustment.resource_code IS '资源类型代码';
COMMENT ON COLUMN sys_tenant_quota_adjustment.adjust_value IS '调整数值';
COMMENT ON COLUMN sys_tenant_quota_adjustment.adjust_type IS '调整类型';
COMMENT ON COLUMN sys_tenant_quota_adjustment.reason IS '调整原因';
COMMENT ON COLUMN sys_tenant_quota_adjustment.effective_time IS '生效时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.expire_time IS '过期时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.create_by IS '创建人ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant_quota_adjustment.update_by IS '更新人ID';
COMMENT ON COLUMN sys_tenant_quota_adjustment.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant_quota_adjustment.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant_quota_adjustment.is_deleted IS '逻辑删除';-- 资源使用计量表
CREATE TABLE sys_resource_usage (
                                    id VARCHAR ( 200 ) NOT NULL,
                                    tenant_id VARCHAR ( 200 ) NOT NULL,
                                    tenant_name VARCHAR ( 100 ) NOT NULL,
                                    resource_code VARCHAR ( 50 ) NOT NULL,
                                    usage_value BIGINT NOT NULL,
                                    business_type VARCHAR ( 50 ) DEFAULT '',
                                    business_id VARCHAR ( 200 ) DEFAULT NULL,
                                    usage_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    remark VARCHAR ( 500 ) DEFAULT '',
                                    create_by VARCHAR ( 200 ) DEFAULT NULL,
                                    create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    update_by VARCHAR ( 200 ) DEFAULT NULL,
                                    update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                    PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_resource_usage IS '资源使用计量表-记录租户资源消耗流水';
COMMENT ON COLUMN sys_resource_usage.id IS '主键ID';
COMMENT ON COLUMN sys_resource_usage.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_resource_usage.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_resource_usage.resource_code IS '资源类型代码';
COMMENT ON COLUMN sys_resource_usage.usage_value IS '消耗数值';
COMMENT ON COLUMN sys_resource_usage.business_type IS '业务类型';
COMMENT ON COLUMN sys_resource_usage.business_id IS '关联业务ID';
COMMENT ON COLUMN sys_resource_usage.usage_time IS '消耗时间';
COMMENT ON COLUMN sys_resource_usage.remark IS '备注';
COMMENT ON COLUMN sys_resource_usage.create_by IS '创建人ID';
COMMENT ON COLUMN sys_resource_usage.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_resource_usage.update_by IS '更新人ID';
COMMENT ON COLUMN sys_resource_usage.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_resource_usage.create_time IS '创建时间';
COMMENT ON COLUMN sys_resource_usage.update_time IS '更新时间';
COMMENT ON COLUMN sys_resource_usage.is_deleted IS '逻辑删除';
-- 订单表
CREATE TABLE bill_order (
                            id VARCHAR ( 200 ) NOT NULL,
                            tenant_id VARCHAR ( 200 ) NOT NULL,
                            tenant_name VARCHAR ( 100 ) NOT NULL,
                            order_no VARCHAR ( 64 ) NOT NULL,
                            product_type VARCHAR ( 50 ) NOT NULL,
                            product_id VARCHAR ( 200 ) DEFAULT NULL,
                            total_amount DECIMAL ( 10, 2 ) NOT NULL,
                            pay_amount DECIMAL ( 10, 2 ) NOT NULL,
                            STATUS VARCHAR ( 50 ) NOT NULL,
                            pay_time TIMESTAMP DEFAULT NULL,
                            pay_channel VARCHAR ( 50 ) DEFAULT '',
                            order_items JSONB DEFAULT '[]' :: jsonb,
                            ext_attributes JSONB DEFAULT '{}' :: jsonb,
                            create_by VARCHAR ( 200 ) DEFAULT NULL,
                            create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            update_by VARCHAR ( 200 ) DEFAULT NULL,
                            update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                            PRIMARY KEY ( id )
);
COMMENT ON TABLE bill_order IS '订单表-记录租户购买订单';
COMMENT ON COLUMN bill_order.id IS '主键ID';
COMMENT ON COLUMN bill_order.tenant_id IS '租户ID';
COMMENT ON COLUMN bill_order.tenant_name IS '租户名称';
COMMENT ON COLUMN bill_order.order_no IS '订单号';
COMMENT ON COLUMN bill_order.product_type IS '产品类型';
COMMENT ON COLUMN bill_order.product_id IS '产品ID';
COMMENT ON COLUMN bill_order.total_amount IS '订单总金额';
COMMENT ON COLUMN bill_order.pay_amount IS '实际支付金额';
COMMENT ON COLUMN bill_order.STATUS IS '订单状态';
COMMENT ON COLUMN bill_order.pay_time IS '支付时间';
COMMENT ON COLUMN bill_order.pay_channel IS '支付渠道';
COMMENT ON COLUMN bill_order.order_items IS '订单明细 (JSONB，商品/服务列表)';
COMMENT ON COLUMN bill_order.ext_attributes IS '行业扩展属性 (JSONB，存储行业特有订单字段)';
COMMENT ON COLUMN bill_order.create_by IS '创建人ID';
COMMENT ON COLUMN bill_order.create_by_name IS '创建人名称';
COMMENT ON COLUMN bill_order.update_by IS '更新人ID';
COMMENT ON COLUMN bill_order.update_by_name IS '更新人名称';
COMMENT ON COLUMN bill_order.create_time IS '创建时间';
COMMENT ON COLUMN bill_order.update_time IS '更新时间';
COMMENT ON COLUMN bill_order.is_deleted IS '逻辑删除';
-- 发票管理表
CREATE TABLE bill_invoice (
                              id VARCHAR ( 200 ) NOT NULL,
                              tenant_id VARCHAR ( 200 ) NOT NULL,
                              tenant_name VARCHAR ( 100 ) NOT NULL,
                              order_id VARCHAR ( 200 ) NOT NULL,
                              invoice_no VARCHAR ( 64 ) DEFAULT '',
                              invoice_type VARCHAR ( 50 ) NOT NULL,
                              invoice_title VARCHAR ( 200 ) NOT NULL,
                              tax_id VARCHAR ( 50 ) NOT NULL,
                              amount DECIMAL ( 10, 2 ) NOT NULL,
                              STATUS VARCHAR ( 50 ) NOT NULL,
                              invoice_url VARCHAR ( 500 ) DEFAULT '',
                              create_by VARCHAR ( 200 ) DEFAULT NULL,
                              create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              update_by VARCHAR ( 200 ) DEFAULT NULL,
                              update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                              PRIMARY KEY ( id )
);
COMMENT ON TABLE bill_invoice IS '发票管理表-记录租户发票申请与开具';
COMMENT ON COLUMN bill_invoice.id IS '主键ID';
COMMENT ON COLUMN bill_invoice.tenant_id IS '租户ID';
COMMENT ON COLUMN bill_invoice.tenant_name IS '租户名称';
COMMENT ON COLUMN bill_invoice.order_id IS '关联订单ID';
COMMENT ON COLUMN bill_invoice.invoice_no IS '发票号码';
COMMENT ON COLUMN bill_invoice.invoice_type IS '发票类型';
COMMENT ON COLUMN bill_invoice.invoice_title IS '发票抬头';
COMMENT ON COLUMN bill_invoice.tax_id IS '税号';
COMMENT ON COLUMN bill_invoice.amount IS '发票金额';
COMMENT ON COLUMN bill_invoice.STATUS IS '状态';
COMMENT ON COLUMN bill_invoice.invoice_url IS '发票文件URL';
COMMENT ON COLUMN bill_invoice.create_by IS '创建人ID';
COMMENT ON COLUMN bill_invoice.create_by_name IS '创建人名称';
COMMENT ON COLUMN bill_invoice.update_by IS '更新人ID';
COMMENT ON COLUMN bill_invoice.update_by_name IS '更新人名称';
COMMENT ON COLUMN bill_invoice.create_time IS '创建时间';
COMMENT ON COLUMN bill_invoice.update_time IS '更新时间';
COMMENT ON COLUMN bill_invoice.is_deleted IS '逻辑删除';
-- =============================================================================
-- 3. 身份与访问控制域 (IAM & perm)
-- =============================================================================
-- 用户基础表
CREATE TABLE sys_user (
                          id VARCHAR ( 200 ) NOT NULL,
                          username VARCHAR ( 50 ) NOT NULL,
                          PASSWORD VARCHAR ( 100 ) NOT NULL,
                          nickname VARCHAR ( 50 ) NOT NULL,
                          email VARCHAR ( 100 ) NOT NULL,
                          phone VARCHAR ( 20 ) NOT NULL,
                          avatar VARCHAR ( 255 ) DEFAULT NULL,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          login_ip VARCHAR ( 50 ) DEFAULT NULL,
                          login_date TIMESTAMP DEFAULT NULL,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user IS '用户基础表-存储全局用户信息 (不区分租户)';
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.PASSWORD IS '加密密码';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.STATUS IS '全局状态';
COMMENT ON COLUMN sys_user.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登录时间';
COMMENT ON COLUMN sys_user.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user.update_by IS '更新人ID';
COMMENT ON COLUMN sys_user.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除';
-- 用户 - 租户关联表
CREATE TABLE sys_user_tenant_rel (
                                     id VARCHAR ( 200 ) NOT NULL,
                                     user_id VARCHAR ( 200 ) NOT NULL,
                                     tenant_id VARCHAR ( 200 ) NOT NULL,
                                     dept_id VARCHAR ( 200 ) DEFAULT NULL,
                                     is_admin BOOLEAN DEFAULT FALSE,
                                     join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_default BOOLEAN DEFAULT FALSE,
                                     create_by VARCHAR ( 200 ) DEFAULT NULL,
                                     create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                     update_by VARCHAR ( 200 ) DEFAULT NULL,
                                     update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                     PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_tenant_rel IS '用户-租户关联表-实现用户与多租户绑定';
COMMENT ON COLUMN sys_user_tenant_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_tenant_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_tenant_rel.dept_id IS '主部门ID';
COMMENT ON COLUMN sys_user_tenant_rel.is_admin IS '是否租户管理员';
COMMENT ON COLUMN sys_user_tenant_rel.join_time IS '加入时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_default IS '是否默认租户';
COMMENT ON COLUMN sys_user_tenant_rel.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_tenant_rel.update_by IS '更新人ID';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_tenant_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_tenant_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_deleted IS '逻辑删除';
-- 角色表
CREATE TABLE sys_role (
                          id VARCHAR ( 200 ) NOT NULL,
                          role_name VARCHAR ( 50 ) NOT NULL,
                          role_desc VARCHAR ( 500 ) DEFAULT NULL,
                          role_code VARCHAR ( 50 ) NOT NULL,
                          tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                          tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                          data_scope VARCHAR ( 50 ) NOT NULL,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          sort_order INT DEFAULT 0,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_role IS '角色表-定义系统/租户/用户级角色';
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_role.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围';
COMMENT ON COLUMN sys_role.STATUS IS '状态';
COMMENT ON COLUMN sys_role.sort_order IS '排序顺序';
COMMENT ON COLUMN sys_role.create_by IS '创建人ID';
COMMENT ON COLUMN sys_role.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role.update_by IS '更新人ID';
COMMENT ON COLUMN sys_role.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除';
CREATE TABLE sys_role_policy (
                                 id VARCHAR ( 200 ) NOT NULL,
                                 target_type VARCHAR ( 50 ) NOT NULL,
                                 target_id VARCHAR ( 200 ) NOT NULL,
                                 target_name VARCHAR ( 100 ) NOT NULL,
                                 role_id VARCHAR ( 200 ) NOT NULL,
                                 role_name VARCHAR ( 50 ) NOT NULL,
                                 action VARCHAR ( 50 ) NOT NULL,
                                 priority INT DEFAULT 0,
                                 inheritance_enabled BOOLEAN DEFAULT TRUE,
                                 create_by VARCHAR ( 200 ) DEFAULT NULL,
                                 create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 update_by VARCHAR ( 200 ) DEFAULT NULL,
                                 update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                 PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_role_policy IS '角色策略控制表-实现角色级联禁用及策略继承';
COMMENT ON COLUMN sys_role_policy.id IS '主键ID';
COMMENT ON COLUMN sys_role_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_role_policy.target_id IS '目标ID';
COMMENT ON COLUMN sys_role_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_role_policy.role_id IS '关联角色ID';
COMMENT ON COLUMN sys_role_policy.role_name IS '关联角色名称';
COMMENT ON COLUMN sys_role_policy.action IS '动作';
COMMENT ON COLUMN sys_role_policy.priority IS '优先级';
COMMENT ON COLUMN sys_role_policy.inheritance_enabled IS '是否向下继承';
COMMENT ON COLUMN sys_role_policy.create_by IS '创建人ID';
COMMENT ON COLUMN sys_role_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role_policy.update_by IS '更新人ID';
COMMENT ON COLUMN sys_role_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_policy.is_deleted IS '逻辑删除';
-- 权限/资源表
CREATE TABLE sys_perm (
                          id VARCHAR ( 200 ) NOT NULL,
                          perm_name VARCHAR ( 50 ) NOT NULL,
                          perm_desc VARCHAR ( 500 ) DEFAULT NULL,
                          perm_code VARCHAR ( 100 ) NOT NULL,
                          perm_key VARCHAR ( 100 ) NOT NULL,
                          perm_type VARCHAR ( 50 ) NOT NULL,
                          parent_id VARCHAR ( 200 ) DEFAULT NULL,
                          parent_name VARCHAR ( 50 ) DEFAULT NULL,
                          path VARCHAR ( 200 ) DEFAULT NULL,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_perm IS '权限/资源表-定义系统所有可授权资源';
COMMENT ON COLUMN sys_perm.id IS '主键ID';
COMMENT ON COLUMN sys_perm.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm.perm_desc IS '权限描述';
COMMENT ON COLUMN sys_perm.perm_code IS '权限标识';
COMMENT ON COLUMN sys_perm.perm_key IS '权限键值(只可超级管理员变更，且变更后需要重启服务并修改相关常量)';
COMMENT ON COLUMN sys_perm.perm_type IS '权限类型';
COMMENT ON COLUMN sys_perm.parent_id IS '父权限ID';
COMMENT ON COLUMN sys_perm.parent_name IS '父权限名称';
COMMENT ON COLUMN sys_perm.path IS '资源路径';
COMMENT ON COLUMN sys_perm.STATUS IS '状态';
COMMENT ON COLUMN sys_perm.create_by IS '创建人ID';
COMMENT ON COLUMN sys_perm.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm.update_by IS '更新人ID';
COMMENT ON COLUMN sys_perm.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm.is_deleted IS '逻辑删除';
-- 权限策略控制表
CREATE TABLE sys_perm_policy (
                                 id VARCHAR ( 200 ) NOT NULL,
                                 target_type VARCHAR ( 50 ) NOT NULL,
                                 target_id VARCHAR ( 200 ) NOT NULL,
                                 target_name VARCHAR ( 100 ) NOT NULL,
                                 perm_id VARCHAR ( 200 ) NOT NULL,
                                 perm_name VARCHAR ( 50 ) NOT NULL,
                                 action VARCHAR ( 50 ) NOT NULL,
                                 priority INT DEFAULT 0,
                                 inheritance_enabled BOOLEAN DEFAULT TRUE,
                                 create_by VARCHAR ( 200 ) DEFAULT NULL,
                                 create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 update_by VARCHAR ( 200 ) DEFAULT NULL,
                                 update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                 PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_perm_policy IS '权限策略控制表-实现四层权限及禁用继承逻辑';
COMMENT ON COLUMN sys_perm_policy.id IS '主键ID';
COMMENT ON COLUMN sys_perm_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_perm_policy.target_id IS '目标ID';
COMMENT ON COLUMN sys_perm_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_perm_policy.perm_id IS '关联权限ID';
COMMENT ON COLUMN sys_perm_policy.perm_name IS '关联权限名称';
COMMENT ON COLUMN sys_perm_policy.action IS '动作';
COMMENT ON COLUMN sys_perm_policy.priority IS '优先级';
COMMENT ON COLUMN sys_perm_policy.inheritance_enabled IS '是否向下继承';
COMMENT ON COLUMN sys_perm_policy.create_by IS '创建人ID';
COMMENT ON COLUMN sys_perm_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm_policy.update_by IS '更新人ID';
COMMENT ON COLUMN sys_perm_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm_policy.is_deleted IS '逻辑删除';
-- 字段权限配置表
CREATE TABLE sys_field_perm_config (
                                       id BIGSERIAL NOT NULL,
                                       perm_code VARCHAR ( 100 ) NOT NULL,
                                       field_name VARCHAR ( 64 ) NOT NULL,
                                       create_by VARCHAR ( 200 ) DEFAULT NULL,
                                       create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                       update_by VARCHAR ( 200 ) DEFAULT NULL,
                                       update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                       PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_field_perm_config IS '字段权限配置表';
COMMENT ON COLUMN sys_field_perm_config.id IS '主键ID';
COMMENT ON COLUMN sys_field_perm_config.perm_code IS '权限编码';
COMMENT ON COLUMN sys_field_perm_config.field_name IS '数据库字段名';
COMMENT ON COLUMN sys_field_perm_config.create_by IS '创建人ID';
COMMENT ON COLUMN sys_field_perm_config.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_field_perm_config.update_by IS '更新人ID';
COMMENT ON COLUMN sys_field_perm_config.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_field_perm_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_field_perm_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_field_perm_config.is_deleted IS '逻辑删除';
-- 用户角色关联表
CREATE TABLE sys_user_role_rel (
                                   id VARCHAR ( 200 ) NOT NULL,
                                   user_id VARCHAR ( 200 ) NOT NULL,
                                   role_id VARCHAR ( 200 ) NOT NULL,
                                   tenant_id VARCHAR ( 200 ) NOT NULL,
                                   effective_time TIMESTAMP DEFAULT NULL,
                                   expire_time TIMESTAMP DEFAULT NULL,
                                   create_by VARCHAR ( 200 ) DEFAULT NULL,
                                   create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   update_by VARCHAR ( 200 ) DEFAULT NULL,
                                   update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                   PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_role_rel IS '用户角色关联表 - 用户与角色的绑定关系';
COMMENT ON COLUMN sys_user_role_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_role_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role_rel.role_id IS '角色ID';
COMMENT ON COLUMN sys_user_role_rel.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_role_rel.effective_time IS '角色生效时间';
COMMENT ON COLUMN sys_user_role_rel.expire_time IS '角色失效时间';
COMMENT ON COLUMN sys_user_role_rel.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user_role_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_role_rel.update_by IS '更新人ID';
COMMENT ON COLUMN sys_user_role_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_role_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_role_rel.is_deleted IS '逻辑删除';
-- 用户 Token 记录表
CREATE TABLE sys_user_token (
                                id VARCHAR ( 200 ) NOT NULL,
                                user_id VARCHAR ( 200 ) NOT NULL,
                                user_name VARCHAR ( 50 ) NOT NULL,
                                tenant_id VARCHAR ( 200 ) NOT NULL,
                                tenant_name VARCHAR ( 50 ) NOT NULL,
                                token VARCHAR ( 255 ) NOT NULL,
                                device_info VARCHAR ( 200 ) DEFAULT '',
                                login_ip VARCHAR ( 50 ) DEFAULT '',
                                login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                expire_time TIMESTAMP NOT NULL,
                                STATUS VARCHAR ( 50 ) NOT NULL,
                                is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_token IS '用户Token记录表-用于多端登录管理和强制下线';
COMMENT ON COLUMN sys_user_token.id IS '主键ID';
COMMENT ON COLUMN sys_user_token.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_token.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_token.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_token.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_token.token IS '登录令牌';
COMMENT ON COLUMN sys_user_token.device_info IS '设备信息';
COMMENT ON COLUMN sys_user_token.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user_token.login_time IS '登录时间';
COMMENT ON COLUMN sys_user_token.expire_time IS '过期时间';
COMMENT ON COLUMN sys_user_token.STATUS IS '状态';
COMMENT ON COLUMN sys_user_token.is_deleted IS '逻辑删除';
-- =============================================================================
-- 4. 组织架构域 (Organization)
-- =============================================================================
-- 部门表
CREATE TABLE sys_dept (
                          id VARCHAR ( 200 ) NOT NULL,
                          dept_name VARCHAR ( 50 ) NOT NULL,
                          dept_desc VARCHAR ( 255 ) DEFAULT NULL,
                          dept_code VARCHAR ( 50 ) NOT NULL,
                          parent_id VARCHAR ( 200 ) DEFAULT NULL,
                          parent_name VARCHAR ( 50 ) DEFAULT NULL,
                          ancestors VARCHAR ( 500 ) DEFAULT '',
                          tenant_id VARCHAR ( 200 ) NOT NULL,
                          tenant_name VARCHAR ( 100 ) NOT NULL,
                          leader_id VARCHAR ( 200 ) DEFAULT NULL,
                          leader_name VARCHAR ( 100 ) DEFAULT NULL,
                          phone VARCHAR ( 20 ) DEFAULT '',
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_dept IS '部门表-存储租户组织架构，支持无限层级';
COMMENT ON COLUMN sys_dept.id IS '主键ID';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.dept_desc IS '部门描述';
COMMENT ON COLUMN sys_dept.dept_code IS '部门编码';
COMMENT ON COLUMN sys_dept.parent_id IS '父部门ID';
COMMENT ON COLUMN sys_dept.parent_name IS '父部门名称';
COMMENT ON COLUMN sys_dept.ancestors IS '祖级列表';
COMMENT ON COLUMN sys_dept.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_dept.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_dept.leader_id IS '部门负责人ID';
COMMENT ON COLUMN sys_dept.leader_name IS '部门负责人名称';
COMMENT ON COLUMN sys_dept.phone IS '部门电话';
COMMENT ON COLUMN sys_dept.STATUS IS '状态';
COMMENT ON COLUMN sys_dept.create_by IS '创建人ID';
COMMENT ON COLUMN sys_dept.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_dept.update_by IS '更新人ID';
COMMENT ON COLUMN sys_dept.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_dept.update_time IS '更新时间';
COMMENT ON COLUMN sys_dept.is_deleted IS '逻辑删除';
-- 岗位表
CREATE TABLE sys_post (
                          id VARCHAR ( 200 ) NOT NULL,
                          post_name VARCHAR ( 50 ) NOT NULL,
                          post_desc VARCHAR ( 50 ) DEFAULT NULL,
                          post_code VARCHAR ( 50 ) NOT NULL,
                          dept_id VARCHAR ( 200 ) NOT NULL,
                          tenant_id VARCHAR ( 200 ) NOT NULL,
                          tenant_name VARCHAR ( 100 ) NOT NULL,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_post IS '岗位表-部门下的具体职位';
COMMENT ON COLUMN sys_post.id IS '主键ID';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.post_desc IS '岗位描述';
COMMENT ON COLUMN sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN sys_post.dept_id IS '所属部门ID';
COMMENT ON COLUMN sys_post.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_post.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_post.STATUS IS '状态';
COMMENT ON COLUMN sys_post.create_by IS '创建人ID';
COMMENT ON COLUMN sys_post.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_post.update_by IS '更新人ID';
COMMENT ON COLUMN sys_post.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_post.create_time IS '创建时间';
COMMENT ON COLUMN sys_post.update_time IS '更新时间';
COMMENT ON COLUMN sys_post.is_deleted IS '逻辑删除';
-- 用户组表
CREATE TABLE sys_user_group (
                                id VARCHAR ( 200 ) NOT NULL,
                                group_name VARCHAR ( 50 ) NOT NULL,
                                group_type VARCHAR ( 50 ) NOT NULL,
                                tenant_id VARCHAR ( 200 ) NOT NULL,
                                tenant_name VARCHAR ( 100 ) NOT NULL,
                                description VARCHAR ( 200 ) DEFAULT '',
                                STATUS VARCHAR ( 50 ) NOT NULL,
                                create_by VARCHAR ( 200 ) DEFAULT NULL,
                                create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                update_by VARCHAR ( 200 ) DEFAULT NULL,
                                update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_group IS '用户组表-虚拟组，用于跨部门权限分配';
COMMENT ON COLUMN sys_user_group.id IS '主键ID';
COMMENT ON COLUMN sys_user_group.group_name IS '组名称';
COMMENT ON COLUMN sys_user_group.group_type IS '组类型';
COMMENT ON COLUMN sys_user_group.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_user_group.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_user_group.description IS '描述';
COMMENT ON COLUMN sys_user_group.STATUS IS '状态';
COMMENT ON COLUMN sys_user_group.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user_group.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_group.update_by IS '更新人ID';
COMMENT ON COLUMN sys_user_group.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_group.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_group.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_group.is_deleted IS '逻辑删除';
-- 用户组成员表
CREATE TABLE sys_user_group_rel (
                                    id VARCHAR ( 200 ) NOT NULL,
                                    group_id VARCHAR ( 200 ) NOT NULL,
                                    user_id VARCHAR ( 200 ) NOT NULL,
                                    create_by VARCHAR ( 200 ) DEFAULT NULL,
                                    create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    update_by VARCHAR ( 200 ) DEFAULT NULL,
                                    update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                    PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_group_rel IS '用户组成员表-用户与用户组的关联';
COMMENT ON COLUMN sys_user_group_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_group_rel.group_id IS '用户组ID';
COMMENT ON COLUMN sys_user_group_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_group_rel.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user_group_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_group_rel.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_user_group_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_group_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_group_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_group_rel.is_deleted IS '逻辑删除';
-- 角色数据权限关联表
CREATE TABLE sys_role_dept_rel (
                                   id VARCHAR ( 200 ) NOT NULL,
                                   role_id VARCHAR ( 200 ) NOT NULL,
                                   dept_id VARCHAR ( 200 ) NOT NULL,
                                   tenant_id VARCHAR ( 200 ) NOT NULL,
                                   create_by VARCHAR ( 200 ) DEFAULT NULL,
                                   create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   update_by VARCHAR ( 200 ) DEFAULT NULL,
                                   update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                   PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_role_dept_rel IS '角色数据权限关联表-角色自定义数据范围时关联的部门';
COMMENT ON COLUMN sys_role_dept_rel.id IS '主键ID';
COMMENT ON COLUMN sys_role_dept_rel.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_dept_rel.dept_id IS '部门ID';
COMMENT ON COLUMN sys_role_dept_rel.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_role_dept_rel.create_by IS '创建人ID';
COMMENT ON COLUMN sys_role_dept_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role_dept_rel.update_by IS '更新人ID';
COMMENT ON COLUMN sys_role_dept_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role_dept_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_dept_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_dept_rel.is_deleted IS '逻辑删除';
-- =============================================================================
-- 5. 动态配置域 (Dynamic Configuration)
-- =============================================================================
-- 动态表单配置表
CREATE TABLE sys_form_config (
                                 id VARCHAR ( 200 ) NOT NULL,
                                 tenant_id VARCHAR ( 200 ) NOT NULL,
                                 tenant_name VARCHAR ( 100 ) NOT NULL,
                                 biz_type VARCHAR ( 50 ) NOT NULL,
                                 form_name VARCHAR ( 100 ) NOT NULL,
                                 form_schema JSONB NOT NULL,
                                 STATUS VARCHAR ( 50 ) NOT NULL,
                                 version INT DEFAULT 1,
                                 create_by VARCHAR ( 200 ) DEFAULT NULL,
                                 create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 update_by VARCHAR ( 200 ) DEFAULT NULL,
                                 update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                 PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_form_config IS '动态表单配置表 - 实现不同行业表单字段动态配置';
COMMENT ON COLUMN sys_form_config.id IS '主键ID';
COMMENT ON COLUMN sys_form_config.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_form_config.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_form_config.biz_type IS '业务类型';
COMMENT ON COLUMN sys_form_config.form_name IS '表单名称';
COMMENT ON COLUMN sys_form_config.form_schema IS '表单结构配置 (JSONB，定义字段类型/校验/选项)';
COMMENT ON COLUMN sys_form_config.STATUS IS '状态 (ENABLED-启用 DISABLED-停用)';
COMMENT ON COLUMN sys_form_config.version IS '版本号 (支持表单版本管理)';
COMMENT ON COLUMN sys_form_config.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_form_config.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_form_config.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_form_config.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_form_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_form_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_form_config.is_deleted IS '逻辑删除';
-- 动态数据源配置表
CREATE TABLE sys_datasource_config (
                                       id VARCHAR ( 200 ) NOT NULL,
                                       tenant_id VARCHAR ( 200 ) NOT NULL,
                                       tenant_name VARCHAR ( 100 ) NOT NULL,
                                       datasource_code VARCHAR ( 50 ) NOT NULL,
                                       datasource_name VARCHAR ( 100 ) NOT NULL,
                                       datasource_type VARCHAR ( 50 ) NOT NULL,
                                       datasource_config JSONB NOT NULL,
                                       cache_enabled BOOLEAN DEFAULT TRUE,
                                       cache_expire INT DEFAULT 300,
                                       STATUS VARCHAR ( 50 ) NOT NULL,
                                       create_by VARCHAR ( 200 ) DEFAULT NULL,
                                       create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                       update_by VARCHAR ( 200 ) DEFAULT NULL,
                                       update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                       PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_datasource_config IS '动态数据源配置表 - 实现下拉框数据来源动态配置';
COMMENT ON COLUMN sys_datasource_config.id IS '主键ID';
COMMENT ON COLUMN sys_datasource_config.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_datasource_config.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_datasource_config.datasource_code IS '数据源编码 (如：dish_list, part_list)';
COMMENT ON COLUMN sys_datasource_config.datasource_name IS '数据源名称';
COMMENT ON COLUMN sys_datasource_config.datasource_type IS '数据源类型 (BUSINESS_TABLE-业务表 API_INTERFACE-API接口 DICT-字典 SQL_QUERY-SQL查询)';
COMMENT ON COLUMN sys_datasource_config.datasource_config IS '数据源配置 (JSONB，定义表名/字段/条件)';
COMMENT ON COLUMN sys_datasource_config.cache_enabled IS '是否启用缓存';
COMMENT ON COLUMN sys_datasource_config.cache_expire IS '缓存过期时间 (秒)';
COMMENT ON COLUMN sys_datasource_config.STATUS IS '状态 (ENABLED-启用 DISABLED-停用)';
COMMENT ON COLUMN sys_datasource_config.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_datasource_config.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_datasource_config.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_datasource_config.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_datasource_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_datasource_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_datasource_config.is_deleted IS '逻辑删除';
-- 打印模板配置表
CREATE TABLE sys_print_template (
                                    id VARCHAR ( 200 ) NOT NULL,
                                    tenant_id VARCHAR ( 200 ) NOT NULL,
                                    tenant_name VARCHAR ( 100 ) NOT NULL,
                                    template_code VARCHAR ( 50 ) NOT NULL,
                                    template_name VARCHAR ( 100 ) NOT NULL,
                                    biz_type VARCHAR ( 50 ) NOT NULL,
                                    template_type VARCHAR ( 50 ) NOT NULL,
                                    template_content TEXT NOT NULL,
                                    template_config JSONB DEFAULT '{}' :: jsonb,
                                    paper_size VARCHAR ( 20 ) DEFAULT 'A4',
                                    orientation VARCHAR ( 10 ) DEFAULT 'portrait',
                                    STATUS VARCHAR ( 50 ) NOT NULL,
                                    is_default BOOLEAN DEFAULT FALSE,
                                    version INT DEFAULT 1,
                                    create_by VARCHAR ( 200 ) DEFAULT NULL,
                                    create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    update_by VARCHAR ( 200 ) DEFAULT NULL,
                                    update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                    PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_print_template IS '打印模板配置表 - 实现不同行业打印/PDF 模板动态配置';
COMMENT ON COLUMN sys_print_template.id IS '主键ID';
COMMENT ON COLUMN sys_print_template.tenant_id IS '所属租户 ID';
COMMENT ON COLUMN sys_print_template.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_print_template.template_code IS '模板编码 (如：education_contract, restaurant_receipt)';
COMMENT ON COLUMN sys_print_template.template_name IS '模板名称';
COMMENT ON COLUMN sys_print_template.biz_type IS '业务类型';
COMMENT ON COLUMN sys_print_template.template_type IS '模板类型 (HTML-Markdown JSON_CONFIG-JSON配置)';
COMMENT ON COLUMN sys_print_template.template_content IS '模板内容 (HTML/模板引擎语法)';
COMMENT ON COLUMN sys_print_template.template_config IS '模板配置 (JSONB，定义变量/条件显示)';
COMMENT ON COLUMN sys_print_template.paper_size IS '纸张大小 (A4/A5/80mm 热敏纸等)';
COMMENT ON COLUMN sys_print_template.orientation IS '纸张方向 (portrait/landscape)';
COMMENT ON COLUMN sys_print_template.STATUS IS '状态 (ENABLED-启用 DISABLED-停用)';
COMMENT ON COLUMN sys_print_template.is_default IS '是否默认模板';
COMMENT ON COLUMN sys_print_template.version IS '版本号';
COMMENT ON COLUMN sys_print_template.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_print_template.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_print_template.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_print_template.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_print_template.create_time IS '创建时间';
COMMENT ON COLUMN sys_print_template.update_time IS '更新时间';
COMMENT ON COLUMN sys_print_template.is_deleted IS '逻辑删除';
-- =============================================================================
-- 6. 系统支撑域 (System Support)
-- =============================================================================
-- 菜单表
CREATE TABLE sys_menu (
                          id VARCHAR ( 200 ) NOT NULL,
                          menu_name VARCHAR ( 50 ) NOT NULL,
                          menu_type VARCHAR ( 50 ) NOT NULL,
                          parent_id VARCHAR ( 200 ) DEFAULT NULL,
                          parent_name VARCHAR ( 50 ) DEFAULT NULL,
                          path VARCHAR ( 200 ) DEFAULT '',
                          component VARCHAR ( 200 ) DEFAULT '',
                          perm_code VARCHAR ( 100 ) DEFAULT '',
                          visible BOOLEAN DEFAULT TRUE,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          sort_order INT DEFAULT 0,
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_menu IS '菜单表 - 前端导航与按钮权限映射';
COMMENT ON COLUMN sys_menu.id IS '主键ID';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.menu_type IS '类型 (DIRECTORY-目录 MENU-菜单 BUTTON-按钮)';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单 ID';
COMMENT ON COLUMN sys_menu.parent_name IS '父菜单名称';
COMMENT ON COLUMN sys_menu.path IS '路由地址';
COMMENT ON COLUMN sys_menu.component IS '组件路径';
COMMENT ON COLUMN sys_menu.perm_code IS '权限标识';
COMMENT ON COLUMN sys_menu.visible IS '是否可见';
COMMENT ON COLUMN sys_menu.STATUS IS '状态 (ENABLED-正常 DISABLED-停用)';
COMMENT ON COLUMN sys_menu.sort_order IS '排序';
COMMENT ON COLUMN sys_menu.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_menu.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_menu.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_menu.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.is_deleted IS '逻辑删除';
-- 字典类型表
CREATE TABLE sys_dict (
                          id VARCHAR ( 200 ) NOT NULL,
                          dict_name VARCHAR ( 100 ) NOT NULL,
                          dict_type VARCHAR ( 100 ) NOT NULL,
                          tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                          tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                          STATUS VARCHAR ( 50 ) NOT NULL,
                          remark VARCHAR ( 500 ) DEFAULT '',
                          create_by VARCHAR ( 200 ) DEFAULT NULL,
                          create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_dict IS '字典类型表 - 定义系统常量类型';
COMMENT ON COLUMN sys_dict.id IS '主键ID';
COMMENT ON COLUMN sys_dict.dict_name IS '字典名称';
COMMENT ON COLUMN sys_dict.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict.tenant_id IS '所属租户 (NULL 为系统字典)';
COMMENT ON COLUMN sys_dict.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_dict.STATUS IS '状态 (ENABLED-正常 DISABLED-停用)';
COMMENT ON COLUMN sys_dict.remark IS '备注';
COMMENT ON COLUMN sys_dict.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_dict.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_dict.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_dict.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_dict.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict.is_deleted IS '逻辑删除';
-- 字典数据表
CREATE TABLE sys_dict_item (
                               id VARCHAR ( 200 ) NOT NULL,
                               dict_id VARCHAR ( 200 ) NOT NULL,
                               dict_label VARCHAR ( 100 ) NOT NULL,
                               dict_value VARCHAR ( 100 ) NOT NULL,
                               sort_order INT DEFAULT 0,
                               STATUS VARCHAR ( 50 ) NOT NULL,
                               create_by VARCHAR ( 200 ) DEFAULT NULL,
                               create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                               update_by VARCHAR ( 200 ) DEFAULT NULL,
                               update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                               create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                               PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_dict_item IS '字典数据表 - 字典具体键值对';
COMMENT ON COLUMN sys_dict_item.id IS '主键ID';
COMMENT ON COLUMN sys_dict_item.dict_id IS '关联字典类型 ID';
COMMENT ON COLUMN sys_dict_item.dict_label IS '字典标签';
COMMENT ON COLUMN sys_dict_item.dict_value IS '字典值';
COMMENT ON COLUMN sys_dict_item.sort_order IS '排序';
COMMENT ON COLUMN sys_dict_item.STATUS IS '状态 (ENABLED-正常 DISABLED-停用)';
COMMENT ON COLUMN sys_dict_item.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_dict_item.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_dict_item.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_dict_item.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_dict_item.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_item.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict_item.is_deleted IS '逻辑删除';
-- 文件资源表 已加入
CREATE TABLE sys_file (
                          id VARCHAR ( 200 ) NOT NULL,
                          tenant_id VARCHAR ( 200 ) NOT NULL,
                          tenant_name VARCHAR ( 150 ) NOT NULL,
                          file_name VARCHAR ( 255 ) NOT NULL,
                          original_name VARCHAR ( 255 ) NOT NULL,
                          file_path VARCHAR ( 500 ) NOT NULL,
                          file_url VARCHAR ( 500 ) NOT NULL,
                          file_size BIGINT NOT NULL,
                          file_type VARCHAR ( 50 ) NOT NULL,
                          mime_type VARCHAR ( 50 ) NOT NULL,
                          biz_type VARCHAR ( 50 ) NOT NULL,
                          upload_by VARCHAR ( 200 ) NOT NULL,
                          upload_by_name VARCHAR ( 50 ) NOT NULL,
                          upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_by VARCHAR ( 200 ) DEFAULT NULL,
                          update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                          PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_file IS '文件资源表 - 存储上传的文件信息';
COMMENT ON COLUMN sys_file.id IS '主键ID';
COMMENT ON COLUMN sys_file.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_file.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_file.file_name IS '文件名称';
COMMENT ON COLUMN sys_file.original_name IS '原始文件名';
COMMENT ON COLUMN sys_file.file_path IS '文件存储路径';
COMMENT ON COLUMN sys_file.file_url IS '文件访问URL';
COMMENT ON COLUMN sys_file.file_size IS '文件大小(字节)';
COMMENT ON COLUMN sys_file.file_type IS '文件类型';
COMMENT ON COLUMN sys_file.mime_type IS 'MIME类型';
COMMENT ON COLUMN sys_file.biz_type IS '业务类型分类';
COMMENT ON COLUMN sys_file.upload_by IS '上传人ID';
COMMENT ON COLUMN sys_file.upload_by_name IS '上传人名称';
COMMENT ON COLUMN sys_file.upload_time IS '上传时间';
COMMENT ON COLUMN sys_file.update_by IS '更新人ID';
COMMENT ON COLUMN sys_file.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_file.update_time IS '更新时间';
COMMENT ON COLUMN sys_file.is_deleted IS '逻辑删除';
-- 通知公告表
CREATE TABLE sys_notice (
                            id VARCHAR ( 200 ) NOT NULL,
                            notice_title VARCHAR ( 100 ) NOT NULL,
                            notice_type VARCHAR ( 50 ) NOT NULL,
                            notice_content TEXT DEFAULT NULL,
                            STATUS VARCHAR ( 50 ) NOT NULL,
                            target_type VARCHAR ( 50 ) NOT NULL,
                            target_ids VARCHAR ( 1000 ) DEFAULT '',
                            tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                            tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                            create_by VARCHAR ( 200 ) DEFAULT NULL,
                            create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            update_by VARCHAR ( 200 ) DEFAULT NULL,
                            update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                            PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_notice IS '通知公告表 - 系统公告与站内信';
COMMENT ON COLUMN sys_notice.id IS '主键ID';
COMMENT ON COLUMN sys_notice.notice_title IS '公告标题';
COMMENT ON COLUMN sys_notice.notice_type IS '类型 (ANNOUNCEMENT-公告 NOTIFICATION-通知)';
COMMENT ON COLUMN sys_notice.notice_content IS '公告内容';
COMMENT ON COLUMN sys_notice.STATUS IS '状态 (PUBLISHED-发布 UNPUBLISHED-下架)';
COMMENT ON COLUMN sys_notice.target_type IS '目标类型 (ALL-全员 TENANT-指定租户 USER-指定用户)';
COMMENT ON COLUMN sys_notice.target_ids IS '目标 ID 集合 (逗号分隔)';
COMMENT ON COLUMN sys_notice.tenant_id IS '所属租户 (NULL 为系统公告)';
COMMENT ON COLUMN sys_notice.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_notice.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_notice.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_notice.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_notice.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_notice.create_time IS '创建时间';
COMMENT ON COLUMN sys_notice.update_time IS '更新时间';
COMMENT ON COLUMN sys_notice.is_deleted IS '逻辑删除';
-- 用户公告阅读状态表
CREATE TABLE sys_notice_user_rel (
                                     id VARCHAR ( 200 ) NOT NULL,
                                     notice_id VARCHAR ( 200 ) NOT NULL,
                                     user_id VARCHAR ( 200 ) NOT NULL,
                                     tenant_id VARCHAR ( 200 ) NOT NULL,
                                     read_status VARCHAR ( 50 ) NOT NULL,
                                     read_time TIMESTAMP DEFAULT NULL,
                                     create_by VARCHAR ( 200 ) DEFAULT NULL,
                                     create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                     update_by VARCHAR ( 200 ) DEFAULT NULL,
                                     update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                     PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_notice_user_rel IS '用户公告阅读状态表 - 记录用户公告已读/未读状态';
COMMENT ON COLUMN sys_notice_user_rel.id IS '主键ID';
COMMENT ON COLUMN sys_notice_user_rel.notice_id IS '公告 ID';
COMMENT ON COLUMN sys_notice_user_rel.user_id IS '用户 ID';
COMMENT ON COLUMN sys_notice_user_rel.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_notice_user_rel.read_status IS '阅读状态 (UNREAD-未读 READ-已读)';
COMMENT ON COLUMN sys_notice_user_rel.read_time IS '阅读时间';
COMMENT ON COLUMN sys_notice_user_rel.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_notice_user_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_notice_user_rel.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_notice_user_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_notice_user_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_notice_user_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_notice_user_rel.is_deleted IS '逻辑删除';
-- =============================================================================
-- 7. 日志与审计域 (Log & Audit)
-- =============================================================================
-- 操作日志表
CREATE TABLE sys_oper_log (
                              id VARCHAR ( 200 ) NOT NULL,
                              tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                              tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                              module VARCHAR ( 50 ) DEFAULT '',
                              business_type VARCHAR ( 50 ) DEFAULT 'OTHER',
                              method VARCHAR ( 100 ) DEFAULT '',
                              request_method VARCHAR ( 10 ) DEFAULT '',
                              operator_name VARCHAR ( 50 ) DEFAULT '',
                              operator_id VARCHAR ( 200 ) DEFAULT NULL,
                              dept_name VARCHAR ( 50 ) DEFAULT '',
                              oper_url VARCHAR ( 255 ) DEFAULT '',
                              oper_ip VARCHAR ( 128 ) DEFAULT '',
                              oper_location VARCHAR ( 255 ) DEFAULT '',
                              oper_param VARCHAR ( 2000 ) DEFAULT '',
                              json_result VARCHAR ( 2000 ) DEFAULT '',
                              STATUS VARCHAR ( 50 ) NOT NULL,
                              error_msg VARCHAR ( 2000 ) DEFAULT '',
                              oper_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              create_by VARCHAR ( 200 ) DEFAULT NULL,
                              create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              update_by VARCHAR ( 200 ) DEFAULT NULL,
                              update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                              PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_oper_log IS '操作日志表 - 审计用户操作行为';
COMMENT ON COLUMN sys_oper_log.id IS '主键ID';
COMMENT ON COLUMN sys_oper_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_oper_log.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_oper_log.module IS '操作模块';
COMMENT ON COLUMN sys_oper_log.business_type IS '业务类型 (INSERT-新增 UPDATE-修改 DELETE-删除 GRANT-授权 EXPORT-导出 IMPORT-导入 OTHER-其他)';
COMMENT ON COLUMN sys_oper_log.method IS '请求方法';
COMMENT ON COLUMN sys_oper_log.request_method IS '请求方式 (GET/POST/PUT/DELETE)';
COMMENT ON COLUMN sys_oper_log.operator_name IS '操作人员姓名';
COMMENT ON COLUMN sys_oper_log.operator_id IS '操作人员 ID';
COMMENT ON COLUMN sys_oper_log.dept_name IS '部门名称';
COMMENT ON COLUMN sys_oper_log.oper_url IS '请求 URL';
COMMENT ON COLUMN sys_oper_log.oper_ip IS '操作 IP';
COMMENT ON COLUMN sys_oper_log.oper_location IS '操作地点';
COMMENT ON COLUMN sys_oper_log.oper_param IS '请求参数';
COMMENT ON COLUMN sys_oper_log.json_result IS '返回结果';
COMMENT ON COLUMN sys_oper_log.STATUS IS '操作状态 (SUCCESS-正常 FAIL-失败)';
COMMENT ON COLUMN sys_oper_log.error_msg IS '错误消息';
COMMENT ON COLUMN sys_oper_log.oper_time IS '操作时间';
COMMENT ON COLUMN sys_oper_log.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_oper_log.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_oper_log.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_oper_log.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_oper_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_oper_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_oper_log.is_deleted IS '逻辑删除';
-- 登录日志表
CREATE TABLE sys_login_log (
                               id VARCHAR ( 200 ) NOT NULL,
                               user_id VARCHAR ( 200 ) DEFAULT NULL,
                               username VARCHAR ( 50 ) DEFAULT '',
                               tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                               tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                               ip_address VARCHAR ( 128 ) DEFAULT '',
                               login_location VARCHAR ( 255 ) DEFAULT '',
                               browser VARCHAR ( 50 ) DEFAULT '',
                               os VARCHAR ( 50 ) DEFAULT '',
                               STATUS VARCHAR ( 50 ) NOT NULL,
                               msg VARCHAR ( 255 ) DEFAULT '',
                               login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               create_by VARCHAR ( 200 ) DEFAULT NULL,
                               create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                               update_by VARCHAR ( 200 ) DEFAULT NULL,
                               update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                               create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                               PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_login_log IS '登录日志表 - 记录用户登录信息';
COMMENT ON COLUMN sys_login_log.id IS '主键ID';
COMMENT ON COLUMN sys_login_log.user_id IS '用户 ID';
COMMENT ON COLUMN sys_login_log.username IS '用户名';
COMMENT ON COLUMN sys_login_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_login_log.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_login_log.ip_address IS '登录 IP';
COMMENT ON COLUMN sys_login_log.login_location IS '登录地点';
COMMENT ON COLUMN sys_login_log.browser IS '浏览器';
COMMENT ON COLUMN sys_login_log.os IS '操作系统';
COMMENT ON COLUMN sys_login_log.STATUS IS '状态 (SUCCESS-成功 FAIL-失败)';
COMMENT ON COLUMN sys_login_log.msg IS '提示消息';
COMMENT ON COLUMN sys_login_log.login_time IS '登录时间';
COMMENT ON COLUMN sys_login_log.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_login_log.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_login_log.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_login_log.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_login_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_login_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_login_log.is_deleted IS '逻辑删除';
-- 数据变更审计表
CREATE TABLE sys_data_audit_log (
                                    id VARCHAR ( 200 ) NOT NULL,
                                    tenant_id VARCHAR ( 200 ) NOT NULL,
                                    tenant_name VARCHAR ( 100 ) NOT NULL,
                                    table_name VARCHAR ( 50 ) NOT NULL,
                                    record_id VARCHAR ( 200 ) NOT NULL,
                                    operator_id VARCHAR ( 200 ) NOT NULL,
                                    operator_name VARCHAR ( 50 ) DEFAULT '',
                                    operate_type VARCHAR ( 50 ) NOT NULL,
                                    old_value JSONB DEFAULT '{}' :: jsonb,
                                    new_value JSONB DEFAULT '{}' :: jsonb,
                                    operate_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    create_by VARCHAR ( 200 ) DEFAULT NULL,
                                    create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    update_by VARCHAR ( 200 ) DEFAULT NULL,
                                    update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                    PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_data_audit_log IS '数据变更审计表 - 记录数据字段变更详情';
COMMENT ON COLUMN sys_data_audit_log.id IS '主键ID';
COMMENT ON COLUMN sys_data_audit_log.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_data_audit_log.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_data_audit_log.table_name IS '表名';
COMMENT ON COLUMN sys_data_audit_log.record_id IS '记录 ID';
COMMENT ON COLUMN sys_data_audit_log.operator_id IS '操作人 ID';
COMMENT ON COLUMN sys_data_audit_log.operator_name IS '操作人名称';
COMMENT ON COLUMN sys_data_audit_log.operate_type IS '操作类型 (INSERT-新增 UPDATE-更新 DELETE-删除)';
COMMENT ON COLUMN sys_data_audit_log.old_value IS '修改前数据 (JSONB)';
COMMENT ON COLUMN sys_data_audit_log.new_value IS '修改后数据 (JSONB)';
COMMENT ON COLUMN sys_data_audit_log.operate_time IS '操作时间';
COMMENT ON COLUMN sys_data_audit_log.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_data_audit_log.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_data_audit_log.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_data_audit_log.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_data_audit_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_data_audit_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_data_audit_log.is_deleted IS '逻辑删除';
-- =============================================================================
-- 8. 消息通知中心 (Notification Center)
-- =============================================================================
-- 消息模板表
CREATE TABLE sys_message_template (
                                      id VARCHAR ( 200 ) NOT NULL,
                                      template_code VARCHAR ( 100 ) NOT NULL,
                                      template_name VARCHAR ( 100 ) NOT NULL,
                                      tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                                      tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                                      biz_type VARCHAR ( 50 ) DEFAULT '',
                                      message_type VARCHAR ( 50 ) NOT NULL,
                                      template_title VARCHAR ( 200 ) DEFAULT '',
                                      template_content TEXT NOT NULL,
                                      template_example JSONB DEFAULT '{}' :: jsonb,
                                      VARIABLES JSONB DEFAULT '[]' :: jsonb,
                                      STATUS VARCHAR ( 50 ) NOT NULL,
                                      version INT DEFAULT 1,
                                      LANGUAGE VARCHAR ( 20 ) DEFAULT 'zh-CN',
                                      create_by VARCHAR ( 200 ) DEFAULT NULL,
                                      create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                      update_by VARCHAR ( 200 ) DEFAULT NULL,
                                      update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                      PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_message_template IS '消息模板表 - 定义各类消息的内容模板';
COMMENT ON COLUMN sys_message_template.id IS '主键ID';
COMMENT ON COLUMN sys_message_template.template_code IS '模板编码 (如：ORDER_PAID, PACKAGE_EXPIRE_WARNING)';
COMMENT ON COLUMN sys_message_template.template_name IS '模板名称';
COMMENT ON COLUMN sys_message_template.tenant_id IS '所属租户 ID (NULL 为系统模板)';
COMMENT ON COLUMN sys_message_template.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_message_template.biz_type IS '业务类型 (如：order, subscription, system)';
COMMENT ON COLUMN sys_message_template.message_type IS '消息类型 (NOTIFICATION-通知 MARKETING-营销 VERIFICATION-验证 REMINDER-提醒)';
COMMENT ON COLUMN sys_message_template.template_title IS '模板标题';
COMMENT ON COLUMN sys_message_template.template_content IS '模板内容 (支持变量占位符，如 ${userName})';
COMMENT ON COLUMN sys_message_template.template_example IS '示例数据 (JSONB，用于测试预览)';
COMMENT ON COLUMN sys_message_template.VARIABLES IS '变量定义 (JSONB，定义变量名/类型/必填)';
COMMENT ON COLUMN sys_message_template.STATUS IS '状态 (ENABLED-启用 DISABLED-停用)';
COMMENT ON COLUMN sys_message_template.version IS '版本号';
COMMENT ON COLUMN sys_message_template.LANGUAGE IS '语言 (zh-CN/en-US 等)';
COMMENT ON COLUMN sys_message_template.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_message_template.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_message_template.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_message_template.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_message_template.create_time IS '创建时间';
COMMENT ON COLUMN sys_message_template.update_time IS '更新时间';
COMMENT ON COLUMN sys_message_template.is_deleted IS '逻辑删除';
-- 站内信收件箱表
CREATE TABLE sys_inbox_message (
                                   id VARCHAR ( 200 ) NOT NULL,
                                   user_id VARCHAR ( 200 ) NOT NULL,
                                   tenant_id VARCHAR ( 200 ) NOT NULL,
                                   tenant_name VARCHAR ( 100 ) NOT NULL,
                                   message_type VARCHAR ( 50 ) NOT NULL,
                                   title VARCHAR ( 200 ) NOT NULL,
                                   content TEXT NOT NULL,
                                   priority VARCHAR ( 50 ) NOT NULL,
                                   is_read BOOLEAN DEFAULT FALSE,
                                   read_time TIMESTAMP DEFAULT NULL,
                                   is_archived BOOLEAN DEFAULT FALSE,
                                   archived_time TIMESTAMP DEFAULT NULL,
                                   expire_time TIMESTAMP DEFAULT NULL,
                                   action_url VARCHAR ( 500 ) DEFAULT '',
                                   action_text VARCHAR ( 50 ) DEFAULT '',
                                   create_by VARCHAR ( 200 ) DEFAULT NULL,
                                   create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   update_by VARCHAR ( 200 ) DEFAULT NULL,
                                   update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                   PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_inbox_message IS '站内信收件箱表 - 用户个人消息 inbox';
COMMENT ON COLUMN sys_inbox_message.id IS '主键ID';
COMMENT ON COLUMN sys_inbox_message.user_id IS '用户 ID';
COMMENT ON COLUMN sys_inbox_message.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_inbox_message.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_inbox_message.message_type IS '消息类型 (SYSTEM_NOTIFICATION-系统通知 APPROVAL_NOTIFICATION-审批通知 BILLING_REMINDER-账单提醒 MARKETING_ACTIVITY-营销活动)';
COMMENT ON COLUMN sys_inbox_message.title IS '消息标题';
COMMENT ON COLUMN sys_inbox_message.content IS '消息内容';
COMMENT ON COLUMN sys_inbox_message.priority IS '优先级 (NORMAL-普通 IMPORTANT-重要 URGENT-紧急)';
COMMENT ON COLUMN sys_inbox_message.is_read IS '是否已读';
COMMENT ON COLUMN sys_inbox_message.read_time IS '阅读时间';
COMMENT ON COLUMN sys_inbox_message.is_archived IS '是否已归档';
COMMENT ON COLUMN sys_inbox_message.archived_time IS '归档时间';
COMMENT ON COLUMN sys_inbox_message.expire_time IS '过期时间 (NULL 为永不过期)';
COMMENT ON COLUMN sys_inbox_message.action_url IS '操作按钮 URL';
COMMENT ON COLUMN sys_inbox_message.action_text IS '操作按钮文案';
COMMENT ON COLUMN sys_inbox_message.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_inbox_message.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_inbox_message.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_inbox_message.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_inbox_message.create_time IS '创建时间';
COMMENT ON COLUMN sys_inbox_message.update_time IS '更新时间';
COMMENT ON COLUMN sys_inbox_message.is_deleted IS '逻辑删除';
-- 定时消息任务表
CREATE TABLE sys_message_schedule (
                                      id VARCHAR ( 200 ) NOT NULL,
                                      task_name VARCHAR ( 100 ) NOT NULL,
                                      tenant_id VARCHAR ( 200 ) DEFAULT NULL,
                                      tenant_name VARCHAR ( 100 ) DEFAULT NULL,
                                      template_id VARCHAR ( 200 ) NOT NULL,
                                      target_type VARCHAR ( 50 ) NOT NULL,
                                      target_ids VARCHAR ( 1000 ) DEFAULT '',
                                      trigger_type VARCHAR ( 50 ) NOT NULL,
                                      trigger_condition JSONB DEFAULT '{}' :: jsonb,
                                      execute_time TIMESTAMP NOT NULL,
                                      repeat_rule JSONB DEFAULT '{}' :: jsonb,
                                      STATUS VARCHAR ( 50 ) NOT NULL,
                                      executed_count INT DEFAULT 0,
                                      last_execute_time TIMESTAMP DEFAULT NULL,
                                      create_by VARCHAR ( 200 ) DEFAULT NULL,
                                      create_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                      update_by VARCHAR ( 200 ) DEFAULT NULL,
                                      update_by_name VARCHAR ( 100 ) DEFAULT NULL,
                                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                      PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_message_schedule IS '定时消息任务表 - 预约发送或周期性发送的消息';
COMMENT ON COLUMN sys_message_schedule.id IS '主键ID';
COMMENT ON COLUMN sys_message_schedule.task_name IS '任务名称';
COMMENT ON COLUMN sys_message_schedule.tenant_id IS '租户 ID';
COMMENT ON COLUMN sys_message_schedule.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_message_schedule.template_id IS '消息模板 ID';
COMMENT ON COLUMN sys_message_schedule.target_type IS '目标类型 (SPECIFIC_USER-指定用户 SPECIFIC_TENANT-指定租户 QUALIFIED_USERS-符合条件的所有用户)';
COMMENT ON COLUMN sys_message_schedule.target_ids IS '目标 ID 集合 (逗号分隔)';
COMMENT ON COLUMN sys_message_schedule.trigger_type IS '触发类型 (ONCE-单次定时 PERIODIC-周期循环 EVENT_TRIGGERED-事件触发)';
COMMENT ON COLUMN sys_message_schedule.trigger_condition IS '触发条件 (JSONB，定义时间/事件规则)';
COMMENT ON COLUMN sys_message_schedule.execute_time IS '计划执行时间';
COMMENT ON COLUMN sys_message_schedule.repeat_rule IS '重复规则 (JSONB，如{"type":"daily","interval":1})';
COMMENT ON COLUMN sys_message_schedule.STATUS IS '状态 (PENDING-待执行 EXECUTING-执行中 COMPLETED-已完成 CANCELLED-已取消)';
COMMENT ON COLUMN sys_message_schedule.executed_count IS '已执行次数';
COMMENT ON COLUMN sys_message_schedule.last_execute_time IS '最后执行时间';
COMMENT ON COLUMN sys_message_schedule.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_message_schedule.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_message_schedule.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_message_schedule.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_message_schedule.create_time IS '创建时间';
COMMENT ON COLUMN sys_message_schedule.update_time IS '更新时间';
COMMENT ON COLUMN sys_message_schedule.is_deleted IS '逻辑删除';
-- =============================================================================
-- 8. 数据分析报表 (Analytics & Reporting) - 后续扩展
-- =============================================================================
-- 报表定义表
-- CREATE TABLE sys_report_definition (
--     id                  BIGINT          NOT NULL,
--     report_code         VARCHAR(100)    NOT NULL,
--     report_name         VARCHAR(100)    NOT NULL,
--     tenant_id           BIGINT          DEFAULT 0,
--     biz_domain          VARCHAR(50)     NOT NULL,
--     report_type         SMALLINT        DEFAULT 1,
--     data_source_type    SMALLINT        DEFAULT 1,
--     data_source_config  JSONB           NOT NULL,
--     dimensions          JSONB           DEFAULT '[]'::jsonb,
--     metrics             JSONB           DEFAULT '[]'::jsonb,
--     filters             JSONB           DEFAULT '[]'::jsonb,
--     chart_config        JSONB           DEFAULT '{}'::jsonb,
--     refresh_type        SMALLINT        DEFAULT 1,
--     refresh_interval    INT             DEFAULT 3600,
--     cache_expire        INT             DEFAULT 1800,
--     status              SMALLINT        DEFAULT 1,
--     sort_order          INT             DEFAULT 0,
--     is_public           BOOLEAN         DEFAULT FALSE,
--     visible_roles       JSONB           DEFAULT '[]'::jsonb,
--     create_by           BIGINT          DEFAULT 0,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     is_deleted          SMALLINT        DEFAULT 0,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_report_definition IS '报表定义表 - 定义报表的结构、数据源、维度指标';
-- COMMENT ON COLUMN sys_report_definition.id IS '主键 ID';
-- COMMENT ON COLUMN sys_report_definition.report_code IS '报表编码 (如：revenue_daily, tenant_growth)';
-- COMMENT ON COLUMN sys_report_definition.report_name IS '报表名称';
-- COMMENT ON COLUMN sys_report_definition.tenant_id IS '所属租户 ID (0 为系统报表)';
-- COMMENT ON COLUMN sys_report_definition.biz_domain IS '业务域 (如：billing, user, resource)';
-- COMMENT ON COLUMN sys_report_definition.report_type IS '报表类型 (1-列表 2-图表 3-透视表 4-仪表盘)';
-- COMMENT ON COLUMN sys_report_definition.data_source_type IS '数据源类型 (1-SQL 查询 2-API 接口 3-聚合表)';
-- COMMENT ON COLUMN sys_report_definition.data_source_config IS '数据源配置 (JSONB，SQL/API 定义)';
-- COMMENT ON COLUMN sys_report_definition.dimensions IS '维度定义 (JSONB，如时间/地区/套餐类型)';
-- COMMENT ON COLUMN sys_report_definition.metrics IS '指标定义 (JSONB，如金额/数量/增长率)';
-- COMMENT ON COLUMN sys_report_definition.filters IS '筛选条件 (JSONB，默认过滤条件)';
-- COMMENT ON COLUMN sys_report_definition.chart_config IS '图表配置 (JSONB，ECharts/AntV 配置)';
-- COMMENT ON COLUMN sys_report_definition.refresh_type IS '刷新类型 (1-实时 2-定时 3-手动)';
-- COMMENT ON COLUMN sys_report_definition.refresh_interval IS '刷新间隔 (秒)';
-- COMMENT ON COLUMN sys_report_definition.cache_expire IS '缓存过期时间 (秒)';
-- COMMENT ON COLUMN sys_report_definition.status IS '状态 (1-启用 0-停用)';
-- COMMENT ON COLUMN sys_report_definition.sort_order IS '排序';
-- COMMENT ON COLUMN sys_report_definition.is_public IS '是否公开';
-- COMMENT ON COLUMN sys_report_definition.visible_roles IS '可见角色 (JSONB，角色 ID 列表)';
-- COMMENT ON COLUMN sys_report_definition.create_by IS '创建人 ID';
-- COMMENT ON COLUMN sys_report_definition.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_report_definition.update_time IS '更新时间';
-- COMMENT ON COLUMN sys_report_definition.is_deleted IS '逻辑删除';
--
-- -- 报表实例数据表
-- CREATE TABLE sys_report_instance (
--     id                  BIGINT          NOT NULL,
--     report_id           BIGINT          NOT NULL,
--     tenant_id           BIGINT          NOT NULL,
--     snapshot_time       TIMESTAMP       NOT NULL,
--     data_period_start   TIMESTAMP       DEFAULT NULL,
--     data_period_end     TIMESTAMP       DEFAULT NULL,
--     row_data            JSONB           NOT NULL,
--     summary_data        JSONB           DEFAULT '{}'::jsonb,
--     total_rows          INT             DEFAULT 0,
--     total_pages         INT             DEFAULT 0,
--     generate_duration   INT             DEFAULT 0,
--     cache_expire_time   TIMESTAMP       DEFAULT NULL,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_report_instance IS '报表实例数据表 - 存储生成的报表数据快照';
-- COMMENT ON COLUMN sys_report_instance.id IS '主键 ID';
-- COMMENT ON COLUMN sys_report_instance.report_id IS '关联报表定义 ID';
-- COMMENT ON COLUMN sys_report_instance.tenant_id IS '租户 ID';
-- COMMENT ON COLUMN sys_report_instance.snapshot_time IS '数据快照时间';
-- COMMENT ON COLUMN sys_report_instance.data_period_start IS '数据起始时间';
-- COMMENT ON COLUMN sys_report_instance.data_period_end IS '数据结束时间';
-- COMMENT ON COLUMN sys_report_instance.row_data IS '明细数据 (JSONB 数组)';
-- COMMENT ON COLUMN sys_report_instance.summary_data IS '汇总数据 (JSONB，如总计/平均值)';
-- COMMENT ON COLUMN sys_report_instance.total_rows IS '总行数';
-- COMMENT ON COLUMN sys_report_instance.total_pages IS '总页数';
-- COMMENT ON COLUMN sys_report_instance.generate_duration IS '生成耗时 (毫秒)';
-- COMMENT ON COLUMN sys_report_instance.cache_expire_time IS '缓存过期时间';
-- COMMENT ON COLUMN sys_report_instance.create_time IS '创建时间';
--
-- -- 仪表板配置表（可删除）
-- CREATE TABLE sys_dashboard (
--     id                  BIGINT          NOT NULL,
--     dashboard_code      VARCHAR(100)    NOT NULL,
--     dashboard_name      VARCHAR(100)    NOT NULL,
--     tenant_id           BIGINT          DEFAULT 0,
--     description         VARCHAR(500)    DEFAULT '',
--     layout_config       JSONB           NOT NULL,
--     widgets             JSONB           NOT NULL,
--     refresh_interval    INT             DEFAULT 60,
--     theme               VARCHAR(50)     DEFAULT 'light',
--     status              SMALLINT        DEFAULT 1,
--     is_public           BOOLEAN         DEFAULT FALSE,
--     visible_roles       JSONB           DEFAULT '[]'::jsonb,
--     create_by           BIGINT          DEFAULT 0,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     is_deleted          SMALLINT        DEFAULT 0,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_dashboard IS '仪表板配置表 - 定义数据看板的布局和组件';
-- COMMENT ON COLUMN sys_dashboard.id IS '主键 ID';
-- COMMENT ON COLUMN sys_dashboard.dashboard_code IS '仪表板编码';
-- COMMENT ON COLUMN sys_dashboard.dashboard_name IS '仪表板名称';
-- COMMENT ON COLUMN sys_dashboard.tenant_id IS '所属租户 ID (0 为系统仪表板)';
-- COMMENT ON COLUMN sys_dashboard.description IS '描述';
-- COMMENT ON COLUMN sys_dashboard.layout_config IS '布局配置 (JSONB，行列数/栅格系统)';
-- COMMENT ON COLUMN sys_dashboard.widgets IS '组件列表 (JSONB，每个组件的位置/大小/关联报表)';
-- COMMENT ON COLUMN sys_dashboard.refresh_interval IS '刷新间隔 (秒)';
-- COMMENT ON COLUMN sys_dashboard.theme IS '主题 (light/dark)';
-- COMMENT ON COLUMN sys_dashboard.status IS '状态 (1-启用 0-停用)';
-- COMMENT ON COLUMN sys_dashboard.is_public IS '是否公开';
-- COMMENT ON COLUMN sys_dashboard.visible_roles IS '可见角色';
-- COMMENT ON COLUMN sys_dashboard.create_by IS '创建人 ID';
-- COMMENT ON COLUMN sys_dashboard.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_dashboard.update_time IS '更新时间';
-- COMMENT ON COLUMN sys_dashboard.is_deleted IS '逻辑删除';
--
-- -- 数据导出任务表（可删除）
-- CREATE TABLE sys_export_task (
--     id                  BIGINT          NOT NULL,
--     task_no             VARCHAR(64)     NOT NULL,
--     tenant_id           BIGINT          NOT NULL,
--     export_type         SMALLINT        DEFAULT 1,
--     source_id           BIGINT          DEFAULT 0,
--     file_name           VARCHAR(255)    NOT NULL,
--     file_format         SMALLINT        DEFAULT 1,
--     file_size           BIGINT          DEFAULT 0,
--     row_count           INT             DEFAULT 0,
--     export_status       SMALLINT        DEFAULT 0,
--     progress            INT             DEFAULT 0,
--     error_msg           VARCHAR(500)    DEFAULT '',
--     download_url        VARCHAR(500)    DEFAULT '',
--     download_count      INT             DEFAULT 0,
--     expire_time         TIMESTAMP       DEFAULT NULL,
--     create_by           BIGINT          DEFAULT 0,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     finish_time         TIMESTAMP       DEFAULT NULL,
--     is_deleted          SMALLINT        DEFAULT 0,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_export_task IS '数据导出任务表 - 异步导出 Excel/CSV 文件';
-- COMMENT ON COLUMN sys_export_task.id IS '主键 ID';
-- COMMENT ON COLUMN sys_export_task.task_no IS '任务编号';
-- COMMENT ON COLUMN sys_export_task.tenant_id IS '租户 ID';
-- COMMENT ON COLUMN sys_export_task.export_type IS '导出类型 (1-报表数据 2-原始数据 3-自定义查询)';
-- COMMENT ON COLUMN sys_export_task.source_id IS '来源 ID (报表 ID/其他业务 ID)';
-- COMMENT ON COLUMN sys_export_task.file_name IS '文件名';
-- COMMENT ON COLUMN sys_export_task.file_format IS '文件格式 (1-Excel 2-Csv 3-Pdf)';
-- COMMENT ON COLUMN sys_export_task.file_size IS '文件大小 (字节)';
-- COMMENT ON COLUMN sys_export_task.row_count IS '数据行数';
-- COMMENT ON COLUMN sys_export_task.export_status IS '导出状态 (0-待处理 1-处理中 2-完成 3-失败)';
-- COMMENT ON COLUMN sys_export_task.progress IS '进度百分比 (0-100)';
-- COMMENT ON COLUMN sys_export_task.error_msg IS '错误信息';
-- COMMENT ON COLUMN sys_export_task.download_url IS '下载 URL';
-- COMMENT ON COLUMN sys_export_task.download_count IS '下载次数';
-- COMMENT ON COLUMN sys_export_task.expire_time IS '过期时间 (7 天后自动清理)';
-- COMMENT ON COLUMN sys_export_task.create_by IS '创建人 ID';
-- COMMENT ON COLUMN sys_export_task.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_export_task.finish_time IS '完成时间';
-- COMMENT ON COLUMN sys_export_task.is_deleted IS '逻辑删除';
--
-- -- 数据统计聚合表 (按天汇总)
-- CREATE TABLE sys_stat_daily (
--     id                  BIGINT          NOT NULL,
--     tenant_id           BIGINT          NOT NULL,
--     stat_date           DATE            NOT NULL,
--     stat_type           VARCHAR(50)     NOT NULL,
--     stat_dimension      VARCHAR(100)    DEFAULT '',
--     dimension_value     VARCHAR(500)    DEFAULT '',
--     metric_code         VARCHAR(50)     NOT NULL,
--     metric_value        DECIMAL(20,4)   DEFAULT 0,
--     metric_delta        DECIMAL(20,4)   DEFAULT 0,
--     compare_value       DECIMAL(20,4)   DEFAULT 0,
--     compare_ratio       DECIMAL(10,4)   DEFAULT 0,
--     ext_data            JSONB           DEFAULT '{}'::jsonb,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_stat_daily IS '数据统计聚合表 - 按天汇总的关键指标';
-- COMMENT ON COLUMN sys_stat_daily.id IS '主键 ID';
-- COMMENT ON COLUMN sys_stat_daily.tenant_id IS '租户 ID';
-- COMMENT ON COLUMN sys_stat_daily.stat_date IS '统计日期';
-- COMMENT ON COLUMN sys_stat_daily.stat_type IS '统计类型 (如：revenue, user, order, resource)';
-- COMMENT ON COLUMN sys_stat_daily.stat_dimension IS '统计维度 (如：package_type, industry)';
-- COMMENT ON COLUMN sys_stat_daily.dimension_value IS '维度值';
-- COMMENT ON COLUMN sys_stat_daily.metric_code IS '指标编码 (如：total_amount, new_count)';
-- COMMENT ON COLUMN sys_stat_daily.metric_value IS '指标值';
-- COMMENT ON COLUMN sys_stat_daily.metric_delta IS '较昨日增量';
-- COMMENT ON COLUMN sys_stat_daily.compare_value IS '对比值 (上周同期/上月同期)';
-- COMMENT ON COLUMN sys_stat_daily.compare_ratio IS '对比增长率';
-- COMMENT ON COLUMN sys_stat_daily.ext_data IS '扩展数据 (JSONB)';
-- COMMENT ON COLUMN sys_stat_daily.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_stat_daily.update_time IS '更新时间';
--
-- -- 数据统计聚合表 (按月汇总)
-- CREATE TABLE sys_stat_monthly (
--     id                  BIGINT          NOT NULL,
--     tenant_id           BIGINT          NOT NULL,
--     stat_month          DATE            NOT NULL,
--     stat_type           VARCHAR(50)     NOT NULL,
--     stat_dimension      VARCHAR(100)    DEFAULT '',
--     dimension_value     VARCHAR(500)    DEFAULT '',
--     metric_code         VARCHAR(50)     NOT NULL,
--     metric_value        DECIMAL(20,4)   DEFAULT 0,
--     metric_delta        DECIMAL(20,4)   DEFAULT 0,
--     compare_value       DECIMAL(20,4)   DEFAULT 0,
--     compare_ratio       DECIMAL(10,4)   DEFAULT 0,
--     ext_data            JSONB           DEFAULT '{}'::jsonb,
--     create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
--     PRIMARY KEY (id)
-- );
-- COMMENT ON TABLE sys_stat_monthly IS '数据统计聚合表 - 按月汇总的关键指标';
-- COMMENT ON COLUMN sys_stat_monthly.id IS '主键 ID';
-- COMMENT ON COLUMN sys_stat_monthly.tenant_id IS '租户 ID';
-- COMMENT ON COLUMN sys_stat_monthly.stat_month IS '统计月份 (月初日期)';
-- COMMENT ON COLUMN sys_stat_monthly.stat_type IS '统计类型';
-- COMMENT ON COLUMN sys_stat_monthly.stat_dimension IS '统计维度';
-- COMMENT ON COLUMN sys_stat_monthly.dimension_value IS '维度值';
-- COMMENT ON COLUMN sys_stat_monthly.metric_code IS '指标编码';
-- COMMENT ON COLUMN sys_stat_monthly.metric_value IS '指标值';
-- COMMENT ON COLUMN sys_stat_monthly.metric_delta IS '较上月增量';
-- COMMENT ON COLUMN sys_stat_monthly.compare_value IS '对比值 (去年同期)';
-- COMMENT ON COLUMN sys_stat_monthly.compare_ratio IS '对比增长率';
-- COMMENT ON COLUMN sys_stat_monthly.ext_data IS '扩展数据';
-- COMMENT ON COLUMN sys_stat_monthly.create_time IS '创建时间';
-- COMMENT ON COLUMN sys_stat_monthly.update_time IS '更新时间';
-- 租户表增加冗余字段
-- 按照依赖顺序删除
DELETE
FROM
    sys_user_role_rel;
DELETE
FROM
    sys_user_tenant_rel;
DELETE
FROM
    sys_role_dept_rel;
DELETE
FROM
    sys_user_group_rel;
DELETE
FROM
    sys_notice_user_rel;
DELETE
FROM
    sys_resource_usage;
DELETE
FROM
    sys_tenant_quota_adjustment;
DELETE
FROM
    prod_package_quota;
DELETE
FROM
    sys_perm_policy;
DELETE
FROM
    sys_inbox_message;
DELETE
FROM
    sys_message_schedule;
DELETE
FROM
    sys_data_audit_log;
DELETE
FROM
    sys_oper_log;
DELETE
FROM
    sys_login_log;
DELETE
FROM
    sys_tenant_subscription;
DELETE
FROM
    bill_order;
DELETE
FROM
    bill_invoice;
DELETE
FROM
    sys_user_token;
DELETE
FROM
    sys_file;
DELETE
FROM
    sys_notice;
DELETE
FROM
    sys_message_template;
DELETE
FROM
    sys_dept;
DELETE
FROM
    sys_post;
DELETE
FROM
    sys_user_group;
DELETE
FROM
    sys_role;
DELETE
FROM
    sys_perm;
DELETE
FROM
    sys_menu;
DELETE
FROM
    sys_dict_item;
DELETE
FROM
    sys_dict;
DELETE
FROM
    sys_form_config;
DELETE
FROM
    sys_datasource_config;
DELETE
FROM
    sys_print_template;
DELETE
FROM
    sys_user;
DELETE
FROM
    sys_tenant;
DELETE
FROM
    prod_package;