package org.couchbase.quickstart.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:configprops.properties")
@ConfigurationProperties(prefix = "db")

public class ConfigProperties {

    private String hostname, username, password, bucketName;

    public String getHostName() {
        return hostname;
    }

    public void setHostName(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBucketName() { return bucketName; }

    public void setBucketName(String bucketName) { this.bucketName = bucketName; }

}
