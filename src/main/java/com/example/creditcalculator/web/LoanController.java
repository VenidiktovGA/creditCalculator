package com.example.creditcalculator.web;

import com.example.creditcalculator.model.LoanRequest;
import com.example.creditcalculator.model.LoanType;
import com.example.creditcalculator.service.LoanCalculatorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoanController {

    private final LoanCalculatorService calculatorService;
    private final String currencySymbol;

    public LoanController(LoanCalculatorService calculatorService,
                          @Value("${app.currency.symbol:$}") String currencySymbol) {
        this.calculatorService = calculatorService;
        this.currencySymbol = currencySymbol;
    }

    @ModelAttribute("currency")
    public String currency() {
        return currencySymbol;
    }

    @ModelAttribute("loanTypes")
    public LoanType[] loanTypes() {
        return LoanType.values();
    }

    @GetMapping("/mortgage")
    public String mortgage(Model model) {
        if (!model.containsAttribute("loanRequest")) {
            model.addAttribute("loanRequest", new LoanRequest());
        }
        return "mortgage";
    }

    @PostMapping({"/mortgage/calculate", "/calculate"})
    public String calculate(@Valid @ModelAttribute("loanRequest") LoanRequest request,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "mortgage";
        }
        model.addAttribute("result", calculatorService.calculate(request));
        return "mortgage";
    }
}
