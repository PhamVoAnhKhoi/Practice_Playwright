package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.UserLifecycleTests;

public class AddCandidatePage {

    private Page page;
    private Locator btnAdd;
    private Locator txtAddCandidate;
    private Locator inputFirstName;
    private Locator inputMiddleName;
    private Locator inputLastName;
    private Locator inputEmail;
    private Locator btnSave;
    private Locator toastSuccessfully;
    private static final Logger log = LoggerFactory.getLogger(AddCandidatePage.class);

    public AddCandidatePage(Page page){
        this.page = page;
        this.btnAdd = page.locator("//button[contains(@class,'oxd-button') and normalize-space(.)='Add']");
        this.txtAddCandidate = page.locator("//div[contains(@class,'orangehrm-card-container')]/descendant::h6[normalize-space()='Add Candidate']");
        this.inputFirstName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='firstName']");
        this.inputMiddleName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='middleName']");
        this.inputLastName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='lastName']");
        this.inputEmail = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Email')]/parent::div/following-sibling::div/input");
        this.btnSave = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");

    }

    @Step("Check Add Candidate Form Visible")
    public boolean isNavigateToAddCandidateForm(){
        try{
            txtAddCandidate.waitFor();
            return txtAddCandidate.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Input candidate information")
    @Description("Input first name: {firstName}"
            + "Input middle name: {middleName}"
            + "Input last name: {lastName}"
            + "Input email: {email}")
    public void inputCandidateInfo(String firstName, String middleName, String lastName, String email){
        inputFirstName.waitFor();
        inputFirstName.fill(firstName);
        inputMiddleName.fill(middleName);
        inputLastName.fill(lastName);
        inputEmail.fill(email);
        log.info("Input candidate information");
    }

    @Step("Click Save button")
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
}
