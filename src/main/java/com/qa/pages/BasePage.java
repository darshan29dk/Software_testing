package com.qa.pages;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected void type(By by, String text) {
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        e.clear();
        e.sendKeys(text);
    }

    protected void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    protected String text(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).getText();
    }

    protected boolean isPresent(By by) {
        return !driver.findElements(by).isEmpty();
    }

    protected boolean isVisible(By by) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected String getAttribute(By by, String attributeName) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by)).getAttribute(attributeName);
    }

    protected Object executeJs(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public boolean waitForUrlContains(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }
}
