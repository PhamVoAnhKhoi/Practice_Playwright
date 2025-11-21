package DAO;

import utils.DBUtils;
import utils.SystemUser;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static List<SystemUser> getAllUsersFromDB(String tableName) {
        List<SystemUser> users = new ArrayList<>();

        String query = "SELECT username, user_role, employee_name, status FROM " + tableName;

        try {
            ResultSet rs = DBUtils.executeQuery(query);
            while (rs.next()) {
                SystemUser user = new SystemUser(
                        rs.getString("username"),
                        rs.getString("user_role"),
                        rs.getString("employee_name"),
                        rs.getString("status")
                );
                users.add(user);
            }
            rs.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to map users: " + e.getMessage());
        }

        return users;
    }

    public static SystemUser getUserByUsername(String username, String tableName) {
        String query = "SELECT username, user_role, employee_name, status FROM " + tableName +
                " WHERE username = '" + username + "'";

        try {
            ResultSet rs = DBUtils.executeQuery(query);
            if (rs.next()) {
                SystemUser user = new SystemUser(
                        rs.getString("username"),
                        rs.getString("user_role"),
                        rs.getString("employee_name"),
                        rs.getString("status")
                );
                rs.close();
                return user;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user: " + e.getMessage());
        }

        return null;
    }

    public static void insertUser(SystemUser user, String tableName) {
        String sql = String.format(
                "INSERT INTO " + tableName + " (username, user_role, employee_name, status) " +
                        "VALUES ('%s', '%s', '%s', '%s')",
                user.getUsername(),
                user.getUserRole(),
                user.getEmployeeName(),
                user.getStatus()
        );

        DBUtils.executeUpdate(sql);
    }

    public static void deleteUserByUsername(String username, String tableName) {
        String sql = "DELETE FROM " + tableName + " WHERE username = '" + username + "'";
        DBUtils.executeDelete(sql);
    }

}
