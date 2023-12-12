package org.couchbase.quickstart.springboot.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DBProperties {

    @Value("${spring.couchbase.bootstrap-hosts}")
    private String hostName;
    @Value("${spring.couchbase.bucket.user}")
    private String username;
    @Value("${spring.couchbase.bucket.password}")
    private String password;
    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;
    @Value("${spring.couchbase.bucket.scope}")
    private String scopeName;


}
