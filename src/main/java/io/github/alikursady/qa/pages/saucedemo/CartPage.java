package io.github.alikursady.qa.pages.saucedemo;

import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement heading;

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = "[data-test='checkout']")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Your Cart".equals(textOf(heading));
    }

    @Step("Read the product names in the cart")
    public List<String> productNames() {
        until(ExpectedConditions.visibilityOf(heading));
        return cartItems.stream()
                .map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText())
                .toList();
    }

    public boolean isCheckoutAvailable() {
        return until(ExpectedConditions.elementToBeClickable(checkoutButton)).isEnabled();
    }
}
