package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeListPage {
    private Page page;
    private Locator employeeListTBButton;
    private Locator employeeIdInput;
    private Locator searchButton;
    private Locator iconDeleteButton;
    private Locator confirmDeleteButton;
    private Locator toastSuccessfully;

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public EmployeeListPage(Page page){
        this.page =page;
        this.employeeListTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Employee List']");
        this.employeeIdInput = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Employee Id')]/parent::div/following-sibling::div/input");
        this.searchButton = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.iconDeleteButton = page.locator("//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        this.confirmDeleteButton = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
    }

    public void navigateToEmployeeListPage(){
        employeeListTBButton.waitFor();
        employeeListTBButton.click();
    }

    public void searchEmployeeById(String id){
        employeeIdInput.waitFor();
        employeeIdInput.fill(id);
        searchButton.click();
    }
    public void deleteEmployee(){
        iconDeleteButton.waitFor();
        iconDeleteButton.click();
        confirmDeleteButton.waitFor();
        confirmDeleteButton.click();
    }
    @Step("Delete Successfully")
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
