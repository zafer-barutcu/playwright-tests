package hooks;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.pageFactory.PlaywrightFactory;
import utils.SmartLocator;

import java.util.Arrays;


public class CucumberHooks {


    public static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    public static final ThreadLocal<Page> page = new ThreadLocal<>();
    public static SmartLocator smartLocator;
    @Before
    public void setUp() {
            context.set(PlaywrightFactory.createBrowserContext());
            page.set(context.get().newPage());
           smartLocator = new SmartLocator("src/test/resources/locators/homepage.json");
    }

    @After
    public void tearDown() {
            if (page.get() != null) {
                page.get().close();
            }
            if (context.get() != null) {
                context.get().close();
            }
            PlaywrightFactory.closeBrowser();

            // ThreadLocal temizliği
            context.remove();
            page.remove();
        }

}
