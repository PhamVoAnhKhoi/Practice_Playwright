package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddUserPage{
    private Page page;
    private Locator btnAdd;
    private Locator txtAddUser;
    private Locator ddlUserRole; //Drop down list User Role
    private Locator optionAdminUserRole;
    private Locator inputEmployeeName;
    private Locator optionEmployeeName;
    private Locator optionSearching;
    private Locator ddlUserStatus;
    private Locator optionEnabledStatus;
    private Locator inputUsername;
    private Locator inputPassword;
    private Locator inputConfirmPassword;
    private Locator btnSave;
    private Locator toastSuccessfully;

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public AddUserPage(Page page){
        this.page = page;
        this.btnAdd = page.locator("//button[contains(@class,'oxd-button') and normalize-space(.)='Add']");
        this.txtAddUser = page.locator("//div[contains(@class,'orangehrm-card-container')]/descendant::h6[normalize-space()='Add User']");
        this.ddlUserRole = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Role')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::div[contains(@class,'oxd-select-text-input')]");
        this.optionAdminUserRole = page.locator("//div[@role='option']/descendant::span[normalize-space(.)='Admin']");
        this.inputEmployeeName = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Employee Name')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.optionEmployeeName = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::span");
        this.optionSearching = page.locator("//div[contains(@class,'oxd-autocomplete-dropdown')]/descendant::div[@role='option' and normalize-space(.)='Searching....']");
        this.ddlUserStatus = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Status')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::div[contains(@class,'oxd-select-text-input')]");
        this.optionEnabledStatus = page.locator("//div[@role='option']/descendant::span[normalize-space(.)='Enabled']");
        this.inputUsername = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Username')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.inputPassword = page.locator("//label[contains(@class,'oxd-label') and normalize-space(text())='Password']/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.inputConfirmPassword = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Confirm Password')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
        this.btnSave = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");

    }

    //Add user
    @Step("Click Add button")
    public void clickAddButton(){
        btnAdd.waitFor();
        btnAdd.click();
    }

    @Step("Add user form visible")
    public boolean isAddUserFormVisible(){
        try{
            txtAddUser.waitFor();
            return txtAddUser.isVisible();
        }
        catch(TimeoutError e){
            return false;
        }
    }

    @Step("Select role for user")
    public void selectUserRole(){
        ddlUserRole.click();
        optionAdminUserRole.waitFor();
        optionAdminUserRole.click();
    }

    @Step("Input user information")
    public void inputUserInfo(String inputName, String fullName,String userName, String passWord, String confirmPassword ){
        inputEmployeeName.waitFor();
        inputEmployeeName.fill(inputName);
        page.waitForSelector("//div[contains(@class,'oxd-autocomplete-dropdown')]",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

        Locator matchingOption = optionEmployeeName.filter(
                new Locator.FilterOptions().setHasText(fullName.trim())
        );
        matchingOption.first().click();
        inputUsername.waitFor();
        inputUsername.fill(userName);
        inputPassword.fill(passWord);
        inputConfirmPassword.fill(confirmPassword);
    }

    @Step("Select status for user")
    public void selectStatus(){
        ddlUserStatus.click();
        optionEnabledStatus.waitFor();
        optionEnabledStatus.click();
    }
    @Step("Click Save button")
    public void clickSaveButton(){
        btnSave.click();
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
