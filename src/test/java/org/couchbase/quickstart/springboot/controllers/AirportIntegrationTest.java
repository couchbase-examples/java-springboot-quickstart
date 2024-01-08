package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Airport.Geo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @BeforeEach
        void setUp() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_delete");
        }

        @AfterEach
        void tearDown() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/airport_delete");
        }

        @Test
        void testGetAirport() {
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/airport_1254",
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport airport = response.getBody();
                assert airport != null;
                Airport expectedAirport = Airport.builder().id("1254").type("airport").airportname("Calais Dunkerque")
                                .city("Calais").country("France").faa("CQF").icao("LFAC").tz("Europe/Paris")
                                .geo(new Geo(12.0, 50.962097, 1.954764)).build();
                assertThat(airport).isEqualTo(expectedAirport);
        }

        @Test
        void testCreateAirport() {
                Airport airport = Airport.builder().id("airport_create").type("airport").airportname("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                ResponseEntity<Airport> response = restTemplate.postForEntity(
                                "http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Airport createdAirport = response.getBody();
                assert createdAirport != null;
                assertThat(createdAirport).isEqualTo(airport);
        }

        @Test
        void testUpdateAirport() {
                Airport airport = Airport.builder().id("airport_update").type("airport")
                                .airportname("Updated Test Airport").city("Updated Test City")
                                .country("Updated Test Country").faa("TST").icao("TEST")
                                .tz("Updated Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.put("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport);
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(),
                                                Airport.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airport updatedAirport = response.getBody();
                assert updatedAirport != null;
                assertThat(updatedAirport).isEqualTo(airport);
        }

        @Test
        void testDeleteAirport() {
                Airport airport = Airport.builder().id("airport_delete").type("airport").airportname("Test Airport")
                                .city("Test City").country("Test Country").faa("TST").icao("TEST")
                                .tz("Test Timezone").geo(new Geo(1.0, 2.0, 3.0)).build();
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport,
                                Airport.class);
                restTemplate.delete("http://localhost:" + port + "/api/v1/airport/" + airport.getId());
                ResponseEntity<Airport> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(),
                                                Airport.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirports() {
                ResponseEntity<List<Airport>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airport/list", HttpMethod.GET, null,
                                new ParameterizedTypeReference<List<Airport>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Airport> airports = response.getBody();
                assert airports != null;

                Airport airport = Airport.builder().id("1254").type("airport").airportname("Calais Dunkerque")
                                .city("Calais").country("France").faa("CQF").icao("LFAC").tz("Europe/Paris")
                                .geo(new Geo(12.0, 50.962097, 1.954764)).build();
                assertThat(airports.get(0)).isEqualTo(airport);

                assertThat(airports).hasSize(1967);
        }

        @Test
        void testListDirectConnections() {
                List<String> destinationAirportCodes = List.of("SFO", "LAX", "JFK", "MRS");
                Map<String, Integer> expectedRouteCounts = Map.of(
                                "SFO", 249,
                                "LAX", 482,
                                "JFK", 454,
                                "MRS", 128);

                for (String airportCode : destinationAirportCodes) {
                        ResponseEntity<List<String>> response = restTemplate.exchange(
                                        "http://localhost:" + port + "/api/v1/airport/direct-connections/"
                                                        + airportCode,
                                        HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                                        });
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                        List<String> routes = response.getBody();
                        assertThat(routes).isNotNull();
                        assertThat(routes).hasSize(expectedRouteCounts.get(airportCode));
                }
        }
}
