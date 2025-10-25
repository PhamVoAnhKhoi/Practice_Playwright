package tests;

import base.AuthenticatedBaseTest;
import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.ConfigReader;
import utils.DataHelper;
import utils.ScreenshotHelper;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecruitmentLifecycleTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RecruitmentLifecycleTests.class);

    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    UserManagementPage userManagementPage;
    JobTitlePage jobTitlePage;
    RecruitmentPage recruitmentPage;
    AddVacancyPage addVacancyPage;
    AddCandidatePage addCandidatePage;
    LoginPage loginPage;
    String uniqueJobTitle;
    String uniqueVacancyName;
    String uniqueFirstName;
    String uniqueMiddleName;
    String uniqueLastName;
    String uniqueEmail;
    String uniqueUserId;
    String uniqueEmployeeName;


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

        loginPage = new LoginPage(page);

        loginPage.navigateToLoginPage();
        loginPage.loginAccount(ConfigReader.getAdminUser(), ConfigReader.getAdminPassword());//Password is correct

    }

    @Test(description = "Test flow: Create & Delete Employee - Job Title - Vacancy")
    @Severity(SeverityLevel.NORMAL)
    public void shouldCreateVacancyAddCandidateAndCleanupSuccessfully(){
        log.info("======== Create Employee ========");
        createEmployee();
        verifyCreateEmployeeSuccess();
        log.info("======== Create Job Title ========");
        createJobTitle();
        verifyCreateJobTitleSuccess();
        log.info("======== Create Vacancies ========");
        createVacancies();
        verifyCreateVacanciesSuccess();
        log.info("======== Create Candidates ========");
        createCandidates();
        verifyCreateCandidatesSuccess();
        log.info("======== Delete Candidates ========");
        deleteCandidates();
        verifyDeleteCandidateSuccess();
        log.info("======== Delete Vacancies ========");
        deleteVacancies();
        verifyDeleteVacanciesSuccess();
        log.info("======== Delete Job Title ========");
        deleteJobTitle();
        verifyDeleteJobTitleSuccess();
        log.info("======== Delete Employee ========");
        deleteEmployee();
        verifyDeleteEmployeeSuccess();
    }

    //Candidate Action
    private void createCandidates(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.clickAddButton();
        assertThat(addCandidatePage.isNavigateToAddCandidateForm())
                .as("Form Add Candidates must be visible")
                .isTrue();
        addCandidatePage.inputCandidateInfo(uniqueFirstName, uniqueMiddleName, uniqueLastName, uniqueEmail);
        addCandidatePage.clickSaveButton();
    }

    private void verifyCreateCandidatesSuccess(){
        assertThat(addCandidatePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.inputSearchCandidateName(uniqueFirstName,uniqueEmployeeName);
        recruitmentPage.clickSearchButton();
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isCandidatePresentInTable(uniqueEmployeeName))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Candidate visible in table");
    }

    private void deleteCandidates(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.inputSearchCandidateName(uniqueFirstName, uniqueEmployeeName);
        recruitmentPage.clickSearchButton();
        recruitmentPage.waitForSearchResult();
        recruitmentPage.deleteCandidate(uniqueEmployeeName);
        assertThat(recruitmentPage.confirmDeleteNotificationIsVisible())
                .as("Confirm delete notification must be visible")
                .isTrue();
        recruitmentPage.confirmDelete();
    }

    private void verifyDeleteCandidateSuccess(){
        assertThat(recruitmentPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Candidate Successfully");

        log.info("======== Check Candidate ========");
        recruitmentPage.inputSearchCandidateName(uniqueFirstName, uniqueEmployeeName);
        recruitmentPage.clickSearchButton();
        assertThat(recruitmentPage.invalidSearchResult()).
                as("Message invalid must visible").isTrue();
        recruitmentPage.waitForSearchResult();

        assertThat(recruitmentPage.isCandidateNotVisibleInTable(uniqueEmployeeName))
                .as("Candidate name should not be visible in table after deletion")
                .isTrue();
        log.info("No record is found");
        ScreenshotHelper.captureAndAttach(page,"Candidate does not exist in table");
    }

    //Job title Action
    private void createJobTitle(){
        userManagementPage.navigateToAdminPage();
        jobTitlePage.clickJobDropdownTB();
        jobTitlePage.selectOptionJobTitle();
        assertThat(jobTitlePage.isNavigateToJobTitle())
                .as("The text of Job title must be visible")
                .isTrue();
        jobTitlePage.clickAddButton();
        jobTitlePage.inputJobTitle(uniqueJobTitle);
        jobTitlePage.clickSaveButton();
    }

    private void verifyCreateJobTitleSuccess(){
        assertThat(jobTitlePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        jobTitlePage.waitForLoading();
        assertThat(jobTitlePage.isJobTitlePresentInTable(uniqueJobTitle))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Job title visible in table");
    }

    private void deleteJobTitle(){
        userManagementPage.navigateToAdminPage();
        jobTitlePage.clickJobDropdownTB();
        jobTitlePage.selectOptionJobTitle();
        jobTitlePage.deleteJobTitle(uniqueJobTitle);
        jobTitlePage.confirmDeleteNotificationIsVisible();
        jobTitlePage.confirmDelete();
    }

    private void verifyDeleteJobTitleSuccess(){
        assertThat(jobTitlePage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Job title Successfully");

        log.info("======== Check Job Title ========");
        assertThat(jobTitlePage.isJobTitleNotVisibleInTable(uniqueJobTitle))
                .as("Job title should not be visible in table after deletion")
                .isTrue();
        log.info("No record is found");
        ScreenshotHelper.captureAndAttach(page,"Job title does not exist in table");
    }

    //Vacancies Action
    private void createVacancies(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.clickAddButton();
        addVacancyPage.inputVacancyInfo(uniqueVacancyName, uniqueJobTitle, uniqueEmployeeName);
        addVacancyPage.clickSaveButton();
    }

    private void verifyCreateVacanciesSuccess(){
//        assertThat(addVacancyPage.isCreateSuccessfully())
//                .as("Create fail")
//                .isTrue();

        assertThat(addVacancyPage.isNavigateToEditForm())
                .as("Create fail")
                .isTrue();

        log.info("Create Successfully");
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.selectOptionVacancyName(uniqueVacancyName);
        recruitmentPage.clickSearchButton();
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isVacancyPresentInTable(uniqueVacancyName))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Vacancies visible in table");
    }

    private void deleteVacancies(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.selectOptionVacancyName(uniqueVacancyName);
        recruitmentPage.clickSearchButton();
        recruitmentPage.waitForSearchResult();
        recruitmentPage.deleteVacancy(uniqueVacancyName);
        assertThat(recruitmentPage.confirmDeleteNotificationIsVisible())
                .as("Confirm delete notification must be visible")
                .isTrue();
        recruitmentPage.confirmDelete();
    }

    private void verifyDeleteVacanciesSuccess(){
        assertThat(recruitmentPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Vacancies Successfully");

        log.info("======== Check Vacancy ========");
        recruitmentPage.selectOptionVacancyName(uniqueVacancyName);
        recruitmentPage.clickSearchButton();
        assertThat(recruitmentPage.isNoRecordInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isVacancyNotVisibleInTable(uniqueVacancyName))
                .as("Vacancy name should not be visible in table after deletion")
                .isTrue();
        log.info("No record is found");
        ScreenshotHelper.captureAndAttach(page,"Vacancies does not exist in table");
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
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);
        pimPage.waitForSearchResult();
        assertThat(pimPage.isEmployeePresentInTable(uniqueUserId))
                .as("Search result should return exactly")
                .isTrue();
        ScreenshotHelper.captureAndAttach(page,"Employee visible in table");
    }

    private void deleteEmployee(){
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

        log.info("No record is found");
        ScreenshotHelper.captureAndAttach(page,"Employee does not exist in table");
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
        uniqueEmail = DataHelper.generateUniqueEmail();
        log.info("Email: " + uniqueEmail);
        //Generate Unique Data
        uniqueJobTitle = DataHelper.generateUniqueJobTitle();
        log.info("Job title: " + uniqueJobTitle);
        uniqueVacancyName = DataHelper.generateUniqueVacancyName();
        log.info("Vacancies: " + uniqueVacancyName);
    }
}
