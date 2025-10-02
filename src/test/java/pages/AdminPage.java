package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUser;

import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class AdminPage {


    private Page page;
    private Locator sideBar;
    private Locator adminSBButton; //Xpath Axes
    private Locator rows;
    private Locator headerTitle;
    //Sidebar menu
    private Locator sideBarMenuAxes;
    private Locator sideBarMenuDynamic;
    //Top bar
    private Locator topBarDynamic;
    private Locator topBarAxes;
    //table header
    private Locator tableHeader ;
    private Locator tableHeaderAxes;
    //Label table sort
    private Locator labelSortDynamic;
    private Locator labelSortAxes;

    private Locator dropDownAdmin;

    private static final Logger log = LoggerFactory.getLogger(AdminPage.class);

    public AdminPage(Page page){
        this.page = page;
        this.sideBar = page.locator("nav[class='oxd-navbar-nav']");
        this.adminSBButton = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='Admin']");
        this.headerTitle = page.locator("//div[contains(@class,'oxd-topbar-header-title')]/descendant::h6[normalize-space(.)='Admin']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");

        this.sideBarMenuAxes = page.locator("//ul[@class='oxd-main-menu']/ancestor::div[contains(@class,'oxd-sidepanel-body')]");
        this.sideBarMenuDynamic = page.locator("//span[contains(@class,'oxd-main-menu-item') and normalize-space(.)='Admin']");

        this.topBarDynamic = page.locator("//li[contains(@class,'oxd-topbar-body-nav-tab')]//span[contains(normalize-space(text()),'User Management')]");
        this.topBarAxes = page.locator("//li[contains(@class,'oxd-topbar-body-nav-tab')]//descendant::span[normalize-space(text())='User Management']");

        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.tableHeaderAxes = page.locator("//div[contains(@class,'oxd-table-header')]//descendant::div[contains(@class,'oxd-table-row')]" );

        this.labelSortDynamic = page.locator("//label[contains(@class,'oxd-label') and normalize-space(text())='Username']");
        this.labelSortAxes = page.locator("//input[contains(@class,'oxd-input')]//ancestor::div[contains(@class,'oxd-input-group')]//label[normalize-space(text())='Username']");

        this.dropDownAdmin = page.locator("//label[contains(@class,'oxd-label') and normalize-space(text())='User Role']/following-sibling::div[contains(@class,'oxd-select-text-input')]");
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

    @Step("Check visible")
    public void checkVisible(){
        log.info("Check Xpath Axes Visiable");
        topBarAxes.waitFor();
        assertThat(sideBarMenuAxes).isVisible();
        assertThat(topBarAxes).isVisible();
        tableHeaderAxes.waitFor();
        assertThat(tableHeaderAxes).isVisible();
        labelSortAxes.waitFor();
        assertThat(labelSortAxes).isVisible();
    }
    @Step("Check Xpath Dynamic visiable")
    public void checkVisibleDynamic(){
        log.info("Check Xpath Dynamic visiable");
        topBarDynamic.waitFor();
        assertThat(sideBarMenuDynamic).isVisible();
        assertThat(topBarDynamic).isVisible();
        tableHeader.waitFor();
        assertThat(tableHeader).isVisible();
        labelSortDynamic.waitFor();
        assertThat(labelSortDynamic).isVisible();

    }
    @Step("Click admin button on sidebar")
    public void clickAdminSideBarButton(){
        sideBar.waitFor();
        assertThat(sideBar).isVisible();
        assertThat(adminSBButton).isVisible();
        log.info("SideBar visiable");
        adminSBButton.click();
        log.info("Header:" + headerTitle.textContent());
        assertThat(headerTitle).isVisible();
    }
}
