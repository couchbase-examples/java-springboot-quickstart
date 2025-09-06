package org.couchbase.quickstart.springboot.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotEnvConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger log = LoggerFactory.getLogger(DotEnvConfiguration.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        try {
            // Load .env file if it exists
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Create a property source from .env entries
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Only add if not already set by system environment
                if (System.getenv(key) == null) {
                    envMap.put(key, value);
                    log.debug("Loaded from .env: {}", key);
                }
            });
            
            if (!envMap.isEmpty()) {
                environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envMap));
                log.info("Environment variables loaded from .env file: {}", envMap.keySet());
            }
            
        } catch (Exception e) {
            log.error("Could not load .env file", e);
        }
    }
}