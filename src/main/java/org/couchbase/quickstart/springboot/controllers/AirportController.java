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
import org.couchbase.quickstart.springboot.models.Airport;
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

@RestController
@RequestMapping("/api/v1/airport")
public class AirportController {

    private Cluster cluster;
    private Collection airportCol;
    private DBProperties dbProperties;
    private Bucket bucket;

    public AirportController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing airport controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.airportCol = bucket.scope("inventory").collection("airport");
        this.dbProperties = dbProperties;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable String id) {
        Airport createdAirport = airportCol.get(id).contentAs(Airport.class);
        return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Airport> createAirport(@PathVariable String id, @RequestBody Airport airport) {
        airportCol.insert(id, airport);
        Airport createdAirport = airportCol.get(id).contentAs(Airport.class);
        return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable String id, @RequestBody Airport airport) {
        airportCol.replace(id, airport);
        Airport updatedAirport = airportCol.get(id).contentAs(Airport.class);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable String id) {
        airportCol.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Airport>> listAirports() {
        String statement = "SELECT airport.* FROM `" + dbProperties.getBucketName() + "`.`inventory`.`airport`";
        List<Airport> airports = cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airport.class);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @GetMapping("/direct-connections")
    public ResponseEntity<List<Airport>> listDirectConnections(@RequestParam String airportCode) {
        String statement = "SELECT airport.* FROM `" + dbProperties.getBucketName()
                + "`.`inventory`.`airport` as airport JOIN `" + dbProperties.getBucketName()
                + "`.`inventory`.`route` as route on route.sourceairport = airport.faa WHERE airport.faa = \""
                + airportCode + "\" and route.stops = 0";
        List<Airport> airports = cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airport.class);

        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

}