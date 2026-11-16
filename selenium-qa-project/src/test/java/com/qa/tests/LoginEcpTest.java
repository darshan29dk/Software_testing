package com.qa.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.base.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ScanFormPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginEcpTest extends BaseTest {

    @ParameterizedTest(name = "{0} [email=''{1}'', pass=''{2}''] -> ''{3}''")
    @DisplayName("ECP: Email and Password credential validation partitions")
    @CsvSource({
        "EMPTY_BOTH, '', '', 'Please enter both email and password.'",
        "EMPTY_EMAIL, '', 'SecurityKey123!', 'Please enter both email and password.'",
        "EMPTY_PASSWORD, 'operator@globalshield.internal', '', 'Please enter both email and password.'",
        "INVALID_CREDENTIALS, 'unregistered_operator@globalshield.internal', 'WrongPass123!', 'Invalid email or password'"
    })
    void emailAndPasswordPartitions(String partition, String email, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.login(email, password);
        assertThat(loginPage.getErrorMessage()).contains(expectedMessage);
    }

    @ParameterizedTest(name = "{0} [''{1}''] -> ''{2}''")
    @DisplayName("ECP: Scan Target URL validation partitions")
    @CsvSource({
        "MALFORMED_URL, not-a-valid-url, 'Invalid URL format. Please include protocol, e.g. https://example.com'",
        "FTP_SCHEME, ftp://vault.internal/repo, 'Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited.'",
        "FILE_SCHEME, file:///etc/shadow, 'Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited.'",
        "LOOPBACK_IP, http://127.0.0.1:8080, 'Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode.'",
        "PRIVATE_SUBNET, http://192.168.1.1, 'Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode.'",
        "VALID_HTTPS, https://owasp.org, 'Valid target hostname: owasp.org (https:)'",
        "VALID_HTTP, http://example.com, 'Valid target hostname: example.com (http:)'"
    })
    void scanUrlPartitions(String partition, String url, String expectedMessage) {
        ScanFormPage scanForm = new ScanFormPage(driver).open();
        scanForm.enterTargetUrl(url);
        assertThat(scanForm.getUrlValidationMessage()).contains(expectedMessage);
    }
}
