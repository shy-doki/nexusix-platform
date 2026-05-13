package com.shy.nexusix.boot;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.shy.nexusix")
@MapperScan("com.shy.nexusix.**.mapper")
@EnableEncryptableProperties
public class NexusixBootApplication {

//    public static void main(String[] args) {
//
//        SpringApplication.run(NexusixBootApplication.class, args);
//
//        System.out.println("服务启动成功");
//
//        // Api文档访问地址
//        System.out.println("Api文档访问地址：http://localhost:8081/NexusIxService/doc.html#/home");
//
//        // swagger访问地址
//        System.out.println("swagger访问地址：http://localhost:8081/NexusIxService/swagger-ui/index.html");
//
//    }

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("mySecretKey"); // 你的加密密钥
        encryptor.setAlgorithm("PBEWithMD5AndDES"); // 默认算法，也可用更安全的

        String plainText = "myDatabasePassword";
        String encryptedText = encryptor.encrypt(plainText);

        System.out.println("Encrypted: " + encryptedText);
        // 输出类似: ENC(8Zk+...)
    }

}
