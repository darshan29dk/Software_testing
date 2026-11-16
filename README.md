# Software_testing

## Automated UI & White-Box Testing Suite for GlobalShield

This repository contains an automated quality assurance test suite adapting a **Maven + Selenium 4 + JUnit 5 + AssertJ + WebDriverManager** Page Object Model (POM) framework to functionally test [GlobalShield](https://globalshield.onrender.com), a web application security and vulnerability management platform.

> **Security & Ethics Notice:** All automated tests perform non-destructive functional UI verification only (form validation, boundary value checks, navigation, and state progression). No attack payloads, exploit vectors, or security scans are executed against the target environment.

---

## 📊 Test Execution Summary

| Metric | Result | Status |
| :--- | :--- | :--- |
| **Total Tests** | **41** | ✅ All Passed |
| **Passed** | **41** | 100% Pass Rate |
| **Failed** | **0** | 0 Failures |
| **Errors** | **0** | 0 Errors |
| **Skipped** | **0** | 0 Skipped |
| **Execution Duration** | **01:48 min** | Clean Run (`-Dheadless=true`) |
| **Environment** | Java 17, Maven 3.9.9, Chrome 156 (Headless) | Windows 11 |

### Breakdown by Test Suite

| Test Class | Category | Test Count | Status | Description |
| :--- | :--- | :---: | :---: | :--- |
| `DiscountWhiteBoxTest` | White-Box / Logic | 22 | Passed | Basis path testing ($V(G)=9$), tier and items boundary analysis |
| `LoginEcpTest` | Black-Box / UI ECP | 11 | Passed | Equivalence class partitioning for Email, Password, and Scan URL |
| `PasswordBvaTest` | Black-Box / UI BVA | 7 | Passed | Boundary value analysis on registration password length |
| `ScanFlowTest` | E2E System Flow | 1 | Passed | End-to-end user navigation: Login → Register → Dashboard → Scan Wizard |
| **Total** | | **41** | **ALL PASSED** | |

---

## 📋 Equivalence Class Partitioning (ECP) Table

Equivalence Class Partitioning systematically tests valid and invalid input classes for authentication and scan target onboarding forms.

| Field | Partition ID | Partition Description | Test Input Value | Partition Type | Expected Result / Real System Message |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Email & Password** | `EMPTY_BOTH` | Both email & password empty | Email: `""`<br>Password: `""` | Invalid | `"Please enter both email and password."` |
| **Email** | `EMPTY_EMAIL` | Missing email with valid password | Email: `""`<br>Password: `"SecurityKey123!"` | Invalid | `"Please enter both email and password."` |
| **Password** | `EMPTY_PASSWORD` | Valid email with missing password | Email: `"operator@globalshield.internal"`<br>Password: `""` | Invalid | `"Please enter both email and password."` |
| **Credentials** | `INVALID_CREDENTIALS` | Unregistered / invalid account credentials | Email: `"unregistered_operator@globalshield.internal"`<br>Password: `"WrongPass123!"` | Invalid | `"Invalid email or password"` |
| **Scan Target URL** | `MALFORMED_URL` | URL missing schema/protocol | `"not-a-valid-url"` | Invalid | `"Invalid URL format. Please include protocol, e.g. https://example.com"` |
| **Scan Target URL** | `FTP_SCHEME` | Prohibited scheme (FTP) | `"ftp://vault.internal/repo"` | Invalid | `"Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited."` |
| **Scan Target URL** | `FILE_SCHEME` | Prohibited scheme (Local file) | `"file:///etc/shadow"` | Invalid | `"Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited."` |
| **Scan Target URL** | `LOOPBACK_IP` | SSRF protection: Loopback IP | `"http://127.0.0.1:8080"` | Invalid | `"Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode."` |
| **Scan Target URL** | `PRIVATE_SUBNET` | SSRF protection: Private subnet | `"http://192.168.1.1"` | Invalid | `"Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode."` |
| **Scan Target URL** | `VALID_HTTPS` | Valid public HTTPS target URL | `"https://owasp.org"` | Valid | `"Valid target hostname: owasp.org (https:)"` |
| **Scan Target URL** | `VALID_HTTP` | Valid public HTTP target URL | `"http://example.com"` | Valid | `"Valid target hostname: example.com (http:)"` |

---

## 📏 Boundary Value Analysis (BVA) Table

Boundary Value Analysis tests critical thresholds on user registration password length where minimum allowed length is $Min = 8$ characters and nominal upper limit is $Max = 64$ characters.

| Field | Boundary Point | Password Length | Generated Test Value | Validity | Expected Result / System Behavior |
| :--- | :--- | :---: | :--- | :---: | :--- |
| **Registration Password** | **Min - 1** | 7 chars | `"A1!aaaa"` | ❌ Invalid | Displays error: `"Password must be at least 8 characters long."` |
| **Registration Password** | **Min** | 8 chars | `"A1!aaaaa"` | ✅ Valid | Accepted; length error is **not** displayed. |
| **Registration Password** | **Min + 1** | 9 chars | `"A1!aaaaaa"` | ✅ Valid | Accepted; length error is **not** displayed. |
| **Registration Password** | **Nominal** | 16 chars | `"A1!aaaaaaaaaaaaa"` | ✅ Valid | Accepted; length error is **not** displayed. |
| **Registration Password** | **Max - 1** | 63 chars | `"A1!" + 60 'a's` | ✅ Valid | Accepted; length error is **not** displayed. |
| **Registration Password** | **Max** | 64 chars | `"A1!" + 61 'a's` | ✅ Valid | Accepted; length error is **not** displayed. |
| **Registration Password** | **Max + 1** | 65 chars | `"A1!" + 62 'a's` | ✅ Valid | Accepted; length error is **not** displayed. |

---

## 🔄 End-to-End (E2E) Main User Flow

The `ScanFlowTest` verifies the complete authenticated customer journey:
1. **Landing & Login:** Opens `LoginPage` at `/login` and verifies page readiness.
2. **Tab Transition:** Switches to `SignupPage` ("Create Account") verifying form fields.
3. **Return Navigation:** Returns to Sign In tab.
4. **Operations Dashboard:** Navigates to authenticated `/dashboard` and validates command cards and register links.
5. **Target Onboarding Wizard:** Enters Target Registration at `/targets/new`:
   - Enters target identification name.
   - Enters valid primary URL (`https://owasp.org`).
   - Asserts real-time validation message: `"Valid target hostname: owasp.org (https:)"`.
   - Selects target type (`WEB_URL`).
   - Clicks **Next: Scan Scope** to advance to Step 2.

---

## 🧩 Page Object Model (POM) Structure

The application pages are encapsulated under `src/main/java/com/qa/pages`:

```
src/main/java/com/qa/pages/
├── BasePage.java         # Explicit 30s WebDriverWait, SafeClick, SafeType, JS helpers
├── LoginPage.java        # Email/password inputs, Sign In tab, error banner retrieval
├── SignupPage.java       # Full name, email, password inputs, Create Account tab
├── DashboardPage.java    # Authenticated operations metrics, 'Register Target' action
└── ScanFormPage.java     # Target wizard Step 1/2 inputs, real-time URL validation message
```

*Note: In accordance with POM best practices, Page classes contain element locators and user actions only, with strictly zero assertions.*

---

## ⚙️ How to Run the Tests

### Prerequisites
- **Java Development Kit (JDK):** 17+
- **Apache Maven:** 3.8+
- **Google Chrome:** Latest stable release

### Execution Commands

```bash
# Run the entire test suite in headless mode (Recommended)
mvn clean test -Dheadless=true

# Run with visible Chrome browser
mvn clean test

# Run individual test classes
mvn test -Dtest=LoginEcpTest -Dheadless=true
mvn test -Dtest=PasswordBvaTest -Dheadless=true
mvn test -Dtest=ScanFlowTest -Dheadless=true
mvn test -Dtest=DiscountWhiteBoxTest
```

### Surefire Test Reports
HTML and XML test reports are generated automatically at:
```
target/surefire-reports/
```
