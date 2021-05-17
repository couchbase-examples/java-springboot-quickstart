package org.couchbase.quickstart.controllers;

import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.kv.ReplaceOptions;
import org.couchbase.quickstart.factories.DatabaseFactory;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.ProfileList;
import org.couchbase.quickstart.models.ProfileResult;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Locale;
import java.util.UUID;

import com.couchbase.client.java.*;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryResult;
import static com.couchbase.client.java.query.QueryOptions.queryOptions;

@RestController
@RequestMapping("/api/v1/userprofiles")
public class UserProfileController {

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post User Profile")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Profile.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<ProfileResult> save(@RequestBody final Profile userProfile) {

        if (userProfile.getEmail() == null || userProfile.getEmail() == "") {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } else if (userProfile.getPassword() == null || userProfile.getPassword() == ""){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        Cluster cluster = DatabaseFactory.getCluster();
        try {
            Collection collection = DatabaseFactory.getCollection(cluster);
            UUID id = UUID.randomUUID();
            JsonObject profile = JsonObject.create()
                   .put("pid", id.toString())
                   .put("firstName", userProfile.getFirstName())
                   .put("lastName", userProfile.getLastName())
                   .put("email", userProfile.getEmail())
                   .put("password", userProfile.getPassword());
            MutationResult result = collection.upsert(String.format("%s", id), profile);
            userProfile.setPid(id);
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProfileResult(userProfile, result.toString()));
        } catch (Exception e) {
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ProfileResult(new Profile(), String.format("Error: %s %s", e.getMessage(), e.getStackTrace().toString())));
        }
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for User Profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<ProfileList> getProfiles(
            @RequestParam(required=false, defaultValue = "5") int limit,
            @RequestParam(required=false, defaultValue = "0") int skip,
            @RequestParam(required=true) String searchFirstName) {

        ProfileList profileList = new ProfileList();
        Cluster cluster = DatabaseFactory.getCluster();
        try {
            /* replace code with implementation here */

            final QueryResult result = cluster.query("SELECT p.* FROM user_profile._default.profile p WHERE lower(p.firstName) LIKE %$firstName% LIMIT $limit OFFSET $skip",
                    queryOptions().parameters(JsonObject.create()
                            .put("firstName", searchFirstName.toLowerCase())
                            .put("limit", limit)
                            .put("skip", skip)));

            for (JsonObject row : result.rowsAsObject()){
                System.out.println(String.format("Found row with ID %s",row.toString()));
            }
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.OK).body(profileList);
        } catch (Exception e){
            profileList.setMessage(String.format("Error: %s",e.getMessage()));
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(profileList);
        }
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a Users Profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<ProfileResult> getProfile(@RequestParam(required=true) UUID pid) {

        Cluster cluster = DatabaseFactory.getCluster();
        try {
            Collection collection = DatabaseFactory.getCollection(cluster);
            GetResult result = collection.get(pid.toString());
            Profile profile = result.contentAs(Profile.class);
            ProfileResult profileResult = new ProfileResult(profile, "");
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.OK).body(profileResult);
        } catch (Exception e){
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ProfileResult(new Profile(), String.format("Error: %s",e.getMessage())));
        }
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Modify a Users Profile", response = Profile.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = Profile.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<ProfileResult> update( @PathVariable("pid") UUID pid, @RequestBody Profile profile) {

        Cluster cluster = DatabaseFactory.getCluster();
        try {
            Collection collection = DatabaseFactory.getCollection(cluster);
            GetResult result = collection.get(pid.toString());
            JsonObject content = result.contentAsObject();
            content.put("firstName", profile.getFirstName())
                    .put("lastName", profile.getLastName())
                    .put("email", profile.getEmail())
                    .put("password", profile.getPassword());
            collection.replace(pid.toString(), content, ReplaceOptions.replaceOptions().cas(result.cas()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProfileResult(profile, ""));
        } catch (Exception e){
            cluster.disconnect();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ProfileResult(new Profile(), String.format("Error: %s",e.getMessage())));
        }
    }
}
