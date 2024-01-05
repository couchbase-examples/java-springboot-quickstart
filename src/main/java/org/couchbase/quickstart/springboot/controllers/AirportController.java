package org.couchbase.quickstart.springboot.controllers;

import java.util.List;

import javax.validation.Valid;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Route;
import org.couchbase.quickstart.springboot.services.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/airport")
@Slf4j
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // All errors
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String DOCUMENT_NOT_FOUND = "Document Not Found";
    private static final String DOCUMENT_EXISTS = "Document Exists";

    @Operation(summary = "Get an airport by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable String id) {
        try {
            Airport airport = airportService.getAirportById(id);
            if (airport != null) {
                return new ResponseEntity<>(airport, HttpStatus.OK);
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

    @Operation(summary = "Create an airport")
    @PostMapping("/{id}")
    public ResponseEntity<Airport> createAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        try {
            Airport newAirport = airportService.createAirport(airport);
            return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error(DOCUMENT_EXISTS + ": " + id);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update an airport")
    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        try {
            Airport updatedAirport = airportService.updateAirport(id, airport);
            if (updatedAirport != null) {
                return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
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

    @Operation(summary = "Delete an airport")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable String id) {
        try {
            airportService.deleteAirport(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException e) {
            log.error("Document not found: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airports")
    @GetMapping("/list")
    public ResponseEntity<List<Airport>> listAirports() {
        try {
            List<Airport> airports = airportService.listAirports();
            return new ResponseEntity<>(airports, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all direct connections from an airport")
    @GetMapping("/direct-connections/{airportCode}")
    public ResponseEntity<List<String>> listDirectConnections(@PathVariable String airportCode) {
        try {
            List<String> destinationAirports = airportService.listDirectConnections(airportCode).stream()
                    .map(Route::getDestinationairport).toList();
            return new ResponseEntity<>(destinationAirports, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}