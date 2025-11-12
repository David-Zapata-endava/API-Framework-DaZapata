package com.example.framework;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

/**
 * I created these test-scoped assertion helper methods to make my assertions more readable.
 */
public class TestResponseValidator {

    public static void assertStatusCode(Response response, int expected) {
        Assertions.assertEquals(expected, response.getStatusCode(), "Unexpected status code. Response body: " + response.asString());
    }

    public static void assertJsonContainsKey(Response response, String jsonPath) {
        Object val = response.jsonPath().get(jsonPath);
        Assertions.assertNotNull(val, "Expected JSON path '" + jsonPath + "' to be present. Full body: " + response.asString());
    }
}
