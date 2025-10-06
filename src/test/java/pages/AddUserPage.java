package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddUserPage {
    private Page page;
    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public AddUserPage(Page page){
        this.page = page;
    }
    @Step("Click Add User button on topbar in PIM")
    public void clickAddUserButton(){

    }
}
