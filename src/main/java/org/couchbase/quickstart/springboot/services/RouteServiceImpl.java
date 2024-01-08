package org.couchbase.quickstart.springboot.services;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Route;
import org.couchbase.quickstart.springboot.repositories.RouteRepository;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route getRouteById(String id) {
        return routeRepository.findById(id);
    }

    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    public Route updateRoute(String id, Route route) {
        return routeRepository.update(id, route);
    }

    public void deleteRoute(String id) {
        routeRepository.delete(id);
    }

    public List<Route> listRoutes(int limit, int offset) {
        return routeRepository.findAll(limit, offset);
    }

}
