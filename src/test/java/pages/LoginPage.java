package pages;

import base.baseTest;
import models.Login;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginPage extends baseTest {

    Login login;

    @BeforeClass
    public void initPageObjects() {
        login = new Login(page);  // Page đã có từ baseTest.setUp()
    }

    //Happy Test
    @Test
    public void happyPathLogin(){
        //Navigate to Login Page
        login.navigateToLoginPage();

        //Enter Account
        login.loginAccount("Admin","admin123");//Nhập Password đúng

        //Click Login button
        login.clicklogin();
        page.waitForSelector("text=Dashboard");
        assertThat(page.url()).contains("dashboard");
    }
}
