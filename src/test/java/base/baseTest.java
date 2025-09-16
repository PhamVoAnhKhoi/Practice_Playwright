package base;

import com.microsoft.playwright.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class baseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                .setHeadless(false) //false = Display Browser, true = Don't Display (Default)
                .setSlowMo(1000) //Speed(ms)
        );
        context = browser.newContext();
        page = context.newPage();
    }
    @AfterClass
    public void tearDown(){
        context.close();
        browser.close();
        playwright.close();
    }
}
