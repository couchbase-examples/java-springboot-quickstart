package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.configs.CouchbaseConfig;
import org.couchbase.quickstart.springboot.models.Route;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private final Cluster cluster;
    private final Collection routeCol;
    private final CouchbaseConfig couchbaseConfig;

    public RouteRepositoryImpl(Cluster cluster, Bucket bucket, CouchbaseConfig couchbaseConfig) {
        this.cluster = cluster;
        this.routeCol = bucket.scope("inventory").collection("route");
        this.couchbaseConfig = couchbaseConfig;
    }

    @Override
    public Route findById(String id) {
        return routeCol.get(id).contentAs(Route.class);
    }

    @Override
    public Route save(Route route) {
        routeCol.insert(route.getId(), route);
        return route;
    }

    @Override
    public Route update(String id, Route route) {
        routeCol.replace(id, route);
        return route;
    }

    @Override
    public void delete(String id) {
        routeCol.remove(id);
    }

    @Override
    public List<Route> findAll(int limit, int offset) {
        String statement = "SELECT route.* FROM `" + couchbaseConfig.getBucketName()
                + "`.`inventory`.`route` LIMIT $1 OFFSET $2";
        return cluster
                .query(statement,
                        QueryOptions.queryOptions().parameters(JsonArray.from(limit, offset))
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Route.class);
    }
}
