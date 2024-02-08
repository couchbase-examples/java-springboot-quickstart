package org.couchbase.quickstart.springboot.controllers;

import java.util.List;

import jakarta.validation.Valid;

import org.couchbase.quickstart.springboot.models.Airline;
import org.couchbase.quickstart.springboot.services.AirlineService;
import org.springframework.context.annotation.Description;
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
@RequestMapping("/api/v1/airline")
@Slf4j
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // Error messages
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String DOCUMENT_NOT_FOUND = "Document Not Found";

    @GetMapping("/{id}")
    @Operation(summary = "Get an airline by ID")
    @Description(value = "Get Airline by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to retrieve a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `getAirline`")
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

    @PostMapping("/{id}")
    @Operation(summary = "Create an airline")
    @Description(value = "Create Airport with specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to create a new document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `createAirline`")
    public ResponseEntity<Airline> createAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        try {
            Airline newAirline = airlineService.createAirline(airline);
            return new ResponseEntity<>(newAirline, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error("Document already exists: " + id);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an airline")
    @Description(value = "Update Airport with specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to update a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `updateAirline`")
    public ResponseEntity<Airline> updateAirline(@PathVariable String id, @Valid @RequestBody Airline airline) {
        try {
            Airline updatedAirline = airlineService.updateAirline(id, airline);
            if (updatedAirline != null) {
                return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
            } else {
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an airline")
    @Description(value = "Delete Airport with specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to delete a document with a specified ID. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `deleteAirline`")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        try {
            airlineService.deleteAirline(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DocumentNotFoundException e) {
            log.error(DOCUMENT_NOT_FOUND + ": " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "List all airlines")
    @Description(value = "List all Airports.\n\nThis provides an example of using N1QL to query all documents of a specific type. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `listAirlines`")
    public ResponseEntity<List<Airline>> listAirlines(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            List<Airline> airlines = airlineService.listAirlines(limit, offset);
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines by country")
    @GetMapping("/country/{country}")
    @Description(value = "List all Airports by country.\n\nThis provides an example of using N1QL to query all documents of a specific type by a specific field. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `listAirlinesByCountry`")
    public ResponseEntity<List<Airline>> listAirlinesByCountry(@PathVariable String country,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            List<Airline> airlines = airlineService.listAirlinesByCountry(country, limit, offset);
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "List all airlines by destination airport")
    @GetMapping("/destination/{destinationAirport}")
    @Description(value = "List all Airports by destination airport.\n\nThis provides an example of using N1QL to query all documents of a specific type by a specific field. \n\n Code: [`controllers/AirlineController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/master/src/main/java/org/couchbase/quickstart/springboot/controllers/AirlineController.java) \n File: `AirlineController.java` \n Method: `listAirlinesByDestinationAirport`")
    public ResponseEntity<List<Airline>> listAirlinesByDestinationAirport(
            @PathVariable String destinationAirport,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            List<Airline> airlines = airlineService.listAirlinesByDestinationAirport(destinationAirport, limit, offset);
            return new ResponseEntity<>(airlines, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
