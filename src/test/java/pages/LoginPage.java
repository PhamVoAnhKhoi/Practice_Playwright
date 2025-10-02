package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class LoginPage {
    private Page page;

    private Locator userName;
    private Locator passWord;
    private Locator loginButton;
    private Locator invalidError;
    private Locator requiredMessage;
    private Locator profileUser;

    private String waitElementLogin = "text=Dashboard";
    private String loginPageURL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    //Constructor LoginPage
    public LoginPage(Page page) {
        this.page = page;
        this.userName = page.locator("input[name='username']");
        this.passWord = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
        this.invalidError = page.locator("text=Invalid credentials");
        this.requiredMessage = page.locator("text=Required");
        this.profileUser = page.locator("li[class='oxd-userdropdown']");
    }

    //Navigate to Login Page
    @Step("Navigate to Login page.")
    public void navigateToLoginPage(){
        page.navigate(loginPageURL);
        loginButton.waitFor();
    }

    //Enter Account
    @Step("Login with username: {username}, password: {password}")
    public void loginAccount(String username, String password){
        userName.fill(username);
        passWord.fill(password);
        loginButton.click();
    }

    //Verify Login status
    @Step("Login success")
    public boolean isLoginSuccess(){
        page.waitForSelector(waitElementLogin);
        try {
            profileUser.waitFor();
            return profileUser.isVisible();
        } catch(Exception e)
        {
            return false;
        }
    }

    //Show error
    @Step("Invalid credentials")
    public String getInvalidError(){
        return invalidError.textContent().trim();
    }

    // Take all message "Required"
    public List<String> getAllRequiredMessages() {
        requiredMessage.first().waitFor();
        List<String> messages = new ArrayList<>();
        int count = requiredMessage.count();
        for (int i = 0; i < count; i++) {
            if (requiredMessage.nth(i).isVisible()) {
                String text = requiredMessage.nth(i).textContent();
                messages.add(text == null ? "" : text.trim());
            }
        }
        return messages;
    }
}
