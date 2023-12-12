package org.couchbase.quickstart.springboot.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.transactions.TransactionQueryOptions;
import com.couchbase.client.java.transactions.config.TransactionOptions;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public class RouteController {

    private Cluster cluster;
    private Collection routeCol;
    private DBProperties dbProperties;
    private Bucket bucket;

    public RouteController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing route controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.routeCol = bucket.scope("inventory").collection("route");
        this.dbProperties = dbProperties;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable String id) {
        Route createdRoute = routeCol.get(id).contentAs(Route.class);
        return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Route> createRoute(@PathVariable String id, @RequestBody Route route) {
        routeCol.insert(id, route);
        Route createdRoute = routeCol.get(id).contentAs(Route.class);
        return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable String id, @RequestBody Route route) {
        routeCol.replace(id, route);
        Route updatedRoute = routeCol.get(id).contentAs(Route.class);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeCol.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Route>> listRoutes() {
        String statement = "SELECT airport.* FROM `" + dbProperties.getBucketName() + "`.`inventory`.`route`";
        List<Route> routes = cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Route.class);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

}
