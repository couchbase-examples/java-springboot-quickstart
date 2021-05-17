package org.couchbase.quickstart.controllers;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.couchbase.quickstart.configs.ConfigDb;
import org.couchbase.quickstart.models.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post User Profile")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Profile.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Profile> save(@RequestBody final Profile userProfile)
    {
        /* replace code with implementation here */

        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }


    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for User Profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<List<Profile>> getProfiles(
            @RequestParam(required=false, defaultValue = "5") int limit,
            @RequestParam(required=false, defaultValue = "0") int skip,
            @RequestParam(required=true) String searchFirstName) {

        /* replace code with implementation here */
        Collection collection = getDbCollection();

        List<Profile> profiles = new ArrayList<Profile>();
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a Users Profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<Profile> getProfile(@RequestParam(required=true) UUID pid) {

        /* replace code with implementation here */

        Profile profile = new Profile();
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Modify a Users Profile", response = Profile.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Profile.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Profile> update(
            @PathVariable("id") UUID id,
            @RequestBody Profile profile) {

        /* replace code with implementation here */

        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    private Collection getDbCollection(){
        ConfigDb config = ConfigDb.getInstance();

        Cluster cluster = Cluster.connect(config.getHostName(), config.getUsername(), config.getPassword());
        Bucket bucket = cluster.bucket(config.getBucketName());
        Scope scope = bucket.scope(config.getScope());
        Collection collection = scope.collection(config.getCollection());
        return collection;
    }
}
