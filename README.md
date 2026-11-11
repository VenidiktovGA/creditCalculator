# Loan Calculator

A small Spring Boot web application that calculates loan payments, total interest
and a full month-by-month repayment schedule.

## Tech stack

| Component | Version |
|-----------|---------|
| Java      | 25      |
| Spring Boot | 3.5.16 |
| Build     | Maven   |
| View      | Thymeleaf |
| Validation | Jakarta Bean Validation |

## Features

- Loan amount, annual interest rate and term in months as input.
- Two repayment schemes:
  - **Annuity** — equal payments every month.
  - **Differentiated** — principal repaid in equal parts, so payments decrease.
- Summary: first/last payment, total interest, total paid, overpayment percentage.
- Principal vs. interest split bar.
- Full payment schedule (month, payment, principal, interest, remaining balance).
- Server-side validation with error messages next to the fields.
- Responsive layout, light/dark color schemes, print-friendly schedule.

## Project structure

```
creditCalculator/
├── pom.xml
└── src
    ├── main
    │   ├── java/com/example/creditcalculator
    │   │   ├── CreditCalculatorApplication.java
    │   │   ├── model
    │   │   │   ├── LoanRequest.java
    │   │   │   ├── LoanResult.java
    │   │   │   ├── LoanType.java
    │   │   │   └── PaymentRow.java
    │   │   ├── service
    │   │   │   └── LoanCalculatorService.java
    │   │   └── web
    │   │       └── LoanController.java
    │   └── resources
    │       ├── application.properties
    │       ├── static/css/style.css
    │       └── templates/index.html
    └── test/java/com/example/creditcalculator
        ├── service/LoanCalculatorServiceTest.java
        └── web/LoanControllerTest.java
```

## Run

```bash
./mvnw spring-boot:run     # Windows: mvnw.cmd spring-boot:run
```

Then open <http://localhost:8080>.

## Build and test

```bash
./mvnw clean verify
java -jar target/credit-calculator-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Method | Path         | Description                                  |
|--------|--------------|----------------------------------------------|
| GET    | `/`          | Calculator form                              |
| POST   | `/calculate` | Validates input and renders the calculation  |

## Configuration

`src/main/resources/application.properties`:

- `app.currency.symbol` — currency symbol shown in the UI (default `$`).
- `server.port` — HTTP port (default `8080`).

## Calculation notes

- Monthly rate = annual rate / 12, charged on the outstanding balance.
- Annuity payment: `P = S · r / (1 − (1 + r)^−n)`, where `S` is the principal,
  `r` the monthly rate and `n` the number of months.
- Money is rounded to cents; the final payment absorbs rounding leftovers so the
  balance always ends at exactly zero.
- Fees, insurance and other bank commissions are not included.
