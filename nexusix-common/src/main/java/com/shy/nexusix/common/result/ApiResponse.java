package com.shy.nexusix.common.result;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>API响应包装器，提供统一的响应格式</p>
 *
 * @author shy
 */
@Data
public class ApiResponse implements Serializable {

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

    public ApiResponse() {
    }

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     */
    public ApiResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 创建成功响应（无数据）
     *
     * @return 成功响应
     */
    public static ApiResponse success() {
        return new ApiResponse(200, "操作成功", null);
    }

    /**
     * 创建成功响应（带消息）
     *
     * @param msg 消息
     * @return 成功响应
     */
    public static ApiResponse success(String msg) {
        return new ApiResponse(200, msg, null);
    }

    /**
     * 创建成功响应（带数据）
     *
     * @param data 数据
     * @return 成功响应
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "操作成功", data);
    }

    /**
     * 创建成功响应（带消息和数据）
     *
     * @param msg  消息
     * @param data 数据
     * @return 成功响应
     */
    public static ApiResponse success(String msg, Object data) {
        return new ApiResponse(200, msg, data);
    }

    /**
     * 创建失败响应（默认500）
     *
     * @return 失败响应
     */
    public static ApiResponse error() {
        return new ApiResponse(500, "操作失败", null);
    }

    /**
     * 创建失败响应（带消息）
     *
     * @param msg 消息
     * @return 失败响应
     */
    public static ApiResponse error(String msg) {
        return new ApiResponse(500, msg, null);
    }

    /**
     * 创建失败响应（带状态码和消息）
     *
     * @param code 状态码
     * @param msg  消息
     * @return 失败响应
     */
    public static ApiResponse error(int code, String msg) {
        return new ApiResponse(code, msg, null);
    }

    /**
     * 创建失败响应（带状态码、消息和数据）
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @return 失败响应
     */
    public static ApiResponse error(int code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }

    /**
     * 设置状态码（链式调用）
     *
     * @param code 状态码
     * @return 当前实例
     */
    public ApiResponse setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 设置消息（链式调用）
     *
     * @param msg 消息
     * @return 当前实例
     */
    public ApiResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置数据（链式调用）
     *
     * @param data 数据
     * @return 当前实例
     */
    public ApiResponse setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 添加附加数据（链式调用）
     *
     * @param key   键
     * @param value 值
     * @return 当前实例
     */
    public ApiResponse set(String key, Object value) {
        if (extra == null) {
            extra = new ConcurrentHashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    /**
     * 批量添加附加数据（链式调用）
     *
     * @param map 键值对
     * @return 当前实例
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
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return extra != null ? extra.get(key) : null;
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return code == 200;
    }

    /**
     * 判断是否失败
     *
     * @return 是否失败
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
