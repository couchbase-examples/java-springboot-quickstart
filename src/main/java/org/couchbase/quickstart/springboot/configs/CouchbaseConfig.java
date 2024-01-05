package org.couchbase.quickstart.springboot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CouchbaseConfig {

    private final DBProperties dbProp;

    public CouchbaseConfig(DBProperties dbProp) {
        this.dbProp = dbProp;
    }


    /**
     * NOTE: If connecting to Couchbase Capella, you must enable TLS.
     * <p>
     * The simplest way to enable TLS is to edit {@code application.properties}
     * and make sure the {@code spring.couchbase.bootstrap-hosts} config property
     * starts with "couchbases://" (note the final 's'), like this:
     * <pre>
     * spring.couchbase.bootstrap-hosts=couchbases://my-cluster.cloud.couchbase.com
     * </pre>
     * Alternatively, you can enable TLS by writing code to configure the cluster environment;
     * see the commented-out code in this method for an example.
     */
    @Bean(destroyMethod = "disconnect")
    public Cluster getCouchbaseCluster() {
        try {
            log.debug("Connecting to Couchbase cluster at " + dbProp.getHostName());
            return Cluster.connect(dbProp.getHostName(), dbProp.getUsername(), dbProp.getPassword());

            // Here is an alternative version that enables TLS by configuring the cluster environment.
/*      return Cluster.connect(
            dbProp.getHostName(),
            ClusterOptions.clusterOptions(dbProp.getUsername(), dbProp.getPassword())
                .environment(env -> { // Configure cluster environment properties here
                    env.securityConfig().enableTls(true);

                    // If you're connecting to Capella, the SDK already knows which certificates to trust.
                    // When using TLS with non-Capella clusters, you must tell the SDK which certificates to trust.
                    env.securityConfig().trustCertificate(
                        Paths.get("/path/to/trusted-root-certificate.pem")
                    );
                })
        );

 */

        } catch (CouchbaseException e) {
            log.error("Could not connect to Couchbase cluster at " + dbProp.getHostName());
            log.error("Please check the username (" + dbProp.getUsername() + ") and password (" + dbProp.getPassword() + ")");
            throw e;
        } catch (Exception e) {
            log.error("Could not connect to Couchbase cluster at " + dbProp.getHostName());
            throw e;
        }

    }

    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster) {

        try {
            // Creates the cluster if it does not exist yet
            if (!cluster.buckets().getAllBuckets().containsKey(dbProp.getBucketName())) {
                cluster.buckets().createBucket(
                        BucketSettings.create(dbProp.getBucketName())
                                .bucketType(BucketType.COUCHBASE)
                                .minimumDurabilityLevel(DurabilityLevel.NONE)
                                .ramQuotaMB(128));
            }
            return cluster.bucket(dbProp.getBucketName());
        } catch (Exception e) {
            log.error("Could not connect to Couchbase bucket " + dbProp.getBucketName());
            throw e;
        }
    }
}
