package io.github.alikursady.qa.tests;

import io.github.alikursady.qa.BaseTest;
import io.github.alikursady.qa.pages.internet.FramePage;
import io.github.alikursady.qa.pages.internet.JavaScriptAlertsPage;
import io.github.alikursady.qa.pages.internet.NestedFramesPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Feature("Frames and dialogs")
public class FrameAndAlertTest extends BaseTest {

    @Test(description = "Content inside an iframe is reachable and the parent document is restored")
    public void readsAcrossTheFrameBoundary() {
        FramePage page = new FramePage(driver()).open();

        assertEquals(page.editorText(), "Your content goes here.");
        assertEquals(page.headingText(), "An iFrame containing the TinyMCE WYSIWYG Editor",
                "Should be back on the parent document after the frame switch");
    }

    @Test(description = "Nested frames can be entered and stepped back out of")
    public void navigatesNestedFrames() {
        NestedFramesPage page = new NestedFramesPage(driver()).open();

        assertEquals(page.textInTopChild("frame-middle"), "MIDDLE");
        assertEquals(page.textInSiblingViaParent("frame-middle", "frame-left"), "LEFT");
        assertEquals(page.bottomText(), "BOTTOM");
    }

    @Test(description = "Accepting, dismissing and answering native dialogs")
    public void nativeDialogsAreHandled() {
        JavaScriptAlertsPage page = new JavaScriptAlertsPage(driver()).open();

        assertEquals(page.acceptAlert(), "You successfully clicked an alert");
        assertEquals(page.dismissConfirm(), "You clicked: Cancel");
        assertEquals(page.answerPrompt("hello"), "You entered: hello");
    }
}
