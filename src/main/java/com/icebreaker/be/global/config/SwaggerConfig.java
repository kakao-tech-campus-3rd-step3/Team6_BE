package com.icebreaker.be.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final List<Server> servers = List.of(
            new Server().url("http://localhost:8080").description("로컬 개발 서버"),
            new Server().url("http://3.37.95.91").description("EC2 배포 서버")
    );

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(servers)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("얼음 땡 Backend API")
                .version("v1.0.0")
                .description("얼음 땡 Backend API 문서");
    }
}
