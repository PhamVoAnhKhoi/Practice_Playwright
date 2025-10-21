package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecruitmentPage {
    private Page page;
    private Locator btnRecruitmentSB;
    private Locator vacanciesTBButton;
    private Locator candidatesTBButton;
    private Locator btnAdd;
    private Locator ddlVacancy;
    private Locator optionVacancy;
    private Locator btnSearch;
    private Locator btnConfirmDelete;
    private Locator tableHeader;
    private Locator rows;
    private Locator btnDelete;
    private Locator loadSpinnerTable;
    private Locator toastSuccessfully;
    private Locator toastNoRecordFound;
    private Locator inputCandidateName;
    private Locator optionCandidateName;
    private Locator optionSearching;
    private Locator optionNoRecordFound;
    private Locator msgErrorInvalid;

    private static final Logger log = LoggerFactory.getLogger(RecruitmentPage.class);

    public RecruitmentPage(Page page){
        this.page = page;
        this.btnRecruitmentSB = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='Recruitment']");
        this.vacanciesTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Vacancies']");
        this.candidatesTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Candidates']");
        this.btnAdd = page.locator("//button[contains(@class,'oxd-button') and normalize-space(.)='Add']");
        this.ddlVacancy = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Vacancy')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::div[contains(@class,'oxd-select-text-input')]");
        this.optionVacancy = page.locator("//div[@role='option' and contains(@class,'oxd-select-option')]");
        this.btnSearch = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.btnConfirmDelete = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.toastNoRecordFound = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'No Records Found')]");
        this.inputCandidateName = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Candidate Name')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.optionCandidateName = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::span");
        this.optionSearching = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option' and normalize-space(.)='Searching....']");
        this.optionNoRecordFound = page.locator("//div[@role='option' and normalize-space(.)='No Records Found']");
        this.msgErrorInvalid = page.locator("//span[contains(@class,'oxd-text') and normalize-space(.)='Invalid']");
    }


    @Step("Navigate to Recruitment page")
    public void navigateToRecruitmentPage(){
        btnRecruitmentSB.waitFor();
        btnRecruitmentSB.click();
    }

    @Step("Navigate to Vancancies")
    public void navigateToVacanciesPage(){
        vacanciesTBButton.waitFor();
        vacanciesTBButton.click();
    }

    @Step("Click Add button")
    public void clickAddButton(){
        btnAdd.waitFor();
        btnAdd.click();
    }

    @Step("Search by Vacancy Name")
    public void searchVacancyName(String vacancyName){
        ddlVacancy.waitFor();
        ddlVacancy.click();

        optionVacancy.first().waitFor();
        Locator matchingOption = optionVacancy.filter(
                new Locator.FilterOptions().setHasText(vacancyName.trim())
        );
        if(matchingOption.count()>0){
            log.info("Selecting vacancy name: {}", vacancyName);
            matchingOption.first().click();
        }

        btnSearch.click();
    }

    @Step("Check user present in table")
    public boolean isVacancyPresentInTable(String vacancies){
        rows.first().waitFor();
        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(vacancies.trim())
        );

        int count = matchingRows.count();
        log.info("Found " + count + " matching rows for username: " + vacancies);

        return count == 1;
    }

    @Step("Delete vacancy")
    public void deleteVacancy(String vacancyName){
        tableHeader.waitFor();
        Locator targetRow = rows.filter(new Locator.FilterOptions().setHasText(vacancyName.trim())).first();
        targetRow.waitFor();
        btnDelete = targetRow.locator("xpath=.//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        btnDelete.click();
        btnConfirmDelete.waitFor();
        btnConfirmDelete.click();
    }

    @Step("Check Delete Successful")
    public boolean isDeleteSuccessfully(){
        try{
            toastSuccessfully.waitFor();
            return toastSuccessfully.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Wait for search results to load")
    public void waitForSearchResult(){
        try{
            loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        }
        catch(Exception ignored){
            log.info("Spinner not visible - skipping wait for visible state.");
        }
        loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        tableHeader.waitFor();
        log.info("[Vacancies]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("No record is found")
    public boolean isNoRecordInvisibleAfterDelete(){
        try{
            toastNoRecordFound.waitFor();
            return toastNoRecordFound.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Verify vacancy '{vacancyName}' is not visible in the table after deletion")
    public boolean isVacancyNotVisibleInTable(String vacancyName) {
        tableHeader.waitFor();

        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(vacancyName.trim())
        );

        int count = matchingRows.count();
        log.info("After deletion, found {} matching rows for employee '{}'", count, vacancyName);

        return count == 0;
    }

    @Step("Navigate to candidates page")
    public void navigateToCandidatesPage(){
        candidatesTBButton.waitFor();
        candidatesTBButton.click();
    }

    @Step("Search by candidate name")
    public void searchCandidateName(String inputName, String employeeName){
        inputCandidateName.waitFor();
        inputCandidateName.fill(inputName);
        page.waitForSelector("//div[contains(@class,'oxd-autocomplete-dropdown')]",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        try{
            Locator matchingOption = optionCandidateName.filter(
                    new Locator.FilterOptions().setHasText(employeeName.trim())
            );
            log.info("Selecting candidate name: {}", employeeName);
            matchingOption.first().click();
        }
        catch (Exception e){
            log.info("No record found");
        }
        btnSearch.click();
    }

    @Step("Check user present in table")
    public boolean isCandidatePresentInTable(String candidates){
        rows.first().waitFor();
        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(candidates.trim())
        );

        int count = matchingRows.count();
        log.info("Found " + count + " matching rows for candidate: " + candidates);

        return count == 1;
    }

    @Step("Delete candidate")
    public void deleteCandidate(String candidateName){
        tableHeader.waitFor();
        Locator targetRow = rows.filter(new Locator.FilterOptions().setHasText(candidateName.trim())).first();
        targetRow.waitFor();
        btnDelete = targetRow.locator("xpath=.//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        btnDelete.click();
        btnConfirmDelete.waitFor();
        btnConfirmDelete.click();
    }

    @Step("Verify vacancy '{vacancyName}' is not visible in the table after deletion")
    public boolean isCandidateNotVisibleInTable(String candidateName) {
        tableHeader.waitFor();

        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(candidateName.trim())
        );

        int count = matchingRows.count();
        log.info("After deletion, found {} matching rows for employee '{}'", count, candidateName);

        return count == 0;
    }

    @Step("Is Invalid search result visible?")
    public boolean invalidSearchResult(){
        try{
            msgErrorInvalid.waitFor();
            return msgErrorInvalid.isVisible();
        }
        catch (Exception e){
            return false;
        }

    }
}
