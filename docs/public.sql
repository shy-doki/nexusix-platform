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

alter table sys_tenant
    owner to postgres;

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

alter table sys_user
    owner to postgres;

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

alter table sys_user_policy
    owner to postgres;

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

alter table sys_perm
    owner to postgres;

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

alter table sys_perm_policy
    owner to postgres;

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

alter table sys_role
    owner to postgres;

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

alter table sys_role_policy
    owner to postgres;

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

alter table sys_tenant_subscription
    owner to postgres;

