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
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7202, 'ROLE_EMPLOYEE', '普通员工', '基本业务权限', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.580771', 1, '2026-06-15 08:19:09.580771', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7203, 'ROLE_AUDITOR', '审计员', '审计监控权限', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.580771', 1, '2026-06-15 08:19:09.580771', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_code, role_name, role_desc, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7201, 'ROLE_ADMIN', '管理员', '拥有管理权限', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.580771', 1, '2026-06-15 08:19:09.580771', 'NOT_DELETED', null);
