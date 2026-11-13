package com.example.creditcalculator.model;

import java.math.BigDecimal;

/**
 * One year of growth in the compound interest schedule.
 *
 * @param year               year number, starting at 1
 * @param totalContributions money added to the account by this year, including the initial amount
 * @param totalInterest      interest earned by this year
 * @param balance            account balance at the end of the year
 * @param barHeight          balance as a percentage of the final balance, used by the chart
 */
public record YearlyGrowth(
        int year,
        BigDecimal totalContributions,
        BigDecimal totalInterest,
        BigDecimal balance,
        int barHeight) {
}
