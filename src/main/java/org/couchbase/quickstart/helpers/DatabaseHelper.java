package org.couchbase.quickstart.helpers;

import com.couchbase.client.core.error.*;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;
import com.couchbase.client.java.query.QueryResult;
import org.couchbase.quickstart.configs.ConfigDb;
import org.couchbase.quickstart.configs.CouchbaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;

public class DatabaseHelper {

    @Autowired
    private CouchbaseConfig couchbaseConfig;

    @Bean
    public void createDb() {
        ConfigDb config = ConfigDb.getInstance();
         Cluster cluster = Cluster.connect(config.getHostName(), config.getUsername(), config.getPassword());
        //Cluster cluster = Cluster.connect(couchbaseConfig.getConnectionString(), couchbaseConfig.getUserName(), couchbaseConfig.getPassword());

        //create bucket - if it already exists this will just fail and we can move on
        System.out.println("*************************************************************");
        System.out.println("* Trying to create bucket - might fail if it already exists *");
        System.out.println("*************************************************************");

        try {
            cluster.buckets().createBucket(BucketSettings.create(config.getBucketName())
                    .bucketType(BucketType.COUCHBASE)
                    .ramQuotaMB(256));
        } catch (BucketExistsException e) {
            System.out.println(String.format("Bucket <%s> already exists",config.getBucketName()));
        } catch (Exception e) {
            System.out.println(String.format("Generic error <%s>",e.getMessage()));
        }

        Bucket bucket = cluster.bucket(config.getBucketName());
        CollectionManager collectionManager = bucket.collections();

        //create scope - if already exists this will just fail
        try {
            collectionManager.createScope(config.getScope());
        } catch(ScopeExistsException e) {
            System.out.println(String.format("Scope <%s> already exists",config.getScope()));
        } catch(CouchbaseException e){
            System.out.println(String.format("Generic error <%s> - this probably means default scope already exists, which is ok",config.getScope()));
        } catch (Exception e) {
            System.out.println(String.format("Generic error <%s>",e.getMessage()));
        }

        //create collection and scope - if already exists this will just fail
        try {
            CollectionSpec spec = CollectionSpec.create(config.getCollection(), config.getScope());
            collectionManager.createCollection(spec);
        } catch (CollectionExistsException e){
            System.out.println(String.format("Collection <%s> already exists",config.getCollection()));
        } catch (Exception e) {
            System.out.println(String.format("Generic error <%s>",e.getMessage()));
        }

        String createIndexQuery = String.format("CREATE INDEX profile_lower_firstName ON default:%s._default.profile(lower(`firstName`));", config.getBucketName());

        try {
            final QueryResult result = cluster.query(createIndexQuery);

            for (JsonObject row : result.rowsAsObject()){
                System.out.println(String.format("Index Creation Status %s",row.getObject("meta").getString("status")));
            }
        } catch (IndexExistsException e){
            System.out.println(String.format("Index <%s> already exists",createIndexQuery));
        } catch (Exception e){
            System.out.println(String.format("General error <%s> when trying to create index <%s>",e.getMessage(), createIndexQuery));
        }
    }
}
