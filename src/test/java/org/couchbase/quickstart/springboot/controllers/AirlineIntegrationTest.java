package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.couchbase.quickstart.springboot.models.Airline;
import org.couchbase.quickstart.springboot.services.AirlineService;
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
class AirlineIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private AirlineService airlineService;

        private void deleteAirline(String airlineId, String cleanupTiming) {
                try {
                        if (airlineService.getAirlineById(airlineId) != null) {
                                restTemplate.delete("/api/v1/airline/" + airlineId);
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException | ResourceAccessException e) {
                        log.warn("Document " + airlineId + " not present " + cleanupTiming);
                } catch (Exception e) {
                        log.debug("Cleanup: Could not delete test airline {}: {} (this is expected during test cleanup)", airlineId, e.getMessage());
                }
        }

        private void deleteTestAirlineData(String cleanupTiming) {
                deleteAirline("airline_create", cleanupTiming);
                deleteAirline("airline_update", cleanupTiming);
                deleteAirline("airline_delete", cleanupTiming);
        }

        @BeforeEach
        void setUp() {
                deleteTestAirlineData("prior to test");
        }

        @AfterEach
        void tearDown() {
                deleteTestAirlineData("after test");
        }

        @Test
        void testGetAirline() {
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("/api/v1/airline/airline_10", Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline airline = response.getBody();
                assert airline != null;
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
                                "/api/v1/airline/" + airline.getId(), airline,
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
                                .name("Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();
                restTemplate.postForEntity("/api/v1/airline/" + airline.getId(), airline, Airline.class);

                Airline updatedAirline = Airline.builder()
                                .id("airline_update")
                                .type("airline")
                                .name("Updated Test Airline")
                                .iata("TA")
                                .icao("TST")
                                .callsign("TEST")
                                .country("United States")
                                .build();

                HttpEntity<Airline> requestEntity = new HttpEntity<>(updatedAirline);
                ResponseEntity<Airline> responseEntity = restTemplate.exchange(
                                "/api/v1/airline/" + updatedAirline.getId(),
                                HttpMethod.PUT, requestEntity, Airline.class);

                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                Airline retrievedAirline = responseEntity.getBody();
                assert retrievedAirline != null;
                assertThat(retrievedAirline).isEqualTo(updatedAirline);
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
                restTemplate.postForEntity("/api/v1/airline/" + airline.getId(), airline,
                                Airline.class);
                restTemplate.delete("/api/v1/airline/" + airlineIdToDelete);
                ResponseEntity<Airline> response = restTemplate
                                .getForEntity("/api/v1/airline/" + airlineIdToDelete,
                                                Airline.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListAirlines() {
                int limit = 10;
                int offset = 0;
                ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                "/api/v1/airline/list?limit=" + limit + "&offset="
                                                + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
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
                                "/api/v1/airline/list?country=" + country + "&limit=" + limit + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
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
                                "/api/v1/airline/list?country=" + country + "&limit=" + limit + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
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
                Map<String, List<Airline>> expectedAirlinesByDestination = new HashMap<>();

                expectedAirlinesByDestination.put("SFO", Arrays.asList(
                                Airline.builder().id("3029").type("airline").name("JetBlue Airways")
                                                .iata("B6").icao("JBU").callsign("JETBLUE").country("United States")
                                                .build(),
                                Airline.builder().id("1355").type("airline").name("British Airways")
                                                .iata("BA").icao("BAW").callsign("SPEEDBIRD").country("United Kingdom")
                                                .build()));

                expectedAirlinesByDestination.put("MRS", Arrays.asList(
                                Airline.builder().id("137").type("airline").name("Air France")
                                                .iata("AF").icao("AFR").callsign("AIRFRANS").country("France").build(),
                                Airline.builder().id("24").type("airline").name("American Airlines")
                                                .iata("AA").icao("AAL").callsign("AMERICAN").country("United States")
                                                .build()));

                int limit = 10;
                int offset = 0;
                for (Map.Entry<String, List<Airline>> entry : expectedAirlinesByDestination.entrySet()) {
                        String destinationAirport = entry.getKey();
                        List<Airline> expectedAirlines = entry.getValue();

                        ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                        "/api/v1/airline/to-airport?destinationAirport=" + destinationAirport
                                                        + "&limit=" + limit + "&offset=" + offset,
                                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                                        });
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                        List<Airline> airlines = response.getBody();
                        assert airlines != null;

                        assertThat(airlines).containsAll(expectedAirlines);
                }
        }

}
