package com.example.framework;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private final Config config;

    public ApiClient(Config config) {
        this.config = config;
    }

    public RequestBuilder request() {
        return new RequestBuilder(config.getBaseUrl());
    }

    public Response get(String path) {
        return request().get(path);
    }

    public Response post(String path, Object body) {
        return request().body(body).post(path);
    }
}
