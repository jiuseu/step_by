package com.example.test.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    public Info apiInfo(){
        return new Info()
                .title("SpringDoc Swagger")
                .description("SpringDoc를 이용한 Swagger ui 테스트")
                .version("1.0.0");
    }
}
