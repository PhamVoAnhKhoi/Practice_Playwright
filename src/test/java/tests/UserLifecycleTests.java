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
import utils.ScreenshotHelper;
import utils.SystemUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserLifecycleTests extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(UserLifecycleTests.class);
    AddEmployeePage addEmployeePage;
    PIMPage pimPage;
    UserManagementPage userManagementPage;
    AddUserPage addUserPage;
    String uniqueFirstName;
    String uniqueMiddleName;
    String uniqueLastName;
    String uniqueFullName;
    String uniqueUserName;
    String uniqueUserId;

    @BeforeClass
    public void prepareTestData(){
        log.info("======== Generate unique data for User ========");
        createUniqueData();
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
        verifyCreateUserSuccess();
    }

    @Test(description = "Test flow: Delete Empolyee & User",
            dependsOnMethods = {"createEmployeeAndUser"})
    @Description("Clean up data after test")
    @Severity(SeverityLevel.NORMAL)
    public void deleteEmployeeAndUser(){
        deleteUser();
        verifyDeleteUserSuccess();
        deleteEmployee();
        verifyDeleteEmployeeSuccess();
    }

    private void createUniqueData(){
        uniqueFirstName = DataHelper.generateUniqueFirstName();
        log.info("FirstName: " + uniqueFirstName);
        uniqueLastName = DataHelper.generateUniqueLastName();
        log.info("LastName: " + uniqueLastName);
        uniqueMiddleName = DataHelper.generateUniqueMiddleName();
        if(uniqueMiddleName == null){
            uniqueFullName = uniqueFirstName + " " + " "+ uniqueLastName;
        }
        else {
            uniqueFullName = uniqueFirstName + " " + uniqueMiddleName + " " + uniqueLastName;
        }
        log.info("FullName: " + uniqueFullName);
        uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("UserName: " + uniqueUserName);
        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id: " + uniqueUserId);
    }

    private void createEmployee(){
        log.info("======== Create Employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToAddEmployeePage();
        addEmployeePage.inputEmployeeInfo(uniqueFirstName, uniqueMiddleName, uniqueLastName, uniqueUserId);
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
        userManagementPage.navigateToAdminPage();
        assertThat(userManagementPage.isHeaderTitleVisible())
                .as("Header tittle must be visible")
                .isTrue();
        addUserPage.clickAddButton();
        assertThat(addUserPage.isAddUserFormVisible())
                .as("Form Add User must be visible")
                .isTrue();
        addUserPage.selectUserRole();
        addUserPage.selectStatus();
        addUserPage.inputEmployeeName(uniqueFirstName, uniqueFullName);
        addUserPage.inputUsername(uniqueUserName);
        addUserPage.inputPassword(AccountData.EMPLOYEEPASSWORD);
        addUserPage.inputConfirmPassword(AccountData.EMPLOYEEPASSWORD);

        addUserPage.clickSaveButton();
    }

    private void verifyCreateUserSuccess(){
        assertThat(addUserPage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        //check user is visible
        log.info("======== Check user is visible in table ========");
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
        assertThat(userManagementPage.isUserPresentInTable(uniqueUserName))
                .as("User search result should return exactly one record")
                .isTrue();

        SystemUser actualUser = userManagementPage.getUserDetailsFromTable(uniqueUserName);
        assertThat(actualUser).as("User must exist in table").isNotNull();
        assertThat(actualUser.getUserRole()).isEqualTo(AccountData.USERROLE);
        assertThat(actualUser.getStatus()).isEqualTo(AccountData.USERSTATUS);
        log.info("Verified user details: " + actualUser);
        ScreenshotHelper.captureAndAttach(page,"User visible in table");
    }

    private void deleteUser(){
        log.info("======== Delete user ========");
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
        userManagementPage.deleteUser(uniqueUserName);
        assertThat(userManagementPage.confirmDeleteNotificationIsVisible())
                .as("Confirm delete notification must be visible")
                .isTrue();
        userManagementPage.confirmDelete();
    }

    private void verifyDeleteUserSuccess(){
        assertThat(userManagementPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete user Successfully");

        log.info("======== Check user ========");
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        assertThat(userManagementPage.isUsernameInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        userManagementPage.waitForSearchResult();
        assertThat(userManagementPage.isUserNotVisibleInTable(uniqueUserName))
                .as("User should not be visible in table after deletion")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"User does not exist in table");
        log.info("No record is found");
    }

    private void deleteEmployee(){
        log.info("======== Delete employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueFullName);
        pimPage.waitForSearchResult();
        pimPage.deleteEmployee(uniqueUserId);
        pimPage.confirmDelete();
    }

    private void verifyDeleteEmployeeSuccess(){
        assertThat(pimPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");

        log.info("======== Check employee ========");
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueFullName);

        assertThat(pimPage.isEmployeeInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        pimPage.waitForSearchResult();
        assertThat(pimPage.isEmployeeNotVisibleInTable(uniqueFullName))
                .as("Employee should not be visible in table after deletion")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee does not exist in table");
        log.info("No record is found");
    }
}
