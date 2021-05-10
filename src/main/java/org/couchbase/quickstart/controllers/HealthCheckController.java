package org.couchbase.quickstart.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {

    @GetMapping(path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Service Health", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in checking service health", response = Error.class)
            })
    public String getServiceHealthStatus() {
        return "{ \"serviceStatus\": \"Service is Up\" }";
    }
}
