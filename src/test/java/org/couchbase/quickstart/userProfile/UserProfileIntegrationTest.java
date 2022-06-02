package org.couchbase.quickstart.userProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;

import org.couchbase.quickstart.configs.CollectionNames;
import org.couchbase.quickstart.configs.DBProperties;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.ProfileRequest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserProfileIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private Cluster cluster;
    @Autowired
    private Bucket bucket;
    @Autowired
    private DBProperties prop;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @After
    public void cleanDB() {
        cluster.query("DELETE FROM "+prop.getBucketName()+"._default.profile ");
    }

    @Test
    public void testUserProfileNotFound() {

        this.webTestClient.get()
                .uri("/api/v1/profile?limit=5&skip=0&searchFirstName=Bob")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testCreateUserProfile(){
        //test data
        ProfileRequest createTestProfile = getCreateTestProfile();
        String json = getCreatedUserJson(createTestProfile);

        //run the post test
        EntityExchangeResult<Profile> profileResult = this.webTestClient.post()
                .uri("/api/v1/profile/")
                .bodyValue(json)
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json; charset=utf-8")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Profile.class)
                .returnResult();

        Profile result = bucket.collection(CollectionNames.PROFILE)
                .get(profileResult.getResponseBody().getPid())
                .contentAs(Profile.class);

        assertEquals(result.getFirstName(), createTestProfile.getFirstName());
        assertEquals(result.getLastName(), createTestProfile.getLastName());
        assertEquals(result.getEmail(), createTestProfile.getEmail());
        assertNotEquals(result.getPassword(), createTestProfile.getPassword());
        assertNotNull(result.getPid());
    }


    @Test
    public void testListUsersSuccess() {

        //test data
        Profile testProfile = getTestProfile();
        bucket.collection(CollectionNames.PROFILE).insert(testProfile.getPid(), testProfile);

        EntityExchangeResult<List<Profile>> profileListResult = this.webTestClient.get()
            .uri("/api/v1/profile/profiles/?limit=5&skip=0&search=Jam")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Profile.class)
            .returnResult();

        MatcherAssert.assertThat(profileListResult.getResponseBody(), Matchers.hasSize(1));
        Profile result = profileListResult.getResponseBody().get(0);
        System.out.println(result);
        assertEquals(result.getFirstName(), testProfile.getFirstName());
        assertEquals(result.getLastName(), testProfile.getLastName());
        assertEquals(result.getEmail(), testProfile.getEmail());
        //TBD: encrypted password verify
        //assertTrue(BCrypt.checkpw(testProfile.getPassword(),result.getPassword()));
        assertNotNull(result.getPid());
    }


    @Test
    public void testListUsersNoResult() {

        //test data
        Profile testProfile = getTestProfile();
        bucket.collection(CollectionNames.PROFILE).insert(testProfile.getPid(), testProfile);

        EntityExchangeResult<List<Profile>> profileListResult = this.webTestClient.get()
            .uri("/api/v1/profile/profiles/?limit=5&skip=0&search=Jack")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Profile.class)
            .returnResult();

        MatcherAssert.assertThat(profileListResult.getResponseBody(), Matchers.hasSize(0));
    }

    @Test
    public void testDeleteUserProfile() {

        exceptionRule.expect(DocumentNotFoundException.class);
        exceptionRule.expectMessage("Document with the given id not found");

        //test data
        Profile testProfile = getTestProfile();
        bucket.collection(CollectionNames.PROFILE).insert(testProfile.getPid(), testProfile);

        //delete the user
        this.webTestClient.delete()
                .uri(String.format("/api/v1/profile/%s", testProfile.getPid()))
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json; charset=utf-8")
                .exchange()
                .expectStatus().isOk();

        bucket.collection(CollectionNames.PROFILE).get(testProfile.getPid());
    }

    @Test
    public void testTransferCredits() {
      Profile sourceProfile = getTestProfile(),
              targetProfile = getTestProfile();
      
      bucket.collection(CollectionNames.PROFILE).insert(sourceProfile.getPid(), sourceProfile);
      bucket.collection(CollectionNames.PROFILE).insert(targetProfile.getPid(), targetProfile);

      // transfer credits 
      callTransferCredits(sourceProfile, targetProfile, 100)
        .expectStatus().isOk();

      // attempt to transfer credits again -- should fail
      callTransferCredits(sourceProfile, targetProfile, 100)
        .expectStatus().is5xxServerError();

      sourceProfile = bucket.collection(CollectionNames.PROFILE).get(sourceProfile.getPid()).contentAs(Profile.class);
      targetProfile = bucket.collection(CollectionNames.PROFILE).get(targetProfile.getPid()).contentAs(Profile.class);

      assertNotNull(sourceProfile);
      assertNotNull(targetProfile);

      assertEquals((Integer)0, sourceProfile.getBalance());
      assertEquals((Integer)200, targetProfile.getBalance());
    }

    private ResponseSpec callTransferCredits(Profile sourceProfile, Profile targetProfile, Integer amount) {
      return webTestClient.post()
        .uri("/api/v1/profile/transfer")
//        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters
          .fromFormData("source", sourceProfile.getPid())
          .with("target", targetProfile.getPid())
          .with("amount", amount.toString())
        )
        //.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .exchange();
    }

    private String getCreatedUserJson(ProfileRequest profile) {
        //create json to post to integration test
        return JsonObject.create()
                .put("firstName", profile.getFirstName())
                .put("lastName", profile.getLastName())
                .put("email", profile.getEmail())
                .put("password", profile.getPassword())
                .put("balance", profile.getBalance())
                .toString();
    }

    private ProfileRequest getCreateTestProfile() {
        return new ProfileRequest(
                "James",
                "Gosling",
                "james.gosling@sun.com",
                "password",
                100);
    }

    private Profile getTestProfile() {
        return new Profile(
                UUID.randomUUID().toString(),
                "James",
                "Gosling",
                "james.gosling@sun.com",
                "password",
                100);
    }
}
