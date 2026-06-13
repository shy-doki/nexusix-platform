package com.shy.nexusix.iam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户控制器
 *
 * @author NexusIX
 * @since 2026-06-12
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "系统用户管理", description = "系统用户相关接口")
public class SysUserController {

}
