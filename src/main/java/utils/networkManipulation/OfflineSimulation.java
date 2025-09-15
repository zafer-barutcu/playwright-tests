package utils.networkManipulation;

import com.google.gson.JsonObject;
import com.microsoft.playwright.*;

public class OfflineSimulation {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();

            // Sayfa oluştur ve CDP oturumu başlat
            Page page = context.newPage();
            CDPSession cdpSession = page.context().newCDPSession(page);

            // CDP üzerinden offline modunu etkinleştir
            cdpSession.send("Network.enable", new JsonObject());

            JsonObject offlineConditions = new JsonObject();
            offlineConditions.addProperty("offline", false); // Çevrimdışı mod
            offlineConditions.addProperty("latency", 10);    // Gecikme gerekmiyor
            offlineConditions.addProperty("downloadThroughput", 500); // İndirme yok
            offlineConditions.addProperty("uploadThroughput", 500);   // Yükleme yok

            cdpSession.send("Network.emulateNetworkConditions", offlineConditions);

            // Test için bir URL açmayı deneyin
            try {
                page.navigate("https://example.com");
                System.out.println("Sayfa başlığı: " + page.title());
            } catch (Exception e) {
                System.out.println("Bağlantı başarısız: Çevrimdışı mod etkin!");
            }

            browser.close();
        }
    }
}

