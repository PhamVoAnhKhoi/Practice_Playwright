package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PIMPage {
    private Page page;
    private Locator pimSBButon;
    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);
    public PIMPage(){
        this.page = page;
        this.pimSBButon = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='PIM']");

    }
    @Step("Click PIM button on sidebar")
    public void clickPIMSideBarButton(){
        pimSBButon.waitFor();
        pimSBButon.click();
    }
}
