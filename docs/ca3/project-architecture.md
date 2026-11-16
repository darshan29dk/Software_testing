# Project Architecture and Implementation Details

## 1. System Architecture Overview

This test automation framework is engineered following the enterprise **Page Object Model (POM)** design pattern with a strict separation of concerns between web interactions (page objects) and behavioral verifications (test classes). 

```mermaid
graph TD
    subgraph Test_Layer [Test Specification Layer - JUnit 5 & AssertJ]
        T1[DiscountWhiteBoxTest] -->|White-Box Logic| DC[DiscountCalculator]
        T2[LoginEcpTest] -->|Interacts with| PO1[LoginPage]
        T2 -->|Interacts with| PO4[ScanFormPage]
        T3[PasswordBvaTest] -->|Interacts with| PO2[SignupPage]
        T4[ScanFlowTest] -->|End-to-End Orchestration| PO1
        T4 -->|Navigates| PO2
        T4 -->|Navigates| PO3[DashboardPage]
        T4 -->|Navigates| PO4
    end

    subgraph Base_Layer [Driver & Page Foundations]
        BT[BaseTest] -.->|Manages Lifecycle| WebDriver[(Selenium WebDriver)]
        PO1 -->|Extends| BP[BasePage]
        PO2 -->|Extends| BP
        PO3 -->|Extends| BP
        PO4 -->|Extends| BP
        BP -->|Encapsulates| WebDriver
    end

    subgraph Target_System [Target Under Test]
        WebDriver -->|Automates UI| WebApp["GlobalShield Vulnerability Scanner\n(https://globalshield.onrender.com)"]
    end
```

---

## 2. Technology Stack

| Component | Technology | Version | Purpose in Framework |
|:---|:---|:---:|:---|
| **Language Runtime** | Java SE | **17 (LTS)** | Modern language features, strong typing, pattern matching |
| **Build & Dependency Tool** | Apache Maven | **3.9.9** | Build lifecycle management, compiler plugin, surefire execution |
| **UI Automation** | Selenium WebDriver | **4.25.0** | Browser control, W3C WebDriver protocol, Chrome DevTools Protocol (CDP) |
| **Test Engine** | JUnit Jupiter (JUnit 5) | **5.10.2** | Lifecycle management (`@BeforeEach`, `@AfterEach`), parameterized data injection |
| **Fluent Assertions** | AssertJ | **3.26.0** | Expressive, readable, domain-specific assertions |
| **Driver Management** | WebDriverManager (Boni Garcia) | **5.9.2** | Automated browser binary resolution and driver setup |
| **Logging Provider** | SLF4J Simple | **2.0.13** | Standardized log routing |
| **Reporting Plugin** | Maven Surefire Report Plugin | **3.2.5** | Automated HTML test execution report generation |

---

## 3. Design Patterns & Best Practices

### 3.1 Strict Page Object Model (POM) Separation
- **Page Objects (`src/main/java/com/qa/pages`):** Contain exclusively element locators (`By.id`, `By.cssSelector`, `By.xpath`), synchronization routines, and user interaction methods (`type`, `click`, `enterEmail`, `register`). **No test assertions (`assertThat`, `assertEquals`) exist in page objects.**
- **Test Classes (`src/test/java/com/qa/tests`):** Responsible for test data definition, step sequencing, and all business assertions.

### 3.2 Dynamic Synchronization Strategy
- Global explicit timeout is configured to **30 seconds** (`new WebDriverWait(driver, Duration.ofSeconds(30))`) to account for Render free-tier cold starts.
- Polling is driven by explicit `ExpectedConditions` (`visibilityOfElementLocated`, `elementToBeClickable`).
- Quick-presence checks (`isVisible(By)`) utilize immediate `driver.findElements(...)` non-blocking lookups, ensuring that asserting absence of error alerts executes in under 5 ms rather than stalling for 30 seconds.

### 3.3 WebDriver Lifecycle & CDP Network Simulation
- Drivers are instantiated per test in `@BeforeEach` and cleanly terminated in `@AfterEach`.
- For Chromium browsers, `executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", ...)` injects session mock tokens into `sessionStorage` and intercepts API endpoints like `/auth/me` and `/request-otp`. This prevents third-party rate limits (`RATE_LIMIT_EXCEEDED`) from failing functional UI tests while enabling deterministic execution.

---

## 4. Source Tree Structure
```
c:\Users\Darshan\Downloads\selenium-qa-project\
├── pom.xml                                    # Maven configuration & plugins
├── README.md                                  # Comprehensive documentation
├── .gitignore                                 # Clean version control exclusions
├── docs/ca3/                                  # CA3 formal submission deliverables
│   ├── ecp-test-design.md
│   ├── bva-test-design.md
│   ├── control-flow-graph.md
│   ├── cyclomatic-complexity.md
│   ├── basis-path-testing.md
│   ├── execution-results.md
│   ├── project-architecture.md
│   └── presentation-content.md
├── src/main/java/com/qa/
│   ├── logic/
│   │   └── DiscountCalculator.java            # White-box logic (V(G)=9, 8 decisions)
│   └── pages/
│       ├── BasePage.java                      # Reusable web actions & wait wrappers
│       ├── LoginPage.java                     # Auth & Sign In page interactions
│       ├── SignupPage.java                    # Registration & OTP transitions
│       ├── DashboardPage.java                 # Post-login Operations Dashboard
│       └── ScanFormPage.java                  # Multi-step Target Wizard & URL checks
└── src/test/java/com/qa/
    ├── base/
    │   └── BaseTest.java                      # Cross-browser & CDP lifecycle harness
    └── tests/
        ├── DiscountWhiteBoxTest.java          # 22 White-box basis path & BVA tests
        ├── LoginEcpTest.java                  # 11 Black-box ECP parameterized tests
        ├── PasswordBvaTest.java               # 7 Black-box BVA parameterized tests
        └── ScanFlowTest.java                  # 1 End-to-End user journey test
```
