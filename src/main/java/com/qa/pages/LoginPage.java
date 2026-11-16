package com.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final String url = "https://globalshield.onrender.com/login";

    private final By emailInput = By.cssSelector("input[type='email']");
    private final By passwordInput = By.cssSelector("input[type='password']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By createAccountTab = By.cssSelector(".auth-nav-tabs button:last-child");
    private final By signInTab = By.cssSelector(".auth-nav-tabs button:first-child");
    private final By errorAlert = By.cssSelector("div[style*='status-danger'], .login-page-container div[style*='var(--status-danger']");
    private final By loginContainer = By.cssSelector(".login-page-container");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        return this;
    }

    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public void clickSignIn() {
        click(submitButton);
    }

    public void login(String email, String password) {
        // Disable HTML5 native validation so React onSubmit handler fires for testing empty/invalid inputs
        executeJs("document.querySelectorAll('form').forEach(f => f.setAttribute('novalidate', 'true'));");
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        } else {
            WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
            e.clear();
        }

        if (password != null && !password.isEmpty()) {
            enterPassword(password);
        } else {
            WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
            p.clear();
        }

        clickSignIn();
    }

    public SignupPage clickCreateAccountTab() {
        click(createAccountTab);
        return new SignupPage(driver);
    }

    public String getErrorMessage() {
        return text(errorAlert);
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorAlert);
    }

    public String getEmailValidationMessage() {
        return getAttribute(emailInput, "validationMessage");
    }

    public boolean isOnLoginPage() {
        return waitForUrlContains("/login");
    }

    public boolean isOnDashboard() {
        return waitForUrlContains("/dashboard");
    }
}
