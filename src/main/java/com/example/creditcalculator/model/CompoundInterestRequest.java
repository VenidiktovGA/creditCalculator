package com.example.creditcalculator.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * User input for the compound interest calculator form.
 */
public class CompoundInterestRequest {

    @NotNull(message = "Укажите начальную сумму")
    @DecimalMin(value = "0.00", message = "Начальная сумма не может быть отрицательной")
    @DecimalMax(value = "1000000000.00", message = "Начальная сумма слишком велика")
    private BigDecimal initialAmount = new BigDecimal("10000");

    @NotNull(message = "Укажите сумму пополнения")
    @DecimalMin(value = "0.00", message = "Сумма пополнения не может быть отрицательной")
    @DecimalMax(value = "1000000000.00", message = "Сумма пополнения слишком велика")
    private BigDecimal monthlyContribution = new BigDecimal("500");

    @NotNull(message = "Укажите процентную ставку")
    @DecimalMin(value = "0.0", message = "Процентная ставка не может быть отрицательной")
    @DecimalMax(value = "100.0", message = "Процентная ставка не может превышать 100%")
    private BigDecimal annualInterestRate = new BigDecimal("8");

    @NotNull(message = "Укажите срок")
    @Min(value = 1, message = "Срок должен быть не меньше 1 года")
    @Max(value = 50, message = "Срок не может превышать 50 лет")
    private Integer years = 10;

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public BigDecimal getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(BigDecimal monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }
}
