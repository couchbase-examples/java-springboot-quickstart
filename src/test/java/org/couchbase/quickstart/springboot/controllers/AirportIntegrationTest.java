package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
                assertThat(airport)
                                .isEqualTo(new Airport("1254", "airport", "Calais Dunkerque", "Calais", "France", "CQF",
                                                "LFAC", "Europe/Paris", new Geo(12.0, 50.962097, 1.954764)));
        }

        @Test
        void testCreateAirport() {
                Airport airport = new Airport("airport_create", "airport", "Test Airport", "Test City", "Test Country",
                                "TST",
                                "TEST", "Test Timezone", new Geo(1.0, 2.0, 3.0));
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
                Airport airport = new Airport("airport_update", "airport", "Updated Test Airport", "Updated Test City",
                                "Updated Test Country", "TST", "TEST", "Updated Test Timezone", new Geo(1.0, 2.0, 3.0));
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
                Airport airport = new Airport("airport_delete", "airport", "Test Airport", "Test City", "Test Country",
                                "TST",
                                "TEST", "Test Timezone", new Geo(1.0, 2.0, 3.0));
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

                Airport airport = new Airport("1254", "airport", "Calais Dunkerque", "Calais", "France", "CQF", "LFAC",
                                "Europe/Paris", new Geo(12.0, 50.962097, 1.954764));
                assertThat(airports.get(0)).isEqualTo(airport);

                assertThat(airports).hasSize(1967);
        }

        // Uncomment this test and modify it similarly if you want to include it
        // @Test
        // void testListDirectConnections() {
        // ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:"
        // + port + "/api/v1/airport/direct-connections?airportCode=test", List.class);
        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // // Add more assertions as needed
        // }
}
