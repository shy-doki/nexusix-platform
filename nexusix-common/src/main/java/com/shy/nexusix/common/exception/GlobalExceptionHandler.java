package com.shy.nexusix.common.exception;

import cn.dev33.satoken.exception.*;
import com.shy.nexusix.common.result.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * <p>全局异常处理器，拦截Controller抛出的异常并返回统一格式的错误响应</p>
 *
 * @author shy
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param ex 业务异常
     * @return 错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse handleBusinessException(BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数验证异常
     *
     * @param ex 参数验证异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数验证异常: {}", ex.getMessage(), ex);
        // 拼接所有字段错误信息
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ApiResponse.error(400, "参数验证失败: " + errorMsg);
    }

    /**
     * 处理运行时异常
     *
     * @param ex 运行时异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException ex) {
        log.error("运行时异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(500, "系统运行时异常: " + ex.getMessage());
    }

    /**
     * 处理IO异常
     *
     * @param ex IO异常
     * @return 错误响应
     */
    @ExceptionHandler(IOException.class)
    public ApiResponse handleIOException(IOException ex) {
        log.error("IO异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(500, "IO异常: " + ex.getMessage());
    }

    /**
     * 处理SQL异常
     *
     * @param ex SQL异常
     * @return 错误响应
     */
    @ExceptionHandler(SQLException.class)
    public ApiResponse handleSQLException(SQLException ex) {
        log.error("SQL异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(500, "数据库操作异常: " + ex.getMessage());
    }

    /**
     * 处理未登录异常
     *
     * @param ex 未登录异常
     * @return 错误响应
     */
    @ExceptionHandler(NotLoginException.class)
    public ApiResponse handleNotLoginException(NotLoginException ex) {
        log.error("未登录异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(401, ex.getMessage());
    }

    /**
     * 处理缺少权限异常
     *
     * @param ex 缺少权限异常
     * @return 错误响应
     */
    @ExceptionHandler(NotPermissionException.class)
    public ApiResponse handleNotPermissionException(NotPermissionException ex) {
        log.error("缺少权限异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(403, "缺少权限：" + ex.getPermission());
    }

    /**
     * 处理缺少角色异常
     *
     * @param ex 缺少角色异常
     * @return 错误响应
     */
    @ExceptionHandler(NotRoleException.class)
    public ApiResponse handleNotRoleException(NotRoleException ex) {
        log.error("缺少角色异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(403, "缺少角色：" + ex.getRole());
    }

    /**
     * 处理二级认证校验失败异常
     *
     * @param ex 二级认证校验失败异常
     * @return 错误响应
     */
    @ExceptionHandler(NotSafeException.class)
    public ApiResponse handleNotSafeException(NotSafeException ex) {
        log.error("二级认证校验失败异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(403, "二级认证校验失败：" + ex.getService());
    }

    /**
     * 处理服务封禁异常
     *
     * @param ex 服务封禁异常
     * @return 错误响应
     */
    @ExceptionHandler(DisableServiceException.class)
    public ApiResponse handleDisableServiceException(DisableServiceException ex) {
        log.error("服务封禁异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(403, "当前账号 " + ex.getService() + " 服务已被封禁 (level=" + ex.getLevel() + ")：" + ex.getDisableTime() + "秒后解封");
    }

    /**
     * 处理通用异常（兜底）
     *
     * @param ex 通用异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(500, "系统内部错误: " + ex.getMessage());
    }

}
