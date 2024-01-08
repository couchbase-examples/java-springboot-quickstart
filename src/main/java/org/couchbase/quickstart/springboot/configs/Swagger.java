package org.couchbase.quickstart.springboot.configs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class Swagger {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage(
                        "org.couchbase.quickstart.springboot.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Quickstart in Couchbase with Spring Boot",
                "<html><body><h2>A quickstart API using Java and Spring Boot with Couchbase and travel-sample data</h2><p>We have a visual representation of the API documentation using Swagger which allows you to interact with the API's endpoints directly through the browser. It provides a clear view of the API including endpoints, HTTP methods, request parameters, and response objects.</p><p>Click on an individual endpoint to expand it and see detailed information. This includes the endpoint's description, possible response status codes, and the request parameters it accepts.</p><p><strong>Trying Out the API</strong></p><p>You can try out an API by clicking on the \"Try it out\" button next to the endpoints.</p><ul><li><strong>Parameters:</strong> If an endpoint requires parameters, Swagger UI provides input boxes for you to fill in. This could include path parameters, query strings, headers, or the body of a POST/PUT request.</li><li><strong>Execution:</strong> Once you've inputted all the necessary parameters, you can click the \"Execute\" button to make a live API call. Swagger UI will send the request to the API and display the response directly in the documentation. This includes the response code, response headers, and response body.</li></ul><p><strong>Models</strong></p><p>Swagger documents the structure of request and response bodies using models. These models define the expected data structure using JSON schema and are extremely helpful in understanding what data to send and expect.</p><p>For details on the API, please check the tutorial on the Couchbase Developer Portal: <a href=\"https://developer.couchbase.com/tutorial-quickstart-java-spring-boot\">https://developer.couchbase.com/tutorial-quickstart-java-spring-boot</a></p></body></html>",
                "v1.0",
                "Terms of service",
                new Contact("Your Name", "Your Website URL", "Your Email"),
                "License of API",
                "API License URL",
                Collections.emptyList());
    }
}
