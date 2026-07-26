package io.github.alikursady.qa.tests;

import io.github.alikursady.qa.BaseTest;
import io.github.alikursady.qa.config.Config;
import io.github.alikursady.qa.pages.saucedemo.LoginPage;
import io.github.alikursady.qa.pages.saucedemo.ProductsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(description = "A valid user reaches the products page")
    public void validLoginOpensProducts() {
        ProductsPage products = new LoginPage(driver())
                .open()
                .loginAs(Config.get("standard.user"), Config.get("valid.password"));

        assertTrue(products.isLoaded(), "Products page did not load after login");
        assertEquals(products.itemCount(), 6, "Unexpected number of products");
    }

    @Test(dataProvider = "invalidLogins",
            dataProviderClass = io.github.alikursady.qa.data.LoginData.class,
            description = "Rejected logins show the matching error")
    public void invalidLoginShowsError(String user, String pass, String expectedError) {
        LoginPage page = new LoginPage(driver())
                .open()
                .loginExpectingError(user, pass);

        assertEquals(page.errorMessage(), expectedError);
    }
}
