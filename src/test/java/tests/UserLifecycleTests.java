package tests;

import base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.LoginPage;
import pages.PIMPage;
import utils.ConfigReader;
import utils.DataHelper;

public class UserLifecycleTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);
    LoginPage loginPage;
    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    @BeforeMethod
    public void setUpUserLifecycle(){
        loginPage = new LoginPage(page);
        pimPage = new PIMPage(page);
        addEmployeePage = new AddEmployeePage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(ConfigReader.getAdminUser(),ConfigReader.getAdminPassword());
        pimPage.clickPIMSideBarButton();
    }

    @Test
    public void createEmployeeAndUser(){
        String uniqueFirstName = DataHelper.generateUniqueFirstName();

        String uniqueLastName = DataHelper.generateUniqueLastName();

        String uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("Unique:" + uniqueUserName);

        addEmployeePage.clickAddEmployeeButton();
    }
}
