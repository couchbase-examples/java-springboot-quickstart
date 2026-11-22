package org.couchbase.quickstart.springboot.repositories;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Route;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private final Scope inventoryScope;
    private final Collection routeCol;

    public RouteRepositoryImpl(Scope inventoryScope) {
        this.inventoryScope = inventoryScope;
        this.routeCol = inventoryScope.collection("route");
    }

    @Override
    public Route findById(String id) {
        return routeCol.get(id).contentAs(Route.class);
    }

    @Override
    public Route save(Route route) {
        routeCol.insert(route.getId(), route);
        return route;
    }

    @Override
    public Route update(String id, Route route) {
        routeCol.replace(id, route);
        return route;
    }

    @Override
    public void delete(String id) {
        routeCol.remove(id);
    }

    @Override
    public List<Route> findAll(int limit, int offset) {
        String statement = "SELECT route.* FROM route LIMIT $1 OFFSET $2";
        return inventoryScope
                .query(statement,
                        QueryOptions.queryOptions().parameters(JsonArray.from(limit, offset))
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Route.class);
    }
}
