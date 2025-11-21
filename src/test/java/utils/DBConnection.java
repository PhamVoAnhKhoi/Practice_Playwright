package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        ConfigReader.getDBUrl(),
                        ConfigReader.getDBUsername(),
                        ConfigReader.getDBPassword()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to DB: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Cannot close connection: " + e.getMessage());
        }
    }
}
