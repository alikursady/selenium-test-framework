package io.github.alikursady.qa.tests;

import io.github.alikursady.qa.BaseTest;
import io.github.alikursady.qa.pages.internet.DynamicControlsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Feature("Dynamic elements")
public class DynamicElementTest extends BaseTest {

    @Test(description = "Removing the checkbox reports back once it is gone")
    public void checkboxCanBeRemoved() {
        String message = new DynamicControlsPage(driver()).open().removeCheckbox();
        assertEquals(message, "It's gone!");
    }

    @Test(description = "The disabled input becomes editable after the toggle")
    public void inputBecomesEnabled() {
        DynamicControlsPage page = new DynamicControlsPage(driver()).open();
        assertFalse(page.isInputEnabled(), "Input should start disabled");
        assertTrue(page.enableInput(), "Input should be enabled after the toggle");
    }
}
