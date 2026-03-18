package base;

import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.PlaywrightFactory;

public class AuthenticatedBaseTest extends BaseTest {

    private static final String authPath = "auth.json";

    @Override
    @BeforeMethod
    public void setUp() {
        page = PlaywrightFactory.initalizeBrowserWithAuth(authPath);
        page.navigate(ConfigReader.getDashboardUrl());
    }
}
