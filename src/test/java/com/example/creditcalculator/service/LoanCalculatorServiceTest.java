package com.example.creditcalculator.service;

import com.example.creditcalculator.model.LoanRequest;
import com.example.creditcalculator.model.LoanResult;
import com.example.creditcalculator.model.LoanType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LoanCalculatorServiceTest {

    private final LoanCalculatorService service = new LoanCalculatorService();

    private LoanRequest request(String amount, String rate, int months, LoanType type) {
        LoanRequest request = new LoanRequest();
        request.setAmount(new BigDecimal(amount));
        request.setAnnualInterestRate(new BigDecimal(rate));
        request.setTermMonths(months);
        request.setType(type);
        return request;
    }

    @Test
    void annuityPaymentMatchesKnownValue() {
        LoanResult result = service.calculate(
                request("10000", "12", 12, LoanType.ANNUITY));

        assertThat(result.firstPayment()).isEqualByComparingTo("888.49");
        assertThat(result.schedule()).hasSize(12);
        assertThat(result.schedule().getLast().remainingBalance()).isEqualByComparingTo("0.00");
        assertThat(result.totalPaid()).isEqualByComparingTo("10661.86");
        assertThat(result.totalInterest()).isEqualByComparingTo("661.86");
    }

    @Test
    void differentiatedScheduleDeclinesOverTime() {
        LoanResult result = service.calculate(
                request("10000", "12", 12, LoanType.DIFFERENTIATED));

        assertThat(result.firstPayment()).isEqualByComparingTo("933.33");
        assertThat(result.lastPayment()).isEqualByComparingTo("841.70");
        assertThat(result.firstPayment()).isGreaterThan(result.lastPayment());
        assertThat(result.schedule()).hasSize(12);
        assertThat(result.schedule().getLast().remainingBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void zeroRateLoanHasNoInterest() {
        LoanResult result = service.calculate(
                request("1200", "0", 12, LoanType.ANNUITY));

        assertThat(result.firstPayment()).isEqualByComparingTo("100.00");
        assertThat(result.totalInterest()).isEqualByComparingTo("0.00");
        assertThat(result.overpayment()).isEqualByComparingTo("0.0");
        assertThat(result.schedule().getLast().remainingBalance()).isEqualByComparingTo("0.00");
    }

    @Test
    void scheduleAlwaysClosesTheBalance() {
        LoanResult result = service.calculate(
                request("123456.78", "7.75", 37, LoanType.ANNUITY));

        assertThat(result.schedule()).hasSize(37);
        assertThat(result.schedule().getLast().remainingBalance()).isEqualByComparingTo("0.00");
        assertThat(result.totalPaid())
                .isEqualByComparingTo(result.schedule().stream()
                        .map(row -> row.payment())
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertThat(result.principalShare()).isBetween(1, 100);
    }
}
