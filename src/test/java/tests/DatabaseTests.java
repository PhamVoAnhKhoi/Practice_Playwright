package tests;

import base.AuthenticatedBaseTest;
import helpers.DataHelper;
import helpers.ScreenshotHelper;
import io.qameta.allure.Description;
import org.assertj.core.api.AssertionsForClassTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.AccountData;
import utils.ConfigReader;
import utils.DBSetupUtils;
import utils.SystemUser;
import DAO.UserDAO;

import static org.assertj.core.api.Assertions.assertThat;


public class DatabaseTests extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(DatabaseTests.class);
    UserManagementPage userManagementPage;
    AddUserPage addUserPage;
    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    EditUserPage editUserPage;
    String uniqueFirstName;
    String uniqueMiddleName;
    String uniqueLastName;
    String uniqueEmployeeName;
    String uniqueUserName;
    String uniqueUserId;
    String tableName = ConfigReader.getDBTableName();

    @BeforeClass
    public void prepareTestData(){
        log.info("======== Generate unique data for User ========");
        createUniqueData();
        setUpTableDatabase();
    }

    @BeforeMethod
    public void setUpDatabaseTest(){
        userManagementPage = new UserManagementPage(page);
        addUserPage = new AddUserPage(page);
        pimPage = new PIMPage(page);
        addEmployeePage = new AddEmployeePage(page);
        editUserPage = new EditUserPage(page);
    }

    @Test
    @Description("After creating a user in UI, navigate to User Management page, read table data, insert into DB")
    public void syncUIToDBTest() {

        createEmployee();
        verifyCreateEmployeeSuccess();

        createUser();
        verifyCreateUserSuccess();

        searchUser();

        //Extract user from UI table
        SystemUser uiUser = userManagementPage.getUserDetailsFromTable(uniqueUserName);

        assertThat(uiUser)
                .as("User must exist in Table")
                .isNotNull();
        log.info("[UI] User extracted before edit: " + uiUser);

        //Insert user into PostgreSQL
        UserDAO.insertUser(uiUser, tableName);
        log.info("[DB] Inserted user: " + uiUser.getUsername());

        //Verify user exists in DB
        SystemUser dbUser = UserDAO.getUserByUsername(uniqueUserName, tableName);
        assertThat(dbUser)
                .as("User must exist in Database")
                .isNotNull();

        editUserInfo();

        searchUser();

        uiUser = userManagementPage.getUserDetailsFromTable(uniqueUserName);
        assertThat(uiUser)
                .as("User must exist in Table")
                .isNotNull();
        log.info("[UI] User extracted after edited: " + uiUser);
        assertUserEquals(uiUser,dbUser);

        log.info("[DB] Verified user: " + dbUser);
    }

    @AfterMethod
    public void deleteEmployeeAndUser(){
        tearDownTableDatabase();
        deleteUser();
        verifyDeleteUserSuccess();
        deleteEmployee();
        verifyDeleteEmployeeSuccess();
    }


    private void assertUserEquals(SystemUser ui, SystemUser db) {
        log.info("=========== Compare data between UI and DB ===========");
        assertThat(ui.getUsername())
                .as("Username is mismatched")
                .isEqualTo(db.getUsername());
        log.info("Username is matched");

        assertThat(ui.getUserRole())
                .as("Role is mismatched")
                .isEqualTo(db.getUserRole());
        log.info("Role is matched");

        assertThat(ui.getEmployeeName())
                .as("Employee name is mismatched")
                .isEqualTo(db.getEmployeeName());
        log.info("Employee name is matched");

        assertThat(ui.getStatus())
                .as("Status is matched")
                .isNotEqualTo(db.getStatus());
        log.info("Status is mismatched");
    }

    private void setUpTableDatabase(){
        DBSetupUtils.createUserTableIfNotExists(tableName);
        //Verify table is dropped
        assertThat(DBSetupUtils.isTableVisible(tableName))
                .as("Table does not exist in Database")
                .isTrue();
        log.info("Table is created successfully");
    }

    private void tearDownTableDatabase(){
        DBSetupUtils.dropUserTableIfExists(tableName);
        //Verify table is created
        assertThat(DBSetupUtils.isTableDisible(tableName))
                .as("Table still exist in Database")
                .isTrue();
        log.info("Table is deleted successfully");
    }

    private void createUniqueData(){
        uniqueFirstName = DataHelper.generateUniqueFirstName();
        log.info("FirstName: " + uniqueFirstName);
        uniqueLastName = DataHelper.generateUniqueLastName();
        log.info("LastName: " + uniqueLastName);
        uniqueMiddleName = DataHelper.generateUniqueMiddleName();
        if(uniqueMiddleName == null){
            uniqueEmployeeName = uniqueFirstName + " " + " "+ uniqueLastName;
        }
        else {
            uniqueEmployeeName = uniqueFirstName + " " + uniqueMiddleName + " " + uniqueLastName;
        }
        log.info("FullName: " + uniqueEmployeeName);
        uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("UserName: " + uniqueUserName);
        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id: " + uniqueUserId);
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

    private void createUser(){
        userManagementPage.navigateToAdminPage();
        addUserPage.clickAddButton();
        addUserPage.selectStatus();
        addUserPage.selectUserRole();
        log.info("FullName: " + uniqueEmployeeName);
        addUserPage.inputUserInfo(uniqueFirstName,uniqueEmployeeName,uniqueUserName,AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addUserPage.clickSaveButton();
    }

    private void verifyCreateEmployeeSuccess(){
        assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");
        log.info("======== Check create employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);
        pimPage.waitForSearchResult();
        assertThat(pimPage.isEmployeePresentInTable(uniqueUserId))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee visible in table");
    }

    private void createEmployee(){
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToAddEmployeePage();
        addEmployeePage.inputEmployeeInfo(uniqueFirstName,uniqueMiddleName,uniqueLastName,uniqueUserId);
        addEmployeePage.clickSaveButton();
    }

    private void searchUser(){
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
    }

    private void editUserInfo(){
        log.info("======== Edit user ========");
        searchUser();
        userManagementPage.navigateToEditUserPage(uniqueUserName);
        editUserPage.selectStatus();
        editUserPage.clickSaveButton();
        ScreenshotHelper.captureAndAttach(page,"Edit Status User");
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
        AssertionsForClassTypes.assertThat(userManagementPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete user Successfully");

        log.info("======== Check user ========");
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        AssertionsForClassTypes.assertThat(userManagementPage.isUsernameInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        userManagementPage.waitForSearchResult();
        AssertionsForClassTypes.assertThat(userManagementPage.isUserNotVisibleInTable(uniqueUserName))
                .as("User should not be visible in table after deletion")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"User does not exist in table");
        log.info("No record is found");
    }

    private void deleteEmployee(){
        log.info("======== Delete employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);
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
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);

        assertThat(pimPage.isEmployeeInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        pimPage.waitForSearchResult();
        assertThat(pimPage.isEmployeeNotVisibleInTable(uniqueEmployeeName))
                .as("Employee should not be visible in table after deletion")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee does not exist in table");
        log.info("No record is found");
    }
}
