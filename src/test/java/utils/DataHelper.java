package utils;

import java.security.SecureRandom;
import java.util.UUID;

public class DataHelper {
    private DataHelper(){}

    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private static final String CHAR_POOL =
//            "abcdefghijklmnopqrstuvwxyz" +  // chữ thường
//            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +  // chữ hoa
            "0123456789"; //+                  // số
//            "!@#$%^&*()-_=+[]{};:,.<>?";    // ký tự đặc biệt

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomUserId(int length){
        if (length <= 0 || length > 10){
            throw new IllegalArgumentException("Độ dài phải trong khoảng 1 - 10 ký tự.");
        }
        StringBuilder userId = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHAR_POOL.length());
            userId.append(CHAR_POOL.charAt(index));
        }

        return userId.toString();
    }

    public static String generateUniqueFirstName() {
        return "FirstName_" + getTimestamp();
    }

    public static String generateUniqueLastName() {
        return "LastName_" + getTimestamp();
    }

    public static String generateUniqueUsername() {
        return "Username_" + getTimestamp();
    }
}
