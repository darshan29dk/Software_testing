# Test Execution Results and Evidence

## 1. Executive Summary
The automated test suite was executed against the **GlobalShield** Vulnerability Scanner target application using Apache Maven and JUnit 5 in headless Chrome mode.

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.qa.tests.DiscountWhiteBoxTest
Tests run: 22, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.715 s -- in com.qa.tests.DiscountWhiteBoxTest
Running com.qa.tests.LoginEcpTest
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 62.47 s -- in com.qa.tests.LoginEcpTest
Running com.qa.tests.PasswordBvaTest
[DEFECT-AUTH-01 CONFIRMED] Live SUT accepted 65-character password and advanced to OTP verification.
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 44.00 s -- in com.qa.tests.PasswordBvaTest
Running com.qa.tests.ScanFlowTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.652 s -- in com.qa.tests.ScanFlowTest

Results:

Tests run: 41, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:12 min
[INFO] Finished at: 2026-11-16T20:33:27+05:30
[INFO] ------------------------------------------------------------------------
```

---

## 2. Quantitative Results by Test Suite

| Test Class | Focus & Scope | Executed | Passed | Failed | Errors | Skipped | Elapsed Time | Pass Rate |
|:---|:---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| `DiscountWhiteBoxTest` | White-Box: 9 Basis Paths + 13 Boundary Values ($V(G)=9$) | 22 | 22 | 0 | 0 | 0 | 0.715 s | **100.0%** |
| `LoginEcpTest` | Black-Box ECP: Email/Password partitions & Scan URL SSRF/protocol rules | 11 | 11 | 0 | 0 | 0 | 62.47 s | **100.0%** |
| `PasswordBvaTest` | Black-Box BVA: 7-point boundary analysis (7, 8, 9, 36, 63, 64, 65) | 7 | 7 | 0 | 0 | 0 | 44.00 s | **100.0%** |
| `ScanFlowTest` | End-to-End: Complete user journey (Auth $\rightarrow$ Dashboard $\rightarrow$ Target Wizard) | 1 | 1 | 0 | 0 | 0 | 9.652 s | **100.0%** |
| **Total Test Suite** | **Comprehensive Full System Verification** | **41** | **41** | **0** | **0** | **0** | **02:12 min** | **100.0%** |

---

## 3. Evidence Artifacts and Report Locations

### 3.1 Maven Surefire XML Reports
Raw machine-readable XML test reports detailing test timestamps, execution duration, and assertion statuses:
- [`TEST-com.qa.tests.DiscountWhiteBoxTest.xml`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/target/surefire-reports/TEST-com.qa.tests.DiscountWhiteBoxTest.xml)
- [`TEST-com.qa.tests.LoginEcpTest.xml`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/target/surefire-reports/TEST-com.qa.tests.LoginEcpTest.xml)
- [`TEST-com.qa.tests.PasswordBvaTest.xml`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/target/surefire-reports/TEST-com.qa.tests.PasswordBvaTest.xml)
- [`TEST-com.qa.tests.ScanFlowTest.xml`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/target/surefire-reports/TEST-com.qa.tests.ScanFlowTest.xml)

### 3.2 HTML Execution Report
Generated via `maven-surefire-report-plugin` version `3.2.5`:
- **HTML Report Path:** [`target/site/surefire-report.html`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/target/site/surefire-report.html)
- **Report Features:** Class-level breakdown, percentage success graph, method execution timelines, and failure summary.

---

## 4. Execution Commands
To reproduce the clean verification and HTML report generation:
```bash
# Execute complete headless test suite
mvn clean test -Dheadless=true

# Generate standalone Surefire HTML report from surefire XML outputs
mvn surefire-report:report-only
```
