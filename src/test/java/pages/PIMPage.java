package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PIMPage {
    private Page page;
    private Locator btnPIMSB;
    private Locator btnEmployeeListTB;
    private Locator btnAddEmployeeTB;
    private Locator inputEmployeeName;
    private Locator optionSearchName;
    private Locator optionSearching;
    private Locator btnSearch;
    private Locator btnIconDelete;
    private Locator btnConfirmDelete;
    private Locator loadSpinnerTable;
    private Locator toastSuccessfully;
    private Locator toastNoRecordFound;
    private Locator rows;
    private Locator tableHeader;




    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public PIMPage(Page page){
        this.page =page;
        this.btnPIMSB = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='PIM']");
        this.btnEmployeeListTB = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Employee List']");
        this.btnAddEmployeeTB = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Add Employee']");
        this.inputEmployeeName = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Employee Name')]/ancestor::div[contains(@class,'oxd-grid-item')]/descendant::input");
        this.optionSearchName = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option']");
        this.optionSearching = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option' and normalize-space(.)='Searching....']");
        this.btnSearch = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.btnIconDelete = page.locator("//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        this.btnConfirmDelete = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.toastNoRecordFound = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'No Records Found')]");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");

    }

    @Step("Click Add Employee button on topbar in PIM")
    public void navigateToAddEmployeePage(){
        btnAddEmployeeTB.waitFor();
        btnAddEmployeeTB.click();
    }

    @Step("Click EmployeeList button ")
    public void navigateToEmployeeListPage(){
        btnEmployeeListTB.waitFor();
        btnEmployeeListTB.click();
    }

    @Step("Click PIM button on sidebar")
    public void clickPIMSideBarButton(){
        btnPIMSB.waitFor();
        btnPIMSB.click();
    }

    @Step("Search employee by Firstname")
    public void searchEmployeeByFirstname(String firstName, String fullName){
        inputEmployeeName.waitFor();
        inputEmployeeName.fill(firstName);
        optionSearchName.waitFor();
        optionSearching.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));


        String fullname = optionSearchName.innerText();

        log.info("Result search fullname: " + fullname);
        log.info("Unique Fullname: " + fullName);
        if(fullname.equals(fullName)){
            optionSearchName.click();
            btnSearch.click();
        }
        else{
            btnSearch.click();
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
        log.info("[Employee]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("Delete employee")
    public void deleteEmployee(){
        tableHeader.waitFor();
        btnIconDelete.waitFor();
        btnIconDelete.click();
        btnConfirmDelete.waitFor();
        btnConfirmDelete.click();
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
    @Step("Verify employee '{fullName}' is not visible in the table after deletion")
    public boolean isEmployeeNotVisibleInTable(String fullName) {
        tableHeader.waitFor();

        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(fullName.trim())
        );

        int count = matchingRows.count();
        log.info("After deletion, found {} matching rows for employee '{}'", count, fullName);

        return count == 0;
    }
}
