package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Route;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private final Cluster cluster;
    private final Collection routeCol;
    private final DBProperties dbProperties;

    @SuppressWarnings("unused")
    private final Bucket bucket;

    public RouteRepositoryImpl(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        this.cluster = cluster;
        this.bucket = bucket;
        this.routeCol = bucket.scope("inventory").collection("route");
        this.dbProperties = dbProperties;
    }

    public Route findById(String id) {
        return routeCol.get(id).contentAs(Route.class);
    }

    public Route save(Route route) {
        routeCol.insert(route.getId(), route);
        return route;
    }

    public Route update(String id, Route route) {
        routeCol.replace(id, route);
        return route;
    }

    public void delete(String id) {
        routeCol.remove(id);
    }

    public List<Route> findAll() {
        String statement = "SELECT route.* FROM `" + dbProperties.getBucketName() + "`.`inventory`.`route`";
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Route.class);
    }

}
