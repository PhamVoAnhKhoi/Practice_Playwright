package base;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import utils.PlaywrightFactory;

public class BaseTest {
    protected Page page;

    @BeforeMethod
    public void setUp(){
        page = PlaywrightFactory.initalizeBrowser();
    }
    @AfterMethod
    public void closeBrowser(){
        PlaywrightFactory.tearDown();
    }
}
