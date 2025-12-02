package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.UserLifecycleTests;

import java.sql.ResultSet;

public class DBSetupUtils {

    private static final Logger log = LoggerFactory.getLogger(DBSetupUtils.class);

    public static void createUserTableIfNotExists(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id SERIAL PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "user_role VARCHAR(50) NOT NULL," +
                "employee_name VARCHAR(100) NOT NULL," +
                "status VARCHAR(50) NOT NULL" +
                ")";
        DBUtils.executeUpdate(sql);
        log.info("User table created (if not exists)");
    }

    public static void dropUserTableIfExists(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        DBUtils.executeUpdate(sql);
        log.info("User table dropped (if exists)");
    }

    public static void truncateUserTable(String tableName) {
        String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE";
        DBUtils.executeUpdate(sql);
        log.info("User table truncated");
    }

    public static boolean isTableVisible(String tableName){
        String sql = "SELECT true FROM information_schema.tables " +
                "WHERE table_name = '" + tableName + "'";
        try {
            ResultSet rs = DBUtils.executeQuery(sql);
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (Exception e) {
            throw new RuntimeException("DB setup check failed: " + e.getMessage());
        }
    }

    public static boolean isTableDisible(String tableName){
        return !isTableVisible(tableName);
    }

}
