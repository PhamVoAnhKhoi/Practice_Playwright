package utils;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class SetupAuthState {
    private static final Logger log = LoggerFactory.getLogger(SetupAuthState.class);

    public static void main(String[] args) {
        System.out.println("Starting Authentication State Generator...");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(100)
            );
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate(ConfigReader.getLoginPageUrl());
            log.info("Please login manually in the opened browser...");
            log.info("After login, close the browser window to save auth state.");

            // Wait for user press Enter
            log.info("Press Enter please!");
            System.in.read();

            // Save authentication state in file auth.json
            context.storageState(new BrowserContext.StorageStateOptions()
                    .setPath(Paths.get("auth.json"))
            );
            log.info("Authentication state saved successfully to auth.json!");
            browser.close();
        } catch (Exception e) {
            log.info("Error while generating auth state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
