package com.example.creditcalculator.web;

import com.example.creditcalculator.model.CompoundInterestRequest;
import com.example.creditcalculator.model.LoanRequest;
import com.example.creditcalculator.service.CompoundInterestService;
import com.example.creditcalculator.service.LoanCalculatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private final LoanCalculatorService loanCalculatorService;
    private final CompoundInterestService compoundInterestService;
    private final String currencySymbol;

    public HomeController(LoanCalculatorService loanCalculatorService,
                          CompoundInterestService compoundInterestService,
                          @Value("${app.currency.symbol:$}") String currencySymbol) {
        this.loanCalculatorService = loanCalculatorService;
        this.compoundInterestService = compoundInterestService;
        this.currencySymbol = currencySymbol;
    }

    @ModelAttribute("currency")
    public String currency() {
        return currencySymbol;
    }

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("example", loanCalculatorService.calculate(new LoanRequest()));
        model.addAttribute("compoundExample",
                compoundInterestService.calculate(new CompoundInterestRequest()));
        return "landing";
    }
}
