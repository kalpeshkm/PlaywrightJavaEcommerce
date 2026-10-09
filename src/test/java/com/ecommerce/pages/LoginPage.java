
package com.ecommerce.pages;

import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    private final String username =
            "[data-test='username']";

    private final String password =
            "[data-test='password']";

    private final String loginButton =
            "[data-test='login-button']";

    private final String errorMessage =
            "[data-test='error']";

    public void enterUsername(String user) {
        page.locator(username).fill(user);
    }

    public void enterPassword(String pass) {
        page.locator(password).fill(pass);
    }

    public void clickLogin() {
        page.locator(loginButton).click();
    }

    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }

    public String getErrorMessage() {
        return page.locator(errorMessage).innerText();
    }
}