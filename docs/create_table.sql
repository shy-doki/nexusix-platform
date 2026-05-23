-- 租户信息表
CREATE TABLE sys_tenant (
                            id BIGINT NOT NULL,
                            tenant_code VARCHAR(50) NOT NULL,
                            tenant_name VARCHAR(100) NOT NULL,
                            tenant_type VARCHAR(50) NOT NULL,
                            tenant_desc VARCHAR(500) NOT NULL,
                            tenant_logo_url VARCHAR(500) NOT NULL,
                            parent_id BIGINT NOT NULL,
                            parent_code VARCHAR(50) NOT NULL,
                            parent_name VARCHAR(100) NOT NULL,
                            path VARCHAR(1000) NOT NULL,
                            contact_name VARCHAR(50) NOT NULL,
                            contact_phone VARCHAR(20) NOT NULL,
                            status VARCHAR(20) NOT NULL,
                            expire_time TIMESTAMP NOT NULL,
                            package_id VARCHAR(50) NOT NULL,
                            package_name VARCHAR(100) NOT NULL,
                            ext_attributes JSONB DEFAULT '{}'::jsonb,
                            has_children BOOLEAN NOT NULL,
                            create_by BIGINT NOT NULL,
                            create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_by BIGINT NOT NULL,
                            update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_deleted VARCHAR(20) NOT NULL,
                            deleted_at TIMESTAMP DEFAULT NULL,
                            PRIMARY KEY (id)
);

-- 租户套餐订阅表
CREATE TABLE sys_tenant_subscription (
                                         id BIGINT NOT NULL,
                                         subscription_code VARCHAR(50) NOT NULL,
                                         tenant_id BIGINT NOT NULL,
                                         tenant_code VARCHAR(50) NOT NULL,
                                         tenant_name VARCHAR(100) NOT NULL,
                                         package_id BIGINT NOT NULL,
                                         subscription_type VARCHAR(20) NOT NULL,
                                         start_time TIMESTAMP NOT NULL,
                                         end_time TIMESTAMP NOT NULL,
                                         status VARCHAR(20) NOT NULL,
                                         is_auto_renew BOOLEAN NOT NULL,
                                         source_type VARCHAR(20) NOT NULL,
                                         parent_id BIGINT NOT NULL,
                                         create_by BIGINT NOT NULL,
                                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_by BIGINT NOT NULL,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         is_deleted VARCHAR(20) NOT NULL,
                                         deleted_at TIMESTAMP DEFAULT NULL,
                                         PRIMARY KEY (id)
);

-- 用户基础表
CREATE TABLE sys_user (
                          id BIGINT NOT NULL,
                          user_code VARCHAR(50) NOT NULL,
                          user_name VARCHAR(50) NOT NULL,
                          password VARCHAR(100) NOT NULL,
                          nick_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          phone VARCHAR(20) NOT NULL,
                          avatar VARCHAR(500) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          login_ip VARCHAR(45) NOT NULL,
                          login_date TIMESTAMP NOT NULL,
                          create_by BIGINT NOT NULL,
                          create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_by BIGINT NOT NULL,
                          update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          deleted_at TIMESTAMP DEFAULT NULL,
                          PRIMARY KEY (id)
);

-- 用户 - 租户关联表
CREATE TABLE sys_user_tenant_rel (
                                     id BIGINT NOT NULL,
                                     user_id BIGINT NOT NULL,
                                     tenant_id BIGINT NOT NULL,
                                     dept_id BIGINT NOT NULL,
                                     is_admin BOOLEAN NOT NULL,
                                     join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_default BOOLEAN NOT NULL,
                                     create_by BIGINT NOT NULL,
                                     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     update_by BIGINT NOT NULL,
                                     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     is_deleted VARCHAR(20) NOT NULL,
                                     deleted_at TIMESTAMP DEFAULT NULL,
                                     PRIMARY KEY (id)
);

-- 权限/资源表
CREATE TABLE sys_perm (
                          id BIGINT NOT NULL,
                          perm_name VARCHAR(100) NOT NULL,
                          perm_desc VARCHAR(200) NOT NULL,
                          perm_code VARCHAR(100) NOT NULL,
                          perm_key VARCHAR(100) NOT NULL,
                          perm_type VARCHAR(20) NOT NULL,
                          parent_id BIGINT NOT NULL,
                          parent_name VARCHAR(100) NOT NULL,
                          path VARCHAR(200) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          create_by_id BIGINT NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_by_id BIGINT NOT NULL,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          deleted_at TIMESTAMP DEFAULT NULL,
                          PRIMARY KEY (id)
);

-- 权限策略控制表 层级划分 与 多租户多实例权限配置
CREATE TABLE sys_perm_policy (
                                 id BIGINT NOT NULL,
                                 policy_code VARCHAR(100) NOT NULL,
                                 policy_name VARCHAR(100) NOT NULL,
                                 target_id BIGINT NOT NULL,
                                 target_type VARCHAR(20) NOT NULL,
                                 perm_id BIGINT NOT NULL,
                                 tenant_id BIGINT NOT NULL,
                                 table_name VARCHAR(64) NOT NULL,
                                 table_desc VARCHAR(100) NOT NULL,
                                 access_type VARCHAR(20) NOT NULL,
                                 field_operates JSONB DEFAULT '{}' :: jsonb,
                                 field_un_operates JSONB DEFAULT '{}' :: jsonb,
                                 status VARCHAR(20) NOT NULL,
                                 create_by_id BIGINT NOT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_by_id BIGINT NOT NULL,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR(20) NOT NULL,
                                 deleted_at TIMESTAMP DEFAULT NULL,
                                 PRIMARY KEY (id)
);

-- 用户权限表
CREATE TABLE sys_user_perm_rel (
                                   id BIGINT NOT NULL,
                                   user_id BIGINT NOT NULL,
                                   policy_id BIGINT NOT NULL,
                                   create_by_id BIGINT NOT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_by_id BIGINT NOT NULL,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR(20) NOT NULL,
                                   delete_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (id)
);

-- 角色表
CREATE TABLE sys_role (
                          id BIGINT NOT NULL,
                          role_name VARCHAR(100) NOT NULL,
                          role_desc VARCHAR(200) NOT NULL,
                          role_code VARCHAR(100) NOT NULL,
                          data_scope VARCHAR(20) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          create_by_id BIGINT NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_by_id BIGINT NOT NULL,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          is_deleted VARCHAR(20) NOT NULL,
                          deleted_at TIMESTAMP DEFAULT NULL,
                          PRIMARY KEY (id)
);

-- 角色策略控制表
CREATE TABLE sys_role_policy (
                                 id BIGINT NOT NULL,
                                 policy_code VARCHAR(100) NOT NULL,
                                 policy_name VARCHAR(100) NOT NULL,
                                 target_id BIGINT NOT NULL,
                                 target_type VARCHAR(20) NOT NULL,
                                 role_id BIGINT NOT NULL,
                                 tenant_id BIGINT NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 create_by_id BIGINT NOT NULL,
                                 create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 update_by_id BIGINT NOT NULL,
                                 update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 is_deleted VARCHAR(20) NOT NULL,
                                 deleted_at TIMESTAMP DEFAULT NULL,
                                 PRIMARY KEY (id)
);

-- 用户角色关联表
CREATE TABLE sys_user_role_rel (
                                   id BIGINT NOT NULL,
                                   user_id BIGINT NOT NULL,
                                   role_id BIGINT NOT NULL,
                                   policy_id BIGINT NOT NULL,
                                   create_by_id BIGINT NOT NULL,
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   update_by_id BIGINT NOT NULL,
                                   update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted VARCHAR(20) NOT NULL,
                                   delete_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (id)
);

-- 用户 Token 记录表
CREATE TABLE sys_user_token (
                                id BIGINT NOT NULL,
                                user_id BIGINT NOT NULL,
                                tenant_id BIGINT NOT NULL,
                                token VARCHAR(500) NOT NULL,
                                device_info VARCHAR(200) DEFAULT '',
                                login_ip VARCHAR(45) DEFAULT '',
                                login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                expire_time TIMESTAMP NOT NULL,
                                status VARCHAR(20) NOT NULL,
                                is_deleted VARCHAR(20) NOT NULL,
                                deleted_at TIMESTAMP DEFAULT NULL,
                                PRIMARY KEY (id)
);