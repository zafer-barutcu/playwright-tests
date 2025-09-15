package stepdefinitions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import hooks.CucumberHooks;

public abstract class BaseTests {

    private static void waitForElement(String selector) {
        CucumberHooks.page.get().waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
    }

    protected static void clickElement(String selector) {
        waitForElement(selector);
        CucumberHooks.page.get().locator(selector).click();
    }
    protected static void fillField(Locator locator, String value) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.fill(value);
    }
    public static void fillSequentally(String selector,String value) {
        waitForElement(selector);
        CucumberHooks.page.get().locator(selector).click();
        CucumberHooks.page.get().locator(selector).pressSequentially(value);
    }
    public static String getText(String selector) {
        waitForElement(selector);
        return CucumberHooks.page.get().locator(selector).textContent();
    }
    public static String getInnerText(String selector) {
        waitForElement(selector);
        return CucumberHooks.page.get().locator(selector).innerHTML();
    }
    public static String getAttribute(String selector, String attribute) {
        waitForElement(selector);
        return CucumberHooks.page.get().locator(selector).getAttribute(attribute);
    }
    public static boolean isVisible(String selector) {
        waitForElement(selector);
        return CucumberHooks.page.get().locator(selector).isVisible();
    }
    public static void waitForText(String selector, String text) {
        waitForElement(selector);
        CucumberHooks.page.get().locator(selector).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        String elementText = CucumberHooks.page.get().locator(selector).locator(selector).textContent();
        if (elementText != null && elementText.contains(text)) {
            return;
        }
        throw new RuntimeException("Text not found in element: " + selector);
    }
    public static void scrollIntoView(String selector) {
        waitForElement(selector);
        CucumberHooks.page.get().locator(selector).scrollIntoViewIfNeeded();
    }
    public static void hitKeyboardButton(String button){
        CucumberHooks.page.get().keyboard().press(button);
    }



}
