package com.example.framework.logging;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class Slf4jRestAssuredFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger("http-requests");

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        try {
            String reqLine = String.format("%s %s", requestSpec.getMethod(), requestSpec.getURI());
            String headers = requestSpec.getHeaders().asList().stream()
                    .map(h -> h.getName() + ": " + h.getValue())
                    .collect(Collectors.joining(" | "));
            log.info("HTTP REQUEST -> {} | headers: {} | body: {}", reqLine, headers, requestSpec.getBody() == null ? "<empty>" : requestSpec.getBody());

            Response response = ctx.next(requestSpec, responseSpec);

            log.info("HTTP RESPONSE <- {} {} | status: {} | content-type: {} | body-size: {}",
                    requestSpec.getMethod(), requestSpec.getURI(), response.getStatusCode(), response.getContentType(), response.getBody() == null ? 0 : response.getBody().asByteArray().length);

            return response;
        } catch (Exception e) {
            log.error("Error in logging filter", e);
            return ctx.next(requestSpec, responseSpec);
        }
    }
}
