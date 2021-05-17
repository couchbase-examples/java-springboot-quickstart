package org.couchbase.quickstart.factories;

import com.couchbase.client.java.*;
import org.couchbase.quickstart.configs.ConfigDb;

public class DatabaseFactory {

    public static final Collection getCollection(Cluster cluster) {
        ConfigDb config = ConfigDb.getInstance();

        Bucket bucket = cluster.bucket(config.getBucketName());
        Scope scope = bucket.scope(config.getScope());
        Collection collection = scope.collection(config.getCollection());
        return collection;
    }

    public static final Cluster getCluster() {
        ConfigDb config = ConfigDb.getInstance();
        Cluster cluster = Cluster.connect(config.getHostName(), config.getUsername(), config.getPassword());
        return cluster;
    }
}
