package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.configs.CouchbaseConfig;
import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Route;
import org.springframework.stereotype.Repository;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class AirportRepositoryImpl implements AirportRepository {

    private final Cluster cluster;
    private final Collection airportCol;
    private final CouchbaseConfig couchbaseConfig;

    public AirportRepositoryImpl(Cluster cluster, Bucket bucket, CouchbaseConfig couchbaseConfig) {
        this.cluster = cluster;
        this.airportCol = bucket.scope("inventory").collection("airport");
        this.couchbaseConfig = couchbaseConfig;
    }

    @Override
    public Airport findById(String id) {
        return airportCol.get(id).contentAs(Airport.class);
    }

    @Override
    public Airport save(Airport airport) {
        airportCol.insert(airport.getId(), airport);
        return airport;
    }

    @Override
    public Airport update(String id, Airport airport) {
        airportCol.replace(id, airport);
        return airport;
    }

    @Override
    public void delete(String id) {
        airportCol.remove(id);
    }

    @Override
    public List<Airport> findAll(int limit, int offset) {
        String statement = "SELECT airport.* FROM `" + couchbaseConfig.getBucketName() + "`.`inventory`.`airport` LIMIT "
                + limit + " OFFSET " + offset;
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airport.class);
    }

    @Override
    public List<Route> findDirectConnections(String airportCode, int limit, int offset) {
        String statement = "SELECT route.* FROM `" + couchbaseConfig.getBucketName()
                + "`.`inventory`.`airport` as airport JOIN `" + couchbaseConfig.getBucketName()
                + "`.`inventory`.`route` as route on route.sourceairport = airport.faa WHERE airport.faa = \""
                + airportCode + "\" and route.stops = 0 LIMIT " + limit + " OFFSET " + offset;
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Route.class);

    }
}
