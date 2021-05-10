package com.hubert.momoservice.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  public @Bean
  OpenAPI noteAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Mobile Money Api")
                .description("Rest API documentation of the Mobile Money Service system")
                .version("v1")
        );
  }
}
