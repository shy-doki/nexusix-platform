package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

/**
 * <p>
 * Fastjson2 枚举序列化器
 * </p>
 *
 * <p>
 * 作用: 将枚举对象转换为 JSON 值
 * 触发时机: 当字段标注了 @EnumField 注解时
 * </p>
 *
 * @author shy
 * @since 2026-04-19
 */
public class BaseEnumSerializer implements ObjectWriter<Object> {

    /**
     * 核心方法: 将枚举对象写入 JSON
     *
     * @param jsonWriter JSON 写入器，用于写入 JSON 内容
     * @param object     要序列化的对象 (枚举实例)
     * @param fieldName  字段名称
     * @param fieldType  字段类型
     * @param features   序列化特性标志位
     */
    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {

        if (object == null) {
            jsonWriter.writeNull();
            return;
        }

        // 强转换成 BaseEnum
        BaseEnum baseEnum = (BaseEnum) object;

        // 获取枚举的 code 值并写入 JSON
        jsonWriter.writeInt32(baseEnum.getCode());

    }

}
