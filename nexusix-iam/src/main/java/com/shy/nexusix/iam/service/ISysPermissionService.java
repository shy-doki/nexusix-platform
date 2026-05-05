package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.iam.rto.SysPermissionAddRTO;
import com.shy.nexusix.iam.rto.SysPermissionQueryRTO;
import com.shy.nexusix.iam.rto.SysPermissionUpdateRTO;
import com.shy.nexusix.iam.vo.SysPermissionCommonVO;
import com.shy.nexusix.iam.vo.SysPermissionDetailVO;
import com.shy.nexusix.iam.vo.SysPermissionTreeVO;

import java.util.List;

public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermissionCommonVO> queryPermissionList();

    IPage<SysPermissionCommonVO> queryPermissionPage(PageCommonRTO page);

    List<SysPermissionTreeVO> queryPermissionTreeList();

    IPage<SysPermissionCommonVO> queryPermission(SysPermissionQueryRTO queryParam);

    SysPermissionDetailVO queryPermissionDetail(String id);

    Integer addPermission(SysPermissionAddRTO addParam);

    Integer updatePermission(SysPermissionUpdateRTO updateParam);

    Integer updatePermissionStatus(String id, String status);

    Integer deletePermission(String id);

    Integer batchAddPermission(List<SysPermissionAddRTO> addParamList);

    Integer batchUpdatePermission(List<SysPermissionUpdateRTO> updateParamList);

    Integer batchDeletePermission(List<String> ids);

}
