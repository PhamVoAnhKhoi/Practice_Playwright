package tests;

import base.AuthenticatedBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.AccountData;
import utils.DataHelper;
//import utils.AccountData;
//import utils.ConfigReader;
//import utils.DataHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserLifecycleTests extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);
    AddEmployeePage addEmployeePage;
    PIMPage pimPage;
    UserManagementPage userManagementPage;
    AddUserPage addUserPage;
    String uniqueFirstName;
    String uniqueLastName;
    String uniqueFullName;
    String uniqueUserName;
    String uniqueUserId;

    @BeforeClass
    public void generateAccount(){
        log.info("======== Generate unique data for User ========");
        uniqueFirstName = DataHelper.generateUniqueFirstName();
        log.info("FirstName: " + uniqueFirstName);
        uniqueLastName = DataHelper.generateUniqueLastName();
        log.info("LastName: " + uniqueLastName);
        uniqueFullName = uniqueFirstName + "  " + uniqueLastName;
        log.info("FullName: " + uniqueFullName);
        uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("Unique: " + uniqueUserName);
        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id: " +uniqueUserId);
    }

    @BeforeMethod
    public void setUpUserLifecycle(){
        addEmployeePage = new AddEmployeePage(page);
        pimPage = new PIMPage(page);
        userManagementPage = new UserManagementPage(page);
        addUserPage = new AddUserPage(page);
    }

    @Test(description = "Test flow: Create Employee & User")
    @Severity(SeverityLevel.NORMAL)
    public void createEmployeeAndUser(){
        createEmployee();
        createUser();
    }

    @Test(description = "Test flow: Delete Empolyee & User",
            dependsOnMethods = {"createEmployeeAndUser"})
    @Description("Clean up data after test")
    @Severity(SeverityLevel.NORMAL)
    public void deleteEmployeeAndUser(){
        deleteUser();
        deleteEmployee();
    }

    private void createEmployee(){
        log.info("======== Create Employee ========");
        pimPage.clickPIMSideBarButton();
        addEmployeePage.navigateToAddEmployeePage();
        addEmployeePage.addEmployee(uniqueFirstName,uniqueLastName,uniqueUserId);
        //addEmployeePage.clickCreateLoginDetailsButton();
        //addEmployeePage.addDetailsUser(uniqueUserName, AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addEmployeePage.clickSaveButton();
        assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

    }

    private void createUser(){
        log.info("======== Create user ========");
        userManagementPage.clickAdminSideBarButton();
        assertThat(userManagementPage.isHeaderTitleVisible())
                .as("Header tittle must be visible")
                .isTrue();
        addUserPage.clickAddButton();
        assertThat(addUserPage.isAddUserFormVisible())
                .as("Form Add User must be visible")
                .isTrue();
        addUserPage.selectUserRole();
        addUserPage.selectStatus();
        addUserPage.inputUserInfo(uniqueFullName, uniqueUserName, AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addUserPage.clickSaveButton();
        assertThat(addUserPage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        //check user is visible
        log.info("======== Check user is visible in table ========");
        userManagementPage.clickAdminSideBarButton();
        userManagementPage.searchUsername(uniqueUserName);
        userManagementPage.waitForSearchResult();
        assertThat(userManagementPage.isUsenameVisibleInTable(uniqueUserName)).isTrue();
        log.info("Username is visible in table");
    }

    private void deleteUser(){
        log.info("======== Delete user ========");
        userManagementPage.clickAdminSideBarButton();
        userManagementPage.searchUsername(uniqueUserName);
        userManagementPage.waitForSearchResult();
        userManagementPage.deleteUser();
        assertThat(userManagementPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete user Successfully");

        log.info("======== Check user ========");
        userManagementPage.searchUsername(uniqueUserName);
        assertThat(userManagementPage.isUsernameInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        log.info("No record is found");

    }

    private void deleteEmployee(){
        log.info("======== Delete employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueFullName);
        pimPage.waitForSearchResult();
        pimPage.deleteEmployee();
        assertThat(pimPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");

        log.info("======== Check employee ========");
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueFullName);
        assertThat(pimPage.isEmployeeInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        log.info("No record is found");
    }

}
