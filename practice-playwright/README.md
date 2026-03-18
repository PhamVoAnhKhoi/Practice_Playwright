# Admin Tests Automation Framework for OrangeHRM
This repository provides a framework for automating the testing of OrangeHRM’s admin features using Playwright, TestNG, and Allure reporting.  
It focuses on automating key user management operations (such as creating and removing users) and applies the Page Object Model (POM) to ensure the codebase remains maintainable and scalable.

## Table of Contents
1.  [Prerequisites](#prerequisites)
2.  [Project Structure](#project-structure)
3.  [Setup Instructions](#setup-instructions)
4.  [Running Tests](#running-tests)
5.  [Reporting](#reporting)

## Prerequisites
Before running the tests, make sure you have:
- **Java JDK: 20+**
- **Maven:  3.9.11+**
- **Git: 2.47.1+**
- **TestNG: 7.10.2+**
- **Assert: 3.26.3**
- **Playwright: 1.55.0+**
- **Allure Commandline: 2.29.0+** (for report generation)

## Project Structure
The project adopts the Page Object Model (POM) design pattern to improve scalability and maintainability.
```
.
├── src
│    └── test
│         ├── java
│         │     ├── base
│         │     │     └── BaseTest.java   
│         │     ├── pages
│         │     │     ├── AdminPage.java
│         │     │     └── LoginPage.java
│         │     ├── tests
│         │     │     ├── AdminTests.java
│         │     │     └── LoginTests.java
│         │     └── utils
│         │           ├── AccountData.java
│         │           ├── ConfigReader.java
│         │           └── PlaywrightFactory.java
│         └── resources
│                └──config.properties
├── pom.xml
└── testng.xml

```

* **base:** Contains the base test class for setup and teardown logic (browser/session management).
* **pages** Contains Page Object classes, wrapping UI elements and interaction methods.
* **tests** Contains TestNG test classes and assertion logic.
* **ulils** Contains helper classes for configuration, test data, and browser management.

## Setup Instructions
1.  **Clone the Repository:**
    ```sh
    git clone git@gitsdc.tma.com.vn:pvakhoi-batch49/practice-playwright.git
    cd [your-repo-folder]
    ```
2.  **Install Dependencies:**
    The project uses Maven for dependency management. All dependencies are listed in `pom.xml` and will be downloaded automatically.

## Running Tests
The framework uses TestNG as the test runner, executed via Maven.

1.  **Run the Entire Test Suite:**
    Open a terminal in the project root directory and run:
    ```sh
    mvn clean test
    ```

2.  **Run a Specific Test Class:**
    To run only the `LoginTests` class, for example:
    ```sh
    mvn clean test -Dtest=LoginTests
    ```
## Reporting
The project is integrated with Allure for detailed test reporting.

1.  **Generate Allure Results:**
    After running `mvn clean test`, Allure results will be generated in the `allure-results` directory.

2.  **View the Allure Report:**
    To generate and open the HTML report, run the following commands:
    ```sh
    mvn allure:serve
    ```

