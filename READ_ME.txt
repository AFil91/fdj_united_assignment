# API Automation Test Suite

## Overview

This project is an automated test suite designed to validate a REST API for account management and financial transactions (deposits, withdrawals, and configuration limits).

The framework is built using **Java**, **TestNG**, and **REST Assured**, with **Maven** for build and dependency management.

It supports both **smoke** and **regression** testing, allowing flexible execution depending on the testing scope.

---

## Features

*  API testing using REST Assured
*  TestNG-based execution (groups, suites, data providers)
*  Smoke vs Regression test separation
*  Data-driven testing for negative scenarios
*  Centralized configuration (base URL, credentials)
*  Structured test classes by domain
*  Detailed request/response logging

---

## Project Structure

```
api-automation/
│
├── src/test/java/
│   ├── config/
│   │   └── Config.java
│   │
│   └── tests/
│       ├── AccountTests.java
│       ├── TransactionTests.java
│       └── ConfigLimitTests.java
│
├── testng-master.xml
├── testng-smoke.xml
├── testng-regression.xml
├── testng-all.xml
│
└── pom.xml
```

---

## Tech Stack

* Java 17
* TestNG
* REST Assured
* Maven

---

## Test Design

### Test Classes

Tests are grouped by functionality:

* **AccountTests** → account creation and validation
* **TransactionTests** → deposits and transaction flows
* **ConfigLimitTests** → configuration-related validations

---

### 🔹 Groups

Tests are categorized using TestNG groups:

* `smoke` → critical flows
* `regression` → full coverage
* `all` → optional grouping for full runs

Example:

```java
@Test(groups = {"smoke"})
public void adminCreateAccountTest() { ... }
```

---

### 🔹 Data-Driven Testing

Negative scenarios are implemented using `@DataProvider`:

```java
@DataProvider(name = "invalidAmounts")
public Object[][] invalidAmounts() {
    return new Object[][]{
        {-50},
        {"abc"},
        {0.5}
    };
}
```

---

## ▶️ How to Run Tests

### Run all tests

```bash
mvn clean test
```

### Run smoke suite

```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

### Run regression suite

```bash
mvn test -DsuiteXmlFile=testng-regression.xml
```

---

## Configuration

Base configuration is stored in:

```java
public class Config {
    public static final String BASE_URL = "http://localhost:9090/api";

    public static final String USERNAME_USER = "user";
    public static final String PASSWORD_USER = "userxxx";

    public static final String USERNAME_ADMIN = "admin";
    public static final String PASSWORD_ADMIN = "adminxxx";
}
```

---

## What is Tested

### Account API

* Create account (admin/user)
* Validation of invalid names
* Response structure (e.g., `createdAt` not null)

### Transactions API

* Deposit transactions
* Balance updates
* Transaction list updates
* Invalid input handling

### Business Logic Validation

* Balance consistency
* Transaction count changes
* Response correctness

---

##  Why This Framework

This framework was designed with the following goals:

### 🔹 Fast Feedback

Smoke tests validate critical functionality quickly.

### 🔹 Maintainability

Clear separation of concerns and reusable setup logic.

### 🔹 Scalability

Easy to extend with new endpoints or scenarios.

### 🔹 Reliability

API-level testing avoids flakiness common in UI tests.

---

##  Author

Ana Filote

---
