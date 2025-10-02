package utils;

public class AccountData {
    private AccountData(){}

    public static final String ADMINUSERNAME = ConfigReader.getAdminUser();
    public static final String ADMINPASSWORD = ConfigReader.getAdminPassword();
    public static final String INVALIDPASSWORD = ConfigReader.getInvalidPassword();
    public static final String INVALIDSTATUS = ConfigReader.getInvalidStatus();
    public static final String EMPTYUSERNAME = ConfigReader.getEmptyUsername();
    public static final String EMPTYUSERPASSWORD = ConfigReader.getEmptyPassword();
    public static final String EMPTYSTATUS = ConfigReader.getEmptyStatus();
}
