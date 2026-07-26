package io.github.alikursady.qa.tests;

import io.github.alikursady.qa.BaseTest;
import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.saucedemo.CartPage;
import io.github.alikursady.qa.pages.saucedemo.LoginPage;
import io.github.alikursady.qa.pages.saucedemo.ProductsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Feature("Cart")
public class CartTest extends BaseTest {

    private ProductsPage loginAsStandardUser() {
        return new LoginPage(driver())
                .open()
                .loginAs(Config.get("standard.user"), Config.get("valid.password"));
    }

    @Test(dataProvider = "products",
            dataProviderClass = io.github.alikursady.qa.data.LoginData.class,
            description = "A product added on the list page shows up in the cart")
    public void addedProductAppearsInCart(String productName) {
        CartPage cart = loginAsStandardUser()
                .addToCart(productName)
                .openCart();

        assertTrue(cart.isLoaded(), "Cart page did not load");
        assertEquals(cart.productNames(), java.util.List.of(productName));
        assertTrue(cart.isCheckoutAvailable(), "Checkout should be available with one item");
    }

    @Test(description = "The cart badge counts each added product")
    public void cartBadgeTracksItemCount() {
        ProductsPage products = loginAsStandardUser();
        assertEquals(products.cartBadgeCount(), 0, "Cart should start empty");

        products.addToCart("Sauce Labs Backpack");
        assertEquals(products.cartBadgeCount(), 1);

        products.addToCart("Sauce Labs Bike Light");
        assertEquals(products.cartBadgeCount(), 2);
    }
}
