package com.shy.nexusix.tenant.converter;

import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.tenant.entity.SysTenant;
import com.shy.nexusix.tenant.vo.SysTenantCommonVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>
 * 租户实体转换器，基于 MapStruct 实现租户实体（SysTenant）与视图对象（SysTenantCommonVO）之间的类型安全转换。
 * </p>
 *
 * <p><b>设计意图：</b></p>
 * <p>本转换器遵循项目统一的转换层架构，负责将持久层实体对象转换为前端展示用的视图对象。
 * 通过 MapStruct 的编译期代码生成机制，避免运行时反射带来的性能开销，
 * 同时保证字段映射的类型安全和可追溯性。</p>
 *
 * <p><b>核心职责：</b></p>
 * <ul>
 *   <li>实体到 VO 的单对象转换（{@link #toCommonVO}）</li>
 *   <li>实体到 VO 的列表批量转换（{@link #entityListToCommonVoList}）</li>
 *   <li>字段名映射（如 createBy → createByName、createAt → createTime 等审计字段）</li>
 *   <li>状态码到描述的转换（如租户状态码 → 状态描述、删除标记码 → 删除标记描述）</li>
 * </ul>
 *
 * <p><b>字段映射说明：</b></p>
 * <table>
 *   <tr><th>实体字段</th><th>VO字段</th><th>转换方式</th></tr>
 *   <tr><td>createBy</td><td>createByName</td><td>直接映射</td></tr>
 *   <tr><td>createAt</td><td>createTime</td><td>直接映射</td></tr>
 *   <tr><td>updateBy</td><td>updateByName</td><td>直接映射</td></tr>
 *   <tr><td>updateAt</td><td>updateTime</td><td>直接映射</td></tr>
 *   <tr><td>deletedAt</td><td>deleteTime</td><td>直接映射</td></tr>
 *   <tr><td>status</td><td>status</td><td>通过 qualifiedByName = "intStatusToDesc" 转换</td></tr>
 *   <tr><td>isDeleted</td><td>isDeleted</td><td>通过 qualifiedByName = "intDeletedToDesc" 转换</td></tr>
 * </table>
 *
 * <p><b>Spring 集成：</b></p>
 * <p>通过 {@code @Mapper(componentModel = "spring")} 声明为 Spring Bean，
 * 可在服务层通过 {@code @Autowired} 注入使用。</p>
 *
 * @author shy
 * @since 2026-04-27
 * @see SysTenant
 * @see SysTenantCommonVO
 */
@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * <p>
     * 将租户实体对象转换为租户公共视图对象。
     * </p>
     *
     * <p><b>转换规则：</b></p>
     * <ul>
     *   <li>审计字段重命名：createBy→createByName、createAt→createTime、updateBy→updateByName、updateAt→updateTime、deletedAt→deleteTime</li>
     *   <li>状态字段转换：通过 {@code intStatusToDesc} 方法将状态码转换为可读的状态描述</li>
     *   <li>删除标记转换：通过 {@code intDeletedToDesc} 方法将删除标记码转换为可读的描述</li>
     * </ul>
     *
     * @param entity 租户实体对象，来源于数据库查询
     * @return 租户公共视图对象，用于前端展示
     */
    @Named("toCommonVO")
    @Mapping(source = "createBy", target = "createByName")
    @Mapping(source = "createAt", target = "createTime")
    @Mapping(source = "updateBy", target = "updateByName")
    @Mapping(source = "updateAt", target = "updateTime")
    @Mapping(source = "deletedAt", target = "deleteTime")
    @Mapping(source = "status", target = "status", qualifiedByName = "intStatusToDesc")
    @Mapping(source = "isDeleted", target = "isDeleted", qualifiedByName = "intDeletedToDesc")
    SysTenantCommonVO toCommonVO(SysTenant entity);

    /**
     * <p>
     * 将租户实体列表批量转换为租户公共视图对象列表。
     * </p>
     *
     * <p>通过 {@code @IterableMapping(qualifiedByName = "toCommonVO")} 确保列表中每个元素的转换
     * 均使用 {@link #toCommonVO} 方法的映射规则，保持单对象与批量转换的一致性。</p>
     *
     * @param entityList 租户实体对象列表
     * @return 租户公共视图对象列表，与输入列表一一对应
     */
    @IterableMapping(qualifiedByName  = "toCommonVO")
    List<SysTenantCommonVO> entityListToCommonVoList(List<SysTenant> entityList);

    /**
     * <p>
     * 将租户状态枚举转换为描述字符串。
     * </p>
     *
     * <p>用于将 {@link GlobalEnum.TenantStatus} 枚举直接转换为用户可读的状态描述，
     * 如 ENABLED → "已启用"、DISABLED → "已停用"、EXPIRED → "已过期"。</p>
     *
     * @param status 租户状态枚举
     * @return 状态描述字符串，若枚举为 null 则返回 null
     */
    @Named("statusToDesc")
    default String statusToDesc(GlobalEnum.TenantStatus status) {
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>
     * 将租户状态枚举转换为状态码。
     * </p>
     *
     * <p>用于将 {@link GlobalEnum.TenantStatus} 枚举转换为存储在数据库中的状态码字符串，
     * 如 ENABLED → "ENABLED"、DISABLED → "DISABLED"。</p>
     *
     * @param status 租户状态枚举
     * @return 状态码字符串，若枚举为 null 则返回 null
     */
    @Named("statusToCode")
    default String statusToCode(GlobalEnum.TenantStatus status) {
        return status != null ? status.getCode() : null;
    }

    /**
     * <p>
     * 将整数状态码转换为状态描述字符串。
     * </p>
     *
     * <p>此方法用于 MapStruct 映射中的 qualifiedByName 引用，在实体到 VO 的转换过程中
     * 将数据库中存储的状态码（如 "ENABLED"）转换为前端展示的状态描述（如 "已启用"）。</p>
     *
     * <p><b>调用链：</b>code → {@link GlobalEnum.TenantStatus#getByCode(String)} → desc</p>
     *
     * @param code 状态码字符串，来源于实体字段
     * @return 状态描述字符串，若 code 为 null 或无法匹配则返回 null
     */
    @Named("intStatusToDesc")
    default String intStatusToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.TenantStatus status = GlobalEnum.TenantStatus.getByCode(code);
        return status != null ? status.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记枚举转换为描述字符串。
     * </p>
     *
     * <p>用于将 {@link GlobalEnum.Deleted} 枚举直接转换为用户可读的删除标记描述，
     * 如 NOT_DELETED → "未删除"、DELETED → "已删除"。</p>
     *
     * @param del 删除标记枚举
     * @return 删除标记描述字符串，若枚举为 null 则返回 null
     */
    @Named("isDeletedToDesc")
    default String isDeletedToDesc(GlobalEnum.Deleted del) {
        return del != null ? del.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记枚举转换为删除标记码。
     * </p>
     *
     * <p>用于将 {@link GlobalEnum.Deleted} 枚举转换为存储在数据库中的标记码字符串，
     * 如 NOT_DELETED → "NOT_DELETED"、DELETED → "DELETED"。</p>
     *
     * @param del 删除标记枚举
     * @return 删除标记码字符串，若枚举为 null 则返回 null
     */
    @Named("isDeletedToCode")
    default String isDeletedToCode(GlobalEnum.Deleted del) {
        return del != null ? del.getCode() : null;
    }

    /**
     * <p>
     * 将整数删除标记码转换为删除标记描述字符串。
     * </p>
     *
     * <p>此方法用于 MapStruct 映射中的 qualifiedByName 引用，在实体到 VO 的转换过程中
     * 将数据库中存储的删除标记码（如 "NOT_DELETED"）转换为前端展示的描述（如 "未删除"）。</p>
     *
     * <p><b>调用链：</b>code → {@link GlobalEnum.Deleted#getByCode(String)} → desc</p>
     *
     * @param code 删除标记码字符串，来源于实体字段
     * @return 删除标记描述字符串，若 code 为 null 或无法匹配则返回 null
     */
    @Named("intDeletedToDesc")
    default String intDeletedToDesc(String code) {
        if (code == null) return null;
        GlobalEnum.Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }


}
