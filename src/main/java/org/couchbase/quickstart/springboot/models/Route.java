package org.couchbase.quickstart.springboot.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    private String id;

    private String type;

    private String airline;

    private String airlineId;

    private String sourceAirport;

    private String destinationAirport;

    private int stops;

    private String equipment;

    private List<Schedule> schedule;

    private float distance;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schedule {

        private int day;

        private String flight;

        private String utc;
    }
}