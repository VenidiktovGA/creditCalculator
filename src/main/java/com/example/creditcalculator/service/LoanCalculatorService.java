package com.example.creditcalculator.service;

import com.example.creditcalculator.model.LoanRequest;
import com.example.creditcalculator.model.LoanResult;
import com.example.creditcalculator.model.LoanType;
import com.example.creditcalculator.model.PaymentRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculates annuity and differentiated loan schedules.
 *
 * <p>All money values are rounded to 2 decimal places. The final payment of a
 * schedule absorbs rounding leftovers so the balance always ends at exactly zero.
 */
@Service
public class LoanCalculatorService {

    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);
    private static final int MONEY_SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);

    public LoanResult calculate(LoanRequest request) {
        BigDecimal principal = request.getAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = monthlyRate(request.getAnnualInterestRate());
        int months = request.getTermMonths();

        List<PaymentRow> schedule = request.getType() == LoanType.ANNUITY
                ? annuitySchedule(principal, monthlyRate, months)
                : differentiatedSchedule(principal, monthlyRate, months);

        BigDecimal totalPaid = schedule.stream()
                .map(PaymentRow::payment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInterest = totalPaid.subtract(principal);
        BigDecimal overpayment = principal.signum() == 0
                ? BigDecimal.ZERO
                : totalInterest.multiply(HUNDRED, MC).divide(principal, 1, RoundingMode.HALF_UP);
        int principalShare = totalPaid.signum() == 0
                ? 100
                : principal.multiply(HUNDRED, MC).divide(totalPaid, 0, RoundingMode.HALF_UP).intValue();

        return new LoanResult(
                request,
                schedule.getFirst().payment(),
                schedule.getLast().payment(),
                totalInterest,
                totalPaid,
                overpayment,
                principalShare,
                List.copyOf(schedule));
    }

    private BigDecimal monthlyRate(BigDecimal annualInterestRate) {
        return annualInterestRate
                .divide(HUNDRED, MC)
                .divide(MONTHS_PER_YEAR, MC);
    }

    /**
     * Annuity: {@code P = S * r / (1 - (1 + r)^-n)}.
     */
    private BigDecimal annuityPayment(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (monthlyRate.signum() == 0) {
            return principal.divide(BigDecimal.valueOf(months), MONEY_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal growth = BigDecimal.ONE.add(monthlyRate).pow(months, MC);
        return principal.multiply(monthlyRate, MC)
                .multiply(growth, MC)
                .divide(growth.subtract(BigDecimal.ONE), MC)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private List<PaymentRow> annuitySchedule(BigDecimal principal, BigDecimal monthlyRate, int months) {
        BigDecimal payment = annuityPayment(principal, monthlyRate, months);
        List<PaymentRow> schedule = new ArrayList<>(months);
        BigDecimal balance = principal;

        for (int month = 1; month <= months; month++) {
            BigDecimal interest = interestFor(balance, monthlyRate);
            BigDecimal principalPart = month == months
                    ? balance
                    : payment.subtract(interest);

            balance = balance.subtract(principalPart);
            if (balance.signum() < 0) {
                balance = BigDecimal.ZERO.setScale(MONEY_SCALE);
            }
            schedule.add(new PaymentRow(month, principalPart.add(interest), principalPart, interest, balance));
        }
        return schedule;
    }

    private List<PaymentRow> differentiatedSchedule(BigDecimal principal, BigDecimal monthlyRate, int months) {
        BigDecimal monthlyPrincipal = principal.divide(
                BigDecimal.valueOf(months), MONEY_SCALE, RoundingMode.HALF_UP);
        List<PaymentRow> schedule = new ArrayList<>(months);
        BigDecimal balance = principal;

        for (int month = 1; month <= months; month++) {
            BigDecimal principalPart = month == months ? balance : monthlyPrincipal;
            BigDecimal interest = interestFor(balance, monthlyRate);

            balance = balance.subtract(principalPart);
            if (balance.signum() < 0) {
                balance = BigDecimal.ZERO.setScale(MONEY_SCALE);
            }
            schedule.add(new PaymentRow(month, principalPart.add(interest), principalPart, interest, balance));
        }
        return schedule;
    }

    private BigDecimal interestFor(BigDecimal balance, BigDecimal monthlyRate) {
        return balance.multiply(monthlyRate, MC).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
