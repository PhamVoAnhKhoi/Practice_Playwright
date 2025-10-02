package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddEmployeePage {
    private Page page;
    private Locator addEmployeeTBButton;
    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public AddEmployeePage(){
        this.page = page;
        this.addEmployeeTBButton = page.locator("//li[contains(@class,'oxd-topbar-body')]/descendant::a[normalize-space(text())='Add Employee']");
    }
    @Step("Click Add Employee button on topbar in PIM")
    public void clickAddEmployeeButton(){
        addEmployeeTBButton.waitFor();
        addEmployeeTBButton.click();
    }
}
