package io.github.alikursady.qa.pages.internet;

import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class JavaScriptAlertsPage extends BasePage {

    @FindBy(css = "button[onclick='jsAlert()']")
    private WebElement alertButton;

    @FindBy(css = "button[onclick='jsConfirm()']")
    private WebElement confirmButton;

    @FindBy(css = "button[onclick='jsPrompt()']")
    private WebElement promptButton;

    @FindBy(id = "result")
    private WebElement result;

    public JavaScriptAlertsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the JavaScript alerts page")
    public JavaScriptAlertsPage open() {
        driver.get(Config.get("internet.url") + "/javascript_alerts");
        until(ExpectedConditions.visibilityOf(alertButton));
        return this;
    }

    @Step("Trigger the alert and accept it")
    public String acceptAlert() {
        click(alertButton);
        awaitAlert().accept();
        return textOf(result);
    }

    @Step("Trigger the confirm dialog and dismiss it")
    public String dismissConfirm() {
        click(confirmButton);
        awaitAlert().dismiss();
        return textOf(result);
    }

    @Step("Trigger the prompt and answer with '{answer}'")
    public String answerPrompt(String answer) {
        click(promptButton);
        Alert alert = awaitAlert();
        alert.sendKeys(answer);
        alert.accept();
        return textOf(result);
    }
}
