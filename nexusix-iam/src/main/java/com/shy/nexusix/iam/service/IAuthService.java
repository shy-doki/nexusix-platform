package com.shy.nexusix.iam.service;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;
import com.shy.nexusix.iam.rto.RegisterRTO;

import com.shy.nexusix.iam.vo.CurrentUserVO;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 认证服务接口 - 定义登录、登出、会话管理等认证相关业务方法
 * </p>
 * <p>
 * 本接口封装所有认证相关的业务逻辑，包括：
 * - 用户登录（含密码校验、租户绑定、Session初始化）
 * - 用户注册（含数据校验、密码加密、重复检查）
 * - 用户登出（含缓存清理、Token失效）
 * - 租户切换（会话更新、权限缓存刷新）
 * - 二级认证（密码二次验证）
 * - 当前用户信息获取
 * </p>
 *
 * @author shy
 * @since 2026-05-08
 */
public interface IAuthService {

    /**
     * <p>
     * 用户登录
     * </p>
     * <p>
     * 执行完整的用户登录流程：
     * 1. 校验用户名密码
     * 2. 校验用户状态
     * 3. 查询用户租户关联
     * 4. 校验租户状态
     * 5. 执行Sa-Token登录（Session写入Redis）
     * 6. 初始化租户上下文
     * 7. 预热权限缓存
     * 8. 更新登录信息
     * 9. 创建Token记录
     * </p>
     *
     * @param loginRTO 登录请求参数（用户名、密码）
     * @param request  HTTP请求对象（用于获取客户端IP）
     * @return 登录结果，包含Token、用户信息、租户信息、权限列表
     * @throws com.shy.nexusix.common.exception.BusinessException 登录失败时抛出（用户名密码错误、用户禁用、租户冻结等）
     * @author shy
     * @since 2026-05-08
     */
    LoginVO login(LoginRTO loginRTO, HttpServletRequest request);

    /**
     * <p>
     * 用户登出
     * </p>
     * <p>
     * 执行完整的用户登出流程：
     * 1. 清除用户权限/角色缓存
     * 2. 失效数据库Token记录
     * 3. 执行Sa-Token登出（清除Redis会话）
     * </p>
     *
     * @return 登出结果
     * @author shy
     * @since 2026-05-08
     */
    String logout();

    /**
     * <p>
     * 切换租户上下文
     * </p>
     * <p>
     * 执行租户切换流程：
     * 1. 校验用户是否属于目标租户
     * 2. 校验租户状态
     * 3. 更新Sa-Token Session中的租户信息
     * 4. 清除权限缓存（不同租户权限可能不同）
     * </p>
     *
     * @param tenantId 目标租户ID
     * @return 切换结果
     * @throws com.shy.nexusix.common.exception.BusinessException 切换失败时抛出（用户不属于该租户、租户冻结等）
     * @author shy
     * @since 2026-05-08
     */
    String switchTenant(Long tenantId);

    /**
     * <p>
     * 获取当前登录用户信息
     * </p>
     * <p>
     * 从Sa-Token Session和权限缓存中获取当前用户的完整信息：
     * - 用户ID、用户名
     * - 租户ID、租户名称
     * - 权限列表
     * - 角色列表
     * </p>
     *
     * @return 当前用户信息Map
     * @throws com.shy.nexusix.common.exception.BusinessException 未登录时抛出
     * @author shy
     * @since 2026-05-08
     */
    CurrentUserVO getCurrentUser();

    /**
     * <p>
     * 用户注册
     * </p>
     * <p>
     * 执行完整的用户注册流程：
     * 1. 校验两次密码输入是否一致
     * 2. 校验用户名是否已存在
     * 3. 校验邮箱是否已被注册
     * 4. 校验手机号是否已被注册
     * 5. BCrypt加密密码
     * 6. 构建用户实体并保存
     * 7. 返回注册结果（不含敏感数据）
     * </p>
     *
     * @param registerRTO 注册请求参数（用户名、密码、确认密码、昵称、邮箱、手机号）
     * @return 注册结果，包含用户ID、用户名、昵称、邮箱、手机号
     * @throws com.shy.nexusix.common.exception.BusinessException 注册失败时抛出（密码不一致、用户名已存在、邮箱已注册、手机号已注册等）
     * @author shy
     * @since 2026-05-08
     */
    RegisterVO register(RegisterRTO registerRTO);

}