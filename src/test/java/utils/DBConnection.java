package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.UserLifecycleTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);
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
            log.info("Cannot close connection: " + e.getMessage());
        }
    }
}
