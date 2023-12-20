package org.couchbase.quickstart.springboot.models;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

    @NotBlank(message = "ID is mandatory")
    private String id;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Airport name is mandatory")
    private String airportname;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Country is mandatory")
    private String country;

    // @NotBlank(message = "FAA is mandatory")
    @Pattern(regexp = "[A-Z]{3}", message = "FAA must be 3 uppercase letters")
    private String faa;

    // @NotBlank(message = "ICAO is mandatory")
    @Pattern(regexp = "[A-Z]{4}", message = "ICAO must be 4 uppercase letters")
    private String icao;

    @NotBlank(message = "Timezone is mandatory")
    private String tz;

    @Valid
    private Geo geo;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Geo {

        // @NotBlank(message = "Altitude is mandatory")
        private double alt;

        // @NotBlank(message = "Latitude is mandatory")
        private double lat;

        // @NotBlank(message = "Longitude is mandatory")
        private double lon;
    }
}
