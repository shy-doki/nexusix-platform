package com.shy.nexusix.common.exception;

import lombok.Data;

/**
 * <p>
 * 业务异常类
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
public class BusinessException extends RuntimeException {

    // 错误码
    private int code;

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public BusinessException(String message) {
        this(500, message);
    }

}
