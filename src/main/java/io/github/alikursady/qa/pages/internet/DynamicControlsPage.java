package io.github.alikursady.qa.pages.internet;

import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DynamicControlsPage extends BasePage {

    // Waiting for the checkbox to go away has to go through a raw locator: a
    // PageFactory proxy would re-resolve on every poll and burn the whole
    // AjaxElementLocatorFactory timeout before reporting it missing.
    private static final By CHECKBOX = By.cssSelector("#checkbox input");

    @FindBy(css = "#checkbox-example button")
    private WebElement toggleCheckboxButton;

    @FindBy(css = "#input-example button")
    private WebElement toggleInputButton;

    @FindBy(css = "#input-example input")
    private WebElement textInput;

    @FindBy(id = "message")
    private WebElement message;

    public DynamicControlsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the dynamic controls page")
    public DynamicControlsPage open() {
        driver.get(Config.get("internet.url") + "/dynamic_controls");
        until(ExpectedConditions.visibilityOf(toggleCheckboxButton));
        return this;
    }

    @Step("Remove the checkbox and wait for it to disappear")
    public String removeCheckbox() {
        click(toggleCheckboxButton);
        until(ExpectedConditions.invisibilityOfElementLocated(CHECKBOX));
        return textOf(message);
    }

    @Step("Enable the text input and wait for it to become editable")
    public boolean enableInput() {
        click(toggleInputButton);
        until(ExpectedConditions.elementToBeClickable(textInput));
        return textInput.isEnabled();
    }

    public boolean isInputEnabled() {
        return textInput.isEnabled();
    }
}
