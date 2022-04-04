package org.couchbase.quickstart.configs;

import com.couchbase.client.core.deps.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.core.error.BucketExistsException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

    @Autowired
    private DBProperties dbProp;

    /**
     * NOTE: To connect with Couchbase CAPELLA please use the commented method bellow as it requires TLS
     */
/*  @Bean
    public Cluster getCouchbaseCluster(){
        ClusterEnvironment env = ClusterEnvironment.builder()
                .securityConfig(SecurityConfig.enableTls(true)
                        .trustManagerFactory(InsecureTrustManagerFactory.INSTANCE))
                .ioConfig(IoConfig.enableDnsSrv(true))
                .build();
        return Cluster.connect(dbProp.getHostName(),
                ClusterOptions.clusterOptions(dbProp.getUsername(), dbProp.getPassword()).environment(env));
    }
*/

    /**
     * NOTE: To connect with Couchbase locally use the methode bellow
     */
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
