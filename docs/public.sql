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

comment on column sys_tenant.create_by is '创建人ID';

comment on column sys_tenant.create_at is '创建时间';

comment on column sys_tenant.update_by is '更新人ID';

comment on column sys_tenant.update_at is '更新时间';

comment on column sys_tenant.is_deleted is '逻辑删除标记';

comment on column sys_tenant.deleted_at is '删除时间';

INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (100, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团，覆盖华东、华南、华北、西南四大区域', 'https://logo.example.com/wx.png', 0, 'ROOT', '根节点', '/100', '张总', '13800001001', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 5000, "region": "全国", "industry": "综合"}', true, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (110, 'WX_EAST', '万象集团-华东分公司', 'BRANCH', '负责华东区域业务运营', 'https://logo.example.com/wx_east.png', 100, 'WANXIANG', '万象集团', '/100/110', '李经理', '13800001002', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 1200, "region": "华东", "industry": "综合"}', true, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'OFFICE', '上海地区业务运营中心', 'https://logo.example.com/wx_sh.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/111', '王主管', '13800001003', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 300, "region": "上海", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (112, 'WX_EAST_HZ', '万象集团-华东-杭州办事处', 'OFFICE', '杭州地区业务运营中心', 'https://logo.example.com/wx_hz.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/112', '孙主管', '13800001004', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 200, "region": "杭州", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (113, 'WX_EAST_NJ', '万象集团-华东-南京办事处', 'OFFICE', '南京地区业务运营中心（已停用）', 'https://logo.example.com/wx_nj.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/113', '赵主管', '13800001005', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 50, "region": "南京", "industry": "综合"}', false, 'DISABLED', '业务调整，合并至上海办事处', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (120, 'WX_SOUTH', '万象集团-华南分公司', 'BRANCH', '负责华南区域业务运营', 'https://logo.example.com/wx_south.png', 100, 'WANXIANG', '万象集团', '/100/120', '陈经理', '13800001006', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 1000, "region": "华南", "industry": "综合"}', true, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (121, 'WX_SOUTH_SZ', '万象集团-华南-深圳办事处', 'OFFICE', '深圳地区业务运营中心', 'https://logo.example.com/wx_sz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/121', '周主管', '13800001007', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 350, "region": "深圳", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (122, 'WX_SOUTH_GZ', '万象集团-华南-广州办事处', 'OFFICE', '广州地区业务运营中心', 'https://logo.example.com/wx_gz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/122', '林主管', '13800001008', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 280, "region": "广州", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (123, 'WX_SOUTH_XM', '万象集团-华南-厦门办事处', 'OFFICE', '厦门地区业务运营中心', 'https://logo.example.com/wx_xm.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/123', '吴主管', '13800001009', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 150, "region": "厦门", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (130, 'WX_NORTH', '万象集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/wx_north.png', 100, 'WANXIANG', '万象集团', '/100/130', '武经理', '13800001010', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 800, "region": "华北", "industry": "综合"}', true, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (131, 'WX_NORTH_BJ', '万象集团-华北-北京办事处', 'OFFICE', '北京地区业务运营中心', 'https://logo.example.com/wx_bj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/131', '郑主管', '13800001011', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 400, "region": "北京", "industry": "综合"}', false, 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (132, 'WX_NORTH_TJ', '万象集团-华北-天津办事处', 'OFFICE', '天津地区业务运营中心（已过期）', 'https://logo.example.com/wx_tj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/132', '冯主管', '13800001012', '2025-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 100, "region": "天津", "industry": "综合"}', false, 'EXPIRED', '租约未续费已过期', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (140, 'WX_SOUTHWEST', '万象集团-西南分公司', 'BRANCH', '负责西南区域业务运营（已停用）', 'https://logo.example.com/wx_sw.png', 100, 'WANXIANG', '万象集团', '/100/140', '何经理', '13800001013', '2030-12-31 23:59:59.000000', 1, '企业旗舰版', '{"scale": 300, "region": "西南", "industry": "综合"}', false, 'DISABLED', '战略调整暂停运营', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (200, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dx.png', 0, 'ROOT', '根节点', '/200', '丁总', '13800002001', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 3000, "region": "全国", "industry": "制造业"}', true, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (210, 'DX_CENTRAL', '鼎新集团-华中分公司', 'BRANCH', '负责华中区域业务运营', 'https://logo.example.com/dx_central.png', 200, 'DINGXIN', '鼎新集团', '/200/210', '马经理', '13800002002', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 800, "region": "华中", "industry": "制造业"}', true, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'OFFICE', '武汉地区业务运营中心', 'https://logo.example.com/dx_wh.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/211', '董主管', '13800002003', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 300, "region": "武汉", "industry": "制造业"}', false, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (212, 'DX_CENTRAL_CS', '鼎新集团-华中-长沙办事处', 'OFFICE', '长沙地区业务运营中心', 'https://logo.example.com/dx_cs.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/212', '谢主管', '13800002004', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 200, "region": "长沙", "industry": "制造业"}', false, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (220, 'DX_NORTH', '鼎新集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/dx_north.png', 200, 'DINGXIN', '鼎新集团', '/200/220', '韩经理', '13800002005', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 600, "region": "华北", "industry": "制造业"}', true, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (221, 'DX_NORTH_SJZ', '鼎新集团-华北-石家庄办事处', 'OFFICE', '石家庄地区业务运营中心', 'https://logo.example.com/dx_sjz.png', 220, 'DX_NORTH', '鼎新集团-华北分公司', '/200/220/221', '郭员工', '13800002006', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 150, "region": "石家庄", "industry": "制造业"}', false, 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (230, 'DX_NORTHEAST', '鼎新集团-东北分公司', 'BRANCH', '负责东北区域业务运营（待激活）', 'https://logo.example.com/dx_ne.png', 200, 'DINGXIN', '鼎新集团', '/200/230', '曹经理', '13800002007', '2032-06-30 23:59:59.000000', 2, '制造业专版', '{"scale": 0, "region": "东北", "industry": "制造业"}', false, 'PENDING', '新设立，尚未正式运营', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (300, 'XINGCHEN', '星辰科技有限公司', 'COMPANY', '专注互联网科技研发的创新企业', 'https://logo.example.com/xc.png', 0, 'ROOT', '根节点', '/300', '程总', '13800003001', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 500, "region": "全国", "industry": "互联网"}', true, 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (310, 'XC_RD', '星辰科技-研发中心', 'DEPARTMENT', '核心产品研发与技术攻关', 'https://logo.example.com/xc_rd.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/310', '严经理', '13800003002', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 200, "region": "杭州", "industry": "互联网"}', false, 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (320, 'XC_MARKET', '星辰科技-营销中心', 'DEPARTMENT', '市场推广与销售管理', 'https://logo.example.com/xc_market.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/320', '蔡总监', '13800003003', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 100, "region": "上海", "industry": "互联网"}', false, 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPARTMENT', '海外市场拓展与国际合作', 'https://logo.example.com/xc_overseas.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/330', '韩经理', '13800003004', '2031-09-30 23:59:59.000000', 3, '科技初创版', '{"scale": 80, "region": "海外", "industry": "互联网"}', false, 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (400, 'HAINA', '海纳百川集团', 'ENTERPRISE', '多元化投资控股集团', 'https://logo.example.com/hn.png', 0, 'ROOT', '根节点', '/400', '海总', '13800004001', '2033-03-31 23:59:59.000000', 4, '集团定制版', '{"scale": 2000, "region": "全国", "industry": "投资控股"}', true, 'ENABLED', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (410, 'HN_EDU', '海纳百川-教育科技', 'SUBSIDIARY', '在线教育平台与教育科技产品', 'https://logo.example.com/hn_edu.png', 400, 'HAINA', '海纳百川集团', '/400/410', '楚总', '13800005001', '2033-03-31 23:59:59.000000', 5, '教育行业版', '{"scale": 400, "region": "全国", "industry": "教育"}', false, 'ENABLED', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (420, 'HN_MEDICAL', '海纳百川-医疗健康', 'SUBSIDIARY', '智慧医疗与大健康服务', 'https://logo.example.com/hn_med.png', 400, 'HAINA', '海纳百川集团', '/400/420', '乔总', '13800006001', '2033-03-31 23:59:59.000000', 5, '医疗行业版', '{"scale": 350, "region": "全国", "industry": "医疗健康"}', false, 'ENABLED', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (500, 'TIANXIANG', '天翔物流', 'COMPANY', '全国性物流配送企业（已停用）', 'https://logo.example.com/tx.png', 0, 'ROOT', '根节点', '/500', '田总', '13800007001', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 600, "region": "全国", "industry": "物流"}', true, 'DISABLED', '经营异常，暂停服务', 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (510, 'TX_WAREHOUSE', '天翔物流-仓储部', 'DEPARTMENT', '全国仓储管理中心', 'https://logo.example.com/tx_wh.png', 500, 'TIANXIANG', '天翔物流', '/500/510', '田主管', '13800007002', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 200, "region": "全国", "industry": "物流"}', false, 'DISABLED', '随母公司停用', 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (520, 'TX_TRANSPORT', '天翔物流-运输部', 'DEPARTMENT', '全国干线运输调度中心（待激活）', 'https://logo.example.com/tx_tp.png', 500, 'TIANXIANG', '天翔物流', '/500/520', '田调度', '13800007003', '2026-03-31 23:59:59.000000', 6, '物流基础版', '{"scale": 150, "region": "全国", "industry": "物流"}', false, 'PENDING', '待母公司恢复运营', 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

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

comment on column sys_role_policy.create_by is '创建人ID';

comment on column sys_role_policy.create_at is '创建时间';

comment on column sys_role_policy.update_by is '更新人ID';

comment on column sys_role_policy.update_at is '更新时间';

comment on column sys_role_policy.is_deleted is '逻辑删除标记';

comment on column sys_role_policy.deleted_at is '删除时间';

INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2001, 'RP_001', 'admin→超级管理员', 1001, 'USER', 3001, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2002, 'RP_002', 'zhang_zong→租户管理员', 1002, 'USER', 3002, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2003, 'RP_003', 'li_jingli→分公司管理员', 1003, 'USER', 3009, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2004, 'RP_004', 'wang_zhuguan→办事处主管', 1004, 'USER', 3010, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2005, 'RP_005', 'zhang_san→普通员工', 1005, 'USER', 3011, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2006, 'RP_006', 'li_si→普通员工', 1006, 'USER', 3011, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2007, 'RP_007', 'wang_wu→部门管理员(华南)', 1007, 'USER', 3003, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2008, 'RP_008', 'wang_wu→普通员工(深圳)', 1008, 'USER', 3011, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2009, 'RP_009', 'zhao_liu→销售专员', 1009, 'USER', 3012, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2010, 'RP_010', 'sun_qi→普通员工', 1010, 'USER', 3011, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2011, 'RP_011', 'zhou_ba→财务专员', 1011, 'USER', 3007, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2012, 'RP_012', 'wu_jiu→分公司管理员(华北)', 1012, 'USER', 3013, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2013, 'RP_013', 'zheng_shi→研发工程师', 1013, 'USER', 3022, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2014, 'RP_014', 'qian_yi→人事专员', 1014, 'USER', 3008, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2015, 'RP_015', 'chen_er→普通员工', 1015, 'USER', 3011, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2016, 'RP_016', 'feng_san→租户管理员', 1016, 'USER', 3002, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2017, 'RP_017', 'chu_si→部门管理员', 1017, 'USER', 3028, 'ACTIVE', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2018, 'RP_018', 'huang_audit→审计员', 1018, 'USER', 3005, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2019, 'RP_019', 'xu_readonly→只读用户', 1019, 'USER', 3006, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2020, 'RP_020', 'dingxin_ceo→租户管理员', 1020, 'USER', 3015, 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2021, 'RP_021', 'dong_manager→办事处管理员', 1021, 'USER', 3019, 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2022, 'RP_022', 'guo_staff→普通员工', 1022, 'USER', 3017, 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2023, 'RP_023', 'xingchen_cto→租户管理员', 1023, 'USER', 3021, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2024, 'RP_024', 'yan_dev→研发工程师', 1024, 'USER', 3024, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2025, 'RP_025', 'cai_market→营销总监', 1025, 'USER', 3023, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2026, 'RP_026', 'han_overseas→海外经理', 1026, 'USER', 3026, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2027, 'RP_027', 'hai_manager→租户管理员', 1027, 'USER', 3027, 'ACTIVE', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2028, 'RP_028', 'multi_user→普通员工(万象)', 1028, 'USER', 3004, 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2029, 'RP_029', 'multi_user→普通员工(鼎新)', 1029, 'USER', 3017, 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2030, 'RP_030', 'multi_user→研发工程师(星辰)', 1030, 'USER', 3022, 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_role
(
    id          bigint       not null
        primary key,
    role_name   varchar(100) not null,
    role_desc   varchar(200) not null,
    role_code   varchar(100) not null,
    role_level  varchar(20)  not null,
    tenant_id   bigint       not null,
    tenant_code varchar(50)  not null,
    tenant_name varchar(100) not null,
    data_scope  varchar(20)  not null,
    sort_order  integer      not null,
    create_by   bigint       not null,
    create_at   timestamp(6) default CURRENT_TIMESTAMP,
    update_by   bigint       not null,
    update_at   timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted  varchar(20)  not null,
    deleted_at  timestamp(6)
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

comment on column sys_role.create_by is '创建人ID';

comment on column sys_role.create_at is '创建时间';

comment on column sys_role.update_by is '更新人ID';

comment on column sys_role.update_at is '更新时间';

comment on column sys_role.is_deleted is '逻辑删除标记';

comment on column sys_role.deleted_at is '删除时间';

INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3001, '超级管理员', '系统最高权限管理员', 'SUPER_ADMIN', 'SYSTEM', 100, 'WANXIANG', '万象集团', 'ALL', 1, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3002, '租户管理员', '万象集团租户级管理员', 'TENANT_ADMIN', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 2, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3003, '部门管理员', '万象集团部门级管理员', 'DEPT_MANAGER', 'DEPT', 100, 'WANXIANG', '万象集团', 'DEPT_AND_SUB', 3, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3004, '普通员工', '万象集团普通员工', 'EMPLOYEE', 'USER', 100, 'WANXIANG', '万象集团', 'SELF', 4, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3005, '审计员', '万象集团审计专员', 'AUDITOR', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 5, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3006, '只读用户', '万象集团只读权限用户', 'READONLY', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 6, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3007, '财务专员', '万象集团财务人员', 'FINANCE', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 7, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3008, '人事专员', '万象集团人力资源专员', 'HR', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 8, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3009, '分公司管理员', '华东分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 110, 'WX_EAST', '万象集团-华东分公司', 'DEPT_AND_SUB', 1, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3010, '办事处主管', '上海办事处主管', 'OFFICE_MANAGER', 'OFFICE', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'DEPT', 1, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3011, '普通员工', '上海办事处普通员工', 'STAFF', 'USER', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'SELF', 2, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3012, '销售人员', '华南分公司销售专员', 'SALES', 'USER', 120, 'WX_SOUTH', '万象集团-华南分公司', 'SELF', 1, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3013, '分公司管理员', '华北分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 130, 'WX_NORTH', '万象集团-华北分公司', 'DEPT_AND_SUB', 1, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3014, '普通员工', '华北分公司普通员工', 'STAFF', 'USER', 130, 'WX_NORTH', '万象集团-华北分公司', 'SELF', 2, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3015, '租户管理员', '鼎新集团管理员', 'TENANT_ADMIN', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 1, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3016, '部门管理员', '鼎新集团部门管理员', 'DEPT_MANAGER', 'DEPT', 200, 'DINGXIN', '鼎新集团', 'DEPT_AND_SUB', 2, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3017, '普通员工', '鼎新集团普通员工', 'EMPLOYEE', 'USER', 200, 'DINGXIN', '鼎新集团', 'SELF', 3, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3018, '审计员', '鼎新集团审计专员', 'AUDITOR', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 4, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3019, '办事处管理员', '武汉办事处管理员', 'OFFICE_MANAGER', 'OFFICE', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'DEPT', 1, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3020, '普通员工', '武汉办事处普通员工', 'STAFF', 'USER', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'SELF', 2, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3021, '租户管理员', '星辰科技管理员', 'TENANT_ADMIN', 'TENANT', 300, 'XINGCHEN', '星辰科技有限公司', 'ALL', 1, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3022, '研发工程师', '星辰科技研发人员', 'DEV_ENGINEER', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 2, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3023, '营销总监', '星辰科技营销负责人', 'MARKET_DIRECTOR', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 3, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3024, '研发工程师', '研发中心开发工程师', 'DEV_ENGINEER', 'USER', 310, 'XC_RD', '星辰科技-研发中心', 'SELF', 1, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3025, '营销专员', '营销中心市场专员', 'MARKET_SPECIALIST', 'USER', 320, 'XC_MARKET', '星辰科技-营销中心', 'SELF', 1, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3026, '海外经理', '海外事业部负责人', 'OVERSEAS_MANAGER', 'DEPT', 330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPT', 1, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3027, '租户管理员', '海纳百川集团管理员', 'TENANT_ADMIN', 'TENANT', 400, 'HAINA', '海纳百川集团', 'ALL', 1, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3028, '部门管理员', '教育科技部门管理员', 'DEPT_MANAGER', 'DEPT', 410, 'HN_EDU', '海纳百川-教育科技', 'DEPT_AND_SUB', 1, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3029, '普通员工', '教育科技普通员工', 'STAFF', 'USER', 410, 'HN_EDU', '海纳百川-教育科技', 'SELF', 2, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3030, '普通员工', '医疗健康普通员工', 'STAFF', 'USER', 420, 'HN_MEDICAL', '海纳百川-医疗健康', 'SELF', 1, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_perm_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    perm_id        bigint       not null,
    table_name     varchar(64)  not null,
    table_desc     varchar(100) not null,
    access_type    varchar(20)  not null,
    field_operates jsonb        default '[]'::jsonb,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
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

comment on column sys_perm_policy.target_id is '授权目标ID（角色策略ID、用户策略ID、系统租户ID）';

comment on column sys_perm_policy.target_type is '授权目标类型';

comment on column sys_perm_policy.perm_id is '关联权限ID';

comment on column sys_perm_policy.table_name is '控制的数据表名';

comment on column sys_perm_policy.table_desc is '数据表描述';

comment on column sys_perm_policy.access_type is '访问类型';

comment on column sys_perm_policy.field_operates is '允许操作的字段列表';

comment on column sys_perm_policy.status is '策略状态';

comment on column sys_perm_policy.disable_reason is '禁用原因';

comment on column sys_perm_policy.create_by is '创建人ID';

comment on column sys_perm_policy.create_at is '创建时间';

comment on column sys_perm_policy.update_by is '更新人ID';

comment on column sys_perm_policy.update_at is '更新时间';

comment on column sys_perm_policy.is_deleted is '逻辑删除标记';

comment on column sys_perm_policy.deleted_at is '删除时间';

INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5001, 'PP_TENANT_001', '万象集团-租户查看能力', 100, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code", "tenant_name", "tenant_type", "tenant_desc", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5002, 'PP_TENANT_002', '万象集团-用户查看能力', 100, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "nick_name", "email", "phone"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5003, 'PP_TENANT_003', '万象集团-报表查看能力', 100, 'TENANT', 9018, 'sys_report', '报表表', 'QUERY', '["report_name", "report_type", "create_at"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5004, 'PP_TENANT_004', '万象集团-数据查询能力', 100, 'TENANT', 9020, 'sys_data', '数据表', 'QUERY', '["data_name", "data_type", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5005, 'PP_TENANT_005', '鼎新集团-租户查看能力', 200, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code", "tenant_name", "status"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5006, 'PP_TENANT_006', '鼎新集团-用户查看能力', 200, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "email"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5007, 'PP_TENANT_007', '星辰科技-租户查看能力', 300, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code", "tenant_name", "status"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5008, 'PP_TENANT_008', '星辰科技-用户查看能力', 300, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "nick_name"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5009, 'PP_TENANT_009', '海纳百川-租户查看能力', 400, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code", "tenant_name", "status"]', 'ACTIVE', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5010, 'PP_ROLE_001', '超级管理员-租户创建权限', 2001, 'ROLE', 9004, 'sys_tenant', '租户表', 'CREATE', '["tenant_code", "tenant_name", "tenant_type", "parent_id", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5011, 'PP_ROLE_002', '超级管理员-用户创建权限', 2001, 'ROLE', 9009, 'sys_user', '用户表', 'CREATE', '["user_code", "user_name", "nick_name", "email", "phone"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5012, 'PP_ROLE_003', '超级管理员-系统配置权限', 2001, 'ROLE', 9023, 'sys_config', '系统配置表', 'UPDATE', '["config_key", "config_value", "description"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5013, 'PP_ROLE_004', '超级管理员-系统监控权限', 2001, 'ROLE', 9022, 'sys_monitor', '系统监控表', 'QUERY', '["cpu_usage", "mem_usage", "disk_usage", "online_users"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5014, 'PP_ROLE_005', '租户管理员-租户编辑权限', 2002, 'ROLE', 9005, 'sys_tenant', '租户表', 'UPDATE', '["tenant_name", "tenant_desc", "contact_name", "contact_phone", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5015, 'PP_ROLE_006', '租户管理员-用户管理权限', 2002, 'ROLE', 9007, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "nick_name", "email", "phone", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5016, 'PP_ROLE_007', '租户管理员-角色管理权限', 2002, 'ROLE', 9011, 'sys_role', '角色表', 'QUERY', '["role_name", "role_code", "role_level", "data_scope"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5017, 'PP_ROLE_008', '租户管理员-数据查询权限', 2016, 'ROLE', 9020, 'sys_data', '数据表', 'QUERY', '["data_name", "data_type", "status", "create_at"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5018, 'PP_ROLE_009', '分公司管理员-用户管理权限', 2003, 'ROLE', 9007, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "email", "phone"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5019, 'PP_ROLE_010', '部门管理员-数据修改权限', 2007, 'ROLE', 9021, 'sys_data', '数据表', 'UPDATE', '["data_name", "data_type", "status"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5020, 'PP_ROLE_011', '审计员-审计查看权限', 2018, 'ROLE', 9028, 'sys_audit_log', '审计日志表', 'QUERY', '["operator", "operation", "target", "result", "create_at"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5021, 'PP_ROLE_012', '审计员-审计审批权限', 2018, 'ROLE', 9029, 'sys_audit_log', '审计日志表', 'UPDATE', '["approve_status", "approve_comment"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5022, 'PP_ROLE_013', '营销总监-报表导出权限', 2025, 'ROLE', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name", "report_type", "report_data"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5023, 'PP_ROLE_014', '星辰管理员-权限分配权限', 2023, 'ROLE', 9016, 'sys_perm_policy', '权限策略表', 'CREATE', '["policy_code", "policy_name", "target_type", "perm_id", "access_type", "field_operates"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5024, 'PP_ROLE_015', '鼎新管理员-用户创建权限', 2020, 'ROLE', 9009, 'sys_user', '用户表', 'CREATE', '["user_code", "user_name", "nick_name", "email", "phone"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5025, 'PP_USER_001', 'zhang_zong-财务查看额外权限', 1002, 'USER', 9025, 'sys_finance', '财务表', 'QUERY', '["amount", "date", "type", "department"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5026, 'PP_USER_002', 'zhou_ba-财务操作额外权限', 1011, 'USER', 9026, 'sys_finance', '财务表', 'UPDATE', '["amount", "status", "remark"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5027, 'PP_USER_003', 'multi_user-万象-报表导出权限', 1028, 'USER', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name", "report_type"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5028, 'PP_DISABLED_001', '万象集团-南京办-租户查看(已禁用)', 113, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code", "tenant_name", "status"]', 'DISABLED', '南京办事处已停用，上级策略级联禁用', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5029, 'PP_DISABLED_002', '万象集团-西南分公司-用户查看(已禁用)', 140, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name"]', 'DISABLED', '西南分公司战略调整暂停运营，级联禁用', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5030, 'PP_EXPIRED_001', '万象集团-天津办-报表查看(已过期)', 132, 'TENANT', 9018, 'sys_report', '报表表', 'QUERY', '["report_name", "report_type", "create_at"]', 'DISABLED', '租约已过期，权限自动失效', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5031, 'PP_MULTI_R_001', 'multi_user-万象-数据查询(角色)', 2028, 'ROLE', 9020, 'sys_data', '数据表', 'QUERY', '["data_name", "data_type", "status", "create_at"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5032, 'PP_MULTI_R_002', 'multi_user-万象-用户查看(角色)', 2028, 'ROLE', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "nick_name", "email"]', 'ACTIVE', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5033, 'PP_MULTI_U_001', 'multi_user-万象-数据修改(用户-禁用)', 1028, 'USER', 9021, 'sys_data', '数据表', 'UPDATE', '["data_name", "data_type", "status"]', 'DISABLED', '个人数据修改权限已回收', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5034, 'PP_MULTI_R_003', 'multi_user-鼎新-数据查询(角色)', 2029, 'ROLE', 9020, 'sys_data', '数据表', 'QUERY', '["data_name", "data_type", "status", "create_at", "update_at"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5035, 'PP_MULTI_R_004', 'multi_user-鼎新-报表查看(角色)', 2029, 'ROLE', 9018, 'sys_report', '报表表', 'QUERY', '["report_name", "report_type", "create_at"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5036, 'PP_MULTI_R_DISABLED_001', 'multi_user-鼎新-审计查看(角色-失效)', 2031, 'ROLE', 9028, 'sys_audit_log', '审计日志表', 'QUERY', '["operator", "operation", "target", "result", "create_at"]', 'DISABLED', '角色已失效，级联禁用', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5037, 'PP_MULTI_R_DISABLED_002', 'multi_user-鼎新-审计审批(角色-失效)', 2031, 'ROLE', 9029, 'sys_audit_log', '审计日志表', 'UPDATE', '["approve_status", "approve_comment"]', 'DISABLED', '角色已失效，级联禁用', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5038, 'PP_MULTI_U_002', 'multi_user-鼎新-数据修改(用户)', 1029, 'USER', 9021, 'sys_data', '数据表', 'UPDATE', '["data_name", "data_type", "status", "remark"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5039, 'PP_MULTI_U_003', 'multi_user-鼎新-财务查看(用户)', 1029, 'USER', 9025, 'sys_finance', '财务表', 'QUERY', '["amount", "date", "type", "department", "status"]', 'ACTIVE', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5040, 'PP_MULTI_R_005', 'multi_user-星辰-数据修改(角色-研发)', 2030, 'ROLE', 9021, 'sys_data', '数据表', 'UPDATE', '["data_name", "data_type", "status", "version"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5041, 'PP_MULTI_R_006', 'multi_user-星辰-报表查看(角色-研发)', 2030, 'ROLE', 9018, 'sys_report', '报表表', 'QUERY', '["report_name", "report_type", "report_data", "create_at"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5042, 'PP_MULTI_R_007', 'multi_user-星辰-报表导出(角色-营销)', 2032, 'ROLE', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name", "report_type", "report_data", "create_at", "update_at"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5043, 'PP_MULTI_R_008', 'multi_user-星辰-用户查看(角色-营销)', 2032, 'ROLE', 9008, 'sys_user', '用户表', 'QUERY', '["user_code", "user_name", "nick_name", "email", "phone"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5044, 'PP_MULTI_U_DISABLED_001', 'multi_user-星辰-报表导出(用户-禁用)', 1030, 'USER', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name", "report_type"]', 'DISABLED', '个人导出权限被管理员收回', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5045, 'PP_MULTI_U_004', 'multi_user-星辰-系统监控(用户)', 1030, 'USER', 9022, 'sys_monitor', '系统监控表', 'QUERY', '["cpu_usage", "mem_usage", "disk_usage", "online_users"]', 'ACTIVE', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_perm
(
    id          bigint       not null
        primary key,
    perm_name   varchar(100) not null,
    perm_desc   varchar(200) not null,
    perm_code   varchar(100) not null,
    perm_key    varchar(100) not null,
    perm_type   varchar(20)  not null,
    parent_id   bigint       not null,
    parent_name varchar(100) not null,
    path        varchar(200) not null,
    create_by   bigint       not null,
    create_at   timestamp(6) default CURRENT_TIMESTAMP,
    update_by   bigint       not null,
    update_at   timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted  varchar(20)  not null,
    deleted_at  timestamp(6)
);

comment on table sys_perm is '权限表';

comment on column sys_perm.id is '主键ID';

comment on column sys_perm.perm_name is '权限名称，';

comment on column sys_perm.perm_desc is '权限描述';

comment on column sys_perm.perm_code is '权限编码';

comment on column sys_perm.perm_key is '权限标识';

comment on column sys_perm.perm_type is '权限类型';

comment on column sys_perm.parent_id is '父权限ID';

comment on column sys_perm.parent_name is '父权限名称';

comment on column sys_perm.path is '权限路径';

comment on column sys_perm.create_by is '创建人ID';

comment on column sys_perm.create_at is '创建时间';

comment on column sys_perm.update_by is '更新人ID';

comment on column sys_perm.update_at is '更新时间';

comment on column sys_perm.is_deleted is '逻辑删除标记';

comment on column sys_perm.deleted_at is '删除时间';

INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9001, '系统管理', '系统管理模块入口', 'system:admin', 'system_admin', 'MENU', 0, '根节点', '/9001', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9002, '租户管理', '租户信息管理', 'tenant:manage', 'tenant_manage', 'MENU', 9001, '系统管理', '/9001/9002', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9003, '租户查看', '查看租户信息', 'tenant:view', 'tenant_view', 'BUTTON', 9002, '租户管理', '/9001/9002/9003', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9004, '租户创建', '创建新租户', 'tenant:create', 'tenant_create', 'BUTTON', 9002, '租户管理', '/9001/9002/9004', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9005, '租户编辑', '编辑租户信息', 'tenant:update', 'tenant_update', 'BUTTON', 9002, '租户管理', '/9001/9002/9005', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9006, '租户删除', '删除租户', 'tenant:delete', 'tenant_delete', 'BUTTON', 9002, '租户管理', '/9001/9002/9006', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9007, '用户管理', '用户信息管理', 'user:manage', 'user_manage', 'MENU', 9001, '系统管理', '/9001/9007', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9008, '用户查看', '查看用户信息', 'user:view', 'user_view', 'BUTTON', 9007, '用户管理', '/9001/9007/9008', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9009, '用户创建', '创建新用户', 'user:create', 'user_create', 'BUTTON', 9007, '用户管理', '/9001/9007/9009', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9010, '用户编辑', '编辑用户信息', 'user:update', 'user_update', 'BUTTON', 9007, '用户管理', '/9001/9007/9010', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9011, '角色管理', '角色信息管理', 'role:manage', 'role_manage', 'MENU', 9001, '系统管理', '/9001/9011', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9012, '角色查看', '查看角色信息', 'role:view', 'role_view', 'BUTTON', 9011, '角色管理', '/9001/9011/9012', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9013, '角色分配', '为用户分配角色', 'role:assign', 'role_assign', 'BUTTON', 9011, '角色管理', '/9001/9011/9013', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9014, '权限管理', '权限策略管理', 'perm:manage', 'perm_manage', 'MENU', 9001, '系统管理', '/9001/9014', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9015, '权限查看', '查看权限配置', 'perm:view', 'perm_view', 'BUTTON', 9014, '权限管理', '/9001/9014/9015', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9016, '权限分配', '分配权限策略', 'perm:assign', 'perm_assign', 'BUTTON', 9014, '权限管理', '/9001/9014/9016', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9017, '业务管理', '业务管理模块入口', 'business:admin', 'business_admin', 'MENU', 0, '根节点', '/9017', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9018, '报表查看', '查看业务报表', 'report:view', 'report_view', 'MENU', 9017, '业务管理', '/9017/9018', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9019, '报表导出', '导出报表数据', 'report:export', 'report_export', 'BUTTON', 9018, '报表查看', '/9017/9018/9019', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9020, '数据查询', '数据查询功能', 'data:query', 'data_query', 'MENU', 9017, '业务管理', '/9017/9020', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9021, '数据修改', '修改业务数据', 'data:modify', 'data_modify', 'BUTTON', 9020, '数据查询', '/9017/9020/9021', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9022, '系统监控', '系统运行监控', 'system:monitor', 'system_monitor', 'MENU', 9017, '业务管理', '/9017/9022', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9023, '系统配置', '系统参数配置', 'system:config', 'system_config', 'BUTTON', 9022, '系统监控', '/9017/9022/9023', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9024, '财务管理', '财务管理模块入口', 'finance:admin', 'finance_admin', 'MENU', 0, '根节点', '/9024', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9025, '财务查看', '查看财务数据', 'finance:view', 'finance_view', 'BUTTON', 9024, '财务管理', '/9024/9025', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9026, '财务操作', '执行财务操作', 'finance:operate', 'finance_operate', 'BUTTON', 9024, '财务管理', '/9024/9026', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9027, '审计管理', '审计管理模块入口', 'audit:admin', 'audit_admin', 'MENU', 0, '根节点', '/9027', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9028, '审计查看', '查看审计日志', 'audit:view', 'audit_view', 'BUTTON', 9027, '审计管理', '/9027/9028', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9029, '审计审批', '审批审计事项', 'audit:approve', 'audit_approve', 'BUTTON', 9027, '审计管理', '/9027/9029', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9030, '订阅管理', '套餐订阅管理', 'subscription:manage', 'subscription_manage', 'MENU', 0, '根节点', '/9030', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_user_policy
(
    id             bigint       not null
        primary key,
    policy_code    varchar(100) not null,
    policy_name    varchar(100) not null,
    target_id      bigint       not null,
    target_type    varchar(20)  not null,
    user_id        bigint       not null,
    is_admin       boolean      not null,
    is_default     boolean      not null,
    join_time      timestamp(6) default CURRENT_TIMESTAMP,
    status         varchar(20)  not null,
    disable_reason varchar(200) default NULL::character varying,
    create_by      bigint       not null,
    create_at      timestamp(6) default CURRENT_TIMESTAMP,
    update_by      bigint       not null,
    update_at      timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted     varchar(20)  not null,
    deleted_at     timestamp(6)
);

comment on table sys_user_policy is '租户策略表 ';

comment on column sys_user_policy.id is '主键ID';

comment on column sys_user_policy.policy_code is '策略编码';

comment on column sys_user_policy.policy_name is '策略名称';

comment on column sys_user_policy.target_id is '授权目标ID（系统租户ID）';

comment on column sys_user_policy.target_type is '授权目标类型';

comment on column sys_user_policy.user_id is '用户ID';

comment on column sys_user_policy.is_admin is '是否为该租户管理员';

comment on column sys_user_policy.is_default is '是否默认租户';

comment on column sys_user_policy.join_time is '加入租户时间';

comment on column sys_user_policy.status is '策略状态';

comment on column sys_user_policy.disable_reason is '禁用原因';

comment on column sys_user_policy.create_by is '创建人ID';

comment on column sys_user_policy.create_at is '创建时间';

comment on column sys_user_policy.update_by is '更新人ID';

comment on column sys_user_policy.update_at is '更新时间';

comment on column sys_user_policy.is_deleted is '逻辑删除标记';

comment on column sys_user_policy.deleted_at is '删除时间';

INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1016, 'UP_016', 'feng_san-万象集团-默认策略', 100, 'TENANT', 15, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1012, 'UP_012', 'wu_jiu-华北分公司-默认策略', 130, 'TENANT', 11, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1025, 'UP_025', 'cai_market-营销中心-默认策略', 320, 'TENANT', 24, true, true, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1008, 'UP_008', 'wang_wu-深圳办事处-附加策略', 121, 'TENANT', 7, false, false, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1020, 'UP_020', 'dingxin_ceo-鼎新集团-默认策略', 200, 'TENANT', 19, true, true, '2026-02-01 08:00:00.000000', 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1030, 'UP_030', 'multi_user-星辰科技-附加策略', 300, 'TENANT', 30, false, false, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1029, 'UP_029', 'multi_user-鼎新集团-附加策略', 200, 'TENANT', 30, false, false, '2026-02-01 08:00:00.000000', 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1021, 'UP_021', 'dong_manager-武汉办事处-默认策略', 211, 'TENANT', 20, true, true, '2026-02-01 08:00:00.000000', 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1015, 'UP_015', 'chen_er-广州办事处-默认策略', 122, 'TENANT', 14, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1013, 'UP_013', 'zheng_shi-星辰科技-默认策略', 300, 'TENANT', 12, false, true, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1011, 'UP_011', 'zhou_ba-万象集团-默认策略', 100, 'TENANT', 10, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1001, 'UP_001', 'admin-万象集团-默认策略', 100, 'TENANT', 1, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1007, 'UP_007', 'wang_wu-华南分公司-默认策略', 120, 'TENANT', 7, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1005, 'UP_005', 'zhang_san-上海办事处-默认策略', 111, 'TENANT', 5, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1009, 'UP_009', 'zhao_liu-广州办事处-默认策略', 122, 'TENANT', 8, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1024, 'UP_024', 'yan_dev-研发中心-默认策略', 310, 'TENANT', 23, false, true, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1003, 'UP_003', 'li_jingli-华东分公司-默认策略', 110, 'TENANT', 3, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1002, 'UP_002', 'zhang_zong-万象集团-默认策略', 100, 'TENANT', 2, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1004, 'UP_004', 'wang_zhuguan-上海办事处-默认策略', 111, 'TENANT', 4, true, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1010, 'UP_010', 'sun_qi-杭州办事处-默认策略', 112, 'TENANT', 9, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1014, 'UP_014', 'qian_yi-万象集团-默认策略', 100, 'TENANT', 13, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1018, 'UP_018', 'huang_audit-万象集团-默认策略', 100, 'TENANT', 17, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1027, 'UP_027', 'hai_manager-海纳百川-默认策略', 400, 'TENANT', 26, true, true, '2026-04-01 08:00:00.000000', 'ENABLED', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1006, 'UP_006', 'li_si-深圳办事处-默认策略', 121, 'TENANT', 6, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1023, 'UP_023', 'xingchen_cto-星辰科技-默认策略', 300, 'TENANT', 22, true, true, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1022, 'UP_022', 'guo_staff-石家庄办事处-默认策略', 221, 'TENANT', 21, false, true, '2026-02-01 08:00:00.000000', 'ENABLED', null, 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1026, 'UP_026', 'han_overseas-海外事业部-默认策略', 330, 'TENANT', 25, true, true, '2026-03-01 08:00:00.000000', 'ENABLED', null, 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1017, 'UP_017', 'chu_si-教育科技-默认策略', 410, 'TENANT', 16, true, true, '2026-04-01 08:00:00.000000', 'ENABLED', null, 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1028, 'UP_028', 'multi_user-万象集团-默认策略', 100, 'TENANT', 30, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1019, 'UP_019', 'xu_readonly-万象集团-默认策略', 100, 'TENANT', 18, false, true, '2026-01-01 08:00:00.000000', 'ENABLED', null, 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);

create table sys_user
(
    id         bigint       not null
        primary key,
    user_code  varchar(50)  not null,
    user_name  varchar(50)  not null,
    password   varchar(100) not null,
    nick_name  varchar(50)  not null,
    email      varchar(100) not null,
    phone      varchar(20)  not null,
    avatar     varchar(500) not null,
    login_ip   varchar(45)  not null,
    login_date timestamp(6) not null,
    create_by  bigint       not null,
    create_at  timestamp(6) default CURRENT_TIMESTAMP,
    update_by  bigint       not null,
    update_at  timestamp(6) default CURRENT_TIMESTAMP,
    is_deleted varchar(20)  not null,
    deleted_at timestamp(6)
);

comment on table sys_user is '用户表';

comment on column sys_user.id is '主键ID';

comment on column sys_user.user_code is '用户编码';

comment on column sys_user.user_name is '登录用户名';

comment on column sys_user.password is '加密密码';

comment on column sys_user.nick_name is '用户昵称';

comment on column sys_user.email is '邮箱地址';

comment on column sys_user.phone is '手机号码';

comment on column sys_user.avatar is '头像地址';

comment on column sys_user.login_ip is '最后登录IP地址';

comment on column sys_user.login_date is '最后登录时间';

comment on column sys_user.create_by is '创建人ID';

comment on column sys_user.create_at is '创建时间';

comment on column sys_user.update_by is '更新人ID';

comment on column sys_user.update_at is '更新时间';

comment on column sys_user.is_deleted is '逻辑删除标记';

comment on column sys_user.deleted_at is '删除时间';

INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1, 'U001', 'admin', 'admin123', '超级管理员', 'admin@nexusix.com', '13800000001', 'https://avatar.example.com/admin.png', '192.168.1.1', '2026-06-10 08:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2, 'U002', 'zhang_zong', 'zhang123', '张总', 'zhangzong@wanxiang.com', '13800000002', 'https://avatar.example.com/zhangzong.png', '192.168.1.2', '2026-06-10 08:30:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3, 'U003', 'li_jingli', 'li123', '李经理', 'lijingli@wanxiang.com', '13800000003', 'https://avatar.example.com/lijingli.png', '192.168.1.3', '2026-06-10 09:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4, 'U004', 'wang_zhuguan', 'wang123', '王主管', 'wangzhuguan@wanxiang.com', '13800000004', 'https://avatar.example.com/wangzhuguan.png', '192.168.1.4', '2026-06-10 09:15:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5, 'U005', 'zhang_san', 'zhang123', '张三', 'zhangsan@wanxiang.com', '13800000005', 'https://avatar.example.com/zhangsan.png', '192.168.1.5', '2026-06-10 09:30:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6, 'U006', 'li_si', 'li123', '李四', 'lisi@wanxiang.com', '13800000006', 'https://avatar.example.com/lisi.png', '192.168.1.6', '2026-06-10 09:45:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7, 'U007', 'wang_wu', 'wang123', '王五', 'wangwu@wanxiang.com', '13800000007', 'https://avatar.example.com/wangwu.png', '192.168.1.7', '2026-06-10 10:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (8, 'U008', 'zhao_liu', 'zhao123', '赵六', 'zhaoliu@wanxiang.com', '13800000008', 'https://avatar.example.com/zhaoliu.png', '192.168.1.8', '2026-06-10 10:15:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9, 'U009', 'sun_qi', 'sun123', '孙七', 'sunqi@wanxiang.com', '13800000009', 'https://avatar.example.com/sunqi.png', '192.168.1.9', '2026-06-10 10:30:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (10, 'U010', 'zhou_ba', 'zhou123', '周八', 'zhouba@wanxiang.com', '13800000010', 'https://avatar.example.com/zhouba.png', '192.168.1.10', '2026-06-10 10:45:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (11, 'U011', 'wu_jiu', 'wu123', '武九', 'wujiu@wanxiang.com', '13800000011', 'https://avatar.example.com/wujiu.png', '192.168.1.11', '2026-06-10 11:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (12, 'U012', 'zheng_shi', 'zheng123', '郑十', 'zhengshi@xingchen.com', '13800000012', 'https://avatar.example.com/zhengshi.png', '192.168.3.1', '2026-06-10 11:15:00.000000', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (13, 'U013', 'qian_yi', 'qian123', '钱一', 'qianyi@wanxiang.com', '13800000013', 'https://avatar.example.com/qianyi.png', '192.168.1.12', '2026-06-10 11:30:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (14, 'U014', 'chen_er', 'chen123', '陈二', 'chener@wanxiang.com', '13800000014', 'https://avatar.example.com/chener.png', '192.168.1.13', '2026-06-10 13:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (15, 'U015', 'feng_san', 'feng123', '冯三', 'fengsan@wanxiang.com', '13800000015', 'https://avatar.example.com/fengsan.png', '192.168.1.14', '2026-06-10 13:15:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (16, 'U016', 'chu_si', 'chu123', '楚四', 'chusi@haina.com', '13800000016', 'https://avatar.example.com/chusi.png', '192.168.4.1', '2026-06-10 13:30:00.000000', 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (17, 'U017', 'huang_audit', 'huang123', '黄审计', 'huang@wanxiang.com', '13800000017', 'https://avatar.example.com/huang.png', '192.168.1.15', '2026-06-10 13:45:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (18, 'U018', 'xu_readonly', 'xu123', '徐只读', 'xu@wanxiang.com', '13800000018', 'https://avatar.example.com/xu.png', '192.168.1.16', '2026-06-10 14:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (19, 'U019', 'dingxin_ceo', 'ding123', '丁总', 'ding@dingxin.com', '13800000019', 'https://avatar.example.com/ding.png', '192.168.2.1', '2026-06-10 08:00:00.000000', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (20, 'U020', 'dong_manager', 'dong123', '董经理', 'dong@dingxin.com', '13800000020', 'https://avatar.example.com/dong.png', '192.168.2.2', '2026-06-10 08:30:00.000000', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (21, 'U021', 'guo_staff', 'guo123', '郭员工', 'guo@dingxin.com', '13800000021', 'https://avatar.example.com/guo.png', '192.168.2.3', '2026-06-10 09:00:00.000000', 1, '2026-02-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (22, 'U022', 'xingchen_cto', 'cheng123', '程总', 'cheng@xingchen.com', '13800000022', 'https://avatar.example.com/cheng.png', '192.168.3.2', '2026-06-10 09:30:00.000000', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (23, 'U023', 'yan_dev', 'yan123', '严研发', 'yan@xingchen.com', '13800000023', 'https://avatar.example.com/yan.png', '192.168.3.3', '2026-06-10 10:00:00.000000', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (24, 'U024', 'cai_market', 'cai123', '蔡营销', 'cai@xingchen.com', '13800000024', 'https://avatar.example.com/cai.png', '192.168.3.4', '2026-06-10 10:30:00.000000', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (25, 'U025', 'han_overseas', 'han123', '韩海外', 'han@xingchen.com', '13800000025', 'https://avatar.example.com/han.png', '192.168.3.5', '2026-06-10 11:00:00.000000', 1, '2026-03-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (26, 'U026', 'hai_manager', 'hai123', '海经理', 'hai@haina.com', '13800000026', 'https://avatar.example.com/hai.png', '192.168.4.2', '2026-06-10 08:00:00.000000', 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (27, 'U027', 'lu_edu', 'lu123', '陆教育', 'lu@haina.com', '13800000027', 'https://avatar.example.com/lu.png', '192.168.4.3', '2026-06-10 08:30:00.000000', 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (28, 'U028', 'qiao_med', 'qiao123', '乔医疗', 'qiao@haina.com', '13800000028', 'https://avatar.example.com/qiao.png', '192.168.4.4', '2026-06-10 09:00:00.000000', 1, '2026-04-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (29, 'U029', 'tian_logistics', 'tian123', '田物流', 'tian@tianxiang.com', '13800000029', 'https://avatar.example.com/tian.png', '192.168.5.1', '2026-06-10 14:00:00.000000', 1, '2026-05-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (30, 'U030', 'multi_user', 'multi123', '多租户用户', 'multiuser@nexusix.com', '13800000030', 'https://avatar.example.com/multi.png', '192.168.1.100', '2026-06-10 07:00:00.000000', 1, '2026-01-01 08:00:00.000000', 1, '2026-06-01 10:00:00.000000', 'NOT_DELETED', null);
