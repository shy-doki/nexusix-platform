-- ============================================================
-- NexusIX Platform 测试数据脚本
-- 生成时间: 2026-06-10
-- 说明: 每张表30条记录，体现多层级租户、多租户用户、多角色、多权限
-- 策略表绑定关系:
--   sys_user_policy.target_id = sys_tenant.id (系统租户ID)
--   sys_role_policy.target_id = sys_user_policy.id (用户策略ID)
--   sys_perm_policy.target_id = 随target_type变化:
--     TENANT → sys_tenant.id / ROLE → sys_role_policy.id / USER → sys_user_policy.id
-- ============================================================

-- ============================================================
-- 1. sys_tenant (30条) - 多层级租户结构
-- ============================================================
INSERT INTO sys_tenant (id, tenant_code, tenant_name, tenant_type, tenant_desc, tenant_logo_url, parent_id, parent_code, parent_name, path, contact_name, contact_phone, expire_time, package_id, package_name, ext_attributes, has_children, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- 根租户 A: 万象集团
(100, 'WANXIANG', '万象集团', 'ENTERPRISE', '综合性企业集团，覆盖华东、华南、华北、西南四大区域', 'https://logo.example.com/wx.png', 0, 'ROOT', '根节点', '/100', '张总', '13800001001', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":5000,"region":"全国"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(110, 'WX_EAST', '万象集团-华东分公司', 'BRANCH', '负责华东区域业务运营', 'https://logo.example.com/wx_east.png', 100, 'WANXIANG', '万象集团', '/100/110', '李经理', '13800001002', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":1200,"region":"华东"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'OFFICE', '上海地区业务运营中心', 'https://logo.example.com/wx_sh.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/111', '王主管', '13800001003', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":300,"region":"上海"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(112, 'WX_EAST_HZ', '万象集团-华东-杭州办事处', 'OFFICE', '杭州地区业务运营中心', 'https://logo.example.com/wx_hz.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/112', '孙主管', '13800001004', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":200,"region":"杭州"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(113, 'WX_EAST_NJ', '万象集团-华东-南京办事处', 'OFFICE', '南京地区业务运营中心（已停用）', 'https://logo.example.com/wx_nj.png', 110, 'WX_EAST', '万象集团-华东分公司', '/100/110/113', '赵主管', '13800001005', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":50,"region":"南京"}'::jsonb, false, 'DISABLED', '业务调整，合并至上海办事处', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(120, 'WX_SOUTH', '万象集团-华南分公司', 'BRANCH', '负责华南区域业务运营', 'https://logo.example.com/wx_south.png', 100, 'WANXIANG', '万象集团', '/100/120', '陈经理', '13800001006', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":1000,"region":"华南"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(121, 'WX_SOUTH_SZ', '万象集团-华南-深圳办事处', 'OFFICE', '深圳地区业务运营中心', 'https://logo.example.com/wx_sz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/121', '周主管', '13800001007', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":350,"region":"深圳"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(122, 'WX_SOUTH_GZ', '万象集团-华南-广州办事处', 'OFFICE', '广州地区业务运营中心', 'https://logo.example.com/wx_gz.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/122', '林主管', '13800001008', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":280,"region":"广州"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(123, 'WX_SOUTH_XM', '万象集团-华南-厦门办事处', 'OFFICE', '厦门地区业务运营中心', 'https://logo.example.com/wx_xm.png', 120, 'WX_SOUTH', '万象集团-华南分公司', '/100/120/123', '吴主管', '13800001009', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":150,"region":"厦门"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(130, 'WX_NORTH', '万象集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/wx_north.png', 100, 'WANXIANG', '万象集团', '/100/130', '武经理', '13800001010', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":800,"region":"华北"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(131, 'WX_NORTH_BJ', '万象集团-华北-北京办事处', 'OFFICE', '北京地区业务运营中心', 'https://logo.example.com/wx_bj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/131', '郑主管', '13800001011', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":400,"region":"北京"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(132, 'WX_NORTH_TJ', '万象集团-华北-天津办事处', 'OFFICE', '天津地区业务运营中心（已过期）', 'https://logo.example.com/wx_tj.png', 130, 'WX_NORTH', '万象集团-华北分公司', '/100/130/132', '冯主管', '13800001012', '2025-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":100,"region":"天津"}'::jsonb, false, 'EXPIRED', '租约未续费已过期', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(140, 'WX_SOUTHWEST', '万象集团-西南分公司', 'BRANCH', '负责西南区域业务运营（已停用）', 'https://logo.example.com/wx_sw.png', 100, 'WANXIANG', '万象集团', '/100/140', '何经理', '13800001013', '2030-12-31 23:59:59', 1, '企业旗舰版', '{"industry":"综合","scale":300,"region":"西南"}'::jsonb, false, 'DISABLED', '战略调整暂停运营', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 根租户 B: 鼎新集团
(200, 'DINGXIN', '鼎新集团', 'ENTERPRISE', '专注制造业的集团企业', 'https://logo.example.com/dx.png', 0, 'ROOT', '根节点', '/200', '丁总', '13800002001', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":3000,"region":"全国"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(210, 'DX_CENTRAL', '鼎新集团-华中分公司', 'BRANCH', '负责华中区域业务运营', 'https://logo.example.com/dx_central.png', 200, 'DINGXIN', '鼎新集团', '/200/210', '马经理', '13800002002', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":800,"region":"华中"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'OFFICE', '武汉地区业务运营中心', 'https://logo.example.com/dx_wh.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/211', '董主管', '13800002003', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":300,"region":"武汉"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(212, 'DX_CENTRAL_CS', '鼎新集团-华中-长沙办事处', 'OFFICE', '长沙地区业务运营中心', 'https://logo.example.com/dx_cs.png', 210, 'DX_CENTRAL', '鼎新集团-华中分公司', '/200/210/212', '谢主管', '13800002004', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":200,"region":"长沙"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(220, 'DX_NORTH', '鼎新集团-华北分公司', 'BRANCH', '负责华北区域业务运营', 'https://logo.example.com/dx_north.png', 200, 'DINGXIN', '鼎新集团', '/200/220', '韩经理', '13800002005', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":600,"region":"华北"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(221, 'DX_NORTH_SJZ', '鼎新集团-华北-石家庄办事处', 'OFFICE', '石家庄地区业务运营中心', 'https://logo.example.com/dx_sjz.png', 220, 'DX_NORTH', '鼎新集团-华北分公司', '/200/220/221', '郭员工', '13800002006', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":150,"region":"石家庄"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(230, 'DX_NORTHEAST', '鼎新集团-东北分公司', 'BRANCH', '负责东北区域业务运营（待激活）', 'https://logo.example.com/dx_ne.png', 200, 'DINGXIN', '鼎新集团', '/200/230', '曹经理', '13800002007', '2032-06-30 23:59:59', 2, '制造业专版', '{"industry":"制造业","scale":0,"region":"东北"}'::jsonb, false, 'PENDING', '新设立，尚未正式运营', 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 根租户 C: 星辰科技有限公司
(300, 'XINGCHEN', '星辰科技有限公司', 'COMPANY', '专注互联网科技研发的创新企业', 'https://logo.example.com/xc.png', 0, 'ROOT', '根节点', '/300', '程总', '13800003001', '2031-09-30 23:59:59', 3, '科技初创版', '{"industry":"互联网","scale":500,"region":"全国"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(310, 'XC_RD', '星辰科技-研发中心', 'DEPARTMENT', '核心产品研发与技术攻关', 'https://logo.example.com/xc_rd.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/310', '严经理', '13800003002', '2031-09-30 23:59:59', 3, '科技初创版', '{"industry":"互联网","scale":200,"region":"杭州"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(320, 'XC_MARKET', '星辰科技-营销中心', 'DEPARTMENT', '市场推广与销售管理', 'https://logo.example.com/xc_market.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/320', '蔡总监', '13800003003', '2031-09-30 23:59:59', 3, '科技初创版', '{"industry":"互联网","scale":100,"region":"上海"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPARTMENT', '海外市场拓展与国际合作', 'https://logo.example.com/xc_overseas.png', 300, 'XINGCHEN', '星辰科技有限公司', '/300/330', '韩经理', '13800003004', '2031-09-30 23:59:59', 3, '科技初创版', '{"industry":"互联网","scale":80,"region":"海外"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 根租户 D: 海纳百川集团
(400, 'HAINA', '海纳百川集团', 'ENTERPRISE', '多元化投资控股集团', 'https://logo.example.com/hn.png', 0, 'ROOT', '根节点', '/400', '海总', '13800004001', '2033-03-31 23:59:59', 4, '集团定制版', '{"industry":"投资控股","scale":2000,"region":"全国"}'::jsonb, true, 'ENABLED', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(410, 'HN_EDU', '海纳百川-教育科技', 'SUBSIDIARY', '在线教育平台与教育科技产品', 'https://logo.example.com/hn_edu.png', 400, 'HAINA', '海纳百川集团', '/400/410', '楚总', '13800005001', '2033-03-31 23:59:59', 5, '教育行业版', '{"industry":"教育","scale":400,"region":"全国"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(420, 'HN_MEDICAL', '海纳百川-医疗健康', 'SUBSIDIARY', '智慧医疗与大健康服务', 'https://logo.example.com/hn_med.png', 400, 'HAINA', '海纳百川集团', '/400/420', '乔总', '13800006001', '2033-03-31 23:59:59', 5, '医疗行业版', '{"industry":"医疗健康","scale":350,"region":"全国"}'::jsonb, false, 'ENABLED', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 根租户 E: 天翔物流（已停用）
(500, 'TIANXIANG', '天翔物流', 'COMPANY', '全国性物流配送企业（已停用）', 'https://logo.example.com/tx.png', 0, 'ROOT', '根节点', '/500', '田总', '13800007001', '2026-03-31 23:59:59', 6, '物流基础版', '{"industry":"物流","scale":600,"region":"全国"}'::jsonb, true, 'DISABLED', '经营异常，暂停服务', 1, '2026-05-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(510, 'TX_WAREHOUSE', '天翔物流-仓储部', 'DEPARTMENT', '全国仓储管理中心', 'https://logo.example.com/tx_wh.png', 500, 'TIANXIANG', '天翔物流', '/500/510', '田主管', '13800007002', '2026-03-31 23:59:59', 6, '物流基础版', '{"industry":"物流","scale":200,"region":"全国"}'::jsonb, false, 'DISABLED', '随母公司停用', 1, '2026-05-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(520, 'TX_TRANSPORT', '天翔物流-运输部', 'DEPARTMENT', '全国干线运输调度中心（待激活）', 'https://logo.example.com/tx_tp.png', 500, 'TIANXIANG', '天翔物流', '/500/520', '田调度', '13800007003', '2026-03-31 23:59:59', 6, '物流基础版', '{"industry":"物流","scale":150,"region":"全国"}'::jsonb, false, 'PENDING', '待母公司恢复运营', 1, '2026-05-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 2. sys_user (30条)
-- ============================================================
INSERT INTO sys_user (id, user_code, user_name, password, nick_name, email, phone, avatar, login_ip, login_date, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
(1, 'U001', 'admin', 'admin123', '超级管理员', 'admin@nexusix.com', '13800000001', 'https://avatar.example.com/admin.png', '192.168.1.1', '2026-06-10 08:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2, 'U002', 'zhang_zong', 'zhang123', '张总', 'zhangzong@wanxiang.com', '13800000002', 'https://avatar.example.com/zhangzong.png', '192.168.1.2', '2026-06-10 08:30:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3, 'U003', 'li_jingli', 'li123', '李经理', 'lijingli@wanxiang.com', '13800000003', 'https://avatar.example.com/lijingli.png', '192.168.1.3', '2026-06-10 09:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(4, 'U004', 'wang_zhuguan', 'wang123', '王主管', 'wangzhuguan@wanxiang.com', '13800000004', 'https://avatar.example.com/wangzhuguan.png', '192.168.1.4', '2026-06-10 09:15:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5, 'U005', 'zhang_san', 'zhang123', '张三', 'zhangsan@wanxiang.com', '13800000005', 'https://avatar.example.com/zhangsan.png', '192.168.1.5', '2026-06-10 09:30:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(6, 'U006', 'li_si', 'li123', '李四', 'lisi@wanxiang.com', '13800000006', 'https://avatar.example.com/lisi.png', '192.168.1.6', '2026-06-10 09:45:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(7, 'U007', 'wang_wu', 'wang123', '王五', 'wangwu@wanxiang.com', '13800000007', 'https://avatar.example.com/wangwu.png', '192.168.1.7', '2026-06-10 10:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(8, 'U008', 'zhao_liu', 'zhao123', '赵六', 'zhaoliu@wanxiang.com', '13800000008', 'https://avatar.example.com/zhaoliu.png', '192.168.1.8', '2026-06-10 10:15:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9, 'U009', 'sun_qi', 'sun123', '孙七', 'sunqi@wanxiang.com', '13800000009', 'https://avatar.example.com/sunqi.png', '192.168.1.9', '2026-06-10 10:30:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(10, 'U010', 'zhou_ba', 'zhou123', '周八', 'zhouba@wanxiang.com', '13800000010', 'https://avatar.example.com/zhouba.png', '192.168.1.10', '2026-06-10 10:45:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(11, 'U011', 'wu_jiu', 'wu123', '武九', 'wujiu@wanxiang.com', '13800000011', 'https://avatar.example.com/wujiu.png', '192.168.1.11', '2026-06-10 11:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(12, 'U012', 'zheng_shi', 'zheng123', '郑十', 'zhengshi@xingchen.com', '13800000012', 'https://avatar.example.com/zhengshi.png', '192.168.3.1', '2026-06-10 11:15:00', 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(13, 'U013', 'qian_yi', 'qian123', '钱一', 'qianyi@wanxiang.com', '13800000013', 'https://avatar.example.com/qianyi.png', '192.168.1.12', '2026-06-10 11:30:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(14, 'U014', 'chen_er', 'chen123', '陈二', 'chener@wanxiang.com', '13800000014', 'https://avatar.example.com/chener.png', '192.168.1.13', '2026-06-10 13:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(15, 'U015', 'feng_san', 'feng123', '冯三', 'fengsan@wanxiang.com', '13800000015', 'https://avatar.example.com/fengsan.png', '192.168.1.14', '2026-06-10 13:15:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(16, 'U016', 'chu_si', 'chu123', '楚四', 'chusi@haina.com', '13800000016', 'https://avatar.example.com/chusi.png', '192.168.4.1', '2026-06-10 13:30:00', 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(17, 'U017', 'huang_audit', 'huang123', '黄审计', 'huang@wanxiang.com', '13800000017', 'https://avatar.example.com/huang.png', '192.168.1.15', '2026-06-10 13:45:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(18, 'U018', 'xu_readonly', 'xu123', '徐只读', 'xu@wanxiang.com', '13800000018', 'https://avatar.example.com/xu.png', '192.168.1.16', '2026-06-10 14:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(19, 'U019', 'dingxin_ceo', 'ding123', '丁总', 'ding@dingxin.com', '13800000019', 'https://avatar.example.com/ding.png', '192.168.2.1', '2026-06-10 08:00:00', 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(20, 'U020', 'dong_manager', 'dong123', '董经理', 'dong@dingxin.com', '13800000020', 'https://avatar.example.com/dong.png', '192.168.2.2', '2026-06-10 08:30:00', 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(21, 'U021', 'guo_staff', 'guo123', '郭员工', 'guo@dingxin.com', '13800000021', 'https://avatar.example.com/guo.png', '192.168.2.3', '2026-06-10 09:00:00', 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(22, 'U022', 'xingchen_cto', 'cheng123', '程总', 'cheng@xingchen.com', '13800000022', 'https://avatar.example.com/cheng.png', '192.168.3.2', '2026-06-10 09:30:00', 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(23, 'U023', 'yan_dev', 'yan123', '严研发', 'yan@xingchen.com', '13800000023', 'https://avatar.example.com/yan.png', '192.168.3.3', '2026-06-10 10:00:00', 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(24, 'U024', 'cai_market', 'cai123', '蔡营销', 'cai@xingchen.com', '13800000024', 'https://avatar.example.com/cai.png', '192.168.3.4', '2026-06-10 10:30:00', 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(25, 'U025', 'han_overseas', 'han123', '韩海外', 'han@xingchen.com', '13800000025', 'https://avatar.example.com/han.png', '192.168.3.5', '2026-06-10 11:00:00', 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(26, 'U026', 'hai_manager', 'hai123', '海经理', 'hai@haina.com', '13800000026', 'https://avatar.example.com/hai.png', '192.168.4.2', '2026-06-10 08:00:00', 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(27, 'U027', 'lu_edu', 'lu123', '陆教育', 'lu@haina.com', '13800000027', 'https://avatar.example.com/lu.png', '192.168.4.3', '2026-06-10 08:30:00', 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(28, 'U028', 'qiao_med', 'qiao123', '乔医疗', 'qiao@haina.com', '13800000028', 'https://avatar.example.com/qiao.png', '192.168.4.4', '2026-06-10 09:00:00', 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(29, 'U029', 'tian_logistics', 'tian123', '田物流', 'tian@tianxiang.com', '13800000029', 'https://avatar.example.com/tian.png', '192.168.5.1', '2026-06-10 14:00:00', 1, '2026-05-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(30, 'U030', 'multi_user', 'multi123', '多租户用户', 'multiuser@nexusix.com', '13800000030', 'https://avatar.example.com/multi.png', '192.168.1.100', '2026-06-10 07:00:00', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 3. sys_role (30条) - 各租户下的角色定义
-- ============================================================
INSERT INTO sys_role (id, role_name, role_desc, role_code, role_level, tenant_id, tenant_code, tenant_name, data_scope, sort_order, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- 万象集团(100) 角色
(3001, '超级管理员', '系统最高权限管理员', 'SUPER_ADMIN', 'SYSTEM', 100, 'WANXIANG', '万象集团', 'ALL', 1, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3002, '租户管理员', '万象集团租户级管理员', 'TENANT_ADMIN', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 2, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3003, '部门管理员', '万象集团部门级管理员', 'DEPT_MANAGER', 'DEPT', 100, 'WANXIANG', '万象集团', 'DEPT_AND_SUB', 3, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3004, '普通员工', '万象集团普通员工', 'EMPLOYEE', 'USER', 100, 'WANXIANG', '万象集团', 'SELF', 4, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3005, '审计员', '万象集团审计专员', 'AUDITOR', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 5, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3006, '只读用户', '万象集团只读权限用户', 'READONLY', 'TENANT', 100, 'WANXIANG', '万象集团', 'ALL', 6, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3007, '财务专员', '万象集团财务人员', 'FINANCE', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 7, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3008, '人事专员', '万象集团人力资源专员', 'HR', 'TENANT', 100, 'WANXIANG', '万象集团', 'DEPT', 8, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 华东分公司(110) 角色
(3009, '分公司管理员', '华东分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 110, 'WX_EAST', '万象集团-华东分公司', 'DEPT_AND_SUB', 1, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 上海办事处(111) 角色
(3010, '办事处主管', '上海办事处主管', 'OFFICE_MANAGER', 'OFFICE', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'DEPT', 1, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3011, '普通员工', '上海办事处普通员工', 'STAFF', 'USER', 111, 'WX_EAST_SH', '万象集团-华东-上海办事处', 'SELF', 2, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 华南分公司(120) 角色
(3012, '销售人员', '华南分公司销售专员', 'SALES', 'USER', 120, 'WX_SOUTH', '万象集团-华南分公司', 'SELF', 1, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 华北分公司(130) 角色
(3013, '分公司管理员', '华北分公司管理员', 'BRANCH_ADMIN', 'BRANCH', 130, 'WX_NORTH', '万象集团-华北分公司', 'DEPT_AND_SUB', 1, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3014, '普通员工', '华北分公司普通员工', 'STAFF', 'USER', 130, 'WX_NORTH', '万象集团-华北分公司', 'SELF', 2, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 鼎新集团(200) 角色
(3015, '租户管理员', '鼎新集团管理员', 'TENANT_ADMIN', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 1, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3016, '部门管理员', '鼎新集团部门管理员', 'DEPT_MANAGER', 'DEPT', 200, 'DINGXIN', '鼎新集团', 'DEPT_AND_SUB', 2, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3017, '普通员工', '鼎新集团普通员工', 'EMPLOYEE', 'USER', 200, 'DINGXIN', '鼎新集团', 'SELF', 3, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3018, '审计员', '鼎新集团审计专员', 'AUDITOR', 'TENANT', 200, 'DINGXIN', '鼎新集团', 'ALL', 4, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 武汉办事处(211) 角色
(3019, '办事处管理员', '武汉办事处管理员', 'OFFICE_MANAGER', 'OFFICE', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'DEPT', 1, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3020, '普通员工', '武汉办事处普通员工', 'STAFF', 'USER', 211, 'DX_CENTRAL_WH', '鼎新集团-华中-武汉办事处', 'SELF', 2, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰科技(300) 角色
(3021, '租户管理员', '星辰科技管理员', 'TENANT_ADMIN', 'TENANT', 300, 'XINGCHEN', '星辰科技有限公司', 'ALL', 1, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3022, '研发工程师', '星辰科技研发人员', 'DEV_ENGINEER', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 2, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3023, '营销总监', '星辰科技营销负责人', 'MARKET_DIRECTOR', 'DEPT', 300, 'XINGCHEN', '星辰科技有限公司', 'DEPT', 3, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰研发中心(310) 角色
(3024, '研发工程师', '研发中心开发工程师', 'DEV_ENGINEER', 'USER', 310, 'XC_RD', '星辰科技-研发中心', 'SELF', 1, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰营销中心(320) 角色
(3025, '营销专员', '营销中心市场专员', 'MARKET_SPECIALIST', 'USER', 320, 'XC_MARKET', '星辰科技-营销中心', 'SELF', 1, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 海外事业部(330) 角色
(3026, '海外经理', '海外事业部负责人', 'OVERSEAS_MANAGER', 'DEPT', 330, 'XC_OVERSEAS', '星辰科技-海外事业部', 'DEPT', 1, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 海纳百川(400) 角色
(3027, '租户管理员', '海纳百川集团管理员', 'TENANT_ADMIN', 'TENANT', 400, 'HAINA', '海纳百川集团', 'ALL', 1, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 教育科技(410) 角色
(3028, '部门管理员', '教育科技部门管理员', 'DEPT_MANAGER', 'DEPT', 410, 'HN_EDU', '海纳百川-教育科技', 'DEPT_AND_SUB', 1, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(3029, '普通员工', '教育科技普通员工', 'STAFF', 'USER', 410, 'HN_EDU', '海纳百川-教育科技', 'SELF', 2, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 医疗健康(420) 角色
(3030, '普通员工', '医疗健康普通员工', 'STAFF', 'USER', 420, 'HN_MEDICAL', '海纳百川-医疗健康', 'SELF', 1, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 4. sys_user_policy (30条) - 用户↔租户绑定
-- target_id = 系统租户ID (sys_tenant.id)
-- ============================================================
INSERT INTO sys_user_policy (id, policy_code, policy_name, target_id, target_type, user_id, is_admin, is_default, join_time, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- 万象集团及其子租户
(1001, 'UP_001', 'admin-万象集团-默认策略', 100, 'USER', 1, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1002, 'UP_002', 'zhang_zong-万象集团-默认策略', 100, 'USER', 2, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1003, 'UP_003', 'li_jingli-华东分公司-默认策略', 110, 'USER', 3, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1004, 'UP_004', 'wang_zhuguan-上海办事处-默认策略', 111, 'USER', 4, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1005, 'UP_005', 'zhang_san-上海办事处-默认策略', 111, 'USER', 5, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1006, 'UP_006', 'li_si-深圳办事处-默认策略', 121, 'USER', 6, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1007, 'UP_007', 'wang_wu-华南分公司-默认策略', 120, 'USER', 7, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1008, 'UP_008', 'wang_wu-深圳办事处-附加策略', 121, 'USER', 7, false, false, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1009, 'UP_009', 'zhao_liu-广州办事处-默认策略', 122, 'USER', 8, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1010, 'UP_010', 'sun_qi-杭州办事处-默认策略', 112, 'USER', 9, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1011, 'UP_011', 'zhou_ba-万象集团-默认策略', 100, 'USER', 10, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1012, 'UP_012', 'wu_jiu-华北分公司-默认策略', 130, 'USER', 11, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1013, 'UP_013', 'zheng_shi-星辰科技-默认策略', 300, 'USER', 12, false, true, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1014, 'UP_014', 'qian_yi-万象集团-默认策略', 100, 'USER', 13, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1015, 'UP_015', 'chen_er-广州办事处-默认策略', 122, 'USER', 14, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1016, 'UP_016', 'feng_san-万象集团-默认策略', 100, 'USER', 15, true, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1017, 'UP_017', 'chu_si-教育科技-默认策略', 410, 'USER', 16, true, true, '2026-04-01 08:00:00', 'ENABLED', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1018, 'UP_018', 'huang_audit-万象集团-默认策略', 100, 'USER', 17, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1019, 'UP_019', 'xu_readonly-万象集团-默认策略', 100, 'USER', 18, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 鼎新集团
(1020, 'UP_020', 'dingxin_ceo-鼎新集团-默认策略', 200, 'USER', 19, true, true, '2026-02-01 08:00:00', 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1021, 'UP_021', 'dong_manager-武汉办事处-默认策略', 211, 'USER', 20, true, true, '2026-02-01 08:00:00', 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1022, 'UP_022', 'guo_staff-石家庄办事处-默认策略', 221, 'USER', 21, false, true, '2026-02-01 08:00:00', 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰科技
(1023, 'UP_023', 'xingchen_cto-星辰科技-默认策略', 300, 'USER', 22, true, true, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1024, 'UP_024', 'yan_dev-研发中心-默认策略', 310, 'USER', 23, false, true, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1025, 'UP_025', 'cai_market-营销中心-默认策略', 320, 'USER', 24, true, true, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1026, 'UP_026', 'han_overseas-海外事业部-默认策略', 330, 'USER', 25, true, true, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 海纳百川
(1027, 'UP_027', 'hai_manager-海纳百川-默认策略', 400, 'USER', 26, true, true, '2026-04-01 08:00:00', 'ENABLED', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 多租户用户 multi_user (user_id=30) 绑定到3个不同租户
(1028, 'UP_028', 'multi_user-万象集团-默认策略', 100, 'USER', 30, false, true, '2026-01-01 08:00:00', 'ENABLED', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1029, 'UP_029', 'multi_user-鼎新集团-附加策略', 200, 'USER', 30, false, false, '2026-02-01 08:00:00', 'ENABLED', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(1030, 'UP_030', 'multi_user-星辰科技-附加策略', 300, 'USER', 30, false, false, '2026-03-01 08:00:00', 'ENABLED', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 5. sys_role_policy (30条) - 角色策略: 用户策略↔角色绑定
-- target_id = 用户策略ID (sys_user_policy.id)
-- ============================================================
INSERT INTO sys_role_policy (id, policy_code, policy_name, target_id, target_type, role_id, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- 万象集团角色分配
(2001, 'RP_001', 'admin→超级管理员', 1001, 'USER', 3001, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2002, 'RP_002', 'zhang_zong→租户管理员', 1002, 'USER', 3002, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2003, 'RP_003', 'li_jingli→分公司管理员', 1003, 'USER', 3009, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2004, 'RP_004', 'wang_zhuguan→办事处主管', 1004, 'USER', 3010, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2005, 'RP_005', 'zhang_san→普通员工', 1005, 'USER', 3011, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2006, 'RP_006', 'li_si→普通员工', 1006, 'USER', 3011, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2007, 'RP_007', 'wang_wu→部门管理员(华南)', 1007, 'USER', 3003, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2008, 'RP_008', 'wang_wu→普通员工(深圳)', 1008, 'USER', 3011, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2009, 'RP_009', 'zhao_liu→销售专员', 1009, 'USER', 3012, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2010, 'RP_010', 'sun_qi→普通员工', 1010, 'USER', 3011, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2011, 'RP_011', 'zhou_ba→财务专员', 1011, 'USER', 3007, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2012, 'RP_012', 'wu_jiu→分公司管理员(华北)', 1012, 'USER', 3013, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2013, 'RP_013', 'zheng_shi→研发工程师', 1013, 'USER', 3022, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2014, 'RP_014', 'qian_yi→人事专员', 1014, 'USER', 3008, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2015, 'RP_015', 'chen_er→普通员工', 1015, 'USER', 3011, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2016, 'RP_016', 'feng_san→租户管理员', 1016, 'USER', 3002, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2017, 'RP_017', 'chu_si→部门管理员', 1017, 'USER', 3028, 'ACTIVE', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2018, 'RP_018', 'huang_audit→审计员', 1018, 'USER', 3005, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2019, 'RP_019', 'xu_readonly→只读用户', 1019, 'USER', 3006, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 鼎新集团角色分配
(2020, 'RP_020', 'dingxin_ceo→租户管理员', 1020, 'USER', 3015, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2021, 'RP_021', 'dong_manager→办事处管理员', 1021, 'USER', 3019, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2022, 'RP_022', 'guo_staff→普通员工', 1022, 'USER', 3017, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰科技角色分配
(2023, 'RP_023', 'xingchen_cto→租户管理员', 1023, 'USER', 3021, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2024, 'RP_024', 'yan_dev→研发工程师', 1024, 'USER', 3024, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2025, 'RP_025', 'cai_market→营销总监', 1025, 'USER', 3023, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2026, 'RP_026', 'han_overseas→海外经理', 1026, 'USER', 3026, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 海纳百川角色分配
(2027, 'RP_027', 'hai_manager→租户管理员', 1027, 'USER', 3027, 'ACTIVE', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 多租户用户 multi_user 在不同租户下拥有不同角色
(2028, 'RP_028', 'multi_user→普通员工(万象)', 1028, 'USER', 3004, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2029, 'RP_029', 'multi_user→普通员工(鼎新)', 1029, 'USER', 3017, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(2030, 'RP_030', 'multi_user→研发工程师(星辰)', 1030, 'USER', 3022, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 6. sys_perm (30条) - 权限定义
-- ============================================================
INSERT INTO sys_perm (id, perm_name, perm_desc, perm_code, perm_key, perm_type, parent_id, parent_name, path, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- 系统管理模块 (parent=0)
(9001, '系统管理', '系统管理模块入口', 'system:admin', 'system_admin', 'MENU', 0, '根节点', '/9001', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9002, '租户管理', '租户信息管理', 'tenant:manage', 'tenant_manage', 'MENU', 9001, '系统管理', '/9001/9002', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9003, '租户查看', '查看租户信息', 'tenant:view', 'tenant_view', 'BUTTON', 9002, '租户管理', '/9001/9002/9003', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9004, '租户创建', '创建新租户', 'tenant:create', 'tenant_create', 'BUTTON', 9002, '租户管理', '/9001/9002/9004', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9005, '租户编辑', '编辑租户信息', 'tenant:update', 'tenant_update', 'BUTTON', 9002, '租户管理', '/9001/9002/9005', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9006, '租户删除', '删除租户', 'tenant:delete', 'tenant_delete', 'BUTTON', 9002, '租户管理', '/9001/9002/9006', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9007, '用户管理', '用户信息管理', 'user:manage', 'user_manage', 'MENU', 9001, '系统管理', '/9001/9007', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9008, '用户查看', '查看用户信息', 'user:view', 'user_view', 'BUTTON', 9007, '用户管理', '/9001/9007/9008', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9009, '用户创建', '创建新用户', 'user:create', 'user_create', 'BUTTON', 9007, '用户管理', '/9001/9007/9009', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9010, '用户编辑', '编辑用户信息', 'user:update', 'user_update', 'BUTTON', 9007, '用户管理', '/9001/9007/9010', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9011, '角色管理', '角色信息管理', 'role:manage', 'role_manage', 'MENU', 9001, '系统管理', '/9001/9011', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9012, '角色查看', '查看角色信息', 'role:view', 'role_view', 'BUTTON', 9011, '角色管理', '/9001/9011/9012', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9013, '角色分配', '为用户分配角色', 'role:assign', 'role_assign', 'BUTTON', 9011, '角色管理', '/9001/9011/9013', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9014, '权限管理', '权限策略管理', 'perm:manage', 'perm_manage', 'MENU', 9001, '系统管理', '/9001/9014', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9015, '权限查看', '查看权限配置', 'perm:view', 'perm_view', 'BUTTON', 9014, '权限管理', '/9001/9014/9015', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9016, '权限分配', '分配权限策略', 'perm:assign', 'perm_assign', 'BUTTON', 9014, '权限管理', '/9001/9014/9016', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 业务管理模块 (parent=0)
(9017, '业务管理', '业务管理模块入口', 'business:admin', 'business_admin', 'MENU', 0, '根节点', '/9017', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9018, '报表查看', '查看业务报表', 'report:view', 'report_view', 'MENU', 9017, '业务管理', '/9017/9018', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9019, '报表导出', '导出报表数据', 'report:export', 'report_export', 'BUTTON', 9018, '报表查看', '/9017/9018/9019', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9020, '数据查询', '数据查询功能', 'data:query', 'data_query', 'MENU', 9017, '业务管理', '/9017/9020', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9021, '数据修改', '修改业务数据', 'data:modify', 'data_modify', 'BUTTON', 9020, '数据查询', '/9017/9020/9021', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9022, '系统监控', '系统运行监控', 'system:monitor', 'system_monitor', 'MENU', 9017, '业务管理', '/9017/9022', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9023, '系统配置', '系统参数配置', 'system:config', 'system_config', 'BUTTON', 9022, '系统监控', '/9017/9022/9023', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 财务管理模块
(9024, '财务管理', '财务管理模块入口', 'finance:admin', 'finance_admin', 'MENU', 0, '根节点', '/9024', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9025, '财务查看', '查看财务数据', 'finance:view', 'finance_view', 'BUTTON', 9024, '财务管理', '/9024/9025', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9026, '财务操作', '执行财务操作', 'finance:operate', 'finance_operate', 'BUTTON', 9024, '财务管理', '/9024/9026', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 审计管理模块
(9027, '审计管理', '审计管理模块入口', 'audit:admin', 'audit_admin', 'MENU', 0, '根节点', '/9027', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9028, '审计查看', '查看审计日志', 'audit:view', 'audit_view', 'BUTTON', 9027, '审计管理', '/9027/9028', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(9029, '审计审批', '审批审计事项', 'audit:approve', 'audit_approve', 'BUTTON', 9027, '审计管理', '/9027/9029', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 订阅管理模块
(9030, '订阅管理', '套餐订阅管理', 'subscription:manage', 'subscription_manage', 'MENU', 0, '根节点', '/9030', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);

-- ============================================================
-- 7. sys_perm_policy (30条) - 权限策略: ROLE角色权限 + USER个人权限 + TENANT能力边界/禁用示例
-- target_type: TENANT→租户ID / ROLE→角色策略ID / USER→用户策略ID
-- 体现: 租户能力边界 + 角色权限 + 用户个人权限 + 级联禁用
-- ============================================================
INSERT INTO sys_perm_policy (id, policy_code, policy_name, target_id, target_type, perm_id, table_name, table_desc, access_type, field_operates, status, disable_reason, create_by, create_at, update_by, update_at, is_deleted, deleted_at) VALUES
-- ===== TENANT 路径：租户能力边界 =====
-- 万象集团(100) 基础能力
(5001, 'PP_TENANT_001', '万象集团-租户查看能力', 100, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code","tenant_name","tenant_type","tenant_desc","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5002, 'PP_TENANT_002', '万象集团-用户查看能力', 100, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code","user_name","nick_name","email","phone"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5003, 'PP_TENANT_003', '万象集团-报表查看能力', 100, 'TENANT', 9018, 'sys_report', '报表表', 'QUERY', '["report_name","report_type","create_at"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5004, 'PP_TENANT_004', '万象集团-数据查询能力', 100, 'TENANT', 9020, 'sys_data', '数据表', 'QUERY', '["data_name","data_type","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 鼎新集团(200) 基础能力
(5005, 'PP_TENANT_005', '鼎新集团-租户查看能力', 200, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code","tenant_name","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5006, 'PP_TENANT_006', '鼎新集团-用户查看能力', 200, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code","user_name","email"]'::jsonb, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 星辰科技(300) 基础能力
(5007, 'PP_TENANT_007', '星辰科技-租户查看能力', 300, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code","tenant_name","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5008, 'PP_TENANT_008', '星辰科技-用户查看能力', 300, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code","user_name","nick_name"]'::jsonb, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 海纳百川(400) 基础能力
(5009, 'PP_TENANT_009', '海纳百川-租户查看能力', 400, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code","tenant_name","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-04-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),

-- ===== ROLE 路径：角色权限 =====
-- admin(超级管理员) → 角色策略ID=2001
(5010, 'PP_ROLE_001', '超级管理员-租户创建权限', 2001, 'ROLE', 9004, 'sys_tenant', '租户表', 'CREATE', '["tenant_code","tenant_name","tenant_type","parent_id","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5011, 'PP_ROLE_002', '超级管理员-用户创建权限', 2001, 'ROLE', 9009, 'sys_user', '用户表', 'CREATE', '["user_code","user_name","nick_name","email","phone"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5012, 'PP_ROLE_003', '超级管理员-系统配置权限', 2001, 'ROLE', 9023, 'sys_config', '系统配置表', 'UPDATE', '["config_key","config_value","description"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5013, 'PP_ROLE_004', '超级管理员-系统监控权限', 2001, 'ROLE', 9022, 'sys_monitor', '系统监控表', 'QUERY', '["cpu_usage","mem_usage","disk_usage","online_users"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- zhang_zong(租户管理员) → 角色策略ID=2002
(5014, 'PP_ROLE_005', '租户管理员-租户编辑权限', 2002, 'ROLE', 9005, 'sys_tenant', '租户表', 'UPDATE', '["tenant_name","tenant_desc","contact_name","contact_phone","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5015, 'PP_ROLE_006', '租户管理员-用户管理权限', 2002, 'ROLE', 9007, 'sys_user', '用户表', 'QUERY', '["user_code","user_name","nick_name","email","phone","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5016, 'PP_ROLE_007', '租户管理员-角色管理权限', 2002, 'ROLE', 9011, 'sys_role', '角色表', 'QUERY', '["role_name","role_code","role_level","data_scope"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- feng_san(租户管理员) → 角色策略ID=2016
(5017, 'PP_ROLE_008', '租户管理员-数据查询权限', 2016, 'ROLE', 9020, 'sys_data', '数据表', 'QUERY', '["data_name","data_type","status","create_at"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- li_jingli(华东分公司管理员) → 角色策略ID=2003
(5018, 'PP_ROLE_009', '分公司管理员-用户管理权限', 2003, 'ROLE', 9007, 'sys_user', '用户表', 'QUERY', '["user_code","user_name","email","phone"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- wang_wu(部门管理员-华南) → 角色策略ID=2007
(5019, 'PP_ROLE_010', '部门管理员-数据修改权限', 2007, 'ROLE', 9021, 'sys_data', '数据表', 'UPDATE', '["data_name","data_type","status"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- huang_audit(审计员) → 角色策略ID=2018
(5020, 'PP_ROLE_011', '审计员-审计查看权限', 2018, 'ROLE', 9028, 'sys_audit_log', '审计日志表', 'QUERY', '["operator","operation","target","result","create_at"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
(5021, 'PP_ROLE_012', '审计员-审计审批权限', 2018, 'ROLE', 9029, 'sys_audit_log', '审计日志表', 'UPDATE', '["approve_status","approve_comment"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- cai_market(营销总监) → 角色策略ID=2025
(5022, 'PP_ROLE_013', '营销总监-报表导出权限', 2025, 'ROLE', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name","report_type","report_data"]'::jsonb, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- xingchen_cto(星辰科技管理员) → 角色策略ID=2023
(5023, 'PP_ROLE_014', '星辰管理员-权限分配权限', 2023, 'ROLE', 9016, 'sys_perm_policy', '权限策略表', 'CREATE', '["policy_code","policy_name","target_type","perm_id","access_type","field_operates"]'::jsonb, 'ACTIVE', NULL, 1, '2026-03-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- dingxin_ceo(鼎新集团管理员) → 角色策略ID=2020
(5024, 'PP_ROLE_015', '鼎新管理员-用户创建权限', 2020, 'ROLE', 9009, 'sys_user', '用户表', 'CREATE', '["user_code","user_name","nick_name","email","phone"]'::jsonb, 'ACTIVE', NULL, 1, '2026-02-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- ===== USER 路径：用户个人特殊权限 =====
-- zhang_zong(用户策略ID=1002) 个人额外权限：财务管理访问
(5025, 'PP_USER_001', 'zhang_zong-财务查看额外权限', 1002, 'USER', 9025, 'sys_finance', '财务表', 'QUERY', '["amount","date","type","department"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- zhou_ba(财务专员，用户策略ID=1011) 个人额外权限：财务操作
(5026, 'PP_USER_002', 'zhou_ba-财务操作额外权限', 1011, 'USER', 9026, 'sys_finance', '财务表', 'UPDATE', '["amount","status","remark"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- multi_user(万象集团，用户策略ID=1028) 个人额外权限：报表导出
(5027, 'PP_USER_003', 'multi_user-万象-报表导出权限', 1028, 'USER', 9019, 'sys_report', '报表表', 'EXPORT', '["report_name","report_type"]'::jsonb, 'ACTIVE', NULL, 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- ===== 级联禁用示例 =====
-- 南京办事处(113)已停用，租户级查看权限被禁用
(5028, 'PP_DISABLED_001', '万象集团-南京办-租户查看(已禁用)', 113, 'TENANT', 9003, 'sys_tenant', '租户表', 'QUERY', '["tenant_code","tenant_name","status"]'::jsonb, 'DISABLED', '南京办事处已停用，上级策略级联禁用', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 西南分公司(140)已停用，用户级查看权限被禁用
(5029, 'PP_DISABLED_002', '万象集团-西南分公司-用户查看(已禁用)', 140, 'TENANT', 9008, 'sys_user', '用户表', 'QUERY', '["user_code","user_name"]'::jsonb, 'DISABLED', '西南分公司战略调整暂停运营，级联禁用', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL),
-- 天津办事处(132)已过期，权限同时过期
(5030, 'PP_EXPIRED_001', '万象集团-天津办-报表查看(已过期)', 132, 'TENANT', 9018, 'sys_report', '报表表', 'QUERY', '["report_name","report_type","create_at"]'::jsonb, 'DISABLED', '租约已过期，权限自动失效', 1, '2026-01-01 08:00:00', 1, '2026-06-01 10:00:00', 'NOT_DELETED', NULL);