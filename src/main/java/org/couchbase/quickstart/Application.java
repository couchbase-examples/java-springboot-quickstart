package org.couchbase.quickstart;

import org.couchbase.quickstart.runners.DBSetupRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, proxyBeanMethods = false)
public class Application {

    @Autowired
    private DBSetupRunner helper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
