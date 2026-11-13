package com.example.creditcalculator.web;

import com.example.creditcalculator.service.CompoundInterestService;
import com.example.creditcalculator.service.LoanCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
@Import({LoanCalculatorService.class, CompoundInterestService.class})
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void landingPageHighlightsBothCalculators() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("landing"))
                .andExpect(model().attributeExists("example", "compoundExample", "currency"))
                .andExpect(content().string(containsString("Калькулятор ипотечных платежей")))
                .andExpect(content().string(containsString("Калькулятор сложного процента")))
                .andExpect(content().string(containsString("₽")));
    }

    @Test
    void landingPageLinksToBothCalculators() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("href=\"/mortgage\"")))
                .andExpect(content().string(containsString("href=\"/compound-interest\"")));
    }
}
