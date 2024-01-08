package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Airline;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class AirlineRepositoryImpl implements AirlineRepository {

    private final Cluster cluster;
    private final Collection airlineCol;
    private final DBProperties dbProperties;

    @SuppressWarnings("unused")
    private final Bucket bucket;

    public AirlineRepositoryImpl(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        this.cluster = cluster;
        this.bucket = bucket;
        this.airlineCol = bucket.scope("inventory").collection("airline");
        this.dbProperties = dbProperties;
    }

    @Override
    public Airline findById(String id) {
        return airlineCol.get(id).contentAs(Airline.class);
    }

    @Override
    public Airline save(Airline airline) {
        airlineCol.insert(airline.getId(), airline);
        return airline;
    }

    @Override
    public Airline update(String id, Airline airline) {
        airlineCol.replace(id, airline);
        return airline;
    }

    @Override
    public void delete(String id) {
        airlineCol.remove(id);
    }

    @Override
    public List<Airline> findAll(int limit, int offset) {
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM `"
                + dbProperties.getBucketName() + "`.`inventory`.`airline` LIMIT " + limit + " OFFSET " + offset;
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
    }

    @Override
    public List<Airline> findByCountry(String country, int limit, int offset) {
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM `"
                + dbProperties.getBucketName() + "`.`inventory`.`airline` WHERE country = '" + country + "' LIMIT "
                + limit + " OFFSET " + offset;
        return cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS)
                        .parameters(JsonObject.create().put("country", country)))
                .rowsAs(Airline.class);

    }

    @Override
    public List<Airline> findByDestinationAirport(String destinationAirport, int limit, int offset) {
        String statement = "SELECT air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type " +
                "FROM (SELECT DISTINCT META(airline).id AS airlineId " +
                "      FROM `" + dbProperties.getBucketName() + "`.`inventory`.`route` " +
                "      JOIN `" + dbProperties.getBucketName() + "`.`inventory`.`airline` " +
                "      ON route.airlineid = META(airline).id " +
                "      WHERE route.destinationairport = $1) AS subquery " +
                "JOIN `" + dbProperties.getBucketName() + "`.`inventory`.`airline` AS air " +
                "ON META(air).id = subquery.airlineId LIMIT " + limit + " OFFSET " + offset;

        return cluster.query(
                statement,
                QueryOptions.queryOptions().parameters(JsonArray.from(destinationAirport))
                        .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
    }

}