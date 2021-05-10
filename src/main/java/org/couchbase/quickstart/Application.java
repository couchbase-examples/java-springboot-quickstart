package org.couchbase.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

//disables login since apis are open to public
@SpringBootApplication(exclude = SecurityAutoConfiguration.class, proxyBeanMethods = false)

@ConfigurationPropertiesScan("org.couchbase.quickstart.configs")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
