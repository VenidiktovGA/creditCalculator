package com.example.creditcalculator.web;

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

@WebMvcTest(LoanController.class)
@Import(LoanCalculatorService.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mortgagePageIsRendered() throws Exception {
        mockMvc.perform(get("/mortgage"))
                .andExpect(status().isOk())
                .andExpect(view().name("mortgage"))
                .andExpect(model().attributeExists("loanRequest", "loanTypes", "currency"))
                .andExpect(content().string(containsString("Калькулятор ипотечных платежей")));
    }

    @Test
    void calculateAddsResultToModel() throws Exception {
        mockMvc.perform(post("/mortgage/calculate")
                        .param("amount", "10000")
                        .param("annualInterestRate", "12")
                        .param("termMonths", "12")
                        .param("type", "ANNUITY"))
                .andExpect(status().isOk())
                .andExpect(view().name("mortgage"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attributeHasNoErrors("loanRequest"))
                .andExpect(content().string(containsString("График платежей")));
    }

    @Test
    void legacyCalculateEndpointKeepsWorking() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("amount", "10000")
                        .param("annualInterestRate", "12")
                        .param("termMonths", "12")
                        .param("type", "ANNUITY"))
                .andExpect(status().isOk())
                .andExpect(view().name("mortgage"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void invalidInputReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/mortgage/calculate")
                        .param("amount", "0")
                        .param("annualInterestRate", "12")
                        .param("termMonths", "0")
                        .param("type", "ANNUITY"))
                .andExpect(status().isOk())
                .andExpect(view().name("mortgage"))
                .andExpect(model().attributeHasFieldErrors("loanRequest", "amount", "termMonths"))
                .andExpect(model().attributeDoesNotExist("result"))
                .andExpect(content().string(containsString("Сумма кредита должна быть не меньше 1")))
                .andExpect(content().string(containsString("Срок должен быть не меньше 1 месяца")));
    }
}
