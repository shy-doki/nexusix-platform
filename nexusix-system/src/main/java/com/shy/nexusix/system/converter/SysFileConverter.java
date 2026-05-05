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
 * 文件信息转换器 - 负责文件实体、请求对象与视图对象之间的转换
 * </p>
 *
 * @author shy
 * @since 2026-04-07
 */
@Mapper(componentModel = "spring")
public interface SysFileConverter {

    /**
     * <p>
     * 将文件实体转换为通用视图对象
     * </p>
     *
     * @param file 文件实体
     * @return 文件通用视图对象
     * @author shy
     * @since 2026-04-07
     */
    @Named("toCommVO")
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileCommonVO toCommonVO(SysFile file);

    /**
     * <p>
     * 将文件实体转换为详情视图对象
     * </p>
     *
     * @param file 文件实体
     * @return 文件详情视图对象
     * @author shy
     * @since 2026-04-07
     */
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "intDeleteToDesc")
    SysFileDetailVO toDetailVO(SysFile file);

    /**
     * <p>
     * 将更新文件请求对象转换为文件实体
     * </p>
     *
     * @param rto 更新文件请求对象
     * @return 文件实体
     * @author shy
     * @since 2026-04-07
     */
    @Mapping(target = "isDeleted", source = "isDeleted", qualifiedByName = "deletedToCode")
    SysFile toEntityUpdate(SysFileUpdateRTO rto);

    /**
     * <p>
     * 将文件实体列表转换为通用视图对象列表
     * </p>
     *
     * @param list 文件实体列表
     * @return 文件通用视图对象列表
     * @author shy
     * @since 2026-04-07
     */
    @IterableMapping(qualifiedByName = "toCommVO")
    List<SysFileCommonVO> toVoList(List<SysFile> list);

    /**
     * <p>
     * 将文件实体分页对象转换为通用视图对象分页对象
     * </p>
     *
     * @param entityPage 文件实体分页对象
     * @return 文件通用视图对象分页对象
     * @author shy
     * @since 2026-04-07
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
     * <p>
     * 将整数删除标记码转换为删除标记描述字符串
     * </p>
     *
     * @param code 删除标记码
     * @return 删除标记描述字符串
     * @author shy
     * @since 2026-04-07
     */
    @Named("intDeleteToDesc")
    default String intDeleteToDesc(Integer code) {
        if (code == null) return null;
        Deleted deleted = GlobalEnum.Deleted.getByCode(code);
        return deleted != null ? deleted.getDesc() : null;
    }

    /**
     * <p>
     * 将删除标记枚举转换为删除标记码
     * </p>
     *
     * @param del 删除标记枚举
     * @return 删除标记码
     * @author shy
     * @since 2026-04-07
     */
    @Named("deletedToCode")
    default Integer deletedToCode(Deleted del) {
        return del != null ? del.getCode() : null;
    }

}
