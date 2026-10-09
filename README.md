# Playwright Java E-commerce Automation

## Project Overview

This project automates the testing of an e-commerce website using Playwright with Java and TestNG. It covers important user flows such as login, adding products to the cart, and completing the checkout process.

The project follows the Page Object Model (POM) to keep test scripts and page locators organized and maintainable.

## Technologies Used

* Java 21
* Playwright for Java
* TestNG
* Maven
* Extent Reports
* IntelliJ IDEA
* Git and GitHub

## Features

* Automated login testing
* Add products to the shopping cart
* Cart product verification
* Checkout information validation
* Customer details submission
* Order overview validation
* Order completion verification
* Test assertions using TestNG
* Page Object Model (POM) structure
* Extent Reports dependency for test reporting

## Project Structure

```text
PlaywrightJavaEcommerce/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/ecommerce/
│   │           └── pages/
│   │               ├── LoginPage.java
│   │               ├── InventoryPage.java
│   │               └── CheckoutPage.java
│   │
│   └── test/
│       └── java/
│           └── com/ecommerce/
│               ├── base/
│               │   └── BaseTest.java
│               └── tests/
│                   ├── CartTest.java
│                   └── CheckoutTest.java
│
├── pom.xml
├── README.md
├── .gitignore
└── testng.xml
```

*Note: Adjust the structure above to match the files actually present in your project.*

## Prerequisites

Install the following:

1. JDK 21
2. IntelliJ IDEA
3. Git
4. Maven (or use the Maven wrapper if your project includes one)

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/PlaywrightJavaEcommerce.git
cd PlaywrightJavaEcommerce
```

Replace `YOUR-USERNAME` with your GitHub username.

### 2. Install project dependencies

```bash
mvn clean install
```

### 3. Install Playwright browsers

Run the following Maven command from the project root:

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

If the command fails because the Playwright CLI is not configured in the Maven project, install the browsers using the Playwright CLI with the project's dependency classpath or the setup supported by your project.

## Running the Tests

### Run all tests

```bash
mvn test
```

### Run a specific test class

```bash
mvn -Dtest=CartTest test
```

```bash
mvn -Dtest=CheckoutTest test
```

You can also run a test class directly from IntelliJ IDEA by right-clicking it and selecting **Run**.

## Test Scenarios

| Test Class     | Scenario                                                              |
| -------------- | --------------------------------------------------------------------- |
| `CartTest`     | Login, add product, and verify the cart                               |
| `CheckoutTest` | Login, add product, enter customer information, and complete checkout |

## Test Reports

The project includes the Extent Reports dependency. If report generation is configured in the project, run the tests and open the generated HTML report.

The report path depends on your Extent Reports configuration. Common examples include:

```text
test-output/
reports/
ExtentReport.html
```

Check your report configuration to confirm the actual output path.

## Design Pattern

**Page Object Model (POM)**

The project separates page locators and browser actions from test cases. This improves code reusability, readability, and maintenance.

## Future Enhancements

* Data-driven testing
* Cross-browser testing
* Screenshot capture on test failure
* Automatic report generation
* GitHub Actions CI/CD integration

## Author

Kalpesh Mali
QA Manual & Automation Engineer

## Disclaimer

This project is intended for learning and demonstrating automation testing practices. Use a test environment and test credentials only.
