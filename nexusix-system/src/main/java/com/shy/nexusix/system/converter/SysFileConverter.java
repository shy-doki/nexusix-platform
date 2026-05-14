package com.shy.nexusix.system.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shy.nexusix.common.enums.GlobalEnum;
import com.shy.nexusix.common.enums.GlobalEnum.Deleted;
import com.shy.nexusix.system.entity.SysFile;
import com.shy.nexusix.system.rto.SysFileUpdateRTO;
import com.shy.nexusix.system.vo.SysFileCommonVO;
import com.shy.nexusix.system.vo.SysFileDetailVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * <p>
 * 文件信息转换器
 * </p>
 * <p>
 * 用于在 SysFile 实体、RTO 请求对象和 VO 视图对象之间进行转换
 * 使用 MapStruct 实现高性能的对象映射
 * </p>
 *
 * @author shy
 * @since 2026-05-13
 */
@Mapper(componentModel = "spring")
public interface SysFileConverter {

    /**
     * 转换为通用视图对象
     *
     * @param file 文件实体对象
     * @return 文件通用视图对象
     */
    @Named("toCommVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileCommonVO toCommonVO(SysFile file);

    /**
     * 转换为详情视图对象
     *
     * @param file 文件实体对象
     * @return 文件详情视图对象
     */
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileDetailVO toDetailVO(SysFile file);

    /**
     * 将更新请求对象转换为实体对象
     *
     * @param rto 文件更新请求对象
     * @return 文件实体对象
     */
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFile toEntityUpdate(SysFileUpdateRTO rto);

    /**
     * 批量转换为通用视图对象列表
     *
     * @param list 文件实体列表
     * @return 文件通用视图对象列表
     */
    @IterableMapping(qualifiedByName = "toCommVO")
    List<SysFileCommonVO> toVoList(List<SysFile> list);

    /**
     * 分页对象转换：实体分页 -> VO分页
     *
     * @param entityPage 文件实体分页对象
     * @return 文件通用视图对象分页对象
     */
    default IPage<SysFileCommonVO> toVOPage(IPage<SysFile> entityPage) {
        if (entityPage == null) {
            return null;
        }
        IPage<SysFileCommonVO> voPage = new Page<>();
        voPage.setCurrent(entityPage.getCurrent());
        voPage.setSize(entityPage.getSize());
        voPage.setTotal(entityPage.getTotal());

        List<SysFileCommonVO> voList = toVoList(entityPage.getRecords());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * 将逻辑删除标识从整数转换为描述文本
     * <p>0 -> "正常"，1 -> "已删除"</p>
     *
     * @param code 逻辑删除标识代码
     * @return 逻辑删除描述文本
     */
    @Named("intDeleteToDesc")
    default String intDeleteToDesc(String code) {
        if (code == null) return null;
        Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

}
