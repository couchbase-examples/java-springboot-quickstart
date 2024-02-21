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

    @Override
    public Airport getAirportById(String id) {
        return airportRepository.findById(id);
    }

    @Override
    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    @Override
    public Airport updateAirport(String id, Airport airport) {
        return airportRepository.update(id, airport);
    }

    @Override
    public void deleteAirport(String id) {
        airportRepository.delete(id);
    }

    @Override
    public List<Airport> listAirports(int limit, int offset) {
        return airportRepository.findAll(limit, offset);
    }

    @Override
    public List<Route> listDirectConnections(String airportCode, int limit, int offset) {
        return airportRepository.findDirectConnections(airportCode, limit, offset);
    }

}