package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;
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
    private Locator tableHeader;//table header
    private Locator inputUsername;
    private Locator searchButton;
    private Locator iconDeleteButton;
    private Locator confirmDeleteButton;
    private Locator toastSuccessfully;
    private Locator toastNoRecordFound;
    private Locator userNameTable;
    private Locator loadSpinnerTable;

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public UserManagementPage(Page page){
        this.page = page;
        this.sideBar = page.locator("nav[class='oxd-navbar-nav']");
        this.adminSBButton = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='Admin']");
        this.headerTitle = page.locator("//div[contains(@class,'oxd-topbar-header-title')]/descendant::h6[normalize-space(.)='Admin']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.inputUsername = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Username')]/parent::div/following-sibling::div/input");
        this.searchButton = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.iconDeleteButton = page.locator("//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        this.confirmDeleteButton = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.toastNoRecordFound = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'No Records Found')]");
        this.userNameTable = page.locator("//div[contains(@class,'oxd-table-card-cell-checkbox')]/ancestor::div[contains(@class,'oxd-table-cell')]/following-sibling::div[1]/div");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");
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

    @Step("Search user by Username")
    public void searchUsername(String userName){
        inputUsername.fill(userName);
        searchButton.click();
    }

    @Step("Wait for search results to load")
    public void waitForSearchResult(){
        try{
            loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        }
        catch(Exception ignored){
            log.info("Spinner not visible - skipping wait for visible state.");
        }
        loadSpinnerTable.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        log.info("[User]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("Delete user on table")
    public void deleteUser(){
        iconDeleteButton.waitFor();
        iconDeleteButton.click();
        confirmDeleteButton.waitFor();
        confirmDeleteButton.click();
    }

    @Step("Delete Successfully")
    public boolean isDeleteSuccessfully(){
        try{
            toastSuccessfully.waitFor();
            return toastSuccessfully.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("No record is found")
    public boolean isUsernameInvisibleAfterDelete(){
        try{
            toastNoRecordFound.waitFor();
            return toastNoRecordFound.isVisible();
        }
        catch(Exception e){
            return false;
        }
    }

    @Step("Check username visible in table")
    public boolean isUsenameVisibleInTable(String userName){
        try{
            userNameTable.waitFor();
            String username = userNameTable.innerText();
            log.info("Username is visible in table: " + username);
            log.info("Unique Username: " + userName);
            if (username.equals(userName)){
                log.info("Username in table is correct");
                return userNameTable.isVisible();
            }
            else {
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
    }
}
