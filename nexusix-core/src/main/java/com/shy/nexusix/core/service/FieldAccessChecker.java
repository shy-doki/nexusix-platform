package com.shy.nexusix.core.service;

import com.shy.nexusix.common.exception.BusinessException;
import com.shy.nexusix.core.user.UserContext;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FieldAccessChecker {

    public boolean canView(String fieldCode) {
        return check(fieldCode, "viewable");
    }

    public boolean canCreate(String fieldCode) {
        return check(fieldCode, "creatable");
    }

    public boolean canUpdate(String fieldCode) {
        return check(fieldCode, "updatable");
    }

    private boolean check(String fieldCode, String permType) {
        // 查该用户所具有的权限
        Set<String> currentUserValidPerm = UserContext.getAllValidPerms();

        if (currentUserValidPerm.isEmpty()) {
            throw new BusinessException(400, "当前用户无有效权限");
        }

        // 比对权限 用户有效权限中是否包含 指定权限编码
        return currentUserValidPerm.contains(fieldCode);
    }

}
