package com.example.framework;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.Filter;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import static io.restassured.RestAssured.given;

public class RequestBuilder {
    private final String baseUrl;
    private Object body;
    private ContentType contentType = ContentType.JSON;
    private final java.util.Map<String, Object> queryParams = new java.util.LinkedHashMap<>();
    private final java.util.Map<String, String> headers = new java.util.LinkedHashMap<>();

    public RequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        // I relax HTTPS validation so my tests can run in environments without the full trust chain
        RestAssured.useRelaxedHTTPSValidation();
        // I add a default logging filter that uses SLF4J to keep console output consistent
        RestAssured.filters(new com.example.framework.logging.Slf4jRestAssuredFilter());
    }

    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public RequestBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestBuilder queryParam(String name, Object value) {
        queryParams.put(name, value);
        return this;
    }

    public RequestBuilder contentType(ContentType ct) {
        this.contentType = ct;
        return this;
    }

    public Response get(String path) {
        RequestSpecification req = given().contentType(contentType);
        if (!headers.isEmpty()) req = req.headers(headers);
        if (!queryParams.isEmpty()) req = req.queryParams(queryParams);
        if (body != null) req = req.body(body);
        return req.when().get(resolve(path)).then().extract().response();
    }

    public Response post(String path) {
        RequestSpecification req = given().contentType(contentType);
        if (!headers.isEmpty()) req = req.headers(headers);
        if (!queryParams.isEmpty()) req = req.queryParams(queryParams);
        if (body != null) req = req.body(body);
        return req.when().post(resolve(path)).then().extract().response();
    }

    public Response put(String path) {
        RequestSpecification req = given().contentType(contentType);
        if (!headers.isEmpty()) req = req.headers(headers);
        if (!queryParams.isEmpty()) req = req.queryParams(queryParams);
        if (body != null) req = req.body(body);
        return req.when().put(resolve(path)).then().extract().response();
    }

    public Response patch(String path) {
        RequestSpecification req = given().contentType(contentType);
        if (!headers.isEmpty()) req = req.headers(headers);
        if (!queryParams.isEmpty()) req = req.queryParams(queryParams);
        if (body != null) req = req.body(body);
        return req.when().patch(resolve(path)).then().extract().response();
    }

    public Response delete(String path) {
        RequestSpecification req = given().contentType(contentType);
        if (!headers.isEmpty()) req = req.headers(headers);
        if (!queryParams.isEmpty()) req = req.queryParams(queryParams);
        if (body != null) req = req.body(body);
        return req.when().delete(resolve(path)).then().extract().response();
    }

    private String resolve(String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        if (!path.startsWith("/")) path = "/" + path;
        return baseUrl + path;
    }
}
