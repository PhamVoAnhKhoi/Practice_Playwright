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

        attachPlaywrightEventHooks(page);

        return page;
    }

    private static void attachPlaywrightEventHooks(Page page) {

        // Log whenever Page navigate
        page.onFrameNavigated(frame -> {
            log.info("Navigated to URL: {}", frame.url());
        });

        // Log whenever request send
        page.onRequest(request -> {
            log.debug("Request: {} {}", request.method(), request.url());
        });

        // Log whenever response return
        page.onResponse(response -> {
            log.debug("Response: {} {}", response.status(), response.url());
        });

        // Log whenever dialog alert/confirm/prompt appear
        page.onDialog(dialog -> {
            log.warn("Dialog appeared: {}", dialog.message());
            dialog.dismiss();
        });
    }

    public static void tearDown(){
        context.close();
        browser.close();
        playwright.close();
    }

}
