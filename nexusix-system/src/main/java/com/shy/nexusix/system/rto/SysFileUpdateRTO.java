package com.shy.nexusix.system.rto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shy.nexusix.common.annotation.EnumField;
import com.shy.nexusix.common.enums.GlobalEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

import static com.shy.nexusix.common.constant.RegexConstant.Code.SNOWFLAKE_ID;

/**
 * <p>
 * 文件更新请求对象
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Data
@Schema(description = "文件更新请求对象")
public class SysFileUpdateRTO {

    /**
     * <p>主键Id</p>
     * <p>TODO 后续考虑加入加解密注解 这里就不加入正则判断 因为id默认加密传输</p>
     */
    @NotNull(message = "Id不能为空")
    @Schema(description = "主键 ID (雪花算法)", example = "1987654321098765432")
    private Long id;

    /**
     * 所属租户 ID
     */
    @NotNull(message = "租户ID不能为空")
    @Schema(description = "所属租户 ID", example = "1987654321098765432")
    private Long tenantId;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Size(min = 2, max = 100, message = "租户名称必须在2-100字符之间")
    @Schema(description = "租户名称", example = "华东公司")
    private String tenantName;

    /**
     * 存储文件名（UUID 重命名）
     */
    @NotBlank(message = "存储文件名不能为空")
    @Schema(description = "存储文件名（UUID 重命名）", example = "a1b2c3d4e5f6.pdf")
    private String fileName;

    /**
     * 原始文件名
     */
    @NotBlank(message = "原始文件名不能为空")
    @Size(min = 1, max = 255, message = "原始文件名必须在1-255字符之间")
    @Schema(description = "原始文件名", example = "企业营业执照.pdf")
    private String originalName;

    /**
     * 文件存储路径
     */
    @NotBlank(message = "文件存储路径不能为空")
    @Schema(description = "文件存储路径", example = "/uploads/2026/04/report.pdf")
    private String filePath;

    /**
     * 文件访问 URL
     */
    @NotBlank(message = "文件访问URL不能为空")
    @Schema(description = "文件访问 URL", example = "https://oss.example.com/uploads/report.pdf")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @NotNull(message = "文件大小不能为空")
    @Schema(description = "文件大小 (字节)", example = "102400")
    private Long fileSize;

    /**
     * 文件类型（小写扩展名）
     */
    @NotBlank(message = "文件类型不能为空")
    @Schema(description = "文件类型（小写扩展名）", example = ".pdf")
    private String fileType;

    /**
     * MIME 类型
     */
    @NotBlank(message = "MIME类型不能为空")
    @Schema(description = "MIME 类型", example = "application/pdf")
    private String mimeType;

    /**
     * 业务类型分类
     */
    @NotBlank(message = "业务类型不能为空")
    @Size(max = 50, message = "业务类型不能超过50字符")
    @Schema(description = "业务类型分类", example = "license")
    private String bizType;

    /**
     * 上传人 ID
     */
    @NotNull(message = "上传人ID不能为空")
    @Schema(description = "上传人 ID", example = "100")
    private Long uploadBy;

    /**
     * 上传人名称
     */
    @NotBlank(message = "上传人名称不能为空")
    @Size(min = 2, max = 20, message = "上传人名称必须在2-20字符之间")
    @Schema(description = "上传人名称", example = "张三")
    private String uploadName;

    /**
     * 上传时间
     */
    @NotNull(message = "上传时间不能为空")
    @Schema(description = "上传时间", format = "date-time", example = "2026-04-07 15:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime uploadTime;

    /**
     * 逻辑删除 (0-正常 1-删除)
     * 默认为0
     * 超级管理员可以指定逻辑删除状态
     */
    @Schema(description = "逻辑删除 (0-正常 1-删除)", example = "0")
    @EnumField
    private GlobalEnum.Deleted isDeleted;

}
