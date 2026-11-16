package com.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class ScanFormPage extends BasePage {
    private final String url = "https://globalshield.onrender.com/targets/new";

    private final By targetNameInput = By.id("target-name");
    private final By targetUrlInput = By.id("primary-web-target-url");
    private final By targetTypeSelect = By.cssSelector("select");
    private final By nextStepButton = By.xpath("//button[contains(., 'Next Step')]");
    private final By previousStepButton = By.xpath("//button[contains(., 'Previous Step') or contains(., 'Cancel')]");
    private final By inlineUrlFeedback = By.xpath("//input[@id='primary-web-target-url']/ancestor::div[1]/following-sibling::div[contains(@style, '12px')] | //div[contains(@style, 'margin-top: -12px') or contains(@style, 'marginTop: -12px')]");
    private final By stepErrorAlert = By.cssSelector("div[style*='status-danger']");
    private final By wizardHeader = By.xpath("//*[contains(text(), 'Step 1:') or contains(text(), 'Step 2:') or contains(text(), 'Security Target Creation Wizard')]");

    public ScanFormPage(WebDriver driver) {
        super(driver);
    }

    public ScanFormPage open() {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetUrlInput));
        return this;
    }

    public ScanFormPage enterTargetName(String name) {
        type(targetNameInput, name);
        return this;
    }

    public ScanFormPage enterTargetUrl(String url) {
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(targetUrlInput));
        e.clear();
        e.sendKeys(url);
        // Dispatch input and change events to trigger React's URL validation immediately
        executeJs("arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", e);
        return this;
    }

    public ScanFormPage selectTargetType(String typeValue) {
        WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(targetTypeSelect));
        new Select(selectElement).selectByValue(typeValue);
        return this;
    }

    public void clickNextStep() {
        click(nextStepButton);
    }

    public void clickPreviousStep() {
        click(previousStepButton);
    }

    public String getUrlValidationMessage() {
        return text(inlineUrlFeedback);
    }

    public boolean hasUrlValidationMessage() {
        return isVisible(inlineUrlFeedback);
    }

    public String getStepErrorMessage() {
        return text(stepErrorAlert);
    }

    public boolean hasStepErrorMessage() {
        return isVisible(stepErrorAlert);
    }

    public boolean isOnScanForm() {
        return waitForUrlContains("/targets/new");
    }

    public String getWizardHeaderText() {
        return text(wizardHeader);
    }
}
