package org.couchbase.quickstart.userProfile;

import com.couchbase.client.java.json.JsonObject;
import org.couchbase.quickstart.models.Profile;
import org.couchbase.quickstart.models.ProfileResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class userProfileTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testUserProfileNotFound() {
        this.webTestClient.get()
                .uri("/api/v1/userprofiles?limit=5&skip=0&searchFirstName=Bob")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testCreateThenDeleteUserProfile() {
        //test data
        Profile testProfile = getTestProfile();

        //create json to post to integration test
        String json = JsonObject.create()
                .put("firstName", testProfile.getFirstName())
                .put("lastName", testProfile.getLastName())
                .put("password",testProfile.getPassword())
                .put("email",testProfile.getEmail()).toString();

        //run the post test
        EntityExchangeResult<ProfileResult> profileResult = this.webTestClient.post()
                .uri("/api/v1/userprofiles/")
                .bodyValue(json)
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json; charset=utf-8")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProfileResult.class)
                .returnResult();

        //assert results
        ProfileResult newUser =  profileResult.getResponseBody();
        assertEquals(newUser.getFirstName(), testProfile.getFirstName());
        assertEquals(newUser.getLastName(), testProfile.getLastName());
        assertEquals(newUser.getEmail(), testProfile.getEmail());
        assertNotEquals(newUser.getPassword(), testProfile.getPassword());
        assertNotNull(newUser.getPid());

        //delete the user
        this.webTestClient.delete()
                .uri(String.format("/api/v1/userprofiles/%s", newUser.getPid()))
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json; charset=utf-8")
                .exchange()
                .expectStatus().isOk();
    }

    public Profile getTestProfile() {
        return new Profile(
                UUID.randomUUID(),
                "James",
                "Gosling",
                "password",
                "james.gosling@sun.com");
    }
}
