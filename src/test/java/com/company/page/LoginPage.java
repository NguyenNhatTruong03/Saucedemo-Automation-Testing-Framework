package com.company.page;

import org.openqa.selenium.By;

import io.qameta.allure.Step;


public class LoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
	
    @Step("Đăng nhập với username: {0} password: {1}")
    public void login(String username, String password) {
        enterText(usernameInput, username);
        enterText(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }


    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(loginButton);
    }
}
