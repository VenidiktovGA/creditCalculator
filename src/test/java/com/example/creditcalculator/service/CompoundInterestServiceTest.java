package com.example.creditcalculator.service;

import com.example.creditcalculator.model.CompoundInterestRequest;
import com.example.creditcalculator.model.CompoundInterestResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CompoundInterestServiceTest {

    private final CompoundInterestService service = new CompoundInterestService();

    private CompoundInterestRequest request(String initial, String monthly, String rate, int years) {
        CompoundInterestRequest request = new CompoundInterestRequest();
        request.setInitialAmount(new BigDecimal(initial));
        request.setMonthlyContribution(new BigDecimal(monthly));
        request.setAnnualInterestRate(new BigDecimal(rate));
        request.setYears(years);
        return request;
    }

    @Test
    void zeroRateGrowsOnlyByContributions() {
        CompoundInterestResult result = service.calculate(request("1000", "100", "0", 1));

        assertThat(result.finalBalance()).isEqualByComparingTo("2200.00");
        assertThat(result.totalContributions()).isEqualByComparingTo("2200.00");
        assertThat(result.totalInterest()).isEqualByComparingTo("0.00");
        assertThat(result.returnPercent()).isEqualByComparingTo("0.0");
        assertThat(result.schedule()).hasSize(1);
    }

    @Test
    void monthlyCompoundingMatchesKnownValue() {
        CompoundInterestResult result = service.calculate(request("1000", "0", "12", 1));

        assertThat(result.finalBalance()).isEqualByComparingTo("1126.84");
        assertThat(result.totalContributions()).isEqualByComparingTo("1000.00");
        assertThat(result.totalInterest()).isEqualByComparingTo("126.84");
        assertThat(result.schedule().getFirst().barHeight()).isEqualTo(100);
    }

    @Test
    void contributionsAndInterestAddUpToTheBalance() {
        CompoundInterestResult result = service.calculate(request("10000", "500", "8", 10));

        assertThat(result.totalContributions()).isEqualByComparingTo("70000.00");
        assertThat(result.finalBalance())
                .isEqualByComparingTo(result.totalContributions().add(result.totalInterest()));
        assertThat(result.schedule()).hasSize(10);
        assertThat(result.schedule().getLast().balance()).isEqualByComparingTo(result.finalBalance());
        assertThat(result.schedule().getLast().barHeight()).isEqualTo(100);
    }

    @Test
    void zeroInputsProduceZeroBalance() {
        CompoundInterestResult result = service.calculate(request("0", "0", "10", 5));

        assertThat(result.finalBalance()).isEqualByComparingTo("0.00");
        assertThat(result.totalInterest()).isEqualByComparingTo("0.00");
        assertThat(result.returnPercent()).isEqualByComparingTo("0.0");
        assertThat(result.schedule()).hasSize(5);
        assertThat(result.schedule()).allMatch(row -> row.barHeight() == 0);
    }
}
