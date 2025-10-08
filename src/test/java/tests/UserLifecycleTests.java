package tests;

import base.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.AccountData;
import utils.ConfigReader;
import utils.DataHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserLifecycleTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);
    LoginPage loginPage;
    AddEmployeePage addEmployeePage;
    PIMPage employeeListPage;
    UserManagementPage userManagementPage;
    AddUserPage addUserPage;
    String uniqueFirstName;
    String uniqueLastName;
    String uniqueFullName;
    String uniqueUserName;
    String uniqueUserId;
    @BeforeMethod
    public void setUpUserLifecycle(){
        loginPage = new LoginPage(page);
        addEmployeePage = new AddEmployeePage(page);
        employeeListPage = new PIMPage(page);
        userManagementPage = new UserManagementPage(page);
        addUserPage = new AddUserPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(ConfigReader.getAdminUser(),ConfigReader.getAdminPassword());
        genarateAccount();
    }

    @Test
    public void createUser(){
        createEmployee();
        addUser();
    }

    @AfterMethod
    public void resetLifecycle(){
        deleteUser();
        deleteEmployee();
    }

    public void createEmployee(){
        employeeListPage.clickPIMSideBarButton();
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

    public void addUser(){
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
        addUserPage.inputUserInfo(uniqueFullName, uniqueUserName,AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addUserPage.clickSaveButton();
        assertThat(addUserPage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        //check user is visible
        userManagementPage.clickAdminSideBarButton();
        userManagementPage.searchUsername(uniqueUserName);
        assertThat(userManagementPage.isUsenameVisibleInTable(uniqueUserName)).isTrue();
        log.info("Username is visible in table");
    }

    public void genarateAccount(){
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

    public void deleteUser(){
        userManagementPage.clickAdminSideBarButton();
        userManagementPage.searchUsername(uniqueUserName);

        userManagementPage.deleteUser();
        assertThat(userManagementPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete user Successfully");
    }

    public void deleteEmployee(){
        employeeListPage.clickPIMSideBarButton();
        employeeListPage.navigateToEmployeeListPage();
        employeeListPage.searchEmployeeById(uniqueUserId);
        employeeListPage.deleteEmployee();
        assertThat(employeeListPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");
    }
}
