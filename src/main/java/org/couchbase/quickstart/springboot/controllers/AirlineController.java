package org.couchbase.quickstart.springboot.controllers;

import java.net.URI;
import java.util.List;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Airline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/airline")
public class AirlineController {

    private Cluster cluster;
    private Collection airlineCol;
    private DBProperties dbProperties;
    private Bucket bucket;

    public AirlineController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing airline controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.airlineCol = bucket.scope("inventory").collection("airline");
        this.dbProperties = dbProperties;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable String id) {
        try {
            Airline airline = airlineCol.get(id).contentAs(Airline.class);
            return new ResponseEntity<>(airline, HttpStatus.OK);
        } catch (DocumentNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Airline> createAirline(@PathVariable String id, @RequestBody Airline airline) {
        airlineCol.insert(id, airline);
        Airline createdAirline = airlineCol.get(id).contentAs(Airline.class);

        return ResponseEntity.created(URI.create("/api/v1/airline/" + id)).body(createdAirline);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Airline> updateAirline(@PathVariable String id, @RequestBody Airline airline) {
        airlineCol.replace(id, airline);
        Airline updatedAirline = airlineCol.get(id).contentAs(Airline.class);
        return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        airlineCol.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Airline>> listAirlines() {
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM `"
                + dbProperties.getBucketName() + "`.`inventory`.`airline`";
        List<Airline> airlines = cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Airline.class);
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Airline>> listAirlinesByCountry(@PathVariable String country) {
        String statement = "SELECT airline.id, airline.type, airline.name, airline.iata, airline.icao, airline.callsign, airline.country FROM `"
                + dbProperties.getBucketName() + "`.`inventory`.`airline` WHERE country = '" + country + "'";
        List<Airline> airlines = cluster
                .query(statement, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS)
                        .parameters(JsonObject.create().put("country", country)))
                .rowsAs(Airline.class);
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    @GetMapping("/destination/{destinationAirport}")
    public ResponseEntity<List<Airline>> listAirlinesByDestinationAirport(@PathVariable String destinationAirport) {
        String statement = "SELECT air.callsign, air.country, air.iata, air.icao, air.id, air.name, air.type FROM (SELECT DISTINCT META(airline).id AS airlineId FROM `"
                + dbProperties.getBucketName() + "`.`inventory`.`route` JOIN `" + dbProperties.getBucketName()
                + "`.`inventory`.`airline` ON route.airlineid = META(airline).id WHERE route.destinationairport = "
                + destinationAirport + ") AS subquery JOIN `" + dbProperties.getBucketName()
                + "`.`inventory`.`airline` AS air ON META(air).id = subquery.airlineId";
        List<Airline> airlines = cluster
                .query(statement,
                        QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS)
                                .parameters(JsonObject.create().put("destinationAirport", destinationAirport)))
                .rowsAs(Airline.class);
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

}