package com.shy.nexusix.iam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>系统用户控制器</p>
 *
 * @author shy
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "系统用户管理", description = "系统用户相关接口")
public class SysUserController {

}
