package com.shy.nexusix.core.context;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.constant.GlobalConstant;
import com.shy.nexusix.common.exception.BusinessException;

/**
 * <p>租户上下文工具类，封装Sa-Token会话操作，提供租户信息快捷获取方法</p>
 * @author shy
 */
public class TenantContext {

    /**
     * <p>获取当前登录用户的租户ID</p>
     * @return 当前租户ID
     * @throws BusinessException 当前会话未绑定租户时抛出
     */
    public static Long getCurrentTenantId() {
        return null;
    }

    /**
     * <p>获取当前租户名称</p>
     * @return 租户名称，未设置时返回"未知租户"
     */
    public static String getCurrentTenantName() {
        return null;
    }


}
