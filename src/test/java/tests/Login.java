package tests;

import base.baseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.AccountData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Login extends baseTest {

    pages.LoginPage loginPage;
    String username = "username";
    String password = "password";
    AccountData accountData = new AccountData();

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

    //Unhappy Test
    //Invalid password
    @Test
    public void LoginWithInvalidPassword(){
        loginPage = new pages.LoginPage(page);
        //Navigate to Login Page
        loginPage.navigateToLoginPage();

        //Enter Account
        loginPage.loginAccount(accountData.userName,accountData.invalidPassWord);

        //Click Login button
        loginPage.clickloginButton();

        String err = loginPage.getinvalidError();
        System.out.println("Error: " + err);
        assertThat(err).isEqualTo(accountData.invalidStatus);
    }

    //Empty Account
    @Test
    public void LoginWithEmptyAccount(){
        loginPage = new pages.LoginPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(accountData.emptyUserName,accountData.emptyPassWord);//Username & Password is Empty
        loginPage.clickloginButton();

        List<String> err = loginPage.getRequiredMessages();
        System.out.println("Errors: " + err);

        assertThat(err).containsExactlyInAnyOrder(accountData.emptyStatus, accountData.emptyStatus);
    }
    //Empty Username
    @Test
    public void LoginWithEmptyUsername(){
        loginPage = new pages.LoginPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(accountData.emptyUserName,accountData.passWord);//Nhập Password đúng
        loginPage.clickloginButton();

        List<String> err = loginPage.getRequiredMessages();
        System.out.println("Errors: " + err);

        assertThat(err).containsExactlyInAnyOrder(accountData.emptyStatus);
    }

    //Empty Password
    @Test
    public void LoginWithEmptyPassword(){
        loginPage = new pages.LoginPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(accountData.userName,accountData.emptyPassWord);//Password is Empty
        loginPage.clickloginButton();

        List<String> err = loginPage.getRequiredMessages();
        System.out.println("Errors: " + err);

        assertThat(err).containsExactlyInAnyOrder(accountData.emptyStatus);
    }
}
