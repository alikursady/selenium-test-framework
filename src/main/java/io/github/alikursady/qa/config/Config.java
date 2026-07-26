package io.github.alikursady.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

/**
 * Resolves settings in the order: environment variable, JVM system property,
 * config.properties. CI overrides values through the environment, so the file
 * only has to hold sane local defaults.
 */
public final class Config {

    private static final Properties PROPS = load();

    private Config() {
    }

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = Config.class.getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
        return p;
    }

    public static String get(String key) {
        String value = System.getenv(toEnvName(key));
        if (value == null) {
            value = System.getProperty(key);
        }
        if (value == null) {
            value = PROPS.getProperty(key);
        }
        if (value == null) {
            throw new IllegalArgumentException("No value configured for '" + key + "'");
        }
        return value.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static Duration getSeconds(String key) {
        return Duration.ofSeconds(Long.parseLong(get(key)));
    }

    /**
     * explicit.wait.seconds -> EXPLICIT_WAIT_SECONDS
     * <p>
     * Locale.ROOT is not optional here. On a Turkish-locale JVM the default
     * toUpperCase() maps 'i' to 'İ' (dotted capital), so the lookup would miss
     * every environment variable containing an i and silently fall back to the
     * file. Tests would then pass locally and use the wrong values in CI.
     */
    private static String toEnvName(String key) {
        return key.toUpperCase(Locale.ROOT).replace('.', '_');
    }
}
