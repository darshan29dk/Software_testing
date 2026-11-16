# Equivalence Class Partitioning (ECP) Test Design Document

## 1. Overview
Equivalence Class Partitioning (ECP) is a specification-based black-box test technique where the input domain of the system is divided into partitions of equivalent data. Test cases are derived such that one representative value from each partition is evaluated. If one test case in a partition succeeds or fails, all other test cases in the same partition are assumed to exhibit identical behavior.

For the **GlobalShield** Vulnerability Scanner application (`https://globalshield.onrender.com`), formal ECP is applied to the two critical functional entry points:
1. **Authentication Credentials** (`LoginPage`: Email and Password inputs)
2. **Perimeter Target Specification** (`ScanFormPage`: Primary Web Target URL)

---

## 2. Equivalence Partitions Identification

### 2.1 Login Credentials (Email & Password)
- **Partitions for Email Input:**
  - `EC_E1 (Valid)`: Standard RFC 5322 formatted email string associated with a registered account.
  - `EC_E2 (Invalid - Empty)`: Empty string `""` or whitespace-only input.
  - `EC_E3 (Invalid - Unregistered)`: Syntactically valid email format not present in credential store.
- **Partitions for Password Input:**
  - `EC_P1 (Valid)`: Valid credential string satisfying minimum complexity requirements.
  - `EC_P2 (Invalid - Empty)`: Empty string `""` or whitespace-only input.
  - `EC_P3 (Invalid - Mismatched)`: Non-empty string not matching the registered account hash.

### 2.2 Scan Target URL
- **Partitions for Target URL:**
  - `EC_U1 (Valid - Secure HTTPS Scheme)`: Fully qualified URL starting with `https://` pointing to a public target domain.
  - `EC_U2 (Valid - Standard HTTP Scheme)`: Fully qualified URL starting with `http://` pointing to a public target domain.
  - `EC_U3 (Invalid - Missing/Malformed Protocol)`: Domain or raw string without protocol prefix (e.g., missing `http://` or `https://`).
  - `EC_U4 (Invalid - Prohibited Protocol Scheme)`: Disallowed URL schemes such as `ftp://`, `file://`, or `javascript:`.
  - `EC_U5 (Invalid - Loopback / Localhost Destination)`: URLs targeting `127.0.0.1`, `localhost`, or internal loopback adapters (SSRF prevention).
  - `EC_U6 (Invalid - Private RFC 1918 Subnet)`: URLs resolving to internal intranet addresses such as `192.168.x.x` or `10.x.x.x` (SSRF prevention).

---

## 3. Formal ECP Test Design Matrix

The following table reflects the actual automated test execution implemented in [`LoginEcpTest.java`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/src/test/java/com/qa/tests/LoginEcpTest.java):

| Partition ID | Input Field(s) | Equivalence Partition Description | Selected Test Input | Expected Result | Actual Result | Status |
|:---|:---|:---|:---|:---|:---|:---:|
| **ECP-AUTH-01** | Email & Password | Both credentials empty (`EC_E2`, `EC_P2`) | Email: `""`<br>Password: `""` | Error prompt: `"Please enter both email and password."` | Error alert displayed with exact text | **PASS** |
| **ECP-AUTH-02** | Email & Password | Empty email with populated password (`EC_E2`, `EC_P1`) | Email: `""`<br>Password: `"SecurityKey123!"` | Error prompt: `"Please enter both email and password."` | Error alert displayed with exact text | **PASS** |
| **ECP-AUTH-03** | Email & Password | Populated email with empty password (`EC_E1`, `EC_P2`) | Email: `"operator@globalshield.internal"`<br>Password: `""` | Error prompt: `"Please enter both email and password."` | Error alert displayed with exact text | **PASS** |
| **ECP-AUTH-04** | Email & Password | Unregistered account credentials (`EC_E3`, `EC_P3`) | Email: `"unregistered_operator@globalshield.internal"`<br>Password: `"WrongPass123!"` | Error prompt: `"Invalid email or password"` | Error alert displayed with exact text | **PASS** |
| **ECP-SCAN-01** | Target URL | Malformed string lacking protocol (`EC_U3`) | URL: `"not-a-valid-url"` | Inline validation error: `"Invalid URL format. Please include protocol, e.g. https://example.com"` | Real-time feedback contains expected validation message | **PASS** |
| **ECP-SCAN-02** | Target URL | Insecure/Prohibited FTP protocol scheme (`EC_U4`) | URL: `"ftp://vault.internal/repo"` | Inline security rule: `"Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited."` | Real-time feedback contains expected validation message | **PASS** |
| **ECP-SCAN-03** | Target URL | Prohibited local file system protocol scheme (`EC_U4`) | URL: `"file:///etc/shadow"` | Inline security rule: `"Scheme must be http:// or https://. Protocols like file://, javascript:, ftp:// are prohibited."` | Real-time feedback contains expected validation message | **PASS** |
| **ECP-SCAN-04** | Target URL | Loopback IP address / SSRF risk (`EC_U5`) | URL: `"http://127.0.0.1:8080"` | SSRF defense rule: `"Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode."` | Real-time feedback contains expected validation message | **PASS** |
| **ECP-SCAN-05** | Target URL | Private Class C subnet / SSRF risk (`EC_U6`) | URL: `"http://192.168.1.1"` | SSRF defense rule: `"Destination resolves to private, loopback, or cloud metadata. Prohibited in standard mode."` | Real-time feedback contains expected validation message | **PASS** |
| **ECP-SCAN-06** | Target URL | Valid HTTPS secure endpoint (`EC_U1`) | URL: `"https://owasp.org"` | Validation indicator: `"Valid target hostname: owasp.org (https:)"` | Real-time feedback displays green validation confirmation | **PASS** |
| **ECP-SCAN-07** | Target URL | Valid HTTP standard endpoint (`EC_U2`) | URL: `"http://example.com"` | Validation indicator: `"Valid target hostname: example.com (http:)"` | Real-time feedback displays green validation confirmation | **PASS** |

---

## 4. Test Automation Traceability
- **Test Class:** `com.qa.tests.LoginEcpTest`
- **Annotations Used:** `@ParameterizedTest`, `@CsvSource`, `@DisplayName`
- **Page Objects Utilized:**
  - `com.qa.pages.LoginPage`: `open()`, `login(email, password)`, `getErrorMessage()`
  - `com.qa.pages.ScanFormPage`: `open()`, `enterTargetUrl(url)`, `getUrlValidationMessage()`
- **Assertions:** AssertJ fluent assertions (`assertThat(...).contains(...)`)
