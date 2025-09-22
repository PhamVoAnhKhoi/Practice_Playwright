package utils;

public class AccountData {
    public String userName;
    public String passWord;
    public String invalidUserName;
    public String invalidPassWord;
    public String invalidStatus;
    public String emptyUserName;
    public String emptyPassWord;
    public String emptyStatus;


    public AccountData(){
        this.userName = "Admin";
        this.passWord = "admin123";
        this.invalidUserName = "InternTMA";
        this.invalidPassWord = "Abc@123";
        this.invalidStatus = "Invalid credentials";
        this.emptyUserName = "";
        this.emptyPassWord = "";
        this.emptyStatus = "Required";
    }


}
