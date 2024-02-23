package org.couchbase.quickstart.springboot.controllers;

import java.util.List;
import java.util.stream.Collectors;

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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/airport")
@Slf4j
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // Error messages
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String DOCUMENT_NOT_FOUND = "Document Not Found";

    @GetMapping("/{id}")
    @Operation(summary = "Get an airport by ID", description = "Get Airport by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to retrieve a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `getAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport found"),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Parameter(name = "id", description = "The ID of the airport to retrieve", required = true, example = "airport_1254")
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

    @PostMapping("/{id}")
    @Operation(summary = "Create an airport", description = "Create Airport by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to create a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `createAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Airport created"),
            @ApiResponse(responseCode = "409", description = "Airport already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Parameter(name = "id", description = "The ID of the airport to create", required = true, example = "airport_1254")
    public ResponseEntity<Airport> createAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        try {
            Airport newAirport = airportService.createAirport(airport);
            return new ResponseEntity<>(newAirport, HttpStatus.CREATED);
        } catch (DocumentExistsException e) {
            log.error("Document already exists: " + id);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an airport", description = "Update Airport by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to update a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `updateAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport updated"),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Parameter(name = "id", description = "The ID of the airport to update", required = true, example = "airport_1254")
    public ResponseEntity<Airport> updateAirport(@PathVariable String id, @Valid @RequestBody Airport airport) {
        try {
            Airport updatedAirport = airportService.updateAirport(id, airport);
            if (updatedAirport != null) {
                return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
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
    @Operation(summary = "Delete an airport", description = "Delete Airport by specified ID.\n\nThis provides an example of using Key Value operations in Couchbase to delete a document with a specified ID. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `deleteAirport`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Airport deleted"),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Parameter(name = "id", description = "The ID of the airport to delete", required = true, example = "airport_1254")
    public ResponseEntity<Void> deleteAirport(@PathVariable String id) {
        try {
            airportService.deleteAirport(id);
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
    @Operation(summary = "List all airports", description = "List all Airports.\n\nThis provides an example of using N1QL queries in Couchbase to retrieve all documents of a specified type. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `listAirports`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airports found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<Airport>> listAirports(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            List<Airport> airports = airportService.listAirports(limit, offset);
            return new ResponseEntity<>(airports, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/direct-connections")
    @Operation(summary = "List all direct connections from an airport", description = "List all direct connections from an airport.\n\nThis provides an example of using N1QL queries in Couchbase to retrieve all documents of a specified type. \n\n Code: [`controllers/AirportController.java`](https://github.com/couchbase-examples/java-springboot-quickstart/blob/main/src/main/java/org/couchbase/quickstart/springboot/controllers/AirportController.java) \n File: `AirportController.java` \n Method: `listDirectConnections`", tags = {
            "Airport" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Direct connections found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Parameter(name = "airportCode", description = "The code of the airport to retrieve direct connections from", required = true, example = "SFO")
    public ResponseEntity<List<String>> listDirectConnections(@RequestParam(required = true) String airportCode,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            List<String> destinationAirports = airportService.listDirectConnections(airportCode, limit, offset).stream()
                    .map(Route::getDestinationairport)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(destinationAirports, HttpStatus.OK);
        } catch (Exception e) {
            log.error(INTERNAL_SERVER_ERROR + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}