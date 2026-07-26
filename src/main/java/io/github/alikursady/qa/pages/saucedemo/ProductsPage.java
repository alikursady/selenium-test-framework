package io.github.alikursady.qa.pages.saucedemo;

import io.github.alikursady.qa.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProductsPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement heading;

    @FindBy(css = ".inventory_item")
    private List<WebElement> items;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartLink;

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return "Products".equals(textOf(heading));
        } catch (RuntimeException e) {
            return false;
        }
    }

    public int itemCount() {
        until(ExpectedConditions.visibilityOf(heading));
        return items.size();
    }

    @Step("Add '{productName}' to the cart")
    public ProductsPage addToCart(String productName) {
        itemFor(productName).findElement(By.cssSelector("button")).click();
        return this;
    }

    public int cartBadgeCount() {
        List<WebElement> badge = driver.findElements(By.cssSelector(".shopping_cart_badge"));
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    @Step("Open the cart")
    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    private WebElement itemFor(String productName) {
        until(ExpectedConditions.visibilityOf(heading));
        return items.stream()
                .filter(item -> item.findElement(By.cssSelector(".inventory_item_name")).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No product named '" + productName + "'"));
    }
}
