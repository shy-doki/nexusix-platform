package com.shy.nexusix.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.shy.nexusix")
@MapperScan("com.shy.nexusix.**.mapper")
public class NexusixBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(NexusixBootApplication.class, args);

        System.out.println("服务启动成功");

        // Api文档访问地址
        System.out.println("Api文档访问地址：http://localhost:8081/doc.html#/home");

        // swagger访问地址
        System.out.println("swagger访问地址：http://localhost:8081/swagger-ui/index.html");

    }

}
