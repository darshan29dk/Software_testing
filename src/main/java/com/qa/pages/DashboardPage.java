package com.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {
    private final String url = "https://globalshield.onrender.com/dashboard";

    private final By registerTargetLink = By.cssSelector("a[href='/targets/new']");
    private final By targetInventoryLink = By.cssSelector("a[href='/targets']");
    private final By securityScansLink = By.cssSelector("a[href='/assessments']");
    private final By dashboardLink = By.cssSelector("a[href='/dashboard']");
    private final By dashboardHeader = By.xpath("//*[contains(text(), 'Operations Dashboard') or contains(text(), 'GlobalShield') or contains(text(), 'Tenant Targets') or contains(text(), 'Security Engine')]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage open() {
        driver.get(url);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(registerTargetLink),
            ExpectedConditions.visibilityOfElementLocated(dashboardHeader)
        ));
        return this;
    }

    public ScanFormPage goToRegisterTarget() {
        click(registerTargetLink);
        return new ScanFormPage(driver);
    }

    public void goToTargetInventory() {
        click(targetInventoryLink);
    }

    public void goToSecurityScans() {
        click(securityScansLink);
    }

    public boolean isOnDashboard() {
        return waitForUrlContains("/dashboard");
    }

    public boolean isRegisterTargetLinkVisible() {
        return isVisible(registerTargetLink);
    }

    public String getHeaderText() {
        return text(dashboardHeader);
    }
}
