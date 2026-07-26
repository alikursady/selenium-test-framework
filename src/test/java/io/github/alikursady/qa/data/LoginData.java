package io.github.alikursady.qa.data;

import io.github.alikursady.qa.config.Config;
import org.testng.annotations.DataProvider;

public final class LoginData {

    private LoginData() {
    }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return new Object[][]{
                {"locked_out_user", Config.get("valid.password"),
                        "Epic sadface: Sorry, this user has been locked out."},
                {"standard_user", "wrong_password",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"", Config.get("valid.password"),
                        "Epic sadface: Username is required"},
                {Config.get("standard.user"), "",
                        "Epic sadface: Password is required"},
        };
    }

    @DataProvider(name = "products")
    public static Object[][] products() {
        return new Object[][]{
                {"Sauce Labs Backpack"},
                {"Sauce Labs Bike Light"},
                {"Sauce Labs Onesie"},
        };
    }
}
