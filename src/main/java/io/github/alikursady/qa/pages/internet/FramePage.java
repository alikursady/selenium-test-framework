package io.github.alikursady.qa.pages.internet;

import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The TinyMCE instance on this page is served in readonly mode, so the only
 * thing worth asserting here is that content behind the iframe boundary can be
 * reached and that control returns to the parent document afterwards.
 */
public class FramePage extends BasePage {

    private static final By EDITOR_FRAME = By.id("mce_0_ifr");
    private static final By EDITOR_BODY = By.id("tinymce");

    @FindBy(css = "h3")
    private WebElement heading;

    public FramePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the iframe editor page")
    public FramePage open() {
        driver.get(Config.get("internet.url") + "/iframe");
        until(ExpectedConditions.visibilityOf(heading));
        return this;
    }

    @Step("Read the text inside the editor iframe")
    public String editorText() {
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(EDITOR_FRAME));
        String text = until(ExpectedConditions.presenceOfElementLocated(EDITOR_BODY)).getText();
        driver.switchTo().defaultContent();
        return text;
    }

    public String headingText() {
        return textOf(heading);
    }
}
