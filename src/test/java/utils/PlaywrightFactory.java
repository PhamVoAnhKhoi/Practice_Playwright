package utils;

import com.microsoft.playwright.*;

public class PlaywrightFactory {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static Page initalizeBrowser(){
        playwright = Playwright.create();
        boolean headed = Boolean.parseBoolean(ConfigReader.getProperty("headed")); //Default headed = "false"
        int slowMo = Integer.parseInt(ConfigReader.getProperty("slowMo"));
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(!headed) //false = Display Browser, true = Don't Display (Default)
                        .setSlowMo(slowMo) //Speed(ms)
        );
        context = browser.newContext();
        page = context.newPage();
        return page;
    }

    public static void tearDown(){
        context.close();
        browser.close();
        playwright.close();
    }

}
