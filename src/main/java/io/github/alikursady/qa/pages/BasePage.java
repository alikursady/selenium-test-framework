package io.github.alikursady.qa.pages;

import io.github.alikursady.qa.config.Config;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
