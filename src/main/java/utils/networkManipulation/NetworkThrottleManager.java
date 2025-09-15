package utils.networkManipulation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class NetworkThrottleManager {

    private static Properties properties;

    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/network.properties")) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
