package org.couchbase.quickstart.springboot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airline implements Serializable {

    private String id;

    private String type;

    private String name;

    private String iata;

    private String icao;

    private String callsign;

    private String country;
}
