package io.github.alikursady.qa.pages.saucedemo;

import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorBanner;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the saucedemo login page")
    public LoginPage open() {
        driver.get(Config.get("saucedemo.url"));
        until(ExpectedConditions.visibilityOf(loginButton));
        return this;
    }

    @Step("Log in as {user}")
    public ProductsPage loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new ProductsPage(driver);
    }

    @Step("Submit credentials expecting failure")
    public LoginPage loginExpectingError(String user, String pass) {
        if (!user.isEmpty()) {
            type(username, user);
        }
        if (!pass.isEmpty()) {
            type(password, pass);
        }
        click(loginButton);
        return this;
    }

    public String errorMessage() {
        return textOf(errorBanner);
    }
}
