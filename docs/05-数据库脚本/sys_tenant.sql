create table sys_tenant
(
    id              bigint        not null
        primary key,
    tenant_code     varchar(50)   not null,
    tenant_name     varchar(100)  not null,
    tenant_type     varchar(50)   not null,
    tenant_address  varchar(500)  not null,
    tenant_desc     varchar(500)  not null,
    tenant_scale    varchar(50)   not null,
    tenant_logo_url varchar(500) default NULL::character varying,
    invite_code     varchar(50)   not null,
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
comment on column sys_tenant.tenant_address is '租户地址';
comment on column sys_tenant.tenant_desc is '租户描述';
comment on column sys_tenant.tenant_scale is '租户企业规模';
comment on column sys_tenant.tenant_logo_url is '租户LOGO路径';
comment on column sys_tenant.invite_code is '租户邀请码';
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

INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (101, 'WANXIANG', '万象集团', 'ENTERPRISE', '浙江省杭州市上城区万象大厦18层', '综合性企业集团', '大型集团', 'https://logo.example.com/wanxiang.png', 'WX-20250001', 0, '/101', 1, true, '张总', '13800000001', 'zhangzong@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "全国", "industry": "综合"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (102, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '广东省深圳市南山区鼎新工业园1号', '专注制造业的集团企业', '大型集团', 'https://logo.example.com/dingxin.png', 'DX-20250002', 0, '/102', 1, true, '丁总', '13800000002', 'dingzong@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"region": "全国", "industry": "制造业"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (103, 'HAINA', '海纳集团', 'ENTERPRISE', '上海市浦东新区陆家嘴环路1000号海纳金融中心35层', '多元化投资集团', '大型集团', 'https://logo.example.com/haina.png', 'HN-20250003', 0, '/103', 1, false, '海总', '13800000003', 'haizong@haina.com', '2031-12-31 23:59:59.000000', 1, '{"region": "全国", "industry": "投资"}', 'ENABLED', null, 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-01-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (111, 'WX_EAST', '万象-华东分公司', 'BRANCH', '上海市浦东新区陆家嘴环路1000号15层', '负责华东区域业务', '中型企业', 'https://logo.example.com/wx_east.png', 'WX-HD-20250011', 101, '/101/111', 2, true, '李经理', '13800000011', 'lijingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华东"}', 'ENABLED', null, 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (112, 'WX_SOUTH', '万象-华南分公司', 'BRANCH', '广东省广州市天河区珠江新城冼村路5号', '负责华南区域业务', '中型企业', 'https://logo.example.com/wx_south.png', 'WX-HN-20250012', 101, '/101/112', 2, false, '陈经理', '13800000012', 'chenjingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华南"}', 'ENABLED', null, 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (113, 'WX_NORTH', '万象-华北分公司', 'BRANCH', '北京市朝阳区建国门外大街甲6号', '负责华北区域业务', '中型企业', 'https://logo.example.com/wx_north.png', 'WX-HB-20250013', 101, '/101/113', 2, false, '武经理', '13800000013', 'wujingli@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"region": "华北"}', 'DISABLED', '业务调整暂停', 101, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-06-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (121, 'WX_EAST_SH', '万象-华东-上海办事处', 'OFFICE', '上海市黄浦区南京东路123号', '上海地区业务中心', '小型企业', 'https://logo.example.com/wx_sh.png', 'WX-HD-SH-20250021', 111, '/101/111/121', 3, false, '王主管', '13800000021', 'wangzhuguan@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"city": "上海"}', 'ENABLED', null, 111, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (122, 'WX_EAST_NJ', '万象-华东-南京办事处', 'OFFICE', '江苏省南京市玄武区新街口洪武路88号', '南京地区业务中心', '小型企业', 'https://logo.example.com/wx_nj.png', 'WX-HD-NJ-20250022', 111, '/101/111/122', 3, false, '赵主管', '13800000022', 'zhaozhuguan@wanxiang.com', '2030-12-31 23:59:59.000000', 1, '{"city": "南京"}', 'ENABLED', null, 111, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (201, 'DX_CENTRAL', '鼎新-华中分公司', 'BRANCH', '湖北省武汉市江汉区解放大道128号', '负责华中区域制造业务', '中型企业', 'https://logo.example.com/dx_central.png', 'DX-HZ-202500201', 102, '/102/201', 2, false, '马经理', '13800000201', 'majingli@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"region": "华中"}', 'ENABLED', null, 102, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (211, 'DX_CENTRAL_WH', '鼎新-华中-武汉工厂', 'OFFICE', '湖北省武汉市东湖高新区光谷大道77号', '武汉生产制造基地', '小型企业', 'https://logo.example.com/dx_wh.png', 'DX-HZ-WH-202500211', 201, '/102/201/211', 3, false, '董主管', '13800000211', 'dongzhuguan@dingxin.com', '2032-06-30 23:59:59.000000', 2, '{"city": "武汉"}', 'ENABLED', null, 201, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7001, 'TENANT_A', '租户A', 'ENTERPRISE', '北京市海淀区中关村大街1号', '测试租户A', '小型企业', null, 'TEST-A-2026001', 0, '/7001', 1, false, '联系人A', '13870010001', 'tenantA@test.com', '2030-12-31 00:00:00.000000', 1, '{}', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.564919', 1, '2026-06-15 08:19:09.564919', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7002, 'TENANT_B', '租户B', 'ENTERPRISE', '杭州市西湖区文三路90号', '测试租户B', '小型企业', null, 'TEST-B-2026002', 0, '/7002', 1, false, '联系人B', '13870020002', 'tenantB@test.com', '2030-12-31 00:00:00.000000', 1, '{}', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.564919', 1, '2026-06-15 08:19:09.564919', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_address, tenant_desc, tenant_scale, tenant_logo_url, invite_code, parent_id, path, level, has_children, contact_name, contact_phone, contact_email, expire_time, package_id, ext_attributes, status, disable_reason, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7003, 'TENANT_C', '租户C', 'ENTERPRISE', '广州市天河区天河路385号', '测试租户C', '小型企业', null, 'TEST-C-2026003', 0, '/7003', 1, false, '联系人C', '13870030003', 'tenantC@test.com', '2030-12-31 00:00:00.000000', 1, '{}', 'ENABLED', null, 0, 0, 0, 1, '2026-06-15 08:19:09.564919', 1, '2026-06-15 08:19:09.564919', 'NOT_DELETED', null);