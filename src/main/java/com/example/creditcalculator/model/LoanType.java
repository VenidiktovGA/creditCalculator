package com.example.creditcalculator.model;

/**
 * Repayment scheme used by the loan.
 */
public enum LoanType {

    /**
     * Equal monthly payments (annuity). The payment amount stays the same,
     * while the share of interest decreases over time.
     */
    ANNUITY("Аннуитетный", "Равные платежи каждый месяц"),

    /**
     * Declining payments: the principal is repaid in equal parts and interest
     * is charged on the outstanding balance, so payments get smaller.
     */
    DIFFERENTIATED("Дифференцированный", "Платежи уменьшаются каждый месяц");

    private final String label;
    private final String description;

    LoanType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
