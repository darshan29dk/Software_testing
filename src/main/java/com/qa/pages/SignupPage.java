package com.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SignupPage extends BasePage {
    private final String url = "https://globalshield.onrender.com/register";

    private final By fullNameInput = By.cssSelector("input[placeholder='e.g. Darshan K']");
    private final By emailInput = By.cssSelector("input[type='email']");
    private final By passwordInput = By.cssSelector("input[placeholder='••••••••••••']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By signInTab = By.cssSelector(".auth-nav-tabs button:first-child");
    private final By createAccountTab = By.cssSelector(".auth-nav-tabs button:last-child");
    private final By errorAlert = By.cssSelector("div[style*='status-danger'], .login-page-container div[style*='var(--status-danger)']");
    private final By signupContainer = By.cssSelector(".login-page-container");
    private final By otpHeader = By.xpath("//*[contains(text(), 'Registration Email Verification') or contains(text(), 'OTP Code')]");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public SignupPage open() {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(signupContainer));
        wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
        return this;
    }

    public SignupPage enterFullName(String name) {
        type(fullNameInput, name);
        return this;
    }

    public SignupPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    public SignupPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public void clickSubmit() {
        click(submitButton);
    }

    public void register(String fullName, String email, String password) {
        executeJs("document.querySelectorAll('form').forEach(f => f.setAttribute('novalidate', 'true'));");

        if (fullName != null && !fullName.isEmpty()) {
            enterFullName(fullName);
        } else {
            WebElement n = wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
            n.clear();
        }

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

        clickSubmit();
    }

    public LoginPage clickSignInTab() {
        click(signInTab);
        return new LoginPage(driver);
    }

    public String getErrorMessage() {
        return text(errorAlert);
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorAlert);
    }

    public boolean isOnSignupPage() {
        return isPresent(fullNameInput) || waitForUrlContains("/register");
    }

    public boolean isOnOtpVerification() {
        try {
            // If already visible, return immediately
            var elements = driver.findElements(By.xpath("//*[contains(text(), 'Registration Email Verification')]"));
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                return true;
            }
            // If an error is displayed, registration halted before OTP
            if (isErrorDisplayed()) {
                return false;
            }
            // Otherwise wait up to 8s for transition to OTP verification screen
            var shortWait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8));
            return shortWait.until(d -> {
                var list = d.findElements(By.xpath("//*[contains(text(), 'Registration Email Verification')]"));
                return !list.isEmpty() && list.get(0).isDisplayed();
            });
        } catch (Exception e) {
            return false;
        }
    }
}
