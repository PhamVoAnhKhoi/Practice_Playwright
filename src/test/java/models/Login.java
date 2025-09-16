package models;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Login {
    private Page page;
    private Locator userName;
    private Locator passWord;
    private Locator loginButton;


    //Constructor LoginPage
    public Login(Page page) {
        this.page = page;
        this.userName = page.locator("input[name='username']");
        this.passWord = page.locator("input[name='password']");
        this.loginButton = page.locator("button[type='submit']");
    }

    //Navigate to Login Page
    public void navigateToLoginPage(){
        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    //Enter Account
    public void loginAccount(String username, String password){
        userName.fill(username);
        passWord.fill(password);
    }

    //Click login button
    public void clicklogin(){
        loginButton.click();
    }


}
