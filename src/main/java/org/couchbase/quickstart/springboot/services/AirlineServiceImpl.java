package org.couchbase.quickstart.springboot.services;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Airline;
import org.couchbase.quickstart.springboot.repositories.AirlineRepository;
import org.springframework.stereotype.Service;

@Service
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineServiceImpl(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public Airline getAirlineById(String id) {
        return airlineRepository.findById(id);
    }

    @Override
    public Airline createAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    @Override
    public Airline updateAirline(String id, Airline airline) {
        return airlineRepository.update(id, airline);
    }

    @Override
    public void deleteAirline(String id) {
        airlineRepository.delete(id);
    }

    @Override
    public List<Airline> listAirlines() {
        return airlineRepository.findAll();
    }

    @Override
    public List<Airline> listAirlinesByCountry(String country) {
        return airlineRepository.findByCountry(country);
    }

    @Override
    public List<Airline> listAirlinesByDestinationAirport(String destinationAirport) {
        return airlineRepository.findByDestinationAirport(destinationAirport);
    }
}
