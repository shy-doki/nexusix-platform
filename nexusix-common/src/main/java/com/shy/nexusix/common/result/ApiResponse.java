package com.shy.nexusix.common.result;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * API响应包装器
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
public class ApiResponse implements Serializable {

    // 序列化版本UID，用于保证序列化/反序列化的兼容性
    @Serial
    private static final long serialVersionUID = 1L;

    // 状态码
    private int code;

    // 消息
    private String msg;

    // 返回数据
    private Object data;

    // 返回时附加数据
    @JSONField(serialize = false)
    private Map<String, Object> extra;

    /**
     * 构造函数
     */
    public ApiResponse() {
    }

    /**
     * 构造函数
     */
    public ApiResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 创建成功响应
     */
    public static ApiResponse success() {
        return new ApiResponse(200, "操作成功", null);
    }

    /**
     * 创建成功响应带消息
     */
    public static ApiResponse success(String msg) {
        return new ApiResponse(200, msg, null);
    }

    /**
     * 创建成功响应带数据
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "操作成功", data);
    }

    /**
     * 创建成功响应带消息和数据
     */
    public static ApiResponse success(String msg, Object data) {
        return new ApiResponse(200, msg, data);
    }

    /**
     * 创建失败响应
     */
    public static ApiResponse error() {
        return new ApiResponse(500, "操作失败", null);
    }

    /**
     * 创建失败响应带消息
     */
    public static ApiResponse error(String msg) {
        return new ApiResponse(500, msg, null);
    }

    /**
     * 创建失败响应带状态码和消息
     */
    public static ApiResponse error(int code, String msg) {
        return new ApiResponse(code, msg, null);
    }

    /**
     * 创建失败响应带状态码、消息和数据
     */
    public static ApiResponse error(int code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }

    /**
     * 设置状态码
     */
    public ApiResponse setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 设置消息
     */
    public ApiResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置数据
     */
    public ApiResponse setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 添加附加数据
     */
    public ApiResponse set(String key, Object value) {
        if (extra == null) {
            extra = new ConcurrentHashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    /**
     * 添加多个附加数据
     */
    public ApiResponse set(Map<String, Object> map) {
        if (extra == null) {
            extra = new ConcurrentHashMap<>();
        }
        extra.putAll(map);
        return this;
    }

    /**
     * 获取附加数据
     */
    public Object get(String key) {
        return extra != null ? extra.get(key) : null;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code == 200;
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return code != 200;
    }

    /**
     * 创建未授权响应
     */
    public static ApiResponse unauth() {
        return new ApiResponse(401, "未授权", null);
    }

    /**
     * 创建禁止访问响应
     */
    public static ApiResponse forbidden() {
        return new ApiResponse(403, "禁止访问", null);
    }

    /**
     * 创建资源不存在响应
     */
    public static ApiResponse notFound() {
        return new ApiResponse(404, "资源不存在", null);
    }

    /**
     * 创建服务器错误响应
     */
    public static ApiResponse serverError() {
        return new ApiResponse(500, "服务器内部错误", null);
    }

}
