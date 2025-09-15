package utils.pageFactory;

import com.microsoft.playwright.*;
import utils.configReader.ConfigManager;

import java.util.concurrent.Semaphore;

public class PlaywrightFactory {
    private static final int MAX_BROWSERS = ConfigManager.getIntProperty("maximumBrowser");
    private static final Semaphore browserSemaphore = new Semaphore(MAX_BROWSERS);

    public static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    public static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    public static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    public static final ThreadLocal<Page> page = new ThreadLocal<>();

    private static final ThreadLocal<BrowserTypeEnum> browserType = ThreadLocal.withInitial(() -> {

        if (ConfigManager.getBooleanProperty("useBrowserTypeDistribution")) {
            int threadId = (int) (Thread.currentThread().getId() % 3);
            System.out.println("threadId = " + threadId);
            return switch (threadId) {
                case 0 -> BrowserTypeEnum.CHROMIUM;
                case 1 -> BrowserTypeEnum.FIREFOX;
                default -> BrowserTypeEnum.WEBKIT;
            };
        } else {
            return BrowserTypeEnum.valueOf(ConfigManager.getProperty("browserType"));
        }
    });
/**
    Thread 0: CHROMIUM
    Thread 1: FIREFOX
    Thread 2: WEBKIT
    Thread 3: CHROMIUM
    CHROMIUM: 2 thread (Thread 0 ve Thread 3)
    FIREFOX: 1 thread (Thread 1)
    WEBKIT: 1 thread (Thread 2)

    2 thread Chromium browser
    1 thread Firefox browser
    1 thread Webkit browser
 */

//    private static boolean offlineStatus = false;
//    private static int latency = 0;
//    private static int downloadThroughput = 0;
//    private static int uploadThroughput = 0;
    public static synchronized Playwright getPlaywright() {
        if (playwright.get() == null) {
            playwright.set(Playwright.create());
        }
        return playwright.get();
    }

    public static BrowserContext createBrowserContext() {
        try {
            Browser newBrowser = null;
            boolean headless = isHeadlessMode();
            browserSemaphore.acquire(); // Maksimum browser


            newBrowser = switch (browserType.get()) {
                case FIREFOX -> getPlaywright().firefox().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                );
                case WEBKIT ->getPlaywright().webkit().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                );
                default -> getPlaywright().chromium().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless)
                );
            };
            browser.set(newBrowser);

            BrowserContext newContext = newBrowser.newContext();

//            if (ConfigManager.getBooleanProperty("throttleNetwork")) {
//                emulateNetworkConditions(newContext);
//            }

            context.set(newContext);

            return newContext;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("An error occurred while creating browser context", e);
        }
    }

    public static boolean isHeadlessMode() {
        String headlessEnv = System.getenv("HEADLESS");
        if(headlessEnv != null && headlessEnv.equalsIgnoreCase("true")){
            return true;
        }else {
            return ConfigManager.getBooleanProperty("headless");
        }
    }



    public static void closeBrowser() {
        try {
            if (browser.get() != null) {
                browser.get().close();
                browser.remove();
            }
            if (playwright.get() != null) {
                playwright.get().close();
                playwright.remove();
            }
            context.remove();
            page.remove();
        } finally {
            browserSemaphore.release();
        }
    }



    /**
     * Emulate Network Speed
     */

//    public static void applyNetworkConditionsToAllContexts() {
//        Browser currentBrowser = browser.get();
//        if (currentBrowser != null) {
//            for (BrowserContext existingContext : currentBrowser.contexts()) {
//                emulateNetworkConditions(existingContext);
//            }
//        }
//    }
//
//
//    static {
//        offlineStatus = NetworkThrottleManager.getBooleanProperty("offline");
//        latency = NetworkThrottleManager.getIntProperty("latency");
//        downloadThroughput = NetworkThrottleManager.getIntProperty("downloadThroughput");
//        uploadThroughput = NetworkThrottleManager.getIntProperty("uploadThroughput");
//    }
//
//    private static void emulateNetworkConditions(BrowserContext browserContext) {
//
//        //browserContext.setOffline(false);
//        Page simulationPage = browserContext.newPage();
//        CDPSession cdpSession = simulationPage.context().newCDPSession(simulationPage);
//
//        JsonObject networkConditions = new JsonObject();
//        networkConditions.addProperty("offline", offlineStatus);                  // Online
//        networkConditions.addProperty("latency", latency);                   // 500ms latency
//        networkConditions.addProperty("downloadThroughput", downloadThroughput * 1024 / 8); // 500 kbps download
//        networkConditions.addProperty("uploadThroughput", uploadThroughput * 1024 / 8);   // 500 kbps upload
//
//        // send CDP command
//        cdpSession.send("Network.enable", new JsonObject());
//        cdpSession.send("Network.emulateNetworkConditions", networkConditions);
//        simulationPage.close(); // close simulation page
//    }
}