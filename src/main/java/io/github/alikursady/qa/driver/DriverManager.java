package io.github.alikursady.qa.driver;

import org.openqa.selenium.WebDriver;

/**
 * Holds one WebDriver per thread so a parallel suite does not share a browser
 * between tests.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void set(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver on this thread; was setUp() skipped?");
        }
        return driver;
    }

    public static boolean isSet() {
        return DRIVER.get() != null;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
