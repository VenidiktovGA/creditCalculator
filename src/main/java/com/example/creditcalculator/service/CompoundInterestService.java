package com.example.creditcalculator.service;

import com.example.creditcalculator.model.CompoundInterestRequest;
import com.example.creditcalculator.model.CompoundInterestResult;
import com.example.creditcalculator.model.YearlyGrowth;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculates compound growth with monthly capitalization and monthly contributions.
 *
 * <p>Money values are rounded to 2 decimal places every month, which matches the way
 * a bank would add interest to an account.
 */
@Service
public class CompoundInterestService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final int MONEY_SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);

    public CompoundInterestResult calculate(CompoundInterestRequest request) {
        BigDecimal monthlyRate = request.getAnnualInterestRate()
                .divide(HUNDRED, MC)
                .divide(MONTHS_PER_YEAR, MC);
        BigDecimal monthlyContribution = request.getMonthlyContribution()
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal balance = request.getInitialAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal contributions = balance;

        List<YearlyGrowth> yearlyRows = new ArrayList<>(request.getYears());

        for (int year = 1; year <= request.getYears(); year++) {
            for (int month = 0; month < 12; month++) {
                BigDecimal interest = balance.multiply(monthlyRate, MC)
                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
                balance = balance.add(interest)
                        .add(monthlyContribution)
                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
            }
            contributions = contributions.add(monthlyContribution.multiply(MONTHS_PER_YEAR));
            yearlyRows.add(new YearlyGrowth(
                    year,
                    contributions,
                    balance.subtract(contributions),
                    balance,
                    0));
        }

        BigDecimal totalContributions = contributions;
        BigDecimal finalBalance = balance;
        BigDecimal totalInterest = finalBalance.subtract(totalContributions);
        BigDecimal returnPercent = totalContributions.signum() == 0
                ? BigDecimal.ZERO
                : totalInterest.multiply(HUNDRED, MC)
                        .divide(totalContributions, 1, RoundingMode.HALF_UP);

        List<YearlyGrowth> schedule = yearlyRows.stream()
                .map(row -> new YearlyGrowth(
                        row.year(),
                        row.totalContributions(),
                        row.totalInterest(),
                        row.balance(),
                        barHeight(row.balance(), finalBalance)))
                .toList();

        return new CompoundInterestResult(
                request,
                finalBalance,
                totalContributions,
                totalInterest,
                returnPercent,
                schedule);
    }

    private int barHeight(BigDecimal balance, BigDecimal finalBalance) {
        if (finalBalance.signum() == 0) {
            return 0;
        }
        return balance.multiply(HUNDRED, MC)
                .divide(finalBalance, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}
