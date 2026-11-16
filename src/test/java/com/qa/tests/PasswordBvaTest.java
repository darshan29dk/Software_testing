package com.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.base.BaseTest;
import com.qa.pages.SignupPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordBvaTest extends BaseTest {

    @ParameterizedTest(name = "{0} (length={1}) -> valid={2}")
    @DisplayName("BVA: Password length boundary value analysis (Min=8, Max=64)")
    @CsvSource({
        "Min-1, 7, false, 'Password must be at least 8 characters long.'",
        "Min, 8, true, ''",
        "Min+1, 9, true, ''",
        "Nominal, 36, true, ''",
        "Max-1, 63, true, ''",
        "Max, 64, true, ''",
        "Max+1, 65, false, 'Password must not exceed 64 characters.'"
    })
    void passwordLengthBoundaries(String boundaryPoint, int length, boolean isValid, String expectedError) {
        String password = "A1!" + "a".repeat(Math.max(0, length - 3));
        SignupPage signupPage = new SignupPage(driver).open();
        signupPage.register("Security Operator", "bva_test_" + length + "@globalshield.internal", password);

        if (!isValid) {
            assertThat(signupPage.isErrorDisplayed())
                .as("Invalid password length %d (%s) should trigger validation error", length, boundaryPoint)
                .isTrue();
            assertThat(signupPage.getErrorMessage())
                .as("Invalid password length %d (%s) should display expected error message", length, boundaryPoint)
                .contains(expectedError);
            assertThat(signupPage.isOnOtpVerification())
                .as("Invalid password length %d (%s) should not advance to OTP verification", length, boundaryPoint)
                .isFalse();
        } else {
            // For valid boundary points (8, 9, 36, 63, 64), assert successful progression to OTP verification
            assertThat(signupPage.isOnOtpVerification())
                .as("Valid password length %d (%s) should advance to OTP verification step", length, boundaryPoint)
                .isTrue();
            assertThat(signupPage.isErrorDisplayed())
                .as("Valid password length %d (%s) should not display validation error", length, boundaryPoint)
                .isFalse();
        }
    }
}

