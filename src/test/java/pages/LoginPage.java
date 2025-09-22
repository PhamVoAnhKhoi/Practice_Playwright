package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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

    public String getRequiredMessage(String fieldName){
        Locator locator;
         if(fieldName.equals("username")){
             locator = requiredMessage.nth(0);
         }
         else if (fieldName.equals("password")){
             locator = requiredMessage.nth(1);
         }
         else {
             throw new IllegalArgumentException("Invalid field name: " + fieldName);
         }
         //Check element
        if (locator.isVisible()) {
            String text = locator.textContent();
            return text == null ? "Invisible text" : text.trim();
        } else {
            return "Invisible element"; // hoặc throw new AssertionError("Expected required message not visible for " + fieldName);
        }
    }
}
