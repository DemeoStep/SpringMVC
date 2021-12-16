package com.oched.booksprj.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com/oched/booksprj/controllers")
                .pathsToMatch("/*")
                .build();
    }
}
