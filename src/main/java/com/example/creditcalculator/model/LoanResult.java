package com.example.creditcalculator.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Result of a loan calculation.
 *
 * @param request           the input the result was calculated for
 * @param firstPayment      payment for the first month
 * @param lastPayment       payment for the last month
 * @param totalInterest     total interest paid over the whole term
 * @param totalPaid         principal + total interest
 * @param overpayment       total interest as a percentage of the principal
 * @param principalShare    share of the principal in the total amount paid, in percent
 * @param schedule          month-by-month repayment schedule
 */
public record LoanResult(
        LoanRequest request,
        BigDecimal firstPayment,
        BigDecimal lastPayment,
        BigDecimal totalInterest,
        BigDecimal totalPaid,
        BigDecimal overpayment,
        int principalShare,
        List<PaymentRow> schedule) {

    public boolean isAnnuity() {
        return request.getType() == LoanType.ANNUITY;
    }

    public boolean hasConstantPayment() {
        return firstPayment.compareTo(lastPayment) == 0;
    }
}
