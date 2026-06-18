package com.shy.nexusix.common.enums;

/**
 * <p>枚举基类接口，所有枚举类应实现此接口以提供统一的编码和描述</p>
 *
 * @author shy
 */
public interface BaseEnum {

    /**
     * 获取枚举编码
     *
     * @return 枚举编码
     */
    String getCode();

    /**
     * 获取枚举描述
     *
     * @return 枚举描述
     */
    String getDesc();
}
