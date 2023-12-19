package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.couchbase.quickstart.springboot.models.Airline;
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
class AirlineControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // {"id":10,"type":"airline","name":"40-Mile
    // Air","iata":"Q5","icao":"MLA","callsign":"MILE-AIR","country":"United
    // States"}
    @Test
    void testGetAirline() {
        ResponseEntity<Airline> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/airline/airline_10", Airline.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Airline airline = response.getBody();
        assertThat(airline).isNotNull();
        assertThat(airline.getId()).isEqualTo("10");
        assertThat(airline.getType()).isEqualTo("airline");
        assertThat(airline.getName()).isEqualTo("40-Mile Air");
        assertThat(airline.getIata()).isEqualTo("Q5");
        assertThat(airline.getIcao()).isEqualTo("MLA");
        assertThat(airline.getCallsign()).isEqualTo("MILE-AIR");
        assertThat(airline.getCountry()).isEqualTo("United States");
    }

    @Test
    void testCreateAirline() {
        Airline airline = new Airline("airline_11", "airline", "Test Airline", "TA", "TST", "TEST", "United States");
        ResponseEntity<Airline> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline, Airline.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Airline createdAirline = response.getBody();
        assertThat(createdAirline).isNotNull();
        assertThat(createdAirline.getId()).isEqualTo("airline_11");
        assertThat(createdAirline.getType()).isEqualTo("airline");
        assertThat(createdAirline.getName()).isEqualTo("Test Airline");
        assertThat(createdAirline.getIata()).isEqualTo("TA");
        assertThat(createdAirline.getIcao()).isEqualTo("TST");
        assertThat(createdAirline.getCallsign()).isEqualTo("TEST");
        assertThat(createdAirline.getCountry()).isEqualTo("United States");
    }

    @Test
    void testUpdateAirline() {
        Airline airline = new Airline("airline_11", "airline", "Updated Test Airline", "TA", "TST", "TEST",
                "United States");
        restTemplate.put("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline);
        ResponseEntity<Airline> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), Airline.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Airline updatedAirline = response.getBody();
        assertThat(updatedAirline).isNotNull();
        assertThat(updatedAirline.getId()).isEqualTo("airline_11");
        assertThat(updatedAirline.getType()).isEqualTo("airline");
        assertThat(updatedAirline.getName()).isEqualTo("Updated Test Airline");
        assertThat(updatedAirline.getIata()).isEqualTo("TA");
        assertThat(updatedAirline.getIcao()).isEqualTo("TST");
        assertThat(updatedAirline.getCallsign()).isEqualTo("TEST");
        assertThat(updatedAirline.getCountry()).isEqualTo("United States");

    }

    @Test
    void testDeleteAirline() {
        Airline airline = new Airline("airline_11", "airline", "Test Airline", "TA", "TST", "TEST", "United States");
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), airline,
                Airline.class);
        restTemplate.delete("http://localhost:" + port + "/api/v1/airline/" + airline.getId());
        ResponseEntity<Airline> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/airline/" + airline.getId(), Airline.class);
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
        assertThat(airlines).isNotNull();
        assertThat(airlines.size()).isGreaterThan(180);

    }

    @Test
    void testListAirlinesByCountry() {
        // Check that if it contains
        // airline_10226{"id":10226,"type":"airline","name":"Atifly","iata":"A1","icao":"A1F","callsign":"atifly","country":"United
        // States"}
        String country = "United States";
        ResponseEntity<List<Airline>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/airline/country/" + country,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Airline> airlines = response.getBody();
        Airline airline = airlines.stream().filter(a -> a.getId().equals("10226")).findFirst().orElse(null);
        assertThat(airline).isNotNull();
        assertThat(airline.getId()).isEqualTo("10226");
        assertThat(airline.getType()).isEqualTo("airline");
        assertThat(airline.getName()).isEqualTo("Atifly");
        assertThat(airline.getIata()).isEqualTo("A1");
        assertThat(airline.getIcao()).isEqualTo("A1F");
        assertThat(airline.getCallsign()).isEqualTo("atifly");
        assertThat(airline.getCountry()).isEqualTo("United States");

        // {"id":1191,"type":"airline","name":"Air
        // Austral","iata":"UU","icao":"REU","callsign":"REUNION","country":"France"}

        country = "France";
        ResponseEntity<List<Airline>> response2 = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/airline/country/" + country,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Airline>>() {
                });
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        List<Airline> airlines2 = response2.getBody();
        Airline airline2 = airlines2.stream().filter(a -> a.getId().equals("1191")).findFirst().orElse(null);
        assertThat(airline2).isNotNull();
        assertThat(airline2.getId()).isEqualTo("1191");
        assertThat(airline2.getType()).isEqualTo("airline");
        assertThat(airline2.getName()).isEqualTo("Air Austral");
        assertThat(airline2.getIata()).isEqualTo("UU");
        assertThat(airline2.getIcao()).isEqualTo("REU");
        assertThat(airline2.getCallsign()).isEqualTo("REUNION");
        assertThat(airline2.getCountry()).isEqualTo("France");

    }

    // @Test
    // void testListAirlinesByDestinationAirport() {
    // ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:"
    // + port + "/api/v1/airline/destination/test", List.class);
    // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    // // Add more assertions as needed
    // }
}

// package org.couchbase.quickstart.springboot.controllers;
//
// import org.couchbase.quickstart.springboot.configs.CouchbaseConfig;
// import org.couchbase.quickstart.springboot.configs.DBProperties;
// import org.couchbase.quickstart.springboot.configs.Swagger;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;
//
// import com.couchbase.client.java.Bucket;
// import com.couchbase.client.java.Cluster;
// import com.couchbase.client.java.Collection;
//
//
//// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//// @AutoConfigureMockMvc
// @ContextConfiguration(classes = {CouchbaseConfig.class, Swagger.class,
// DBProperties.class})
//// @RunWith(SpringRunner.class)
//
// @RunWith(SpringRunner.class)
// @WebMvcTest(AirlineController.class)
// @WebAppConfiguration
// public class AirlineControllerTest {
//
// @Autowired
// private WebApplicationContext webApplicationContext;
//
// private MockMvc mockMvc;
//
// // ... your other dependencies or setup if needed
//
// @Before
// public void setup() {
// mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
// }
//
// @Test
// public void testGetAirline() {
// // Perform the GET request using mockMvc and assert the response
// mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airline/test"))
// .andExpect(MockMvcResultMatchers.status().isOk())
// .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("test"));
// }
//
// // ... other test methods
// }
