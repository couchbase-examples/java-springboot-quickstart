package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.couchbase.quickstart.springboot.models.Route;
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
class RouteIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @BeforeEach
        void setUp() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_delete");
        }

        @AfterEach
        void tearDown() {
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_create");
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_update");
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/route_delete");
        }

        @Test
        void testGetRoute() throws Exception {
                ResponseEntity<Route> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/route/route_10000", Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Route route = response.getBody();
                assert route != null;

                Route expectedRoute = Route.builder()
                                .id("10000")
                                .type("route")
                                .airline("AF")
                                .airlineid("airline_137")
                                .sourceairport("TLV")
                                .destinationairport("MRS")
                                .stops(0)
                                .equipment("320")
                                .schedule(Arrays.asList(
                                                new Route.Schedule(0, "AF198", "10:13:00"),
                                                new Route.Schedule(0, "AF547", "19:14:00"),
                                                new Route.Schedule(0, "AF943", "01:31:00"),
                                                new Route.Schedule(1, "AF356", "12:40:00"),
                                                new Route.Schedule(1, "AF480", "08:58:00"),
                                                new Route.Schedule(1, "AF250", "12:59:00"),
                                                new Route.Schedule(1, "AF130", "04:45:00"),
                                                new Route.Schedule(2, "AF997", "00:31:00"),
                                                new Route.Schedule(2, "AF223", "19:41:00"),
                                                new Route.Schedule(2, "AF890", "15:14:00"),
                                                new Route.Schedule(2, "AF399", "00:30:00"),
                                                new Route.Schedule(2, "AF328", "16:18:00"),
                                                new Route.Schedule(3, "AF074", "23:50:00"),
                                                new Route.Schedule(3, "AF556", "11:33:00"),
                                                new Route.Schedule(4, "AF064", "13:23:00"),
                                                new Route.Schedule(4, "AF596", "12:09:00"),
                                                new Route.Schedule(4, "AF818", "08:02:00"),
                                                new Route.Schedule(5, "AF967", "11:33:00"),
                                                new Route.Schedule(5, "AF730", "19:42:00"),
                                                new Route.Schedule(6, "AF882", "17:07:00"),
                                                new Route.Schedule(6, "AF485", "17:03:00"),
                                                new Route.Schedule(6, "AF898", "10:01:00"),
                                                new Route.Schedule(6, "AF496", "07:00:00")))
                                .distance(2881.617376098415)
                                .build();
                assertThat(route).isEqualTo(expectedRoute);
        }

        @Test
        void testCreateRoute() throws Exception {

                Route route = Route.builder()
                                .id("route_create")
                                .type("route")
                                .airline("AF")
                                .airlineid("airline_137")
                                .sourceairport("TLV")
                                .destinationairport("MRS")
                                .stops(0)
                                .equipment("320")
                                .schedule(Arrays.asList(
                                                new Route.Schedule(0, "AF198", "10:13:00"),
                                                new Route.Schedule(0, "AF547", "19:14:00"),
                                                new Route.Schedule(0, "AF943", "01:31:00"),
                                                new Route.Schedule(1, "AF356", "12:40:00"),
                                                new Route.Schedule(1, "AF480", "08:58:00"),
                                                new Route.Schedule(1, "AF250", "12:59:00"),
                                                new Route.Schedule(1, "AF130", "04:45:00"),
                                                new Route.Schedule(2, "AF997", "00:31:00"),
                                                new Route.Schedule(2, "AF223", "19:41:00"),
                                                new Route.Schedule(2, "AF890", "15:14:00"),
                                                new Route.Schedule(2, "AF399", "00:30:00"),
                                                new Route.Schedule(2, "AF328", "16:18:00"),
                                                new Route.Schedule(3, "AF074", "23:50:00"),
                                                new Route.Schedule(3, "AF556", "11:33:00"),
                                                new Route.Schedule(4, "AF064", "13:23:00"),
                                                new Route.Schedule(4, "AF596", "12:09:00"),
                                                new Route.Schedule(4, "AF818", "08:02:00"),
                                                new Route.Schedule(5, "AF967", "11:33:00"),
                                                new Route.Schedule(5, "AF730", "19:42:00"),
                                                new Route.Schedule(6, "AF882", "17:07:00"),
                                                new Route.Schedule(6, "AF485", "17:03:00"),
                                                new Route.Schedule(6, "AF898", "10:01:00"),
                                                new Route.Schedule(6, "AF496", "07:00:00")))
                                .distance(2881.617376098415)
                                .build();
                ResponseEntity<Route> response = restTemplate
                                .postForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), route,
                                                Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Route createdRoute = response.getBody();
                assert createdRoute != null;
                assertThat(createdRoute).isEqualTo(route);
        }

        @Test
        void testUpdateRoute() throws Exception {

                Route route = Route.builder()
                                .id("route_update")
                                .type("route")
                                .airline("AF")
                                .airlineid("airline_137")
                                .sourceairport("TLV")
                                .destinationairport("MRS")
                                .stops(0)
                                .equipment("320")
                                .schedule(Arrays.asList(
                                                new Route.Schedule(0, "AF198", "10:13:00"),
                                                new Route.Schedule(0, "AF547", "19:14:00"),
                                                new Route.Schedule(0, "AF943", "01:31:00"),
                                                new Route.Schedule(1, "AF356", "12:40:00"),
                                                new Route.Schedule(1, "AF480", "08:58:00"),
                                                new Route.Schedule(1, "AF250", "12:59:00"),
                                                new Route.Schedule(1, "AF130", "04:45:00"),
                                                new Route.Schedule(2, "AF997", "00:31:00"),
                                                new Route.Schedule(2, "AF223", "19:41:00"),
                                                new Route.Schedule(2, "AF890", "15:14:00"),
                                                new Route.Schedule(2, "AF399", "00:30:00"),
                                                new Route.Schedule(2, "AF328", "16:18:00"),
                                                new Route.Schedule(3, "AF074", "23:50:00"),
                                                new Route.Schedule(3, "AF556", "11:33:00"),
                                                new Route.Schedule(4, "AF064", "13:23:00"),
                                                new Route.Schedule(4, "AF596", "12:09:00"),
                                                new Route.Schedule(4, "AF818", "08:02:00"),
                                                new Route.Schedule(5, "AF967", "11:33:00"),
                                                new Route.Schedule(5, "AF730", "19:42:00"),
                                                new Route.Schedule(6, "AF882", "17:07:00"),
                                                new Route.Schedule(6, "AF485", "17:03:00"),
                                                new Route.Schedule(6, "AF898", "10:01:00"),
                                                new Route.Schedule(6, "AF496", "07:00:00")))
                                .distance(2881.617376098415)
                                .build();

                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), route,
                                Route.class);
                restTemplate.put("http://localhost:" + port + "/api/v1/route/" + route.getId(), route);
                ResponseEntity<Route> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(),
                                                Route.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Route updatedRoute = response.getBody();
                assert updatedRoute != null;
                assertThat(updatedRoute).isEqualTo(route);
        }

        @Test
        void testDeleteRoute() throws Exception {

                Route route = Route.builder()
                                .id("route_delete")
                                .type("route")
                                .airline("AF")
                                .airlineid("airline_137")
                                .sourceairport("TLV")
                                .destinationairport("MRS")
                                .stops(0)
                                .equipment("320")
                                .schedule(Arrays.asList(
                                                new Route.Schedule(0, "AF198", "10:13:00"),
                                                new Route.Schedule(0, "AF547", "19:14:00"),
                                                new Route.Schedule(0, "AF943", "01:31:00"),
                                                new Route.Schedule(1, "AF356", "12:40:00"),
                                                new Route.Schedule(1, "AF480", "08:58:00"),
                                                new Route.Schedule(1, "AF250", "12:59:00"),
                                                new Route.Schedule(1, "AF130", "04:45:00"),
                                                new Route.Schedule(2, "AF997", "00:31:00"),
                                                new Route.Schedule(2, "AF223", "19:41:00"),
                                                new Route.Schedule(2, "AF890", "15:14:00"),
                                                new Route.Schedule(2, "AF399", "00:30:00"),
                                                new Route.Schedule(2, "AF328", "16:18:00"),
                                                new Route.Schedule(3, "AF074", "23:50:00"),
                                                new Route.Schedule(3, "AF556", "11:33:00"),
                                                new Route.Schedule(4, "AF064", "13:23:00"),
                                                new Route.Schedule(4, "AF596", "12:09:00"),
                                                new Route.Schedule(4, "AF818", "08:02:00"),
                                                new Route.Schedule(5, "AF967", "11:33:00"),
                                                new Route.Schedule(5, "AF730", "19:42:00"),
                                                new Route.Schedule(6, "AF882", "17:07:00"),
                                                new Route.Schedule(6, "AF485", "17:03:00"),
                                                new Route.Schedule(6, "AF898", "10:01:00"),
                                                new Route.Schedule(6, "AF496", "07:00:00")))
                                .distance(2881.617376098415)
                                .build();
                restTemplate.postForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(), route,
                                Route.class);
                restTemplate.delete("http://localhost:" + port + "/api/v1/route/" + route.getId());
                ResponseEntity<Route> response = restTemplate
                                .getForEntity("http://localhost:" + port + "/api/v1/route/" + route.getId(),
                                                Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListRoutes() throws Exception {
                int limit = 10;
                int offset = 0;

                ResponseEntity<List<Route>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/route/list?limit=" + limit + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Route>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Route> routes = response.getBody();
                assert routes != null;
                assertThat(routes).hasSize(limit);
        }

}