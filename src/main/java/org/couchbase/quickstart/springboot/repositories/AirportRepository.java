package org.couchbase.quickstart.springboot.repositories;

import org.couchbase.quickstart.springboot.models.Airport;
import java.util.List;

public interface AirportRepository {

    Airport findById(String id);

    Airport save(Airport airport);

    Airport update(String id, Airport airport);

    void delete(String id);

    List<Airport> findAll();

    List<Airport> findDirectConnections(String airportCode);

}