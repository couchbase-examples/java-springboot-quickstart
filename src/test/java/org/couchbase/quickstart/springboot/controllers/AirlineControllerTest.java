package org.couchbase.quickstart.springboot.controllers;

import java.util.Arrays;
import java.util.List;

import org.couchbase.quickstart.springboot.configs.DBProperties;
import org.couchbase.quickstart.springboot.models.Airline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.GetResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AirlineController.class)
class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Cluster cluster;

    @MockBean
    private Collection airlineCol;

    @MockBean
    private DBProperties dbProperties;

    @MockBean
    private Bucket bucket;

    @BeforeEach
    void setUp() {
        airlineCol = Mockito.mock(Collection.class);
        cluster = Mockito.mock(Cluster.class);
        dbProperties = Mockito.mock(DBProperties.class);
        bucket = Mockito.mock(Bucket.class);
    }

    @Test
    void getAirline() throws Exception {
        Airline airline = new Airline();
        GetResult getResult = Mockito.mock(GetResult.class);
        Mockito.when(getResult.contentAs(Airline.class)).thenReturn(airline);
        Mockito.when(airlineCol.get(Mockito.anyString())).thenReturn(getResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airline/{id}", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(airline.getId()));
    }

    @Test
    void createAirline() throws Exception {
        Airline airline = new Airline();
        Mockito.when(airlineCol.insert(Mockito.anyString(), Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/airline/{id}", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(airline)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void updateAirline() throws Exception {
        Airline airline = new Airline();
        Mockito.when(airlineCol.replace(Mockito.anyString(), Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/airline/{id}", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(airline)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteAirline() throws Exception {
        Mockito.doNothing().when(airlineCol).remove(Mockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/airline/{id}", "test"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void listAirlines() throws Exception {
        Airline airline1 = new Airline("10", "airline", "40-Mile Air", "Q5", "MLA", "MILE-AIR", "United States");
        Airline airline2 = new Airline("10123", "airline", "Texas Wings", "TQ", "TXW", "TXW", "United States");
        List<Airline> airlines = Arrays.asList(airline1, airline2);
        
        Mockito.when(airlineCol.list(Mockito.any(),Airline.class)).thenReturn(airlines);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airline/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(airline1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(airline2.getId()));

        
    }
}
