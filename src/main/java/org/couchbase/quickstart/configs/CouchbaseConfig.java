package org.couchbase.quickstart.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

    @Value("${spring.couchbase.bootstrap-hosts}")
    private String connectionString;

    @Value("${spring.couchbase.bucket.name}")
    private String bucket;

    @Value("${spring.couchbase.bucket.user}")
    private String username;

    @Value("${spring.couchbase.bucket.password}")
    private String password;

    @Value("${spring.couchbase.bucket.collection}")
    private String collection;

    @Value("${spring.couchbase.bucket.scope}")
    private String scope;

    public String getConnectionString() {
        return connectionString;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() { return password; }

    public String getBucketName() {
        return bucket;
    }

    public String getCollection(){
        return collection;
    }

    public String getScope() {
        return scope;
    }
}
