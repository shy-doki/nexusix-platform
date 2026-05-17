-- 租户信息表
CREATE TABLE sys_tenant (
                            id VARCHAR ( 200 ) NOT NULL,
                            tenant_name VARCHAR ( 50 ) NOT NULL,
                            tenant_type VARCHAR ( 50 ) NOT NULL,
                            tenant_logo_url VARCHAR ( 200 ) DEFAULT NULL,
                            tenant_desc VARCHAR ( 200 ) DEFAULT NULL,
                            tenant_code VARCHAR ( 100 ) NOT NULL,
                            parent_code VARCHAR ( 100 ) DEFAULT NULL,
                            parent_name VARCHAR ( 50 ) DEFAULT NULL,
                            ancestors VARCHAR ( 500 ) DEFAULT NULL,
                            contact_name VARCHAR ( 50 ) DEFAULT NULL,
                            contact_phone VARCHAR ( 20 ) DEFAULT NULL,
                            status VARCHAR ( 50 ) NOT NULL,
                            expire_time TIMESTAMP DEFAULT NULL,
                            package_code VARCHAR ( 100 ) DEFAULT NULL,
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
COMMENT ON COLUMN sys_tenant.id IS '主键ID';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_type IS '租户类型';
COMMENT ON COLUMN sys_tenant.tenant_logo_url IS '租户logo';
COMMENT ON COLUMN sys_tenant.tenant_desc IS '租户描述';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户唯一编码';
COMMENT ON COLUMN sys_tenant.parent_code IS '父租户编码';
COMMENT ON COLUMN sys_tenant.ancestors IS '祖级列表 (物化路径，如 0,100,200)';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.status IS '状态';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_code IS '当前主套餐编码';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性(JSONB，存储行业特定配置)';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人编码';
COMMENT ON COLUMN sys_tenant.update_by IS '更新人编码';
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
                                         tenant_code VARCHAR ( 100 ) NOT NULL,
                                         tenant_name VARCHAR ( 100 ) NOT NULL,
                                         package_code VARCHAR ( 100 ) NOT NULL,
                                         package_name VARCHAR ( 100 ) NOT NULL,
                                         subscription_type VARCHAR ( 50 ) NOT NULL,
                                         start_time TIMESTAMP NOT NULL,
                                         end_time TIMESTAMP NOT NULL,
                                         status VARCHAR ( 50 ) NOT NULL,
                                         is_auto_renew BOOLEAN DEFAULT FALSE,
                                         source_type VARCHAR ( 50 ) NOT NULL,
                                         parent_grant_code VARCHAR ( 100 ) DEFAULT NULL,
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
COMMENT ON COLUMN sys_tenant_subscription.id IS '主键ID';
COMMENT ON COLUMN sys_tenant_subscription.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_tenant_subscription.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant_subscription.package_code IS '套餐产品编码';
COMMENT ON COLUMN sys_tenant_subscription.package_name IS '套餐产品名称';
COMMENT ON COLUMN sys_tenant_subscription.subscription_type IS '订阅类型';
COMMENT ON COLUMN sys_tenant_subscription.start_time IS '订阅开始时间';
COMMENT ON COLUMN sys_tenant_subscription.end_time IS '订阅结束时间';
COMMENT ON COLUMN sys_tenant_subscription.status IS '状态';
COMMENT ON COLUMN sys_tenant_subscription.is_auto_renew IS '是否自动续费';
COMMENT ON COLUMN sys_tenant_subscription.source_type IS '来源类型';
COMMENT ON COLUMN sys_tenant_subscription.parent_grant_code IS '父租户分配记录编码';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant_subscription.create_by IS '创建人 ID';
COMMENT ON COLUMN sys_tenant_subscription.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant_subscription.update_by IS '更新人 ID';
COMMENT ON COLUMN sys_tenant_subscription.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant_subscription.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_subscription.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant_subscription.is_deleted IS '逻辑删除';
-- 用户基础表
CREATE TABLE sys_user (
                          id VARCHAR ( 200 ) NOT NULL,
                          username VARCHAR ( 50 ) NOT NULL,
                          user_code VARCHAR ( 100 ) NOT NULL,
                          password VARCHAR ( 100 ) NOT NULL,
                          nickname VARCHAR ( 50 ) NOT NULL,
                          email VARCHAR ( 100 ) NOT NULL,
                          phone VARCHAR ( 20 ) NOT NULL,
                          avatar VARCHAR ( 255 ) DEFAULT NULL,
                          status VARCHAR ( 50 ) NOT NULL,
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
COMMENT ON COLUMN sys_user.user_code IS '用户编码';
COMMENT ON COLUMN sys_user.password IS '加密密码';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像地址';
COMMENT ON COLUMN sys_user.status IS '全局状态';
COMMENT ON COLUMN sys_user.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登录时间';
COMMENT ON COLUMN sys_user.create_by IS '创建人编码';
COMMENT ON COLUMN sys_user.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user.update_by IS '更新人编码';
COMMENT ON COLUMN sys_user.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除';
-- 用户 - 租户关联表
CREATE TABLE sys_user_tenant_rel (
                                     id VARCHAR ( 200 ) NOT NULL,
                                     user_code VARCHAR ( 100 ) NOT NULL,
                                     tenant_code VARCHAR ( 100 ) NOT NULL,
                                     dept_code VARCHAR ( 100 ) DEFAULT NULL,
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
COMMENT ON COLUMN sys_user_tenant_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_tenant_rel.dept_code IS '主部门编码';
COMMENT ON COLUMN sys_user_tenant_rel.is_admin IS '是否租户管理员';
COMMENT ON COLUMN sys_user_tenant_rel.join_time IS '加入时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_default IS '是否默认租户';
COMMENT ON COLUMN sys_user_tenant_rel.create_by IS '创建人编码';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_tenant_rel.update_by IS '更新人编码';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_tenant_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_tenant_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_deleted IS '逻辑删除';
-- 权限/资源表
CREATE TABLE sys_perm (
                          id                  VARCHAR(200)    NOT NULL,
                          perm_name           VARCHAR(50)     NOT NULL,
                          perm_desc           VARCHAR(500)    DEFAULT NULL,
                          perm_code           VARCHAR(100)    NOT NULL,
                          perm_key            VARCHAR(100)    NOT NULL,
                          perm_type           VARCHAR(50)     NOT NULL,
                          parent_code           VARCHAR(100)    DEFAULT NULL,
                          parent_name         VARCHAR(50)     DEFAULT NULL,
                          path                VARCHAR(200)    DEFAULT NULL,
                          table_name          VARCHAR(64)     DEFAULT NULL,
                          table_desc          VARCHAR(100)     DEFAULT NULL,
                          field_name          VARCHAR(64)     DEFAULT NULL,
                          field_desc          VARCHAR(100)     DEFAULT NULL,
                          operate_type        VARCHAR(20)     DEFAULT NULL,
                          status              VARCHAR(50)     NOT NULL,
                          create_by           VARCHAR(200)    DEFAULT NULL,
                          create_by_name      VARCHAR(100)    DEFAULT NULL,
                          update_by           VARCHAR(200)    DEFAULT NULL,
                          update_by_name      VARCHAR(100)    DEFAULT NULL,
                          create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                          update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                          is_deleted          VARCHAR(50)     DEFAULT NULL,
                          PRIMARY KEY (id)
);

COMMENT ON TABLE sys_perm IS '权限/资源表-定义系统所有可授权资源（含字段级权限）';
COMMENT ON COLUMN sys_perm.id IS '主键ID';
COMMENT ON COLUMN sys_perm.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm.perm_desc IS '权限描述';
COMMENT ON COLUMN sys_perm.perm_code IS '权限标识';
COMMENT ON COLUMN sys_perm.perm_key IS '权限键值(只可超级管理员变更，且变更后需要重启服务并修改相关常量)';
COMMENT ON COLUMN sys_perm.perm_type IS '权限类型：MENU-菜单 / BUTTON-按钮 / API-接口 / FIELD_GROUP-字段组 / FIELD-字段';
COMMENT ON COLUMN sys_perm.parent_code IS '父权限编码';
COMMENT ON COLUMN sys_perm.parent_name IS '父权限名称';
COMMENT ON COLUMN sys_perm.path IS '资源路径';
COMMENT ON COLUMN sys_perm.table_name IS '关联数据库表名';
COMMENT ON COLUMN sys_perm.table_desc IS '关联数据库表描述';
COMMENT ON COLUMN sys_perm.field_name IS '关联数据库字段名';
COMMENT ON COLUMN sys_perm.field_desc IS '关联数据库字段描述';
COMMENT ON COLUMN sys_perm.operate_type IS '操作类型';
COMMENT ON COLUMN sys_perm.status IS '状态';
COMMENT ON COLUMN sys_perm.create_by IS '创建人编码';
COMMENT ON COLUMN sys_perm.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm.update_by IS '更新人编码';
COMMENT ON COLUMN sys_perm.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm.is_deleted IS '逻辑删除';
-- 权限策略控制表
CREATE TABLE sys_perm_policy (
                                 id VARCHAR ( 200 ) NOT NULL,
                                 policy_code VARCHAR ( 100 ) NOT NULL,
                                 target_type VARCHAR ( 50 ) NOT NULL,
                                 target_code VARCHAR ( 100 ) NOT NULL,
                                 target_name VARCHAR ( 100 ) NOT NULL,
                                 perm_code VARCHAR ( 100 ) NOT NULL,
                                 perm_name VARCHAR ( 50 ) NOT NULL,
                                 action VARCHAR ( 50 ) NOT NULL,
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
COMMENT ON COLUMN sys_perm_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_perm_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_perm_policy.target_code IS '目标编码';
COMMENT ON COLUMN sys_perm_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_perm_policy.perm_code IS '关联权限编码';
COMMENT ON COLUMN sys_perm_policy.perm_name IS '关联权限名称';
COMMENT ON COLUMN sys_perm_policy.action IS '动作';
COMMENT ON COLUMN sys_perm_policy.create_by IS '创建人编码';
COMMENT ON COLUMN sys_perm_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm_policy.update_by IS '更新人编码';
COMMENT ON COLUMN sys_perm_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm_policy.is_deleted IS '逻辑删除';
-- 用户权限表
CREATE TABLE sys_user_perm_rel (
                                   id VARCHAR(64) NOT NULL,
                                   user_code VARCHAR(100) NOT NULL,
                                   user_name VARCHAR(100) NOT NULL,
                                   nick_name VARCHAR(100) NOT NULL,
                                   perm_code VARCHAR(100) NOT NULL,
                                   perm_name VARCHAR(100) NOT NULL,
                                   tenant_code VARCHAR(100) NOT NULL,
                                   tenant_name VARCHAR(100) NOT NULL,
                                   action VARCHAR(20) NOT NULL,
                                   policy_code VARCHAR(100) NOT NULL,
                                   policy_level VARCHAR(20) NOT NULL,
                                   table_name          VARCHAR(64)     NOT NULL,
                                   table_desc          VARCHAR(100)    DEFAULT NULL,
                                   access_type         VARCHAR(20)     NOT NULL,
                                   field_operates      JSONB           NOT NULL DEFAULT '[]'::jsonb,
                                   field_un_operate    JSONB           NOT NULL DEFAULT '[]'::jsonb,
                                   create_by           VARCHAR(200)    DEFAULT NULL,
                                   create_by_name      VARCHAR(100)    DEFAULT NULL,
                                   update_by           VARCHAR(200)    DEFAULT NULL,
                                   update_by_name      VARCHAR(100)    DEFAULT NULL,
                                   create_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                   update_time         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted          VARCHAR(50)     DEFAULT NULL,
                                   PRIMARY KEY (id)
);

COMMENT ON TABLE sys_user_perm_rel IS '用户生效权限表(策略计算结果快照)';
COMMENT ON COLUMN sys_user_perm_rel.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_perm_rel.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_perm_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_perm_rel.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_perm_rel.nick_name IS '用户昵称';
COMMENT ON COLUMN sys_user_perm_rel.perm_code IS '权限编码';
COMMENT ON COLUMN sys_user_perm_rel.perm_name IS '权限名称';
COMMENT ON COLUMN sys_user_perm_rel.action IS '生效动作: ALLOW / DENY';
COMMENT ON COLUMN sys_user_perm_rel.policy_level IS '策略层级: SYSTEM / TENANT / ROLE / USER';
COMMENT ON COLUMN sys_user_perm_rel.policy_code IS '来源策略编码';
COMMENT ON COLUMN sys_user_perm_rel.table_name IS '数据库表名';
COMMENT ON COLUMN sys_user_perm_rel.table_desc IS '表描述';
COMMENT ON COLUMN sys_user_perm_rel.access_type IS '访问类型';
COMMENT ON COLUMN sys_user_perm_rel.field_operates IS '可操作字段';
COMMENT ON COLUMN sys_user_perm_rel.field_un_operate IS '不可操作字段';
COMMENT ON COLUMN sys_user_perm_rel.create_by IS '创建人编码';
COMMENT ON COLUMN sys_user_perm_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_perm_rel.update_by IS '更新人编码';
COMMENT ON COLUMN sys_user_perm_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_perm_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_perm_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_perm_rel.is_deleted IS '逻辑删除';
-- 角色表
CREATE TABLE sys_role (
                          id VARCHAR ( 200 ) NOT NULL,
                          role_name VARCHAR ( 50 ) NOT NULL,
                          role_desc VARCHAR ( 500 ) DEFAULT NULL,
                          role_code VARCHAR ( 100 ) NOT NULL,
                          tenant_code VARCHAR ( 100 ) NOT NULL,
                          tenant_name VARCHAR ( 100 ) NOT NULL,
                          data_scope VARCHAR ( 50 ) NOT NULL,
                          status VARCHAR ( 50 ) NOT NULL,
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
COMMENT ON COLUMN sys_role.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_role.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围';
COMMENT ON COLUMN sys_role.status IS '状态';
COMMENT ON COLUMN sys_role.sort_order IS '排序顺序';
COMMENT ON COLUMN sys_role.create_by IS '创建人编码';
COMMENT ON COLUMN sys_role.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role.update_by IS '更新人编码';
COMMENT ON COLUMN sys_role.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除';
-- 角色策略控制表
CREATE TABLE sys_role_policy (
                                 id VARCHAR ( 200 ) NOT NULL,
                                 target_type VARCHAR ( 50 ) NOT NULL,
                                 target_code VARCHAR ( 100 ) NOT NULL,
                                 target_name VARCHAR ( 100 ) NOT NULL,
                                 role_code VARCHAR ( 100 ) NOT NULL,
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
COMMENT ON COLUMN sys_role_policy.target_code IS '目标编码';
COMMENT ON COLUMN sys_role_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_role_policy.role_code IS '关联角色编码';
COMMENT ON COLUMN sys_role_policy.role_name IS '关联角色名称';
COMMENT ON COLUMN sys_role_policy.action IS '动作';
COMMENT ON COLUMN sys_role_policy.priority IS '优先级';
COMMENT ON COLUMN sys_role_policy.inheritance_enabled IS '是否向下继承';
COMMENT ON COLUMN sys_role_policy.create_by IS '创建人编码';
COMMENT ON COLUMN sys_role_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role_policy.update_by IS '更新人编码';
COMMENT ON COLUMN sys_role_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_policy.is_deleted IS '逻辑删除';
-- 用户角色关联表
CREATE TABLE sys_user_role_rel (
                                   id VARCHAR ( 200 ) NOT NULL,
                                   user_code VARCHAR ( 100 ) NOT NULL,
                                   role_code VARCHAR ( 100 ) NOT NULL,
                                   tenant_code VARCHAR ( 100 ) NOT NULL,
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
COMMENT ON COLUMN sys_user_role_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_role_rel.role_code IS '角色编码';
COMMENT ON COLUMN sys_user_role_rel.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_role_rel.effective_time IS '角色生效时间';
COMMENT ON COLUMN sys_user_role_rel.expire_time IS '角色失效时间';
COMMENT ON COLUMN sys_user_role_rel.create_by IS '创建人编码';
COMMENT ON COLUMN sys_user_role_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_role_rel.update_by IS '更新人编码';
COMMENT ON COLUMN sys_user_role_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_role_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_role_rel.is_deleted IS '逻辑删除';
-- 用户 Token 记录表
CREATE TABLE sys_user_token (
                                id VARCHAR ( 200 ) NOT NULL,
                                user_code VARCHAR ( 100 ) NOT NULL,
                                user_name VARCHAR ( 50 ) NOT NULL,
                                tenant_code VARCHAR ( 100 ) NOT NULL,
                                tenant_name VARCHAR ( 50 ) NOT NULL,
                                token VARCHAR ( 255 ) NOT NULL,
                                device_info VARCHAR ( 200 ) DEFAULT '',
                                login_ip VARCHAR ( 50 ) DEFAULT '',
                                login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                expire_time TIMESTAMP NOT NULL,
                                status VARCHAR ( 50 ) NOT NULL,
                                is_deleted VARCHAR ( 50 ) DEFAULT NULL,
                                PRIMARY KEY ( id )
);
COMMENT ON TABLE sys_user_token IS '用户Token记录表-用于多端登录管理和强制下线';
COMMENT ON COLUMN sys_user_token.id IS '主键ID';
COMMENT ON COLUMN sys_user_token.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_token.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_token.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_token.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_token.token IS '登录令牌';
COMMENT ON COLUMN sys_user_token.device_info IS '设备信息';
COMMENT ON COLUMN sys_user_token.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user_token.login_time IS '登录时间';
COMMENT ON COLUMN sys_user_token.expire_time IS '过期时间';
COMMENT ON COLUMN sys_user_token.status IS '状态';
COMMENT ON COLUMN sys_user_token.is_deleted IS '逻辑删除';