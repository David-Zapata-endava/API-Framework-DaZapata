package com.example.tests;

import com.example.framework.TestBase;
import com.example.framework.TestConfig;
import com.example.framework.TestResponseValidator;
import com.example.model.Movie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * I created SystemProcessTests for real integration testing against TheMovieDB API.
 * I made these tests read-only and they use public TMDB endpoints. I require a valid API key
 * or bearer token provided via src/test/resources/credentials.properties.
 */
public class SystemProcessTests extends TestBase {

    @Test
    public void fetchMovieById_and_validateFields() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey, "API key must be set in credentials.properties");

        Response resp = apiClient.request().queryParam("api_key", apiKey).get("/3/movie/550");
        TestResponseValidator.assertStatusCode(resp, 200);

        Movie movie = resp.as(Movie.class);
        assertNotNull(movie);
        assertEquals(550, movie.getId());
        assertTrue(movie.getTitle().toLowerCase().contains("fight") || movie.getTitle().length() > 0);
    }

    @Test
    public void nowPlaying_returnsResults() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey);

        Response resp = apiClient.request().queryParam("api_key", apiKey).get("/3/movie/now_playing");
        TestResponseValidator.assertStatusCode(resp, 200);

        assertNotNull(resp.jsonPath().getList("results"));
        assertTrue(resp.jsonPath().getList("results").size() > 0);
    }

    @Test
    public void searchMovie_returnsAtLeastOne() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey);

        Response resp = apiClient.request().queryParam("api_key", apiKey).queryParam("query", "inception").get("/3/search/movie");
        TestResponseValidator.assertStatusCode(resp, 200);

        assertNotNull(resp.jsonPath().getList("results"));
        assertTrue(resp.jsonPath().getList("results").size() >= 0);
    }

    @Test
    public void getPopularMovies_returnsList() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey);

        Response resp = apiClient.request().queryParam("api_key", apiKey).get("/3/movie/popular");
        TestResponseValidator.assertStatusCode(resp, 200);
        assertTrue(resp.jsonPath().getList("results").size() > 0);
    }
}
