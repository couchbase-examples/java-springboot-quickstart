package org.couchbase.quickstart.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBProperties {

    @Value("${spring.couchbase.bootstrap-hosts}")
    private String hostName;
    @Value("${spring.couchbase.bucket.user}")
    private String username;
    @Value("${spring.couchbase.bucket.password}")
    private String password;
    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    public String getHostName() {
        return hostName;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBucketName() {
        return bucketName;
    }


}
