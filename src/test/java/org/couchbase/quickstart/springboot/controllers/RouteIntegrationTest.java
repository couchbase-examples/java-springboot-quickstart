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
                                                new Route.Schedule(0, "10:13:00", "AF198"),
                                                new Route.Schedule(0, "19:14:00", "AF547"),
                                                new Route.Schedule(0, "01:31:00", "AF943"),
                                                new Route.Schedule(1, "12:40:00", "AF356"),
                                                new Route.Schedule(1, "08:58:00", "AF480"),
                                                new Route.Schedule(1, "12:59:00", "AF250"),
                                                new Route.Schedule(1, "04:45:00", "AF130"),
                                                new Route.Schedule(2, "00:31:00", "AF997"),
                                                new Route.Schedule(2, "19:41:00", "AF223"),
                                                new Route.Schedule(2, "15:14:00", "AF890"),
                                                new Route.Schedule(2, "00:30:00", "AF399"),
                                                new Route.Schedule(2, "16:18:00", "AF328"),
                                                new Route.Schedule(3, "23:50:00", "AF074"),
                                                new Route.Schedule(3, "11:33:00", "AF556"),
                                                new Route.Schedule(4, "13:23:00", "AF064"),
                                                new Route.Schedule(4, "12:09:00", "AF596"),
                                                new Route.Schedule(4, "08:02:00", "AF818"),
                                                new Route.Schedule(5, "11:33:00", "AF967"),
                                                new Route.Schedule(5, "19:42:00", "AF730"),
                                                new Route.Schedule(6, "17:07:00", "AF882"),
                                                new Route.Schedule(6, "17:03:00", "AF485"),
                                                new Route.Schedule(6, "10:01:00", "AF898"),
                                                new Route.Schedule(6, "07:00:00", "AF496")))
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
                                                new Route.Schedule(0, "10:13:00", "AF198"),
                                                new Route.Schedule(0, "19:14:00", "AF547"),
                                                new Route.Schedule(0, "01:31:00", "AF943"),
                                                new Route.Schedule(1, "12:40:00", "AF356"),
                                                new Route.Schedule(1, "08:58:00", "AF480"),
                                                new Route.Schedule(1, "12:59:00", "AF250"),
                                                new Route.Schedule(1, "04:45:00", "AF130"),
                                                new Route.Schedule(2, "00:31:00", "AF997"),
                                                new Route.Schedule(2, "19:41:00", "AF223"),
                                                new Route.Schedule(2, "15:14:00", "AF890"),
                                                new Route.Schedule(2, "00:30:00", "AF399"),
                                                new Route.Schedule(2, "16:18:00", "AF328"),
                                                new Route.Schedule(3, "23:50:00", "AF074"),
                                                new Route.Schedule(3, "11:33:00", "AF556"),
                                                new Route.Schedule(4, "13:23:00", "AF064"),
                                                new Route.Schedule(4, "12:09:00", "AF596"),
                                                new Route.Schedule(4, "08:02:00", "AF818"),
                                                new Route.Schedule(5, "11:33:00", "AF967"),
                                                new Route.Schedule(5, "19:42:00", "AF730"),
                                                new Route.Schedule(6, "17:07:00", "AF882"),
                                                new Route.Schedule(6, "17:03:00", "AF485"),
                                                new Route.Schedule(6, "10:01:00", "AF898"),
                                                new Route.Schedule(6, "07:00:00", "AF496")))
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
                                                new Route.Schedule(0, "10:13:00", "AF198"),
                                                new Route.Schedule(0, "19:14:00", "AF547"),
                                                new Route.Schedule(0, "01:31:00", "AF943"),
                                                new Route.Schedule(1, "12:40:00", "AF356"),
                                                new Route.Schedule(1, "08:58:00", "AF480"),
                                                new Route.Schedule(1, "12:59:00", "AF250"),
                                                new Route.Schedule(1, "04:45:00", "AF130"),
                                                new Route.Schedule(2, "00:31:00", "AF997"),
                                                new Route.Schedule(2, "19:41:00", "AF223"),
                                                new Route.Schedule(2, "15:14:00", "AF890"),
                                                new Route.Schedule(2, "00:30:00", "AF399"),
                                                new Route.Schedule(2, "16:18:00", "AF328"),
                                                new Route.Schedule(3, "23:50:00", "AF074"),
                                                new Route.Schedule(3, "11:33:00", "AF556"),
                                                new Route.Schedule(4, "13:23:00", "AF064"),
                                                new Route.Schedule(4, "12:09:00", "AF596"),
                                                new Route.Schedule(4, "08:02:00", "AF818"),
                                                new Route.Schedule(5, "11:33:00", "AF967"),
                                                new Route.Schedule(5, "19:42:00", "AF730"),
                                                new Route.Schedule(6, "17:07:00", "AF882"),
                                                new Route.Schedule(6, "17:03:00", "AF485"),
                                                new Route.Schedule(6, "10:01:00", "AF898"),
                                                new Route.Schedule(6, "07:00:00", "AF496")))
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
                                                new Route.Schedule(0, "10:13:00", "AF198"),
                                                new Route.Schedule(0, "19:14:00", "AF547"),
                                                new Route.Schedule(0, "01:31:00", "AF943"),
                                                new Route.Schedule(1, "12:40:00", "AF356"),
                                                new Route.Schedule(1, "08:58:00", "AF480"),
                                                new Route.Schedule(1, "12:59:00", "AF250"),
                                                new Route.Schedule(1, "04:45:00", "AF130"),
                                                new Route.Schedule(2, "00:31:00", "AF997"),
                                                new Route.Schedule(2, "19:41:00", "AF223"),
                                                new Route.Schedule(2, "15:14:00", "AF890"),
                                                new Route.Schedule(2, "00:30:00", "AF399"),
                                                new Route.Schedule(2, "16:18:00", "AF328"),
                                                new Route.Schedule(3, "23:50:00", "AF074"),
                                                new Route.Schedule(3, "11:33:00", "AF556"),
                                                new Route.Schedule(4, "13:23:00", "AF064"),
                                                new Route.Schedule(4, "12:09:00", "AF596"),
                                                new Route.Schedule(4, "08:02:00", "AF818"),
                                                new Route.Schedule(5, "11:33:00", "AF967"),
                                                new Route.Schedule(5, "19:42:00", "AF730"),
                                                new Route.Schedule(6, "17:07:00", "AF882"),
                                                new Route.Schedule(6, "17:03:00", "AF485"),
                                                new Route.Schedule(6, "10:01:00", "AF898"),
                                                new Route.Schedule(6, "07:00:00", "AF496")))
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
                ResponseEntity<List<Route>> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/v1/route/list",
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Route>>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Route> routes = response.getBody();
                assert routes != null;
                assertThat(routes).hasSize(24025);
        }

}