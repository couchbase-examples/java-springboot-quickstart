package org.couchbase.quickstart.springboot.configs;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.core.error.UnambiguousTimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import lombok.Getter;

@Configuration
@Getter
public class CouchbaseConfig {

    private static final Logger log = LoggerFactory.getLogger(CouchbaseConfig.class);

    @Value("#{systemEnvironment['DB_CONN_STR'] ?: '${spring.couchbase.connection-string:localhost}'}")
    private String host;

    @Value("#{systemEnvironment['DB_USERNAME'] ?: '${spring.couchbase.username:Administrator}'}")
    private String username;

    @Value("#{systemEnvironment['DB_PASSWORD'] ?: '${spring.couchbase.password:password}'}")
    private String password;

    @Value("${spring.couchbase.bucket.name:travel-sample}")
    private String bucketName;


    /**
     * NOTE: If connecting to Couchbase Capella, you must enable TLS.
     * <p>
     * The simplest way to enable TLS is to edit {@code application.properties}
     * and make sure the {@code spring.couchbase.bootstrap-hosts} config property
     * starts with "couchbases://" (note the final 's'), like this:
     *
     * <pre>
     * spring.couchbase.bootstrap-hosts=couchbases://my-cluster.cloud.couchbase.com
     * </pre>
     *
     * Alternatively, you can enable TLS by writing code to configure the cluster
     * environment;
     * see the commented-out code in this method for an example.
     */
    @Bean(destroyMethod = "disconnect")
    Cluster getCouchbaseCluster() {
        Cluster cluster = null;
        try {
            log.debug("Connecting to Couchbase cluster at " + host);
            cluster = Cluster.connect(host, username, password);
            cluster.waitUntilReady(Duration.ofSeconds(30));
            return cluster;
        } catch (UnambiguousTimeoutException e) {
            log.warn("Connection to Couchbase cluster at " + host + " timed out, but continuing with partial connectivity");
            return cluster;
        } catch (Exception e) {
            log.error(e.getClass().getName());
            log.error("Could not connect to Couchbase cluster at " + host);
            throw e;
        }

    }

    @Bean
    Bucket getCouchbaseBucket(Cluster cluster) {

        try {
            if (!cluster.buckets().getAllBuckets().containsKey(bucketName)) {
                throw new BucketNotFoundException("Bucket " + bucketName + " does not exist");
            }
            Bucket bucket = cluster.bucket(bucketName);
            bucket.waitUntilReady(Duration.ofSeconds(30));
            return bucket;
        } catch (UnambiguousTimeoutException e) {
            log.error("Connection to bucket " + bucketName + " timed out");
            throw e;
        } catch (BucketNotFoundException e) {
            log.error("Bucket " + bucketName + " does not exist");
            throw e;
        } catch (Exception e) {
            log.error(e.getClass().getName());
            log.error("Could not connect to bucket " + bucketName);
            throw e;
        }
    }
}
