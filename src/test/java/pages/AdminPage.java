package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class AdminPage {


    private Page page;
    private Locator sideBar;
    private Locator adminSBButton; //Xpath Axes

    private Locator sideBarMenu;

    private Locator topBar;

    private Locator headerTitle;
    private Locator tableHeader ;

    private Locator rows;
    private Locator labelSort;

    private static final Logger log = LoggerFactory.getLogger(AdminPage.class);

    public AdminPage(Page page){
        this.page = page;
        this.sideBar = page.locator("nav[class='oxd-navbar-nav']");
        this.adminSBButton = page.locator("//ul[contains(@class,'oxd-main-menu')]/descendant::a[normalize-space(.)='Admin']");
        this.headerTitle = page.locator("//div[contains(@class,'oxd-topbar-header-title')]/descendant::h6[normalize-space(.)='Admin']");
        this.sideBarMenu = page.locator("//ul[@class='oxd-main-menu']/ancestor::div[contains(@class,'oxd-sidepanel-body')]");
        this.topBar = page.locator("//div[contains(@class,'oxd-topbar-body-nav')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.labelSort = page.locator("//label[contains(@class,'oxd-label') and normalize-space(text())='Username']");
    }

    public void getTableColData(){
        tableHeader.waitFor();
        int rowCount;
        rowCount = rows.count();
        log.info("Rows: "+rowCount);

        List<String> usernames = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        List<String> employeeNames = new ArrayList<>();
        List<String> statuses = new ArrayList<>();

        // Get data's cell
        for (int i = 0; i < rowCount; i++) {
            Locator row = rows.nth(i);
            List<String> cells = row.locator("xpath=.//div[contains(@class,'oxd-table-cell')]").allInnerTexts();

            usernames.add(cells.get(1).trim());      // Username
            roles.add(cells.get(2).trim());          // User Role
            employeeNames.add(cells.get(3).trim());  // Employee Name
            statuses.add(cells.get(4).trim());       // Status
        }
        log.info("Username: " + String.join(", ", usernames));
        log.info("Role: " + String.join(", ", roles));
        log.info("Employee Name: " + String.join(", ", employeeNames));
        log.info("Status: " + String.join(", ", statuses));
    }

    @Step("Check visible")
    public void checkVisible(){
        topBar.waitFor();
        assertThat(sideBarMenu).isVisible();
        assertThat(topBar).isVisible();

        tableHeader.waitFor();
        assertThat(tableHeader).isVisible();

        labelSort.waitFor();
        assertThat(labelSort).isVisible();
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
