package org.couchbase.quickstart.springboot.services;


import java.util.List;
import org.couchbase.quickstart.springboot.models.Airport;

public interface AirportService {

    Airport getAirportById(String id);
    Airport createAirport(Airport airport);
    Airport updateAirport(String id, Airport airport);
    void deleteAirport(String id);
    List<Airport> listAirports();
    List<Airport> listDirectConnections(String airportCode);
}
