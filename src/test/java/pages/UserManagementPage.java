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
    private Locator btnAdminSB; //Xpath Axes
    private Locator rows;
    private Locator headerTitle;
    private Locator tableHeader;
    private Locator inputUsername;
    private Locator btnSearch;
    private Locator btnConfirmDelete;
    private Locator toastSuccessfully;
    private Locator toastNoRecordFound;
    private Locator userNameTable;
    private Locator loadSpinnerTable;
    private Locator btnDelete;

    private static final Logger log = LoggerFactory.getLogger(UserManagementPage.class);

    public UserManagementPage(Page page){
        this.page = page;
        this.sideBar = page.locator("nav[class='oxd-navbar-nav']");
        this.btnAdminSB = page.locator("//a[contains(@class,'oxd-main-menu')]/descendant::span[normalize-space(.)='Admin']");
        this.headerTitle = page.locator("//div[contains(@class,'oxd-topbar-header-title')]/descendant::h6[normalize-space(.)='Admin']");
        this.rows = page.locator("//div[contains(@class,'oxd-table-body')]/descendant::div[contains(@class,'oxd-table-row')]");
        this.tableHeader = page.locator("//div[contains(@class,'oxd-table-header') and @role='rowgroup']");
        this.inputUsername = page.locator("//label[contains(@class,'oxd-label') and contains(normalize-space(text()),'Username')]/parent::div/following-sibling::div/input");
        this.btnSearch = page.locator("//div[contains(@class,'oxd-form-actions')]/descendant::button[contains(@class,'oxd-button') and normalize-space(.)='Search']");
        this.btnConfirmDelete = page.locator("//div[contains(@class,'oxd-sheet')]/descendant::button[contains(@class,'oxd-button') and contains(normalize-space(.),'Yes')]");
        this.toastSuccessfully = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'Successfully')]");
        this.toastNoRecordFound = page.locator("//div[contains(@class,'oxd-toast-container')]/descendant::p[contains(@class,'oxd-toast-content-text') and contains(normalize-space(.),'No Records Found')]");
        this.userNameTable = page.locator("//div[contains(@class,'oxd-table-card-cell-checkbox')]/ancestor::div[contains(@class,'oxd-table-cell')]/following-sibling::div[1]/div");
        this.loadSpinnerTable = page.locator("//div[contains(@class,'oxd-table-loader')]/descendant::div[@class='oxd-loading-spinner']");

    }

    @Step("Get all users from the table")
    public List<SystemUser> getUsers() {
        tableHeader.waitFor();

        Map<String, Integer> headerIndexMap = getHeaderIndexMap();
        List<SystemUser> users = new ArrayList<>();

        for (Locator row : getAllRows()) {
            users.add(extractUserFromRow(row, headerIndexMap));
        }

        log.info("Total users found: {}", users.size());
        return users;
    }



    @Step("Click Admin button on sidebar")
    public void clickAdminSideBarButton(){
        sideBar.waitFor();
        btnAdminSB.click();
    }

    @Step("Sidebar is visible")
    public boolean isSidebarVisible(){
        try{
            btnAdminSB.waitFor();
            return btnAdminSB.isVisible();
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
        btnSearch.click();
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
        tableHeader.waitFor();
        log.info("[User]Loading spinner has disappeared. Table is now stable.");
    }

    @Step("Delete user on table")
    public void deleteUser(String username){
        tableHeader.waitFor();
        Locator targetRow = rows.filter(new Locator.FilterOptions().setHasText(username.trim())).first();
        targetRow.waitFor();
        btnDelete = targetRow.locator("xpath=.//button[contains(@class,'oxd-icon-button')]/descendant::i[contains(@class,'oxd-icon bi-trash')]");
        btnDelete.click();
        btnConfirmDelete.waitFor();
        btnConfirmDelete.click();
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

    @Step("Verify user '{username}' is not visible in table after deletion")
    public boolean isUserNotVisibleInTable(String username) {
        tableHeader.waitFor();

        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(username.trim())
        );

        int count = matchingRows.count();
        log.info("After deletion, found {} matching rows for username '{}'", count, username);

        return count == 0;
    }

    @Step("Check user present in table")
    public boolean isUserPresentInTable(String username){
        rows.first().waitFor();
        Locator matchingRows = rows.filter(
                new Locator.FilterOptions().setHasText(username.trim())
        );

        int count = matchingRows.count();
        log.info("Found " + count + " matching rows for username: " + username);

        return count == 1;
    }

    @Step("Get user details from table by username")
    public SystemUser getUserDetailsFromTable(String username) {
        rows.first().waitFor();

        Map<String, Integer> headerIndexMap = getHeaderIndexMap();

        Locator matchedRow = rows.filter(
                new Locator.FilterOptions().setHasText(username.trim())
        ).first();

        matchedRow.waitFor();

        SystemUser user = extractUserFromRow(matchedRow,headerIndexMap);
        log.info("Extracted user details from table: {}", user);

        return user;
    }

    private Map<String, Integer> getHeaderIndexMap() {
        List<String> headerTexts = tableHeader
                .locator("xpath=.//div[contains(@class,'oxd-table-header-cell')]")
                .allInnerTexts();

        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (int i = 0; i < headerTexts.size(); i++) {
            headerIndexMap.put(headerTexts.get(i).trim(), i);
        }

        log.info("Header mapping: {}", headerIndexMap);
        return headerIndexMap;
    }

    private List<Locator> getAllRows() {
        int rowCount = rows.count();
        List<Locator> rowList = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            rowList.add(rows.nth(i));
        }
        return rowList;
    }

    private SystemUser extractUserFromRow(Locator row, Map<String, Integer> headerIndexMap) {
        List<String> cells = row.locator("xpath=.//div[contains(@class,'oxd-table-cell')]").allInnerTexts();

        String username = getCellText(cells, headerIndexMap, "Username");
        String role = getCellText(cells, headerIndexMap, "User Role");
        String employeeName = getCellText(cells, headerIndexMap, "Employee Name");
        String status = getCellText(cells, headerIndexMap, "Status");

        return new SystemUser(username, role, employeeName, status);
    }

    private String getCellText(List<String> cells, Map<String, Integer> headerMap, String key) {
        Integer index = headerMap.get(key);
        if (index == null || index >= cells.size()) {
            log.warn("Column '{}' not found or out of range in table row.", key);
            return "";
        }
        return cells.get(index).trim();
    }

}
