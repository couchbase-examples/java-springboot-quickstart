package org.couchbase.quickstart.springboot.repositories;

import org.couchbase.quickstart.springboot.models.Airline;

import java.util.List;

public interface AirlineRepository {
    Airline findById(String id);
    Airline save(Airline airline);
    Airline update(String id, Airline airline);
    void delete(String id);
    List<Airline> findAll();
    List<Airline> findByCountry(String country);
    List<Airline> findByDestinationAirport(String country);
}
