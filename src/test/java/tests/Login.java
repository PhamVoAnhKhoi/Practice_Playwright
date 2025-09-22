package tests;

import base.baseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.AccountData;

import static org.assertj.core.api.Assertions.assertThat;

public class Login extends baseTest {

    pages.LoginPage loginPage;
    String username = "username";
    String password = "password";
    AccountData accountData = new AccountData();

//    @BeforeMethod
//    public void initPageObjects() {
//        loginPage = new pages.LoginPage(page);  // Page đã có từ baseTest.setUp()
//    }

    //Happy Test
    @Test
    public void happyPathLogin(){
        loginPage = new pages.LoginPage(page);
        //Navigate to Login Page
        loginPage.navigateToLoginPage();

        //Enter Account
        loginPage.loginAccount(accountData.userName,accountData.passWord);//Nhập Password đúng

        //Click Login button
        loginPage.clickloginButton();

        loginPage.isLoginSuccess();
    }
}
