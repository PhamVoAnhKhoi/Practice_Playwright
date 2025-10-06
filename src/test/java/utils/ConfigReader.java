package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")){
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getLoginPageUrl() {
        return getProperty("LOGINPAGEURL");
    }

    public static String getAdminUser() {
        return getProperty("ADMINUSERNAME");
    }

    public static String getAdminPassword() {
        return getProperty("ADMINPASSWORD");
    }

}
