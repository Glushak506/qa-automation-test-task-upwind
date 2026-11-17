package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Reads config.properties once
 */
public final class TestConfig {

    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static String uiBaseUrl() {
        return get("ui.baseUrl");
    }

    public static String apiBaseUrl() {
        return get("api.baseUrl");
    }

    private static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = Objects.requireNonNull(
                TestConfig.class.getResourceAsStream("/config.properties"),
                "config.properties not found in src/test/resources")) {
            properties.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read config.properties", e);
        }
        return properties;
    }
}
