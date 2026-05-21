package com.shy.nexusix.core.context;

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
        return null;
    }

    /**
     * 获取当前租户名称
     *
     * @return 租户名称，未设置时返回"未知租户"
     */
    public static String getCurrentTenantName() {
        return null;
    }


}
