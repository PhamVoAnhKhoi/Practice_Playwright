package utils;

import java.sql.*;

public class DBUtils {

    public static ResultSet executeQuery(String sql) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + e.getMessage());
        }
    }

    public static int executeUpdate(String sql) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    public static void executeDelete(String sql) {
        executeUpdate(sql);
    }
}
