package org.couchbase.quickstart.springboot.services;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Route;
import org.couchbase.quickstart.springboot.repositories.AirportRepository;
import org.springframework.stereotype.Service;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport getAirportById(String id) {
        return airportRepository.findById(id);
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport updateAirport(String id, Airport airport) {
        return airportRepository.update(id, airport);
    }

    public void deleteAirport(String id) {
        airportRepository.delete(id);
    }

    public List<Airport> listAirports() {
        return airportRepository.findAll();
    }

    public List<Route> listDirectConnections(String airportCode) {
        return airportRepository.findDirectConnections(airportCode);
    }

}