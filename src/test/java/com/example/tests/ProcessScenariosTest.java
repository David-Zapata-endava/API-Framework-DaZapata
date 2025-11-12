package com.example.tests;

import com.example.framework.TestBase;
import com.example.framework.TestConfig;
import com.example.framework.TestResponseValidator;
import com.example.model.Movie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * I created ProcessScenariosTest to test process-like scenarios using only TheMovieDB API.
 * I initially used a local stub with WireMock, but I decided to use real TMDB endpoints only
 * and avoid local stubs or non-TMDB services to make my tests more realistic.
 */
public class ProcessScenariosTest extends TestBase {

    @Test
    public void fetchNowPlaying_and_validateFirstResult() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey, "API key required");

        Response resp = apiClient.request().queryParam("api_key", apiKey).get("/3/movie/now_playing");
        TestResponseValidator.assertStatusCode(resp, 200);

        Movie first = resp.jsonPath().getObject("results[0]", Movie.class);
        assertNotNull(first, "Expected at least one now_playing result");
        assertTrue(first.getId() > 0);
        assertNotNull(first.getTitle());
    }

    @Test
    public void search_then_getById_validateConsistency() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey, "API key required");

        Response search = apiClient.request().queryParam("api_key", apiKey).queryParam("query", "matrix").get("/3/search/movie");
        TestResponseValidator.assertStatusCode(search, 200);
        Movie found = search.jsonPath().getObject("results[0]", Movie.class);
        assertNotNull(found, "Expected search to return at least one movie");

        Response byId = apiClient.request().queryParam("api_key", apiKey).get(String.format("/3/movie/%d", found.getId()));
        TestResponseValidator.assertStatusCode(byId, 200);
        Movie details = byId.as(Movie.class);
        assertEquals(found.getId(), details.getId());
        assertTrue(details.getTitle().toLowerCase().contains(found.getTitle().split(" ")[0].toLowerCase()) || details.getTitle().equals(found.getTitle()));
    }
}
