package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Airline;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class AirlineRepositoryImpl implements AirlineRepository {

    private final Scope inventoryScope;
    private final Collection airlineCol;

    public AirlineRepositoryImpl(Scope inventoryScope) {
        this.inventoryScope = inventoryScope;
        this.airlineCol = inventoryScope.collection("airline");
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
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM airline LIMIT $1 OFFSET $2";
        return inventoryScope
                .query(statement,
                        QueryOptions.queryOptions().parameters(JsonArray.from(limit, offset))
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
    }

    @Override
    public List<Airline> findByCountry(String country, int limit, int offset) {
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM airline WHERE country = $1 LIMIT $2 OFFSET $3";
        return inventoryScope
                .query(statement,
                        QueryOptions.queryOptions().parameters(JsonArray.from(country, limit, offset))
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
    }

    @Override
    public List<Airline> findByDestinationAirport(String destinationAirport, int limit, int offset) {
        String statement = "SELECT air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type " +
                "FROM (SELECT DISTINCT META(airline).id AS airlineId " +
                "      FROM route " +
                "      JOIN airline " +
                "      ON route.airlineid = META(airline).id " +
                "      WHERE route.destinationairport = $1) AS subquery " +
                "JOIN airline AS air " +
                "ON META(air).id = subquery.airlineId LIMIT $2 OFFSET $3";

        return inventoryScope.query(
                statement,
                QueryOptions.queryOptions().parameters(JsonArray.from(destinationAirport, limit, offset))
                        .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
    }

}