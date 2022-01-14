package org.couchbase.quickstart.configs;

import org.couchbase.quickstart.controllers.ProfileController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;

@Configuration
public class Swagger {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(ProfileController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }
}