package org.couchbase.quickstart.springboot.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Geo {

        @DecimalMin("-10000.0")
        @DecimalMax("10000.0")
        private double alt;

        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        private double lat;

        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        private double lon;
    }
}
