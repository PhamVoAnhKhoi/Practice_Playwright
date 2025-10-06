package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.UserManagementPage;
import pages.LoginPage;
import utils.AccountData;
import utils.ConfigReader;
import utils.SystemUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class AdminTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(AdminTests.class);
    LoginPage loginPage;
    UserManagementPage userManagementPage;

    @BeforeMethod
    public void setUpAdminPage(){
        loginPage = new LoginPage(page);
        userManagementPage = new UserManagementPage(page);
        loginPage.navigateToLoginPage();
        loginPage.loginAccount(ConfigReader.getAdminUser(),ConfigReader.getAdminPassword());
        assertThat(userManagementPage.isSidebarVisible())
                .as("Sidebar must be visible after log in successfully")
                .isTrue();
        userManagementPage.clickAdminSideBarButton();
        assertThat(userManagementPage.isHeaderTitleVisible())
                .as("Header title must be visible")
                .isTrue();
    }

    @Test(description = "Get colum data from table")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User managerment table")
    public void takeDataFromTable(){
        List<SystemUser> users = userManagementPage.getTableColData();
        assertThat(users).isNotEmpty();

        SystemUser firstUser = users.get(0);
        log.info("First user: {}", firstUser);

        assertThat(firstUser.getUsername()).isNotBlank();
        assertThat(firstUser.getUserRole()).isIn("Admin", "ESS"); // ví dụ hợp lý
        assertThat(firstUser.getStatus()).isIn("Enabled", "Disabled");
    }
}
