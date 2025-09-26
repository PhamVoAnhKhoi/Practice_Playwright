package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminPage;
import pages.LoginPage;
import utils.AccountData;

public class Admin extends BaseTest {
    LoginPage loginPage;
    AdminPage adminPage;
    @BeforeMethod
    public void setUpAdminPage(){
        loginPage = new LoginPage(page);
        adminPage = new AdminPage(page);

        loginPage.navigateToLoginPage();
        loginPage.loginAccount(AccountData.userName,AccountData.passWord);
        adminPage.clickAdminSideBarButton();
    }

    @Test(description = "Get colum data from table")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User managerment table")
    public void takeDataFromTable(){
        adminPage.getTableColData();
    }


    @Test(description = "Visible element")
    @Description("Check element is visible")
    public void checkElement(){
        adminPage.checkVisible();
    }

}
