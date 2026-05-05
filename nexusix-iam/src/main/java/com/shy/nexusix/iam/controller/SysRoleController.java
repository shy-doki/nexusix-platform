package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色管理 - 控制器
 * </p>
 * <p>
 * 提供角色与权限关联管理接口，包括角色权限分配和查询。
 * </p>
 *
 * @author shy
 * @since 2026-05-05
 */
@RestController
@RequestMapping("/sys-role")
@Tag(name = "角色管理", description = "角色与权限关联管理接口")
@Validated
public class SysRoleController {

}
