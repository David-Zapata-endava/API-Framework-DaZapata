package com.example.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private final Properties props = new Properties();

    public TestConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("credentials.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                throw new RuntimeException("credentials.properties not found on classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credentials.properties", e);
        }
    }

    public String getApiKey() {
        return props.getProperty("api.key");
    }

    public String getBearerToken() {
        return props.getProperty("bearer.token");
    }
}
