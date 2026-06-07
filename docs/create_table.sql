/*
 Navicat Premium Data Transfer

 Source Server         : Postgresql17.5
 Source Server Type    : PostgreSQL
 Source Server Version : 170005 (170005)
 Source Host           : localhost:5432
 Source Catalog        : NexusIX
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170005 (170005)
 File Encoding         : 65001

 Date: 06/06/2026 14:49:17
*/


-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_perm";
CREATE TABLE "public"."sys_perm" (
                                     "id" int8 NOT NULL,
                                     "perm_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "perm_desc" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
                                     "perm_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "perm_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "perm_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "parent_id" int8 NOT NULL,
                                     "parent_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "path" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
                                     "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "create_by" int8 NOT NULL,
                                     "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8 NOT NULL,
                                     "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_perm"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_perm"."perm_name" IS '权限名称';
COMMENT ON COLUMN "public"."sys_perm"."perm_desc" IS '权限描述';
COMMENT ON COLUMN "public"."sys_perm"."perm_code" IS '权限编码';
COMMENT ON COLUMN "public"."sys_perm"."perm_key" IS '权限标识';
COMMENT ON COLUMN "public"."sys_perm"."perm_type" IS '权限类型';
COMMENT ON COLUMN "public"."sys_perm"."parent_id" IS '父权限ID';
COMMENT ON COLUMN "public"."sys_perm"."parent_name" IS '父权限名称';
COMMENT ON COLUMN "public"."sys_perm"."path" IS '权限路径';
COMMENT ON COLUMN "public"."sys_perm"."status" IS '权限状态';
COMMENT ON COLUMN "public"."sys_perm"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_perm"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_perm"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_perm"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_perm"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_perm"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_perm" IS '系统权限资源表';

-- ----------------------------
-- Records of sys_perm
-- ----------------------------
INSERT INTO "public"."sys_perm" VALUES (1, '系统管理', '系统管理根菜单', 'SYS_MGMT', 'system', 'MENU', 0, '-', '/1/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (2, '用户管理', '用户管理菜单', 'USER_MGMT', 'system:user', 'MENU', 1, '系统管理', '/1/2/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (3, '用户列表', '用户列表页面', 'USER_LIST', 'system:user:list', 'MENU', 2, '用户管理', '/1/2/3/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (4, '新增用户', '新增用户按钮', 'USER_ADD', 'system:user:add', 'BUTTON', 3, '用户列表', '/1/2/3/4/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (5, '编辑用户', '编辑用户按钮', 'USER_EDIT', 'system:user:edit', 'BUTTON', 3, '用户列表', '/1/2/3/5/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (6, '删除用户', '删除用户按钮', 'USER_DEL', 'system:user:del', 'BUTTON', 3, '用户列表', '/1/2/3/6/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (7, '导出用户', '导出用户按钮', 'USER_EXPORT', 'system:user:export', 'BUTTON', 3, '用户列表', '/1/2/3/7/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (8, '用户接口', '用户相关API', 'USER_API', 'system:user:api', 'API', 2, '用户管理', '/1/2/8/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (9, '角色管理', '角色管理菜单', 'ROLE_MGMT', 'system:role', 'MENU', 1, '系统管理', '/1/9/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (10, '角色列表', '角色列表页面', 'ROLE_LIST', 'system:role:list', 'MENU', 9, '角色管理', '/1/9/10/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (11, '新增角色', '新增角色按钮', 'ROLE_ADD', 'system:role:add', 'BUTTON', 10, '角色列表', '/1/9/10/11/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (12, '分配角色', '分配角色按钮', 'ROLE_ASSIGN', 'system:role:assign', 'BUTTON', 10, '角色列表', '/1/9/10/12/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (13, '角色接口', '角色相关API', 'ROLE_API', 'system:role:api', 'API', 9, '角色管理', '/1/9/13/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (14, '租户管理', '租户管理菜单', 'TENANT_MGMT', 'system:tenant', 'MENU', 1, '系统管理', '/1/14/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (15, '租户列表', '租户列表页面', 'TENANT_LIST', 'system:tenant:list', 'MENU', 14, '租户管理', '/1/14/15/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (16, '新增租户', '新增租户按钮', 'TENANT_ADD', 'system:tenant:add', 'BUTTON', 15, '租户列表', '/1/14/15/16/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (17, '编辑租户', '编辑租户按钮', 'TENANT_EDIT', 'system:tenant:edit', 'BUTTON', 15, '租户列表', '/1/14/15/17/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (18, '权限管理', '权限管理菜单', 'PERM_MGMT', 'system:perm', 'MENU', 1, '系统管理', '/1/18/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (19, '权限列表', '权限列表页面', 'PERM_LIST', 'system:perm:list', 'MENU', 18, '权限管理', '/1/18/19/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (20, '新增权限', '新增权限按钮', 'PERM_ADD', 'system:perm:add', 'BUTTON', 19, '权限列表', '/1/18/19/20/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (21, '业务模块', '业务模块根菜单', 'BIZ_MGMT', 'biz', 'MENU', 0, '-', '/21/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (22, '订单管理', '订单管理菜单', 'ORDER_MGMT', 'biz:order', 'MENU', 21, '业务模块', '/21/22/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (23, '订单列表', '订单列表页面', 'ORDER_LIST', 'biz:order:list', 'MENU', 22, '订单管理', '/21/22/23/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (24, '创建订单', '创建订单按钮', 'ORDER_CREATE', 'biz:order:create', 'BUTTON', 23, '订单列表', '/21/22/23/24/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (25, '取消订单', '取消订单按钮', 'ORDER_CANCEL', 'biz:order:cancel', 'BUTTON', 23, '订单列表', '/21/22/23/25/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (26, '导出订单', '导出订单按钮', 'ORDER_EXPORT', 'biz:order:export', 'BUTTON', 23, '订单列表', '/21/22/23/26/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (27, '订单接口', '订单相关API', 'ORDER_API', 'biz:order:api', 'API', 22, '订单管理', '/21/22/27/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (28, '商品管理', '商品管理菜单', 'PRODUCT_MGMT', 'biz:product', 'MENU', 21, '业务模块', '/21/28/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (29, '商品列表', '商品列表页面', 'PRODUCT_LIST', 'biz:product:list', 'MENU', 28, '商品管理', '/21/28/29/', 'ENABLED', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm" VALUES (30, '禁用权限', '已禁用权限演示', 'PERM_DISABLED', 'system:perm:disabled', 'BUTTON', 3, '用户列表', '/1/2/3/30/', 'DISABLED', 1, '2025-01-01 00:00:00', 1, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Table structure for sys_perm_policy
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_perm_policy";
CREATE TABLE "public"."sys_perm_policy" (
                                            "id" int8 NOT NULL,
                                            "policy_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                            "policy_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                            "target_id" int8 NOT NULL,
                                            "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                            "perm_id" int8 NOT NULL,
                                            "tenant_id" int8 NOT NULL,
                                            "table_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                            "table_desc" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                            "access_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                            "field_operates" jsonb DEFAULT '[]'::jsonb,
                                            "status" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                            "create_by" int8 NOT NULL,
                                            "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                            "update_by" int8 NOT NULL,
                                            "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                            "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                            "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_perm_policy"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."policy_code" IS '策略编码';
COMMENT ON COLUMN "public"."sys_perm_policy"."policy_name" IS '策略名称';
COMMENT ON COLUMN "public"."sys_perm_policy"."target_id" IS '授权目标ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."target_type" IS '授权目标类型';
COMMENT ON COLUMN "public"."sys_perm_policy"."perm_id" IS '关联权限ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."table_name" IS '控制的数据表名';
COMMENT ON COLUMN "public"."sys_perm_policy"."table_desc" IS '数据表描述';
COMMENT ON COLUMN "public"."sys_perm_policy"."access_type" IS '访问类型';
COMMENT ON COLUMN "public"."sys_perm_policy"."field_operates" IS '允许操作的字段，JSON格式';
COMMENT ON COLUMN "public"."sys_perm_policy"."status" IS '策略状态';
COMMENT ON COLUMN "public"."sys_perm_policy"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_perm_policy"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_perm_policy"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_perm_policy"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_perm_policy"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_perm_policy" IS '权限策略表';

-- ----------------------------
-- Records of sys_perm_policy
-- ----------------------------
INSERT INTO "public"."sys_perm_policy" VALUES (1, 'POL-SYS-QUERY-USER', '查询用户-系统级', 0, 'TENANT', 3, 1, 'sys_user', '用户表', 'QUERY', '["id", "user_name", "nick_name"]', 'ACTIVE', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (2, 'POL-SYS-ADD-USER-DIS', '新增用户-系统级禁用', 0, 'TENANT', 4, 1, 'sys_user', '用户表', 'CREATE', '["password", "phone"]', 'DISABLED_SYSTEM_LEVEL', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (3, 'POL-TENANT-ADD-USER', '新增用户-集团A租户级', 2, 'TENANT', 4, 2, 'sys_user', '用户表', 'CREATE', '["user_name", "nick_name", "email"]', 'ACTIVE', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (4, 'POL-TENANT-DEL-USER-DIS', '删除用户-集团A租户级禁用', 2, 'TENANT', 6, 2, 'sys_user', '用户表', 'UPDATE', '[]', 'DISABLED_TENANT_LEVEL', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (5, 'POL-ROLE-ASSIGN-DIS', '分配角色-角色级禁用', 4, 'ROLE', 12, 2, 'sys_user_role_rel', '用户角色关联表', 'UPDATE', '[]', 'DISABLED_ROLE_LEVEL', 2, '2025-01-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (6, 'POL-USER-QUERY-ROLE-DIS', '查询角色-用户级禁用', 5, 'USER', 10, 2, 'sys_role', '角色表', 'QUERY', '[]', 'DISABLED_USER_LEVEL', 2, '2025-03-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (7, 'POL-SYS-QUERY-TENANT', '查询租户-系统级', 0, 'TENANT', 15, 1, 'sys_tenant', '租户表', 'QUERY', '["tenant_name", "status"]', 'ACTIVE', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (8, 'POL-TENANT-ADD-TENANT', '新增租户-集团A租户级', 2, 'TENANT', 16, 2, 'sys_tenant', '租户表', 'CREATE', '["tenant_code", "tenant_name"]', 'ACTIVE', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (9, 'POL-TENANT-UPDATE-USER', '修改用户-集团A租户级', 2, 'TENANT', 5, 2, 'sys_user', '用户表', 'UPDATE', '["nick_name", "email", "phone"]', 'ACTIVE', 2, '2025-01-15 00:00:00', 2, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (10, 'POL-BRANCH-QUERY-USER', '查询用户-华东分公司', 3, 'TENANT', 3, 3, 'sys_user', '用户表', 'QUERY', '["id", "user_name"]', 'ACTIVE', 3, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (11, 'POL-DEPT-QUERY-USER', '查询用户-技术部', 4, 'TENANT', 3, 4, 'sys_user', '用户表', 'QUERY', '["id", "user_name"]', 'ACTIVE', 4, '2025-02-15 00:00:00', 4, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (12, 'POL-SYS-QUERY-ROLE', '查询角色-系统级', 0, 'TENANT', 10, 1, 'sys_role', '角色表', 'QUERY', '["id", "role_name", "role_code"]', 'ACTIVE', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (13, 'POL-SYS-QUERY-PERM', '查询权限-系统级', 0, 'TENANT', 19, 1, 'sys_perm', '权限表', 'QUERY', '["id", "perm_name", "perm_code"]', 'ACTIVE', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (14, 'POL-TENANT-EDIT-USER-DIS', '编辑用户-集团A租户级禁用', 2, 'TENANT', 5, 2, 'sys_user', '用户表', 'UPDATE', '[]', 'DISABLED_TENANT_LEVEL', 2, '2025-02-01 00:00:00', 2, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (15, 'POL-USER-EXPORT-DIS', '导出用户-用户级禁用', 9, 'USER', 7, 2, 'sys_user', '用户表', 'QUERY', '[]', 'DISABLED_USER_LEVEL', 2, '2025-03-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (16, 'POL-TENANT-QUERY-ORDER', '查询订单-集团A租户级', 2, 'TENANT', 23, 2, 'biz_order', '订单表', 'QUERY', '["id", "order_no", "status"]', 'ACTIVE', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (17, 'POL-TENANT-CREATE-ORDER', '创建订单-集团A租户级', 2, 'TENANT', 24, 2, 'biz_order', '订单表', 'CREATE', '["order_no", "product_id", "quantity"]', 'DISABLED_USER_LEVEL', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (18, 'POL-TENANT-CANCEL-ORDER-DIS', '取消订单-集团A租户级禁用', 2, 'TENANT', 25, 2, 'biz_order', '订单表', 'UPDATE', '[]', 'DISABLED_TENANT_LEVEL', 2, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (19, 'POL-SYS-EXPORT-ORDER', '导出订单-系统级', 0, 'TENANT', 26, 1, 'biz_order', '订单表', 'QUERY', '["id", "order_no", "amount"]', 'ACTIVE', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (20, 'POL-USER-CREATE-ORDER-DIS', '创建订单-用户级禁用', 14, 'USER', 24, 2, 'biz_order', '订单表', 'CREATE', '[]', 'DISABLED_USER_LEVEL', 2, '2025-03-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (21, 'POL-BRANCH-QUERY-ORDER', '查询订单-华南分公司', 7, 'TENANT', 23, 7, 'biz_order', '订单表', 'QUERY', '["id", "order_no"]', 'ACTIVE', 7, '2025-02-01 00:00:00', 7, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (22, 'POL-DEPT-QUERY-ORDER', '查询订单-财务部', 8, 'TENANT', 23, 8, 'biz_order', '订单表', 'QUERY', '["id", "order_no", "amount"]', 'ACTIVE', 8, '2025-02-15 00:00:00', 8, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (23, 'POL-ROLE-ADD-ROLE-DIS', '新增角色-角色级禁用', 4, 'ROLE', 11, 2, 'sys_role', '角色表', 'CREATE', '[]', 'DISABLED_ROLE_LEVEL', 2, '2025-03-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (24, 'POL-SYS-ADD-PERM-DIS', '新增权限-系统级禁用', 0, 'TENANT', 20, 1, 'sys_perm', '权限表', 'CREATE', '[]', 'DISABLED_SYSTEM_LEVEL', 1, '2025-06-01 00:00:00', 1, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_perm_policy" VALUES (25, 'POL-DELETED', '已删除策略', 2, 'TENANT', 6, 2, 'sys_user', '用户表', 'UPDATE', '[]', 'ACTIVE', 2, '2025-01-15 00:00:00', 2, '2025-04-01 00:00:00', 'DELETED', '2025-04-01 00:00:00');

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant";
CREATE TABLE "public"."sys_tenant" (
                                       "id" int8 NOT NULL,
                                       "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                       "tenant_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "tenant_desc" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                       "tenant_logo_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                       "parent_id" int8 NOT NULL,
                                       "parent_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "parent_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                       "path" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL,
                                       "contact_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "contact_phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "expire_time" timestamp(6) NOT NULL,
                                       "package_id" varchar(50) COLLATE "pg_catalog"."default",
                                       "package_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                       "ext_attributes" jsonb DEFAULT '{}'::jsonb,
                                       "has_children" bool NOT NULL,
                                       "create_by" int8 NOT NULL,
                                       "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "update_by" int8 NOT NULL,
                                       "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_tenant"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_tenant"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_tenant"."tenant_name" IS '租户名称';
COMMENT ON COLUMN "public"."sys_tenant"."tenant_type" IS '租户类型';
COMMENT ON COLUMN "public"."sys_tenant"."tenant_desc" IS '租户描述信息';
COMMENT ON COLUMN "public"."sys_tenant"."tenant_logo_url" IS '租户LOGO访问地址';
COMMENT ON COLUMN "public"."sys_tenant"."parent_id" IS '父租户ID';
COMMENT ON COLUMN "public"."sys_tenant"."parent_code" IS '父租户编码';
COMMENT ON COLUMN "public"."sys_tenant"."parent_name" IS '父租户名称';
COMMENT ON COLUMN "public"."sys_tenant"."path" IS '租户层级路径';
COMMENT ON COLUMN "public"."sys_tenant"."contact_name" IS '联系人姓名';
COMMENT ON COLUMN "public"."sys_tenant"."contact_phone" IS '联系人电话';
COMMENT ON COLUMN "public"."sys_tenant"."status" IS '租户状态';
COMMENT ON COLUMN "public"."sys_tenant"."expire_time" IS '租户过期时间';
COMMENT ON COLUMN "public"."sys_tenant"."package_id" IS '租户套餐ID';
COMMENT ON COLUMN "public"."sys_tenant"."package_name" IS '租户套餐名称';
COMMENT ON COLUMN "public"."sys_tenant"."ext_attributes" IS '扩展属性，JSON格式';
COMMENT ON COLUMN "public"."sys_tenant"."has_children" IS '是否存在子租户';
COMMENT ON COLUMN "public"."sys_tenant"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_tenant"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_tenant"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_tenant"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_tenant"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_tenant"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_tenant" IS '租户信息表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO "public"."sys_tenant" VALUES (9, 'GRP_A_NORTH', '集团A-华北分公司', '互联网', '二级分公司', '/logo/north.png', 2, 'GRP_A', '集团A', '/1/2/9/', '吴经理', '13800000024', 'ENABLED', '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (10, 'GRP_A_NORTH_DEV', '华北-研发部', '互联网', '三级部门', '/logo/dev.png', 9, 'GRP_A_NORTH', '集团A-华北分公司', '/1/2/9/10/', '郑主管', '13800000025', 'ENABLED', '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 2, '2025-02-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (11, 'GRP_B', '集团B', '餐饮', '已停用租户', '/logo/grp_b.png', 1, 'PLAT', '平台运营方', '/1/11/', '陈总', '13800000005', 'DISABLED', '2025-01-01 00:00:00', NULL, '基础版', '{}', 'f', 1, '2025-01-20 00:00:00', 1, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (12, 'GRP_C', '集团C', '制造', '已过期租户', '/logo/grp_c.png', 1, 'PLAT', '平台运营方', '/1/12/', '周总', '13800000006', 'EXPIRED', '2024-12-31 00:00:00', NULL, '基础版', '{}', 'f', 1, '2024-06-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (13, 'GRP_D', '集团D', '互联网', '二级集团', '/logo/grp_d.png', 1, 'PLAT', '平台运营方', '/1/13/', '冯总', '13800000030', 'ENABLED', '2027-12-31 00:00:00', NULL, '企业版', '{"industry": "教育"}', 't', 1, '2025-03-01 00:00:00', 1, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (14, 'GRP_D_EDU', '集团D-教育事业部', '互联网', '二级分公司', '/logo/edu.png', 13, 'GRP_D', '集团D', '/1/13/14/', '褚经理', '13800000031', 'ENABLED', '2027-12-31 00:00:00', NULL, '商业版', '{}', 'f', 13, '2025-03-15 00:00:00', 13, '2025-03-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (15, 'GRP_DELETED', '已删除集团', '互联网', '逻辑删除演示', '/logo/del.png', 1, 'PLAT', '平台运营方', '/1/15/', '卫总', '13800000007', 'ENABLED', '2026-12-31 00:00:00', NULL, '基础版', '{}', 'f', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'DELETED', '2025-04-01 00:00:00');
INSERT INTO "public"."sys_tenant" VALUES (1, 'PLAT', '平台运营方', '互联网', '平台顶级租户', '/logo/plat.png', 0, '-', '-', '/1/', '管理员', '13800000001', 'ENABLED', '2099-12-31 00:00:00', NULL, '旗舰版', '{}', 't', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (2, 'GRP_A', '集团A', '互联网', '一级企业租户', '/logo/grp_a.png', 1, 'PLAT', '平台运营方', '/1/2/', '张总', '13800000002', 'ENABLED', '2027-06-30 00:00:00', NULL, '企业版', '{"scale": "500+", "industry": "电商"}', 't', 1, '2025-01-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (3, 'GRP_A_EAST', '集团A-华东分公司', '互联网', '二级分公司', '/logo/east.png', 2, 'GRP_A', '集团A', '/1/2/3/', '李经理', '13800000003', 'ENABLED', '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (4, 'GRP_A_EAST_TECH', '华东-技术部', '互联网', '三级部门', '/logo/tech.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/4/', '王主管', '13800000004', 'ENABLED', '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 4, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (5, 'GRP_A_EAST_SALE', '华东-销售部', '互联网', '三级部门', '/logo/sale.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/5/', '赵主管', '13800000020', 'ENABLED', '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (6, 'GRP_A_EAST_HR', '华东-人事部', '互联网', '三级部门', '/logo/hr.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/6/', '钱主管', '13800000021', 'ENABLED', '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (7, 'GRP_A_SOUTH', '集团A-华南分公司', '互联网', '二级分公司', '/logo/south.png', 2, 'GRP_A', '集团A', '/1/2/7/', '孙经理', '13800000022', 'ENABLED', '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (8, 'GRP_A_SOUTH_FIN', '华南-财务部', '互联网', '三级部门', '/logo/fin.png', 7, 'GRP_A_SOUTH', '集团A-华南分公司', '/1/2/7/8/', '周主管', '13800000023', 'ENABLED', '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 2, '2025-02-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Table structure for sys_tenant_subscription
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant_subscription";
CREATE TABLE "public"."sys_tenant_subscription" (
                                                    "id" int8 NOT NULL,
                                                    "subscription_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "tenant_id" int8 NOT NULL,
                                                    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "package_id" int8 NOT NULL,
                                                    "subscription_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "start_time" timestamp(6) NOT NULL,
                                                    "end_time" timestamp(6) NOT NULL,
                                                    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "is_auto_renew" bool NOT NULL,
                                                    "source_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "parent_id" int8 NOT NULL,
                                                    "create_by" int8 NOT NULL,
                                                    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                    "update_by" int8 NOT NULL,
                                                    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_tenant_subscription"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."subscription_code" IS '订阅单编码';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."tenant_name" IS '租户名称';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."package_id" IS '套餐ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."subscription_type" IS '订阅类型';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."start_time" IS '订阅开始时间';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."end_time" IS '订阅结束时间';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."status" IS '订阅状态';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."is_auto_renew" IS '是否自动续费';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."source_type" IS '订阅来源';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."parent_id" IS '父订阅ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_tenant_subscription"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_tenant_subscription" IS '租户套餐订阅记录表';

-- ----------------------------
-- Records of sys_tenant_subscription
-- ----------------------------
INSERT INTO "public"."sys_tenant_subscription" VALUES (1, 'SUB-2025-001', 2, 'GRP_A', '集团A', 101, 'NEW', '2025-01-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 't', 'DIRECT', 0, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (2, 'SUB-2025-002', 3, 'GRP_A_EAST', '集团A-华东分公司', 102, 'NEW', '2025-02-01 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 't', 'PARENT_GRANT', 1, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (3, 'SUB-2025-003', 4, 'GRP_A_EAST_TECH', '华东-技术部', 103, 'NEW', '2025-02-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 'f', 'ADMIN_ASSIGN', 2, 3, '2025-02-15 00:00:00', 4, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (4, 'SUB-2025-004', 5, 'GRP_A_EAST_SALE', '华东-销售部', 103, 'NEW', '2025-02-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 'f', 'ADMIN_ASSIGN', 2, 3, '2025-02-15 00:00:00', 3, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (5, 'SUB-2025-005', 6, 'GRP_A_EAST_HR', '华东-人事部', 103, 'NEW', '2025-02-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 'f', 'ADMIN_ASSIGN', 2, 3, '2025-02-15 00:00:00', 3, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (6, 'SUB-2025-006', 7, 'GRP_A_SOUTH', '集团A-华南分公司', 102, 'NEW', '2025-02-01 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 't', 'PARENT_GRANT', 1, 2, '2025-02-01 00:00:00', 2, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (7, 'SUB-2025-007', 8, 'GRP_A_SOUTH_FIN', '华南-财务部', 103, 'NEW', '2025-02-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 'f', 'ADMIN_ASSIGN', 6, 7, '2025-02-15 00:00:00', 2, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (8, 'SUB-2025-008', 9, 'GRP_A_NORTH', '集团A-华北分公司', 102, 'NEW', '2025-02-01 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 't', 'PARENT_GRANT', 1, 2, '2025-02-01 00:00:00', 2, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (9, 'SUB-2025-009', 10, 'GRP_A_NORTH_DEV', '华北-研发部', 103, 'NEW', '2025-02-15 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 'f', 'ADMIN_ASSIGN', 8, 9, '2025-02-15 00:00:00', 2, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (10, 'SUB-2025-010', 2, 'GRP_A', '集团A', 104, 'UPGRADE', '2025-06-01 00:00:00', '2027-06-30 00:00:00', 'ACTIVE', 't', 'DIRECT', 0, 2, '2025-06-01 00:00:00', 2, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (11, 'SUB-2025-011', 2, 'GRP_A', '集团A', 101, 'RENEWAL', '2026-01-15 00:00:00', '2028-01-15 00:00:00', 'PENDING', 't', 'DIRECT', 0, 2, '2025-12-01 00:00:00', 2, '2025-12-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (12, 'SUB-2025-012', 2, 'GRP_A', '集团A', 103, 'DOWNGRADE', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 'CANCELLED', 'f', 'DIRECT', 0, 2, '2025-04-01 00:00:00', 2, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (13, 'SUB-2024-001', 12, 'GRP_C', '集团C', 103, 'NEW', '2024-01-01 00:00:00', '2024-12-31 00:00:00', 'EXPIRED', 'f', 'DIRECT', 0, 1, '2024-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant_subscription" VALUES (14, 'SUB-2025-013', 13, 'GRP_D', '集团D', 101, 'NEW', '2025-03-01 00:00:00', '2027-12-31 00:00:00', 'ACTIVE', 't', 'DIRECT', 0, 1, '2025-03-01 00:00:00', 13, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user" (
                                     "id" int8 NOT NULL,
                                     "user_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "user_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "password" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "nick_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                     "email" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                     "phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "avatar" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                     "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "login_ip" varchar(45) COLLATE "pg_catalog"."default" NOT NULL,
                                     "login_date" timestamp(6) NOT NULL,
                                     "create_by" int8 NOT NULL,
                                     "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_by" int8 NOT NULL,
                                     "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                     "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user"."user_code" IS '用户编码';
COMMENT ON COLUMN "public"."sys_user"."user_name" IS '登录用户名';
COMMENT ON COLUMN "public"."sys_user"."password" IS '加密密码';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮箱';
COMMENT ON COLUMN "public"."sys_user"."phone" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."status" IS '用户状态';
COMMENT ON COLUMN "public"."sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_user"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user" IS '系统用户基础信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user" VALUES (1, 'U001', 'admin', '$2a$10$h1', '超级管理员', 'admin@nexusix.com', '13800000001', '/avatar/admin.png', 'ENABLED', '10.0.0.1', '2025-06-01 09:00:00', 1, '2025-01-01 00:00:00', 1, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (2, 'U002', 'zhang_zong', '$2a$10$h2', '张总', 'zhang@grpa.com', '13800000002', '/avatar/zhang.png', 'ENABLED', '10.0.1.1', '2025-06-01 10:00:00', 1, '2025-01-15 00:00:00', 2, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (3, 'U003', 'li_jingli', '$2a$10$h3', '李经理', 'li@east.com', '13800000003', '/avatar/li.png', 'ENABLED', '10.0.2.1', '2025-06-01 11:00:00', 2, '2025-02-01 00:00:00', 3, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (4, 'U004', 'wang_zhuguan', '$2a$10$h4', '王主管', 'wang@tech.com', '13800000004', '/avatar/wang.png', 'ENABLED', '10.0.3.1', '2025-06-01 12:00:00', 3, '2025-02-15 00:00:00', 4, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (5, 'U005', 'zhang_san', '$2a$10$h5', '张三', 'zhangsan@grpa.com', '13800000010', '/avatar/zs.png', 'ENABLED', '10.0.1.10', '2025-06-01 14:00:00', 2, '2025-03-01 00:00:00', 5, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (6, 'U006', 'li_si', '$2a$10$h6', '李四', 'lisi@east.com', '13800000011', '/avatar/ls.png', 'ENABLED', '10.0.2.10', '2025-06-01 15:00:00', 3, '2025-03-01 00:00:00', 6, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (7, 'U007', 'wang_wu', '$2a$10$h7', '王五', 'wangwu@tech.com', '13800000012', '/avatar/ww.png', 'ENABLED', '10.0.3.10', '2025-06-01 16:00:00', 4, '2025-03-01 00:00:00', 7, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (8, 'U008', 'zhao_liu', '$2a$10$h8', '赵六', 'zhaoliu@sale.com', '13800000013', '/avatar/zl.png', 'ENABLED', '10.0.3.20', '2025-06-01 17:00:00', 3, '2025-03-01 00:00:00', 8, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (9, 'U009', 'sun_qi', '$2a$10$h9', '孙七', 'sunqi@grpa.com', '13800000014', '/avatar/sq.png', 'ENABLED', '10.0.1.20', '2025-06-01 18:00:00', 2, '2025-03-01 00:00:00', 9, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (10, 'U010', 'zhou_ba', '$2a$10$h10', '周八', 'zhouba@fin.com', '13800000015', '/avatar/zb.png', 'ENABLED', '10.0.4.10', '2025-06-01 19:00:00', 2, '2025-03-01 00:00:00', 10, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (11, 'U011', 'wu_jiu', '$2a$10$h11', '吴九', 'wujiu@north.com', '13800000016', '/avatar/wj.png', 'ENABLED', '10.0.5.10', '2025-06-01 20:00:00', 2, '2025-03-01 00:00:00', 11, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (12, 'U012', 'zheng_shi', '$2a$10$h12', '郑十', 'zhengshi@dev.com', '13800000017', '/avatar/zs2.png', 'ENABLED', '10.0.6.10', '2025-06-01 21:00:00', 2, '2025-03-01 00:00:00', 12, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (13, 'U013', 'qian_yi', '$2a$10$h13', '钱一', 'qianyi@hr.com', '13800000018', '/avatar/qy.png', 'ENABLED', '10.0.3.30', '2025-06-01 22:00:00', 3, '2025-03-01 00:00:00', 13, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (14, 'U014', 'chen_er', '$2a$10$h14', '陈二', 'chener@grpa.com', '13800000019', '/avatar/ce.png', 'ENABLED', '10.0.1.30', '2025-06-01 23:00:00', 2, '2025-03-01 00:00:00', 14, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (15, 'U015', 'feng_san', '$2a$10$h15', '冯三', 'fengsan@grpd.com', '13800000032', '/avatar/fs.png', 'ENABLED', '10.0.7.10', '2025-06-02 09:00:00', 1, '2025-03-01 00:00:00', 15, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (16, 'U016', 'chu_si', '$2a$10$h16', '褚四', 'chusi@edu.com', '13800000033', '/avatar/cs.png', 'ENABLED', '10.0.7.20', '2025-06-02 10:00:00', 13, '2025-03-15 00:00:00', 16, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (17, 'U017', 'zhao_liu_dis', '$2a$10$h17', '赵六(禁用)', 'zhaoliu2@grpa.com', '13800000040', '/avatar/zl2.png', 'DISABLED', '10.0.1.40', '2025-04-01 09:00:00', 2, '2025-03-01 00:00:00', 2, '2025-05-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (18, 'U018', 'sun_deleted', '$2a$10$h18', '孙(已删)', 'sundel@grpa.com', '13800000041', '/avatar/sd.png', 'ENABLED', '10.0.1.50', '2025-05-20 10:00:00', 2, '2025-03-01 00:00:00', 2, '2025-05-20 00:00:00', 'DELETED', '2025-05-20 00:00:00');
INSERT INTO "public"."sys_user" VALUES (19, 'U019', 'huang_auditor', '$2a$10$h19', '黄审计', 'huang@grpa.com', '13800000042', '/avatar/hx.png', 'ENABLED', '10.0.1.60', '2025-06-01 08:00:00', 2, '2025-03-01 00:00:00', 19, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user" VALUES (20, 'U020', 'xu_readonly', '$2a$10$h20', '徐只读', 'xu@east.com', '13800000043', '/avatar/xz.png', 'ENABLED', '10.0.2.20', '2025-06-01 07:00:00', 3, '2025-03-01 00:00:00', 20, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Table structure for sys_user_perm_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_perm_rel";
CREATE TABLE "public"."sys_user_perm_rel" (
                                              "id" int8 NOT NULL,
                                              "user_id" int8 NOT NULL,
                                              "policy_id" int8 NOT NULL,
                                              "create_by" int8 NOT NULL,
                                              "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                              "update_by" int8 NOT NULL,
                                              "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                              "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                              "deleted_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."sys_user_perm_rel"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."policy_id" IS '权限策略ID';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_user_perm_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_perm_rel" IS '用户与权限策略直接关联表';

-- ----------------------------
-- Records of sys_user_perm_rel
-- ----------------------------
INSERT INTO "public"."sys_user_perm_rel" VALUES (1, 2, 1, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (2, 2, 3, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (3, 2, 7, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (4, 2, 8, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (5, 2, 9, 1, '2025-01-15 00:00:00', 2, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (6, 2, 12, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (7, 2, 16, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (8, 2, 17, 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (9, 3, 1, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (10, 3, 7, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (11, 3, 10, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (12, 3, 12, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (13, 3, 16, 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (14, 4, 1, 3, '2025-02-15 00:00:00', 4, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (15, 4, 11, 3, '2025-02-15 00:00:00', 4, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (16, 5, 1, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (17, 5, 6, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (18, 5, 3, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (19, 5, 9, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (20, 5, 16, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (21, 5, 17, 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (22, 6, 1, 3, '2025-03-01 00:00:00', 6, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (23, 6, 10, 3, '2025-03-01 00:00:00', 6, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (24, 6, 16, 3, '2025-03-01 00:00:00', 6, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (25, 7, 1, 4, '2025-03-01 00:00:00', 7, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (26, 7, 11, 4, '2025-03-01 00:00:00', 7, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (27, 8, 1, 3, '2025-03-01 00:00:00', 8, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (28, 8, 10, 3, '2025-03-01 00:00:00', 8, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (29, 9, 1, 2, '2025-03-01 00:00:00', 9, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (30, 9, 3, 2, '2025-03-01 00:00:00', 9, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (31, 9, 15, 2, '2025-03-01 00:00:00', 9, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (32, 10, 1, 2, '2025-03-01 00:00:00', 10, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (33, 10, 22, 2, '2025-03-01 00:00:00', 10, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (34, 11, 1, 2, '2025-03-01 00:00:00', 11, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (35, 14, 1, 2, '2025-03-01 00:00:00', 14, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (36, 14, 16, 2, '2025-03-01 00:00:00', 14, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (37, 14, 20, 2, '2025-03-01 00:00:00', 14, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (38, 19, 1, 2, '2025-03-01 00:00:00', 19, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (39, 19, 16, 2, '2025-03-01 00:00:00', 19, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_perm_rel" VALUES (40, 20, 1, 3, '2025-03-01 00:00:00', 20, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Table structure for sys_user_tenant_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_tenant_rel";
CREATE TABLE "public"."sys_user_tenant_rel" (
                                                "id" int8 NOT NULL,
                                                "user_id" int8 NOT NULL,
                                                "tenant_id" int8 NOT NULL,
                                                "dept_id" int8 NOT NULL,
                                                "is_admin" bool NOT NULL,
                                                "join_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                "is_default" bool NOT NULL,
                                                "create_by" int8 NOT NULL,
                                                "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                "update_by" int8 NOT NULL,
                                                "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."is_admin" IS '是否为该租户管理员';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."join_time" IS '加入租户时间';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."is_default" IS '是否默认租户';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_user_tenant_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_tenant_rel" IS '用户与租户的关联关系';

-- ----------------------------
-- Records of sys_user_tenant_rel
-- ----------------------------
INSERT INTO "public"."sys_user_tenant_rel" VALUES (1, 1, 1, 0, 't', '2025-01-01 00:00:00', 't', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (2, 2, 2, 0, 't', '2025-01-15 00:00:00', 't', 1, '2025-01-15 00:00:00', 2, '2025-01-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (3, 3, 3, 0, 't', '2025-02-01 00:00:00', 't', 2, '2025-02-01 00:00:00', 3, '2025-02-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (4, 4, 4, 0, 't', '2025-02-15 00:00:00', 't', 3, '2025-02-15 00:00:00', 4, '2025-02-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (5, 5, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 5, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (6, 6, 3, 0, 'f', '2025-03-01 00:00:00', 't', 3, '2025-03-01 00:00:00', 6, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (7, 7, 4, 0, 'f', '2025-03-01 00:00:00', 't', 4, '2025-03-01 00:00:00', 7, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (8, 8, 5, 0, 'f', '2025-03-01 00:00:00', 't', 3, '2025-03-01 00:00:00', 8, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (9, 9, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 9, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (10, 10, 8, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 10, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (11, 11, 9, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 11, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (12, 12, 10, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 12, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (13, 13, 6, 0, 'f', '2025-03-01 00:00:00', 't', 3, '2025-03-01 00:00:00', 13, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (14, 14, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 14, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (15, 15, 13, 0, 't', '2025-03-01 00:00:00', 't', 1, '2025-03-01 00:00:00', 15, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (16, 16, 14, 0, 't', '2025-03-15 00:00:00', 't', 13, '2025-03-15 00:00:00', 16, '2025-03-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (17, 17, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 2, '2025-05-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (18, 18, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 2, '2025-05-20 00:00:00', 'DELETED', '2025-05-20 00:00:00');
INSERT INTO "public"."sys_user_tenant_rel" VALUES (19, 19, 2, 0, 'f', '2025-03-01 00:00:00', 't', 2, '2025-03-01 00:00:00', 19, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (20, 20, 3, 0, 'f', '2025-03-01 00:00:00', 't', 3, '2025-03-01 00:00:00', 20, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (21, 5, 3, 0, 'f', '2025-04-01 00:00:00', 'f', 5, '2025-04-01 00:00:00', 5, '2025-04-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (22, 9, 3, 0, 'f', '2025-04-01 00:00:00', 'f', 9, '2025-04-01 00:00:00', 9, '2025-04-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (23, 14, 3, 0, 'f', '2025-04-01 00:00:00', 'f', 14, '2025-04-01 00:00:00', 14, '2025-04-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (24, 6, 4, 0, 'f', '2025-04-15 00:00:00', 'f', 6, '2025-04-15 00:00:00', 6, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (25, 7, 5, 0, 'f', '2025-04-15 00:00:00', 'f', 7, '2025-04-15 00:00:00', 7, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (26, 10, 2, 0, 'f', '2025-04-15 00:00:00', 'f', 10, '2025-04-15 00:00:00', 10, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (27, 11, 2, 0, 'f', '2025-04-15 00:00:00', 'f', 11, '2025-04-15 00:00:00', 11, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_user_tenant_rel" VALUES (28, 12, 9, 0, 'f', '2025-04-15 00:00:00', 'f', 12, '2025-04-15 00:00:00', 12, '2025-04-15 00:00:00', 'NOT_DELETED', NULL);

-- ----------------------------
-- Primary Key structure for table sys_perm
-- ----------------------------
ALTER TABLE "public"."sys_perm" ADD CONSTRAINT "sys_perm_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_perm_policy
-- ----------------------------
ALTER TABLE "public"."sys_perm_policy" ADD CONSTRAINT "sys_perm_policy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant
-- ----------------------------
ALTER TABLE "public"."sys_tenant" ADD CONSTRAINT "sys_tenant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant_subscription
-- ----------------------------
ALTER TABLE "public"."sys_tenant_subscription" ADD CONSTRAINT "sys_tenant_subscription_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_perm_rel
-- ----------------------------
ALTER TABLE "public"."sys_user_perm_rel" ADD CONSTRAINT "sys_user_perm_rel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_tenant_rel
-- ----------------------------
ALTER TABLE "public"."sys_user_tenant_rel" ADD CONSTRAINT "sys_user_tenant_rel_pkey" PRIMARY KEY ("id");
