package org.couchbase.quickstart.springboot.services;


import java.util.List;
import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Route;

public interface AirportService {

    Airport getAirportById(String id);
    Airport createAirport(Airport airport);
    Airport updateAirport(String id, Airport airport);
    void deleteAirport(String id);
    List<Airport> listAirports();
    List<Route> listDirectConnections(String airportCode);
}
