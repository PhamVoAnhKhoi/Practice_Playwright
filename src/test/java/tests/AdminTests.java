package tests;

import base.AuthenticatedBaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.UserManagementPage;
import utils.ScreenshotHelper;
import utils.SystemUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class AdminTests extends AuthenticatedBaseTest {
    private static final Logger log = LoggerFactory.getLogger(AdminTests.class);
    UserManagementPage userManagementPage;

    @BeforeMethod
    public void setUpAdminPage(){
        userManagementPage = new UserManagementPage(page);
        assertThat(userManagementPage.isSidebarVisible())
                .as("Sidebar must be visible after log in successfully")
                .isTrue();

        userManagementPage.navigateToAdminPage();
        assertThat(userManagementPage.isHeaderTitleVisible())
                .as("Header title must be visible")
                .isTrue();
    }

    @Test(description = "Get colum data from table")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User managerment table")
    public void takeDataFromTable(){
        List<SystemUser> users;
        users = userManagementPage.getUsers();
        assertThat(users).isNotEmpty();

        SystemUser firstUser = users.get(0);
        log.info("First user: {}", firstUser);

        assertThat(firstUser.getUsername()).isNotBlank();
        assertThat(firstUser.getUserRole()).isIn("Admin", "ESS");
        assertThat(firstUser.getStatus()).isIn("Enabled", "Disabled");
        ScreenshotHelper.captureAndAttach(page,"take data from table");
    }
}
