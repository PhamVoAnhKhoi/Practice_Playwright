package utils;

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
}
