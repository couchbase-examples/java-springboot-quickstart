package org.couchbase.quickstart.configs;

public class ConfigDb {

    public ConfigDb(
            String hostName,
            String username,
            String password,
            String bucketName,
            String collection,
            String scope) {
       this.hostName = hostName;
       this.username = username;
       this.password = password;
       this.bucketName = bucketName;
       this.collection = collection;
       this.scope = scope;
    }

    private String hostName;
    public String getHostName() {
        return hostName;
    }

    private String username;
    public String getUsername() {
        return username;
    }

    private String password;
    public String getPassword() {
        return password;
    }

    private String bucketName;
    public String getBucketName() {
        return bucketName;
    }

    private String collection;
    public String getCollection() {
        return collection;
    }

    private String scope;
    public String getScope() {
        return scope;
    }

    public static final ConfigDb getInstance(){
        return new ConfigDb("localhost",
                "Administrator",
                "password",
                "user_profile",
                "profile",
                "_default");
    }
}
