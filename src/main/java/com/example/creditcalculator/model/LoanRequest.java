package com.example.creditcalculator.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * User input for the loan calculator form.
 */
public class LoanRequest {

    @NotNull(message = "Укажите сумму кредита")
    @DecimalMin(value = "1.00", message = "Сумма кредита должна быть не меньше 1")
    @DecimalMax(value = "1000000000.00", message = "Сумма кредита слишком велика")
    private BigDecimal amount = new BigDecimal("250000");

    @NotNull(message = "Укажите процентную ставку")
    @DecimalMin(value = "0.0", message = "Процентная ставка не может быть отрицательной")
    @DecimalMax(value = "100.0", message = "Процентная ставка не может превышать 100%")
    private BigDecimal annualInterestRate = new BigDecimal("12.5");

    @NotNull(message = "Укажите срок кредита")
    @Min(value = 1, message = "Срок должен быть не меньше 1 месяца")
    @Max(value = 600, message = "Срок не может превышать 600 месяцев")
    private Integer termMonths = 60;

    @NotNull(message = "Выберите тип платежей")
    private LoanType type = LoanType.ANNUITY;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }
}
