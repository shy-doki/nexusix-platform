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

}
