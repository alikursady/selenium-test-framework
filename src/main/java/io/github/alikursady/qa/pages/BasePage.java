package io.github.alikursady.qa.pages;

import io.github.alikursady.qa.config.Config;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.getSeconds("explicit.wait.seconds"));
        // AjaxElementLocatorFactory re-finds elements on each call, which keeps
        // PageFactory fields from going stale on pages that re-render.
        PageFactory.initElements(
                new AjaxElementLocatorFactory(driver, (int) Config.getSeconds("explicit.wait.seconds").toSeconds()),
                this);
    }

    protected <T> T until(ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    protected void type(WebElement element, String text) {
        until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected void click(WebElement element) {
        until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Clicks and then waits for the effect the click was supposed to have,
     * clicking again if nothing happened.
     * <p>
     * The application under test is a React app and on a loaded CI runner it
     * intermittently accepts a click without running the handler: the element
     * reports as clickable, the click returns cleanly, and the page does not
     * change. Retrying a click that already worked would undo it, so every
     * retry is gated on the outcome not having happened yet.
     */
    protected void clickUntil(WebElement element, ExpectedCondition<?> settled) {
        Duration perAttempt = Duration.ofSeconds(4);
        TimeoutException last = null;

        for (int attempt = 1; attempt <= 3; attempt++) {
            if (Boolean.TRUE.equals(quietly(settled))) {
                return;
            }
            click(element);
            try {
                new WebDriverWait(driver, perAttempt).until(settled);
                return;
            } catch (TimeoutException e) {
                last = e;
            }
        }
        throw last;
    }

    private Boolean quietly(ExpectedCondition<?> condition) {
        try {
            Object result = condition.apply(driver);
            return result instanceof Boolean flag ? flag : result != null;
        } catch (RuntimeException e) {
            return Boolean.FALSE;
        }
    }

    protected String textOf(WebElement element) {
        return until(ExpectedConditions.visibilityOf(element)).getText();
    }

    protected Alert awaitAlert() {
        return until(ExpectedConditions.alertIsPresent());
    }

    public String title() {
        return driver.getTitle();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
