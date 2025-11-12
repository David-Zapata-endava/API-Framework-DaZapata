package com.example.tests;

import com.example.framework.Config;
import com.example.framework.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SampleApiTest extends TestBase {

    @Test
    public void configLoadsAndApiClientInitialized() {
        // I verify that Config loads base.url from test resources
        Config cfg = new Config();
        assertNotNull(cfg.getBaseUrl());
        assertTrue(cfg.getBaseUrl().contains("http"));

        // I check that apiClient is initialized by TestBase's setUp() method
        assertNotNull(apiClient);
    }
}
