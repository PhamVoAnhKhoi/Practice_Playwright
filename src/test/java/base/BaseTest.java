package base;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    LoginPage loginPage;

    @BeforeMethod
    public void setUp(){
        playwright = Playwright.create();
        boolean headed = Boolean.parseBoolean(System.getProperty("headed", "false")); //Default headed = "false"
        int slowMo = Integer.parseInt(System.getProperty("slowMo", "0"));
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                .setHeadless(!headed) //false = Display Browser, true = Don't Display (Default)
                .setSlowMo(slowMo) //Speed(ms)
        );
        context = browser.newContext();
        page = context.newPage();
    }
    @AfterMethod
    public void tearDown(){
        context.close();
        browser.close();
        playwright.close();
    }
}
