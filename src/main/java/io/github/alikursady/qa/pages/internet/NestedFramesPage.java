package io.github.alikursady.qa.pages.internet;

import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Frame layout of /nested_frames:
 * frame-top  -> frame-left, frame-middle, frame-right
 * frame-bottom
 */
public class NestedFramesPage extends BasePage {

    public NestedFramesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the nested frames page")
    public NestedFramesPage open() {
        driver.get(Config.get("internet.url") + "/nested_frames");
        return this;
    }

    @Step("Read the text of the '{child}' frame nested under frame-top")
    public String textInTopChild(String child) {
        driver.switchTo().defaultContent();
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(child));
        String text = bodyText();
        driver.switchTo().defaultContent();
        return text;
    }

    @Step("Step back up one level from '{child}' to its sibling '{sibling}'")
    public String textInSiblingViaParent(String child, String sibling) {
        driver.switchTo().defaultContent();
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-top"));
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(child));
        driver.switchTo().parentFrame();
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(sibling));
        String text = bodyText();
        driver.switchTo().defaultContent();
        return text;
    }

    @Step("Read the bottom frame")
    public String bottomText() {
        driver.switchTo().defaultContent();
        until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame-bottom"));
        String text = bodyText();
        driver.switchTo().defaultContent();
        return text;
    }

    private String bodyText() {
        return until(ExpectedConditions.presenceOfElementLocated(By.tagName("body"))).getText().trim();
    }
}
