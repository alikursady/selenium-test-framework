package io.github.alikursady.qa.pages;

import io.github.alikursady.qa.config.Config;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
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
     * Clicks, then waits for the effect the click was supposed to have, and
     * tries again if nothing happened.
     * <p>
     * On headless Chrome under Linux this application accepts a WebDriver click
     * without running its handler: the element reports clickable, the call
     * returns cleanly, and nothing changes. It does not reproduce on Windows,
     * and it happens on plain buttons as well as on the cart link, which is an
     * empty anchor with no href drawn entirely in CSS. Running the suite one
     * browser at a time reduced how often it happened but did not stop it, so
     * it is not only contention.
     * <p>
     * The first attempt is a real click, so the normal path still exercises hit
     * testing. Only after that fails does it fall back to dispatching the click
     * on the element directly, which React does pick up. Every attempt is gated
     * on the outcome not having happened yet, because repeating a click that
     * worked would undo it.
     */
    protected void clickUntil(WebElement element, ExpectedCondition<?> settled) {
        Duration perAttempt = Duration.ofSeconds(4);
        TimeoutException last = null;

        for (int attempt = 1; attempt <= 3; attempt++) {
            if (Boolean.TRUE.equals(quietly(settled))) {
                return;
            }
            if (attempt == 1) {
                click(element);
            } else {
                until(ExpectedConditions.elementToBeClickable(element));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
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
