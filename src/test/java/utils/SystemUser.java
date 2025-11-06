package utils;

import java.util.Objects;

public class SystemUser {
    private String username;
    private String userRole;
    private String employeeName;
    private String status;

    public SystemUser(String username, String userRole, String employeeName, String status) {
        this.username = username;
        this.userRole = userRole;
        this.employeeName = employeeName;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "SystemUser{" +
                "username='" + username + '\'' +
                ", userRole='" + userRole + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemUser)) return false;
        SystemUser that = (SystemUser) o;
        return Objects.equals(username, that.username)
                && Objects.equals(userRole, that.userRole)
                && Objects.equals(employeeName, that.employeeName)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userRole, employeeName, status);
    }
}
