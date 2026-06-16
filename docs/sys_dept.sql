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
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7102, 'DEPT_MKT', '市场部', '市场营销部门', 0, '/7102', 1, false, 0, 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.573786', 1, '2026-06-15 08:19:09.573786', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7103, 'DEPT_SALES', '销售部', '销售部门', 0, '/7103', 1, false, 0, 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.573786', 1, '2026-06-15 08:19:09.573786', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, path, level, has_children, leader_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7101, 'DEPT_RD', '研发部', '技术研发部门', 0, '/7101', 1, false, 0, 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.573786', 1, '2026-06-15 08:19:09.573786', 'NOT_DELETED', null);
