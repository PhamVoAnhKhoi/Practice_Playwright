package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddEmployeePage {
    private Page page;
    private Locator addEmployeeTBButton;
    private Locator inputFirstName;
    private Locator inputMiddleName;
    private Locator inputLastName;
    private Locator inputEmpyeeId;
    private Locator loginDetailsButton;
    private Locator inputUsername;
    private Locator inputPassword;
    private Locator inputConfirmPassword;
    private Locator saveButton;
    private Locator toastSuccessfully;


    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public AddEmployeePage(Page page){
        this.page = page;
        this.addEmployeeTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Add Employee']");
        this.inputFirstName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='firstName']");
        this.inputMiddleName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='middleName']");
        this.inputLastName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='lastName']");
        this.inputEmpyeeId = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Employee Id')]/parent::div/following-sibling::div/input");
        this.loginDetailsButton = page.locator("//div[contains(@class,'oxd-switch-wrapper')]/descendant::span[contains(@class,'oxd-switch-input')]");
        this.inputUsername = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Username')]/parent::div/following-sibling::div/input");
        this.inputPassword = page.locator("//label[contains(@class,'oxd-label') and normalize-space(text())='Password']/parent::div/following-sibling::div/input");
        this.inputConfirmPassword = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Confirm Password')]/parent::div/following-sibling::div/input");
        this.saveButton = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");

    }

    @Step("Click Add Employee button on topbar in PIM")
    public void navigateToAddEmployeePage(){
        addEmployeeTBButton.waitFor();
        addEmployeeTBButton.click();
    }

    @Step("Input Employee information")
    public void addEmployee(String firstName, String lastName, String employeeId){
        inputFirstName.fill(firstName);
        inputLastName.fill(lastName);
        inputEmpyeeId.fill(employeeId);
    }

    @Step("Input Login Account Details")
    public void addDetailsUser(String userName, String passWord, String confirmPassWord){
        inputConfirmPassword.waitFor();
        inputUsername.fill(userName);
        inputPassword.fill(passWord);
        inputConfirmPassword.fill(confirmPassWord);
    }

    @Step("Enable 'Create Login Details' if not already enabled")
    public void clickCreateLoginDetailsButton(){
        if (!loginDetailsButton.isChecked()) {
            loginDetailsButton.click();
            log.info("Switched ON 'Create Login Details'");
        } else {
            log.info("Create Login Details is already ON, no action needed");
        }
    }

    @Step("Click Save button")
    public void clickSaveButton(){
        saveButton.click();
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
}
