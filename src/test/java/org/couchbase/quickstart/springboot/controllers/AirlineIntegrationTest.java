package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

        // {"id":10,"type":"airline","name":"40-Mile
        // Air","iata":"Q5","icao":"MLA","callsign":"MILE-AIR","country":"United
        // States"}

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
                Airline airline = new Airline("airline_create", "airline", "Test Airline", "TA", "TST", "TEST",
                                "United States");
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
                Airline airline = new Airline("airline_update", "airline", "Updated Test Airline", "TA", "TST", "TEST",
                                "United States");
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
                Airline airline = new Airline(airlineIdToDelete, "airline", "Test Airline", "TA", "TST", "TEST",
                                "United States");
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
                ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/list", HttpMethod.GET, null,
                                new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Airline> airlines = response.getBody();
                assert airlines != null;
                assertThat(airlines).hasSize(187);
                assertThat(airlines.get(0)).isEqualTo(
                                new Airline("10", "airline", "40-Mile Air", "Q5", "MLA", "MILE-AIR", "United States"));
        }

        @Test
        void testListAirlinesByCountry() {
                // using equals method
                String country = "United States";
                ResponseEntity<List<Airline>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/country/" + country,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                List<Airline> airlines = response.getBody();
                assertThat(airlines).hasSize(127);

                Airline airline = airlines.get(0);
                assertThat(airline).isEqualTo(
                                new Airline("10", "airline", "40-Mile Air", "Q5", "MLA", "MILE-AIR", "United States"));

                // using contains method
                country = "France";
                ResponseEntity<List<Airline>> response2 = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/airline/country/" + country,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                                });
                assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
                List<Airline> airlines2 = response2.getBody();
                assertThat(airlines2).hasSize(21);

                Airline airline2 = airlines2.get(0);
                assertThat(airline2).isEqualTo(
                                new Airline("1191", "airline", "Air Austral", "UU", "REU", "REUNION", "France"));

        }

        // @Test
        // void testListAirlinesByDestinationAirport() {
        // ResponseEntity<List<Airline>> response = restTemplate.exchange(
        // "http://localhost:" + port + "/api/v1/airline/destination_airport/ATL",
        // HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
        // });
        // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // List<Airline> airlines = response.getBody();
        // assert airlines != null;
        // Airline airline = airlines.stream().filter(a ->
        // a.getId().equals("24")).findFirst().orElse(null);
        // assertThat(airline).isNotNull();
        // assertThat(airline.getId()).isEqualTo("24");
        // assertThat(airline.getType()).isEqualTo("airline");
        // assertThat(airline.getName()).isEqualTo("Aerocondor");
        // assertThat(airline.getIata()).isEqualTo("2B");
        // assertThat(airline.getIcao()).isEqualTo("ARD");
        // assertThat(airline.getCallsign()).isEqualTo("AEROCONDOR");
        // assertThat(airline.getCountry()).isEqualTo("Portugal");

        // // {"id":1191,"type":"airline","name":"Air
        // // Austral","iata":"UU","icao":"REU","callsign":"REUNION","country":"France"}

        // ResponseEntity<List<Airline>> response2 = restTemplate.exchange(
        // "http://localhost:" + port + "/api/v1/airline/destination_airport/CDG",
        // HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
        // });
        // assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        // List<Airline> airlines2 = response2.getBody();
        // assert airlines2 != null;
        // Airline airline2 = airlines2.stream().filter(a ->
        // a.getId().equals("1191")).findFirst().orElse(null);
        // assertThat(airline2).isNotNull();
        // assertThat(airline2.getId()).isEqualTo("1191");
        // assertThat(airline2.getType()).isEqualTo("airline");
        // assertThat(airline2.getName()).isEqualTo("Air Austral");
        // assertThat(airline2.getIata()).isEqualTo("UU");
        // assertThat(airline2.getIcao()).isEqualTo("REU");
        // assertThat(airline2.getCallsign()).isEqualTo("REUNION");
        // assertThat(airline2.getCountry()).isEqualTo("France");
        // }
}
