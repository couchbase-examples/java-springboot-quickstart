package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.couchbase.quickstart.springboot.models.Airline;
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
class AirlineIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @BeforeEach
        void setUp() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_delete");
        }

        @AfterEach
        void tearDown() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/airline_delete");
        }

        @Test
        void testGetAirline() {
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airline/airline_10", Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline airline = response.getBody();
                assert airline != null;
                assertThat(airline).isEqualTo(
                                new Airline("10", "airline", "40-Mile Air", "Q5", "MLA", "MILE-AIR", "United States"));
        }

        @Test
        void testCreateAirline() {
                Airline airline = Airline.builder()
                                .id("airline_create")
                                .type("airline")
                                .name("Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                ResponseEntity<Airline> response = restTemplate.postForEntity(
                                "http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Airline createdAirline = response.getBody();

                assert createdAirline != null;
                assertThat(createdAirline).isEqualTo(airline);
        }

        @Test
        void testUpdateAirline() {
                Airline airline = Airline.builder()
                                .id("airline_update")
                                .type("airline")
                                .name("Updated Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                restTemplate.put("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline);
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(),
                                                Airline.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline updatedAirline = response.getBody();
                assertThat(updatedAirline)
                                .isNotNull()
                                .isEqualTo(airline);
        }

        @Test
        void testDeleteAirline() {
                String airlineIdToDelete = "airline_delete";
                Airline airline = Airline.builder()
                                .id(airlineIdToDelete)
                                .type("airline")
                                .name("Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                restTemplate.delete("http://localhost:" + port + "/api/v1/airline/" + airlineIdToDelete);
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/airline/" + airlineIdToDelete,
                                                Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirlines() {
                int limit = 10;
                int offset = 0;
                ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/list?limit=" + limit + "&offset="
                                                + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Airline> airlines = response.getBody();
                assert airlines != null;
                assertThat(airlines).hasSize(10);
                Airline expectedAirline = Airline.builder()
                                .id("10")
                                .type("airline")
                                .name("40-Mile Air")
                                .iata("Q5")
                                .icao("MLA")
                                .callsign("MILE-AIR")
                                .country("United States")
                                .build();
                assertThat(airlines.get(0)).isEqualTo(expectedAirline);
        }

        @Test
        void testListAirlinesByCountry() {
                String country = "United States";
                int limit = 10;
                int offset = 0;
                ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/country/" + country + "?limit=" + limit
                                                + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                List<Airline> airlines = response.getBody();
                assertThat(airlines).hasSize(10);

                Airline airline = airlines.get(0);
                Airline expectedAirline = Airline.builder()
                                .id("10")
                                .type("airline")
                                .name("40-Mile Air")
                                .iata("Q5")
                                .icao("MLA")
                                .callsign("MILE-AIR")
                                .country("United States")
                                .build();
                assertThat(airline).isEqualTo(expectedAirline);

                country = "France";
                ResponseEntity<List<Airline>> response2 = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/country/" + country + "?limit=" + limit
                                                + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
                List<Airline> airlines2 = response2.getBody();
                assertThat(airlines2).hasSize(10);

                Airline airline2 = airlines2.get(0);
                Airline expectedAirline2 = Airline.builder()
                                .id("1191")
                                .type("airline")
                                .name("Air Austral")
                                .iata("UU")
                                .icao("REU")
                                .callsign("REUNION")
                                .country("France")
                                .build();
                assertThat(airline2).isEqualTo(expectedAirline2);
        }

        @Test
        void testListAirlinesByDestinationAirport() {
                Map<String, List<Airline>> expectedAirlinesByDestination = Map.of(
                                "SFO", List.of(
                                                Airline.builder().id("3029").type("airline").name("JetBlue Airways")
                                                                .iata("B6").icao("JBU")
                                                                .callsign("JETBLUE").country("United States").build(),
                                                Airline.builder().id("1355").type("airline").name("British Airways")
                                                                .iata("BA").icao("BAW")
                                                                .callsign("SPEEDBIRD").country("United Kingdom")
                                                                .build()),
                                // Add more expected airlines for SFO
                                "MRS", List.of(
                                                Airline.builder().id("137").type("airline").name("Air France")
                                                                .iata("AF").icao("AFR")
                                                                .callsign("AIRFRANS").country("France").build(),
                                                Airline.builder().id("24").type("airline").name("American Airlines")
                                                                .iata("AA").icao("AAL").callsign("AMERICAN")
                                                                .country("United States").build()
                                // Add more expected airlines for MRS
                                )
                // Add more airports and their expected airlines as needed
                );

                int limit = 10;
                int offset = 0;
                for (Map.Entry<String, List<Airline>> entry : expectedAirlinesByDestination.entrySet()) {
                        String destinationAirport = entry.getKey();
                        List<Airline> expectedAirlines = entry.getValue();

                        ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                        "http://localhost:" + port + "/api/v1/airline/destination/" + destinationAirport
                                                        + "?limit=" + limit + "&offset=" + offset,
                                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                        });
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                        List<Airline> airlines = response.getBody();
                        assertThat(airlines).isNotNull();
                        assert airlines != null;

                        assertThat(airlines).containsAll(expectedAirlines);
                }
        }

}
