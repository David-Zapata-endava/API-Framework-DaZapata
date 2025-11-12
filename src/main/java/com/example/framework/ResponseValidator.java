package com.example.framework;

import io.restassured.response.Response;

/**
 * I built a minimal runtime validator to avoid test-only dependencies in main.
 * I prefer using the test-scope `ResponseValidator` in tests for JUnit-friendly assertions.
 */
public class ResponseValidator {

    public static void ensureStatusCode(Response response, int expected) {
        if (response.getStatusCode() != expected) {
            throw new AssertionError("Unexpected status code: " + response.getStatusCode() + ", body: " + response.asString());
        }
    }

    public static void ensureJsonContainsKey(Response response, String jsonPath) {
        Object val = response.jsonPath().get(jsonPath);
        if (val == null) {
            throw new AssertionError("Expected JSON path '" + jsonPath + "' to be present. Full body: " + response.asString());
        }
    }
}
