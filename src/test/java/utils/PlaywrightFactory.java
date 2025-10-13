package utils;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.AdminTests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightFactory {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;
    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    public static Page initalizeBrowser(){
        return initalizeBrowserWithAuth(null);
    }
    public static Page initalizeBrowserWithAuth(String authPath){
        playwright = Playwright.create();
        boolean headed = Boolean.parseBoolean(ConfigReader.getProperty("headed")); //Default headed = "false"
        int slowMo = Integer.parseInt(ConfigReader.getProperty("slowMo"));
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(!headed) //false = Display Browser, true = Don't Display (Default)
                        .setSlowMo(slowMo) //Speed(ms)
        );

        // If file auth.json exist => use current session
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        if (authPath != null) {
            Path authFile = Paths.get(authPath);
            if (!Files.exists(authFile)) {
                throw new RuntimeException("Authentication file not found: " + authPath +
                        "\nPlease run SetupAuthState.java to generate a new auth.json file.");
            }
            options.setStorageStatePath(authFile);
        } else {
            log.warn("No authentication state used — starting fresh session.");
        }

        context = browser.newContext(options);
        page = context.newPage();

        return page;
    }

    public static void tearDown(){
        context.close();
        browser.close();
        playwright.close();
    }

}
