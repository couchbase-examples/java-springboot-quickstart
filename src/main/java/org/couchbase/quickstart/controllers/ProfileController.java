package org.couchbase.quickstart.controllers;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryScanConsistency;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.couchbase.quickstart.configs.DBProperties;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.ProfileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;
import static org.couchbase.quickstart.configs.CollectionNames.PROFILE;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private Cluster cluster;
    private Collection profileCol;
    private DBProperties dbProperties;

    public ProfileController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
      System.out.println("Initializing profile controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.profileCol = bucket.collection(PROFILE);
        this.dbProperties = dbProperties;
    }


    @CrossOrigin(value="*")
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a user profile from the request")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Profile.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Profile> save(@RequestBody final ProfileRequest userProfile) {
        //generates an id and save the user
        Profile profile = userProfile.getProfile();

        try {
            profileCol.insert(profile.getPid(), profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a user profile by id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<Profile> getProfile(@PathVariable("id") UUID id) {
        Profile profile = profileCol.get(id.toString()).contentAs(Profile.class);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @CrossOrigin(value="*")
    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Update a user profile", response = Profile.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Updated the user profile", response = Profile.class),
            @ApiResponse(code = 404, message = "User profile not found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Profile> update(@PathVariable("id") UUID id, @RequestBody Profile profile) {
        try {
            profileCol.replace(id.toString(), profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(profile);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete a user profile")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity delete(@PathVariable UUID id){

        try {
            profileCol.remove(id.toString());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value="*")
    @GetMapping(path = "/profiles/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for user profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Returns the list of user profiles"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user profiles", response = Error.class)
            })
    public ResponseEntity<List<Profile>> getProfiles(
            @RequestParam(required=false, defaultValue = "5") int limit,
            @RequestParam(required=false, defaultValue = "0") int skip,
            @RequestParam String search) {

        String qryString = "SELECT p.* FROM `"+dbProperties.getBucketName()+"`.`_default`.`"+PROFILE +"` p "+
                            "WHERE lower(p.firstName) LIKE '%"+search.toLowerCase()
                            +"%' OR lower(p.lastName) LIKE '%"+search.toLowerCase()+"%'  LIMIT "+limit+" OFFSET "+skip;
        System.out.println("Query="+qryString);
        //TBD with params: final List<Profile> profiles = cluster.query("SELECT p.* FROM `$bucketName`.`_default`.`$collectionName` p WHERE lower(p.firstName) LIKE '$search' OR lower(p.lastName) LIKE '$search' LIMIT $limit OFFSET $skip",
        final List<Profile> profiles = 
                cluster.query(qryString,
                    queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Profile.class);
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }
}
