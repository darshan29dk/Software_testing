# Boundary Value Analysis (BVA) Test Design Document

## 1. Overview
Boundary Value Analysis (BVA) is a specification-based black-box testing technique that targets boundary conditions on the borders of equivalence partitions. Software systems frequently fail at extremes due to off-by-one errors, loop boundary conditions, relational operator mismatches (`<` vs `<=`), or improper length checking.

In the **GlobalShield** Vulnerability Scanner application, the user account registration feature defines a strict security specification for password length:
$$\text{Allowable Range: } [L_{min}, L_{max}] = [8, 64] \text{ characters}$$

---

## 2. Derivation of 7-Point Boundary Values

For a single bounded interval $[A, B] = [8, 64]$, standard robust Boundary Value Analysis evaluates seven distinct boundary points:
1. **$Min - 1$ ($A - 1$):** Immediately below the lower boundary ($8 - 1 = 7$) $\rightarrow$ **Invalid**
2. **$Min$ ($A$):** Exact lower boundary ($8$) $\rightarrow$ **Valid**
3. **$Min + 1$ ($A + 1$):** Immediately above the lower boundary ($8 + 1 = 9$) $\rightarrow$ **Valid**
4. **$Nominal$:** Standard representative value in the middle range ($\approx \frac{8+64}{2} = 36$) $\rightarrow$ **Valid**
5. **$Max - 1$ ($B - 1$):** Immediately below the upper boundary ($64 - 1 = 63$) $\rightarrow$ **Valid**
6. **$Max$ ($B$):** Exact upper boundary ($64$) $\rightarrow$ **Valid**
7. **$Max + 1$ ($B + 1$):** Immediately above the upper boundary ($64 + 1 = 65$) $\rightarrow$ **Invalid**

---

## 3. Formal BVA Test Design Matrix

The following table reflects the actual automated test execution implemented in [`PasswordBvaTest.java`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/src/test/java/com/qa/tests/PasswordBvaTest.java):

| Boundary ID | Point Type | Password Length | Generated Input Sample | Validity | Expected Result & Verification Criteria | Actual Execution Result | Status |
|:---|:---|:---:|:---|:---:|:---|:---|:---:|
| **BVA-PW-01** | **Min - 1** | **7** | `"A1!aaaa"` | **Invalid** | Validation alert is displayed: `"Password must be at least 8 characters long."`; Form submission halted; Does NOT advance to OTP screen. | Error alert displayed with exact text; OTP screen not reached. | **PASS** |
| **BVA-PW-02** | **Min** | **8** | `"A1!aaaaa"` | **Valid** | Registration succeeds; Advances to OTP verification screen (`Registration Email Verification`); Error alert is NOT displayed. | Advances to OTP verification screen; Zero errors displayed. | **PASS** |
| **BVA-PW-03** | **Min + 1** | **9** | `"A1!aaaaaa"` | **Valid** | Registration succeeds; Advances to OTP verification screen (`Registration Email Verification`); Error alert is NOT displayed. | Advances to OTP verification screen; Zero errors displayed. | **PASS** |
| **BVA-PW-04** | **Nominal** | **36** | `"A1!" + "a"*33` | **Valid** | Registration succeeds; Advances to OTP verification screen (`Registration Email Verification`); Error alert is NOT displayed. | Advances to OTP verification screen; Zero errors displayed. | **PASS** |
| **BVA-PW-05** | **Max - 1** | **63** | `"A1!" + "a"*60` | **Valid** | Registration succeeds; Advances to OTP verification screen (`Registration Email Verification`); Error alert is NOT displayed. | Advances to OTP verification screen; Zero errors displayed. | **PASS** |
| **BVA-PW-06** | **Max** | **64** | `"A1!" + "a"*61` | **Valid** | Registration succeeds; Advances to OTP verification screen (`Registration Email Verification`); Error alert is NOT displayed. | Advances to OTP verification screen; Zero errors displayed. | **PASS** |
| **BVA-PW-07** | **Max + 1** | **65** | `"A1!" + "a"*62` | **Invalid** | Validation alert is displayed: `"Password must not exceed 64 characters."`; Form submission halted; Does NOT advance to OTP screen. | Error alert displayed with exact text; OTP screen not reached. | **PASS** |

---

## 4. Key Fixes & Design Integrity
- **Contradiction Resolved:** In prior unverified iterations, 65 characters was erroneously treated as valid. This contradiction was eliminated by enforcing the documented upper bound of 64 characters, marking 65 as `Invalid`.
- **Meaningful Dual Assertions:**
  - For **Invalid** boundaries (7, 65): Verifies `signupPage.isErrorDisplayed() == true`, `signupPage.getErrorMessage().contains(expectedError)`, and `signupPage.isOnOtpVerification() == false`.
  - For **Valid** boundaries (8, 9, 36, 63, 64): Verifies `signupPage.isOnOtpVerification() == true` (progresses to the OTP verification screen) and `signupPage.isErrorDisplayed() == false`.
  - The implementation strictly avoids weak checks (e.g., merely testing if one error message is absent).

---

## 5. Traceability & Execution
- **Test Class:** `com.qa.tests.PasswordBvaTest`
- **Method:** `passwordLengthBoundaries(String boundaryPoint, int length, boolean isValid, String expectedError)`
- **Framework:** JUnit 5 `@ParameterizedTest` with `@CsvSource`
- **Total Execution Time:** ~40 seconds for 7 browser-driven Selenium runs
- **Outcome:** 7 / 7 Passed (0 Failures, 0 Errors)
