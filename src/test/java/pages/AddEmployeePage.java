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

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public AddEmployeePage(Page page){
        this.page = page;
        this.addEmployeeTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Add Employee']");
        this.inputFirstName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='firstName']");
        this.inputMiddleName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='middleName']");
        this.inputLastName = page.locator("//div[contains(@class,'oxd-input-group')]/descendant::input[@name='lastName']");

    }
    @Step("Click Add Employee button on topbar in PIM")
    public void clickAddEmployeeButton(){
        addEmployeeTBButton.waitFor();
        addEmployeeTBButton.click();
    }


}
