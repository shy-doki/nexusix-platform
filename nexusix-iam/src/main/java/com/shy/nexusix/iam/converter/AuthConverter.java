package com.shy.nexusix.iam.converter;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.iam.entity.SysUser;
import com.shy.nexusix.iam.entity.SysUserToken;
import com.shy.nexusix.iam.rto.RegisterRTO;
import com.shy.nexusix.iam.vo.CurrentUserVO;
import com.shy.nexusix.iam.vo.LoginVO;
import com.shy.nexusix.iam.vo.RegisterVO;
import com.shy.nexusix.tenant.entity.SysTenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 认证服务转换器 - 负责认证场景下的对象转换
 * </p>
 * <p>
 * 主要转换场景:
 * 1. 登录成功构建 LoginVO
 * 2. 注册成功构建 RegisterVO
 * 3. 获取当前用户信息构建 CurrentUserVO
 * 4. 注册 RTO 转 SysUser 实体
 * 5. 登录成功构建 SysUserToken 记录
 * </p>
 *
 * @author shy
 * @since 2026-05-12
 */
@Mapper(componentModel = "spring")
public interface AuthConverter {

    /**
     * 构建登录结果VO
     *
     * @param user        用户实体
     * @param tenant      租户实体
     * @param token       令牌值
     * @param tokenName   令牌名称
     * @param permissions 权限列表
     * @param roles       角色列表
     * @return 登录结果VO
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.nickname")
    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "tenantName", source = "tenant.tenantName")
    LoginVO toLoginVO(SysUser user, SysTenant tenant, String token, String tokenName, List<String> permissions, List<String> roles);

    /**
     * 构建注册结果VO
     *
     * @param user 用户实体
     * @return 注册结果VO
     */
    RegisterVO toRegisterVO(SysUser user);

    /**
     * 构建当前用户信息VO
     *
     * @param userId      用户ID
     * @param userName    用户名称
     * @param tenantId    租户ID
     * @param tenantName  租户名称
     * @param permissions 权限列表
     * @param roles       角色列表
     * @return 当前用户信息VO
     */
    CurrentUserVO toCurrentUserVO(Long userId, String userName, Long tenantId, String tenantName, List<String> permissions, List<String> roles);

    /**
     * 注册RTO转用户实体（基础字段映射，特殊字段由 afterMapping 处理）
     *
     * @param registerRTO 注册请求对象
     * @return 用户实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "loginIp", ignore = true)
    @Mapping(target = "loginDate", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createByName", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateByName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    SysUser registerRTOToEntity(RegisterRTO registerRTO);

    /**
     * 注册RTO转用户实体（完整版，自动处理密码加密和默认字段）
     *
     * @param registerRTO 注册请求对象
     * @return 用户实体
     */
    default SysUser registerRTOToEntityComplete(RegisterRTO registerRTO) {
        SysUser user = registerRTOToEntity(registerRTO);
        user.setPassword(BCrypt.hashpw(registerRTO.getPassword(), BCrypt.gensalt()));
        user.setStatus(GlobalEnum.UserStatus.NORMAL.getCode());
        user.setIsDeleted(GlobalEnum.Deleted.NOT_DELETED.getCode());
        user.setCreateBy(0L);
        user.setCreateByName(registerRTO.getNickname());
        user.setUpdateBy(0L);
        user.setUpdateByName(registerRTO.getNickname());
        return user;
    }

    /**
     * 构建用户Token记录
     *
     * @param user      用户实体
     * @param tenant    租户实体
     * @param clientIp  客户端IP
     * @param loginTime 登录时间
     * @return Token记录实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.nickname")
    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "tenantName", source = "tenant.tenantName")
    @Mapping(target = "token", expression = "cn.dev33.satoken.stp.StpUtil.getTokenValue()")
    @Mapping(target = "deviceInfo", ignore = true)
    @Mapping(target = "loginIp", source = "clientIp")
    @Mapping(target = "loginTime", source = "loginTime")
    @Mapping(target = "expireTime", source = "loginTime")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    SysUserToken buildTokenRecord(SysUser user, SysTenant tenant, String clientIp, LocalDateTime loginTime);

    /**
     * 构建用户Token记录（完整版，自动处理 token、状态和过期时间）
     *
     * @param user      用户实体
     * @param tenant    租户实体
     * @param clientIp  客户端IP
     * @param loginTime 登录时间
     * @return Token记录实体
     */
    default SysUserToken buildTokenRecordComplete(SysUser user, SysTenant tenant, String clientIp, LocalDateTime loginTime) {
        SysUserToken tokenRecord = buildTokenRecord(user, tenant, clientIp, loginTime);
        tokenRecord.setToken(StpUtil.getTokenValue());
        tokenRecord.setStatus(GlobalEnum.TokenStatus.VALID.getCode());
        tokenRecord.setExpireTime(loginTime.plusSeconds(StpUtil.getTokenTimeout()));
        return tokenRecord;
    }
}
