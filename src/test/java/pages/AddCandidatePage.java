package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import io.qameta.allure.Step;

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

    @Step("Input info candidate")
    public void addCandiate(String firstName, String middleName, String lastName, String email){
        inputFirstName.waitFor();
        inputFirstName.fill(firstName);
        inputMiddleName.fill(middleName);
        inputLastName.fill(lastName);
        inputEmail.fill(email);
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
