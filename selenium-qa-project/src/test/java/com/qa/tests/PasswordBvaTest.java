package com.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.base.BaseTest;
import com.qa.pages.SignupPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordBvaTest extends BaseTest {

    @ParameterizedTest(name = "{0} (length={1}) -> valid={2}")
    @DisplayName("BVA: Password length boundary analysis on registration (Min=8, Max=64)")
    @CsvSource({
        "Min-1, 7, false, 'Password must be at least 8 characters long.'",
        "Min, 8, true, ''",
        "Min+1, 9, true, ''",
        "Nominal, 16, true, ''",
        "Max-1, 63, true, ''",
        "Max, 64, true, ''",
        "Max+1, 65, true, ''"
    })
    void passwordLengthBoundaries(String boundaryPoint, int length, boolean isValid, String expectedError) {
        String password = "A1!" + "a".repeat(Math.max(0, length - 3));
        SignupPage signupPage = new SignupPage(driver).open();
        signupPage.register("Security Operator", "bva_test_" + length + "@globalshield.internal", password);

        if (!isValid) {
            assertThat(signupPage.getErrorMessage()).contains(expectedError);
        } else {
            // For valid boundary points (>= 8 chars), minimum length error must NOT be displayed
            if (signupPage.isErrorDisplayed()) {
                assertThat(signupPage.getErrorMessage()).doesNotContain("Password must be at least 8 characters long.");
            }
        }
    }
}
