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
import utils.DataHelper;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecruitmentLifecycleTests extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(RecruitmentLifecycleTests.class);

    PIMPage pimPage;
    AddEmployeePage addEmployeePage;
    UserManagementPage userManagementPage;
    JobTitlePage jobTitlePage;
    RecruitmentPage recruitmentPage;
    AddVacancyPage addVacancyPage;
    AddCandidatePage addCandidatePage;
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
    }

    @Test(description = "Test flow: Create & Delete Employee - Job Title - Vacancy")
    @Severity(SeverityLevel.NORMAL)
    public void shouldCreateVacancyAddCandidateAndCleanupSuccessfully(){
        log.info("======== Create Employee ========");
        createEmployee();
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

    private void createCandidates(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.clickAddButton();
        assertThat(addCandidatePage.isNavigateToAddCandidateForm())
                .as("Form Add Candidates must be visible")
                .isTrue();
        addCandidatePage.addCandiate(uniqueFirstName, uniqueMiddleName, uniqueLastName, uniqueEmail);
    }

    private void verifyCreateCandidatesSuccess(){
        assertThat(addCandidatePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");

        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.searchCandidateName(uniqueFirstName,uniqueEmployeeName);
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isCandidatePresentInTable(uniqueEmployeeName))
                .as("Search result should return exactly")
                .isTrue();
    }

    private void deleteCandidates(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToCandidatesPage();
        recruitmentPage.searchCandidateName(uniqueFirstName, uniqueEmployeeName);
        recruitmentPage.waitForSearchResult();
        recruitmentPage.deleteCandidate(uniqueEmployeeName);
    }

    private void verifyDeleteCandidateSuccess(){
        assertThat(recruitmentPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Candidate Successfully");

        log.info("======== Check Candidate ========");
        recruitmentPage.searchCandidateName(uniqueFirstName, uniqueEmployeeName);
        assertThat(recruitmentPage.invalidSearchResult()).
                as("Message invalid must visible").isTrue();
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isCandidateNotVisibleInTable(uniqueEmployeeName))
                .as("Candidate name should not be visible in table after deletion")
                .isTrue();
        log.info("No record is found");
    }

    private void createJobTitle(){
        userManagementPage.clickAdminSideBarButton();
        jobTitlePage.clickJobDropdownTB();
        jobTitlePage.selectOptionJobTitle();
        assertThat(jobTitlePage.isNavigateToJobTitle())
                .as("The text of Job title must be visible")
                .isTrue();
        jobTitlePage.clickAddButton();
        jobTitlePage.addJobTitle(uniqueJobTitle);
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
    }

    private void deleteJobTitle(){
        userManagementPage.clickAdminSideBarButton();
        jobTitlePage.clickJobDropdownTB();
        jobTitlePage.selectOptionJobTitle();
        jobTitlePage.deleteJobTitle(uniqueJobTitle);
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
    }

    private void createVacancies(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.clickAddButton();
        addVacancyPage.addVacancy(uniqueVacancyName, uniqueJobTitle, uniqueEmployeeName);
    }

    private void verifyCreateVacanciesSuccess(){
        assertThat(addVacancyPage.isNavigateToEditForm())
                .as("Create fail")
                .isTrue();
//        assertThat(addVacancyPage.isCreateSuccessfully())
//                .as("Create fail")
//                .isTrue();
        log.info("Create Successfully");
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.searchVacancyName(uniqueVacancyName);
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isVacancyPresentInTable(uniqueVacancyName))
                .as("Search result should return exactly")
                .isTrue();
    }

    private void deleteVacancies(){
        recruitmentPage.navigateToRecruitmentPage();
        recruitmentPage.navigateToVacanciesPage();
        recruitmentPage.searchVacancyName(uniqueVacancyName);
        recruitmentPage.waitForSearchResult();
        recruitmentPage.deleteVacancy(uniqueVacancyName);
    }

    private void verifyDeleteVacanciesSuccess(){
        assertThat(recruitmentPage.isDeleteSuccessfully())
                .as("Delete fail")
                .isTrue();
        log.info("Delete Employee Successfully");

        log.info("======== Check Vacancy ========");
        recruitmentPage.searchVacancyName(uniqueVacancyName);

        assertThat(recruitmentPage.isNoRecordInvisibleAfterDelete())
                .as("Notification No Record Found must be visible")
                .isTrue();
        recruitmentPage.waitForSearchResult();
        assertThat(recruitmentPage.isVacancyNotVisibleInTable(uniqueVacancyName))
                .as("Vacancy name should not be visible in table after deletion")
                .isTrue();
        log.info("No record is found");
    }

    private void createEmployee(){
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToAddEmployeePage();
        addEmployeePage.addEmployee(uniqueFirstName,uniqueMiddleName,uniqueLastName,uniqueUserId);
        //addEmployeePage.clickCreateLoginDetailsButton();
        //addEmployeePage.addDetailsUser(uniqueUserName, AccountData.EMPLOYEEPASSWORD, AccountData.EMPLOYEEPASSWORD);
        addEmployeePage.clickSaveButton();
        assertThat(addEmployeePage.isCreateSuccessfully())
                .as("Create fail")
                .isTrue();
        log.info("Create Successfully");
    }

    private void deleteEmployee(){
        pimPage.clickPIMSideBarButton();
        pimPage.navigateToEmployeeListPage();
        pimPage.searchEmployeeByFirstname(uniqueFirstName,uniqueEmployeeName);
        pimPage.waitForSearchResult();
        pimPage.deleteEmployee(uniqueUserId);
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
    }

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
