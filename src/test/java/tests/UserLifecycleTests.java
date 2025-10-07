package tests;

import base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AddEmployeePage;
import pages.EmployeeListPage;
import pages.LoginPage;
import pages.PIMPage;
import utils.AccountData;
import utils.ConfigReader;
import utils.DataHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserLifecycleTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);
    LoginPage loginPage;
    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    EmployeeListPage employeeListPage;
    String uniqueFirstName;
    String uniqueLastName;
    String uniqueUserName;
    String uniqueUserId;
    @BeforeMethod
    public void setUpUserLifecycle(){
        loginPage = new LoginPage(page);
        pimPage = new PIMPage(page);
        addEmployeePage = new AddEmployeePage(page);
        employeeListPage = new EmployeeListPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(ConfigReader.getAdminUser(),ConfigReader.getAdminPassword());
        pimPage.clickPIMSideBarButton();
        genarateAccount();
    }

    @Test
    public void createEmployeeAndUser(){
        addEmployeePage.navigateToAddEmployeePage();
        addEmployeePage.addEmployee(uniqueFirstName,uniqueLastName,uniqueUserId);
        addEmployeePage.clickCreateLoginDetailsButton();
        addEmployeePage.addDetailsUser(uniqueUserName, AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        page.waitForTimeout(5000);
        addEmployeePage.clickSaveButton();
        assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");
    }


    public void genarateAccount(){
        uniqueFirstName = DataHelper.generateUniqueFirstName();
        log.info("FirstName: " + uniqueFirstName);
        uniqueLastName = DataHelper.generateUniqueLastName();
        log.info("LastName" + uniqueLastName);
        uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("Unique:" + uniqueUserName);
        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id:" +uniqueUserId);
    }

    @AfterMethod
    public void resetLifecycle(){
        employeeListPage.navigateToEmployeeListPage();
        employeeListPage.searchEmployeeById(uniqueUserId);
        employeeListPage.deleteEmployee();
        assertThat(employeeListPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");
    }
}
