package com.example.framework;

import org.junit.jupiter.api.BeforeEach;

public class TestBase {
    protected Config config;
    protected ApiClient apiClient;

    @BeforeEach
    public void setUp() {
        config = new Config();
        apiClient = new ApiClient(config);
    }
}
