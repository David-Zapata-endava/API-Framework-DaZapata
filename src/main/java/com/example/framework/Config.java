package com.example.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();

    public Config() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                throw new RuntimeException("config.properties not found on classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public String getBaseUrl() {
        return props.getProperty("base.url", "https://api.themoviedb.org");
    }

    public int getRequestTimeoutMs() {
        return Integer.parseInt(props.getProperty("request.timeout", "5000"));
    }
}
