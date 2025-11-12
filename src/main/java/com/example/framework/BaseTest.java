package com.example.framework;

/**
 * I created a lightweight placeholder BaseTest in main sources.
 * My actual test-level BaseTest with JUnit annotations is in `src/test/java`.
 */
public class BaseTest {
    protected Config config;
    protected ApiClient apiClient;

    public void initialize() {
        this.config = new Config();
        this.apiClient = new ApiClient(config);
    }
}
