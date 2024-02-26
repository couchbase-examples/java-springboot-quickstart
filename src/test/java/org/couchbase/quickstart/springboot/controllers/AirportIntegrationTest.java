package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Airport.Geo;
import org.couchbase.quickstart.springboot.services.AirportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.couchbase.client.core.error.DocumentNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AirportService airportService;

        private void deleteAirport(String airportId, String cleanupTiming) {
                try {
                        if (airportService.getAirportById(airportId) != null) {
                                restTemplate.delete("/api/v1/airport/" + airportId);
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException | ResourceAccessException e) {
                        log.warn("Document " + airportId + " not present " + cleanupTiming);
                } catch (Exception e) {
                        log.error("Error deleting test data", e.getMessage());
                }
        }

        private void deleteTestAirportData(String cleanupTiming) {
                deleteAirport("airport_create", cleanupTiming);
                deleteAirport("airport_update", cleanupTiming);
                deleteAirport("airport_delete", cleanupTiming);
        }

        @BeforeEach
        void setUp() {
                deleteTestAirportData("prior to test");
        }

        @AfterEach
        void tearDown() {
                deleteTestAirportData("after test");
        }

        @Test
        void testGetAirport() {
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("/api/v1/airport/airport_1255",
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport airport = response.getBody();
                assert airport != null;

                Airport expectedAirport = Airport.builder().id("1255").type("airport")
                                .airportname("Peronne St Quentin").city("Peronne").country("France").faa(null)
                                .icao("LFAG").tz("Europe/Paris")
                                .geo(Geo.builder().alt(295.0).lat(49.868547).lon(3.029578).build()).build();
                assertThat(airport).isEqualTo(expectedAirport);
        }

        @Test
        void testCreateAirport() {
                Airport airport = Airport.builder().id("airport_create").type("airport").airportname("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(Geo.builder().alt(1.0).lat(2.0).lon(3.0).build()).build();
                ResponseEntity<Airport> response = restTemplate.postForEntity(
                                "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Airport createdAirport = response.getBody();
                assert createdAirport != null;
                assertThat(createdAirport).isEqualTo(airport);
        }

        @Test
        void testUpdateAirport() {

                Airport airport = Airport.builder().id("airport_update").type("airport").airportname("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(Geo.builder().alt(1.0).lat(2.0).lon(3.0).build()).build();
                restTemplate.postForEntity("/api/v1/airport/" + airport.getId(), airport, Airport.class);

                Airport updatedAirport = Airport.builder().id("airport_update").type("airport")
                                .airportname("Updated Test Airport").city("Updated Test City")
                                .country("Updated Test Country").faa("TST").icao("TEST")
                                .tz("Updated Test Timezone").geo(Geo.builder().alt(1.0).lat(2.0).lon(3.0).build())
                                .build();

                HttpEntity<Airport> requestEntity = new HttpEntity<>(updatedAirport);
                ResponseEntity<Airport> responseEntity = restTemplate.exchange(
                                "/api/v1/airport/" + updatedAirport.getId(),
                                HttpMethod.PUT, requestEntity, Airport.class);

                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport retrievedAirport = responseEntity.getBody();
                assert retrievedAirport != null;
                assertThat(retrievedAirport).isEqualTo(updatedAirport);
        }

        @Test
        void testDeleteAirport() {
                Airport airport = Airport.builder().id("airport_delete").type("airport").airportname("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(Geo.builder().alt(1.0).lat(2.0).lon(3.0).build()).build();
                restTemplate.postForEntity("/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.delete("/api/v1/airport/" + airport.getId());
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("/api/v1/airport/" + airport.getId(),
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirports() {
                int limit = 10;
                int offset = 0;

                ResponseEntity<List<Airport>> response = restTemplate.exchange(
                                "/api/v1/airport/list?limit=" + limit + "&offset="
                                                + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Airport> airports = response.getBody();
                assert airports != null;
                assertThat(airports).hasSize(limit);
        }

        @Test
        void testListDirectConnections() {
                List<String> destinationAirportCodes = Arrays.asList("LAX", "JFK", "MRS");

                Map<String, List<String>> expectedDirectConnections = new HashMap<>();

                expectedDirectConnections.put("LAX",
                                Arrays.asList("NRT", "CUN", "GDL", "HMO", "MEX", "MZT", "PVR", "SJD", "ZIH", "ZLO"));
                expectedDirectConnections.put("JFK",
                                Arrays.asList("DEL", "LHR", "EZE", "ATL", "CUN", "MEX", "EZE", "LAX", "SAN", "SEA"));
                expectedDirectConnections.put("MRS",
                                Arrays.asList("AAE", "ALG", "BJA", "BLJ", "CZL", "ORN", "QSF", "TLM", "CDG", "CMN"));

                for (String airportCode : destinationAirportCodes) {
                        int limit = 10;
                        int offset = 0;

                        ResponseEntity<List<String>> response = restTemplate.exchange(
                                        "/api/v1/airport/direct-connections?airportCode=" + airportCode + "&limit="
                                                        + limit + "&offset=" + offset,
                                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                                        });
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                        List<String> routes = response.getBody();
                        assertThat(routes).isNotNull();
                        assertThat(routes).hasSize(limit);
                        assertThat(routes).containsAll(expectedDirectConnections.get(airportCode));
                }
        }

}