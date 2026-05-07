package com.shy.nexusix.iam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shy.nexusix.common.rto.PageCommonRTO;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.rto.SysUserTokenAddRTO;
import com.shy.nexusix.iam.rto.SysUserTokenQueryRTO;
import com.shy.nexusix.iam.rto.SysUserTokenUpdateRTO;
import com.shy.nexusix.iam.vo.SysUserTokenCommonVO;
import com.shy.nexusix.iam.vo.SysUserTokenDetailVO;

import java.util.List;

/**
 * <p>
 * 用户 Token 记录表 - 用于多端登录管理和强制下线 服务类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
public interface ISysUserTokenService extends IService<SysUserToken> {

    /**
     * 查询Token列表
     *
     * @return Token通用视图对象列表
     */
    List<SysUserTokenCommonVO> queryTokenList();

    /**
     * 分页查询Token列表
     *
     * @param page 分页参数
     * @return Token通用视图对象分页
     */
    IPage<SysUserTokenCommonVO> queryTokenPage(PageCommonRTO page);

    /**
     * 条件查询Token列表
     *
     * @param queryParam 查询条件
     * @return Token通用视图对象分页
     */
    IPage<SysUserTokenCommonVO> queryToken(SysUserTokenQueryRTO queryParam);

    /**
     * 查询Token详情
     *
     * @param id Token记录ID
     * @return Token详情视图对象
     */
    SysUserTokenDetailVO queryTokenDetail(String id);

    /**
     * 新增Token
     *
     * @param addParam 新增请求对象
     * @return 影响行数
     */
    Integer addToken(SysUserTokenAddRTO addParam);

    /**
     * 修改Token
     *
     * @param updateParam 更新请求对象
     * @return 影响行数
     */
    Integer updateToken(SysUserTokenUpdateRTO updateParam);

    /**
     * 更新Token状态
     *
     * @param id     Token记录ID
     * @param status 状态值
     * @return 影响行数
     */
    Integer updateTokenStatus(String id, String status);

    /**
     * 删除Token（逻辑删除）
     *
     * @param id Token记录ID
     * @return 影响行数
     */
    Integer deleteToken(String id);

    /**
     * 批量新增Token
     *
     * @param addParamList 新增请求对象列表
     * @return 影响行数
     */
    Integer batchAddToken(List<SysUserTokenAddRTO> addParamList);

    /**
     * 批量修改Token
     *
     * @param updateParamList 更新请求对象列表
     * @return 影响行数
     */
    Integer batchUpdateToken(List<SysUserTokenUpdateRTO> updateParamList);

    /**
     * 批量更新Token状态
     *
     * @param ids    Token记录ID列表
     * @param status 状态值
     * @return 影响行数
     */
    Integer batchUpdateTokenStatus(List<String> ids, String status);

    /**
     * 批量删除Token（逻辑删除）
     *
     * @param ids Token记录ID列表
     * @return 影响行数
     */
    Integer batchDeleteToken(List<String> ids);

}
