package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PIMPage {
    private Page page;
    private Locator pimSBButon;
    private Locator employeeListTBButton;
    private Locator employeeNameInput;
    private Locator optionSearchNameInput;
    private Locator optionSearchingInput;
    private Locator searchButton;
    private Locator iconDeleteButton;
    private Locator confirmDeleteButton;
    private Locator loadSpinnerTable;
    private Locator toastSuccessfully;
    private Locator toastNoRecordFound;



    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public PIMPage(Page page){
        this.page =page;
        this.pimSBButon = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='PIM']");
        this.employeeListTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Employee List']");
        this.employeeNameInput = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Employee Name')]/ancestor::div[contains(@class,'oxd-grid-item')]/descendant::input");
        this.optionSearchNameInput = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option']");
        this.optionSearchingInput = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option' and normalize-space(.)='Searching....']");
        this.searchButton = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.iconDeleteButton = page.locator("//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        this.confirmDeleteButton = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.toastNoRecordFound = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'No Records Found')]");
    }

    @Step("Click EmployeeList button ")
    public void navigateToEmployeeListPage(){
        employeeListTBButton.waitFor();
        employeeListTBButton.click();
    }

    @Step("Click PIM button on sidebar")
    public void clickPIMSideBarButton(){
        pimSBButon.waitFor();
        pimSBButon.click();
    }

    @Step("Search employee by Firstname")
    public void searchEmployeeByFirstname(String firstName, String fullName){
        employeeNameInput.waitFor();
        employeeNameInput.fill(firstName);
        optionSearchNameInput.waitFor();
        optionSearchingInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));


        String fullname = optionSearchNameInput.innerText();

        log.info("Result search fullname: " + fullname);
        log.info("Unique Fullname: " + fullName);
        if(fullname.equals(fullName)){
            optionSearchNameInput.click();
            searchButton.click();
        }
        else{
            searchButton.click();
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
        log.info("[Employee]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("Delete employee")
    public void deleteEmployee(){
        iconDeleteButton.waitFor();
        iconDeleteButton.click();
        confirmDeleteButton.waitFor();
        confirmDeleteButton.click();
    }

    @Step("Check employee is deleted Successfully")
    public boolean isDeleteSuccessfully(){
        try{
            toastSuccessfully.waitFor();
            return toastSuccessfully.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("No record is found")
    public boolean isEmployeeInvisibleAfterDelete(){
        try{
            toastNoRecordFound.waitFor();
            return toastNoRecordFound.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }
}
