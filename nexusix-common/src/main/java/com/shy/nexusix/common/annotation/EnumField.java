package com.shy.nexusix.common.annotation;

import com.alibaba.fastjson2.annotation.JSONField;
import com.shy.nexusix.common.enums.BaseEnumDeserializer;
import com.shy.nexusix.common.enums.BaseEnumSerializer;

import java.lang.annotation.*;

/**
 * <p>
 * 枚举字段注解
 * 作用: 标记字段使用自定义的枚举序列化/反序列化器
 * </p>
 *
 * @author shy
 * @since 2026-04-19
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JSONField(
        // 反序列化器
        deserializeUsing = BaseEnumDeserializer.class,
        // 序列化器
        serializeUsing = BaseEnumSerializer.class
)
public @interface EnumField {
    // 这是一个标记注解，不需要定义任何属性
    // 它的作用是通过元注解 @JSONField 来指定序列化器
}
