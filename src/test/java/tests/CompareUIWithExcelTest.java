package tests;
import base.AuthenticatedBaseTest;
import io.qameta.allure.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CompareUIWithExcelTest extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(CompareUIWithExcelTest.class);
    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    UserManagementPage userManagementPage;
    AddUserPage addUserPage;
    EditUserPage editUserPage;

    String uniqueFirstName;
    String uniqueMiddleName;
    String uniqueLastName;
    String uniqueUserId;
    String uniqueEmployeeName;
    String uniqueUserName;

    @BeforeClass
    public void prepareTestData(){
        log.info("======== Prepare recruitment test data ========");
        generateUniqueData();
    }

    @BeforeMethod
    public void setUpAdminPage() {
        pimPage = new PIMPage(page);
        addEmployeePage = new AddEmployeePage(page);
        userManagementPage = new UserManagementPage(page);
        addUserPage = new AddUserPage(page);
        editUserPage = new EditUserPage(page);
    }
    @Test(description = "Compare UI table data with Excel data")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that all users in Excel match those in the UI table (and vice versa)")
    public void compareExcelAndUITableData() {

        createEmployee();
        verifyCreateEmployeeSuccess();

        createUser();
        verifyCreateUserSuccess();

        searchUser();

        String excelFilePath = ConfigReader.getExcelURL();

        // Step 1: Extract data from UI
        List<SystemUser> uiUsers = userManagementPage.extractAllUsersFromUITable();
        log.info("Extracted {} users from UI table.", uiUsers.size());

        // Step 2: If Excel is null → Override data UI in Excel
        ExcelWriter.writeUsersToExcelIfEmpty(excelFilePath, uiUsers);
        log.info("Save {} users from UI to Excel" , uiUsers.size());

        // Step 3: Edit user information in table
        editUserInfo();

        searchUser();


        // Step 4: Read Excel data
        List<SystemUser> excelUsers = ExcelReader.readUsersFromExcel(excelFilePath);
        log.info("Loaded {} users from Excel file.", excelUsers.size());


        // Step 5: Find mismatches
        List<String> mismatchedUsernames = new ArrayList<>();

        for (SystemUser excelUser : excelUsers) {
            Optional<SystemUser> matchFromUI = uiUsers.stream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(excelUser.getUsername()))
                    .findFirst();

            if (matchFromUI.isEmpty()) {
                mismatchedUsernames.add(excelUser.getUsername());
                continue;
            }

            SystemUser uiUser = matchFromUI.get();


            if (isMismatch(uiUser,excelUser)) {
                mismatchedUsernames.add(excelUser.getUsername());
            }
        }

        // Step 6: Highlight mismatches in Excel
        if (!mismatchedUsernames.isEmpty()) {
            ExcelHighlighter.highlightRows(excelFilePath, mismatchedUsernames);
            log.error("Mismatch detected. Highlighted rows in Excel.");
            ScreenshotHelper.captureAndAttach(page,"UI Table Mismatch");
        }

        // Step 7: Assert
        assertThat(mismatchedUsernames.isEmpty())
                .as("Mismatch found — highlighted in Excel")
                .isTrue();


    }

    @AfterMethod
    public void deleteEmployeeAndUser(){
        deleteUser();
        verifyDeleteUserSuccess();
        deleteEmployee();
        verifyDeleteEmployeeSuccess();
    }

    private void searchUser(){
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
    }
    private boolean isMismatch(SystemUser ui, SystemUser excel){
        return !ui.getUsername().equalsIgnoreCase(excel.getUsername()) ||
                !ui.getEmployeeName().equalsIgnoreCase(excel.getEmployeeName()) ||
                !ui.getUserRole().equalsIgnoreCase(excel.getUserRole()) ||
                !ui.getStatus().equalsIgnoreCase(excel.getStatus());
    }

    private void deleteUser(){
        log.info("======== Delete user ========");
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
        userManagementPage.deleteUser(uniqueUserName);
        AssertionsForClassTypes.assertThat(userManagementPage.confirmDeleteNotificationIsVisible())
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
        AssertionsForClassTypes.assertThat(pimPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");

        log.info("======== Check employee ========");
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);

        AssertionsForClassTypes.assertThat(pimPage.isEmployeeInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        pimPage.waitForSearchResult();
        AssertionsForClassTypes.assertThat(pimPage.isEmployeeNotVisibleInTable(uniqueEmployeeName))
                .as("Employee should not be visible in table after deletion")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee does not exist in table");
        log.info("No record is found");
    }

    private void editUserInfo(){
        log.info("======== Edit user ========");
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
        userManagementPage.navigateToEditUserPage(uniqueUserName);
        editUserPage.selectStatus();
        editUserPage.clickSaveButton();
        ScreenshotHelper.captureAndAttach(page,"Edit Status User");
    }

    private void verifyCreateUserSuccess(){
        AssertionsForClassTypes.assertThat(addUserPage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        //check user is visible
        log.info("======== Check user is visible in table ========");
        userManagementPage.navigateToAdminPage();
        userManagementPage.inputSearchUsername(uniqueUserName);
        userManagementPage.clickSearchButton();
        userManagementPage.waitForSearchResult();
        AssertionsForClassTypes.assertThat(userManagementPage.isUserPresentInTable(uniqueUserName))
                .as("User search result should return exactly one record")
                .isTrue();

        SystemUser actualUser = userManagementPage.getUserDetailsFromTable(uniqueUserName);
        AssertionsForClassTypes.assertThat(actualUser).as("User must exist in table").isNotNull();
        AssertionsForClassTypes.assertThat(actualUser.getUserRole()).isEqualTo(AccountData.USERROLE);
        AssertionsForClassTypes.assertThat(actualUser.getStatus()).isEqualTo(AccountData.USERSTATUS);
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
        AssertionsForClassTypes.assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");
        log.info("======== Check create employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);
        pimPage.waitForSearchResult();
        AssertionsForClassTypes.assertThat(pimPage.isEmployeePresentInTable(uniqueUserId))
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

    //Generate unique data
    private void generateUniqueData(){
        //Generate unique Account
        uniqueFirstName = DataHelper.generateUniqueFirstName();
        log.info("FirstName: " + uniqueFirstName);
        uniqueLastName = DataHelper.generateUniqueLastName();
        log.info("LastName: " + uniqueLastName);
        uniqueMiddleName = DataHelper.generateUniqueMiddleName();
        log.info("MiddleName: " + uniqueMiddleName);
        if(uniqueMiddleName == null){
            uniqueEmployeeName = uniqueFirstName + " " + " " + uniqueLastName;
        }
        else {
            uniqueEmployeeName = uniqueFirstName + " " + uniqueMiddleName + " " + uniqueLastName;
        }
        log.info("Employee Name: " + uniqueEmployeeName);

        uniqueUserName = DataHelper.generateUniqueUsername();
        log.info("UserName: " + uniqueUserName);

        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id: " +uniqueUserId);
    }
}
