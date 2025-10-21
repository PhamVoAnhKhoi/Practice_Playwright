package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddVacancyPage {
    private Page page;
    private Locator btnAdd;
    private Locator txtAddVacancy;
    private Locator inputVacancyName;
    private Locator inputHiringManager;
    private Locator optionHiringManager;
    private Locator ddlJobTitle;
    private Locator optionJobTitle;
    private Locator btnSave;
    private Locator txtEdit;


    private static final Logger log = LoggerFactory.getLogger(AddVacancyPage.class);

    public AddVacancyPage(Page page){
        this.page = page;
        this.btnAdd = page.locator("//button[contains(@class,'oxd-button') and normalize-space(.)='Add']");
        this.txtAddVacancy = page.locator("//div[contains(@class,'orangehrm-card-container')]/descendant::h6[normalize-space()='Add Vacancy']");
        this.inputVacancyName = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Vacancy Name')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.ddlJobTitle = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Job Title')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::div[contains(@class,'oxd-select-text-input')]");
        this.optionJobTitle = page.locator("//div[@role='option' and contains(@class,'oxd-select-option')]");
        this.inputHiringManager = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Hiring Manager')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.optionHiringManager = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::span");
        this.btnSave = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
        this.txtEdit = page.locator("//div[contains(@class,'orangehrm-card-container')]/descendant::h6[normalize-space()='Edit Vacancy']");
    }

    @Step("Input Vacancy Info")
    public void addVacancy(String vacancyName, String jobTitle, String employeeName){
        //Input Vacancy Name
        fillVacancyName(vacancyName);

        //Dropdown list Job Title
        selectJobTitle(jobTitle);

        //Input Hiring Manager
        selectHiringManager(employeeName);

        btnSave.click();
    }

//    @Step("Create Successfully")
//    public boolean isCreateSuccessfully(){
//        try{
//            toastSuccessfully.waitFor();
//            return toastSuccessfully.isVisible();
//        }
//        catch(Exception e){
//            return false;
//        }
//    }

    @Step("Is navigate to Edit form")
    public boolean isNavigateToEditForm(){
        try{
            txtEdit.waitFor();
            return txtEdit.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    private void fillVacancyName(String vacancyName) {
        inputVacancyName.waitFor();
        inputVacancyName.fill(vacancyName);
        log.info("Filled Vacancy Name: {}", vacancyName);
    }

    private void selectJobTitle(String jobTitle) {
        ddlJobTitle.click();
        optionJobTitle.first().waitFor();

        Locator matchingOption = optionJobTitle.filter(
                new Locator.FilterOptions().setHasText(jobTitle.trim())
        );

        log.info("Selecting Job Title: {}", jobTitle);
        matchingOption.first().click();
    }

    private void selectHiringManager(String employeeName) {
        inputHiringManager.fill(employeeName);

        page.waitForSelector(
                "//div[contains(@class,'oxd-autocomplete-dropdown')]",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE)
        );

        Locator matchingOption = optionHiringManager.filter(
                new Locator.FilterOptions().setHasText(employeeName.trim())
        );

        log.info("Selecting Hiring Manager: {}", employeeName);
        matchingOption.first().click();
    }

}
