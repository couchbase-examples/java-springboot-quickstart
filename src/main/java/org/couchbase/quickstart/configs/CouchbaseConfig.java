package org.couchbase.quickstart.configs;

import com.couchbase.client.core.error.BucketExistsException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

    @Autowired
    private DBProperties dbProp;

    @Bean
    public Cluster getCouchbaseCluster(){
        return Cluster.connect(dbProp.getHostName(), dbProp.getUsername(), dbProp.getPassword());
    }

    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster){

        // Creates the cluster if it does not exist yet
        if (!cluster.buckets().getAllBuckets().containsKey(dbProp.getBucketName())) {
            cluster.buckets().createBucket(
                BucketSettings.create(dbProp.getBucketName())
                    .bucketType(BucketType.COUCHBASE)
                    .minimumDurabilityLevel(DurabilityLevel.NONE)
                    .ramQuotaMB(128));
        }
        return cluster.bucket(dbProp.getBucketName());
    }

}
