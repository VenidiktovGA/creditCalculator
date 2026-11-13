package com.example.creditcalculator.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Result of a compound interest calculation.
 *
 * @param request            the input the result was calculated for
 * @param finalBalance       account balance at the end of the term
 * @param totalContributions initial amount plus all monthly contributions
 * @param totalInterest      interest earned over the whole term
 * @param returnPercent      interest as a percentage of all contributions
 * @param schedule           year-by-year growth of the account
 */
public record CompoundInterestResult(
        CompoundInterestRequest request,
        BigDecimal finalBalance,
        BigDecimal totalContributions,
        BigDecimal totalInterest,
        BigDecimal returnPercent,
        List<YearlyGrowth> schedule) {
}
