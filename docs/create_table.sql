CREATE TABLE sys_file (
                          id BIGINT NOT NULL,
                          tenant_id VARCHAR(20) NOT NULL,
                          tenant_code VARCHAR(50) NOT NULL,
                          tenant_name VARCHAR(100) NOT NULL,
                          file_name VARCHAR(255) NOT NULL,
                          original_name VARCHAR(255) NOT NULL,
                          file_path VARCHAR(500) NOT NULL,
                          file_url VARCHAR(500) NOT NULL,
                          file_size BIGINT NOT NULL,
                          file_type VARCHAR(50) NOT NULL,
                          mime_type VARCHAR(100) NOT NULL,
                          biz_type VARCHAR(50) NOT NULL,
                          upload_by_id VARCHAR(20) NOT NULL,
                          upload_by_code VARCHAR(50) NOT NULL,
                          upload_by_name VARCHAR(50) NOT NULL,
                          upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_by_id VARCHAR(20) NOT NULL,
                          update_by_code VARCHAR(50) NOT NULL,
                          update_by_name VARCHAR(50) NOT NULL,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          PRIMARY KEY (id)
);
COMMENT ON TABLE sys_file IS '文件资源表-存储上传的文件信息';
COMMENT ON COLUMN sys_file.id IS '主键ID';
COMMENT ON COLUMN sys_file.tenant_id IS '所属租户ID';
COMMENT ON COLUMN sys_file.tenant_code IS '所属租户编码';
COMMENT ON COLUMN sys_file.tenant_name IS '所属租户名称';
COMMENT ON COLUMN sys_file.file_name IS '文件名称';
COMMENT ON COLUMN sys_file.original_name IS '原始文件名';
COMMENT ON COLUMN sys_file.file_path IS '文件存储路径';
COMMENT ON COLUMN sys_file.file_url IS '文件访问URL';
COMMENT ON COLUMN sys_file.file_size IS '文件大小';
COMMENT ON COLUMN sys_file.file_type IS '文件类型';
COMMENT ON COLUMN sys_file.mime_type IS 'MIME类型';
COMMENT ON COLUMN sys_file.biz_type IS '业务类型分类';
COMMENT ON COLUMN sys_file.upload_by_id IS '上传人ID';
COMMENT ON COLUMN sys_file.upload_by_code IS '上传人编码';
COMMENT ON COLUMN sys_file.upload_by_name IS '上传人名称';
COMMENT ON COLUMN sys_file.upload_time IS '上传时间';
COMMENT ON COLUMN sys_file.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_file.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_file.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_file.update_time IS '更新时间';
COMMENT ON COLUMN sys_file.is_deleted IS '逻辑删除';
-- 租户信息表
CREATE TABLE sys_tenant (
                            id BIGINT NOT NULL,
                            tenant_name VARCHAR(100) NOT NULL,
                            tenant_type VARCHAR(50) NOT NULL,
                            tenant_logo_url VARCHAR(500) NOT NULL,
                            tenant_desc VARCHAR(500) NOT NULL,
                            tenant_code VARCHAR(50) NOT NULL,
                            parent_id VARCHAR(20) NOT NULL,
                            parent_code VARCHAR(50) NOT NULL,
                            parent_name VARCHAR(100) NOT NULL,
                            ancestors VARCHAR(1000) NOT NULL,
                            contact_name VARCHAR(50) NOT NULL,
                            contact_phone VARCHAR(20) NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            expire_time TIMESTAMP NOT NULL,
                            package_code VARCHAR(50) NOT NULL,
                            package_name VARCHAR(100) NOT NULL,
                            ext_attributes JSONB DEFAULT '{}'::jsonb,
                            has_children BOOLEAN NOT NULL,
                            create_by_id VARCHAR(20) NOT NULL,
                            create_by_code VARCHAR(50) NOT NULL,
                            create_by_name VARCHAR(100) NOT NULL,
                            update_by_id VARCHAR(20) NOT NULL,
                            update_by_code VARCHAR(50) NOT NULL,
                            update_by_name VARCHAR(100) NOT NULL,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_deleted VARCHAR(20) NOT NULL,
                            PRIMARY KEY (id)
);
COMMENT ON TABLE sys_tenant IS '租户信息表-存储租户基础信息，支持无限层级';
COMMENT ON COLUMN sys_tenant.id IS '主键ID';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_type IS '租户类型';
COMMENT ON COLUMN sys_tenant.tenant_logo_url IS '租户logo';
COMMENT ON COLUMN sys_tenant.tenant_desc IS '租户描述';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户唯一编码';
COMMENT ON COLUMN sys_tenant.parent_id IS '父租户ID';
COMMENT ON COLUMN sys_tenant.parent_code IS '父租户编码';
COMMENT ON COLUMN sys_tenant.parent_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant.ancestors IS '祖级列表 ';
COMMENT ON COLUMN sys_tenant.contact_name IS '联系人姓名';
COMMENT ON COLUMN sys_tenant.contact_phone IS '联系人电话';
COMMENT ON COLUMN sys_tenant.status IS '状态';
COMMENT ON COLUMN sys_tenant.expire_time IS '服务过期时间';
COMMENT ON COLUMN sys_tenant.package_code IS '当前主套餐编码';
COMMENT ON COLUMN sys_tenant.package_name IS '套餐名称';
COMMENT ON COLUMN sys_tenant.ext_attributes IS '扩展属性';
COMMENT ON COLUMN sys_tenant.has_children IS '是否有子租户';
COMMENT ON COLUMN sys_tenant.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_tenant.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_tenant.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_tenant.update_by_code IS '更新人ID';
COMMENT ON COLUMN sys_tenant.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除';


-- 租户套餐订阅表
CREATE TABLE sys_tenant_subscription (
                                         id BIGINT NOT NULL,
                                         subscription_code VARCHAR(50) NOT NULL,
                                         tenant_id VARCHAR(20) NOT NULL,
                                         tenant_code VARCHAR(50) NOT NULL,
                                         tenant_name VARCHAR(100) NOT NULL,
                                         package_id VARCHAR(20) NOT NULL,
                                         package_code VARCHAR(50) NOT NULL,
                                         package_name VARCHAR(100) NOT NULL,
                                         subscription_type VARCHAR(20) NOT NULL,
                                         start_time TIMESTAMP NOT NULL,
                                         end_time TIMESTAMP NOT NULL,
                                         status VARCHAR(20) NOT NULL,
                                         is_auto_renew BOOLEAN NOT NULL,
                                         source_type VARCHAR(20) NOT NULL,
                                         parent_grant_id VARCHAR(20) NOT NULL,
                                         parent_grant_code VARCHAR(50) NOT NULL,
                                         parent_tenant_id VARCHAR(20) NOT NULL,
                                         parent_tenant_code VARCHAR(50) NOT NULL,
                                         parent_tenant_name VARCHAR(100) NOT NULL,
                                         create_by_id VARCHAR(20) NOT NULL,
                                         create_by_code VARCHAR(50) NOT NULL,
                                         create_by_name VARCHAR(100) NOT NULL,
                                         update_by_id VARCHAR(20) NOT NULL,
                                         update_by_code VARCHAR(50) NOT NULL,
                                         update_by_name VARCHAR(100) NOT NULL,
                                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         is_deleted VARCHAR(20) NOT NULL,
                                         PRIMARY KEY (id)
);
COMMENT ON TABLE sys_tenant_subscription IS '租户套餐订阅表-记录租户购买的套餐及订阅状态';
COMMENT ON COLUMN sys_tenant_subscription.id IS '主键ID';
COMMENT ON COLUMN sys_tenant_subscription.subscription_code IS '订阅编码';
COMMENT ON COLUMN sys_tenant_subscription.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_tenant_subscription.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_tenant_subscription.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant_subscription.package_id IS '套餐产品ID';
COMMENT ON COLUMN sys_tenant_subscription.package_code IS '套餐产品编码';
COMMENT ON COLUMN sys_tenant_subscription.package_name IS '套餐产品名称';
COMMENT ON COLUMN sys_tenant_subscription.subscription_type IS '订阅类型';
COMMENT ON COLUMN sys_tenant_subscription.start_time IS '订阅开始时间';
COMMENT ON COLUMN sys_tenant_subscription.end_time IS '订阅结束时间';
COMMENT ON COLUMN sys_tenant_subscription.status IS '状态';
COMMENT ON COLUMN sys_tenant_subscription.is_auto_renew IS '是否自动续费';
COMMENT ON COLUMN sys_tenant_subscription.source_type IS '来源类型';
COMMENT ON COLUMN sys_tenant_subscription.parent_grant_id IS '父租户分配记录ID';
COMMENT ON COLUMN sys_tenant_subscription.parent_grant_code IS '父租户分配记录编码';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_id IS '父租户ID';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_code IS '父租户编码';
COMMENT ON COLUMN sys_tenant_subscription.parent_tenant_name IS '父租户名称';
COMMENT ON COLUMN sys_tenant_subscription.create_by_id IS '创建人 ID';
COMMENT ON COLUMN sys_tenant_subscription.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_tenant_subscription.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_tenant_subscription.update_by_id IS '更新人 ID';
COMMENT ON COLUMN sys_tenant_subscription.update_by_code IS '更新人ID';
COMMENT ON COLUMN sys_tenant_subscription.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_tenant_subscription.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant_subscription.update_time IS '更新时间';
COMMENT ON COLUMN sys_tenant_subscription.is_deleted IS '逻辑删除';

-- 用户基础表
CREATE TABLE sys_user (
                          id BIGINT NOT NULL,
                          username VARCHAR(50) NOT NULL,
                          user_code VARCHAR(50) NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          nickname VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          phone VARCHAR(20) NOT NULL,
                          avatar VARCHAR(500) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          login_ip VARCHAR(45) NOT NULL,
                          login_date TIMESTAMP NOT NULL,
                          create_by_id VARCHAR(20) NOT NULL,
                          create_by_code VARCHAR(50) NOT NULL,
                          create_by_name VARCHAR(50) NOT NULL,
                          update_by_id VARCHAR(20) NOT NULL,
                          update_by_code VARCHAR(50) NOT NULL,
                          update_by_name VARCHAR(50) NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user IS '用户基础表-存储全局用户信息';
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
COMMENT ON COLUMN sys_user.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_user.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_user.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_user.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_user.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除';

-- 用户 - 租户关联表
CREATE TABLE sys_user_tenant_rel (
                                     id BIGINT NOT NULL,
                                     user_id VARCHAR(20) NOT NULL,
                                     user_code VARCHAR(50) NOT NULL,
                                     user_name VARCHAR(100) NOT NULL,
                                     tenant_id VARCHAR(20) NOT NULL,
                                     tenant_code VARCHAR(50) NOT NULL,
                                     tenant_name VARCHAR(100) NOT NULL,
                                     dept_id VARCHAR(20) NOT NULL,
                                     dept_code VARCHAR(50) NOT NULL,
                                     dept_name VARCHAR(100) NOT NULL,
                                     is_admin BOOLEAN NOT NULL,
                                     join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_default BOOLEAN NOT NULL,
                                     create_by_id VARCHAR(20) NOT NULL,
                                     create_by_code VARCHAR(50) NOT NULL,
                                     create_by_name VARCHAR(50) NOT NULL,
                                     update_by_id VARCHAR(20) NOT NULL,
                                     update_by_code VARCHAR(50) NOT NULL,
                                     update_by_name VARCHAR(50) NOT NULL,
                                     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_deleted VARCHAR(20) NOT NULL,
                                     PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_tenant_rel IS '用户-租户关联表-实现用户与多租户绑定';
COMMENT ON COLUMN sys_user_tenant_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_tenant_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_tenant_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_tenant_rel.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_tenant_rel.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_tenant_rel.dept_id IS '主部门ID';
COMMENT ON COLUMN sys_user_tenant_rel.dept_code IS '主部门编码';
COMMENT ON COLUMN sys_user_tenant_rel.dept_name IS '主部门名称';
COMMENT ON COLUMN sys_user_tenant_rel.is_admin IS '是否租户管理员';
COMMENT ON COLUMN sys_user_tenant_rel.join_time IS '加入时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_default IS '是否默认租户';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_user_tenant_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_user_tenant_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_tenant_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_tenant_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_tenant_rel.is_deleted IS '逻辑删除';

-- 权限/资源表
CREATE TABLE sys_perm (
                          id BIGINT NOT NULL,
                          perm_name VARCHAR(100) NOT NULL,
                          perm_desc VARCHAR(200) NOT NULL,
                          perm_code VARCHAR(100) NOT NULL,
                          perm_key VARCHAR(100) NOT NULL,
                          perm_type VARCHAR(20) NOT NULL,
                          parent_id VARCHAR(20) NOT NULL,
                          parent_code VARCHAR(100) NOT NULL,
                          parent_name VARCHAR(100) NOT NULL,
                          path VARCHAR(200) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          create_by_id VARCHAR(20) NOT NULL,
                          create_by_code VARCHAR(50) NOT NULL,
                          create_by_name VARCHAR(50) NOT NULL,
                          update_by_id VARCHAR(20) NOT NULL,
                          update_by_code VARCHAR(50) NOT NULL,
                          update_by_name VARCHAR(50) NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          PRIMARY KEY (id)
);
COMMENT ON TABLE sys_perm IS '权限/资源表-定义系统所有可授权资源';
COMMENT ON COLUMN sys_perm.id IS '主键ID';
COMMENT ON COLUMN sys_perm.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm.perm_desc IS '权限描述';
COMMENT ON COLUMN sys_perm.perm_code IS '权限标识';
COMMENT ON COLUMN sys_perm.perm_key IS '权限键值';
COMMENT ON COLUMN sys_perm.perm_type IS '权限类型';
COMMENT ON COLUMN sys_perm.parent_id IS '父权限ID';
COMMENT ON COLUMN sys_perm.parent_code IS '父权限编码';
COMMENT ON COLUMN sys_perm.parent_name IS '父权限名称';
COMMENT ON COLUMN sys_perm.path IS '资源路径';
COMMENT ON COLUMN sys_perm.status IS '状态';
COMMENT ON COLUMN sys_perm.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_perm.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_perm.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_perm.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_perm.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm.is_deleted IS '逻辑删除';

-- 权限策略控制表 层级划分 与 多租户多实例权限配置
CREATE TABLE sys_perm_policy (
                                 id BIGINT NOT NULL,
                                 policy_code VARCHAR(100) NOT NULL,
                                 policy_name VARCHAR(100) NOT NULL,
                                 target_id VARCHAR(20) NOT NULL,
                                 target_code VARCHAR(100) NOT NULL,
                                 target_name VARCHAR(100) NOT NULL,
                                 target_type VARCHAR(20) NOT NULL,
                                 perm_id VARCHAR(20) NOT NULL,
                                 perm_code VARCHAR(100) NOT NULL,
                                 perm_name VARCHAR(100) NOT NULL,
                                 tenant_id VARCHAR(20) NOT NULL,
                                 tenant_code VARCHAR(50) NOT NULL,
                                 tenant_name VARCHAR(100) NOT NULL,
                                 table_name VARCHAR(64) NOT NULL,
                                 table_desc VARCHAR(100) NOT NULL,
                                 access_type VARCHAR(20) NOT NULL,
                                 field_operates JSONB DEFAULT '{}' :: jsonb,
                                 field_un_operates JSONB DEFAULT '{}' :: jsonb,
                                 status VARCHAR(20) NOT NULL,
                                 create_by_id VARCHAR(20) NOT NULL,
                                 create_by_code VARCHAR(50) NOT NULL,
                                 create_by_name VARCHAR(50) NOT NULL,
                                 update_by_id VARCHAR(20) NOT NULL,
                                 update_by_code VARCHAR(50) NOT NULL,
                                 update_by_name VARCHAR(50) NOT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR(20) NOT NULL,
                                 PRIMARY KEY (id)
);
COMMENT ON TABLE sys_perm_policy IS '权限策略控制表-实现四层权限及禁用继承逻辑';
COMMENT ON COLUMN sys_perm_policy.id IS '主键ID';
COMMENT ON COLUMN sys_perm_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_perm_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_perm_policy.target_id IS '目标ID（为租户级；租户用户ID；角色级：租户角色ID；用户级：用户ID）';
COMMENT ON COLUMN sys_perm_policy.target_code IS '目标编码';
COMMENT ON COLUMN sys_perm_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_perm_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_perm_policy.perm_id IS '权限ID';
COMMENT ON COLUMN sys_perm_policy.perm_code IS '权限编码';
COMMENT ON COLUMN sys_perm_policy.perm_name IS '权限名称';
COMMENT ON COLUMN sys_perm_policy.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_perm_policy.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_perm_policy.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_perm_policy.table_name IS '数据库表名';
COMMENT ON COLUMN sys_perm_policy.table_desc IS '表描述';
COMMENT ON COLUMN sys_perm_policy.access_type IS '访问类型';
COMMENT ON COLUMN sys_perm_policy.field_operates IS '可操作字段';
COMMENT ON COLUMN sys_perm_policy.field_un_operates IS '不可操作字段';
COMMENT ON COLUMN sys_perm_policy.status IS '状态';
COMMENT ON COLUMN sys_perm_policy.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_perm_policy.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_perm_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_perm_policy.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_perm_policy.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_perm_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_perm_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_perm_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_perm_policy.is_deleted IS '逻辑删除';

-- 用户权限表
CREATE TABLE sys_user_perm_rel (
                                   id BIGINT NOT NULL,
                                   user_id VARCHAR(20) NOT NULL,
                                   user_code VARCHAR(50) NOT NULL,
                                   user_name VARCHAR(100) NOT NULL,
                                   nick_name VARCHAR(100) NOT NULL,
                                   perm_id VARCHAR(20) NOT NULL,
                                   perm_code VARCHAR(100) NOT NULL,
                                   perm_name VARCHAR(100) NOT NULL,
                                   policy_id VARCHAR(20) NOT NULL,
                                   policy_code VARCHAR(100) NOT NULL,
                                   policy_name VARCHAR(100) NOT NULL,
                                   policy_level VARCHAR(20) NOT NULL,
                                   create_by_id VARCHAR(20) NOT NULL,
                                   create_by_code VARCHAR(50) NOT NULL,
                                   create_by_name VARCHAR(50) NOT NULL,
                                   update_by_id VARCHAR(20) NOT NULL,
                                   update_by_code VARCHAR(50) NOT NULL,
                                   update_by_name VARCHAR(50) NOT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR(20) NOT NULL,
                                   PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_perm_rel IS '用户生效权限表';
COMMENT ON COLUMN sys_user_perm_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_perm_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_perm_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_perm_rel.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_perm_rel.nick_name IS '用户昵称';
COMMENT ON COLUMN sys_user_perm_rel.perm_id IS '权限ID';
COMMENT ON COLUMN sys_user_perm_rel.perm_code IS '权限编码';
COMMENT ON COLUMN sys_user_perm_rel.perm_name IS '权限名称';
COMMENT ON COLUMN sys_user_perm_rel.policy_id IS '策略ID';
COMMENT ON COLUMN sys_user_perm_rel.policy_code IS '策略编码';
COMMENT ON COLUMN sys_user_perm_rel.policy_name IS '策略名称';
COMMENT ON COLUMN sys_user_perm_rel.policy_level IS '策略层级';
COMMENT ON COLUMN sys_user_perm_rel.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_user_perm_rel.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_user_perm_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_perm_rel.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_user_perm_rel.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_user_perm_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_perm_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_perm_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_perm_rel.is_deleted IS '逻辑删除';

-- 角色表
CREATE TABLE sys_role (
                          id BIGINT NOT NULL,
                          role_name VARCHAR(100) NOT NULL,
                          role_desc VARCHAR(200) NOT NULL,
                          role_code VARCHAR(100) NOT NULL,
                          data_scope VARCHAR(20) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          create_by_id VARCHAR(20) NOT NULL,
                          create_by_code VARCHAR(50) NOT NULL,
                          create_by_name VARCHAR(50) NOT NULL,
                          update_by_id VARCHAR(20) NOT NULL,
                          update_by_code VARCHAR(50) NOT NULL,
                          update_by_name VARCHAR(50) NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          PRIMARY KEY (id)
);
COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围';
COMMENT ON COLUMN sys_role.status IS '状态';
COMMENT ON COLUMN sys_role.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_role.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_role.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_role.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_role.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除';

-- 角色策略控制表
CREATE TABLE sys_role_policy (
                                 id BIGINT NOT NULL,
                                 policy_code VARCHAR(100) NOT NULL,
                                 policy_name VARCHAR(100) NOT NULL,
                                 target_id VARCHAR(20) NOT NULL,
                                 target_code VARCHAR(100) NOT NULL,
                                 target_name VARCHAR(100) NOT NULL,
                                 target_type VARCHAR(20) NOT NULL,
                                 role_id VARCHAR(20) NOT NULL,
                                 role_code VARCHAR(100) NOT NULL,
                                 role_name VARCHAR(100) NOT NULL,
                                 tenant_id VARCHAR(20) NOT NULL,
                                 tenant_code VARCHAR(50) NOT NULL,
                                 tenant_name VARCHAR(100) NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 create_by_id VARCHAR(20) NOT NULL,
                                 create_by_code VARCHAR(50) NOT NULL,
                                 create_by_name VARCHAR(50) NOT NULL,
                                 update_by_id VARCHAR(20) NOT NULL,
                                 update_by_code VARCHAR(50) NOT NULL,
                                 update_by_name VARCHAR(50) NOT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR(20) NOT NULL,
                                 PRIMARY KEY (id)
);
COMMENT ON TABLE sys_role_policy IS '角色策略控制表-实现角色级联禁用及策略继承';
COMMENT ON COLUMN sys_role_policy.id IS '主键ID';
COMMENT ON COLUMN sys_role_policy.policy_code IS '策略编码';
COMMENT ON COLUMN sys_role_policy.policy_name IS '策略名称';
COMMENT ON COLUMN sys_role_policy.target_id IS '目标ID（租户级：租户角色ID；用户级：用户角色ID）';
COMMENT ON COLUMN sys_role_policy.target_code IS '目标编码';
COMMENT ON COLUMN sys_role_policy.target_name IS '目标名称';
COMMENT ON COLUMN sys_role_policy.target_type IS '目标类型';
COMMENT ON COLUMN sys_role_policy.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_policy.role_code IS '角色编码';
COMMENT ON COLUMN sys_role_policy.role_name IS '角色名称';
COMMENT ON COLUMN sys_role_policy.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_role_policy.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_role_policy.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_role_policy.status IS '状态';
COMMENT ON COLUMN sys_role_policy.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_role_policy.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_role_policy.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_role_policy.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_role_policy.update_by_code IS '更新人编码';
COMMENT ON COLUMN sys_role_policy.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_role_policy.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_policy.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_policy.is_deleted IS '逻辑删除';

-- 用户角色关联表
CREATE TABLE sys_user_role_rel (
                                   id BIGINT NOT NULL,
                                   user_id VARCHAR(20) NOT NULL,
                                   user_code VARCHAR(50) NOT NULL,
                                   user_name VARCHAR(100) NOT NULL,
                                   nick_name VARCHAR(100) NOT NULL,
                                   role_id VARCHAR(20) NOT NULL,
                                   role_code VARCHAR(100) NOT NULL,
                                   role_name VARCHAR(100) NOT NULL,
                                   policy_id VARCHAR(20) NOT NULL,
                                   policy_code VARCHAR(100) NOT NULL,
                                   policy_name VARCHAR(100) NOT NULL,
                                   policy_level VARCHAR(20) NOT NULL,
                                   create_by_id VARCHAR(20) NOT NULL,
                                   create_by_code VARCHAR(50) NOT NULL,
                                   create_by_name VARCHAR(50) NOT NULL,
                                   update_by_id VARCHAR(20) NOT NULL,
                                   update_by_code VARCHAR(50) NOT NULL,
                                   update_by_name VARCHAR(50) NOT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR(20) NOT NULL,
                                   PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_role_rel IS '用户角色表-用户与角色的绑定关系';
COMMENT ON COLUMN sys_user_role_rel.id IS '主键ID';
COMMENT ON COLUMN sys_user_role_rel.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role_rel.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_role_rel.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_role_rel.nick_name IS '用户昵称';
COMMENT ON COLUMN sys_user_role_rel.role_id IS '角色ID';
COMMENT ON COLUMN sys_user_role_rel.role_code IS '角色编码';
COMMENT ON COLUMN sys_user_role_rel.role_name IS '角色名称';
COMMENT ON COLUMN sys_user_role_rel.policy_id IS '策略ID';
COMMENT ON COLUMN sys_user_role_rel.policy_code IS '策略编码';
COMMENT ON COLUMN sys_user_role_rel.policy_name IS '策略名称';
COMMENT ON COLUMN sys_user_role_rel.policy_level IS '策略级别';
COMMENT ON COLUMN sys_user_role_rel.create_by_id IS '创建人ID';
COMMENT ON COLUMN sys_user_role_rel.create_by_code IS '创建人编码';
COMMENT ON COLUMN sys_user_role_rel.create_by_name IS '创建人名称';
COMMENT ON COLUMN sys_user_role_rel.update_by_id IS '更新人ID';
COMMENT ON COLUMN sys_user_role_rel.update_by_code IS '更新人ID';
COMMENT ON COLUMN sys_user_role_rel.update_by_name IS '更新人名称';
COMMENT ON COLUMN sys_user_role_rel.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role_rel.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_role_rel.is_deleted IS '逻辑删除';

-- 用户 Token 记录表
CREATE TABLE sys_user_token (
                                id BIGINT NOT NULL,
                                user_id VARCHAR(20) NOT NULL,
                                user_code VARCHAR(50) NOT NULL,
                                user_name VARCHAR(50) NOT NULL,
                                tenant_id VARCHAR(20) NOT NULL,
                                tenant_code VARCHAR(50) NOT NULL,
                                tenant_name VARCHAR(100) NOT NULL,
                                token VARCHAR(500) NOT NULL,
                                device_info VARCHAR(200) DEFAULT '',
                                login_ip VARCHAR(45) DEFAULT '',
                                login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                expire_time TIMESTAMP NOT NULL,
                                status VARCHAR(20) NOT NULL,
                                is_deleted VARCHAR(20) NOT NULL,
                                PRIMARY KEY (id)
);
COMMENT ON TABLE sys_user_token IS '用户Token记录表-用于多端登录管理和强制下线';
COMMENT ON COLUMN sys_user_token.id IS '主键ID';
COMMENT ON COLUMN sys_user_token.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_token.user_code IS '用户编码';
COMMENT ON COLUMN sys_user_token.user_name IS '用户名称';
COMMENT ON COLUMN sys_user_token.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_token.tenant_code IS '租户编码';
COMMENT ON COLUMN sys_user_token.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_user_token.token IS '登录令牌';
COMMENT ON COLUMN sys_user_token.device_info IS '设备信息';
COMMENT ON COLUMN sys_user_token.login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user_token.login_time IS '登录时间';
COMMENT ON COLUMN sys_user_token.expire_time IS '过期时间';
COMMENT ON COLUMN sys_user_token.status IS '状态';
COMMENT ON COLUMN sys_user_token.is_deleted IS '逻辑删除';