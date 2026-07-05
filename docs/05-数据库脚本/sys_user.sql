create table sys_user
(
    id             bigint       not null
        primary key,
    user_code      varchar(50)  not null,
    user_name      varchar(50)  not null,
    password       varchar(200) not null,
    plain_password varchar(100) not null,
    nick_name      varchar(50)  not null,
    real_name      varchar(50)  not null,
    email          varchar(100) not null,
    phone          varchar(20)  not null,
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

comment on column sys_user.password is '密码哈希值';

comment on column sys_user.plain_password is '明文密码';

comment on column sys_user.nick_name is '昵称';

comment on column sys_user.real_name is '真实姓名';

comment on column sys_user.email is '电子邮箱';

comment on column sys_user.phone is '手机号码';

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

INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (1, 'U001', 'admin', '$2a$10$encrypted_password', '123456', '超管', '系统管理员', 'admin@example.com', '13800000001', '/avatar/admin.png', 'MALE', '1985-01-01', 'ENABLED', null, '2025-06-12 10:30:00.000000', '192.168.1.100', 0, 0, 0, 1, '2025-01-01 08:00:00.000000', 1, '2025-06-12 10:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (4, 'U004', 'wangwu', '$2a$10$encrypted_password', '123456', '王五', '王五', 'wangwu@wanxiang.com', '13800000004', '/avatar/wangwu.png', 'FEMALE', '1992-09-10', 'ENABLED', null, '2025-06-12 08:30:00.000000', '192.168.1.103', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-12 08:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (6, 'U006', 'sunqi', '$2a$10$encrypted_password', '123456', '孙七', '孙七', 'sunqi@wanxiang.com', '13800000006', '/avatar/sunqi.png', 'FEMALE', '1993-04-18', 'ENABLED', null, '2025-06-10 14:20:00.000000', '192.168.1.104', 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-06-10 14:20:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7, 'U007', 'zhouba', '$2a$10$encrypted_password', '123456', '周八', '周八', 'zhouba@wanxiang.com', '13800000007', '/avatar/zhouba.png', 'MALE', '1991-07-30', 'ENABLED', null, '2025-06-12 07:45:00.000000', '192.168.1.105', 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-06-12 07:45:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (8, 'U008', 'wujiu', '$2a$10$encrypted_password', '123456', '吴九', '吴九', 'wujiu@wanxiang.com', '13800000008', '/avatar/wujiu.png', 'MALE', '1994-12-05', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-01-06 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (9, 'U009', 'zhengshi', '$2a$10$encrypted_password', '123456', '郑十', '郑十', 'zhengshi@wanxiang.com', '13800000009', '/avatar/zhengshi.png', 'FEMALE', '1996-02-14', 'DISABLED', '已离职', null, null, 0, 0, 0, 1, '2025-01-06 08:00:00.000000', 1, '2025-05-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (10, 'U010', 'qianyi', '$2a$10$encrypted_password', '123456', '钱一', '钱一', 'qianyi@wanxiang.com', '13800000010', '/avatar/qianyi.png', 'MALE', '1989-08-22', 'ENABLED', null, '2025-06-11 18:00:00.000000', '192.168.1.106', 0, 0, 0, 1, '2025-01-07 08:00:00.000000', 1, '2025-06-11 18:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (11, 'U011', 'chener', '$2a$10$encrypted_password', '123456', '陈二', '陈二', 'chener@dingxin.com', '13800000011', '/avatar/chener.png', 'MALE', '1987-05-10', 'ENABLED', null, '2025-06-12 09:15:00.000000', '192.168.2.101', 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-06-12 09:15:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (12, 'U012', 'fengsan', '$2a$10$encrypted_password', '123456', '冯三', '冯三', 'fengsan@dingxin.com', '13800000012', '/avatar/fengsan.png', 'MALE', '1990-11-20', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-01-08 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (13, 'U013', 'chusi', '$2a$10$encrypted_password', '123456', '楚四', '楚四', 'chusi@dingxin.com', '13800000013', '/avatar/chusi.png', 'FEMALE', '1992-06-30', 'ENABLED', null, '2025-06-11 15:30:00.000000', '192.168.2.102', 0, 0, 0, 1, '2025-01-08 08:00:00.000000', 1, '2025-06-11 15:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (14, 'U014', 'weiwu', '$2a$10$encrypted_password', '123456', '魏五', '魏五', 'weiwu@dingxin.com', '13800000014', '/avatar/weiwu.png', 'MALE', '1993-09-18', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-09 08:00:00.000000', 1, '2025-01-09 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (15, 'U015', 'jianglu', '$2a$10$encrypted_password', '123456', '蒋六', '蒋六', 'jianglu@dingxin.com', '13800000015', '/avatar/jianglu.png', 'FEMALE', '1991-03-25', 'ENABLED', null, '2025-06-10 11:00:00.000000', '192.168.2.103', 0, 0, 0, 1, '2025-01-09 08:00:00.000000', 1, '2025-06-10 11:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (16, 'U016', 'shenqi', '$2a$10$encrypted_password', '123456', '沈七', '沈七', 'shenqi@haina.com', '13800000016', '/avatar/shenqi.png', 'MALE', '1988-12-12', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-01-10 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (17, 'U017', 'hanba', '$2a$10$encrypted_password', '123456', '韩八', '韩八', 'hanba@haina.com', '13800000017', '/avatar/hanba.png', 'MALE', '1994-07-08', 'ENABLED', null, '2025-06-11 13:20:00.000000', '192.168.3.101', 0, 0, 0, 1, '2025-01-10 08:00:00.000000', 1, '2025-06-11 13:20:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (18, 'U018', 'yangjiu', '$2a$10$encrypted_password', '123456', '杨九', '杨九', 'yangjiu@example.com', '13800000018', '/avatar/yangjiu.png', 'MALE', '1990-10-15', 'ENABLED', null, '2025-06-12 08:00:00.000000', '192.168.1.107', 0, 0, 0, 1, '2025-01-11 08:00:00.000000', 1, '2025-06-12 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (19, 'U019', 'zhushi', '$2a$10$encrypted_password', '123456', '朱十', '朱十', 'zhushi@example.com', '13800000019', '/avatar/zhushi.png', 'FEMALE', '1992-05-20', 'ENABLED', null, '2025-06-11 17:30:00.000000', '192.168.1.108', 0, 0, 0, 1, '2025-01-11 08:00:00.000000', 1, '2025-06-11 17:30:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (20, 'U020', 'intern01', '$2a$10$encrypted_password', '123456', '实习生A', '实习生A', 'intern01@wanxiang.com', '13800000020', '/avatar/intern01.png', 'MALE', '2000-01-01', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (21, 'U021', 'intern02', '$2a$10$encrypted_password', '123456', '实习生B', '实习生B', 'intern02@wanxiang.com', '13800000021', '/avatar/intern02.png', 'FEMALE', '2001-06-15', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-03-01 08:00:00.000000', 1, '2025-03-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (22, 'U022', 'contractor01', '$2a$10$encrypted_password', '123456', '外包C', '外包C', 'contractor01@external.com', '13800000022', '/avatar/contractor01.png', 'MALE', '1995-08-20', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-04-01 08:00:00.000000', 1, '2025-04-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (23, 'U023', 'user23', '$2a$10$encrypted_password', '123456', '用户23', '用户23', 'user23@wanxiang.com', '13800000023', '/avatar/user23.png', 'MALE', '1989-03-10', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (24, 'U024', 'user24', '$2a$10$encrypted_password', '123456', '用户24', '用户24', 'user24@wanxiang.com', '13800000024', '/avatar/user24.png', 'FEMALE', '1991-07-22', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-01 08:00:00.000000', 1, '2025-02-01 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (25, 'U025', 'user25', '$2a$10$encrypted_password', '123456', '用户25', '用户25', 'user25@dingxin.com', '13800000025', '/avatar/user25.png', 'MALE', '1993-11-05', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-02 08:00:00.000000', 1, '2025-02-02 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (26, 'U026', 'user26', '$2a$10$encrypted_password', '123456', '用户26', '用户26', 'user26@dingxin.com', '13800000026', '/avatar/user26.png', 'FEMALE', '1990-04-18', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-02 08:00:00.000000', 1, '2025-02-02 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (27, 'U027', 'user27', '$2a$10$encrypted_password', '123456', '用户27', '用户27', 'user27@haina.com', '13800000027', '/avatar/user27.png', 'MALE', '1988-09-30', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-03 08:00:00.000000', 1, '2025-02-03 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (28, 'U028', 'user28', '$2a$10$encrypted_password', '123456', '用户28', '用户28', 'user28@wanxiang.com', '13800000028', '/avatar/user28.png', 'MALE', '1992-12-25', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-04 08:00:00.000000', 1, '2025-02-04 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (29, 'U029', 'user29', '$2a$10$encrypted_password', '123456', '用户29', '用户29', 'user29@wanxiang.com', '13800000029', '/avatar/user29.png', 'FEMALE', '1994-05-12', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-04 08:00:00.000000', 1, '2025-02-04 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (30, 'U030', 'user30', '$2a$10$encrypted_password', '123456', '用户30', '用户30', 'user30@dingxin.com', '13800000030', '/avatar/user30.png', 'MALE', '1987-08-08', 'ENABLED', null, null, null, 0, 0, 0, 1, '2025-02-05 08:00:00.000000', 1, '2025-02-05 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (5, 'U005', 'zhaoliu', '$2a$10$encrypted_password', '123456', '赵六', '赵六', 'zhaoliu@wanxiang.com', '13800000005', '/avatar/zhaoliu.png', 'MALE', '1995-11-25', 'ENABLED', null, '2026-06-13 15:29:58.746089', null, 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-01-05 08:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (2, 'U002', 'zhangsan', '$2a$10$encrypted_password', '123456', '张三', '张三', 'zhangsan@wanxiang.com', '13800000002', '/avatar/zhangsan.png', 'MALE', '1990-03-15', 'ENABLED', null, '2026-06-12 22:27:14.829712', '192.168.1.101', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-12 09:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (3, 'U003', 'lisi', '$2a$10$encrypted_password', '123456', '李四', '李四', 'lisi@wanxiang.com', '13800000003', '/avatar/lisi.png', 'MALE', '1988-06-20', 'ENABLED', null, '2026-06-13 18:30:38.544006', '192.168.1.102', 0, 0, 0, 1, '2025-01-05 08:00:00.000000', 1, '2025-06-11 16:00:00.000000', 'NOT_DELETED', null);
INSERT INTO public.sys_user (id, user_code, user_name, password, plain_password, nick_name, real_name, email, phone, avatar_url, gender, birthday, status, disable_reason, last_login_at, last_login_ip, create_tenant, create_dept, create_role, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES (7301, 'TEST_ALL', 'test_all', '$2a$10$dummyhash', '123456', '全能测试', '全能测试员', 'test_all@test.com', '13973010001', null, 'MALE', '1990-01-01', 'ENABLED', null, '2026-06-16 19:45:34.496942', null, 0, 0, 0, 1, '2026-06-15 08:19:09.587299', 1, '2026-06-15 08:19:09.587299', 'NOT_DELETED', null);
