package com.zestmarket.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8085");
        localServer.setDescription("Direct Order Service");

        return new OpenAPI().servers(List.of(gatewayServer, localServer));
    }
}
