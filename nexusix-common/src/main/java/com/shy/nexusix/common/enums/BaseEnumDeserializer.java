package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <p>Fastjson2枚举通用反序列化器，将JSON值转换为枚举对象</p>
 * <p>当字段标注@EnumField注解时触发</p>
 *
 * @author shy
 */
public class BaseEnumDeserializer implements ObjectReader<Object> {

    /**
     * 读取JSON并转换为枚举对象
     *
     * @param jsonReader JSON读取器
     * @param fieldType  字段类型
     * @param fieldName  字段名称
     * @param features   读取特性标志位
     * @return 转换后的枚举对象
     */
    @Override
    public Object readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        Class<?> enumClass = getRawClass(fieldType);

        if (!BaseEnum.class.isAssignableFrom(enumClass)) {
            throw new JSONException("类型 " + enumClass.getName() + " 未实现 BaseEnum 接口");
        }

        if (jsonReader.isNull()) {
            jsonReader.readNull();
            return null;
        }

        // 读取JSON中的值（支持整数和字符串）
        Object value;
        if (jsonReader.isInt()) {
            value = jsonReader.readInt32Value();
        } else if (jsonReader.isString()) {
            value = jsonReader.readString();
        } else {
            value = jsonReader.readAny();
        }

        // 调用枚举类的parse方法进行转换
        try {
            Method parseMethod = enumClass.getMethod("parse", Object.class);
            Object result = parseMethod.invoke(null, value);
            if (result == null) {
                throw new JSONException("无法将值 '" + value + "' 转换为枚举类型 " + enumClass.getSimpleName());
            }
            return result;
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException("枚举转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从Type中提取原始Class
     *
     * @param type 类型对象
     * @return Class对象
     */
    private Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        return null;
    }

}
