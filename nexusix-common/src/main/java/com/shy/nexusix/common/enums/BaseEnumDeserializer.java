package com.shy.nexusix.common.enums;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <p>
 * Fastjson2 枚举通用反序列化器
 * </p>
 *
 * <p>
 * 作用: 将 JSON 中的值转换为枚举对象
 * 触发时机: 当字段标注了 @EnumField 注解时
 * </p>
 *
 * @author shy
 * @since 2026-04-19
 */
public class BaseEnumDeserializer implements ObjectReader<Object> {

    /**
     * 核心方法: 读取 JSON 并转换为枚举对象
     *
     * @param jsonReader JSON 读取器，用于读取 JSON 内容
     * @param fieldType  字段类型 (如 TenantStatus.class)
     * @param fieldName  字段名称 (如 "status")
     * @param features   读取特性标志位
     * @return 转换后的枚举对象
     *
     * @throws JSONException 转换失败时抛出
     */
    @Override
    public Object readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {

        // 获取枚举类
        Class<?> enumClass = getRawClass(fieldType);

        if (!BaseEnum.class.isAssignableFrom(enumClass)) {
            throw new JSONException("类型 " + enumClass.getName() + " 未实现 BaseEnum 接口");
        }

        if (jsonReader.isNull()) {
            jsonReader.readNull();
            return null;
        }

        // 获取 JSON 中的值
        Object value;
        if (jsonReader.isInt()) {
            value = jsonReader.readInt32Value();
        } else if (jsonReader.isString()) {
            value = jsonReader.readString();
        } else {
            value = jsonReader.readAny();
        }

        // 调用 parse 方法
        try {
            Method parseMethod = enumClass.getMethod("parse", Object.class);
            Object result = parseMethod.invoke(null, value);
            if (result == null) {
                throw new JSONException(
                        "无法将值 '" + value + "' 转换为枚举类型 " + enumClass.getSimpleName()
                );
            }
            return result;
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException("枚举转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 辅助方法: 从 Type 中提取原始 Class
     *
     * @param type 类型对象
     * @return Class 对象
     */
    private Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        return null;
    }

}
