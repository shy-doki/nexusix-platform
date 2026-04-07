package com.shy.nexusix.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作日志表 - 审计用户操作行为
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_oper_log")
@Schema(name="SysOperLog对象", description="操作日志表 - 审计用户操作行为")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "租户 ID", example = "1987654321098765432")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "操作模块", example = "用户管理")
    @TableField(value = "module")
    private String module;

    @Schema(description = "业务类型", example = "1")
    @TableField(value = "business_type")
    private Integer businessType;

    @Schema(description = "请求方法", example = "com.shy.nexusix.system.controller.SysUserController.list()")
    @TableField(value = "method")
    private String method;

    @Schema(description = "请求方式 (GET/POST)", example = "POST")
    @TableField(value = "request_method")
    private String requestMethod;

    @Schema(description = "操作人员姓名", example = "张三")
    @TableField(value = "operator_name")
    private String operatorName;

    @Schema(description = "操作人员 ID", example = "100")
    @TableField(value = "operator_id")
    private Long operatorId;

    @Schema(description = "部门名称", example = "技术部")
    @TableField(value = "dept_name")
    private String deptName;

    @Schema(description = "请求 URL", example = "/system/user/list")
    @TableField(value = "oper_url")
    private String operUrl;

    @Schema(description = "操作 IP", example = "192.168.1.100")
    @TableField(value = "oper_ip")
    private String operIp;

    @Schema(description = "操作地点", example = "北京市")
    @TableField(value = "oper_location")
    private String operLocation;

    @Schema(description = "请求参数", example = "{\"pageNum\": 1, \"pageSize\": 10}")
    @TableField(value = "oper_param")
    private String operParam;

    @Schema(description = "返回结果", example = "{\"code\": 200, \"data\": [...]}")
    @TableField(value = "json_result")
    private String jsonResult;

    @Schema(description = "操作状态 (1-正常 0-失败)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "错误消息", example = "操作失败：xxx")
    @TableField(value = "error_msg")
    private String errorMsg;

    @Schema(description = "操作时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "oper_time")
    private LocalDateTime operTime;


}
