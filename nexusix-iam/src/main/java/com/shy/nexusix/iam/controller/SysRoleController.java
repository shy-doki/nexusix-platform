package com.shy.nexusix.iam.controller;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.service.ISysPermissionPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys-role")
@Tag(name = "角色管理", description = "角色与权限关联管理接口")
public class SysRoleController {

}
