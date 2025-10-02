package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
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

    public static String getInvalidUsername(){
        return getProperty("INVALIDUSERNAME");
    }

    public static String getInvalidPassword(){
        return getProperty("INVALIDPASSWORD");
    }

    public static String getInvalidStatus(){
        return getProperty("INVALIDSTATUS");
    }

    public static String getEmptyUsername(){
        return getProperty("EMPTYUSERNAME");
    }
    public static String getEmptyPassword(){
        return getProperty("EMPTYUSERPASSWORD");
    }
    public static String getEmptyStatus(){
        return getProperty("EMPTYSTATUS");
    }
}
