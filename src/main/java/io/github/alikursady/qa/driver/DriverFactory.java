package io.github.alikursady.qa.driver;

import io.github.alikursady.qa.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DriverFactory {

    private static final Map<String, Boolean> RESOLVED = new ConcurrentHashMap<>();

    private DriverFactory() {
    }

    /**
     * WebDriverManager writes into a shared cache directory. Letting every
     * parallel thread call setup() at once made session creation fail
     * intermittently, so resolution happens once per browser.
     */
    private static void resolveDriverOnce(String browser, Runnable setup) {
        RESOLVED.computeIfAbsent(browser, key -> {
            setup.run();
            return Boolean.TRUE;
        });
    }

    public static WebDriver create() {
        String browser = Config.get("browser").toLowerCase(Locale.ROOT);
        boolean headless = Config.getBoolean("headless");

        WebDriver driver = switch (browser) {
            case "chrome" -> chrome(headless);
            case "firefox" -> firefox(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        driver.manage().timeouts().pageLoadTimeout(Config.getSeconds("page.load.timeout.seconds"));
        return driver;
    }

    private static WebDriver chrome(boolean headless) {
        resolveDriverOnce("chrome", () -> WebDriverManager.chromedriver().setup());
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        // Chrome refuses to start as root in most CI containers without these two.
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        // Suppresses the "Change your password" bubble that steals focus on the
        // saucedemo login form.
        options.addArguments("--disable-features=PasswordLeakDetection");
        return new org.openqa.selenium.chrome.ChromeDriver(options);
    }

    private static WebDriver firefox(boolean headless) {
        resolveDriverOnce("firefox", () -> WebDriverManager.firefoxdriver().setup());
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        return new org.openqa.selenium.firefox.FirefoxDriver(options);
    }
}
