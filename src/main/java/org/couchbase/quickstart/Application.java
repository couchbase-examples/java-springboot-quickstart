package org.couchbase.quickstart;

import org.couchbase.quickstart.helpers.DatabaseHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//disables login since apis are open to public
@SpringBootApplication(exclude = SecurityAutoConfiguration.class, proxyBeanMethods = false)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        //bootstrap database
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.createDb();
    }

}
