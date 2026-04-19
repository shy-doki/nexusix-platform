package com.shy.nexusix.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NexusixBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(NexusixBootApplication.class, args);

        System.out.println("服务启动成功");

    }

}
