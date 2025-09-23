package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginPage {
    private Page page;

    private Locator userName;
    private Locator passWord;
    private Locator loginButton;
    private Locator invalidError;
    private Locator requiredMessage;
    private Locator profileUser;

    private String waitElementLogin = "text=Dashboard";

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
    public void navigateToLoginPage(){
        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        page.waitForTimeout(30000);
    }

    //Enter Account
    public void loginAccount(String username, String password){
        userName.fill(username);
        passWord.fill(password);
    }

    //Click login button
    public void clickloginButton(){
        loginButton.click();
    }

    public void isLoginSuccess(){
        assertThat(page.waitForSelector(waitElementLogin));
        try {
            if(profileUser.isVisible()){
                System.out.println("Profile is visible");
            }
            else {
                System.out.println("Profile is not visible");
            }
        }
        catch(Error error){
            System.out.println("Message: " + error);
        }
    }

    //Show error
    public String getinvalidError(){
        return invalidError.textContent().trim();
    }

    // Lấy tất cả message "Required"
    public List<String> getRequiredMessages() {
        List<String> messages = new ArrayList<>();
        try {
            int count = requiredMessage.count();
            for (int i = 0; i < count; i++) {
                if (requiredMessage.nth(i).isVisible()) {
                    String text = requiredMessage.nth(i).textContent();
                    messages.add(text == null ? "" : text.trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Don't take Required messages: " + e.getMessage());
        }
        return messages;
    }
}
