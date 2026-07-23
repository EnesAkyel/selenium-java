# selenium-java

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.45-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.12-orange?style=for-the-badge)
![Allure](https://img.shields.io/badge/Allure-2.21-orange?style=for-the-badge)

UI test automation for [OrangeHRM](https://opensource-demo.orangehrmlive.com) — an open-source enterprise HR application. Covers authentication, employee management, and leave management across smoke and regression suites with parallel execution.

---

## Tech Stack

| Tool               | Version | Purpose                                                     |
|--------------------|---------|-------------------------------------------------------------|
| Java               | 25      | Language                                                    |
| Selenium WebDriver | 4.46.0  | Browser automation                                          |
| TestNG             | 7.12.0  | Test runner, parallel execution, DataProvider               |
| WebDriverManager   | 6.3.4   | Automatic driver binary management                          |
| Allure             | 2.35.3  | Reporting — steps, screenshots on failure, environment info |
| Maven              | 3.x     | Build and dependency management                             |

---

## Project Structure

```
src/test/java/SeleniumWebAutomation/
├── common/
│   ├── CommonTest.java        # @BeforeMethod/@AfterMethod; screenshot attached to Allure on failure
│   └── BasePage.java          # Driver reference, PageFactory init, explicit wait helpers
├── components/
│   └── TableComponent.java    # Reusable OrangeHRM table abstraction — row count, find by text, row actions
├── config/
│   ├── ConfigReader.java      # Reads config.properties
│   └── BrowserFactory.java    # Creates Chrome or Firefox driver; auto-headless when CI=true
├── driver/
│   └── DriverManager.java     # ThreadLocal<WebDriver> — parallel-safe, no inheritance required
├── pages/
│   ├── LoginPage.java         # Login form, error alert, field validation errors
│   ├── DashboardPage.java     # Post-login landing page, module navigation
│   ├── PimPage.java           # Employee list, search by ID, add employee, delete employee
│   └── LeavePage.java         # Leave list, leave types table
├── tests/
│   ├── LoginTest.java         # Valid login, invalid credentials, empty field validation
│   ├── PimTest.java           # Employee list smoke, search by ID (DataProvider), add/delete lifecycle
│   └── LeaveTest.java         # Leave module smoke, leave types populated
└── utils/
    └── Constants.java         # Shared assertion strings
src/test/resources/
├── config.properties          # Base URL, browser, timeouts, credentials
└── allure.properties          # Allure results directory
```

---

## Architecture

### DriverManager

`ThreadLocal<WebDriver>` lives in a standalone static utility, not in a base class. Any layer — page, component, test — accesses the current thread's driver via `DriverManager.getDriver()`. This makes the driver lifecycle explicit and keeps it out of the inheritance chain.

### BasePage

Page objects extend `BasePage`, which accepts `WebDriver` via constructor, initialises `PageFactory`, and exposes `waitForVisible` / `waitForClickable` backed by `WebDriverWait`. No test logic in pages — pages only interact with the UI and return the next page.

### Fluent page interface

Page methods return the next page object so tests read as a linear chain:

```java
new LoginPage()
    .loginWith(ConfigReader.getUsername(), ConfigReader.getPassword())
    .navigateToPim()
    .clickAddEmployee()
    .fillEmployeeForm("John", "Doe", "0001");
```

### TableComponent

`TableComponent` wraps OrangeHRM's `.oxd-table` structure and provides `getRowCount()`, `findRowContaining(text)`, and `clickActionInRow(text, iconClass)`. Pages compose it rather than duplicating table traversal logic.

### Screenshot on failure

`CommonTest.tearDown` checks `ITestResult.getStatus()`. On failure, it captures a PNG screenshot via `TakesScreenshot` and attaches it to the Allure report with `@Attachment`. The driver is always quit and the `ThreadLocal` is always cleared, regardless of outcome.

---

## Test Suites

| Suite      | File                    | Groups       | Parallel                 |
|------------|-------------------------|--------------|--------------------------|
| Smoke      | `testng-smoke.xml`      | `smoke` only | Sequential               |
| Regression | `testng-regression.xml` | All tests    | `tests` level, 3 threads |

---

## Running Locally

### Prerequisites

- Java 25+
- Maven 3.x
- Google Chrome or Firefox installed

### Smoke (default)

```bash
mvn test
```

### Regression

```bash
mvn test -Dtestng.suite=testng-regression.xml
```

### Firefox

```bash
mvn test -Dbrowser=firefox
```

### Headless

```bash
mvn test -Dheadless=true
```

### Allure Report

```bash
mvn allure:report
open target/site/allure-maven-plugin/index.html
```

---

## CI

GitHub Actions runs smoke tests on every push. Regression runs on `main` only, after smoke passes.

Headless mode activates automatically when the `CI` environment variable is `true` — set by default in all GitHub Actions runners.

---

## Author

**Enes Akyel**  
SDET | QA Automation Engineer  
[LinkedIn](https://www.linkedin.com/in/enes-akyel-2a77a7122/) · [GitHub](https://github.com/EnesAkyel)
