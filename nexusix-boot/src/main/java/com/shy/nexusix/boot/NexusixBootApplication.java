package com.shy.nexusix.boot;

import cn.dev33.satoken.stp.StpUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>NexusIX平台启动类</p>
 * @author shy
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.shy.nexusix")
@MapperScan("com.shy.nexusix.**.mapper")
@EnableEncryptableProperties
public class NexusixBootApplication {

    /**
     * <p>应用入口</p>
     * @param args 启动参数
     */
    public static void main(String[] args) {

        SpringApplication.run(NexusixBootApplication.class, args);

        // 启动成功提示
        System.out.println("服务启动成功");

        // Api文档地址
        System.out.println("Api文档访问地址：http://localhost:8081/NexusIxService/doc.html#/home");

        // Swagger地址
        System.out.println("swagger访问地址：http://localhost:8081/NexusIxService/swagger-ui/index.html");

    }

//    public static void main(String[] args) {
//        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setPassword("mySecretKey"); // 加密密钥
//        encryptor.setAlgorithm("PBEWithMD5AndDES"); // 加密算法
//
//        String plainText = "myDatabasePassword";
//        String encryptedText = encryptor.encrypt(plainText);
//
//        System.out.println("Encrypted: " + encryptedText);
//        // 输出加密结果，格式如: ENC(8Zk+...)
//    }

}
