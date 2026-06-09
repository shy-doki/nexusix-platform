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
                                       "disable_reason" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL,
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
);

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
COMMENT ON COLUMN "public"."sys_tenant"."disable_reason" IS '禁用原因';
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
INSERT INTO "public"."sys_tenant" VALUES (1, 'PLAT', '平台运营方', '互联网', '平台顶级租户', '/logo/plat.png', 0, '-', '-', '/1/', '管理员', '13800000001', 'ENABLED', NULL, '2099-12-31 00:00:00', NULL, '旗舰版', '{}', 't', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (2, 'GRP_A', '集团A', '互联网', '一级企业租户', '/logo/grp_a.png', 1, 'PLAT', '平台运营方', '/1/2/', '张总', '13800000002', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '企业版', '{"scale": "500+", "industry": "电商"}', 't', 1, '2025-01-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (3, 'GRP_A_EAST', '集团A-华东分公司', '互联网', '二级分公司', '/logo/east.png', 2, 'GRP_A', '集团A', '/1/2/3/', '李经理', '13800000003', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (4, 'GRP_A_EAST_TECH', '华东-技术部', '互联网', '三级部门', '/logo/tech.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/4/', '王主管', '13800000004', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 4, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (5, 'GRP_A_EAST_SALE', '华东-销售部', '互联网', '三级部门', '/logo/sale.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/5/', '赵主管', '13800000020', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (6, 'GRP_A_EAST_HR', '华东-人事部', '互联网', '三级部门', '/logo/hr.png', 3, 'GRP_A_EAST', '集团A-华东分公司', '/1/2/3/6/', '钱主管', '13800000021', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 3, '2025-02-15 00:00:00', 3, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (7, 'GRP_A_SOUTH', '集团A-华南分公司', '互联网', '二级分公司', '/logo/south.png', 2, 'GRP_A', '集团A', '/1/2/7/', '孙经理', '13800000022', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (8, 'GRP_A_SOUTH_FIN', '华南-财务部', '互联网', '三级部门', '/logo/fin.png', 7, 'GRP_A_SOUTH', '集团A-华南分公司', '/1/2/7/8/', '周主管', '13800000023', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 2, '2025-02-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (9, 'GRP_A_NORTH', '集团A-华北分公司', '互联网', '二级分公司', '/logo/north.png', 2, 'GRP_A', '集团A', '/1/2/9/', '吴经理', '13800000024', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '商业版', '{}', 't', 2, '2025-02-01 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (10, 'GRP_A_NORTH_DEV', '华北-研发部', '互联网', '三级部门', '/logo/dev.png', 9, 'GRP_A_NORTH', '集团A-华北分公司', '/1/2/9/10/', '郑主管', '13800000025', 'ENABLED', NULL, '2027-06-30 00:00:00', NULL, '基础版', '{}', 'f', 2, '2025-02-15 00:00:00', 2, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (11, 'GRP_B', '集团B', '餐饮', '已停用租户', '/logo/grp_b.png', 1, 'PLAT', '平台运营方', '/1/11/', '陈总', '13800000005', 'DISABLED', '租户主动停用/平台管理员禁用', '2025-01-01 00:00:00', NULL, '基础版', '{}', 'f', 1, '2025-01-20 00:00:00', 1, '2025-06-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (12, 'GRP_C', '集团C', '制造', '已过期租户', '/logo/grp_c.png', 1, 'PLAT', '平台运营方', '/1/12/', '周总', '13800000006', 'EXPIRED', '租户订阅套餐已过期', '2024-12-31 00:00:00', NULL, '基础版', '{}', 'f', 1, '2024-06-01 00:00:00', 1, '2025-01-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (13, 'GRP_D', '集团D', '互联网', '二级集团', '/logo/grp_d.png', 1, 'PLAT', '平台运营方', '/1/13/', '冯总', '13800000030', 'ENABLED', NULL, '2027-12-31 00:00:00', NULL, '企业版', '{"industry": "教育"}', 't', 1, '2025-03-01 00:00:00', 1, '2025-03-01 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (14, 'GRP_D_EDU', '集团D-教育事业部', '互联网', '二级分公司', '/logo/edu.png', 13, 'GRP_D', '集团D', '/1/13/14/', '褚经理', '13800000031', 'ENABLED', NULL, '2027-12-31 00:00:00', NULL, '商业版', '{}', 'f', 13, '2025-03-15 00:00:00', 13, '2025-03-15 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (15, 'GRP_DELETED', '已删除集团', '互联网', '逻辑删除演示', '/logo/del.png', 1, 'PLAT', '平台运营方', '/1/15/', '卫总', '13800000007', 'ENABLED', NULL, '2026-12-31 00:00:00', NULL, '基础版', '{}', 'f', 1, '2025-01-01 00:00:00', 1, '2025-01-01 00:00:00', 'DELETED', '2025-04-01 00:00:00');
INSERT INTO "public"."sys_tenant" VALUES (16, 'GOV_PLAT', '政务总平台', '政务', '全国政务统一运营根租户', '/logo/gov_plat.png', 0, '-', '-', '/16/', '李主任', '13900000001', 'ENABLED', NULL, '2099-12-31 00:00:00', NULL, '旗舰版', '{"region":"全国","level":"国家级"}', 't', 1, '2025-06-10 00:00:00', 1, '2025-06-10 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (17, 'EDU_PLAT', '教育总平台', '教育', '全国教育行业根租户', '/logo/edu_plat.png', 0, '-', '-', '/17/', '王院长', '13900000002', 'ENABLED', NULL, '2099-12-31 00:00:00', NULL, '旗舰版', '{"industry":"教育","scope":"全学段"}', 't', 1, '2025-06-10 00:00:00', 1, '2025-06-10 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (18, 'FIN_PLAT', '金融总平台', '金融', '金融行业统一根租户', '/logo/fin_plat.png', 0, '-', '-', '/18/', '赵行长', '13900000003', 'ENABLED', NULL, '2099-12-31 00:00:00', NULL, '旗舰版', '{"industry":"金融","type":"综合金融"}', 't', 1, '2025-06-10 00:00:00', 1, '2025-06-10 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (19, 'GOV_BJ', '北京政务体系', '政务', '北京市政务二级租户', '/logo/gov_bj.png', 16, 'GOV_PLAT', '政务总平台', '/16/19/', '张局长', '13900000004', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '企业版', '{"city":"北京"}', 't', 1, '2025-06-11 00:00:00', 16, '2025-06-11 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (20, 'GOV_SH', '上海政务体系', '政务', '上海市政务二级租户', '/logo/gov_sh.png', 16, 'GOV_PLAT', '政务总平台', '/16/20/', '刘局长', '13900000005', 'DISABLED', '平台统一暂停服务，临时禁用', '2028-05-30 00:00:00', NULL, '企业版', '{"city":"上海"}', 't', 1, '2025-06-11 00:00:00', 16, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (21, 'GOV_GZ', '广州政务体系', '政务', '广州市政务二级租户', '/logo/gov_gz.png', 16, 'GOV_PLAT', '政务总平台', '/16/21/', '陈局长', '13900000006', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '企业版', '{"city":"广州"}', 'f', 1, '2025-06-11 00:00:00', 16, '2025-06-11 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (22, 'EDU_UNI', '高校联盟体系', '教育', '全国高校二级租户', '/logo/edu_uni.png', 17, 'EDU_PLAT', '教育总平台', '/17/22/', '马校长', '13900000007', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '企业版', '{"type":"高等教育"}', 't', 1, '2025-06-11 00:00:00', 17, '2025-06-11 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (23, 'EDU_K12', '中小学体系', '教育', 'K12基础教育二级租户', '/logo/edu_k12.png', 17, 'EDU_PLAT', '教育总平台', '/17/23/', '林校长', '13900000008', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '商业版', '{"type":"基础教育"}', 'f', 1, '2025-06-11 00:00:00', 17, '2025-06-11 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (24, 'FIN_BANK', '银行板块', '金融', '银行业务二级租户', '/logo/fin_bank.png', 18, 'FIN_PLAT', '金融总平台', '/18/24/', '孙总', '13900000009', 'ENABLED', NULL, '2028-10-31 00:00:00', NULL, '企业版', '{"biz":"银行"}', 't', 1, '2025-06-11 00:00:00', 18, '2025-06-11 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (25, 'FIN_INS', '保险板块', '金融', '保险业务二级租户', '/logo/fin_ins.png', 18, 'FIN_PLAT', '金融总平台', '/18/25/', '周总', '13900000010', 'EXPIRED', '套餐服务到期，未续费自动停用', '2024-12-31 00:00:00', NULL, '商业版', '{"biz":"保险"}', 'f', 1, '2025-06-11 00:00:00', 18, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (26, 'GOV_BJ_ADMIN', '北京政务办公厅', '政务', '市级政务办公部门', '/logo/gov_bj_admin.png', 19, 'GOV_BJ', '北京政务体系', '/16/19/26/', '吴主任', '13900000011', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '商业版', '{"dept":"综合办公"}', 'f', 19, '2025-06-12 00:00:00', 19, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (27, 'GOV_BJ_TECH', '北京政务技术中心', '政务', '政务信息化技术部门', '/logo/gov_bj_tech.png', 19, 'GOV_BJ', '北京政务体系', '/16/19/27/', '郑主任', '13900000012', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '商业版', '{"dept":"技术运维"}', 't', 19, '2025-06-12 00:00:00', 19, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (28, 'EDU_UNI_TSING', '清华大学', '教育', '顶尖综合类高校', '/logo/edu_tsing.png', 22, 'EDU_UNI', '高校联盟体系', '/17/22/28/', '黄校长', '13900000013', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '企业版', '{"school":"综合大学"}', 't', 22, '2025-06-12 00:00:00', 22, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (29, 'EDU_UNI_PKU', '北京大学', '教育', '人文社科类高校', '/logo/edu_pku.png', 22, 'EDU_UNI', '高校联盟体系', '/17/22/29/', '徐校长', '13900000014', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '企业版', '{"school":"文科强校"}', 't', 22, '2025-06-12 00:00:00', 22, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (30, 'EDU_UNI_FDU', '复旦大学', '教育', '华东重点高校', '/logo/edu_fdu.png', 22, 'EDU_UNI', '高校联盟体系', '/17/22/30/', '朱校长', '13900000015', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '商业版', '{"school":"华东名校"}', 'f', 22, '2025-06-12 00:00:00', 22, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (31, 'FIN_BANK_CMBC', '招商银行', '金融', '全国股份制银行', '/logo/fin_cmbc.png', 24, 'FIN_BANK', '银行板块', '/18/24/31/', '何行长', '13900000016', 'EXPIRED', '年度服务套餐到期，暂停服务', '2024-11-30 00:00:00', NULL, '商业版', '{"bank":"股份制银行"}', 'f', 24, '2025-06-12 00:00:00', 24, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (32, 'FIN_BANK_ICBC', '工商银行', '金融', '国有大型商业银行', '/logo/fin_icbc.png', 24, 'FIN_BANK', '银行板块', '/18/24/32/', '郭行长', '13900000017', 'ENABLED', NULL, '2028-10-31 00:00:00', NULL, '企业版', '{"bank":"国有大行"}', 't', 24, '2025-06-12 00:00:00', 24, '2025-06-12 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (33, 'GOV_BJ_TECH_DEV', '技术研发组', '政务', '系统研发小组', '/logo/gov_dev.png', 27, 'GOV_BJ_TECH', '北京政务技术中心', '/16/19/27/33/', '马工', '13900000018', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '基础版', '{"group":"研发"}', 'f', 27, '2025-06-13 00:00:00', 27, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (34, 'GOV_BJ_TECH_OP', '运维保障组', '政务', '线上运维小组', '/logo/gov_op.png', 27, 'GOV_BJ_TECH', '北京政务技术中心', '/16/19/27/34/', '梁工', '13900000019', 'ENABLED', NULL, '2028-05-30 00:00:00', NULL, '基础版', '{"group":"运维"}', 'f', 27, '2025-06-13 00:00:00', 27, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (35, 'EDU_TSING_CS', '计算机学院', '教育', '计算机相关院系', '/logo/edu_cs.png', 28, 'EDU_UNI_TSING', '清华大学', '/17/22/28/35/', '崔院长', '13900000020', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '基础版', '{"college":"计算机"}', 'f', 28, '2025-06-13 00:00:00', 28, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (36, 'EDU_TSING_ME', '机械工程学院', '教育', '机械工科院系', '/logo/edu_me.png', 28, 'EDU_UNI_TSING', '清华大学', '/17/22/28/36/', '方院长', '13900000021', 'ENABLED', NULL, '2028-08-31 00:00:00', NULL, '基础版', '{"college":"机械"}', 'f', 28, '2025-06-13 00:00:00', 28, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (37, 'FIN_ICBC_RETAIL', '零售业务部', '金融', '个人金融业务部门', '/logo/fin_retail.png', 32, 'FIN_BANK_ICBC', '工商银行', '/18/24/32/37/', '钟经理', '13900000022', 'ENABLED', NULL, '2028-10-31 00:00:00', NULL, '基础版', '{"dept":"零售业务"}', 'f', 32, '2025-06-13 00:00:00', 32, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (38, 'FIN_ICBC_CORP', '公司业务部', '金融', '对公金融业务部门', '/logo/fin_corp.png', 32, 'FIN_BANK_ICBC', '工商银行', '/18/24/32/38/', '吕经理', '13900000023', 'ENABLED', NULL, '2028-10-31 00:00:00', NULL, '基础版', '{"dept":"公司业务"}', 'f', 32, '2025-06-13 00:00:00', 32, '2025-06-13 00:00:00', 'NOT_DELETED', NULL);
INSERT INTO "public"."sys_tenant" VALUES (39, 'TEST_DEL_TENANT', '测试删除租户', '互联网', '用于逻辑删除演示', '/logo/test_del.png', 16, 'GOV_PLAT', '政务总平台', '/16/39/', '测试员', '13900000099', 'ENABLED', NULL, '2027-01-01 00:00:00', NULL, '基础版', '{}', 'f', 1, '2025-06-13 00:00:00', 1, '2025-06-14 00:00:00', 'DELETED', '2025-06-14 00:00:00');

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

-- ----------------------------
-- Table structure for prod_package
-- ----------------------------
DROP TABLE IF EXISTS "public"."prod_package";
CREATE TABLE "public"."prod_package" (
                                        "id" int8 NOT NULL,
                                        "package_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                        "package_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                        "description" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                        "price" numeric(10,2) DEFAULT 0.00,
                                        "cycle_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                        "cycle_value" int4 DEFAULT 1,
                                        "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                        "sort_order" int4 DEFAULT 0,
                                        "ext_attributes" jsonb DEFAULT '{}'::jsonb,
                                        "create_by" int8 NOT NULL,
                                        "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                        "update_by" int8 NOT NULL,
                                        "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                        "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                        "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."prod_package"."id" IS '主键ID';
COMMENT ON COLUMN "public"."prod_package"."package_name" IS '套餐名称';
COMMENT ON COLUMN "public"."prod_package"."package_code" IS '套餐编码';
COMMENT ON COLUMN "public"."prod_package"."description" IS '套餐描述';
COMMENT ON COLUMN "public"."prod_package"."price" IS '价格';
COMMENT ON COLUMN "public"."prod_package"."cycle_type" IS '周期类型';
COMMENT ON COLUMN "public"."prod_package"."cycle_value" IS '周期数值';
COMMENT ON COLUMN "public"."prod_package"."status" IS '套餐状态';
COMMENT ON COLUMN "public"."prod_package"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."prod_package"."ext_attributes" IS '扩展属性，JSON格式';
COMMENT ON COLUMN "public"."prod_package"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."prod_package"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."prod_package"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."prod_package"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."prod_package"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."prod_package"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."prod_package" IS '产品套餐定义表';

-- ----------------------------
-- Table structure for prod_package_quota
-- ----------------------------
DROP TABLE IF EXISTS "public"."prod_package_quota";
CREATE TABLE "public"."prod_package_quota" (
                                               "id" int8 NOT NULL,
                                               "package_id" int8 NOT NULL,
                                               "resource_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                               "resource_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                               "quota_value" int8 NOT NULL,
                                               "unit" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                               "is_allow_overage" bool NOT NULL,
                                               "create_by" int8 NOT NULL,
                                               "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                               "update_by" int8 NOT NULL,
                                               "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                               "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                               "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."prod_package_quota"."id" IS '主键ID';
COMMENT ON COLUMN "public"."prod_package_quota"."package_id" IS '关联套餐ID';
COMMENT ON COLUMN "public"."prod_package_quota"."resource_code" IS '资源类型代码';
COMMENT ON COLUMN "public"."prod_package_quota"."resource_name" IS '资源名称';
COMMENT ON COLUMN "public"."prod_package_quota"."quota_value" IS '配额数值';
COMMENT ON COLUMN "public"."prod_package_quota"."unit" IS '单位';
COMMENT ON COLUMN "public"."prod_package_quota"."is_allow_overage" IS '是否允许超额使用';
COMMENT ON COLUMN "public"."prod_package_quota"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."prod_package_quota"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."prod_package_quota"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."prod_package_quota"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."prod_package_quota"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."prod_package_quota"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."prod_package_quota" IS '套餐配额模板表';

-- ----------------------------
-- Table structure for sys_tenant_quota_adjustment
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant_quota_adjustment";
CREATE TABLE "public"."sys_tenant_quota_adjustment" (
                                                        "id" int8 NOT NULL,
                                                        "tenant_id" int8 NOT NULL,
                                                        "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "resource_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "adjust_value" int8 NOT NULL,
                                                        "adjust_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "reason" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "effective_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                        "expire_time" timestamp(6),
                                                        "operator_id" int8 NOT NULL,
                                                        "create_by" int8 NOT NULL,
                                                        "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                        "update_by" int8 NOT NULL,
                                                        "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                                        "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                                        "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."tenant_name" IS '租户名称';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."resource_code" IS '资源类型代码';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."adjust_value" IS '调整数值';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."adjust_type" IS '调整类型';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."reason" IS '调整原因';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."effective_time" IS '生效时间';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."expire_time" IS '过期时间';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."operator_id" IS '操作人ID';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_tenant_quota_adjustment"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_tenant_quota_adjustment" IS '租户配额调整表';

-- ----------------------------
-- Table structure for sys_resource_usage
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_resource_usage";
CREATE TABLE "public"."sys_resource_usage" (
                                               "id" int8 NOT NULL,
                                               "tenant_id" int8 NOT NULL,
                                               "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                               "resource_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                               "usage_value" int8 NOT NULL,
                                               "business_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                               "business_id" int8 NOT NULL,
                                               "usage_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                               "remark" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                               "create_by" int8 NOT NULL,
                                               "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                               "update_by" int8 NOT NULL,
                                               "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                               "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                               "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_resource_usage"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_resource_usage"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_resource_usage"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_resource_usage"."resource_code" IS '资源类型代码';
COMMENT ON COLUMN "public"."sys_resource_usage"."usage_value" IS '消耗数值';
COMMENT ON COLUMN "public"."sys_resource_usage"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."sys_resource_usage"."business_id" IS '关联业务ID';
COMMENT ON COLUMN "public"."sys_resource_usage"."usage_time" IS '消耗时间';
COMMENT ON COLUMN "public"."sys_resource_usage"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_resource_usage"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_resource_usage"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_resource_usage"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_resource_usage"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_resource_usage"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."sys_resource_usage"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_resource_usage" IS '资源使用计量表';

-- ----------------------------
-- Table structure for bill_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."bill_order";
CREATE TABLE "public"."bill_order" (
                                       "id" int8 NOT NULL,
                                       "tenant_id" int8 NOT NULL,
                                       "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                       "order_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                       "product_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "product_id" int8 NOT NULL,
                                       "total_amount" numeric(10,2) NOT NULL,
                                       "pay_amount" numeric(10,2) DEFAULT 0.00,
                                       "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "pay_time" timestamp(6),
                                       "pay_channel" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                       "order_items" jsonb DEFAULT '[]'::jsonb,
                                       "ext_attributes" jsonb DEFAULT '{}'::jsonb,
                                       "create_by" int8 NOT NULL,
                                       "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "update_by" int8 NOT NULL,
                                       "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                       "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."bill_order"."id" IS '主键ID';
COMMENT ON COLUMN "public"."bill_order"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."bill_order"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."bill_order"."tenant_name" IS '租户名称';
COMMENT ON COLUMN "public"."bill_order"."order_no" IS '订单号';
COMMENT ON COLUMN "public"."bill_order"."product_type" IS '产品类型';
COMMENT ON COLUMN "public"."bill_order"."product_id" IS '产品ID';
COMMENT ON COLUMN "public"."bill_order"."total_amount" IS '订单总金额';
COMMENT ON COLUMN "public"."bill_order"."pay_amount" IS '实际支付金额';
COMMENT ON COLUMN "public"."bill_order"."status" IS '订单状态';
COMMENT ON COLUMN "public"."bill_order"."pay_time" IS '支付时间';
COMMENT ON COLUMN "public"."bill_order"."pay_channel" IS '支付渠道';
COMMENT ON COLUMN "public"."bill_order"."order_items" IS '订单明细，JSON格式';
COMMENT ON COLUMN "public"."bill_order"."ext_attributes" IS '扩展属性，JSON格式';
COMMENT ON COLUMN "public"."bill_order"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."bill_order"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."bill_order"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."bill_order"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."bill_order"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."bill_order"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."bill_order" IS '订单表';

-- ----------------------------
-- Table structure for bill_invoice
-- ----------------------------
DROP TABLE IF EXISTS "public"."bill_invoice";
CREATE TABLE "public"."bill_invoice" (
                                         "id" int8 NOT NULL,
                                         "tenant_id" int8 NOT NULL,
                                         "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                         "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                         "order_id" int8 NOT NULL,
                                         "invoice_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "invoice_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                         "invoice_title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
                                         "tax_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
                                         "amount" numeric(10,2) NOT NULL,
                                         "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                         "invoice_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
                                         "create_by" int8 NOT NULL,
                                         "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                         "update_by" int8 NOT NULL,
                                         "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                         "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
                                         "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."bill_invoice"."id" IS '主键ID';
COMMENT ON COLUMN "public"."bill_invoice"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."bill_invoice"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."bill_invoice"."tenant_name" IS '租户名称';
COMMENT ON COLUMN "public"."bill_invoice"."order_id" IS '关联订单ID';
COMMENT ON COLUMN "public"."bill_invoice"."invoice_no" IS '发票号码';
COMMENT ON COLUMN "public"."bill_invoice"."invoice_type" IS '发票类型';
COMMENT ON COLUMN "public"."bill_invoice"."invoice_title" IS '发票抬头';
COMMENT ON COLUMN "public"."bill_invoice"."tax_id" IS '税号';
COMMENT ON COLUMN "public"."bill_invoice"."amount" IS '发票金额';
COMMENT ON COLUMN "public"."bill_invoice"."status" IS '发票状态';
COMMENT ON COLUMN "public"."bill_invoice"."invoice_url" IS '发票文件URL';
COMMENT ON COLUMN "public"."bill_invoice"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."bill_invoice"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."bill_invoice"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."bill_invoice"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."bill_invoice"."is_deleted" IS '删除状态';
COMMENT ON COLUMN "public"."bill_invoice"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."bill_invoice" IS '发票管理表';

-- ----------------------------
-- Primary Key structure for table prod_package
-- ----------------------------
ALTER TABLE "public"."prod_package" ADD CONSTRAINT "prod_package_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table prod_package_quota
-- ----------------------------
ALTER TABLE "public"."prod_package_quota" ADD CONSTRAINT "prod_package_quota_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant_quota_adjustment
-- ----------------------------
ALTER TABLE "public"."sys_tenant_quota_adjustment" ADD CONSTRAINT "sys_tenant_quota_adjustment_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_resource_usage
-- ----------------------------
ALTER TABLE "public"."sys_resource_usage" ADD CONSTRAINT "sys_resource_usage_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table bill_order
-- ----------------------------
ALTER TABLE "public"."bill_order" ADD CONSTRAINT "bill_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table bill_invoice
-- ----------------------------
ALTER TABLE "public"."bill_invoice" ADD CONSTRAINT "bill_invoice_pkey" PRIMARY KEY ("id");
-- =============================================================================
-- 身份与访问控制域 (IAM) 补充�?- �?public.sql 设计规范转换
-- =============================================================================

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role" (
    "id" int8 NOT NULL,
    "role_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "role_desc" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "role_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "role_level" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "data_scope" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "sort_order" int4 NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_role"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_desc" IS '角色描述';
COMMENT ON COLUMN "public"."sys_role"."role_code" IS '角色编码';
COMMENT ON COLUMN "public"."sys_role"."role_level" IS '角色层级：SYSTEM/TENANT/USER';
COMMENT ON COLUMN "public"."sys_role"."tenant_id" IS '所属租户ID（系统级�?�?;
COMMENT ON COLUMN "public"."sys_role"."tenant_code" IS '租户编码（冗余）';
COMMENT ON COLUMN "public"."sys_role"."tenant_name" IS '租户名称（冗余）';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据权限范围：ALL/DEPT/DEPT_AND_SUB/SELF/CUSTOM';
COMMENT ON COLUMN "public"."sys_role"."sort_order" IS '排序�?;
COMMENT ON COLUMN "public"."sys_role"."status" IS '角色状态：ENABLED/DISABLED';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_role"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_role"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."is_deleted" IS '删除状态：NOT_DELETED/DELETED';
COMMENT ON COLUMN "public"."sys_role"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_role" IS '角色�?;

-- ----------------------------
-- Table structure for sys_role_policy
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_policy";
CREATE TABLE "public"."sys_role_policy" (
    "id" int8 NOT NULL,
    "policy_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "policy_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "target_id" int8 NOT NULL,
    "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "role_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_role_policy"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_role_policy"."policy_code" IS '策略编码';
COMMENT ON COLUMN "public"."sys_role_policy"."policy_name" IS '策略名称';
COMMENT ON COLUMN "public"."sys_role_policy"."target_id" IS '授权目标ID';
COMMENT ON COLUMN "public"."sys_role_policy"."target_type" IS '授权目标类型：TENANT/ROLE/USER';
COMMENT ON COLUMN "public"."sys_role_policy"."role_id" IS '关联角色ID';
COMMENT ON COLUMN "public"."sys_role_policy"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_role_policy"."status" IS '策略状态：ACTIVE/DISABLED_SYSTEM_LEVEL/DISABLED_TENANT_LEVEL/DISABLED_ROLE_LEVEL/DISABLED_USER_LEVEL';
COMMENT ON COLUMN "public"."sys_role_policy"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_role_policy"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role_policy"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_role_policy"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role_policy"."is_deleted" IS '删除状态：NOT_DELETED/DELETED';
COMMENT ON COLUMN "public"."sys_role_policy"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_role_policy" IS '角色策略控制�?;

-- ----------------------------
-- Table structure for sys_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role_rel";
CREATE TABLE "public"."sys_user_role_rel" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "role_id" int8 NOT NULL,
    "policy_id" int8 NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user_role_rel"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."policy_id" IS '权限策略ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_role_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_role_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_role_rel"."is_deleted" IS '删除状态：NOT_DELETED/DELETED';
COMMENT ON COLUMN "public"."sys_user_role_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_role_rel" IS '用户角色关联�?;

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_token";
CREATE TABLE "public"."sys_user_token" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "token" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "device_info" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "login_ip" varchar(45) COLLATE "pg_catalog"."default" NOT NULL,
    "login_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "expire_time" timestamp(6) NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user_token"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_token"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_token"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_user_token"."token" IS '登录令牌';
COMMENT ON COLUMN "public"."sys_user_token"."device_info" IS '设备信息';
COMMENT ON COLUMN "public"."sys_user_token"."login_ip" IS '登录IP';
COMMENT ON COLUMN "public"."sys_user_token"."login_at" IS '登录时间';
COMMENT ON COLUMN "public"."sys_user_token"."expire_time" IS '过期时间';
COMMENT ON COLUMN "public"."sys_user_token"."status" IS 'Token状态：ACTIVE/EXPIRED/REVOKED';
COMMENT ON COLUMN "public"."sys_user_token"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_token"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_token"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_token"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_token"."is_deleted" IS '删除状态：NOT_DELETED/DELETED';
COMMENT ON COLUMN "public"."sys_user_token"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_token" IS '用户Token记录�?;

-- ----------------------------
-- Table structure for sys_tenant_security
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant_security";
CREATE TABLE "public"."sys_tenant_security" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "pwd_min_length" int4 NOT NULL,
    "pwd_complexity" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "pwd_expire_days" int4 NOT NULL,
    "login_fail_limit" int4 NOT NULL,
    "lock_duration" int4 NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_tenant_security"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_tenant_security"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_tenant_security"."tenant_code" IS '租户编码（冗余）';
COMMENT ON COLUMN "public"."sys_tenant_security"."pwd_min_length" IS '密码最小长�?;
COMMENT ON COLUMN "public"."sys_tenant_security"."pwd_complexity" IS '密码复杂度：NONE/LETTER_NUMBER/LETTER_NUMBER_SPECIAL';
COMMENT ON COLUMN "public"."sys_tenant_security"."pwd_expire_days" IS '密码过期天数�?表示永不过期�?;
COMMENT ON COLUMN "public"."sys_tenant_security"."login_fail_limit" IS '登录失败锁定次数';
COMMENT ON COLUMN "public"."sys_tenant_security"."lock_duration" IS '锁定时长（分钟）';
COMMENT ON COLUMN "public"."sys_tenant_security"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_tenant_security"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_tenant_security"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_tenant_security"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_tenant_security"."is_deleted" IS '删除状态：NOT_DELETED/DELETED';
COMMENT ON COLUMN "public"."sys_tenant_security"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_tenant_security" IS '租户安全策略配置�?;

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_policy
-- ----------------------------
ALTER TABLE "public"."sys_role_policy" ADD CONSTRAINT "sys_role_policy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_role_rel
-- ----------------------------
ALTER TABLE "public"."sys_user_role_rel" ADD CONSTRAINT "sys_user_role_rel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_token
-- ----------------------------
ALTER TABLE "public"."sys_user_token" ADD CONSTRAINT "sys_user_token_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_tenant_security
-- ----------------------------
ALTER TABLE "public"."sys_tenant_security" ADD CONSTRAINT "sys_tenant_security_pkey" PRIMARY KEY ("id");
-- =============================================================================
-- 组织架构�?(Organization) - �?public.sql 设计规范转换
-- =============================================================================

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept" (
    "id" int8 NOT NULL,
    "dept_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id" int8 NOT NULL,
    "path" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "leader_id" int8 NOT NULL,
    "phone" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "sort_order" int4 NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "dept_desc" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dept"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_dept"."parent_id" IS '父部门ID';
COMMENT ON COLUMN "public"."sys_dept"."path" IS '部门层级路径';
COMMENT ON COLUMN "public"."sys_dept"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_dept"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_dept"."leader_id" IS '部门负责人ID';
COMMENT ON COLUMN "public"."sys_dept"."phone" IS '部门电话';
COMMENT ON COLUMN "public"."sys_dept"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."sys_dept"."status" IS '部门状�?;
COMMENT ON COLUMN "public"."sys_dept"."dept_desc" IS '部门描述';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_dept"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_dept"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dept"."is_deleted" IS '删除状�?;
COMMENT ON COLUMN "public"."sys_dept"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_dept" IS '部门信息�?;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post" (
    "id" int8 NOT NULL,
    "post_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "post_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "dept_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "sort_order" int4 NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "post_desc" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_post"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_post"."post_name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_post"."post_code" IS '岗位编码';
COMMENT ON COLUMN "public"."sys_post"."dept_id" IS '所属部门ID';
COMMENT ON COLUMN "public"."sys_post"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_post"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_post"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."sys_post"."status" IS '岗位状�?;
COMMENT ON COLUMN "public"."sys_post"."post_desc" IS '岗位描述';
COMMENT ON COLUMN "public"."sys_post"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_post"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_post"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_post"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_post"."is_deleted" IS '删除状�?;
COMMENT ON COLUMN "public"."sys_post"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_post" IS '岗位信息�?;

-- ----------------------------
-- Table structure for sys_user_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_group";
CREATE TABLE "public"."sys_user_group" (
    "id" int8 NOT NULL,
    "group_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "group_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "description" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user_group"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_group"."group_name" IS '用户组名�?;
COMMENT ON COLUMN "public"."sys_user_group"."group_type" IS '用户组类�?;
COMMENT ON COLUMN "public"."sys_user_group"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_user_group"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_user_group"."description" IS '描述';
COMMENT ON COLUMN "public"."sys_user_group"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_group"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_group"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_group"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_group"."is_deleted" IS '删除状�?;
COMMENT ON COLUMN "public"."sys_user_group"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_group" IS '用户组信息表';

-- ----------------------------
-- Table structure for sys_user_group_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_group_rel";
CREATE TABLE "public"."sys_user_group_rel" (
    "id" int8 NOT NULL,
    "group_id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_user_group_rel"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_group_rel"."group_id" IS '用户组ID';
COMMENT ON COLUMN "public"."sys_user_group_rel"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_group_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_user_group_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_group_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_user_group_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_group_rel"."is_deleted" IS '删除状�?;
COMMENT ON COLUMN "public"."sys_user_group_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_user_group_rel" IS '用户组成员关联表';

-- ----------------------------
-- Table structure for sys_role_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept_rel";
CREATE TABLE "public"."sys_role_dept_rel" (
    "id" int8 NOT NULL,
    "role_id" int8 NOT NULL,
    "dept_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_role_dept_rel"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."tenant_code" IS '租户编码';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role_dept_rel"."is_deleted" IS '删除状�?;
COMMENT ON COLUMN "public"."sys_role_dept_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_role_dept_rel" IS '角色数据权限关联�?;

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept" ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post" ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_group
-- ----------------------------
ALTER TABLE "public"."sys_user_group" ADD CONSTRAINT "sys_user_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_user_group_rel
-- ----------------------------
ALTER TABLE "public"."sys_user_group_rel" ADD CONSTRAINT "sys_user_group_rel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_dept_rel
-- ----------------------------
ALTER TABLE "public"."sys_role_dept_rel" ADD CONSTRAINT "sys_role_dept_rel_pkey" PRIMARY KEY ("id");
/*
 NexusIX Platform - 系统支撑�?/ 动态配置域 / 消息通知�?/ 审计�?/ 行业适配�?
 Source Server Type    : PostgreSQL
 Source Server Version : 170005
 Source Schema         : public

 设计规范�?   1. 主键：BIGINT NOT NULL，由雪花算法生成
   2. 状态字段：VARCHAR(20)，使用字符串枚举
   3. 时间字段命名：create_at / update_at / deleted_at
   4. 逻辑删除：is_deleted VARCHAR(20)，枚举�?NOT_DELETED/DELETED
   5. 冗余字段：关联表中适当冗余名称字段
   6. 审计字段：create_by, create_at, update_by, update_at, is_deleted, deleted_at
   7. 所有VARCHAR字段使用 COLLATE "pg_catalog"."default"
   8. 表结构使用双引号包裹：如 "public"."表名"
   9. 每个字段和表都要有COMMENT ON注释
   10. 添加PRIMARY KEY约束
   11. JSONB字段默认值：对象�?{}'::jsonb，数组用'[]'::jsonb

 Date: 09/06/2026
*/


-- =============================================================================
-- 1. 系统支撑�?(System Support)
-- =============================================================================

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu" (
    "id" int8 NOT NULL,
    "menu_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "menu_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id" int8 NOT NULL,
    "parent_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "menu_desc" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "icon" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "path" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "component" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "perm_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "visible" bool NOT NULL DEFAULT TRUE,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "sort_order" int4 DEFAULT 0,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_menu"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型(DIRECTORY/MENU/BUTTON)';
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID(0为根菜单)';
COMMENT ON COLUMN "public"."sys_menu"."parent_name" IS '父菜单名�?冗余)';
COMMENT ON COLUMN "public"."sys_menu"."menu_desc" IS '菜单描述';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件路径';
COMMENT ON COLUMN "public"."sys_menu"."perm_code" IS '权限标识';
COMMENT ON COLUMN "public"."sys_menu"."visible" IS '是否可见';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '菜单状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_menu"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_menu"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_menu"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_menu"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_menu" IS '菜单�?前端导航与按钮权限映�?;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict";
CREATE TABLE "public"."sys_dict" (
    "id" int8 NOT NULL,
    "dict_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "dict_type" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "remark" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_dict"."dict_name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict"."dict_type" IS '字典类型编码';
COMMENT ON COLUMN "public"."sys_dict"."tenant_id" IS '所属租户ID(0为系统字�?';
COMMENT ON COLUMN "public"."sys_dict"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_dict"."status" IS '字典状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_dict"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_dict"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_dict"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_dict"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_dict"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_dict" IS '字典类型�?定义系统常量类型';

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_item";
CREATE TABLE "public"."sys_dict_item" (
    "id" int8 NOT NULL,
    "dict_id" int8 NOT NULL,
    "dict_label" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "dict_value" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "sort_order" int4 DEFAULT 0,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dict_item"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_dict_item"."dict_id" IS '关联字典类型ID';
COMMENT ON COLUMN "public"."sys_dict_item"."dict_label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_item"."dict_value" IS '字典�?;
COMMENT ON COLUMN "public"."sys_dict_item"."sort_order" IS '排序';
COMMENT ON COLUMN "public"."sys_dict_item"."status" IS '字典项状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_dict_item"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_dict_item"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict_item"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_dict_item"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_item"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_dict_item"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_dict_item" IS '字典数据�?字典具体键值对';

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_file";
CREATE TABLE "public"."sys_file" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "file_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "file_path" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "file_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "file_size" int8 DEFAULT 0,
    "file_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "upload_by" int8 NOT NULL,
    "upload_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_file"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_file"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_file"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_file"."file_name" IS '文件名称';
COMMENT ON COLUMN "public"."sys_file"."file_path" IS '文件存储路径';
COMMENT ON COLUMN "public"."sys_file"."file_url" IS '文件访问URL';
COMMENT ON COLUMN "public"."sys_file"."file_size" IS '文件大小(字节)';
COMMENT ON COLUMN "public"."sys_file"."file_type" IS '文件类型';
COMMENT ON COLUMN "public"."sys_file"."upload_by" IS '上传人ID';
COMMENT ON COLUMN "public"."sys_file"."upload_time" IS '上传时间';
COMMENT ON COLUMN "public"."sys_file"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_file"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_file"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_file"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_file"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_file"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_file" IS '文件资源�?存储上传的文件信�?;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice" (
    "id" int8 NOT NULL,
    "notice_title" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "notice_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "notice_content" text,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "target_ids" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_notice"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "public"."sys_notice"."notice_type" IS '公告类型(ANNOUNCEMENT/NOTIFICATION)';
COMMENT ON COLUMN "public"."sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "public"."sys_notice"."status" IS '公告状�?PUBLISHED/UNPUBLISHED)';
COMMENT ON COLUMN "public"."sys_notice"."target_type" IS '目标类型(ALL/SPECIFIED_TENANT/SPECIFIED_USER)';
COMMENT ON COLUMN "public"."sys_notice"."target_ids" IS '目标ID集合(逗号分隔)';
COMMENT ON COLUMN "public"."sys_notice"."tenant_id" IS '所属租户ID(0为系统公�?';
COMMENT ON COLUMN "public"."sys_notice"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_notice"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_notice"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_notice"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_notice"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_notice"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_notice"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_notice" IS '通知公告�?系统公告与站内信';

-- ----------------------------
-- Table structure for sys_notice_user_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice_user_rel";
CREATE TABLE "public"."sys_notice_user_rel" (
    "id" int8 NOT NULL,
    "notice_id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "read_status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "read_time" timestamp(6),
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_notice_user_rel"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."notice_id" IS '公告ID';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."read_status" IS '阅读状�?UNREAD/READ)';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."read_time" IS '阅读时间';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_notice_user_rel"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_notice_user_rel" IS '用户公告阅读状态表-记录用户公告已读/未读状�?;


-- =============================================================================
-- 2. 动态配置域 (Dynamic Configuration)
-- =============================================================================

-- ----------------------------
-- Table structure for sys_form_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_form_config";
CREATE TABLE "public"."sys_form_config" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "biz_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "form_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "form_schema" jsonb NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "version" int4 DEFAULT 1,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_form_config"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_form_config"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_form_config"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_form_config"."biz_type" IS '业务类型(如education_order, restaurant_order)';
COMMENT ON COLUMN "public"."sys_form_config"."form_name" IS '表单名称';
COMMENT ON COLUMN "public"."sys_form_config"."form_schema" IS '表单结构配置(JSONB,定义字段类型/校验/选项)';
COMMENT ON COLUMN "public"."sys_form_config"."status" IS '表单状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_form_config"."version" IS '版本�?支持表单版本管理)';
COMMENT ON COLUMN "public"."sys_form_config"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_form_config"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_form_config"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_form_config"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_form_config"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_form_config"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_form_config" IS '动态表单配置表-实现不同行业表单字段动态配�?;

-- ----------------------------
-- Table structure for sys_datasource_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_datasource_config";
CREATE TABLE "public"."sys_datasource_config" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "datasource_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "datasource_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "datasource_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "datasource_config" jsonb NOT NULL,
    "cache_enabled" bool NOT NULL DEFAULT TRUE,
    "cache_expire" int4 DEFAULT 300,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_datasource_config"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_datasource_config"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_datasource_config"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_datasource_config"."datasource_code" IS '数据源编�?如dish_list, part_list)';
COMMENT ON COLUMN "public"."sys_datasource_config"."datasource_name" IS '数据源名�?;
COMMENT ON COLUMN "public"."sys_datasource_config"."datasource_type" IS '数据源类�?BIZ_TABLE/API/DICT/SQL)';
COMMENT ON COLUMN "public"."sys_datasource_config"."datasource_config" IS '数据源配�?JSONB,定义表名/字段/条件)';
COMMENT ON COLUMN "public"."sys_datasource_config"."cache_enabled" IS '是否启用缓存';
COMMENT ON COLUMN "public"."sys_datasource_config"."cache_expire" IS '缓存过期时间(�?';
COMMENT ON COLUMN "public"."sys_datasource_config"."status" IS '数据源状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_datasource_config"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_datasource_config"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_datasource_config"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_datasource_config"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_datasource_config"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_datasource_config"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_datasource_config" IS '动态数据源配置�?实现下拉框数据来源动态配�?;

-- ----------------------------
-- Table structure for sys_print_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_print_template";
CREATE TABLE "public"."sys_print_template" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "template_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "template_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "biz_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "template_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "template_content" text NOT NULL,
    "template_config" jsonb DEFAULT '{}'::jsonb,
    "paper_size" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'A4',
    "orientation" varchar(10) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'portrait',
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "is_default" bool NOT NULL DEFAULT FALSE,
    "version" int4 DEFAULT 1,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_print_template"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_print_template"."tenant_id" IS '所属租户ID';
COMMENT ON COLUMN "public"."sys_print_template"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_print_template"."template_code" IS '模板编码(如education_contract, restaurant_receipt)';
COMMENT ON COLUMN "public"."sys_print_template"."template_name" IS '模板名称';
COMMENT ON COLUMN "public"."sys_print_template"."biz_type" IS '业务类型';
COMMENT ON COLUMN "public"."sys_print_template"."template_type" IS '模板类型(HTML/MARKDOWN/JSON_CONFIG)';
COMMENT ON COLUMN "public"."sys_print_template"."template_content" IS '模板内容(HTML/模板引擎语法)';
COMMENT ON COLUMN "public"."sys_print_template"."template_config" IS '模板配置(JSONB,定义变量/条件显示)';
COMMENT ON COLUMN "public"."sys_print_template"."paper_size" IS '纸张大小(A4/A5/80mm热敏纸等)';
COMMENT ON COLUMN "public"."sys_print_template"."orientation" IS '纸张方向(portrait/landscape)';
COMMENT ON COLUMN "public"."sys_print_template"."status" IS '模板状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_print_template"."is_default" IS '是否默认模板';
COMMENT ON COLUMN "public"."sys_print_template"."version" IS '版本�?;
COMMENT ON COLUMN "public"."sys_print_template"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_print_template"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_print_template"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_print_template"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_print_template"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_print_template"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_print_template" IS '打印模板配置�?实现不同行业打印/PDF模板动态配�?;


-- =============================================================================
-- 3. 审计�?(Audit)
-- =============================================================================

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_oper_log";
CREATE TABLE "public"."sys_oper_log" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "module" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "business_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "method" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "request_method" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
    "operator_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "operator_id" int8 NOT NULL,
    "dept_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "oper_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "oper_ip" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
    "oper_location" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "oper_param" varchar(2000) COLLATE "pg_catalog"."default" NOT NULL,
    "json_result" varchar(2000) COLLATE "pg_catalog"."default" NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "error_msg" varchar(2000) COLLATE "pg_catalog"."default" NOT NULL,
    "oper_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_oper_log"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_oper_log"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_oper_log"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_oper_log"."module" IS '操作模块';
COMMENT ON COLUMN "public"."sys_oper_log"."business_type" IS '业务类型(INSERT/UPDATE/DELETE/EXPORT/IMPORT/OTHER)';
COMMENT ON COLUMN "public"."sys_oper_log"."method" IS '请求方法';
COMMENT ON COLUMN "public"."sys_oper_log"."request_method" IS '请求方式(GET/POST)';
COMMENT ON COLUMN "public"."sys_oper_log"."operator_name" IS '操作人员姓名';
COMMENT ON COLUMN "public"."sys_oper_log"."operator_id" IS '操作人员ID';
COMMENT ON COLUMN "public"."sys_oper_log"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_ip" IS '操作IP';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_location" IS '操作地点';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "public"."sys_oper_log"."json_result" IS '返回结果';
COMMENT ON COLUMN "public"."sys_oper_log"."status" IS '操作状�?SUCCESS/FAILED)';
COMMENT ON COLUMN "public"."sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "public"."sys_oper_log"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_oper_log"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_oper_log" IS '操作日志�?审计用户操作行为';

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_login_log";
CREATE TABLE "public"."sys_login_log" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "ip_address" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
    "login_location" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "browser" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "os" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "msg" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "login_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_login_log"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_login_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_login_log"."username" IS '用户�?;
COMMENT ON COLUMN "public"."sys_login_log"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_login_log"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_login_log"."ip_address" IS '登录IP';
COMMENT ON COLUMN "public"."sys_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."sys_login_log"."browser" IS '浏览�?;
COMMENT ON COLUMN "public"."sys_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."sys_login_log"."status" IS '登录状�?SUCCESS/FAILED)';
COMMENT ON COLUMN "public"."sys_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."sys_login_log"."login_time" IS '登录时间';
COMMENT ON COLUMN "public"."sys_login_log"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_login_log"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_login_log" IS '登录日志�?记录用户登录信息';

-- ----------------------------
-- Table structure for sys_data_audit_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_data_audit_log";
CREATE TABLE "public"."sys_data_audit_log" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "table_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "record_id" int8 NOT NULL,
    "operator_id" int8 NOT NULL,
    "operate_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "old_value" jsonb DEFAULT '{}'::jsonb,
    "new_value" jsonb DEFAULT '{}'::jsonb,
    "operate_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_data_audit_log"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_data_audit_log"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."table_name" IS '表名';
COMMENT ON COLUMN "public"."sys_data_audit_log"."record_id" IS '记录ID';
COMMENT ON COLUMN "public"."sys_data_audit_log"."operator_id" IS '操作人ID';
COMMENT ON COLUMN "public"."sys_data_audit_log"."operate_type" IS '操作类型(UPDATE/DELETE)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."old_value" IS '修改前数�?JSONB)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."new_value" IS '修改后数�?JSONB)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."operate_time" IS '操作时间';
COMMENT ON COLUMN "public"."sys_data_audit_log"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_data_audit_log"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_data_audit_log" IS '数据变更审计�?记录数据字段变更详情';


-- =============================================================================
-- 4. 消息通知�?(Notification Center)
-- =============================================================================

-- ----------------------------
-- Table structure for sys_message_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_message_template";
CREATE TABLE "public"."sys_message_template" (
    "id" int8 NOT NULL,
    "template_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "template_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "biz_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "message_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "template_title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "template_content" text NOT NULL,
    "template_example" jsonb DEFAULT '{}'::jsonb,
    "variables" jsonb DEFAULT '[]'::jsonb,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "version" int4 DEFAULT 1,
    "language" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'zh-CN',
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_message_template"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_message_template"."template_code" IS '模板编码(如ORDER_PAID, PACKAGE_EXPIRE_WARNING)';
COMMENT ON COLUMN "public"."sys_message_template"."template_name" IS '模板名称';
COMMENT ON COLUMN "public"."sys_message_template"."tenant_id" IS '所属租户ID(0为系统模�?';
COMMENT ON COLUMN "public"."sys_message_template"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_message_template"."biz_type" IS '业务类型(如order, subscription, system)';
COMMENT ON COLUMN "public"."sys_message_template"."message_type" IS '消息类型(NOTIFICATION/MARKETING/VERIFICATION/REMINDER)';
COMMENT ON COLUMN "public"."sys_message_template"."template_title" IS '模板标题';
COMMENT ON COLUMN "public"."sys_message_template"."template_content" IS '模板内容(支持变量占位�?�?{userName})';
COMMENT ON COLUMN "public"."sys_message_template"."template_example" IS '示例数据(JSONB,用于测试预览)';
COMMENT ON COLUMN "public"."sys_message_template"."variables" IS '变量定义(JSONB,定义变量�?类型/必填)';
COMMENT ON COLUMN "public"."sys_message_template"."status" IS '模板状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_message_template"."version" IS '版本�?;
COMMENT ON COLUMN "public"."sys_message_template"."language" IS '语言(zh-CN/en-US�?';
COMMENT ON COLUMN "public"."sys_message_template"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_message_template"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_message_template"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_message_template"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_message_template"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_message_template"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_message_template" IS '消息模板�?定义各类消息的内容模�?;

-- ----------------------------
-- Table structure for sys_inbox_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_inbox_message";
CREATE TABLE "public"."sys_inbox_message" (
    "id" int8 NOT NULL,
    "user_id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "message_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "content" text NOT NULL,
    "priority" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "is_read" bool NOT NULL DEFAULT FALSE,
    "read_time" timestamp(6),
    "is_archived" bool NOT NULL DEFAULT FALSE,
    "archived_time" timestamp(6),
    "expire_time" timestamp(6),
    "action_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "action_text" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_inbox_message"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_inbox_message"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_inbox_message"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_inbox_message"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_inbox_message"."message_type" IS '消息类型(SYSTEM/APPROVAL/BILLING/MARKETING)';
COMMENT ON COLUMN "public"."sys_inbox_message"."title" IS '消息标题';
COMMENT ON COLUMN "public"."sys_inbox_message"."content" IS '消息内容';
COMMENT ON COLUMN "public"."sys_inbox_message"."priority" IS '优先�?NORMAL/IMPORTANT/URGENT)';
COMMENT ON COLUMN "public"."sys_inbox_message"."is_read" IS '是否已读';
COMMENT ON COLUMN "public"."sys_inbox_message"."read_time" IS '阅读时间';
COMMENT ON COLUMN "public"."sys_inbox_message"."is_archived" IS '是否已归�?;
COMMENT ON COLUMN "public"."sys_inbox_message"."archived_time" IS '归档时间';
COMMENT ON COLUMN "public"."sys_inbox_message"."expire_time" IS '过期时间(NULL为永不过�?';
COMMENT ON COLUMN "public"."sys_inbox_message"."action_url" IS '操作按钮URL';
COMMENT ON COLUMN "public"."sys_inbox_message"."action_text" IS '操作按钮文案';
COMMENT ON COLUMN "public"."sys_inbox_message"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_inbox_message"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_inbox_message"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_inbox_message"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_inbox_message"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_inbox_message"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_inbox_message" IS '站内信收件箱�?用户个人消息inbox';

-- ----------------------------
-- Table structure for sys_message_schedule
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_message_schedule";
CREATE TABLE "public"."sys_message_schedule" (
    "id" int8 NOT NULL,
    "task_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "template_id" int8 NOT NULL,
    "target_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "target_ids" varchar(1000) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_condition" jsonb DEFAULT '{}'::jsonb,
    "execute_time" timestamp(6) NOT NULL,
    "repeat_rule" jsonb DEFAULT '{}'::jsonb,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "executed_count" int4 DEFAULT 0,
    "last_execute_time" timestamp(6),
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_message_schedule"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_message_schedule"."task_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_message_schedule"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_message_schedule"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_message_schedule"."template_id" IS '消息模板ID';
COMMENT ON COLUMN "public"."sys_message_schedule"."target_type" IS '目标类型(SPECIFIED_USER/SPECIFIED_TENANT/CONDITION_MATCH)';
COMMENT ON COLUMN "public"."sys_message_schedule"."target_ids" IS '目标ID集合(逗号分隔)';
COMMENT ON COLUMN "public"."sys_message_schedule"."trigger_type" IS '触发类型(ONCE/PERIODIC/EVENT)';
COMMENT ON COLUMN "public"."sys_message_schedule"."trigger_condition" IS '触发条件(JSONB,定义时间/事件规则)';
COMMENT ON COLUMN "public"."sys_message_schedule"."execute_time" IS '计划执行时间';
COMMENT ON COLUMN "public"."sys_message_schedule"."repeat_rule" IS '重复规则(JSONB,如{"type":"daily","interval":1})';
COMMENT ON COLUMN "public"."sys_message_schedule"."status" IS '任务状�?PENDING/EXECUTING/COMPLETED/CANCELLED)';
COMMENT ON COLUMN "public"."sys_message_schedule"."executed_count" IS '已执行次�?;
COMMENT ON COLUMN "public"."sys_message_schedule"."last_execute_time" IS '最后执行时�?;
COMMENT ON COLUMN "public"."sys_message_schedule"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_message_schedule"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_message_schedule"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_message_schedule"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_message_schedule"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_message_schedule"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_message_schedule" IS '定时消息任务�?预约发送或周期性发送的消息';


-- =============================================================================
-- 5. 行业适配�?(Industry Adaptation)
-- =============================================================================

-- ----------------------------
-- Table structure for sys_industry_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_industry_template";
CREATE TABLE "public"."sys_industry_template" (
    "id" int8 NOT NULL,
    "template_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "template_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "industry_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "template_desc" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "template_config" jsonb DEFAULT '{}'::jsonb,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_industry_template"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_industry_template"."template_code" IS '行业模板编码';
COMMENT ON COLUMN "public"."sys_industry_template"."template_name" IS '行业模板名称';
COMMENT ON COLUMN "public"."sys_industry_template"."industry_type" IS '行业类型(INTERNET/MANUFACTURING/EDUCATION/GOVERNMENT/FINANCE/HEALTHCARE/RESTAURANT)';
COMMENT ON COLUMN "public"."sys_industry_template"."template_desc" IS '模板描述';
COMMENT ON COLUMN "public"."sys_industry_template"."template_config" IS '模板配置(JSONB,包含默认权限/表单/数据源等)';
COMMENT ON COLUMN "public"."sys_industry_template"."status" IS '模板状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_industry_template"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_industry_template"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_industry_template"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_industry_template"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_industry_template"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_industry_template"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_industry_template" IS '行业模板�?定义不同行业的初始化配置模板';

-- ----------------------------
-- Table structure for sys_form_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_form_template";
CREATE TABLE "public"."sys_form_template" (
    "id" int8 NOT NULL,
    "template_id" int8 NOT NULL,
    "form_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "form_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "biz_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "form_schema" jsonb NOT NULL,
    "form_version" int4 DEFAULT 1,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_form_template"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_form_template"."template_id" IS '关联行业模板ID';
COMMENT ON COLUMN "public"."sys_form_template"."form_code" IS '表单编码';
COMMENT ON COLUMN "public"."sys_form_template"."form_name" IS '表单名称';
COMMENT ON COLUMN "public"."sys_form_template"."biz_type" IS '业务类型';
COMMENT ON COLUMN "public"."sys_form_template"."form_schema" IS '表单结构配置(JSONB)';
COMMENT ON COLUMN "public"."sys_form_template"."form_version" IS '版本�?;
COMMENT ON COLUMN "public"."sys_form_template"."status" IS '表单状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_form_template"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_form_template"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_form_template"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_form_template"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_form_template"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_form_template"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_form_template" IS '表单模板�?定义行业模板下的动态表单配�?;

-- ----------------------------
-- Table structure for sys_tenant_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_tenant_config";
CREATE TABLE "public"."sys_tenant_config" (
    "id" int8 NOT NULL,
    "tenant_id" int8 NOT NULL,
    "tenant_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "config_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "config_value" text NOT NULL,
    "config_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "config_group" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "config_desc" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "inheritable" bool NOT NULL DEFAULT TRUE,
    "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by" int8 NOT NULL,
    "create_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_by" int8 NOT NULL,
    "update_at" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "is_deleted" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
    "deleted_at" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_tenant_config"."id" IS '主键ID(雪花算法)';
COMMENT ON COLUMN "public"."sys_tenant_config"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."sys_tenant_config"."tenant_code" IS '租户编码(冗余)';
COMMENT ON COLUMN "public"."sys_tenant_config"."config_key" IS '配置�?;
COMMENT ON COLUMN "public"."sys_tenant_config"."config_value" IS '配置�?;
COMMENT ON COLUMN "public"."sys_tenant_config"."config_type" IS '配置类型(STRING/NUMBER/BOOLEAN/JSON)';
COMMENT ON COLUMN "public"."sys_tenant_config"."config_group" IS '配置分组';
COMMENT ON COLUMN "public"."sys_tenant_config"."config_desc" IS '配置描述';
COMMENT ON COLUMN "public"."sys_tenant_config"."inheritable" IS '是否可继承给子租�?;
COMMENT ON COLUMN "public"."sys_tenant_config"."status" IS '配置状�?ENABLED/DISABLED)';
COMMENT ON COLUMN "public"."sys_tenant_config"."create_by" IS '创建人ID';
COMMENT ON COLUMN "public"."sys_tenant_config"."create_at" IS '创建时间';
COMMENT ON COLUMN "public"."sys_tenant_config"."update_by" IS '更新人ID';
COMMENT ON COLUMN "public"."sys_tenant_config"."update_at" IS '更新时间';
COMMENT ON COLUMN "public"."sys_tenant_config"."is_deleted" IS '删除状�?NOT_DELETED/DELETED)';
COMMENT ON COLUMN "public"."sys_tenant_config"."deleted_at" IS '删除时间';
COMMENT ON TABLE "public"."sys_tenant_config" IS '租户配置�?存储租户级别的键值对配置';


-- =============================================================================
-- Primary Key Constraints
-- =============================================================================

-- ----------------------------
-- Primary Key structure for sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_dict
-- ----------------------------
ALTER TABLE "public"."sys_dict" ADD CONSTRAINT "sys_dict_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_dict_item
-- ----------------------------
ALTER TABLE "public"."sys_dict_item" ADD CONSTRAINT "sys_dict_item_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_file
-- ----------------------------
ALTER TABLE "public"."sys_file" ADD CONSTRAINT "sys_file_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice" ADD CONSTRAINT "sys_notice_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_notice_user_rel
-- ----------------------------
ALTER TABLE "public"."sys_notice_user_rel" ADD CONSTRAINT "sys_notice_user_rel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_form_config
-- ----------------------------
ALTER TABLE "public"."sys_form_config" ADD CONSTRAINT "sys_form_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_datasource_config
-- ----------------------------
ALTER TABLE "public"."sys_datasource_config" ADD CONSTRAINT "sys_datasource_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_print_template
-- ----------------------------
ALTER TABLE "public"."sys_print_template" ADD CONSTRAINT "sys_print_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_oper_log
-- ----------------------------
ALTER TABLE "public"."sys_oper_log" ADD CONSTRAINT "sys_oper_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_login_log
-- ----------------------------
ALTER TABLE "public"."sys_login_log" ADD CONSTRAINT "sys_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_data_audit_log
-- ----------------------------
ALTER TABLE "public"."sys_data_audit_log" ADD CONSTRAINT "sys_data_audit_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_message_template
-- ----------------------------
ALTER TABLE "public"."sys_message_template" ADD CONSTRAINT "sys_message_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_inbox_message
-- ----------------------------
ALTER TABLE "public"."sys_inbox_message" ADD CONSTRAINT "sys_inbox_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_message_schedule
-- ----------------------------
ALTER TABLE "public"."sys_message_schedule" ADD CONSTRAINT "sys_message_schedule_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_industry_template
-- ----------------------------
ALTER TABLE "public"."sys_industry_template" ADD CONSTRAINT "sys_industry_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_form_template
-- ----------------------------
ALTER TABLE "public"."sys_form_template" ADD CONSTRAINT "sys_form_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for sys_tenant_config
-- ----------------------------
ALTER TABLE "public"."sys_tenant_config" ADD CONSTRAINT "sys_tenant_config_pkey" PRIMARY KEY ("id");
