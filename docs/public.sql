create table sys_dept
(
    id             bigint       not null primary key,
    dept_code      varchar(50)  not null,
    dept_name      varchar(100) not null,
    dept_desc      varchar(200) not null,
    parent_id      bigint       not null,
    parent_code    varchar(50)  not null,
    parent_name    varchar(100) not null,
    path           varchar(1000) not null,
    tenant_id      bigint       not null,
    tenant_code    varchar(50)  not null,
    tenant_name    varchar(100) not null,
    leader_id      bigint       not null,
    leader_name    varchar(50)  not null,
    sort_order     integer      not null,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_dept is '部门表';

comment on column sys_dept.id is '主键ID';

comment on column sys_dept.dept_code is '部门编码';

comment on column sys_dept.dept_name is '部门名称';

comment on column sys_dept.dept_desc is '部门描述';

comment on column sys_dept.parent_id is '父部门ID';

comment on column sys_dept.parent_code is '父部门编码';

comment on column sys_dept.parent_name is '父部门名称';

comment on column sys_dept.path is '部门层级路径';

comment on column sys_dept.tenant_id is '所属租户ID';

comment on column sys_dept.tenant_code is '租户编码';

comment on column sys_dept.tenant_name is '租户名称';

comment on column sys_dept.leader_id is '部门负责人ID';

comment on column sys_dept.leader_name is '部门负责人姓名';

comment on column sys_dept.sort_order is '排序序号';

comment on column sys_dept.status is '部门状态';

comment on column sys_dept.disable_reason is '禁用原因';

comment on column sys_dept.create_tenant is '创建时所属租户ID';

comment on column sys_dept.create_dept is '创建时所属部门ID';

comment on column sys_dept.create_role is '创建时使用角色ID';

comment on column sys_dept.create_by is '创建人ID';

comment on column sys_dept.create_at is '创建时间';

comment on column sys_dept.update_by is '更新人ID';

comment on column sys_dept.update_at is '更新时间';

comment on column sys_dept.is_deleted is '逻辑删除标记';

comment on column sys_dept.deleted_at is '删除时间';

alter table sys_dept owner to postgres;

INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6001, 'DEPT_ZJB', '总经办', '万象集团总经理办公室', 0, 'ROOT', '根节点', '/6001', 100, 'WANXIANG', '万象集团', 1, 'admin', 1, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6002, 'DEPT_CW', '财务部', '万象集团财务管理中心', 0, 'ROOT', '根节点', '/6002', 100, 'WANXIANG', '万象集团', 10, 'zhou_ba', 2, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6003, 'DEPT_HR', '人力资源部', '万象集团人力资源管理', 0, 'ROOT', '根节点', '/6003', 100, 'WANXIANG', '万象集团', 13, 'qian_yi', 3, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6004, 'DEPT_IT', '信息技术部', '万象集团信息技术与系统支持', 0, 'ROOT', '根节点', '/6004', 100, 'WANXIANG', '万象集团', 18, 'xu_readonly', 4, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6005, 'DEPT_SJ', '审计部', '万象集团内部审计监督', 0, 'ROOT', '根节点', '/6005', 100, 'WANXIANG', '万象集团', 17, 'huang_audit', 5, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6006, 'DEPT_ZHGL', '综合管理部', '华东分公司综合行政管理', 6001, 'DEPT_ZJB', '总经办', '/6001/6006', 110, 'WX_EAST', '万象集团-华东分公司', 3, 'li_jingli', 1, 'ENABLED', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6007, 'DEPT_YW1', '业务一部', '华东分公司核心业务部门', 6001, 'DEPT_ZJB', '总经办', '/6001/6007', 110, 'WX_EAST', '万象集团-华东分公司', 4, 'wang_zhuguan', 2, 'ENABLED', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6008, 'DEPT_XS', '销售部', '华南分公司销售业务管理', 6001, 'DEPT_ZJB', '总经办', '/6001/6008', 120, 'WX_SOUTH', '万象集团-华南分公司', 7, 'wang_wu', 1, 'ENABLED', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6009, 'DEPT_KF', '客服部', '华南分公司客户服务支持', 6001, 'DEPT_ZJB', '总经办', '/6001/6009', 120, 'WX_SOUTH', '万象集团-华南分公司', 8, 'zhao_liu', 2, 'ENABLED', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6010, 'DEPT_YY', '运营部', '华北分公司运营管理', 6001, 'DEPT_ZJB', '总经办', '/6001/6010', 130, 'WX_NORTH', '万象集团-华北分公司', 11, 'wu_jiu', 1, 'ENABLED', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6011, 'DEPT_SC', '生产管理部', '鼎新集团生产调度与管理', 0, 'ROOT', '根节点', '/6011', 200, 'DINGXIN', '鼎新集团', 19, 'dingxin_ceo', 1, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6012, 'DEPT_ZL', '质量部', '鼎新集团质量检验与控制', 0, 'ROOT', '根节点', '/6012', 200, 'DINGXIN', '鼎新集团', 20, 'dong_manager', 2, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6013, 'DEPT_CG', '采购部', '鼎新集团物资采购管理', 0, 'ROOT', '根节点', '/6013', 200, 'DINGXIN', '鼎新集团', 21, 'guo_staff', 3, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6014, 'DEPT_YF1', '研发一部', '星辰科技核心产品研发', 0, 'ROOT', '根节点', '/6014', 300, 'XINGCHEN', '星辰科技有限公司', 22, 'xingchen_cto', 1, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6015, 'DEPT_YF2', '研发二部', '星辰科技前沿技术研究', 0, 'ROOT', '根节点', '/6015', 300, 'XINGCHEN', '星辰科技有限公司', 23, 'yan_dev', 2, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6016, 'DEPT_SC2', '市场部', '星辰科技市场推广与品牌', 0, 'ROOT', '根节点', '/6016', 300, 'XINGCHEN', '星辰科技有限公司', 24, 'cai_market', 3, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6017, 'DEPT_HW', '海外业务部', '星辰科技海外市场拓展', 0, 'ROOT', '根节点', '/6017', 300, 'XINGCHEN', '星辰科技有限公司', 25, 'han_overseas', 4, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6018, 'DEPT_TZ', '投资部', '海纳百川投资管理与决策', 0, 'ROOT', '根节点', '/6018', 400, 'HAINA', '海纳百川集团', 26, 'hai_manager', 1, 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6019, 'DEPT_FK', '风控部', '海纳百川风险控制与合规', 0, 'ROOT', '根节点', '/6019', 400, 'HAINA', '海纳百川集团', 26, 'hai_manager', 2, 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6020, 'DEPT_KC', '课程研发部', '教育科技课程内容研发', 0, 'ROOT', '根节点', '/6020', 410, 'HN_EDU', '海纳百川-教育科技', 16, 'chu_si', 1, 'ENABLED', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6021, 'DEPT_JX', '教学运营部', '教育科技教学运营管理', 0, 'ROOT', '根节点', '/6021', 410, 'HN_EDU', '海纳百川-教育科技', 27, 'lu_edu', 2, 'ENABLED', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6022, 'DEPT_YL', '医疗技术部', '医疗健康技术研发与支持', 0, 'ROOT', '根节点', '/6022', 420, 'HN_MEDICAL', '海纳百川-医疗健康', 28, 'qiao_med', 1, 'ENABLED', null, 420, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6023, 'DEPT_JK', '健康管理部', '医疗健康运营与健康管理', 0, 'ROOT', '根节点', '/6023', 420, 'HN_MEDICAL', '海纳百川-医疗健康', 28, 'qiao_med', 2, 'ENABLED', null, 420, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6024, 'DEPT_CC', '仓储管理部', '天翔物流仓储与库存管理', 0, 'ROOT', '根节点', '/6024', 500, 'TIANXIANG', '天翔物流', 29, 'tian_logistics', 1, 'ENABLED', null, 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6025, 'DEPT_YS', '运输调度部', '天翔物流运输调度与配送', 0, 'ROOT', '根节点', '/6025', 500, 'TIANXIANG', '天翔物流', 29, 'tian_logistics', 2, 'ENABLED', null, 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6026, 'DEPT_ZH', '综合部', '西南分公司综合事务管理', 0, 'ROOT', '根节点', '/6026', 140, 'WX_SOUTHWEST', '万象集团-西南分公司', 1, 'admin', 1, 'DISABLED', '西南分公司已停用', 140, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6027, 'DEPT_CB', '筹备组', '东北分公司筹备工作组', 0, 'ROOT', '根节点', '/6027', 230, 'DX_NORTHEAST', '鼎新集团-东北分公司', 1, 'admin', 1, 'PENDING', '新设立，尚未正式运营', 230, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6028, 'DEPT_NJ', '南京业务部', '南京办事处业务部门', 0, 'ROOT', '根节点', '/6028', 113, 'WX_EAST_NJ', '万象集团-华东-南京办事处', 1, 'admin', 1, 'DISABLED', '南京办事处已停用', 113, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6029, 'DEPT_TJ', '天津业务部', '天津办事处业务部门', 0, 'ROOT', '根节点', '/6029', 132, 'WX_NORTH_TJ', '万象集团-华北-天津办事处', 1, 'admin', 1, 'EXPIRED', '租约已过期', 132, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept (id, dept_code, dept_name, dept_desc, parent_id, parent_code, parent_name, path, tenant_id, tenant_code, tenant_name, leader_id, leader_name, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6030, 'DEPT_GJ', '国际合作部', '海外事业部国际合作管理', 0, 'ROOT', '根节点', '/6030', 330, 'XC_OVERSEAS', '星辰科技-海外事业部', 25, 'han_overseas', 1, 'ENABLED', null, 330, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_dept_policy
(
    id             bigint       not null primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    user_id        bigint       not null,
    is_primary     boolean      not null default false,
    join_time      timestamp(6) default CURRENT_TIMESTAMP,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_dept_policy is '部门策略表';

comment on column sys_dept_policy.id is '主键ID';

comment on column sys_dept_policy.policy_code is '策略编码';

comment on column sys_dept_policy.policy_name is '策略名称';

comment on column sys_dept_policy.target_id is '授权目标ID（部门ID）';

comment on column sys_dept_policy.target_type is '授权目标类型';

comment on column sys_dept_policy.user_id is '用户ID';

comment on column sys_dept_policy.is_primary is '是否主部门';

comment on column sys_dept_policy.join_time is '加入部门时间';

comment on column sys_dept_policy.status is '策略状态';

comment on column sys_dept_policy.disable_reason is '禁用原因';

comment on column sys_dept_policy.create_tenant is '创建时所属租户ID';

comment on column sys_dept_policy.create_dept is '创建时所属部门ID';

comment on column sys_dept_policy.create_role is '创建时使用角色ID';

comment on column sys_dept_policy.create_by is '创建人ID';

comment on column sys_dept_policy.create_at is '创建时间';

comment on column sys_dept_policy.update_by is '更新人ID';

comment on column sys_dept_policy.update_at is '更新时间';

comment on column sys_dept_policy.is_deleted is '逻辑删除标记';

comment on column sys_dept_policy.deleted_at is '删除时间';

alter table sys_dept_policy owner to postgres;

INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7001, 'DP_001', 'admin→总经办(主)', 6001, 'DEPT', 1, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7002, 'DP_002', 'zhang_zong→总经办(主)', 6001, 'DEPT', 2, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7003, 'DP_003', 'li_jingli→综合管理部(主)', 6006, 'DEPT', 3, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7004, 'DP_004', 'wang_zhuguan→业务一部(主)', 6007, 'DEPT', 4, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7005, 'DP_005', 'zhang_san→业务一部(主)', 6007, 'DEPT', 5, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7006, 'DP_006', 'li_si→销售部(主)', 6008, 'DEPT', 6, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7007, 'DP_007', 'wang_wu→销售部(主)', 6008, 'DEPT', 7, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7008, 'DP_008', 'wang_wu→客服部(兼)', 6009, 'DEPT', 7, false, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7009, 'DP_009', 'zhao_liu→客服部(主)', 6009, 'DEPT', 8, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7010, 'DP_010', 'sun_qi→业务一部(主)', 6007, 'DEPT', 9, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7011, 'DP_011', 'zhou_ba→财务部(主)', 6002, 'DEPT', 10, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7012, 'DP_012', 'wu_jiu→运营部(主)', 6010, 'DEPT', 11, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7013, 'DP_013', 'zheng_shi→研发一部(主)', 6014, 'DEPT', 12, true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7014, 'DP_014', 'qian_yi→人力资源部(主)', 6003, 'DEPT', 13, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7015, 'DP_015', 'chen_er→客服部(主)', 6009, 'DEPT', 14, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7016, 'DP_016', 'feng_san→总经办(主)', 6001, 'DEPT', 15, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7017, 'DP_017', 'chu_si→课程研发部(主)', 6020, 'DEPT', 16, true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7018, 'DP_018', 'huang_audit→审计部(主)', 6005, 'DEPT', 17, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7019, 'DP_019', 'xu_readonly→信息技术部(主)', 6004, 'DEPT', 18, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7020, 'DP_020', 'dingxin_ceo→生产管理部(主)', 6011, 'DEPT', 19, true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7021, 'DP_021', 'dong_manager→质量部(主)', 6012, 'DEPT', 20, true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7022, 'DP_022', 'guo_staff→采购部(主)', 6013, 'DEPT', 21, true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7023, 'DP_023', 'xingchen_cto→研发一部(主)', 6014, 'DEPT', 22, true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7024, 'DP_024', 'yan_dev→研发二部(主)', 6015, 'DEPT', 23, true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7025, 'DP_025', 'cai_market→市场部(主)', 6016, 'DEPT', 24, true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7026, 'DP_026', 'han_overseas→海外业务部(主)', 6017, 'DEPT', 25, true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7027, 'DP_027', 'hai_manager→投资部(主)', 6018, 'DEPT', 26, true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7028, 'DP_028', 'lu_edu→教学运营部(主)', 6021, 'DEPT', 27, true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7029, 'DP_029', 'qiao_med→医疗技术部(主)', 6022, 'DEPT', 28, true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 420, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7030, 'DP_030', 'tian_logistics→仓储管理部(主)', 6024, 'DEPT', 29, true, '2026-05-01 08:00:00.000000', 'ACTIVE', null, 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7031, 'DP_031', 'multi_user→信息技术部(主)', 6004, 'DEPT', 30, true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7032, 'DP_032', 'multi_user→采购部(兼)', 6013, 'DEPT', 30, false, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_dept_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7033, 'DP_033', 'multi_user→研发一部(兼)', 6014, 'DEPT', 30, false, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_tenant_subscription
(
    id                bigint       not null
        primary key,
    subscription_code varchar(50)  not null,
    tenant_id         bigint       not null,
    tenant_code       varchar(50)  not null,
    tenant_name       varchar(100) not null,
    package_id        bigint       not null,
    subscription_type varchar(20)  not null,
    start_time        timestamp(6) not null,
    end_time          timestamp(6) not null,
    status            varchar(20)  not null,
    disable_reason    varchar(200) default NULL::character varying,
    is_auto_renew     boolean      not null,
    source_type       varchar(20)  not null,
    parent_id         bigint       not null,
    create_tenant     bigint       not null,
    create_dept       bigint       not null,
    create_role       bigint       not null,
    create_by         bigint       not null,
    create_at         timestamp(6) default CURRENT_TIMESTAMP,
    update_by         bigint       not null,
    update_at         timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted        varchar(20)  not null,
    deleted_at        timestamp(6)
);

comment on table sys_tenant_subscription is '租户订阅记录表';

comment on column sys_tenant_subscription.id is '主键ID';

comment on column sys_tenant_subscription.subscription_code is '订阅单编码';

comment on column sys_tenant_subscription.tenant_id is '租户ID';

comment on column sys_tenant_subscription.tenant_code is '租户编码';

comment on column sys_tenant_subscription.tenant_name is '租户名称';

comment on column sys_tenant_subscription.package_id is '套餐产品ID';

comment on column sys_tenant_subscription.subscription_type is '订阅类型';

comment on column sys_tenant_subscription.start_time is '订阅开始时间';

comment on column sys_tenant_subscription.end_time is '订阅结束时间';

comment on column sys_tenant_subscription.status is '订阅状态';

comment on column sys_tenant_subscription.disable_reason is '禁用原因';

comment on column sys_tenant_subscription.is_auto_renew is '是否自动续费';

comment on column sys_tenant_subscription.source_type is '来源类型';

comment on column sys_tenant_subscription.parent_id is '父订阅ID';

comment on column sys_tenant_subscription.create_tenant is '创建时所属租户ID';

comment on column sys_tenant_subscription.create_dept is '创建时所属部门ID';

comment on column sys_tenant_subscription.create_role is '创建时使用角色ID';

comment on column sys_tenant_subscription.create_by is '创建人ID';

comment on column sys_tenant_subscription.create_at is '创建时间';

comment on column sys_tenant_subscription.update_by is '更新人ID';

comment on column sys_tenant_subscription.update_at is '更新时间';

comment on column sys_tenant_subscription.is_deleted is '逻辑删除标记';

comment on column sys_tenant_subscription.deleted_at is '删除时间';

create table sys_tenant
(
    id              bigint        not null
        primary key,
    tenant_code     varchar(50)   not null,
    tenant_name     varchar(100)  not null,
    tenant_type     varchar(50)   not null,
    tenant_desc     varchar(500)  not null,
    tenant_logo_url varchar(500)  not null,
    parent_id       bigint        not null,
    parent_code     varchar(50)   not null,
    parent_name     varchar(100)  not null,
    path            varchar(1000) not null,
    contact_name    varchar(50)   not null,
    contact_phone   varchar(20)   not null,
    expire_time     timestamp(6)  not null,
    package_id      bigint        not null,
    package_name    varchar(100)  not null,
    ext_attributes  jsonb        default '{}'::jsonb,
    has_children    boolean       not null,
    status          varchar(20)   not null,
    disable_reason  varchar(200) default NULL::character varying,
    create_tenant   bigint        not null,
    create_dept     bigint        not null,
    create_role     bigint        not null,
    create_by       bigint        not null,
    create_at       timestamp(6) default CURRENT_TIMESTAMP,
    update_by       bigint        not null,
    update_at       timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted      varchar(20)   not null,
    deleted_at      timestamp(6)
);

comment on table sys_tenant is '租户表';

comment on column sys_tenant.id is '主键ID';

comment on column sys_tenant.tenant_code is '租户编码';

comment on column sys_tenant.tenant_name is '租户名称';

comment on column sys_tenant.tenant_type is '租户类型';

comment on column sys_tenant.tenant_desc is '租户描述';

comment on column sys_tenant.tenant_logo_url is '租户LOGO';

comment on column sys_tenant.parent_id is '父租户ID';

comment on column sys_tenant.parent_code is '父租户编码';

comment on column sys_tenant.parent_name is '父租户名称';

comment on column sys_tenant.path is '租户层级路径';

comment on column sys_tenant.contact_name is '联系人姓名';

comment on column sys_tenant.contact_phone is '联系人电话';

comment on column sys_tenant.expire_time is '服务过期时间';

comment on column sys_tenant.package_id is '主套餐ID';

comment on column sys_tenant.package_name is '主套餐名称';

comment on column sys_tenant.ext_attributes is '扩展属性';

comment on column sys_tenant.has_children is '是否存在子租户';

comment on column sys_tenant.status is '策略状态';

comment on column sys_tenant.disable_reason is '禁用原因';

comment on column sys_tenant.create_tenant is '创建时所属租户ID';

comment on column sys_tenant.create_dept is '创建时所属部门ID';

comment on column sys_tenant.create_role is '创建时使用角色ID';

comment on column sys_tenant.create_by is '创建人ID';

comment on column sys_tenant.create_at is '创建时间';

comment on column sys_tenant.update_by is '更新人ID';

comment on column sys_tenant.update_at is '更新时间';

comment on column sys_tenant.is_deleted is '逻辑删除标记';

comment on column sys_tenant.deleted_at is '删除时间';

INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (100, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团，覆盖华东、华南、华北、西南四大区域', 'https://logo.example.com/wx.png', 0, 'ROOT', '根节点', '/100', '张总', '13800001001', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 5000, "region": "全国", "industry": "综合"}', true, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (110, 'WX_EAST', '万象集团-华东分公司', 'BRANCH', '负责华东区域业务运营', 'https://logo.example.com/wx_east.png', 100, 'WANXIANG', '万象集团', '/100/110', '李经理', '13800001002', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 1200, "region": "华东", "industry": "综合"}', true, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'OFFICE', '上海地区业务运营中心', 'https://logo.example.com/wx_sh.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/111', '王主管', '13800001003', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 300, "region": "上海", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (112, 'WX_EAST_HZ', '万象集团-华东-杭州办事处', 'OFFICE', '杭州地区业务运营中心', 'https://logo.example.com/wx_hz.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/112', '孙主管', '13800001004', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 200, "region": "杭州", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (113, 'WX_EAST_NJ', '万象集团-华东-南京办事处', 'OFFICE', '南京地区业务运营中心（已停用）', 'https://logo.example.com/wx_nj.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/113', '赵主管', '13800001005', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 50, "region": "南京", "industry": "综合"}', false, 'DISABLED', '业务调整，合并至上海办事处', 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (120, 'WX_SOUTH', '万象集团-华南分公司', 'BRANCH', '负责华南区域业务运营', 'https://logo.example.com/wx_south.png', 100, 'WANXIANG', '万象集团', '/100/120', '陈经理', '13800001006', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 1000, "region": "华南", "industry": "综合"}', true, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (121, 'WX_SOUTH_SZ', '万象集团-华南-深圳办事处', 'OFFICE', '深圳地区业务运营中心', 'https://logo.example.com/wx_sz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/121', '周主管', '13800001007', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 350, "region": "深圳", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (122, 'WX_SOUTH_GZ', '万象集团-华南-广州办事处', 'OFFICE', '广州地区业务运营中心', 'https://logo.example.com/wx_gz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/122', '林主管', '13800001008', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 280, "region": "广州", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (123, 'WX_SOUTH_XM', '万象集团-华南-厦门办事处', 'OFFICE', '厦门地区业务运营中心', 'https://logo.example.com/wx_xm.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/123', '吴主管', '13800001009', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 150, "region": "厦门", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (130, 'WX_NORTH', '万象集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/wx_north.png', 100, 'WANXIANG', '万象集团', '/100/130', '武经理', '13800001010', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 800, "region": "华北", "industry": "综合"}', true, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (131, 'WX_NORTH_BJ', '万象集团-华北-北京办事处', 'OFFICE', '北京地区业务运营中心', 'https://logo.example.com/wx_bj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/131', '郑主管', '13800001011', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 400, "region": "北京", "industry": "综合"}', false, 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (132, 'WX_NORTH_TJ', '万象集团-华北-天津办事处', 'OFFICE', '天津地区业务运营中心（已过期）', 'https://logo.example.com/wx_tj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/132', '冯主管', '13800001012', '2025-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 100, "region": "天津", "industry": "综合"}', false, 'EXPIRED', '租约未续费已过期', 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (140, 'WX_SOUTHWEST', '万象集团-西南分公司', 'BRANCH', '负责西南区域业务运营（已停用）', 'https://logo.example.com/wx_sw.png', 100, 'WANXIANG', '万象集团', '/100/140', '何经理', '13800001013', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 300, "region": "西南", "industry": "综合"}', false, 'DISABLED', '战略调整暂停运营', 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (200, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dx.png', 0, 'ROOT', '根节点', '/200', '丁总', '13800002001', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 3000, "region": "全国", "industry": "制造业"}', true, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (210, 'DX_CENTRAL', '鼎新集团-华中分公司', 'BRANCH', '负责华中区域业务运营', 'https://logo.example.com/dx_central.png', 200, 'DINGXIN', '鼎新集团', '/200/210', '马经理', '13800002002', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 800, "region": "华中", "industry": "制造业"}', true, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'OFFICE', '武汉地区业务运营中心', 'https://logo.example.com/dx_wh.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/211', '董主管', '13800002003', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 300, "region": "武汉", "industry": "制造业"}', false, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (212, 'DX_CENTRAL_CS', '鼎新集团-华中-长沙办事处', 'OFFICE', '长沙地区业务运营中心', 'https://logo.example.com/dx_cs.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/212', '谢主管', '13800002004', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 200, "region": "长沙", "industry": "制造业"}', false, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (220, 'DX_NORTH', '鼎新集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/dx_north.png', 200, 'DINGXIN', '鼎新集团', '/200/220', '韩经理', '13800002005', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 600, "region": "华北", "industry": "制造业"}', true, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (221, 'DX_NORTH_SJZ', '鼎新集团-华北-石家庄办事处', 'OFFICE', '石家庄地区业务运营中心', 'https://logo.example.com/dx_sjz.png', 220, 'DX_NORTH', '鼎新集团-华北分公司', '/200/220/221', '郭员工', '13800002006', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 150, "region": "石家庄", "industry": "制造业"}', false, 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (230, 'DX_NORTHEAST', '鼎新集团-东北分公司', 'BRANCH', '负责东北区域业务运营（待激活）', 'https://logo.example.com/dx_ne.png', 200, 'DINGXIN', '鼎新集团', '/200/230', '曹经理', '13800002007', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 0, "region": "东北", "industry": "制造业"}', false, 'PENDING', '新设立，尚未正式运营', 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (300, 'XINGCHEN', '星辰科技有限公司', 'COMPANY', '专注互联网科技研发的创新企业', 'https://logo.example.com/xc.png', 0, 'ROOT', '根节点', '/300', '程总', '13800003001', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 500, "region": "全国", "industry": "互联网"}', true, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (310, 'XC_RD', '星辰科技-研发中心', 'DEPARTMENT', '核心产品研发与技术攻关', 'https://logo.example.com/xc_rd.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/310', '严经理', '13800003002', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 200, "region": "杭州", "industry": "互联网"}', false, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (320, 'XC_MARKET', '星辰科技-营销中心', 'DEPARTMENT', '市场推广与销售管理', 'https://logo.example.com/xc_market.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/320', '蔡总监', '13800003003', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 100, "region": "上海", "industry": "互联网"}', false, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPARTMENT', '海外市场拓展与国际合作', 'https://logo.example.com/xc_overseas.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/330', '韩经理', '13800003004', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 80, "region": "海外", "industry": "互联网"}', false, 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (400, 'HAINA', '海纳百川集团', 'ENTERPRISE', '多元化投资控股集团', 'https://logo.example.com/hn.png', 0, 'ROOT', '根节点', '/400', '海总', '13800004001', '2033-03-31 23:59:59.000000', 4, '集团定制版', '{"scale": 2000, "region": "全国", "industry": "投资控股"}', true, 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (410, 'HN_EDU', '海纳百川-教育科技', 'SUBSIDIARY', '在线教育平台与教育科技产品', 'https://logo.example.com/hn_edu.png', 400, 'HAINA', '海纳百川集团', '/400/410', '楚总', '13800005001', '2033-03-31 23:59:59.000000', 5, '教育行业版', '{"scale": 400, "region": "全国", "industry": "教育"}', false, 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (420, 'HN_MEDICAL', '海纳百川-医疗健康', 'SUBSIDIARY', '智慧医疗与大健康服务', 'https://logo.example.com/hn_med.png', 400, 'HAINA', '海纳百川集团', '/400/420', '乔总', '13800006001', '2033-03-31 23:59:59.000000', 5, '医疗行业版', '{"scale": 350, "region": "全国", "industry": "医疗健康"}', false, 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (500, 'TIANXIANG', '天翔物流', 'COMPANY', '全国性物流配送企业（已停用）', 'https://logo.example.com/tx.png', 0, 'ROOT', '根节点', '/500', '田总', '13800007001', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 600, "region": "全国", "industry": "物流"}', true, 'DISABLED', '经营异常，暂停服务', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (510, 'TX_WAREHOUSE', '天翔物流-仓储部', 'DEPARTMENT', '全国仓储管理中心', 'https://logo.example.com/tx_wh.png', 500, 'TIANXIANG', '天翔物流', '/500/510', '田主管', '13800007002', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 200, "region": "全国", "industry": "物流"}', false, 'DISABLED', '随母公司停用', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (520, 'TX_TRANSPORT', '天翔物流-运输部', 'DEPARTMENT', '全国干线运输调度中心（待激活）', 'https://logo.example.com/tx_tp.png', 500, 'TIANXIANG', '天翔物流', '/500/520', '田调度', '13800007003', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 150, "region": "全国", "industry": "物流"}', false, 'PENDING', '待母公司恢复运营', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_role_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    role_id        bigint       not null,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_role_policy is '角色策略表';

comment on column sys_role_policy.id is '主键ID';

comment on column sys_role_policy.policy_code is '策略编码';

comment on column sys_role_policy.policy_name is '策略名称';

comment on column sys_role_policy.target_id is '授权目标ID（用户策略ID）';

comment on column sys_role_policy.target_type is '授权目标类型';

comment on column sys_role_policy.role_id is '关联角色ID';

comment on column sys_role_policy.status is '策略状态';

comment on column sys_role_policy.disable_reason is '禁用原因';

comment on column sys_role_policy.create_tenant is '创建时所属租户ID';

comment on column sys_role_policy.create_dept is '创建时所属部门ID';

comment on column sys_role_policy.create_role is '创建时使用角色ID';

comment on column sys_role_policy.create_by is '创建人ID';

comment on column sys_role_policy.create_at is '创建时间';

comment on column sys_role_policy.update_by is '更新人ID';

comment on column sys_role_policy.update_at is '更新时间';

comment on column sys_role_policy.is_deleted is '逻辑删除标记';

comment on column sys_role_policy.deleted_at is '删除时间';

INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2001, 'RP_001', 'admin→超级管理员', 1001, 'USER', 3001, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2002, 'RP_002', 'zhang_zong→租户管理员', 1002, 'USER', 3002, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2003, 'RP_003', 'li_jingli→分公司管理员', 1003, 'USER', 3009, 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2004, 'RP_004', 'wang_zhuguan→办事处主管', 1004, 'USER', 3010, 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2005, 'RP_005', 'zhang_san→普通员工', 1005, 'USER', 3011, 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2006, 'RP_006', 'li_si→普通员工', 1006, 'USER', 3011, 'ACTIVE', null, 121, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2007, 'RP_007', 'wang_wu→部门管理员(华南)', 1007, 'USER', 3003, 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2008, 'RP_008', 'wang_wu→普通员工(深圳)', 1008, 'USER', 3011, 'ACTIVE', null, 121, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2009, 'RP_009', 'zhao_liu→销售专员', 1009, 'USER', 3012, 'ACTIVE', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2010, 'RP_010', 'sun_qi→普通员工', 1010, 'USER', 3011, 'ACTIVE', null, 112, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2011, 'RP_011', 'zhou_ba→财务专员', 1011, 'USER', 3007, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2012, 'RP_012', 'wu_jiu→分公司管理员(华北)', 1012, 'USER', 3013, 'ACTIVE', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2013, 'RP_013', 'zheng_shi→研发工程师', 1013, 'USER', 3022, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2014, 'RP_014', 'qian_yi→人事专员', 1014, 'USER', 3008, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2015, 'RP_015', 'chen_er→普通员工', 1015, 'USER', 3011, 'ACTIVE', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2016, 'RP_016', 'feng_san→租户管理员', 1016, 'USER', 3002, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2017, 'RP_017', 'chu_si→部门管理员', 1017, 'USER', 3028, 'ACTIVE', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2018, 'RP_018', 'huang_audit→审计员', 1018, 'USER', 3005, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2019, 'RP_019', 'xu_readonly→只读用户', 1019, 'USER', 3006, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2020, 'RP_020', 'dingxin_ceo→租户管理员', 1020, 'USER', 3015, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2021, 'RP_021', 'dong_manager→办事处管理员', 1021, 'USER', 3019, 'ACTIVE', null, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2022, 'RP_022', 'guo_staff→普通员工', 1022, 'USER', 3017, 'ACTIVE', null, 221, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2023, 'RP_023', 'xingchen_cto→租户管理员', 1023, 'USER', 3021, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2024, 'RP_024', 'yan_dev→研发工程师', 1024, 'USER', 3024, 'ACTIVE', null, 310, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2025, 'RP_025', 'cai_market→营销总监', 1025, 'USER', 3023, 'ACTIVE', null, 320, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2026, 'RP_026', 'han_overseas→海外经理', 1026, 'USER', 3026, 'ACTIVE', null, 330, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2027, 'RP_027', 'hai_manager→租户管理员', 1027, 'USER', 3027, 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2028, 'RP_028', 'multi_user→普通员工(万象)', 1028, 'USER', 3004, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2029, 'RP_029', 'multi_user→普通员工(鼎新)', 1029, 'USER', 3017, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2030, 'RP_030', 'multi_user→研发工程师(星辰)', 1030, 'USER', 3022, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2031, 'RP_031', 'multi_user→审计员(鼎新-失效)', 1029, 'USER', 3018, 'DISABLED', '角色策略已失效', 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2032, 'RP_032', 'multi_user→营销专员(星辰)', 1030, 'USER', 3025, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_role
(
    id            bigint       not null
        primary key,
    role_name     varchar(100) not null,
    role_desc     varchar(200) not null,
    role_code     varchar(100) not null,
    role_level    varchar(20)  not null,
    tenant_id     bigint       not null,
    tenant_code   varchar(50)  not null,
    tenant_name   varchar(100) not null,
    data_scope    varchar(20)  not null,
    sort_order    integer      not null,
    create_tenant bigint       not null,
    create_dept   bigint       not null,
    create_role   bigint       not null,
    create_by     bigint       not null,
    create_at     timestamp(6) default CURRENT_TIMESTAMP,
    update_by     bigint       not null,
    update_at     timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted    varchar(20)  not null,
    deleted_at    timestamp(6)
);

comment on table sys_role is '角色表';

comment on column sys_role.id is '主键ID';

comment on column sys_role.role_name is '角色名称';

comment on column sys_role.role_desc is '角色描述';

comment on column sys_role.role_code is '角色编码';

comment on column sys_role.role_level is '角色层级';

comment on column sys_role.tenant_id is '所属租户ID';

comment on column sys_role.tenant_code is '租户编码';

comment on column sys_role.tenant_name is '租户名称';

comment on column sys_role.data_scope is '数据权限范围';

comment on column sys_role.sort_order is '排序序号';

comment on column sys_role.create_tenant is '创建时所属租户ID';

comment on column sys_role.create_dept is '创建时所属部门ID';

comment on column sys_role.create_role is '创建时使用角色ID';

comment on column sys_role.create_by is '创建人ID';

comment on column sys_role.create_at is '创建时间';

comment on column sys_role.update_by is '更新人ID';

comment on column sys_role.update_at is '更新时间';

comment on column sys_role.is_deleted is '逻辑删除标记';

comment on column sys_role.deleted_at is '删除时间';

INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3001, '超级管理员', '系统最高权限管理员', 'SUPER_ADMIN', 'SYSTEM', 100, 'WANXIANG', '万象集团', 'ALL', 1, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3002, '租户管理员', '万象集团租户级管理员', 'TENANT_ADMIN', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 2, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3003, '部门管理员', '万象集团部门级管理员', 'DEPT_MANAGER', 'DEPT', 100, 'WANXIANG', '万象集团', 'DEPT_AND_SUB', 3, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3004, '普通员工', '万象集团普通员工', 'EMPLOYEE', 'USER', 100, 'WANXIANG', '万象集团', 'SELF', 4, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3005, '审计员', '万象集团审计专员', 'AUDITOR', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 5, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3006, '只读用户', '万象集团只读权限用户', 'READONLY', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 6, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3007, '财务专员', '万象集团财务人员', 'FINANCE', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 7, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3008, '人事专员', '万象集团人力资源专员', 'HR', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 8, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3009, '分公司管理员', '华东分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 110, 'WX_EAST', '万象集团-华东分公司', 'DEPT_AND_SUB', 1, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3010, '办事处主管', '上海办事处主管', 'OFFICE_MANAGER', 'OFFICE', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'DEPT', 1, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3011, '普通员工', '上海办事处普通员工', 'STAFF', 'USER', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'SELF', 2, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3012, '销售人员', '华南分公司销售专员', 'SALES', 'USER', 120, 'WX_SOUTH', '万象集团-华南分公司', 'SELF', 1, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3013, '分公司管理员', '华北分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 130, 'WX_NORTH', '万象集团-华北分公司', 'DEPT_AND_SUB', 1, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3014, '普通员工', '华北分公司普通员工', 'STAFF', 'USER', 130, 'WX_NORTH', '万象集团-华北分公司', 'SELF', 2, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3015, '租户管理员', '鼎新集团管理员', 'TENANT_ADMIN', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 1, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3016, '部门管理员', '鼎新集团部门管理员', 'DEPT_MANAGER', 'DEPT', 200, 'DINGXIN', '鼎新集团', 'DEPT_AND_SUB', 2, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3017, '普通员工', '鼎新集团普通员工', 'EMPLOYEE', 'USER', 200, 'DINGXIN', '鼎新集团', 'SELF', 3, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3018, '审计员', '鼎新集团审计专员', 'AUDITOR', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 4, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3019, '办事处管理员', '武汉办事处管理员', 'OFFICE_MANAGER', 'OFFICE', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'DEPT', 1, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3020, '普通员工', '武汉办事处普通员工', 'STAFF', 'USER', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'SELF', 2, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3021, '租户管理员', '星辰科技管理员', 'TENANT_ADMIN', 'TENANT', 300, 'XINGCHEN', '星辰科技有限公司', 'ALL', 1, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3022, '研发工程师', '星辰科技研发人员', 'DEV_ENGINEER', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 2, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3023, '营销总监', '星辰科技营销负责人', 'MARKET_DIRECTOR', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 3, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3024, '研发工程师', '星辰科技研发二部研发人员', 'DEV_ENGINEER_2', 'DEPT', 310, 'XC_RD', '星辰科技-研发中心', 'DEPT', 2, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3025, '营销专员', '星辰科技营销专员', 'MARKET_STAFF', 'USER', 320, 'XC_MARKET', '星辰科技-营销中心', 'SELF', 1, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3026, '海外经理', '星辰科技海外事业部经理', 'OVERSEAS_MANAGER', 'DEPT', 330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPT', 1, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3027, '租户管理员', '海纳百川管理员', 'TENANT_ADMIN', 'TENANT', 400, 'HAINA', '海纳百川集团', 'ALL', 1, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3028, '部门管理员', '海纳百川部门管理员', 'DEPT_MANAGER', 'DEPT', 410, 'HN_EDU', '海纳百川-教育科技', 'DEPT_AND_SUB', 1, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3029, '租户管理员', '天翔物流管理员', 'TENANT_ADMIN', 'TENANT', 500, 'TIANXIANG', '天翔物流', 'ALL', 1, 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3030, '普通员工', '天翔物流普通员工', 'EMPLOYEE', 'USER', 500, 'TIANXIANG', '天翔物流', 'SELF', 2, 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_perm_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    perm_id        bigint       not null,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_perm_policy is '权限策略表';

comment on column sys_perm_policy.id is '主键ID';

comment on column sys_perm_policy.policy_code is '策略编码';

comment on column sys_perm_policy.policy_name is '策略名称';

comment on column sys_perm_policy.target_id is '授权目标ID';

comment on column sys_perm_policy.target_type is '授权目标类型';

comment on column sys_perm_policy.perm_id is '权限ID';

comment on column sys_perm_policy.status is '策略状态';

comment on column sys_perm_policy.disable_reason is '禁用原因';

comment on column sys_perm_policy.create_tenant is '创建时所属租户ID';

comment on column sys_perm_policy.create_dept is '创建时所属部门ID';

comment on column sys_perm_policy.create_role is '创建时使用角色ID';

comment on column sys_perm_policy.create_by is '创建人ID';

comment on column sys_perm_policy.create_at is '创建时间';

comment on column sys_perm_policy.update_by is '更新人ID';

comment on column sys_perm_policy.update_at is '更新时间';

comment on column sys_perm_policy.is_deleted is '逻辑删除标记';

comment on column sys_perm_policy.deleted_at is '删除时间';

alter table sys_perm_policy owner to postgres;

INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5001, 'PP_001', '万象集团→系统管理', 100, 'TENANT', 4001, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5002, 'PP_002', '万象集团→租户管理', 100, 'TENANT', 4002, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5003, 'PP_003', '万象集团→用户管理', 100, 'TENANT', 4003, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5004, 'PP_004', '鼎新集团→系统管理', 200, 'TENANT', 4001, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5005, 'PP_005', '鼎新集团→生产管理', 200, 'TENANT', 4017, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5006, 'PP_006', '星辰科技→系统管理', 300, 'TENANT', 4001, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5007, 'PP_007', '星辰科技→研发管理', 300, 'TENANT', 4022, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5008, 'PP_008', '海纳百川→系统管理', 400, 'TENANT', 4001, 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5009, 'PP_009', '天翔物流→物流管理', 500, 'TENANT', 4021, 'DISABLED', '租户已停用', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5010, 'PP_010', '天翔物流→仓储管理', 500, 'TENANT', 4020, 'DISABLED', '租户已停用', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5011, 'PP_011', '超级管理员→权限管理', 3001, 'ROLE', 4005, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5012, 'PP_012', '超级管理员→系统配置', 3001, 'ROLE', 4030, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5013, 'PP_013', '租户管理员→用户管理', 3002, 'ROLE', 4003, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5014, 'PP_014', '租户管理员→角色管理', 3002, 'ROLE', 4004, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5015, 'PP_015', '部门管理员→部门管理', 3003, 'ROLE', 4006, 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5016, 'PP_016', '普通员工→数据查看', 3004, 'ROLE', 4010, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5017, 'PP_017', '审计员→审计管理', 3005, 'ROLE', 4009, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5018, 'PP_018', '只读用户→数据查看', 3006, 'ROLE', 4010, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5019, 'PP_019', '财务专员→财务管理', 3007, 'ROLE', 4007, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5020, 'PP_020', '人事专员→人事管理', 3008, 'ROLE', 4008, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5021, 'PP_021', '分公司管理员→部门管理', 3009, 'ROLE', 4006, 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5022, 'PP_022', '办事处主管→数据查看', 3010, 'ROLE', 4010, 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5023, 'PP_023', '普通员工(上海)→数据查看', 3011, 'ROLE', 4010, 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5024, 'PP_024', '销售人员→销售管理', 3012, 'ROLE', 4027, 'ACTIVE', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5025, 'PP_025', '分公司管理员(华北)→部门管理', 3013, 'ROLE', 4006, 'ACTIVE', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5026, 'PP_026', '租户管理员(鼎新)→用户管理', 3015, 'ROLE', 4003, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5027, 'PP_027', '部门管理员(鼎新)→部门管理', 3016, 'ROLE', 4006, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5028, 'PP_028', '普通员工(鼎新)→数据查看', 3017, 'ROLE', 4010, 'ACTIVE', null, 221, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5029, 'PP_029', '审计员(鼎新)→审计管理', 3018, 'ROLE', 4009, 'DISABLED', '角色策略已失效', 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5030, 'PP_030', '办事处管理员(武汉)→数据查看', 3019, 'ROLE', 4010, 'ACTIVE', null, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5031, 'PP_031', '租户管理员(星辰)→用户管理', 3021, 'ROLE', 4003, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5032, 'PP_032', '研发工程师→研发管理', 3022, 'ROLE', 4022, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5033, 'PP_033', '营销总监→市场营销', 3023, 'ROLE', 4026, 'ACTIVE', null, 320, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5034, 'PP_034', '研发工程师(二部)→代码仓库', 3024, 'ROLE', 4023, 'ACTIVE', null, 310, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5035, 'PP_035', '租户管理员(海纳百川)→用户管理', 3027, 'ROLE', 4003, 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5036, 'PP_036', 'admin→系统配置', 1, 'USER', 4030, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5037, 'PP_037', 'zhang_zong→数据导出', 2, 'USER', 4013, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5038, 'PP_038', 'wang_wu→客户管理', 7, 'USER', 4018, 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5039, 'PP_039', 'dingxin_ceo→采购管理', 19, 'USER', 4028, 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5040, 'PP_040', 'xingchen_cto→部署管理', 22, 'USER', 4025, 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5041, 'PP_041', 'hai_manager→数据导出', 26, 'USER', 4013, 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5042, 'PP_042', 'multi_user→测试管理', 30, 'USER', 4024, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5043, 'PP_043', 'huang_audit→报表查看', 17, 'USER', 4014, 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5044, 'PP_044', 'li_jingli→报表编辑', 3, 'USER', 4015, 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5045, 'PP_045', 'dong_manager→质量管理', 20, 'USER', 4029, 'ACTIVE', null, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_perm
(
    id            bigint       not null
        primary key,
    perm_name     varchar(100) not null,
    perm_desc     varchar(200) not null,
    perm_code     varchar(100) not null,
    perm_type     varchar(20)  not null,
    parent_id     bigint       not null,
    parent_code   varchar(50)  not null,
    parent_name   varchar(100) not null,
    path          varchar(1000) not null,
    sort_order    integer      not null,
    status        varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant bigint       not null,
    create_dept   bigint       not null,
    create_role   bigint       not null,
    create_by     bigint       not null,
    create_at     timestamp(6) default CURRENT_TIMESTAMP,
    update_by     bigint       not null,
    update_at     timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted    varchar(20)  not null,
    deleted_at    timestamp(6)
);

comment on table sys_perm is '权限表';

comment on column sys_perm.id is '主键ID';

comment on column sys_perm.perm_name is '权限名称';

comment on column sys_perm.perm_desc is '权限描述';

comment on column sys_perm.perm_code is '权限编码';

comment on column sys_perm.perm_type is '权限类型';

comment on column sys_perm.parent_id is '父权限ID';

comment on column sys_perm.parent_code is '父权限编码';

comment on column sys_perm.parent_name is '父权限名称';

comment on column sys_perm.path is '权限层级路径';

comment on column sys_perm.sort_order is '排序序号';

comment on column sys_perm.status is '权限状态';

comment on column sys_perm.disable_reason is '禁用原因';

comment on column sys_perm.create_tenant is '创建时所属租户ID';

comment on column sys_perm.create_dept is '创建时所属部门ID';

comment on column sys_perm.create_role is '创建时使用角色ID';

comment on column sys_perm.create_by is '创建人ID';

comment on column sys_perm.create_at is '创建时间';

comment on column sys_perm.update_by is '更新人ID';

comment on column sys_perm.update_at is '更新时间';

comment on column sys_perm.is_deleted is '逻辑删除标记';

comment on column sys_perm.deleted_at is '删除时间';

alter table sys_perm owner to postgres;

INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4001, '系统管理', '系统基础管理权限', 'SYSTEM_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4001', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4002, '租户管理', '租户信息管理权限', 'TENANT_MANAGE', 'MENU', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4002', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4003, '用户管理', '用户信息管理权限', 'USER_MANAGE', 'MENU', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4003', 2, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4004, '角色管理', '角色信息管理权限', 'ROLE_MANAGE', 'MENU', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4004', 3, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4005, '权限管理', '权限信息管理权限', 'PERM_MANAGE', 'MENU', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4005', 4, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4006, '部门管理', '部门信息管理权限', 'DEPT_MANAGE', 'MENU', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4006', 5, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4007, '财务管理', '财务数据管理权限', 'FINANCE_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4007', 2, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4008, '人事管理', '人力资源管理权限', 'HR_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4008', 3, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4009, '审计管理', '审计监督权限', 'AUDIT_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4009', 4, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4010, '数据查看', '基础数据查看权限', 'DATA_VIEW', 'BUTTON', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4010', 6, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4011, '数据编辑', '基础数据编辑权限', 'DATA_EDIT', 'BUTTON', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4011', 7, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4012, '数据删除', '基础数据删除权限', 'DATA_DELETE', 'BUTTON', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4012', 8, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4013, '数据导出', '基础数据导出权限', 'DATA_EXPORT', 'BUTTON', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4013', 9, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4014, '报表查看', '报表数据查看权限', 'REPORT_VIEW', 'BUTTON', 4007, 'FINANCE_MANAGE', '财务管理', '/4007/4014', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4015, '报表编辑', '报表数据编辑权限', 'REPORT_EDIT', 'BUTTON', 4007, 'FINANCE_MANAGE', '财务管理', '/4007/4015', 2, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4016, '订单管理', '订单业务管理权限', 'ORDER_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4016', 5, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4017, '产品管理', '产品信息管理权限', 'PRODUCT_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4017', 6, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4018, '客户管理', '客户信息管理权限', 'CUSTOMER_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4018', 7, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4019, '供应商管理', '供应商信息管理权限', 'SUPPLIER_MANAGE', 'MENU', 4017, 'PRODUCT_MANAGE', '产品管理', '/4017/4019', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4020, '仓储管理', '仓储信息管理权限', 'WAREHOUSE_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4020', 8, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4021, '物流管理', '物流配送管理权限', 'LOGISTICS_MANAGE', 'MENU', 4020, 'WAREHOUSE_MANAGE', '仓储管理', '/4020/4021', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4022, '研发管理', '研发项目管理权限', 'DEV_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4022', 9, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4023, '代码仓库', '代码仓库访问权限', 'CODE_REPO', 'BUTTON', 4022, 'DEV_MANAGE', '研发管理', '/4022/4023', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4024, '测试管理', '测试用例管理权限', 'TEST_MANAGE', 'BUTTON', 4022, 'DEV_MANAGE', '研发管理', '/4022/4024', 2, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4025, '部署管理', '部署发布管理权限', 'DEPLOY_MANAGE', 'BUTTON', 4022, 'DEV_MANAGE', '研发管理', '/4022/4025', 3, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4026, '市场营销', '市场推广管理权限', 'MARKET_MANAGE', 'MENU', 0, 'ROOT', '根节点', '/4026', 10, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4027, '销售管理', '销售业务管理权限', 'SALES_MANAGE', 'MENU', 4026, 'MARKET_MANAGE', '市场营销', '/4026/4027', 1, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4028, '采购管理', '采购业务管理权限', 'PROCUREMENT_MANAGE', 'MENU', 4017, 'PRODUCT_MANAGE', '产品管理', '/4017/4028', 2, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4029, '质量管理', '质量检验管理权限', 'QUALITY_MANAGE', 'MENU', 4017, 'PRODUCT_MANAGE', '产品管理', '/4017/4029', 3, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_type, parent_id, parent_code, parent_name, path, sort_order, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4030, '系统配置', '系统参数配置权限', 'SYSTEM_CONFIG', 'BUTTON', 4001, 'SYSTEM_MANAGE', '系统管理', '/4001/4030', 10, 'ENABLED', null, 0, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_user_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    user_id        bigint       not null,
    tenant_id      bigint       not null,
    tenant_code    varchar(50)  not null,
    tenant_name    varchar(100) not null,
    is_primary     boolean      not null default false,
    join_time      timestamp(6) default CURRENT_TIMESTAMP,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint       not null,
    create_dept    bigint       not null,
    create_role    bigint       not null,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_user_policy is '用户策略表';

comment on column sys_user_policy.id is '主键ID';

comment on column sys_user_policy.policy_code is '策略编码';

comment on column sys_user_policy.policy_name is '策略名称';

comment on column sys_user_policy.target_id is '授权目标ID（租户ID）';

comment on column sys_user_policy.target_type is '授权目标类型';

comment on column sys_user_policy.user_id is '用户ID';

comment on column sys_user_policy.tenant_id is '所属租户ID';

comment on column sys_user_policy.tenant_code is '租户编码';

comment on column sys_user_policy.tenant_name is '租户名称';

comment on column sys_user_policy.is_primary is '是否主租户';

comment on column sys_user_policy.join_time is '加入租户时间';

comment on column sys_user_policy.status is '策略状态';

comment on column sys_user_policy.disable_reason is '禁用原因';

comment on column sys_user_policy.create_tenant is '创建时所属租户ID';

comment on column sys_user_policy.create_dept is '创建时所属部门ID';

comment on column sys_user_policy.create_role is '创建时使用角色ID';

comment on column sys_user_policy.create_by is '创建人ID';

comment on column sys_user_policy.create_at is '创建时间';

comment on column sys_user_policy.update_by is '更新人ID';

comment on column sys_user_policy.update_at is '更新时间';

comment on column sys_user_policy.is_deleted is '逻辑删除标记';

comment on column sys_user_policy.deleted_at is '删除时间';

alter table sys_user_policy owner to postgres;

INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1001, 'UP_001', 'admin→万象集团(主)', 100, 'TENANT', 1, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1002, 'UP_002', 'zhang_zong→万象集团(主)', 100, 'TENANT', 2, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1003, 'UP_003', 'li_jingli→华东分公司(主)', 110, 'TENANT', 3, 110, 'WX_EAST', '万象集团-华东分公司', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1004, 'UP_004', 'wang_zhuguan→上海办事处(主)', 111, 'TENANT', 4, 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1005, 'UP_005', 'zhang_san→上海办事处(主)', 111, 'TENANT', 5, 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1006, 'UP_006', 'li_si→深圳办事处(主)', 121, 'TENANT', 6, 121, 'WX_SOUTH_SZ', '万象集团-华南-深圳办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 121, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1007, 'UP_007', 'wang_wu→华南分公司(主)', 120, 'TENANT', 7, 120, 'WX_SOUTH', '万象集团-华南分公司', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1008, 'UP_008', 'wang_wu→深圳办事处(兼)', 121, 'TENANT', 7, 121, 'WX_SOUTH_SZ', '万象集团-华南-深圳办事处', false, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 121, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1009, 'UP_009', 'zhao_liu→广州办事处(主)', 122, 'TENANT', 8, 122, 'WX_SOUTH_GZ', '万象集团-华南-广州办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1010, 'UP_010', 'sun_qi→杭州办事处(主)', 112, 'TENANT', 9, 112, 'WX_EAST_HZ', '万象集团-华东-杭州办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 112, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1011, 'UP_011', 'zhou_ba→万象集团(主)', 100, 'TENANT', 10, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1012, 'UP_012', 'wu_jiu→华北分公司(主)', 130, 'TENANT', 11, 130, 'WX_NORTH', '万象集团-华北分公司', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1013, 'UP_013', 'zheng_shi→星辰科技(主)', 300, 'TENANT', 12, 300, 'XINGCHEN', '星辰科技有限公司', true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1014, 'UP_014', 'qian_yi→万象集团(主)', 100, 'TENANT', 13, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1015, 'UP_015', 'chen_er→广州办事处(主)', 122, 'TENANT', 14, 122, 'WX_SOUTH_GZ', '万象集团-华南-广州办事处', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1016, 'UP_016', 'feng_san→万象集团(主)', 100, 'TENANT', 15, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1017, 'UP_017', 'chu_si→教育科技(主)', 410, 'TENANT', 16, 410, 'HN_EDU', '海纳百川-教育科技', true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1018, 'UP_018', 'huang_audit→万象集团(主)', 100, 'TENANT', 17, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1019, 'UP_019', 'xu_readonly→万象集团(主)', 100, 'TENANT', 18, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1020, 'UP_020', 'dingxin_ceo→鼎新集团(主)', 200, 'TENANT', 19, 200, 'DINGXIN', '鼎新集团', true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1021, 'UP_021', 'dong_manager→武汉办事处(主)', 211, 'TENANT', 20, 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1022, 'UP_022', 'guo_staff→石家庄办事处(主)', 221, 'TENANT', 21, 221, 'DX_NORTH_SJZ', '鼎新集团-华北-石家庄办事处', true, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 221, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1023, 'UP_023', 'xingchen_cto→星辰科技(主)', 300, 'TENANT', 22, 300, 'XINGCHEN', '星辰科技有限公司', true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1024, 'UP_024', 'yan_dev→研发中心(主)', 310, 'TENANT', 23, 310, 'XC_RD', '星辰科技-研发中心', true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 310, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1025, 'UP_025', 'cai_market→营销中心(主)', 320, 'TENANT', 24, 320, 'XC_MARKET', '星辰科技-营销中心', true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 320, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1026, 'UP_026', 'han_overseas→海外事业部(主)', 330, 'TENANT', 25, 330, 'XC_OVERSEAS', '星辰科技-海外事业部', true, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 330, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1027, 'UP_027', 'hai_manager→海纳百川(主)', 400, 'TENANT', 26, 400, 'HAINA', '海纳百川集团', true, '2026-04-01 08:00:00.000000', 'ACTIVE', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1028, 'UP_028', 'multi_user→万象集团(主)', 100, 'TENANT', 30, 100, 'WANXIANG', '万象集团', true, '2026-01-01 08:00:00.000000', 'ACTIVE', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1029, 'UP_029', 'multi_user→鼎新集团(兼)', 200, 'TENANT', 30, 200, 'DINGXIN', '鼎新集团', false, '2026-02-01 08:00:00.000000', 'ACTIVE', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, tenant_id, tenant_code, tenant_name, is_primary, join_time, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1030, 'UP_030', 'multi_user→星辰科技(兼)', 300, 'TENANT', 30, 300, 'XINGCHEN', '星辰科技有限公司', false, '2026-03-01 08:00:00.000000', 'ACTIVE', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_user
(
    id             bigint        not null
        primary key,
    user_code      varchar(50)   not null,
    user_name      varchar(50)   not null,
    nick_name      varchar(50)   not null,
    email          varchar(100)  not null,
    phone          varchar(20)   not null,
    password       varchar(200)  not null,
    avatar_url     varchar(500)  not null,
    gender         varchar(10)   not null,
    birthday       date,
    status         varchar(20)   not null,
    disable_reason varchar(200) default NULL::character varying,
    create_tenant  bigint        not null,
    create_dept    bigint        not null,
    create_role    bigint        not null,
    create_by      bigint        not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint        not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)   not null,
    deleted_at     timestamp(6)
);

comment on table sys_user is '用户表';

comment on column sys_user.id is '主键ID';

comment on column sys_user.user_code is '用户编码';

comment on column sys_user.user_name is '用户名';

comment on column sys_user.nick_name is '昵称';

comment on column sys_user.email is '邮箱';

comment on column sys_user.phone is '手机号';

comment on column sys_user.password is '密码';

comment on column sys_user.avatar_url is '头像URL';

comment on column sys_user.gender is '性别';

comment on column sys_user.birthday is '生日';

comment on column sys_user.status is '用户状态';

comment on column sys_user.disable_reason is '禁用原因';

comment on column sys_user.create_tenant is '创建时所属租户ID';

comment on column sys_user.create_dept is '创建时所属部门ID';

comment on column sys_user.create_role is '创建时使用角色ID';

comment on column sys_user.create_by is '创建人ID';

comment on column sys_user.create_at is '创建时间';

comment on column sys_user.update_by is '更新人ID';

comment on column sys_user.update_at is '更新时间';

comment on column sys_user.is_deleted is '逻辑删除标记';

comment on column sys_user.deleted_at is '删除时间';

alter table sys_user owner to postgres;

INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1, 'U_ADMIN', 'admin', '系统管理员', 'admin@wanxiang.com', '13800000001', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/admin.png', 'MALE', '1990-01-01', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2, 'U_ZHANG_ZONG', 'zhang_zong', '张总', 'zhangzong@wanxiang.com', '13800000002', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/zhang_zong.png', 'MALE', '1985-03-15', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3, 'U_LI_JINGLI', 'li_jingli', '李经理', 'lijingli@wanxiang.com', '13800000003', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/li_jingli.png', 'MALE', '1988-06-20', 'ENABLED', null, 110, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4, 'U_WANG_ZHUGUAN', 'wang_zhuguan', '王主管', 'wangzhuguan@wanxiang.com', '13800000004', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/wang_zhuguan.png', 'FEMALE', '1992-09-10', 'ENABLED', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5, 'U_ZHANG_SAN', 'zhang_san', '张三', 'zhangsan@wanxiang.com', '13800000005', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/zhang_san.png', 'MALE', '1995-12-01', 'ENABLED', null, 111, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6, 'U_LI_SI', 'li_si', '李四', 'lisi@wanxiang.com', '13800000006', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/li_si.png', 'MALE', '1993-04-18', 'ENABLED', null, 121, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7, 'U_WANG_WU', 'wang_wu', '王五', 'wangwu@wanxiang.com', '13800000007', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/wang_wu.png', 'MALE', '1991-07-25', 'ENABLED', null, 120, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (8, 'U_ZHAO_LIU', 'zhao_liu', '赵六', 'zhaoliu@wanxiang.com', '13800000008', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/zhao_liu.png', 'FEMALE', '1994-11-30', 'ENABLED', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9, 'U_SUN_QI', 'sun_qi', '孙七', 'sunqi@wanxiang.com', '13800000009', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/sun_qi.png', 'MALE', '1996-02-14', 'ENABLED', null, 112, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (10, 'U_ZHOU_BA', 'zhou_ba', '周八', 'zhouba@wanxiang.com', '13800000010', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/zhou_ba.png', 'MALE', '1989-08-08', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (11, 'U_WU_JIU', 'wu_jiu', '吴九', 'wujiu@wanxiang.com', '13800000011', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/wu_jiu.png', 'MALE', '1987-05-22', 'ENABLED', null, 130, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (12, 'U_ZHENG_SHI', 'zheng_shi', '郑十', 'zhengshi@xingchen.com', '13800000012', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/zheng_shi.png', 'FEMALE', '1997-01-15', 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (13, 'U_QIAN_YI', 'qian_yi', '钱一', 'qianyi@wanxiang.com', '13800000013', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/qian_yi.png', 'FEMALE', '1990-10-05', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (14, 'U_CHEN_ER', 'chen_er', '陈二', 'chener@wanxiang.com', '13800000014', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/chen_er.png', 'MALE', '1994-03-28', 'ENABLED', null, 122, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (15, 'U_FENG_SAN', 'feng_san', '冯三', 'fengsan@wanxiang.com', '13800000015', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/feng_san.png', 'MALE', '1986-12-12', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (16, 'U_CHU_SI', 'chu_si', '楚四', 'chusi@haina.com', '13800000016', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/chu_si.png', 'FEMALE', '1992-06-30', 'ENABLED', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (17, 'U_HUANG_AUDIT', 'huang_audit', '黄审计', 'huangaudit@wanxiang.com', '13800000017', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/huang_audit.png', 'MALE', '1988-09-18', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (18, 'U_XU_READONLY', 'xu_readonly', '徐只读', 'xureadonly@wanxiang.com', '13800000018', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/xu_readonly.png', 'MALE', '1993-11-22', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (19, 'U_DINGXIN_CEO', 'dingxin_ceo', '丁总', 'dingxinceo@dingxin.com', '13800000019', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/dingxin_ceo.png', 'MALE', '1982-04-08', 'ENABLED', null, 200, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (20, 'U_DONG_MANAGER', 'dong_manager', '董主管', 'dongmanager@dingxin.com', '13800000020', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/dong_manager.png', 'MALE', '1990-07-14', 'ENABLED', null, 211, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (21, 'U_GUO_STAFF', 'guo_staff', '郭员工', 'guostaff@dingxin.com', '13800000021', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/guo_staff.png', 'FEMALE', '1996-03-05', 'ENABLED', null, 221, 0, 0, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (22, 'U_XINGCHEN_CTO', 'xingchen_cto', '程总', 'xingchencto@xingchen.com', '13800000022', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/xingchen_cto.png', 'MALE', '1984-01-20', 'ENABLED', null, 300, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (23, 'U_YAN_DEV', 'yan_dev', '严经理', 'yandev@xingchen.com', '13800000023', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/yan_dev.png', 'MALE', '1991-08-16', 'ENABLED', null, 310, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (24, 'U_CAI_MARKET', 'cai_market', '蔡总监', 'caimarket@xingchen.com', '13800000024', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/cai_market.png', 'FEMALE', '1989-05-12', 'ENABLED', null, 320, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (25, 'U_HAN_OVERSEAS', 'han_overseas', '韩经理', 'hanoverseas@xingchen.com', '13800000025', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/han_overseas.png', 'MALE', '1987-02-28', 'ENABLED', null, 330, 0, 0, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (26, 'U_HAI_MANAGER', 'hai_manager', '海总', 'haimanager@haina.com', '13800000026', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/hai_manager.png', 'MALE', '1983-10-10', 'ENABLED', null, 400, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (27, 'U_LU_EDU', 'lu_edu', '陆老师', 'luedu@haina.com', '13800000027', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/lu_edu.png', 'FEMALE', '1992-04-25', 'ENABLED', null, 410, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (28, 'U_QIAO_MED', 'qiao_med', '乔医生', 'qiaomed@haina.com', '13800000028', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/qiao_med.png', 'MALE', '1988-12-08', 'ENABLED', null, 420, 0, 0, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (29, 'U_TIAN_LOGISTICS', 'tian_logistics', '田调度', 'tianlogistics@tianxiang.com', '13800000029', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/tian_logistics.png', 'MALE', '1990-06-15', 'DISABLED', '所属租户已停用', 500, 0, 0, 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, nick_name, email, phone, password, avatar_url, gender, birthday, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (30, 'U_MULTI_USER', 'multi_user', '多租户用户', 'multiuser@wanxiang.com', '13800000030', '$2a$10$encrypted_password_hash', 'https://avatar.example.com/multi_user.png', 'MALE', '1993-09-03', 'ENABLED', null, 100, 0, 0, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);