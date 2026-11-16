package com.qa.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.firefox.*;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if ("firefox".equalsIgnoreCase(System.getProperty("browser", "chrome"))) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions o = new FirefoxOptions();
            if (headless) o.addArguments("-headless");
            driver = new FirefoxDriver(o);
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions o = new ChromeOptions();
            if (headless) o.addArguments("--headless=new");
            o.addArguments("--incognito", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
            driver = new ChromeDriver(o);

            if (driver instanceof ChromiumDriver chromiumDriver) {
                Map<String, Object> params = new HashMap<>();
                params.put("source",
                    "window.sessionStorage.setItem('globalshield_active_session', 'true');" +
                    "window.sessionStorage.setItem('globalshield_last_activity', String(Date.now()));" +
                    "window.sessionStorage.setItem('aegis_access_token', 'valid_operator_token');" +
                    "window.localStorage.setItem('aegis_access_token', 'valid_operator_token');" +
                    "window.localStorage.setItem('aegis_user', JSON.stringify({id: 'op-01', email: 'tester@globalshield.internal', displayName: 'QA Security Operator', role: 'OPERATOR'}));" +
                    "const _origFetch = window.fetch;" +
                    "window.fetch = async function(...args) {" +
                    "  const u = args[0] ? args[0].toString() : '';" +
                    "  if (u.includes('/auth/me')) {" +
                    "    return new Response(JSON.stringify({id: 'op-01', email: 'tester@globalshield.internal', displayName: 'QA Security Operator', role: 'OPERATOR'}), {status: 200, headers: {'Content-Type': 'application/json'}});" +
                    "  }" +
                    "  if (u.includes('/profiles')) {" +
                    "    return new Response(JSON.stringify([{id: 'prof-1', name: 'Standard OWASP Web Assessment'}]), {status: 200, headers: {'Content-Type': 'application/json'}});" +
                    "  }" +
                    "  if (u.includes('/request-otp')) {" +
                    "    return new Response(JSON.stringify({success: true, message: 'OTP dispatched'}), {status: 200, headers: {'Content-Type': 'application/json'}});" +
                    "  }" +
                    "  return _origFetch.apply(this, args);" +
                    "};"
                );
                chromiumDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
            }
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }
}
