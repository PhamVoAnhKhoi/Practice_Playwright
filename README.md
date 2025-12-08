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
- **Playwright Browsers Installed** (Chromium, Firefox, WebKit)
- **Allure Commandline: 2.29.0+** (for report generation)
- **PostgreSQL: 17.5-1**

## Project Structure
The project adopts the Page Object Model (POM) design pattern to improve scalability and maintainability.
```
.
├── src
│    └── test
│         ├── java
│         │     ├── base
│         │     │     ├── AuthenticateBaseTest.java
│         │     │     └── BaseTest.java   
│         │     ├── DAO
│         │     │     └── UserDAO.java
│         │     ├── factory
│         │     │     └── PlaywrightFactory.java
│         │     ├── helpers
│         │     │     ├── DataHelper.java
│         │     │     └── ScreenshotHelper.java
│         │     ├── listeners
│         │     │     └── TestListener.java
│         │     ├── pages
│         │     │     ├── AdminPage.java
│         │     │     └── LoginPage.java
│         │     ├── tests
│         │     │     ├── AdminTests.java
│         │     │     └── LoginTests.java
│         │     └── utils
│         │           ├── AccountData.java
│         │           ├── ConfigReader.java
│         │           ├── DBConnection.java
│         │           ├── DBSetupUtils.java
│         │           ├── DBUtils.java
│         │           ├── ExcelHighlighter.java
│         │           ├── ExcelReader.java
│         │           ├── ExcelWriter.java
│         │           ├── SetupAuthState.java
│         │           └── SystemUser.java
│         └── resources
│                └──config.properties
├── pom.xml
└── testng.xml

```

* **base:** Contains the base test class for setup and teardown logic (browser/session management).
* **DAO:** Contains Database Access Object for performing CRUD operations on database entities.
* **factory:** Manages Playwright initialization and browser configuration.
* **helpers:** Utility classes that provide common reusable functions used across tests.
* **listaners:** TestNG listeners that handle test events.
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
    ```sh
    mvn clean install
    ```
    Check installation successfully
    ```sh
    mvn -version
    ```
3.  **Install Playwright Browsers**
    ```sh
    mvn playwright:install
    ```
5. **Configure PostgreSQL Database**
    1. **Install PostgreSQL (if not installed):** https://www.postgresql.org/download/
    2. **Create Database:**
        * **Open  pgAdmin 4 application**
           ```sh
            Servers → PostgreSQL 17 → Databases
            ```
          **Open Query Tool:**
          Click right mouse at Databases → Select Query Tool
        * **Run SQL query for creating Database**
          ```sql
          CREATE DATABASE orangehrm_test
          WITH OWNER = postgres
          ENCODING = 'UTF8'
          CONNECTION LIMIT = -1;
          ```
          **Note:**  
          `-1`: Number of users accessing  
          `UTF8`: Character encoding  
          `postgres`: Owner name  
          `orangehrm_test`: Your database name
        * **Create User (Optional)**
          ```sql
          CREATE USER test_user WITH ENCRYPTED PASSWORD 'your_password';
          ```
          Note:  
          `test_user`: Your username  
          `your_password`: Your user password
6. **Setup file config.properties**
    ```sql
    DBURL = jdbc:postgresql://localhost:2909/postgres
    DBUSERNAME = postgres
    DBPASSWORD = Tma@2022@T1P
    TABLENAME = users
    ```
   **Note:**  
   `localhost`: Your localhost name retrieved from the **Host name/address** field at **Connection** in the Server properties.  
   `2909`: Your port name retrieved from the **Post** field at **Connection** in the Server properties.  
   `postgres`: Your database name retrieved from the **Maintenance database** field at **Connection** in the Server properties.
   `users`: Your table name which you want to create
## Running Tests
The framework uses TestNG as the test runner and Playwright authentication state for all admin-related testcases.  
Note: Testcase `DemoFailureTests` just only run when you want to test feature `Capture screanshot when faifure`.

Before running any test except `LoginTests`, you must generate a valid file (`auth.json`) using the `utils.SetupAuthState`.  
This file stores the authenticated session so tests can run without logging in again.

1.  **Generate Authentication State**
    Open a terminal in the project root directory and run `SetupAuthState` class:
    ```sh
    mvn -Dexec.mainClass="utils.SetupAuthState"            
    ```
    **Note**: 
    * Perform the steps based on the instructions logged in the Terminal.
    * After running successfully, check the `auth.json` file to see whether the login state has been saved.
    
2. **Run the Entire Test Suite:**
    Run all testcases following the test suit:
    ```sh
    mvn clean test
    ```
3. **Run a Specific Test Class:**
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
3. **Generate standalone report**
   ```sh
   allure generate --single-file target/allure-results -o target/allure-report --clean 
    ```
   Report will be generated in folder: `target\allure-report`  
4. **View Excel Results:** The Excel will be generated clone file in `target/test-output/excel-clones` after running relative testcases 
