package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditUserPage {
    private Page page;
    private Locator ddlUserStatus;
    private Locator optionDisabledStatus;
    private Locator btnSave;
    private static final Logger log = LoggerFactory.getLogger(EditUserPage.class);
    public EditUserPage(Page page){
        this.page = page;
        this.ddlUserStatus = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(),'Status')]/ancestor::div[contains(@class,'oxd-input-group')]/descendant::div[contains(@class,'oxd-select-text-input')]");
        this.optionDisabledStatus = page.locator("//div[@role='option']/descendant::span[normalize-space(.)='Disabled']");
        this.btnSave = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[normalize-space(.)='Save']");
    }

    @Step("Select status for user")
    public void selectStatus(){
        ddlUserStatus.click();
        optionDisabledStatus.waitFor();
        optionDisabledStatus.click();
        log.info("Select option Disable");
    }

    @Step("Click button save")
    public void clickSaveButton(){
        btnSave.waitFor();
        btnSave.click();
    }

}
