package utils;

import java.util.UUID;

public class DataHelper {
    private DataHelper(){}

    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String generateUniqueFirstName() {
        return "FirstName_" + getTimestamp();
    }

    public static String generateUniqueLastName() {
        return "LastName_" + getTimestamp();
    }

    public static String generateUniqueUsername() {
        return "User_" + getTimestamp();
    }
}
