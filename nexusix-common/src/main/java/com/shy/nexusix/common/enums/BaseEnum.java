package com.shy.nexusix.common.enums;

/**
 * <p>
 * 枚举基类接口
 * </p>
 * <p>
 * 所有枚举类都应实现此接口，提供获取编码和描述的方法
 * </p>
 *
 * @author shy
 * @since 2026-04-07
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
