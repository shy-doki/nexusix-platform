package com.shy.nexusix.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Knife4j/Swagger 配置类
 * </p>
 * <p>
 * 配置 OpenAPI 文档信息，包括接口文档标题、描述、版本、联系人等信息
 * </p>
 *
 * @author shy
 * @since 2026-04-19
 */
@Configuration
public class Knife4jConfig {

    /**
     * 配置 OpenAPI 文档信息
     *
     * @return OpenAPI 对象，包含文档元数据
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 文档标题
                        .title("NexusIX Platform API 文档")
                        // 文档版本
                        .version("1.0.0")
                        // 文档描述
                        .description("NexusIX 多租户平台接口文档，提供租户管理、用户认证、权限控制等核心功能")
                        // 服务条款链接
                        .termsOfService("http://localhost:8081")
                        // 联系人信息
                        .contact(new Contact()
                                .name("shy")
                                .email("shy@example.com")
                                .url("https://github.com/your-repo"))
                        // 许可证信息
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }

}
