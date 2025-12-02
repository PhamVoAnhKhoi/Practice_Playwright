package tests;

import base.AuthenticatedBaseTest;
import helpers.DataHelper;
import helpers.ScreenshotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DemoFailureTests extends AuthenticatedBaseTest {

    private static final Logger log = LoggerFactory.getLogger(RecruitmentLifecycleTests.class);

    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    UserManagementPage userManagementPage;
    JobTitlePage jobTitlePage;
    RecruitmentPage recruitmentPage;
    AddVacancyPage addVacancyPage;
    AddCandidatePage addCandidatePage;
    String uniqueFirstName;
    String uniqueMiddleName;
    String uniqueLastName;
    String uniqueUserId;
    String uniqueEmployeeName;
    String demoFailureTest;

    @BeforeClass
    public void prepareTestData(){
        log.info("======== Prepare recruitment test data ========");
        generateUniqueData();
    }


    @BeforeMethod
    public void setUpRecruitmentLifecycle(){
        pimPage = new PIMPage(page);
        addEmployeePage = new AddEmployeePage(page);
        userManagementPage = new UserManagementPage(page);
        jobTitlePage = new JobTitlePage(page);
        recruitmentPage = new RecruitmentPage(page);
        addVacancyPage = new AddVacancyPage(page);
        addCandidatePage = new AddCandidatePage(page);
    }

    @Test
    public void DemoFailureTest(){
       createEmployee();
       verifyCreateEmployeeSuccess();
    }

    @AfterMethod
    public void cleanUp(){

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
        uniqueUserId = DataHelper.generateRandomUserId(5);
        log.info("Id: " +uniqueUserId);

        demoFailureTest = "Hello world";
    }

    //Employee Action
    private void createEmployee(){
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToAddEmployeePage();
        addEmployeePage.inputEmployeeInfo(uniqueFirstName, uniqueMiddleName, uniqueLastName, uniqueUserId);
        //addEmployeePage.clickCreateLoginDetailsButton();
        //addEmployeePage.addDetailsUser(uniqueUserName, AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addEmployeePage.clickSaveButton();
    }

    private void verifyCreateEmployeeSuccess(){
        assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");
        log.info("======== Check create employee ========");
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        //Demo Failure
        pimPage.searchEmployeeByFirstname(demoFailureTest,uniqueEmployeeName);
        pimPage.waitForSearchResult();
        assertThat(pimPage.isEmployeePresentInTable(uniqueUserId))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee visible in table");
    }
}
