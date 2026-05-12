package com.shy.nexusix.core.tenant;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>
 * 租户上下文类 - 封装Sa-Token会话操作
 * </p>
 * <p>
 * 提供获取当前登录用户ID、租户ID、用户名的快捷方法。
 * 所有数据从Sa-Token Session中读取，Session数据由sa-token-redis持久化到Redis。
 * </p>
 *
 * @author shy
 * @since 2026-05-07
 */
public class TenantContext {

    /**
     * 获取当前登录用户的租户ID
     * 数据流：StpUtil.getSession() → Redis → 返回tenantId
     *
     * @return 当前租户ID
     * @throws BusinessException 当前会话未绑定租户时抛出
     */
    public static Long getCurrentTenantId() {
        SaSession session = StpUtil.getSession(false);
        if (session == null) {
            throw new BusinessException(401, "当前会话不存在");
        }

        Object tenantId = session.get(GlobalConstant.Session.TENANT_ID);
        if (tenantId == null) {
            throw new BusinessException(401, "当前会话未绑定租户");
        }

        return parseLongSafely(tenantId);
    }

    /**
     * 获取当前租户名称
     *
     * @return 租户名称，未设置时返回"未知租户"
     */
    public static String getCurrentTenantName() {
        SaSession session = StpUtil.getSession(false);
        if (session == null) {
            return "未知租户";
        }

        Object tenantName = session.get(GlobalConstant.Session.TENANT_NAME);

        return tenantName != null ? tenantName.toString() : "未知租户";
    }

    /**
     * 安全获取租户ID（不抛异常）
     * 适用于允许匿名访问或需要默认租户的场景
     *
     * @return 当前租户ID，未登录或未绑定时返回默认值1L
     */
    public static Long getCurrentTenantIdOrDefault() {
        if (!StpUtil.isLogin()) {
            return 1L;
        }

        SaSession session = StpUtil.getSession(false);
        if (session == null) {
            return 1L;
        }

        Object tenantId = session.get(GlobalConstant.Session.TENANT_ID);
        if (tenantId == null) {
            return 1L;
        }

        return parseLongSafely(tenantId);
    }

    /**
     * 检查当前用户是否已绑定租户
     *
     * @return true-已绑定租户
     */
    public static boolean hasTenant() {
        if (!StpUtil.isLogin()) {
            throw new BusinessException(10010, "未登录");
        }

        SaSession session = StpUtil.getSession(false);
        if (session == null) {
            throw new BusinessException(401, "当前会话不存在");
        }

        return session.get(GlobalConstant.Session.TENANT_ID) != null;
    }

    /**
     * 安全解析Long值
     */
    private static Long parseLongSafely(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 1L;
        }
    }

}
