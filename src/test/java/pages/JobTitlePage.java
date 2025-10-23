package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobTitlePage {
    private Page page;
    private static final Logger log = LoggerFactory.getLogger(JobTitlePage.class);

    private Locator btnJobTB;
    private Locator optionJobTitle;
    private Locator txtJobTitle;
    private Locator btnAdd;
    private Locator txtAddJobTitle;
    private Locator inputJobTitle;
    private Locator btnSave;
    private Locator toastSuccessfully;
    private Locator loadSpinnerTable;
    private Locator tableHeader;
    private Locator rows;
    private Locator btnDelete;
    private Locator btnConfirmDelete;
    private Locator confirmDeleteNotification;

    public JobTitlePage(Page page){
        this.page = page;
        this.txtJobTitle = page.locator("//div[contains(@class,'orangehrm-header-container')]/descendant::h6[normalize-space()='Job Titles']");
        this.btnJobTB = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::span[normalize-space(text())='Job']");
        this.optionJobTitle = page.locator("//ul[contains(@class,'oxd-dropdown-menu') and @role='menu']/descendant::a[normalize-space(.)='Job Titles']");
        this.btnAdd = page.locator("//button[contains(@class,'oxd-button') and normalize-space(.)='Add']");
        this.txtAddJobTitle = page.locator("//div[contains(@class,'orangehrm-card-container')]/descendant::h6[normalize-space()='Add Job Title']");
        this.inputJobTitle = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Job Title')]/parent::div/following-sibling::div/input");
        this.btnSave = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.btnConfirmDelete = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.confirmDeleteNotification = page.locator("//div[contains(@class,'orangehrm-modal-header')]/parent::div");

    }
    @Step("Click Job dropdown list on topbar")
    public void clickJobDropdownTB(){
        btnJobTB.waitFor();
        btnJobTB.click();
        log.info("Click Job dropdown list on topbar ");
    }
    @Step("Select option Job Title")
    public void selectOptionJobTitle(){
        optionJobTitle.waitFor();
        optionJobTitle.click();
        log.info("Select option Job Title");
    }
    @Step("Navigate to Job title page")
    public boolean isNavigateToJobTitle(){
        try{
            txtJobTitle.waitFor();
            return txtJobTitle.isVisible();
        }
        catch(TimeoutError e){
            return false;
        }
    }
    @Step("Click button Add")
    public void clickAddButton(){
        btnAdd.waitFor();
        btnAdd.click();
        log.info("Click button Add");
    }

    @Step("Input Job title: {jobTitle}")
    public void inputJobTitle(String jobTitle){
        inputJobTitle.waitFor();
        inputJobTitle.fill(jobTitle);
        log.info("Input job title: " + jobTitle);
    }

    @Step("Click button Save")
    public void clickSaveButton(){
        btnSave.waitFor();
        btnSave.click();
        log.info("Click button Save");
    }

    @Step("Create Successfully")
    public boolean isCreateSuccessfully(){
        try{
            toastSuccessfully.waitFor();
            return toastSuccessfully.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Wait for search results to load")
    public void waitForLoading(){
        try{
            loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            log.info("Loading spinner is visible");
        }
        catch(Exception ignored){
            log.info("Spinner not visible - skipping wait for visible state.");
        }
        loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        tableHeader.waitFor();
        log.info("[Job Title]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("Check job title present in table")
    public boolean isJobTitlePresentInTable(String jobTitle){
        rows.first().waitFor();
        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(jobTitle.trim())
        );

        int count = matchingRows.count();
        log.info("Found " + count + " matching rows for username: " + jobTitle);

        return count == 1;
    }

    @Step("Delete job title")
    public void deleteJobTitle(String jobTitle){
        tableHeader.waitFor();
        Locator targetRow = rows.filter(new Locator.FilterOptions().setHasText(jobTitle.trim())).first();
        targetRow.waitFor();
        btnDelete = targetRow.locator("xpath=.//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        btnDelete.click();
        log.info("Click button Delete in table");
    }

    @Step("Check Confirm delete notification is visible")
    public boolean confirmDeleteNotificationIsVisible(){
        try{
            confirmDeleteNotification.waitFor();
            return confirmDeleteNotification.isVisible();
        }
        catch (Exception e){
            return false;
        }
    }

    @Step("Confirm Delete")
    public void confirmDelete(){
        btnConfirmDelete.waitFor();
        btnConfirmDelete.click();
        log.info("Button confirm delete is clicked");
    }

    @Step("Is Delete Successfully")
    public boolean isDeleteSuccessfully(){
        try{
            toastSuccessfully.waitFor();
            return toastSuccessfully.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Verify job title '{jobTitle}' is not visible in the table after deletion")
    public boolean isJobTitleNotVisibleInTable(String jobTitle) {
        tableHeader.waitFor();

        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(jobTitle.trim())
        );

        int count = matchingRows.count();
        log.info("After deletion, found {} matching rows for job title '{}'", count, jobTitle);

        return count == 0;
    }

}
