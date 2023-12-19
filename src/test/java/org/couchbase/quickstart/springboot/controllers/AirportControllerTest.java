// package org.couchbase.quickstart.springboot.controllers;

// import static org.hamcrest.Matchers.hasSize;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.Arrays;
// import java.util.List;

// import com.couchbase.client.java.query.QueryResult;
// import org.couchbase.quickstart.springboot.configs.DBProperties;
// import org.couchbase.quickstart.springboot.models.Airport;
// import org.hamcrest.Matchers;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.runner.RunWith;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.web.servlet.MockMvc;

// import com.couchbase.client.java.Bucket;
// import com.couchbase.client.java.Cluster;
// import com.couchbase.client.java.Collection;
// import com.couchbase.client.java.kv.GetResult;

// @RunWith(SpringRunner.class)
// @WebMvcTest(AirportController.class)
// class AirportControllerTest {

//    @Autowired
//    private MockMvc mvc;

//    @MockBean
//    private Cluster cluster;

//    @MockBean
//    private Collection airportCol;

//    @MockBean
//    private DBProperties dbProperties;

//    @MockBean
//    private Bucket bucket;

//    @BeforeEach
//    void setUp() {
//        airportCol = Mockito.mock(Collection.class);
//        cluster = Mockito.mock(Cluster.class);
//        dbProperties = Mockito.mock(DBProperties.class);
//        bucket = Mockito.mock(Bucket.class);
//    }


//    @Test
//    void getAirport() throws Exception {
//        Airport airport = new Airport();
//        GetResult getResult = mock(GetResult.class);
//        when(getResult.contentAs(Airport.class)).thenReturn(airport);
//        when(airportCol.get(anyString())).thenReturn(getResult);

//        mvc.perform(get("/api/v1/airport/{id}", "test"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", Matchers.is(airport.getId())));
//    }

//    @Test
//    void listAirports() throws Exception {
//        List<Airport> airports = Arrays.asList(new Airport(), new Airport());
//        QueryResult queryResult = mock(QueryResult.class);
//        when(queryResult.rowsAs(Airport.class)).thenReturn(airports);
//        when(cluster.query(anyString())).thenReturn(queryResult);
//        mvc.perform(get("/api/v1/airport/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }

//    @Test
//    void createAirport() throws Exception {
//        Airport airport = new Airport();
//        when(airportCol.insert(anyString(), any())).thenReturn(airport);

//        mvc.perform(post("/api/v1/airport/{id}", "test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonConvert.SerializeObject(airport)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(airport.getId())));
//    }

//    @Test
//    void updateAirport() throws Exception {
//        Airport airport = new Airport();
//        when(airportCol.replace(anyString(), any())).thenReturn(airport);

//        mvc.perform(put("/api/v1/airport/{id}", "test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonConvert.SerializeObject(airport)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(airport.getId())));
//    }

//    @Test
//    void deleteAirport() throws Exception {
//        doNothing().when(airportCol).remove(anyString());

//        mvc.perform(delete("/api/v1/airport/{id}", "test"))
//                .andExpect(status().isNoContent());
//    }

//    @Test
//    void listDirectConnections() throws Exception {
//        List<Airport> airports = Arrays.asList(new Airport(), new Airport());
//        when(cluster.query(anyString(), any())).thenReturn(airports);

//        mvc.perform(get("/api/v1/airport/direct-connections?airportCode=test"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)));
//    }

// }
