package com.shy.nexusix.iam.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件资源表 - 存储上传的文件信息
 * </p>
 *
 * @author shy
 * @since 2026-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_file")
@ApiModel(value="SysFile对象", description="文件资源表 - 存储上传的文件信息")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "所属租户ID")
    private String tenantId;

    @ApiModelProperty(value = "所属租户名称")
    private String tenantName;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "原始文件名")
    private String originalName;

    @ApiModelProperty(value = "文件存储路径")
    private String filePath;

    @ApiModelProperty(value = "文件访问URL")
    private String fileUrl;

    @ApiModelProperty(value = "文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "MIME类型")
    private String mimeType;

    @ApiModelProperty(value = "业务类型分类")
    private String bizType;

    @ApiModelProperty(value = "上传人ID")
    private String uploadBy;

    @ApiModelProperty(value = "上传人名称")
    private String uploadByName;

    @ApiModelProperty(value = "上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty(value = "更新人ID")
    private String updateBy;

    @ApiModelProperty(value = "更新人名称")
    private String updateByName;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除")
    private String isDeleted;


}
