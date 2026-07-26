package io.github.alikursady.qa.pages.saucedemo;

import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

    /**
     * The products page uses the same .title element, so a bare text read here
     * can pick up "Products" before the navigation has finished.
     */
    private void awaitLoaded() {
        until(ExpectedConditions.textToBe(By.cssSelector(".title"), "Your Cart"));
    }

    public boolean isLoaded() {
        try {
            awaitLoaded();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Read the product names in the cart")
    public List<String> productNames() {
        awaitLoaded();
        return cartItems.stream()
                .map(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText())
                .toList();
    }

    public boolean isCheckoutAvailable() {
        return until(ExpectedConditions.elementToBeClickable(checkoutButton)).isEnabled();
    }
}
