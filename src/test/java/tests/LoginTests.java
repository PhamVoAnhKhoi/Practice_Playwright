package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.AccountData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Login")
public class LoginTests extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);
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
        loginPage.loginAccount(AccountData.ADMINUSERNAME,AccountData.ADMINPASSWORD);//Password is correct
        assertThat(loginPage.isLoginSuccess()).as("User should be logged in").isTrue();
    }

    //Unhappy Test
    //Invalid password
    @Test (description = "Login with invalid Password")
    @Description("Login fail with correct Username & invalid Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithInvalidPassword(){
        loginPage.loginAccount(AccountData.ADMINUSERNAME,AccountData.INVALIDPASSWORD);
        String err = loginPage.getInvalidError();
        AssertionsForClassTypes.assertThat(err).isEqualTo(AccountData.INVALIDSTATUS);
    }

    //Empty Account
    @Test (description = "Login with empty Account")
    @Description("Login fail with empty Username & Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyAccount(){
        loginPage.loginAccount(AccountData.EMPTYUSERNAME,AccountData.EMPTYUSERPASSWORD);//Username & Password is Empty
        List<String> err = loginPage.getAllRequiredMessages();
        assertThat(err).containsExactlyInAnyOrder(AccountData.EMPTYSTATUS,AccountData.EMPTYSTATUS);
        log.info("Errors: " + err);
    }
    //Empty Username
    @Test (description = "Login with empty Username")
    @Description("Login fail with empty Username & correct Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyUsername(){
        loginPage.loginAccount(AccountData.EMPTYUSERNAME,AccountData.ADMINPASSWORD);//Password is correct
        List<String> err = loginPage.getAllRequiredMessages();
        assertThat(err).containsExactlyInAnyOrder(AccountData.EMPTYSTATUS);
        log.info("Errors: " + err);
    }

    //Empty Password
    @Test (description = "Login with empty Password")
    @Description("Login fail with correct Username & empty Password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Unhappy Login Test")
    public void loginWithEmptyPassword(){
        loginPage.loginAccount(AccountData.ADMINUSERNAME,AccountData.EMPTYUSERPASSWORD);//Username is correct
        List<String> err = loginPage.getAllRequiredMessages();
        assertThat(err).containsExactlyInAnyOrder(AccountData.EMPTYSTATUS);
        log.info("Errors: " + err);
    }
}
