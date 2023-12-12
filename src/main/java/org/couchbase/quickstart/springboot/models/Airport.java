package org.couchbase.quickstart.springboot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

    private String id;

    private String type;

    private String airportname;

    private String city;

    private String country;

    private String faa;

    private String icao;

    private String tz;

    private Geo geo;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Geo {

        private float alt;

        private float lat;

        private float lon;
    }
}
