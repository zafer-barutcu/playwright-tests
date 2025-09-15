package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import hooks.CucumberHooks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmartLocator {
    private Map<String, Object> locatorConfig;

    public SmartLocator( String configFile) {
        loadLocatorConfig(configFile);
    }

    private void loadLocatorConfig(String configFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            locatorConfig = mapper.readValue(
                    new File(configFile),
                    Map.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Locator config issue", e);
        }
    }

    public Locator find(String elementKey) {
        List<String> locatorStrategies = getLocatorStrategies(elementKey);
        Locator locator = null;
        for (String locatorStrategy : locatorStrategies) {
            try {
                locator = resolveLocator(locatorStrategy);
                if (locator.count() > 0) {
                    return locator;
                }
            } catch (Exception e) {
              //  saveLocatorImage(locator,elementKey);
                e.printStackTrace();
            }
        }

        throw new RuntimeException("Locator is not found: " + elementKey);
    }

    private void saveLocatorImage(Locator locator, String elementKey){
        try{
            byte[] screenshots = locator.screenshot();
            System.out.println("screenshot.length = " + screenshots.length);

            // byte[]'i BufferedImage'e dönüştür
            ByteArrayInputStream bis = new ByteArrayInputStream(screenshots);
            BufferedImage screenshot = ImageIO.read(bis);

            if (screenshot == null) {
                System.err.println("Görseli okuma işlemi başarısız oldu.");
            }

            // Görseli kaydet
            String IMAGE_DIR = System.getProperty("user.dir") + "/locator_images/"; // Proje kök dizininden
            File outputDir = new File(IMAGE_DIR);
            if (!outputDir.exists()) {
                boolean created = outputDir.mkdirs(); // Dizin yoksa oluştur
                if (created) {
                    System.out.println("Dizin oluşturuldu: " + outputDir.getAbsolutePath());
                } else {
                    System.out.println("Dizin zaten var veya oluşturulamadı.");
                }
            }
            File outputFile = new File(outputDir, elementKey + ".png");
            boolean saved = ImageIO.write(screenshot, "png", outputFile); // Görseli kaydetmeye çalış

            if (saved) {
                System.out.println("Görsel başarıyla kaydedildi: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("Görsel kaydedilemedi.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private List<String> getLocatorStrategies(String elementKey) {
        List<String> strategies = new ArrayList<>();
        Map<String, Object> elementConfig = (Map<String, Object>) locatorConfig.get(elementKey);

        if (elementConfig == null) {
            throw new RuntimeException("Element configuration not found: " + elementKey);
        }

        // Priority
        if (elementConfig.containsKey("css"))
            strategies.add("css:" + elementConfig.get("css"));
        if (elementConfig.containsKey("xpath"))
            strategies.add("xpath:" + elementConfig.get("xpath"));
        if (elementConfig.containsKey("text"))
            strategies.add("text:" + elementConfig.get("text"));

        return strategies;
    }

    private Locator resolveLocator(String locatorStrategy) {
        String[] parts = locatorStrategy.split(":", 2);
        String type = parts[0];
        String selector = parts[1];

        switch (type) {
            case "css":
            case "xpath":
                return CucumberHooks.page.get().locator(selector);
            case "text":
                return CucumberHooks.page.get().getByText(selector);
            default:
                throw new RuntimeException("Unsupported locator type: " + type);
        }
    }
}

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microsoft.playwright.Locator;
//import hooks.CucumberHooks;
//import org.sikuli.script.Pattern;
//import org.sikuli.script.Screen;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import javax.imageio.ImageIO;
//
//public class SmartLocator {
//    private Map<String, Object> locatorConfig;
//    private static final String IMAGE_DIR = System.getProperty("user.dir") + "/locator_images/"; // Proje kök dizininden
//
//    public SmartLocator(String configFile) {
//        loadLocatorConfig(configFile);
//    }
//
//    private void loadLocatorConfig(String configFile) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            locatorConfig = mapper.readValue(
//                    new File(configFile),
//                    Map.class
//            );
//        } catch (IOException e) {
//            throw new RuntimeException("Locator config issue", e);
//        }
//    }
//
//    public Locator find(String elementKey) {
//        List<String> locatorStrategies = getLocatorStrategies(elementKey);
//
//        for (String locatorStrategy : locatorStrategies) {
//            try {
//                Locator locator = resolveLocator(locatorStrategy);
//                if (locator.count() > 0) {
//                    return locator;
//                }
//            } catch (Exception ignored) {
//            }
//        }
//
//        // Eğer bulamazsa görsel tabanlı deneme yap
//        try {
//            return findWithImage(elementKey);
//        } catch (Exception e) {
//            captureElementImage(elementKey); // Görsel kaydet
//            throw new RuntimeException("Locator and image-based search failed: " + elementKey);
//        }
//    }
//
//    private List<String> getLocatorStrategies(String elementKey) {
//        List<String> strategies = new ArrayList<>();
//        Map<String, Object> elementConfig = (Map<String, Object>) locatorConfig.get(elementKey);
//
//        if (elementConfig == null) {
//            captureElementImage(elementKey); // Görsel kaydet
//            throw new RuntimeException("Element configuration not found: " + elementKey);
//        }
//
//        // Öncelik sırasına göre locator'ları ekle
//        if (elementConfig.containsKey("css"))
//            strategies.add("css:" + elementConfig.get("css"));
//        if (elementConfig.containsKey("xpath"))
//            strategies.add("xpath:" + elementConfig.get("xpath"));
//        if (elementConfig.containsKey("text"))
//            strategies.add("text:" + elementConfig.get("text"));
//
//        return strategies;
//    }
//
//    private Locator resolveLocator(String locatorStrategy) {
//        String[] parts = locatorStrategy.split(":", 2);
//        String type = parts[0];
//        String selector = parts[1];
//
//        switch (type) {
//            case "css":
//            case "xpath":
//                return CucumberHooks.page.get().locator(selector);
//            case "text":
//                return CucumberHooks.page.get().getByText(selector);
//            default:
//                throw new RuntimeException("Unsupported locator type: " + type);
//        }
//    }
//
//    private Locator findWithImage(String elementKey) throws Exception {
//        Screen screen = new Screen();
//        File imageFile = new File(IMAGE_DIR + elementKey + ".png");
//
//        if (!imageFile.exists()) {
//            throw new RuntimeException("Image not found for element: " + elementKey);
//        }
//
//        Pattern pattern = new Pattern(imageFile.getAbsolutePath());
//        if (screen.exists(pattern) != null) {
//            screen.click(pattern);
//            return null;
//        }
//
//        throw new RuntimeException("Image-based search failed for element: " + elementKey);
//    }
//
//    private void captureElementImage(String elementKey) {
//        try {
//            // Elementi bul
//            Locator element = CucumberHooks.page.get().locator(elementKey);
//            byte[] screenshotBytes = element.screenshot(new Locator.ScreenshotOptions().setQuality(7));  // Full page değil, sadece elementin görüntüsünü al
//            System.out.println("Element screenshot alınan byte dizisinin uzunluğu: " + screenshotBytes.length);
//
//            // byte[]'i BufferedImage'e dönüştür
//            ByteArrayInputStream bis = new ByteArrayInputStream(screenshotBytes);
//            BufferedImage screenshot = ImageIO.read(bis);
//
//            // Görseli kaydet
//            File outputDir = new File(IMAGE_DIR);
//            if (!outputDir.exists()) {
//                boolean created = outputDir.mkdirs(); // Dizin yoksa oluştur
//                if (created) {
//                    System.out.println("Dizin oluşturuldu: " + outputDir.getAbsolutePath());
//                } else {
//                    System.out.println("Dizin zaten var veya oluşturulamadı.");
//                }
//            }
//
//            // Görseli kaydet
//            File outputFile = new File(outputDir, elementKey + ".png");
//            boolean saved = ImageIO.write(screenshot, "png", outputFile); // Görseli kaydetmeye çalış
//
//            if (saved) {
//                System.out.println("Görsel başarıyla kaydedildi: " + outputFile.getAbsolutePath());
//            } else {
//                System.out.println("Görsel kaydedilemedi.");
//            }
//        } catch (IOException e) {
//            System.err.println("Resim alma veya kaydetme sırasında hata oluştu: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Failed to capture image for element: " + elementKey, e);
//        }
//    }
//
//
//}
