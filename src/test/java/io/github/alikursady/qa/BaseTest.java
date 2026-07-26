package io.github.alikursady.qa;

import io.github.alikursady.qa.driver.DriverFactory;
import io.github.alikursady.qa.driver.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DriverManager.set(DriverFactory.create());
    }

    /**
     * The screenshot is taken here rather than in an ITestListener because
     * TestNG runs @AfterMethod before the listener callback, so by the time
     * onTestFailure fires the driver would already be gone.
     */
    @AfterMethod(alwaysRun = true)
    public void stopBrowser(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && DriverManager.isSet()) {
            attachScreenshot(result.getMethod().getMethodName());
        }
        DriverManager.quit();
    }

    private void attachScreenshot(String name) {
        try {
            byte[] png = ((TakesScreenshot) DriverManager.get()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), "png");
        } catch (RuntimeException e) {
            // A dead session cannot be photographed; losing the screenshot must
            // not replace the real failure in the report.
            System.err.println("Could not capture screenshot for " + name + ": " + e.getMessage());
        }
    }

    protected WebDriver driver() {
        return DriverManager.get();
    }
}
