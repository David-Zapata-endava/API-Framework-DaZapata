package com.example.tests;

import com.example.framework.TestBase;
import com.example.framework.TestConfig;
import com.example.framework.TestResponseValidator;
import com.example.model.Movie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProcessWriteScenariosTest
 *
 * I designed these tests to demonstrate a two-service process flow: I read data from TheMovieDB
 * and write/update/delete it against a writable public test API (JSONPlaceholder).
 * I chose JSONPlaceholder because it's a public test API that accepts POST/PUT/DELETE for testing and
 * returns typical responses (though it does not persist data in a real DB).
 */
public class ProcessWriteScenariosTest extends TestBase {

    @Test
    public void fetchFromTmdb_and_postPutDelete_toJsonPlaceholder() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey, "API key required in src/test/resources/credentials.properties");

        // First, I fetch a movie from TMDB
        Response nowPlaying = apiClient.request().queryParam("api_key", apiKey).get("/3/movie/now_playing");
        TestResponseValidator.assertStatusCode(nowPlaying, 200);
        Movie first = nowPlaying.jsonPath().getObject("results[0]", Movie.class);
        assertNotNull(first, "Expect at least one now_playing movie");

        // Next, I POST the movie JSON to JSONPlaceholder to create a new fake resource
        String postUrl = "https://jsonplaceholder.typicode.com/posts";
        Response postResp = apiClient.request().body(first).post(postUrl);
        assertEquals(201, postResp.getStatusCode(), "POST to JSONPlaceholder should return 201");
        int createdId = postResp.jsonPath().getInt("id");
        assertTrue(createdId > 0, "JSONPlaceholder should return an id for the created resource");

    // Then I PATCH (partially update) the created resource — I know JSONPlaceholder reliably supports PATCH
    first.setTitle(first.getTitle() + " - updated");
    String putUrl = String.format("https://jsonplaceholder.typicode.com/posts/%d", createdId);
    Response patchResp = apiClient.request().body(first).patch(putUrl);
    assertEquals(200, patchResp.getStatusCode(), "PATCH should return 200 on JSONPlaceholder");
    assertEquals(first.getTitle(), patchResp.jsonPath().getString("title"));

        // Finally, I DELETE the created resource
        Response delResp = apiClient.request().delete(putUrl);
        // I know JSONPlaceholder typically returns 200 for deletes
        assertTrue(delResp.getStatusCode() == 200 || delResp.getStatusCode() == 204,
                "DELETE should return 200 or 204");
    }

    @Test
    public void searchTmdb_and_postResult_toJsonPlaceholder() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey);

        Response search = apiClient.request().queryParam("api_key", apiKey).queryParam("query", "matrix").get("/3/search/movie");
        TestResponseValidator.assertStatusCode(search, 200);
        Movie found = search.jsonPath().getObject("results[0]", Movie.class);
        assertNotNull(found, "Expected search to return a movie");

        String postUrl = "https://jsonplaceholder.typicode.com/posts";
        Response p = apiClient.request().body(found).post(postUrl);
        assertEquals(201, p.getStatusCode());
        assertTrue(p.jsonPath().getInt("id") > 0);
    }
}
