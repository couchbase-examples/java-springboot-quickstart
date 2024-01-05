package org.couchbase.quickstart.springboot.controllers;

import java.util.List;

import javax.validation.Valid;

import org.couchbase.quickstart.springboot.models.Airline;
import org.couchbase.quickstart.springboot.services.AirlineService;
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

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/airline")
@Slf4j
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // All errors
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String DOCUMENT_NOT_FOUND = "Document Not Found";
    private static final String DOCUMENT_EXISTS = "Document Exists";

    @Operation(summary = "Get an airline by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable String id) {
        try {
            Airline airline = airlineService.getAirlineById(id);
            if (airline != null) {
                return new ResponseEntity<>(airline, HttpStatus.OK);
            } else {
                log.error(DOCUMENT_NOT_FOUND + ": " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DocumentNotFoundException e) {
            log.error(DOCUMENT_NOT_FOUND + ": " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create an airline")
    @PostMapping("/{id}")
    public ResponseEntity<Airline> createAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        try {
            Airline newAirline = airlineService.createAirline(airline);
            return new ResponseEntity<>(newAirline, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error(DOCUMENT_EXISTS + ": " + id);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update an airline")
    @PutMapping("/{id}")
    public ResponseEntity<Airline> updateAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        try {
            Airline updatedAirline = airlineService.updateAirline(id, airline);
            if (updatedAirline != null) {
                return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DocumentNotFoundException e) {
            log.error("Document not found: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete an airline")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        try {
            airlineService.deleteAirline(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException e) {
            log.error("Document not found: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines")
    @GetMapping("/list")
    public ResponseEntity<List<Airline>> listAirlines() {
        try {
            List<Airline> airlines = airlineService.listAirlines();
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines by country")
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Airline>> listAirlinesByCountry(@PathVariable String country) {
        try {
            List<Airline> airlines = airlineService.listAirlinesByCountry(country);
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
