package com.example.tests;

import com.example.framework.TestBase;
import com.example.framework.TestResponseValidator;
import com.example.framework.TestConfig;
import com.example.model.Movie;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TMDBApiTest extends TestBase {

    @Test
    public void getMovieById_usingApiKey_returnsMovieWithTitle() {
        TestConfig cfg = new TestConfig();
        String apiKey = cfg.getApiKey();
        assertNotNull(apiKey, "API key must be provided in credentials.properties");

        Response resp = apiClient.request()
                .queryParam("api_key", apiKey)
                .get("/3/movie/550");

        TestResponseValidator.assertStatusCode(resp, 200);

        // I deserialize the response to a DTO using Jackson
        Movie movie = resp.as(Movie.class);
        assertNotNull(movie);
        assertEquals(550, movie.getId());
        assertNotNull(movie.getTitle());
        // I know the title for id 550 is "Fight Club"
        assertTrue(movie.getTitle().toLowerCase().contains("fight"));
    }

    @Test
    public void getNowPlaying_usingBearerToken_returns200() {
        TestConfig cfg = new TestConfig();
        String token = cfg.getBearerToken();
        assertNotNull(token, "Bearer token must be provided in credentials.properties");

        Response resp = apiClient.request()
                .header("Authorization", "Bearer " + token)
                .get("/3/movie/now_playing");

        TestResponseValidator.assertStatusCode(resp, 200);
        assertNotNull(resp.jsonPath().getList("results"));
    }
}
