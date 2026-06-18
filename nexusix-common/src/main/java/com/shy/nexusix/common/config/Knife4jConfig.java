package com.shy.nexusix.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Knife4j/Swagger配置类，配置OpenAPI文档信息</p>
 *
 * @author shy
 */
@Configuration
public class Knife4jConfig {

    /**
     * <p>配置OpenAPI文档元数据</p>
     *
     * @return OpenAPI对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NexusIX Platform API 文档")
                        .version("1.0.0")
                        .description("NexusIX 多租户平台接口文档")
                        .termsOfService("http://localhost:8081")
                        .contact(new Contact()
                                .name("shy")
                                .email("shy@example.com")
                                .url("https://github.com/your-repo"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }

}
