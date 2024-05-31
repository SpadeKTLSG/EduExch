package com.shop.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Admin的Swagger配置类
 *
 * @author SK
 * @date 2024/05/31
 */
@Configuration
@ComponentScan(basePackages = "com.shop.admin.controller")
public class SwaggerConfig {


    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.0")
                .info(new Info().title("Admin API文档")
                        .contact(new Contact())
                        .description("员工端API文档")
                        .version("1.0.0"));

    }
}
