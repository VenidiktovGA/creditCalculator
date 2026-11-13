package com.example.creditcalculator.web;

import com.example.creditcalculator.model.CompoundInterestRequest;
import com.example.creditcalculator.service.CompoundInterestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompoundInterestController {

    private final CompoundInterestService calculatorService;
    private final String currencySymbol;

    public CompoundInterestController(CompoundInterestService calculatorService,
                                      @Value("${app.currency.symbol:\u20BD}") String currencySymbol) {
        this.calculatorService = calculatorService;
        this.currencySymbol = currencySymbol;
    }

    @ModelAttribute("currency")
    public String currency() {
        return currencySymbol;
    }

    @GetMapping("/compound-interest")
    public String compoundInterest(Model model) {
        if (!model.containsAttribute("compoundRequest")) {
            model.addAttribute("compoundRequest", new CompoundInterestRequest());
        }
        return "compound-interest";
    }

    @PostMapping("/compound-interest/calculate")
    public String calculate(@Valid @ModelAttribute("compoundRequest") CompoundInterestRequest request,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "compound-interest";
        }
        model.addAttribute("result", calculatorService.calculate(request));
        return "compound-interest";
    }
}
