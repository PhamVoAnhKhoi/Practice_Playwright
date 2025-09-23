package tests;

import base.BaseTest;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.AccountData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Login")
@Listeners({AllureTestNg.class})
public class Login extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(Login.class);
    LoginPage loginPage;


    @BeforeMethod
    public void setUpLoginPage(){
        loginPage = new LoginPage(page);
        loginPage.navigateToLoginPage();
    }

    //Happy Test
    @Test (description = "Happy Path Login Test")
    @Description("Login successfully with correct Username & Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Happy Login Test")
    public void happyPathLogin(){
        loginPage.loginAccount(AccountData.userName,AccountData.passWord);//Nhập Password đúng
        assertThat(loginPage.isLoginSuccess()).as("User should be logged in").isTrue();
    }

    //Unhappy Test
    //Invalid password
    @Test (description = "Login with invalid Password")
    @Description("Login fail with correct Username & invalid Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithInvalidPassword(){
        loginPage.loginAccount(AccountData.userName,AccountData.invalidPassWord);
        String err = loginPage.getinvalidError();
        AssertionsForClassTypes.assertThat(err).isEqualTo(AccountData.invalidStatus);
    }

    //Empty Account
    @Test (description = "Login with empty Account")
    @Description("Login fail with empty Username & Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyAccount(){
        loginPage.loginAccount(AccountData.emptyUserName,AccountData.emptyPassWord);//Username & Password is Empty
        loginPage.emptyAccount();
    }
//    //Empty Username
    @Test (description = "Login with empty Username")
    @Description("Login fail with empty Username & correct Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyUsername(){
        loginPage.loginAccount(AccountData.emptyUserName,AccountData.passWord);//Nhập Password đúng
        loginPage.getRequiredMessages();
    }

    //Empty Password
    @Test (description = "Login with empty Password")
    @Description("Login fail with correct Username & empty Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyPassword(){
        loginPage.loginAccount(AccountData.userName,AccountData.emptyPassWord);//Password is Empty
        loginPage.getRequiredMessages();
    }
}
