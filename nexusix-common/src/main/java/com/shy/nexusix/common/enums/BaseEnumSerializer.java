package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

/**
 * <p>Fastjson2枚举序列化器，将枚举对象转换为JSON值</p>
 * <p>当字段标注@EnumField注解时触发</p>
 *
 * @author shy
 */
public class BaseEnumSerializer implements ObjectWriter<Object> {

    /**
     * 将枚举对象写入JSON，输出code值
     *
     * @param jsonWriter JSON写入器
     * @param object     要序列化的枚举实例
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

        BaseEnum baseEnum = (BaseEnum) object;
        jsonWriter.writeString(baseEnum.getCode());
    }

}
