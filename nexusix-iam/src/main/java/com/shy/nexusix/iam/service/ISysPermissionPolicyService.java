package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.iam.entity.SysPermissionPolicy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermissionPolicyAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionPolicyUpdateRTO;
import com.shy.nexusix.iam.rto.SysRolePermissionAssignRTO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionPolicyDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;

import java.util.List;

public interface ISysPermissionPolicyService extends IService<SysPermissionPolicy> {

    List<SysPermissionPolicyCommonVO> queryPolicyList();

    IPage<SysPermissionPolicyCommonVO> queryPolicyPage(com.shy.nexusix.common.rto.PageCommonRTO page);

    IPage<SysPermissionPolicyCommonVO> queryPolicy(SysPermissionPolicyQueryRTO queryParam);

    SysPermissionPolicyDetailVO queryPolicyDetail(String id);

    Integer addPolicy(SysPermissionPolicyAddRTO addParam);

    Integer updatePolicy(SysPermissionPolicyUpdateRTO updateParam);

    Integer deletePolicy(String id);

    Integer batchAddPolicy(List<SysPermissionPolicyAddRTO> addParamList);

    Integer batchDeletePolicy(List<String> ids);

    Integer assignRolePermission(SysRolePermissionAssignRTO assignParam);

}
