package com.shy.nexusix.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通知公告表 - 系统公告与站内信
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_notice")
@Schema(name="SysNotice对象", description="通知公告表 - 系统公告与站内信")
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "公告标题", example = "系统维护通知")
    @TableField(value = "notice_title")
    private String noticeTitle;

    @Schema(description = "类型 (1-公告 2-通知)", example = "1")
    @TableField(value = "notice_type")
    private Integer noticeType;

    @Schema(description = "公告内容", example = "<p>系统将于今晚进行维护...</p>")
    @TableField(value = "notice_content")
    private String noticeContent;

    @Schema(description = "状态 (1-发布 0-下架)", example = "1")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "目标类型 (1-全员 2-指定租户 3-指定用户)", example = "1")
    @TableField(value = "target_type")
    private Integer targetType;

    @Schema(description = "目标 ID 集合 (逗号分隔)", example = "100,200,300")
    @TableField(value = "target_ids")
    private String targetIds;

    @Schema(description = "所属租户 (0 为系统公告)", example = "0")
    @TableField(value = "tenant_id")
    private Long tenantId;

    @Schema(description = "创建人 ID", example = "100")
    @TableField(value = "create_by")
    private Long createBy;

    @Schema(description = "创建时间", format = "date-time", example = "2026-04-07 15:45:30")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
