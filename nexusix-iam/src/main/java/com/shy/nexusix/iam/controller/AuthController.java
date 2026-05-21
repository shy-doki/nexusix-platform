package com.shy.nexusix.iam.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.core.context.UserContext;
import com.shy.nexusix.iam.dto.UserContextDTO;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.service.IAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "认证服务相关接口")

public class AuthController {

    @Autowired
    private IAuthService iAuthService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRTO param) {
        return iAuthService.login(param);
    }

    @GetMapping("/ceshi")
    public void ceshi() {
        UserContextDTO dto = new UserContextDTO();
        // 1. 租户信息
        UserContextDTO.TenantInfo tenant = new UserContextDTO.TenantInfo();
        tenant.setTenantCode("T2024001");
        tenant.setTenantName("测试科技有限公司");
        dto.setTenantInfo(tenant);

        // 2. 权限信息
        UserContextDTO.PermInfo perm = new UserContextDTO.PermInfo();
        perm.setPerms(Arrays.asList("user:view", "user:edit", "order:view", "system:admin"));
        perm.setValidPerms(Arrays.asList("user:view", "user:edit", "order:view"));
        perm.setInvalidPerm(Arrays.asList("system:admin")); // 过期或被禁用的权限
        dto.setPermInfo(perm);

        // 3. 字段权限 - query 操作
        Map<String, UserContextDTO.EntityFieldPerm> queryMap = new HashMap<>();

        UserContextDTO.EntityFieldPerm userQuery = new UserContextDTO.EntityFieldPerm();
        userQuery.setVisibleFields(Arrays.asList("id", "username", "realName", "phone", "email", "status", "createTime"));
        userQuery.setInvisibleFields(Arrays.asList("password", "idCard", "salary", "address"));
        queryMap.put("sys_user", userQuery);

        UserContextDTO.EntityFieldPerm orderQuery = new UserContextDTO.EntityFieldPerm();
        orderQuery.setVisibleFields(Arrays.asList("id", "orderNo", "amount", "status", "createTime"));
        orderQuery.setInvisibleFields(Arrays.asList("userId", "payToken", "clientIp"));
        queryMap.put("sys_order", orderQuery);

        // 动态加一张表，不用改 DTO 代码
        UserContextDTO.EntityFieldPerm productQuery = new UserContextDTO.EntityFieldPerm();
        productQuery.setVisibleFields(Arrays.asList("id", "productName", "price"));
        productQuery.setInvisibleFields(Arrays.asList("costPrice", "supplierInfo"));
        queryMap.put("sys_product", productQuery);

        perm.setQuery(queryMap);

        // 4. 字段权限 - create 操作
        Map<String, UserContextDTO.EntityFieldPerm> createMap = new HashMap<>();

        UserContextDTO.EntityFieldPerm userCreate = new UserContextDTO.EntityFieldPerm();
        userCreate.setVisibleFields(Arrays.asList("username", "realName", "phone", "email", "status"));
        userCreate.setInvisibleFields(Arrays.asList("id", "password", "createTime", "updateTime"));
        createMap.put("sys_user", userCreate);

        UserContextDTO.EntityFieldPerm orderCreate = new UserContextDTO.EntityFieldPerm();
        orderCreate.setVisibleFields(Arrays.asList("orderNo", "amount", "status"));
        orderCreate.setInvisibleFields(Arrays.asList("id", "payTime", "finishTime"));
        createMap.put("sys_order", orderCreate);

        perm.setCreate(createMap);

        // 5. 字段权限 - update 操作
        Map<String, UserContextDTO.EntityFieldPerm> updateMap = new HashMap<>();

        UserContextDTO.EntityFieldPerm userUpdate = new UserContextDTO.EntityFieldPerm();
        userUpdate.setVisibleFields(Arrays.asList("realName", "phone", "email", "status"));
        userUpdate.setInvisibleFields(Arrays.asList("id", "username", "password", "createTime"));
        updateMap.put("sys_user", userUpdate);

        UserContextDTO.EntityFieldPerm orderUpdate = new UserContextDTO.EntityFieldPerm();
        orderUpdate.setVisibleFields(Arrays.asList("status"));
        orderUpdate.setInvisibleFields(Arrays.asList("id", "orderNo", "amount", "payToken"));
        updateMap.put("sys_order", orderUpdate);

        perm.setUpdate(updateMap);
        StpUtil.getSession().set("userContext", dto);
    }

}
