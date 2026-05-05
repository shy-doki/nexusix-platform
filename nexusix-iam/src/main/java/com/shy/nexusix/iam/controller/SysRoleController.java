package com.shy.nexusix.iam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色管理 - 控制器
 * </p>
 * <p>
 * 提供角色的CRUD、批量操作、状态切换、权限分配、用户分配等管理接口。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@RestController
@RequestMapping("/sys-role")
@Tag(name = "角色管理", description = "角色基础信息与权限用户关联管理接口")
@Validated
public class SysRoleController {

}
