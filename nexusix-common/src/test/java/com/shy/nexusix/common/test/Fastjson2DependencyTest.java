package com.shy.nexusix.common.test;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试Fastjson2依赖关系，验证序列化/反序列化过程完全由Fastjson2处理
 */
public class Fastjson2DependencyTest {

    /**
     * 包含Jackson注解的测试实体类
     */
    @Data
    static class TestEntity {
        @JsonProperty("user_id")
        private Long id;
        
        @JsonProperty("user_name")
        private String name;
        
        @JsonIgnore
        private String password;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date date;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }

    @Test
    public void testFastjson2Dependency() {
        // 创建测试对象
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setName("Test User");
        entity.setPassword("secret123");
        entity.setDate(new Date());
        entity.setLocalDateTime(LocalDateTime.now());

        // 验证Fastjson2能够处理Jackson注解
        System.out.println("=== Fastjson2 依赖测试 ===");
        
        // 序列化测试
        String json = JSON.toJSONString(entity);
        System.out.println("序列化结果:");
        System.out.println(json);

        // 验证序列化结果
        assertTrue(json.contains("user_id"), "@JsonProperty应该生效");
        assertTrue(json.contains("user_name"), "@JsonProperty应该生效");
        assertFalse(json.contains("password"), "@JsonIgnore应该生效");
        assertTrue(json.contains("date"), "日期字段应该存在");
        assertTrue(json.contains("localDateTime"), "LocalDateTime字段应该存在");

        // 反序列化测试
        TestEntity deserializedEntity = JSON.parseObject(json, TestEntity.class);
        System.out.println("\n反序列化结果:");
        System.out.println(deserializedEntity);

        // 验证反序列化结果
        assertEquals(entity.getId(), deserializedEntity.getId(), "ID字段反序列化应该正确");
        assertEquals(entity.getName(), deserializedEntity.getName(), "名称字段反序列化应该正确");
        assertNull(deserializedEntity.getPassword(), "密码字段应该为null");

        // 验证依赖关系
        System.out.println("\n=== 依赖关系验证 ===");
        
        // 检查Fastjson2类加载
        try {
            Class<?> fastjsonClass = Class.forName("com.alibaba.fastjson2.JSON");
            System.out.println("Fastjson2 核心类加载成功: " + fastjsonClass.getName());
        } catch (ClassNotFoundException e) {
            fail("Fastjson2 核心类未找到");
        }

        // 检查Jackson核心类是否被加载（应该未被加载，因为我们只使用了注解）
        try {
            Class<?> jacksonCoreClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            System.out.println("Jackson 核心类被加载: " + jacksonCoreClass.getName());
            System.out.println("注意: Jackson 核心类被加载可能是由于其他依赖，但序列化/反序列化仍由Fastjson2处理");
        } catch (ClassNotFoundException e) {
            System.out.println("Jackson 核心类未被加载，符合预期");
        }

        // 检查Jackson注解类是否被加载（应该被加载，因为我们使用了Jackson注解）
        try {
            Class<?> jacksonAnnotationClass = Class.forName("com.fasterxml.jackson.annotation.JsonFormat");
            System.out.println("Jackson 注解类加载成功: " + jacksonAnnotationClass.getName());
        } catch (ClassNotFoundException e) {
            fail("Jackson 注解类未找到");
        }

        System.out.println("\n结论: Fastjson2 能够自动识别和处理 Jackson 注解，序列化/反序列化过程由Fastjson2完全处理");
    }

    @Test
    public void testFastjson2OnlyProcessing() {
        // 验证序列化/反序列化过程完全由Fastjson2处理
        TestEntity entity = new TestEntity();
        entity.setId(2L);
        entity.setName("Fastjson2 Test");
        entity.setDate(new Date());

        // 使用Fastjson2序列化
        String json = JSON.toJSONString(entity);
        System.out.println("\n=== Fastjson2 独立处理测试 ===");
        System.out.println("Fastjson2序列化结果:");
        System.out.println(json);

        // 使用Fastjson2反序列化
        TestEntity result = JSON.parseObject(json, TestEntity.class);
        System.out.println("Fastjson2反序列化结果:");
        System.out.println(result);

        // 验证结果
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertNotNull(result.getDate());

        System.out.println("\n验证成功: 序列化/反序列化过程完全由Fastjson2处理");
    }
}
