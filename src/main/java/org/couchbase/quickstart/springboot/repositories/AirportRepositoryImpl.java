package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Airport;

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
    private final DBProperties dbProperties;

    @SuppressWarnings("unused")
    private final Bucket bucket;

    public AirportRepositoryImpl(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        this.cluster = cluster;
        this.bucket = bucket;
        this.airportCol = bucket.scope("inventory").collection("airport");
        this.dbProperties = dbProperties;
    }

    public Airport findById(String id) {
        return airportCol.get(id).contentAs(Airport.class);
    }

    public Airport save(Airport airport) {
        airportCol.insert(airport.getId(), airport);
        return airport;
    }

    public Airport update(String id, Airport airport) {
        airportCol.replace(id, airport);
        return airport;
    }

    public void delete(String id) {
        airportCol.remove(id);
    }

    public List<Airport> findAll() {
        String statement = "SELECT airport.* FROM `" + dbProperties.getBucketName() + "`.`inventory`.`airport`";
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airport.class);
    }

    public List<Airport> findDirectConnections(String airportCode) {
        String statement = "SELECT airport.* FROM `" + dbProperties.getBucketName()
                + "`.`inventory`.`airport` as airport JOIN `" + dbProperties.getBucketName()
                + "`.`inventory`.`route` as route on route.sourceairport = airport.faa WHERE airport.faa = \""
                + airportCode + "\" and route.stops = 0";
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airport.class);

    }
}
