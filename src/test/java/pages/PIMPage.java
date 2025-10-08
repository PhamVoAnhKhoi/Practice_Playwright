package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PIMPage {
    private Page page;
    private Locator pimSBButon;
    private Locator employeeListTBButton;
    private Locator employeeIdInput;
    private Locator searchButton;
    private Locator iconDeleteButton;
    private Locator confirmDeleteButton;
    private Locator toastSuccessfully;
    private Locator rows;

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public PIMPage(Page page){
        this.page =page;
        this.pimSBButon = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='PIM']");
        this.employeeListTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Employee List']");
        this.employeeIdInput = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Employee Id')]/parent::div/following-sibling::div/input");
        this.searchButton = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.iconDeleteButton = page.locator("//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        this.confirmDeleteButton = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
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

    @Step("Search employee by ID")
    public void searchEmployeeById(String id){
        employeeIdInput.waitFor();
        employeeIdInput.fill(id);
        int oldCount = rows.count(); //Store the number of old row in table
        log.info("Old count: "+oldCount);
        searchButton.click();
        //page.waitForTimeout(2000);
        page.waitForCondition(() -> rows.count() != oldCount, new Page.WaitForConditionOptions().setTimeout(5000));
        log.info("row:" + rows.count());
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
}
