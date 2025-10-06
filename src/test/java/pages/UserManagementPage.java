package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUser;

import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class UserManagementPage {


    private Page page;
    private Locator sideBar;
    private Locator adminSBButton; //Xpath Axes
    private Locator rows;
    private Locator headerTitle;

    //table header
    private Locator tableHeader ;


    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public UserManagementPage(Page page){
        this.page = page;
        this.sideBar = page.locator("nav[class='oxd-navbar-nav']");
        this.adminSBButton = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='Admin']");
        this.headerTitle = page.locator("//div[contains(@class,'oxd-topbar-header-title')]/descendant::h6[normalize-space(.)='Admin']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
    }

    public List<SystemUser> getTableColData(){
        tableHeader.waitFor();
        List<String> headerTexts = tableHeader.locator("xpath=.//div[contains(@class,'oxd-table-header-cell')]").allInnerTexts();
        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (int i = 0; i < headerTexts.size(); i++) {
            headerIndexMap.put(headerTexts.get(i).trim(), i);
        }
        log.info("Header mapping: " + headerIndexMap);

        int rowCount = rows.count();
        List<SystemUser> users = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            Locator row = rows.nth(i);
            List<String> cells = row.locator("xpath=.//div[contains(@class,'oxd-table-cell')]").allInnerTexts();

            String username = cells.get(headerIndexMap.get("Username")).trim();
            String role = cells.get(headerIndexMap.get("User Role")).trim();
            String employeeName = cells.get(headerIndexMap.get("Employee Name")).trim();
            String status = cells.get(headerIndexMap.get("Status")).trim();

            users.add(new SystemUser(username, role, employeeName, status));
        }

        // Log kết quả
        for (SystemUser user : users) {
            log.info(user.toString());
        }
        return users;
    }

    @Step("Click Admin button on sidebar")
    public void clickAdminSideBarButton(){
        sideBar.waitFor();
        adminSBButton.click();
    }

    @Step("Sidebar is visible")
    public boolean isSidebarVisible(){
        try{
            adminSBButton.waitFor();
            return adminSBButton.isVisible();
        }
        catch(TimeoutError e){
            return false;
        }
    }

    @Step("Header title is visible")
    public boolean isHeaderTitleVisible(){
        try{
            headerTitle.waitFor();
            return headerTitle.isVisible();
        }
        catch(TimeoutError e){
            return false;
        }
    }
}
