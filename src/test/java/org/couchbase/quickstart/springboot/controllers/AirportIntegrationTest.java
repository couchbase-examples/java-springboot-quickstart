package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Airport;
import org.couchbase.quickstart.springboot.models.Airport.Geo;
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
    // {"id":1254,"type":"airport","airportname":"Calais Dunkerque","city":"Calais","country":"France","faa":"CQF","icao":"LFAC","tz":"Europe/Paris","geo":{"lat":50.962097,"lon":1.954764,"alt":12.0}}

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
     void testGetAirport() throws Exception {
        ResponseEntity<Airport> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/airport/airport_1254", Airport.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Airport airport = response.getBody();
        assertThat(airport).isNotNull();
        assertThat(airport.getId()).isEqualTo("1254");
        assertThat(airport.getType()).isEqualTo("airport");
        assertThat(airport.getAirportname()).isEqualTo("Calais Dunkerque");
        assertThat(airport.getCity()).isEqualTo("Calais");
        assertThat(airport.getCountry()).isEqualTo("France");
        assertThat(airport.getFaa()).isEqualTo("CQF");
        assertThat(airport.getIcao()).isEqualTo("LFAC");
        assertThat(airport.getTz()).isEqualTo("Europe/Paris");
        assertThat(airport.getGeo().getLat()).isEqualTo(50.962097);
        assertThat(airport.getGeo().getLon()).isEqualTo(1.954764);
        assertThat(airport.getGeo().getAlt()).isEqualTo(12.0);
    }

    @Test
     void testCreateAirport() throws Exception {
        Airport airport = new Airport("airport_1255", "airport", "Test Airport", "Test City", "Test Country", "TST", "TEST", "Test Timezone", new Geo(1.0, 2.0, 3.0));
        ResponseEntity<Airport> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport, Airport.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Airport createdAirport = response.getBody();
        assertThat(createdAirport).isNotNull();
        assertThat(createdAirport.getId()).isEqualTo("airport_1255");
        assertThat(createdAirport.getType()).isEqualTo("airport");
        assertThat(createdAirport.getAirportname()).isEqualTo("Test Airport");
        assertThat(createdAirport.getCity()).isEqualTo("Test City");
        assertThat(createdAirport.getCountry()).isEqualTo("Test Country");
        assertThat(createdAirport.getFaa()).isEqualTo("TST");
        assertThat(createdAirport.getIcao()).isEqualTo("TEST");
        assertThat(createdAirport.getTz()).isEqualTo("Test Timezone");
        assertThat(createdAirport.getGeo().getAlt()).isEqualTo(1.0);
        assertThat(createdAirport.getGeo().getLat()).isEqualTo(2.0);
        assertThat(createdAirport.getGeo().getLon()).isEqualTo(3.0);
    }

    @Test
     void testUpdateAirport() throws Exception {
        Airport airport = new Airport("airport_1255", "airport", "Updated Test Airport", "Updated Test City", "Updated Test Country", "TST", "TEST", "Updated Test Timezone", new Geo(1.0, 2.0, 3.0));
        restTemplate.put("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport);
        ResponseEntity<Airport> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), Airport.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Airport updatedAirport = response.getBody();
        assertThat(updatedAirport).isNotNull();
        assertThat(updatedAirport.getId()).isEqualTo("airport_1255");
        assertThat(updatedAirport.getType()).isEqualTo("airport");
        assertThat(updatedAirport.getAirportname()).isEqualTo("Updated Test Airport");
        assertThat(updatedAirport.getCity()).isEqualTo("Updated Test City");
        assertThat(updatedAirport.getCountry()).isEqualTo("Updated Test Country");
        assertThat(updatedAirport.getFaa()).isEqualTo("TST");
        assertThat(updatedAirport.getIcao()).isEqualTo("TEST");
        assertThat(updatedAirport.getTz()).isEqualTo("Updated Test Timezone");
        assertThat(updatedAirport.getGeo().getAlt()).isEqualTo(1.0);
        assertThat(updatedAirport.getGeo().getLat()).isEqualTo(2.0);
        assertThat(updatedAirport.getGeo().getLon()).isEqualTo(3.0);
    }

    @Test
     void testDeleteAirport() throws Exception {
        Airport airport = new Airport("airport_1255", "airport", "Test Airport", "Test City", "Test Country", "TST", "TEST", "Test Timezone", new Geo(1.0, 2.0, 3.0));
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), airport, Airport.class);
        restTemplate.delete("http://localhost:" + port + "/api/v1/airport/" + airport.getId());
        ResponseEntity<Airport> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/airport/" + airport.getId(), Airport.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
     void testListAirports() throws Exception {
        ResponseEntity<List<Airport>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/airport/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Airport>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Airport> airports = response.getBody();
        assertThat(airports).isNotNull();
        assertThat(airports.size()).isGreaterThan(100);
    }

    // @Test
    //  void testListDirectConnections() throws Exception {
    //     ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/airport/direct-connections?airportCode=test", List.class);
    //     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    //     // Add more assertions as needed
    // }
}
