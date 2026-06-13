create table sys_perm
(
    id              bigint        not null
        primary key,
    perm_code       varchar(100)  not null,
    perm_name       varchar(100)  not null,
    perm_desc       varchar(200) default NULL::character varying,
    perm_type       varchar(20)   not null,
    parent_id       bigint        not null,
    path            varchar(1000) not null,
    level           integer       not null,
    resource_type   varchar(50)  default NULL::character varying,
    resource_path   varchar(200) default NULL::character varying,
    resource_method varchar(20)  default NULL::character varying,
    icon            varchar(100) default NULL::character varying,
    sort_order      integer       not null,
    is_visible      boolean       not null,
    status          varchar(20)   not null,
    disable_reason  varchar(200) default NULL::character varying,
    create_tenant   bigint        not null,
    create_dept     bigint        not null,
    create_role     bigint        not null,
    create_by       bigint        not null,
    create_at       timestamp(6)  not null,
    update_by       bigint        not null,
    update_at       timestamp(6)  not null,
    is_deleted      varchar(20)   not null,
    deleted_at      timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_perm is '权限表';

comment on column sys_perm.id is '主键ID';

comment on column sys_perm.perm_code is '权限编码';

comment on column sys_perm.perm_name is '权限名称';

comment on column sys_perm.perm_desc is '权限描述';

comment on column sys_perm.perm_type is '权限类型';

comment on column sys_perm.parent_id is '父权限ID，';

comment on column sys_perm.path is '权限层级路径';

comment on column sys_perm.level is '层级深度';

comment on column sys_perm.resource_type is '资源类型';

comment on column sys_perm.resource_path is '资源路径';

comment on column sys_perm.resource_method is '资源方法';

comment on column sys_perm.icon is '图标';

comment on column sys_perm.sort_order is '排序序号';

comment on column sys_perm.is_visible is '是否可见';

comment on column sys_perm.status is '状态';

comment on column sys_perm.disable_reason is '禁用原因';

comment on column sys_perm.create_tenant is '创建租户ID';

comment on column sys_perm.create_dept is '创建部门ID';

comment on column sys_perm.create_role is '创建角色ID';

comment on column sys_perm.create_by is '创建人用户ID';

comment on column sys_perm.create_at is '创建时间';

comment on column sys_perm.update_by is '更新人用户ID';

comment on column sys_perm.update_at is '更新时间';

comment on column sys_perm.is_deleted is '逻辑删除状态';

comment on column sys_perm.deleted_at is '删除时间';

alter table sys_perm
    owner to postgres;

INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (501, 'PERM_SYSTEM', '系统管理', '系统管理菜单', 'MENU', 0, '/501', 1, 'URL', '/system', null, 'system', 1, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (502, 'PERM_BUSINESS', '业务管理', '业务管理菜单', 'MENU', 0, '/502', 1, 'URL', '/business', null, 'business', 2, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (503, 'PERM_REPORT', '报表中心', '报表查看菜单', 'MENU', 0, '/503', 1, 'URL', '/report', null, 'chart', 3, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (504, 'PERM_MONITOR', '系统监控', '系统监控菜单', 'MENU', 0, '/504', 1, 'URL', '/monitor', null, 'monitor', 4, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (511, 'PERM_USER_MANAGE', '用户管理', '用户信息管理', 'MENU', 501, '/501/511', 2, 'URL', '/system/user', null, 'user', 1, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (512, 'PERM_ROLE_MANAGE', '角色管理', '角色权限管理', 'MENU', 501, '/501/512', 2, 'URL', '/system/role', null, 'role', 2, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (513, 'PERM_DEPT_MANAGE', '部门管理', '部门组织管理', 'MENU', 501, '/501/513', 2, 'URL', '/system/dept', null, 'dept', 3, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (514, 'PERM_TENANT_MANAGE', '租户管理', '租户信息管理', 'MENU', 501, '/501/514', 2, 'URL', '/system/tenant', null, 'tenant', 4, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (515, 'PERM_MENU_MANAGE', '菜单管理', '菜单权限管理', 'MENU', 501, '/501/515', 2, 'URL', '/system/menu', null, 'menu', 5, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (521, 'PERM_USER_VIEW', '用户查看', '查看用户信息', 'BUTTON', 511, '/501/511/521', 3, 'METHOD', '/api/user/list', 'GET', null, 1, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (522, 'PERM_USER_ADD', '用户新增', '新增用户', 'BUTTON', 511, '/501/511/522', 3, 'METHOD', '/api/user/create', 'POST', null, 2, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (523, 'PERM_USER_EDIT', '用户编辑', '编辑用户信息', 'BUTTON', 511, '/501/511/523', 3, 'METHOD', '/api/user/update', 'PUT', null, 3, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (524, 'PERM_USER_DELETE', '用户删除', '删除用户', 'BUTTON', 511, '/501/511/524', 3, 'METHOD', '/api/user/delete', 'DELETE', null, 4, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (525, 'PERM_USER_EXPORT', '用户导出', '导出用户数据', 'BUTTON', 511, '/501/511/525', 3, 'METHOD', '/api/user/export', 'POST', null, 5, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (526, 'PERM_USER_IMPORT', '用户导入', '导入用户数据', 'BUTTON', 511, '/501/511/526', 3, 'METHOD', '/api/user/import', 'POST', null, 6, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (527, 'PERM_USER_RESET_PWD', '重置密码', '重置用户密码', 'BUTTON', 511, '/501/511/527', 3, 'METHOD', '/api/user/reset-password', 'POST', null, 7, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (531, 'PERM_ROLE_VIEW', '角色查看', '查看角色信息', 'BUTTON', 512, '/501/512/531', 3, 'METHOD', '/api/role/list', 'GET', null, 1, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (532, 'PERM_ROLE_ADD', '角色新增', '新增角色', 'BUTTON', 512, '/501/512/532', 3, 'METHOD', '/api/role/create', 'POST', null, 2, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (533, 'PERM_ROLE_EDIT', '角色编辑', '编辑角色信息', 'BUTTON', 512, '/501/512/533', 3, 'METHOD', '/api/role/update', 'PUT', null, 3, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (534, 'PERM_ROLE_DELETE', '角色删除', '删除角色', 'BUTTON', 512, '/501/512/534', 3, 'METHOD', '/api/role/delete', 'DELETE', null, 4, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (535, 'PERM_ROLE_AUTH', '角色授权', '配置角色权限', 'BUTTON', 512, '/501/512/535', 3, 'METHOD', '/api/role/grant', 'POST', null, 5, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (541, 'PERM_ORDER_MANAGE', '订单管理', '订单信息管理', 'MENU', 502, '/502/541', 2, 'URL', '/business/order', null, 'order', 1, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (542, 'PERM_PRODUCT_MANAGE', '产品管理', '产品信息管理', 'MENU', 502, '/502/542', 2, 'URL', '/business/product', null, 'product', 2, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (543, 'PERM_CUSTOMER_MANAGE', '客户管理', '客户信息管理', 'MENU', 502, '/502/543', 2, 'URL', '/business/customer', null, 'customer', 3, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (544, 'PERM_SUPPLIER_MANAGE', '供应商管理', '供应商信息管理', 'MENU', 502, '/502/544', 2, 'URL', '/business/supplier', null, 'supplier', 4, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (551, 'PERM_SALES_REPORT', '销售报表', '销售数据报表', 'MENU', 503, '/503/551', 2, 'URL', '/report/sales', null, 'chart-line', 1, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (552, 'PERM_FINANCE_REPORT', '财务报表', '财务数据报表', 'MENU', 503, '/503/552', 2, 'URL', '/report/finance', null, 'chart-bar', 2, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (553, 'PERM_PROD_REPORT', '生产报表', '生产数据报表', 'MENU', 503, '/503/553', 2, 'URL', '/report/production', null, 'chart-pie', 3, true, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (561, 'PERM_DATA_USER', '用户数据权限', '用户表数据访问', 'DATA', 0, '/561', 1, 'TABLE', 'sys_user', null, null, 1, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (562, 'PERM_DATA_ORDER', '订单数据权限', '订单表数据访问', 'DATA', 0, '/562', 1, 'TABLE', 'biz_order', null, null, 2, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_code, perm_name, perm_desc, perm_type, parent_id, path, level, resource_type, resource_path, resource_method, icon, sort_order, is_visible, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (563, 'PERM_DATA_SALARY', '薪资数据权限', '薪资表数据访问', 'DATA', 0, '/563', 1, 'TABLE', 'hr_salary', null, null, 3, false, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);

create table sys_tenant
(
    id              bigint        not null
        primary key,
    tenant_code     varchar(50)   not null,
    tenant_name     varchar(100)  not null,
    tenant_type     varchar(50)   not null,
    tenant_desc     varchar(500) default NULL::character varying,
    tenant_logo_url varchar(500) default NULL::character varying,
    parent_id       bigint        not null,
    path            varchar(1000) not null,
    level           integer       not null,
    has_children    boolean       not null,
    contact_name    varchar(50)   not null,
    contact_phone   varchar(20)   not null,
    contact_email   varchar(100) default NULL::character varying,
    expire_time     timestamp(6)  not null,
    package_id      bigint        not null,
    ext_attributes  jsonb        default '{}'::jsonb,
    status          varchar(20)   not null,
    disable_reason  varchar(200) default NULL::character varying,
    create_tenant   bigint        not null,
    create_dept     bigint        not null,
    create_role     bigint        not null,
    create_by       bigint        not null,
    create_at       timestamp(6)  not null,
    update_by       bigint        not null,
    update_at       timestamp(6)  not null,
    is_deleted      varchar(20)   not null,
    deleted_at      timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_tenant is '租户表';

comment on column sys_tenant.id is '主键ID';

comment on column sys_tenant.tenant_code is '租户编码';

comment on column sys_tenant.tenant_name is '租户名称';

comment on column sys_tenant.tenant_type is '租户类型';

comment on column sys_tenant.tenant_desc is '租户描述';

comment on column sys_tenant.tenant_logo_url is '租户LOGO路径';

comment on column sys_tenant.parent_id is '父租户ID';

comment on column sys_tenant.path is '层级路径';

comment on column sys_tenant.level is '层级深度';

comment on column sys_tenant.has_children is '是否存在子租户';

comment on column sys_tenant.contact_name is '联系人姓名';

comment on column sys_tenant.contact_phone is '联系人电话';

comment on column sys_tenant.contact_email is '联系人邮箱';

comment on column sys_tenant.expire_time is '服务过期时间';

comment on column sys_tenant.package_id is '套餐ID';

comment on column sys_tenant.ext_attributes is '扩展属性';

comment on column sys_tenant.status is '状态';

comment on column sys_tenant.disable_reason is '禁用原因';

comment on column sys_tenant.create_tenant is '创建租户ID';

comment on column sys_tenant.create_dept is '创建部门ID';

comment on column sys_tenant.create_role is '创建角色ID';

comment on column sys_tenant.create_by is '创建人用户ID';

comment on column sys_tenant.create_at is '创建时间';

comment on column sys_tenant.update_by is '更新人用户ID';

comment on column sys_tenant.update_at is '更新时间';

comment on column sys_tenant.is_deleted is '逻辑删除状态';

comment on column sys_tenant.deleted_at is '删除时间';

alter table sys_tenant
    owner to postgres;

INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (101, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团', 'https://logo.example.com/wanxiang.png', 0, '/101', 1, true, '张总', '13800000001', 'zhangzong@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "全国", "industry": "综合"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (102, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dingxin.png', 0, '/102', 1, true, '丁总', '13800000002', 'dingzong@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"region": "全国", "industry": "制造业"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (103, 'HAINA', '海纳集团', 'ENTERPRISE', '多元化投资集团', 'https://logo.example.com/haina.png', 0, '/103', 1, false, '海总', '13800000003', 'haizong@haina.com', '2031-12-31 23:59:59.000000', 1, '{"region": "全国", "industry": "投资"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (111, 'WX_EAST', '万象-华东分公司', 'BRANCH', '负责华东区域业务', 'https://logo.example.com/wx_east.png', 101, '/101/111', 2, true, '李经理', '13800000011', 'lijingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华东"}', 'ENABLED', null, 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (112, 'WX_SOUTH', '万象-华南分公司', 'BRANCH', '负责华南区域业务', 'https://logo.example.com/wx_south.png', 101, '/101/112', 2, false, '陈经理', '13800000012', 'chenjingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华南"}', 'ENABLED', null, 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (113, 'WX_NORTH', '万象-华北分公司', 'BRANCH', '负责华北区域业务', 'https://logo.example.com/wx_north.png', 101, '/101/113', 2, false, '武经理', '13800000013', 'wujingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华北"}', 'DISABLED', '业务调整暂停', 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-06-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (121, 'WX_EAST_SH', '万象-华东-上海办事处', 'OFFICE', '上海地区业务中心', 'https://logo.example.com/wx_sh.png', 111, '/101/111/121', 3, false, '王主管', '13800000021', 'wangzhuguan@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"city": "上海"}', 'ENABLED', null, 111, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (122, 'WX_EAST_NJ', '万象-华东-南京办事处', 'OFFICE', '南京地区业务中心', 'https://logo.example.com/wx_nj.png', 111, '/101/111/122', 3, false, '赵主管', '13800000022', 'zhaozhuguan@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"city": "南京"}', 'ENABLED', null, 111, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (201, 'DX_CENTRAL', '鼎新-华中分公司', 'BRANCH', '负责华中区域制造业务', 'https://logo.example.com/dx_central.png', 102, '/102/201', 2, false, '马经理', '13800000201', 'majingli@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"region": "华中"}', 'ENABLED', null, 102, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (211, 'DX_CENTRAL_WH', '鼎新-华中-武汉工厂', 'OFFICE', '武汉生产制造基地', 'https://logo.example.com/dx_wh.png', 201, '/102/201/211', 3, false, '董主管', '13800000211', 'dongzhuguan@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"city": "武汉"}', 'ENABLED', null, 201, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);

create table sys_dept
(
    id             bigint        not null
        primary key,
    dept_code      varchar(50)   not null,
    dept_name      varchar(100)  not null,
    dept_desc      varchar(200) default NULL::character varying,
    parent_id      bigint        not null,
    path           varchar(1000) not null,
    level          integer       not null,
    has_children   boolean       not null,
    leader_id      bigint        not null,
    status         varchar(20)   not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint        not null,
    create_dept    bigint        not null,
    create_role    bigint        not null,
    create_by      bigint        not null,
    create_at      timestamp(6)  not null,
    update_by      bigint        not null,
    update_at      timestamp(6)  not null,
    is_deleted     varchar(20)   not null,
    deleted_at     timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_dept is '部门表';

comment on column sys_dept.id is '主键ID';

comment on column sys_dept.dept_code is '部门编码';

comment on column sys_dept.dept_name is '部门名称';

comment on column sys_dept.dept_desc is '部门描述';

comment on column sys_dept.parent_id is '父部门ID';

comment on column sys_dept.path is '层级路径';

comment on column sys_dept.level is '层级深度';

comment on column sys_dept.has_children is '是否存在子部门';

comment on column sys_dept.leader_id is '部门负责人用户ID';

comment on column sys_dept.status is '状态';

comment on column sys_dept.disable_reason is '禁用原因';

comment on column sys_dept.create_tenant is '创建租户ID';

comment on column sys_dept.create_dept is '创建部门ID';

comment on column sys_dept.create_role is '创建角色ID';

comment on column sys_dept.create_by is '创建人用户ID';

comment on column sys_dept.create_at is '创建时间';

comment on column sys_dept.update_by is '更新人用户ID';

comment on column sys_dept.update_at is '更新时间';

comment on column sys_dept.is_deleted is '逻辑删除状态';

comment on column sys_dept.deleted_at is '删除时间';

alter table sys_dept
    owner to postgres;

INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (301, 'DEPT_FIN', '财务部', '财务管理部门', 0, '/301', 1, true, 1, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (302, 'DEPT_RD', '研发部', '产品研发部门', 0, '/302', 1, true, 2, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (303, 'DEPT_SALES', '销售部', '市场销售部门', 0, '/303', 1, true, 3, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (304, 'DEPT_HR', '人力资源部', '人力资源管理', 0, '/304', 1, false, 4, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (305, 'DEPT_IT', '信息技术部', 'IT支持与系统维护', 0, '/305', 1, false, 5, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (306, 'DEPT_MKT', '市场部', '市场营销推广', 0, '/306', 1, false, 6, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (307, 'DEPT_PROD', '生产部', '生产制造管理', 0, '/307', 1, true, 7, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (308, 'DEPT_QC', '质量部', '质量控制与检验', 0, '/308', 1, false, 8, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (309, 'DEPT_LOG', '物流部', '物流与供应链管理', 0, '/309', 1, false, 9, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (310, 'DEPT_CS', '客服部', '客户服务支持', 0, '/310', 1, false, 10, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (311, 'DEPT_FIN_ACC', '会计组', '日常会计核算', 301, '/301/311', 2, false, 1, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (312, 'DEPT_FIN_TAX', '税务组', '税务筹划与申报', 301, '/301/312', 2, false, 2, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (313, 'DEPT_FIN_AUDIT', '审计组', '内部审计监督', 301, '/301/313', 2, false, 3, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (321, 'DEPT_RD_FE', '前端开发组', '前端技术研发', 302, '/302/321', 2, false, 1, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (322, 'DEPT_RD_BE', '后端开发组', '后端技术研发', 302, '/302/322', 2, false, 2, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (323, 'DEPT_RD_TEST', '测试组', '软件测试与质量保障', 302, '/302/323', 2, false, 3, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (324, 'DEPT_RD_UI', 'UI设计组', '用户界面设计', 302, '/302/324', 2, false, 4, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (331, 'DEPT_SALES_NORTH', '华北销售组', '华北区域销售', 303, '/303/331', 2, false, 1, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (332, 'DEPT_SALES_SOUTH', '华南销售组', '华南区域销售', 303, '/303/332', 2, false, 2, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (333, 'DEPT_SALES_EAST', '华东销售组', '华东区域销售', 303, '/303/333', 2, false, 3, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (371, 'DEPT_PROD_LINE1', '生产一线', '第一生产线', 307, '/307/371', 2, false, 1, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (372, 'DEPT_PROD_LINE2', '生产二线', '第二生产线', 307, '/307/372', 2, false, 2, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (373, 'DEPT_PROD_WH', '生产仓储组', '生产物料仓储', 307, '/307/373', 2, false, 3, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (391, 'DEPT_PROJ_A', '项目A组', '临时项目A团队', 0, '/391', 1, false, 20, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (392, 'DEPT_PROJ_B', '项目B组', '临时项目B团队', 0, '/392', 1, false, 21, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (381, 'DEPT_OLD', '已撤销部门', '历史遗留部门', 0, '/381', 1, false, 99, 'DISABLED', '部门合并已撤销', 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-05-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (341, 'DEPT_LEGAL', '法务部', '法律事务管理', 0, '/341', 1, false, 11, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (342, 'DEPT_ADMIN', '行政部', '行政后勤管理', 0, '/342', 1, false, 12, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (343, 'DEPT_BD', '商务拓展部', '商务合作与拓展', 0, '/343', 1, false, 13, 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);

create table sys_role
(
    id             bigint       not null
        primary key,
    role_code      varchar(100) not null,
    role_name      varchar(100) not null,
    role_desc      varchar(200) default NULL::character varying,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) not null,
    update_by      bigint       not null,
    update_at      timestamp(6) not null,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键ID';

comment on column sys_role.role_code is '角色编码';

comment on column sys_role.role_name is '角色名称';

comment on column sys_role.role_desc is '角色描述';

comment on column sys_role.status is '状态';

comment on column sys_role.disable_reason is '禁用原因';

comment on column sys_role.create_tenant is '创建租户ID';

comment on column sys_role.create_dept is '创建部门ID';

comment on column sys_role.create_role is '创建角色ID';

comment on column sys_role.create_by is '创建人用户ID';

comment on column sys_role.create_at is '创建时间';

comment on column sys_role.update_by is '更新人用户ID';

comment on column sys_role.update_at is '更新时间';

comment on column sys_role.is_deleted is '逻辑删除状态';

comment on column sys_role.deleted_at is '删除时间';

alter table sys_role
    owner to postgres;

INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (401, 'SUPER_ADMIN', '超级管理员', '系统最高权限', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (402, 'SYS_ADMIN', '系统管理员', '系统配置管理', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (403, 'SYS_AUDITOR', '系统审计员', '系统审计监督', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (411, 'TENANT_ADMIN', '租户管理员', '租户级管理员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (412, 'TENANT_OPERATOR', '租户运营', '租户运营人员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (413, 'TENANT_VIEWER', '租户查看员', '租户只读权限', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (421, 'DEPT_MANAGER', '部门经理', '部门管理者', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (422, 'DEPT_LEADER', '部门主管', '部门负责人', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (423, 'DEPT_SUPERVISOR', '部门组长', '部门小组长', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (431, 'EMPLOYEE', '普通员工', '普通员工角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (432, 'INTERN', '实习生', '实习员工角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (433, 'CONTRACTOR', '外包人员', '外包员工角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (441, 'FINANCE_STAFF', '财务专员', '财务人员角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (442, 'HR_STAFF', 'HR专员', '人力资源专员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (443, 'IT_STAFF', 'IT专员', 'IT技术支持', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (444, 'SALES_STAFF', '销售专员', '销售人员角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (445, 'RD_ENGINEER', '研发工程师', '研发技术人员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (446, 'PROD_WORKER', '生产员工', '生产线工人', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (447, 'QC_INSPECTOR', '质检员', '质量检验人员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (448, 'CS_AGENT', '客服专员', '客户服务人员', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (451, 'PROJECT_MANAGER', '项目经理', '项目管理角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (452, 'DATA_ANALYST', '数据分析师', '数据分析角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (453, 'SECURITY_OFFICER', '安全专员', '信息安全角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (454, 'REPORT_VIEWER', '报表查看员', '只能查看报表', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (455, 'GUEST', '访客', '临时访客角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (461, 'OLD_ROLE', '已废弃角色', '历史遗留角色', 'DISABLED', '角色已废弃', 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-05-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (456, 'TRAINER', '培训师', '员工培训角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (457, 'PURCHASER', '采购专员', '采购管理角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (458, 'WAREHOUSE_KEEPER', '仓库管理员', '仓储管理角色', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);

create table sys_user
(
    id             bigint       not null
        primary key,
    user_code      varchar(50)  not null,
    user_name      varchar(50)  not null,
    nick_name      varchar(50)  not null,
    real_name      varchar(50)  not null,
    email          varchar(100) not null,
    phone          varchar(20)  not null,
    password       varchar(200) not null,
    avatar_url     varchar(500) default NULL::character varying,
    gender         varchar(10)  not null,
    birthday       date,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    last_login_at  timestamp(6) default NULL::timestamp without time zone,
    last_login_ip  varchar(50)  default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) not null,
    update_by      bigint       not null,
    update_at      timestamp(6) not null,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_user is '用户表';

comment on column sys_user.id is '主键ID';

comment on column sys_user.user_code is '用户编码';

comment on column sys_user.user_name is '用户名';

comment on column sys_user.nick_name is '昵称';

comment on column sys_user.real_name is '真实姓名';

comment on column sys_user.email is '电子邮箱';

comment on column sys_user.phone is '手机号码';

comment on column sys_user.password is '密码哈希值';

comment on column sys_user.avatar_url is '头像URL';

comment on column sys_user.gender is '性别';

comment on column sys_user.birthday is '出生日期';

comment on column sys_user.status is '状态';

comment on column sys_user.disable_reason is '禁用原因';

comment on column sys_user.last_login_at is '最后登录时间';

comment on column sys_user.last_login_ip is '最后登录IP';

comment on column sys_user.create_tenant is '创建租户ID';

comment on column sys_user.create_dept is '创建部门ID';

comment on column sys_user.create_role is '创建角色ID';

comment on column sys_user.create_by is '创建人用户ID';

comment on column sys_user.create_at is '创建时间';

comment on column sys_user.update_by is '更新人用户ID';

comment on column sys_user.update_at is '更新时间';

comment on column sys_user.is_deleted is '逻辑删除状态';

comment on column sys_user.deleted_at is '删除时间';

alter table sys_user
    owner to postgres;

INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1, 'U001', 'admin', '超管', '系统管理员', 'admin@example.com', '13800000001', '$2a$10$encrypted_password', '/avatar/admin.png', 'MALE', '1985-01-01', 'ENABLED', null, '2025-06-12 10:30:00.000000', '192.168.1.100', 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-06-12 10:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4, 'U004', 'wangwu', '王五', '王五', 'wangwu@wanxiang.com', '13800000004', '$2a$10$encrypted_password', '/avatar/wangwu.png', 'FEMALE', '1992-09-10', 'ENABLED', null, '2025-06-12 08:30:00.000000', '192.168.1.103', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-12 08:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6, 'U006', 'sunqi', '孙七', '孙七', 'sunqi@wanxiang.com', '13800000006', '$2a$10$encrypted_password', '/avatar/sunqi.png', 'FEMALE', '1993-04-18', 'ENABLED', null, '2025-06-10 14:20:00.000000', '192.168.1.104', 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-06-10 14:20:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7, 'U007', 'zhouba', '周八', '周八', 'zhouba@wanxiang.com', '13800000007', '$2a$10$encrypted_password', '/avatar/zhouba.png', 'MALE', '1991-07-30', 'ENABLED', null, '2025-06-12 07:45:00.000000', '192.168.1.105', 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-06-12 07:45:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (8, 'U008', 'wujiu', '吴九', '吴九', 'wujiu@wanxiang.com', '13800000008', '$2a$10$encrypted_password', '/avatar/wujiu.png', 'MALE', '1994-12-05', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-01-06 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9, 'U009', 'zhengshi', '郑十', '郑十', 'zhengshi@wanxiang.com', '13800000009', '$2a$10$encrypted_password', '/avatar/zhengshi.png', 'FEMALE', '1996-02-14', 'DISABLED', '已离职', null, null, 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-05-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (10, 'U010', 'qianyi', '钱一', '钱一', 'qianyi@wanxiang.com', '13800000010', '$2a$10$encrypted_password', '/avatar/qianyi.png', 'MALE', '1989-08-22', 'ENABLED', null, '2025-06-11 18:00:00.000000', '192.168.1.106', 0, 0, 0, 1, '2025-01-07 08:00:00.000000', 1, '2025-06-11 18:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (11, 'U011', 'chener', '陈二', '陈二', 'chener@dingxin.com', '13800000011', '$2a$10$encrypted_password', '/avatar/chener.png', 'MALE', '1987-05-10', 'ENABLED', null, '2025-06-12 09:15:00.000000', '192.168.2.101', 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-06-12 09:15:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (12, 'U012', 'fengsan', '冯三', '冯三', 'fengsan@dingxin.com', '13800000012', '$2a$10$encrypted_password', '/avatar/fengsan.png', 'MALE', '1990-11-20', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-01-08 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (13, 'U013', 'chusi', '楚四', '楚四', 'chusi@dingxin.com', '13800000013', '$2a$10$encrypted_password', '/avatar/chusi.png', 'FEMALE', '1992-06-30', 'ENABLED', null, '2025-06-11 15:30:00.000000', '192.168.2.102', 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-06-11 15:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (14, 'U014', 'weiwu', '魏五', '魏五', 'weiwu@dingxin.com', '13800000014', '$2a$10$encrypted_password', '/avatar/weiwu.png', 'MALE', '1993-09-18', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-09 08:00:00.000000', 1, '2025-01-09 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (15, 'U015', 'jianglu', '蒋六', '蒋六', 'jianglu@dingxin.com', '13800000015', '$2a$10$encrypted_password', '/avatar/jianglu.png', 'FEMALE', '1991-03-25', 'ENABLED', null, '2025-06-10 11:00:00.000000', '192.168.2.103', 0, 0, 0, 1, '2025-01-09 08:00:00.000000', 1, '2025-06-10 11:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (16, 'U016', 'shenqi', '沈七', '沈七', 'shenqi@haina.com', '13800000016', '$2a$10$encrypted_password', '/avatar/shenqi.png', 'MALE', '1988-12-12', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (17, 'U017', 'hanba', '韩八', '韩八', 'hanba@haina.com', '13800000017', '$2a$10$encrypted_password', '/avatar/hanba.png', 'MALE', '1994-07-08', 'ENABLED', null, '2025-06-11 13:20:00.000000', '192.168.3.101', 0, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-06-11 13:20:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (18, 'U018', 'yangjiu', '杨九', '杨九', 'yangjiu@example.com', '13800000018', '$2a$10$encrypted_password', '/avatar/yangjiu.png', 'MALE', '1990-10-15', 'ENABLED', null, '2025-06-12 08:00:00.000000', '192.168.1.107', 0, 0, 0, 1, '2025-01-11 08:00:00.000000', 1, '2025-06-12 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (19, 'U019', 'zhushi', '朱十', '朱十', 'zhushi@example.com', '13800000019', '$2a$10$encrypted_password', '/avatar/zhushi.png', 'FEMALE', '1992-05-20', 'ENABLED', null, '2025-06-11 17:30:00.000000', '192.168.1.108', 0, 0, 0, 1, '2025-01-11 08:00:00.000000', 1, '2025-06-11 17:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (20, 'U020', 'intern01', '实习生A', '实习生A', 'intern01@wanxiang.com', '13800000020', '$2a$10$encrypted_password', '/avatar/intern01.png', 'MALE', '2000-01-01', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (21, 'U021', 'intern02', '实习生B', '实习生B', 'intern02@wanxiang.com', '13800000021', '$2a$10$encrypted_password', '/avatar/intern02.png', 'FEMALE', '2001-06-15', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (22, 'U022', 'contractor01', '外包C', '外包C', 'contractor01@external.com', '13800000022', '$2a$10$encrypted_password', '/avatar/contractor01.png', 'MALE', '1995-08-20', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-04-01 08:00:00.000000', 1, '2025-04-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (23, 'U023', 'user23', '用户23', '用户23', 'user23@wanxiang.com', '13800000023', '$2a$10$encrypted_password', '/avatar/user23.png', 'MALE', '1989-03-10', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (24, 'U024', 'user24', '用户24', '用户24', 'user24@wanxiang.com', '13800000024', '$2a$10$encrypted_password', '/avatar/user24.png', 'FEMALE', '1991-07-22', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (25, 'U025', 'user25', '用户25', '用户25', 'user25@dingxin.com', '13800000025', '$2a$10$encrypted_password', '/avatar/user25.png', 'MALE', '1993-11-05', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-02 08:00:00.000000', 1, '2025-02-02 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (26, 'U026', 'user26', '用户26', '用户26', 'user26@dingxin.com', '13800000026', '$2a$10$encrypted_password', '/avatar/user26.png', 'FEMALE', '1990-04-18', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-02 08:00:00.000000', 1, '2025-02-02 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (27, 'U027', 'user27', '用户27', '用户27', 'user27@haina.com', '13800000027', '$2a$10$encrypted_password', '/avatar/user27.png', 'MALE', '1988-09-30', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-03 08:00:00.000000', 1, '2025-02-03 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (28, 'U028', 'user28', '用户28', '用户28', 'user28@wanxiang.com', '13800000028', '$2a$10$encrypted_password', '/avatar/user28.png', 'MALE', '1992-12-25', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-04 08:00:00.000000', 1, '2025-02-04 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (29, 'U029', 'user29', '用户29', '用户29', 'user29@wanxiang.com', '13800000029', '$2a$10$encrypted_password', '/avatar/user29.png', 'FEMALE', '1994-05-12', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-04 08:00:00.000000', 1, '2025-02-04 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (30, 'U030', 'user30', '用户30', '用户30', 'user30@dingxin.com', '13800000030', '$2a$10$encrypted_password', '/avatar/user30.png', 'MALE', '1987-08-08', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-05 08:00:00.000000', 1, '2025-02-05 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5, 'U005', 'zhaoliu', '赵六', '赵六', 'zhaoliu@wanxiang.com', '13800000005', '$2a$10$encrypted_password', '/avatar/zhaoliu.png', 'MALE', '1995-11-25', 'ENABLED', null, '2026-06-13 15:29:58.746089', null, 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-01-05 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2, 'U002', 'zhangsan', '张三', '张三', 'zhangsan@wanxiang.com', '13800000002', '$2a$10$encrypted_password', '/avatar/zhangsan.png', 'MALE', '1990-03-15', 'ENABLED', null, '2026-06-12 22:27:14.829712', '192.168.1.101', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-12 09:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, real_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3, 'U003', 'lisi', '李四', '李四', 'lisi@wanxiang.com', '13800000003', '$2a$10$encrypted_password', '/avatar/lisi.png', 'MALE', '1988-06-20', 'ENABLED', null, '2026-06-13 16:00:09.861823', '192.168.1.102', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-11 16:00:00.000000', 'NOT_DELETED', null);

create table sys_user_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    user_id        bigint       not null,
    target_type    varchar(20)  not null,
    target_id      bigint       not null,
    is_primary     boolean      not null,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) not null,
    update_by      bigint       not null,
    update_at      timestamp(6) not null,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_user_policy is '用户策略表';

comment on column sys_user_policy.id is '主键ID';

comment on column sys_user_policy.policy_code is '策略编码';

comment on column sys_user_policy.policy_name is '策略名称';

comment on column sys_user_policy.user_id is '用户ID';

comment on column sys_user_policy.target_type is '目标类型';

comment on column sys_user_policy.target_id is '目标ID';

comment on column sys_user_policy.is_primary is '是否为主租户/主部门';

comment on column sys_user_policy.status is '策略状态';

comment on column sys_user_policy.disable_reason is '禁用原因';

comment on column sys_user_policy.create_tenant is '创建租户ID';

comment on column sys_user_policy.create_dept is '创建部门ID';

comment on column sys_user_policy.create_role is '创建角色ID';

comment on column sys_user_policy.create_by is '创建人用户ID';

comment on column sys_user_policy.create_at is '创建时间';

comment on column sys_user_policy.update_by is '更新人用户ID';

comment on column sys_user_policy.update_at is '更新时间';

comment on column sys_user_policy.is_deleted is '逻辑删除状态';

comment on column sys_user_policy.deleted_at is '删除时间';

alter table sys_user_policy
    owner to postgres;

INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4001, 'UP_001', '张三→万象集团', 2, 'TENANT', 101, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-15 08:00:00.000000', 1, '2025-01-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4002, 'UP_002', '张三→财务部', 4001, 'DEPT', 2001, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-15 08:00:00.000000', 1, '2025-01-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4003, 'UP_003', '张三→财务专员角色', 4001, 'ROLE', 3005, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-15 08:00:00.000000', 1, '2025-01-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4004, 'UP_004', '张三→鼎新集团', 2, 'TENANT', 102, false, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4005, 'UP_005', '张三→财务部(鼎新)', 4004, 'DEPT', 2003, true, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4006, 'UP_006', '张三→部门经理角色(鼎新)', 4004, 'ROLE', 3009, false, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4007, 'UP_007', '李四→万象集团', 3, 'TENANT', 101, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-16 08:00:00.000000', 1, '2025-01-16 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4008, 'UP_008', '李四→研发部', 4007, 'DEPT', 2004, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-16 08:00:00.000000', 1, '2025-01-16 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4009, 'UP_009', '李四→研发工程师', 4007, 'ROLE', 3007, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-16 08:00:00.000000', 1, '2025-01-16 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4010, 'UP_010', '王五→万象集团', 4, 'TENANT', 101, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-17 08:00:00.000000', 1, '2025-01-17 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4011, 'UP_011', '王五→销售部', 4010, 'DEPT', 2007, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-17 08:00:00.000000', 1, '2025-01-17 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4012, 'UP_012', '王五→销售专员', 4010, 'ROLE', 3006, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-17 08:00:00.000000', 1, '2025-01-17 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4013, 'UP_013', '赵六→华东分公司', 5, 'TENANT', 111, true, 'ACTIVE', null, 111, 0, 0, 1, '2025-02-15 08:00:00.000000', 1, '2025-02-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4014, 'UP_014', '赵六→研发部(华东)', 4013, 'DEPT', 2005, true, 'ACTIVE', null, 111, 0, 0, 1, '2025-02-15 08:00:00.000000', 1, '2025-02-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4015, 'UP_015', '赵六→普通员工', 4013, 'ROLE', 3004, false, 'ACTIVE', null, 111, 0, 0, 1, '2025-02-15 08:00:00.000000', 1, '2025-02-15 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4016, 'UP_016', '陈二→鼎新集团', 11, 'TENANT', 102, true, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-18 08:00:00.000000', 1, '2025-01-18 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4017, 'UP_017', '陈二→生产部', 4016, 'DEPT', 2012, true, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-18 08:00:00.000000', 1, '2025-01-18 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4018, 'UP_018', '陈二→部门经理', 4016, 'ROLE', 3009, false, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-18 08:00:00.000000', 1, '2025-01-18 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4019, 'UP_019', '冯三→鼎新集团', 12, 'TENANT', 102, true, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-19 08:00:00.000000', 1, '2025-01-19 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4020, 'UP_020', '冯三→质量部', 4019, 'DEPT', 2013, true, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-19 08:00:00.000000', 1, '2025-01-19 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4021, 'UP_021', '冯三→质检员', 4019, 'ROLE', 3012, false, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-19 08:00:00.000000', 1, '2025-01-19 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4022, 'UP_022', '杨九→万象集团', 18, 'TENANT', 101, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-21 08:00:00.000000', 1, '2025-01-21 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4023, 'UP_023', '杨九→研发部(万象)', 4022, 'DEPT', 2004, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-21 08:00:00.000000', 1, '2025-01-21 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4024, 'UP_024', '杨九→研发工程师(万象)', 4022, 'ROLE', 3007, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-21 08:00:00.000000', 1, '2025-01-21 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4025, 'UP_025', '杨九→海纳集团', 18, 'TENANT', 103, false, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-22 08:00:00.000000', 1, '2025-01-22 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4026, 'UP_026', '杨九→客服部(海纳)', 4025, 'DEPT', 2014, true, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-22 08:00:00.000000', 1, '2025-01-22 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4027, 'UP_027', '杨九→普通员工(海纳)', 4025, 'ROLE', 3014, false, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-22 08:00:00.000000', 1, '2025-01-22 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4028, 'UP_028', '实习生A→万象集团', 20, 'TENANT', 101, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4029, 'UP_029', '实习生A→研发部', 4028, 'DEPT', 2004, true, 'ACTIVE', null, 101, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, user_id, target_type, target_id, is_primary, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4030, 'UP_030', '实习生A→实习生角色', 4028, 'ROLE', 3015, false, 'ACTIVE', null, 101, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);

create table sys_tenant_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    source_type    varchar(20)  not null,
    source_id      bigint       not null,
    tenant_id      bigint       not null,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) not null,
    update_by      bigint       not null,
    update_at      timestamp(6) not null,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_tenant_policy is '租户策略表';

comment on column sys_tenant_policy.id is '主键ID';

comment on column sys_tenant_policy.policy_code is '策略编码';

comment on column sys_tenant_policy.policy_name is '策略名称';

comment on column sys_tenant_policy.source_type is '源实体类型';

comment on column sys_tenant_policy.source_id is '系统实体ID';

comment on column sys_tenant_policy.tenant_id is '目标租户ID';

comment on column sys_tenant_policy.status is '策略状态';

comment on column sys_tenant_policy.disable_reason is '禁用原因';

comment on column sys_tenant_policy.create_tenant is '创建租户ID';

comment on column sys_tenant_policy.create_dept is '创建部门ID';

comment on column sys_tenant_policy.create_role is '创建角色ID';

comment on column sys_tenant_policy.create_by is '创建人用户ID';

comment on column sys_tenant_policy.create_at is '创建时间';

comment on column sys_tenant_policy.update_by is '更新人用户ID';

comment on column sys_tenant_policy.update_at is '更新时间';

comment on column sys_tenant_policy.is_deleted is '逻辑删除状态';

comment on column sys_tenant_policy.deleted_at is '删除时间';

alter table sys_tenant_policy
    owner to postgres;

INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2001, 'TP_DEPT_001', '财务部→万象集团', 'DEPT', 301, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2002, 'TP_DEPT_002', '财务部→华东分公司', 'DEPT', 301, 111, 'ACTIVE', null, 111, 0, 0, 1, '2025-02-10 08:00:00.000000', 1, '2025-02-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2003, 'TP_DEPT_003', '财务部→鼎新集团', 'DEPT', 301, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2004, 'TP_DEPT_004', '研发部→万象集团', 'DEPT', 302, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2005, 'TP_DEPT_005', '研发部→华东分公司', 'DEPT', 302, 111, 'ACTIVE', null, 111, 0, 0, 1, '2025-02-10 08:00:00.000000', 1, '2025-02-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2006, 'TP_DEPT_006', '研发部→上海办事处', 'DEPT', 302, 121, 'ACTIVE', null, 121, 0, 0, 1, '2025-03-10 08:00:00.000000', 1, '2025-03-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2007, 'TP_DEPT_007', '销售部→万象集团', 'DEPT', 303, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2008, 'TP_DEPT_008', '销售部→华南分公司', 'DEPT', 303, 112, 'ACTIVE', null, 112, 0, 0, 1, '2025-02-10 08:00:00.000000', 1, '2025-02-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2009, 'TP_DEPT_009', '销售部→鼎新集团', 'DEPT', 303, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2010, 'TP_DEPT_010', '人力资源部→万象集团', 'DEPT', 304, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2011, 'TP_DEPT_011', 'IT部→万象集团', 'DEPT', 305, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2012, 'TP_DEPT_012', '生产部→鼎新集团', 'DEPT', 307, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2013, 'TP_DEPT_013', '质量部→鼎新集团', 'DEPT', 308, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2014, 'TP_DEPT_014', '客服部→海纳集团', 'DEPT', 310, 103, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2015, 'TP_DEPT_015', '生产部→武汉工厂', 'DEPT', 307, 211, 'ACTIVE', null, 211, 0, 0, 1, '2025-03-10 08:00:00.000000', 1, '2025-03-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3001, 'TP_ROLE_001', '超级管理员→万象集团', 'ROLE', 401, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3002, 'TP_ROLE_002', '租户管理员→万象集团', 'ROLE', 411, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3003, 'TP_ROLE_003', '部门经理→万象集团', 'ROLE', 421, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3004, 'TP_ROLE_004', '普通员工→万象集团', 'ROLE', 431, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3005, 'TP_ROLE_005', '财务专员→万象集团', 'ROLE', 441, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3006, 'TP_ROLE_006', '销售专员→万象集团', 'ROLE', 444, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3007, 'TP_ROLE_007', '研发工程师→万象集团', 'ROLE', 445, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3008, 'TP_ROLE_008', '租户管理员→鼎新集团', 'ROLE', 411, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3009, 'TP_ROLE_009', '部门经理→鼎新集团', 'ROLE', 421, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3010, 'TP_ROLE_010', '普通员工→鼎新集团', 'ROLE', 431, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3011, 'TP_ROLE_011', '生产员工→鼎新集团', 'ROLE', 446, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3012, 'TP_ROLE_012', '质检员→鼎新集团', 'ROLE', 447, 102, 'ACTIVE', null, 102, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3013, 'TP_ROLE_013', '租户管理员→海纳集团', 'ROLE', 411, 103, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3014, 'TP_ROLE_014', '普通员工→海纳集团', 'ROLE', 431, 103, 'ACTIVE', null, 103, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant_policy (id, policy_code, policy_name, source_type, source_id, tenant_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3015, 'TP_ROLE_015', '实习生→万象集团', 'ROLE', 432, 101, 'ACTIVE', null, 101, 0, 0, 1, '2025-03-10 08:00:00.000000', 1, '2025-03-10 08:00:00.000000', 'NOT_DELETED', null);

create table sys_perm_policy
(
    id              bigint       not null
        primary key,
    policy_code     varchar(100) not null,
    policy_name     varchar(100) not null,
    perm_id         bigint       not null,
    target_type     varchar(20)  not null,
    target_id       bigint       not null,
    data_scope      varchar(20)  default NULL::character varying,
    table_name      varchar(100) default NULL::character varying,
    field_operation jsonb        default '{}'::jsonb,
    status          varchar(20)  not null,
    disable_reason  varchar(200) default NULL::character varying,
    create_tenant   bigint       not null,
    create_dept     bigint       not null,
    create_role     bigint       not null,
    create_by       bigint       not null,
    create_at       timestamp(6) not null,
    update_by       bigint       not null,
    update_at       timestamp(6) not null,
    is_deleted      varchar(20)  not null,
    deleted_at      timestamp(6) default NULL::timestamp without time zone
);

comment on table sys_perm_policy is '权限策略表';

comment on column sys_perm_policy.id is '主键ID';

comment on column sys_perm_policy.policy_code is '策略编码';

comment on column sys_perm_policy.policy_name is '策略名称';

comment on column sys_perm_policy.perm_id is '权限ID';

comment on column sys_perm_policy.target_type is '目标类型';

comment on column sys_perm_policy.target_id is '目标ID';

comment on column sys_perm_policy.data_scope is '行级权限';

comment on column sys_perm_policy.table_name is '关联的数据表名';

comment on column sys_perm_policy.field_operation is '字段级权限配置';

comment on column sys_perm_policy.status is '策略状态';

comment on column sys_perm_policy.disable_reason is '禁用原因';

comment on column sys_perm_policy.create_tenant is '创建租户ID';

comment on column sys_perm_policy.create_dept is '创建部门ID';

comment on column sys_perm_policy.create_role is '创建角色ID';

comment on column sys_perm_policy.create_by is '创建人用户ID';

comment on column sys_perm_policy.create_at is '创建时间';

comment on column sys_perm_policy.update_by is '更新人用户ID';

comment on column sys_perm_policy.update_at is '更新时间';

comment on column sys_perm_policy.is_deleted is '逻辑删除状态';

comment on column sys_perm_policy.deleted_at is '删除时间';

alter table sys_perm_policy
    owner to postgres;

INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5001, 'PP_001', '用户管理权限→万象集团', 511, 'TENANT', 101, 'ALL', 'sys_user', '{"email": ["READ", "CREATE", "UPDATE"], "phone": ["READ", "CREATE", "UPDATE"], "status": ["READ", "UPDATE"], "password": ["CREATE", "UPDATE"], "user_name": ["READ", "CREATE", "UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5002, 'PP_002', '用户查看权限→万象集团', 521, 'TENANT', 101, 'ALL', 'sys_user', '{"email": ["READ"], "phone": ["READ"], "status": ["READ"], "nick_name": ["READ"], "user_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5003, 'PP_003', '用户新增权限→万象集团', 522, 'TENANT', 101, 'ALL', 'sys_user', '{"email": ["CREATE"], "phone": ["CREATE"], "password": ["CREATE"], "nick_name": ["CREATE"], "user_name": ["CREATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5004, 'PP_004', '用户编辑权限→万象集团', 523, 'TENANT', 101, 'ALL', 'sys_user', '{"email": ["UPDATE"], "phone": ["UPDATE"], "status": ["UPDATE"], "nick_name": ["UPDATE"], "user_name": ["UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5005, 'PP_005', '用户删除权限→万象集团', 524, 'TENANT', 101, 'ALL', null, '{}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5006, 'PP_006', '订单管理权限→万象集团', 541, 'TENANT', 101, 'ALL', 'biz_order', '{"amount": ["READ", "CREATE", "UPDATE"], "status": ["READ", "UPDATE"], "order_no": ["READ", "CREATE", "UPDATE"], "customer_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5007, 'PP_007', '销售报表权限→万象集团', 551, 'TENANT', 101, 'ALL', null, '{}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5008, 'PP_008', '用户管理权限→鼎新集团', 511, 'TENANT', 102, 'ALL', 'sys_user', '{"email": ["READ"], "phone": ["READ"], "status": ["READ", "UPDATE"], "user_name": ["READ", "CREATE", "UPDATE"]}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5009, 'PP_009', '生产报表权限→鼎新集团', 553, 'TENANT', 102, 'ALL', null, '{}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-20 08:00:00.000000', 1, '2025-01-20 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5010, 'PP_010', '用户查看→普通员工角色', 5002, 'ROLE', 3004, 'SELF', 'sys_user', '{"email": ["READ"], "phone": ["READ"], "user_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5011, 'PP_011', '用户管理→租户管理员', 5001, 'ROLE', 3002, 'ALL', 'sys_user', '{"email": ["READ", "CREATE", "UPDATE"], "phone": ["READ", "CREATE", "UPDATE"], "status": ["READ", "UPDATE", "DELETE"], "password": ["CREATE", "UPDATE"], "user_name": ["READ", "CREATE", "UPDATE", "DELETE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5012, 'PP_012', '用户新增→部门经理', 5003, 'ROLE', 3003, 'DEPT_AND_SUB', 'sys_user', '{"email": ["CREATE"], "phone": ["CREATE"], "password": ["CREATE"], "user_name": ["CREATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5013, 'PP_013', '用户编辑→部门经理', 5004, 'ROLE', 3003, 'DEPT_AND_SUB', 'sys_user', '{"email": ["UPDATE"], "phone": ["UPDATE"], "status": ["UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5014, 'PP_014', '订单管理→销售专员', 5006, 'ROLE', 3006, 'SELF', 'biz_order', '{"amount": ["READ"], "order_no": ["READ", "CREATE", "UPDATE"], "customer_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5015, 'PP_015', '销售报表→部门经理', 5007, 'ROLE', 3003, 'DEPT_AND_SUB', null, '{}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5016, 'PP_016', '用户删除→财务部', 5005, 'DEPT', 2001, 'DEPT', null, '{}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-26 08:00:00.000000', 1, '2025-01-26 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5017, 'PP_017', '用户查看→研发部', 5002, 'DEPT', 2004, 'DEPT', 'sys_user', '{"email": ["READ"], "user_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-26 08:00:00.000000', 1, '2025-01-26 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5018, 'PP_018', '订单管理→销售部', 5006, 'DEPT', 2007, 'DEPT', 'biz_order', '{"amount": ["READ", "CREATE", "UPDATE"], "order_no": ["READ", "CREATE", "UPDATE"], "customer_name": ["READ", "CREATE", "UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-26 08:00:00.000000', 1, '2025-01-26 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5019, 'PP_019', '用户管理→张三(特殊授权)', 5001, 'USER', 4001, 'DEPT', 'sys_user', '{"email": ["READ", "UPDATE"], "status": ["READ"], "user_name": ["READ", "UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-27 08:00:00.000000', 1, '2025-01-27 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5020, 'PP_020', '销售报表→王五(特殊授权)', 5007, 'USER', 4010, 'SELF', null, '{}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-27 08:00:00.000000', 1, '2025-01-27 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5021, 'PP_021', '用户管理→租户管理员(鼎新)', 5008, 'ROLE', 3008, 'ALL', 'sys_user', '{"email": ["READ", "CREATE", "UPDATE"], "status": ["READ", "UPDATE"], "user_name": ["READ", "CREATE", "UPDATE", "DELETE"]}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5022, 'PP_022', '用户管理→部门经理(鼎新)', 5008, 'ROLE', 3009, 'DEPT_AND_SUB', 'sys_user', '{"email": ["READ"], "status": ["READ", "UPDATE"], "user_name": ["READ", "CREATE", "UPDATE"]}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5023, 'PP_023', '生产报表→生产员工(鼎新)', 5009, 'ROLE', 3011, 'DEPT', null, '{}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-01-25 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5024, 'PP_024', '用户管理→生产部(鼎新)', 5008, 'DEPT', 2012, 'DEPT', 'sys_user', '{"phone": ["READ"], "status": ["READ"], "user_name": ["READ"]}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-26 08:00:00.000000', 1, '2025-01-26 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5025, 'PP_025', '生产报表→质量部(鼎新)', 5009, 'DEPT', 2013, 'DEPT', null, '{}', 'ACTIVE', null, 102, 0, 0, 1, '2025-01-26 08:00:00.000000', 1, '2025-01-26 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5026, 'PP_026', '用户查看→已禁用角色', 5002, 'ROLE', 3004, 'SELF', 'sys_user', '{"user_name": ["READ"]}', 'DISABLED', '父权限已禁用', 101, 0, 0, 1, '2025-01-25 08:00:00.000000', 1, '2025-05-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5027, 'PP_027', '用户数据权限→财务专员', 561, 'TENANT', 101, 'ALL', 'sys_user', '{"email": ["READ"], "phone": ["READ"], "salary": ["READ", "UPDATE"], "user_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-28 08:00:00.000000', 1, '2025-01-28 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5028, 'PP_028', '订单数据权限→万象集团', 562, 'TENANT', 101, 'ALL', 'biz_order', '{"amount": ["READ", "CREATE", "UPDATE"], "order_no": ["READ", "CREATE", "UPDATE"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-28 08:00:00.000000', 1, '2025-01-28 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5029, 'PP_029', '薪资数据权限→财务部', 563, 'TENANT', 101, 'DEPT', 'hr_salary', '{"bonus": ["READ", "UPDATE"], "total": ["READ"], "base_salary": ["READ", "UPDATE"], "employee_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-28 08:00:00.000000', 1, '2025-01-28 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, perm_id, target_type, target_id, data_scope, table_name, field_operation, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5030, 'PP_030', '薪资数据权限→HR专员', 5029, 'ROLE', 3005, 'DEPT', 'hr_salary', '{"bonus": ["READ", "UPDATE"], "base_salary": ["READ", "CREATE", "UPDATE"], "employee_name": ["READ"]}', 'ACTIVE', null, 101, 0, 0, 1, '2025-01-29 08:00:00.000000', 1, '2025-01-29 08:00:00.000000', 'NOT_DELETED', null);
