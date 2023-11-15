package com.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
          .title("Store API")
          .version("1.0.0")
          .description("Store API 명세서");

        SecurityScheme bearerAuth = new SecurityScheme()
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("Authorization")
          .in(SecurityScheme.In.HEADER)
          .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement addSecuriyItem = new SecurityRequirement();
        addSecuriyItem.addList("Authorization");

        return new OpenAPI()
          .components(new Components().addSecuritySchemes("Authorization", bearerAuth))
          .addSecurityItem(addSecuriyItem)
          .info(info);
    }
}
