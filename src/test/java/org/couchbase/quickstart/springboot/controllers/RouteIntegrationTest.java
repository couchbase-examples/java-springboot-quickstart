package org.couchbase.quickstart.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.couchbase.quickstart.springboot.models.Route;
import org.couchbase.quickstart.springboot.services.RouteService;
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
class RouteIntegrationTest {

        @Value("${local.server.port}")
        private int port;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private RouteService routeService;

        private void deleteRoute(String routeId, String cleanupTiming) {
                try {
                        if (routeService.getRouteById(routeId) != null) {
                                restTemplate.delete("/api/v1/route/" + routeId);
                        }
                } catch (DocumentNotFoundException | DataRetrievalFailureException | ResourceAccessException e) {
                        log.warn("Document " + routeId + " not present " + cleanupTiming);
                } catch (Exception e) {
                        log.debug("Cleanup: Could not delete test route {}: {} (this is expected during test cleanup)", routeId, e.getMessage());
                }
        }

        private void deleteTestRouteData(String cleanupTiming) {
                deleteRoute("route_create", cleanupTiming);
                deleteRoute("route_update", cleanupTiming);
                deleteRoute("route_delete", cleanupTiming);
        }

        @BeforeEach
        void setUp() {
                deleteTestRouteData("prior to test");
        }

        @AfterEach
        void tearDown() {
                deleteTestRouteData("after test");
        }

        @Test
        void testGetRoute() {
                ResponseEntity<Route> response = restTemplate
                                .getForEntity("/api/v1/route/route_10001", Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Route route = response.getBody();
                assert route != null;

                Route expectedRoute = Route.builder()
                                .id("10001")
                                .type("route")
                                .airline("AF")
                                .airlineid("airline_137")
                                .sourceairport("TLV")
                                .destinationairport("NCE")
                                .stops(0)
                                .equipment("320")
                                .schedule(Arrays.asList(
                                                new Route.Schedule(0, "AF248", "21:24:00"),
                                                new Route.Schedule(1, "AF517", "13:36:00"),
                                                new Route.Schedule(1, "AF279", "21:35:00"),
                                                new Route.Schedule(1, "AF753", "00:54:00"),
                                                new Route.Schedule(1, "AF079", "15:29:00"),
                                                new Route.Schedule(1, "AF756", "06:16:00"),
                                                new Route.Schedule(2, "AF499", "03:39:00"),
                                                new Route.Schedule(2, "AF158", "08:49:00"),
                                                new Route.Schedule(2, "AF337", "06:01:00"),
                                                new Route.Schedule(2, "AF436", "11:48:00"),
                                                new Route.Schedule(2, "AF660", "09:35:00"),
                                                new Route.Schedule(3, "AF692", "12:55:00"),
                                                new Route.Schedule(3, "AF815", "19:38:00"),
                                                new Route.Schedule(3, "AF455", "12:33:00"),
                                                new Route.Schedule(3, "AF926", "19:45:00"),
                                                new Route.Schedule(4, "AF133", "10:36:00"),
                                                new Route.Schedule(4, "AF999", "07:46:00"),
                                                new Route.Schedule(4, "AF703", "15:42:00"),
                                                new Route.Schedule(5, "AF656", "05:40:00"),
                                                new Route.Schedule(6, "AF185", "16:21:00"),
                                                new Route.Schedule(6, "AF110", "00:56:00"),
                                                new Route.Schedule(6, "AF783", "06:07:00"),
                                                new Route.Schedule(6, "AF108", "04:54:00"),
                                                new Route.Schedule(6, "AF673", "12:07:00")))
                                .distance(2735.2013399811754)
                                .build();
                assertThat(route).isEqualTo(expectedRoute);
        }

        @Test
        void testCreateRoute() {

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
                                .postForEntity("/api/v1/route/" + route.getId(), route,
                                                Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                Route createdRoute = response.getBody();
                assert createdRoute != null;
                assertThat(createdRoute).isEqualTo(route);
        }

        @Test
        void testUpdateRoute() {

                Route route = Route.builder()
                                .id("route_update")
                                .type("route")
                                .airline("airline")
                                .airlineid("airlineid")
                                .sourceairport("sourceairport")
                                .destinationairport("destinationairport")
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

                restTemplate.postForEntity("/api/v1/route/" + route.getId(), route,
                                Route.class);

                Route updatedRoute = Route.builder().id("route_update")
                                .type("route")
                                .airline("updated_airline")
                                .airlineid("updated_airlineid")
                                .sourceairport("updated_sourceairport")
                                .destinationairport("updated_destinationairport")
                                .stops(0)
                                .equipment("updated_320")
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

                HttpEntity<Route> requestEntity = new HttpEntity<>(updatedRoute);
                ResponseEntity<Route> responseEntity = restTemplate.exchange("/api/v1/route/" + updatedRoute.getId(),
                                HttpMethod.PUT, requestEntity, Route.class);

                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                Route retrievedRoute = responseEntity.getBody();
                assert retrievedRoute != null;
                assertThat(retrievedRoute).isEqualTo(updatedRoute);
        }

        @Test
        void testDeleteRoute() {

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
                restTemplate.postForEntity("/api/v1/route/" + route.getId(), route,
                                Route.class);
                restTemplate.delete("/api/v1/route/" + route.getId());
                ResponseEntity<Route> response = restTemplate
                                .getForEntity("/api/v1/route/" + route.getId(),
                                                Route.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void testListRoutes() {
                int limit = 20;
                int offset = 0;

                ResponseEntity<List<Route>> response = restTemplate.exchange(
                                "/api/v1/route/list?limit=" + limit + "&offset=" + offset,
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Route> routes = response.getBody();
                assert routes != null;
                assertThat(routes).hasSize(limit);
        }

}