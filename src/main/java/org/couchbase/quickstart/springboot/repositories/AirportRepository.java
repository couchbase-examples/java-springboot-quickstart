package org.couchbase.quickstart.springboot.repositories;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Route;

import java.util.List;

public interface AirportRepository {

    Airport findById(String id);

    Airport save(Airport airport);

    Airport update(String id, Airport airport);

    void delete(String id);

    List<Airport> findAll(int limit, int offset);

    List<Route> findDirectConnections(String airportCode, int limit, int offset);
}