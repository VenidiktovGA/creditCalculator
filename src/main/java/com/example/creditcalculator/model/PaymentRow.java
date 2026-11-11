package com.example.creditcalculator.model;

import java.math.BigDecimal;

/**
 * One row of the repayment schedule.
 *
 * @param month            month number, starting at 1
 * @param payment          total payment for the month
 * @param principal        part of the payment that repays the loan body
 * @param interest         part of the payment that covers interest
 * @param remainingBalance loan balance after this payment
 */
public record PaymentRow(
        int month,
        BigDecimal payment,
        BigDecimal principal,
        BigDecimal interest,
        BigDecimal remainingBalance) {
}
