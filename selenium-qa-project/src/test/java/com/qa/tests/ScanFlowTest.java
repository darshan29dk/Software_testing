package com.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.base.BaseTest;
import com.qa.pages.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScanFlowTest extends BaseTest {

    @Test
    @DisplayName("E2E: Happy path user flow - Login/Signup navigation, Dashboard, and Target Scan Onboarding")
    void mainUserFlow() {
        // Step 1: Open Login Page
        LoginPage loginPage = new LoginPage(driver).open();
        assertThat(loginPage.isOnLoginPage()).isTrue();

        // Step 2: Switch to Sign Up tab and verify registration navigation
        SignupPage signupPage = loginPage.clickCreateAccountTab();
        assertThat(signupPage.isOnSignupPage()).isTrue();

        // Step 3: Switch back to Sign In
        loginPage = signupPage.clickSignInTab();
        assertThat(loginPage.isOnLoginPage()).isTrue();

        // Step 4: Access authenticated Operations Dashboard
        DashboardPage dashboard = new DashboardPage(driver).open();
        assertThat(dashboard.isOnDashboard()).isTrue();
        assertThat(dashboard.isRegisterTargetLinkVisible()).isTrue();

        // Step 5: Navigate to Register Target / Scan Form
        ScanFormPage scanForm = dashboard.goToRegisterTarget();
        assertThat(scanForm.isOnScanForm()).isTrue();

        // Step 6: Fill out Step 1 of Scan Target Wizard
        scanForm.enterTargetName("Primary Production Perimeter");
        scanForm.enterTargetUrl("https://owasp.org");

        // Verify real-time valid target feedback
        assertThat(scanForm.getUrlValidationMessage())
                .contains("Valid target hostname: owasp.org (https:)");

        scanForm.selectTargetType("WEB_URL");

        // Step 7: Advance to Step 2
        scanForm.clickNextStep();
        assertThat(scanForm.getWizardHeaderText()).containsAnyOf("Step 2:", "Scope", "Security");
    }
}
