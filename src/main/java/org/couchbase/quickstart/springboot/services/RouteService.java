package org.couchbase.quickstart.springboot.services;

import org.couchbase.quickstart.springboot.models.Route;

import java.util.List;

public interface RouteService {

    Route getRouteById(String id);
    Route createRoute(Route route);
    Route updateRoute(String id, Route route);
    void deleteRoute(String id);
    List<Route> listRoutes();
}
