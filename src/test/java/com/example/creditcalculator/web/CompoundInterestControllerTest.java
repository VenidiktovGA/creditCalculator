package com.example.creditcalculator.web;

import com.example.creditcalculator.service.CompoundInterestService;
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

@WebMvcTest(CompoundInterestController.class)
@Import(CompoundInterestService.class)
class CompoundInterestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void compoundInterestPageIsRendered() throws Exception {
        mockMvc.perform(get("/compound-interest"))
                .andExpect(status().isOk())
                .andExpect(view().name("compound-interest"))
                .andExpect(model().attributeExists("compoundRequest", "currency"))
                .andExpect(content().string(containsString("Калькулятор сложного процента")));
    }

    @Test
    void calculateAddsResultToModel() throws Exception {
        mockMvc.perform(post("/compound-interest/calculate")
                        .param("initialAmount", "1000")
                        .param("monthlyContribution", "100")
                        .param("annualInterestRate", "0")
                        .param("years", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("compound-interest"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attributeHasNoErrors("compoundRequest"))
                .andExpect(content().string(containsString("Рост по годам")))
                .andExpect(content().string(containsString("1\u00A0000,00\u00A0₽")))
                .andExpect(content().string(containsString("line-chart")))
                .andExpect(content().string(containsString("chart-line-balance")))
                .andExpect(content().string(containsString("chart-line-contributions")))
                .andExpect(content().string(containsString("chart-grid-line")))
                .andExpect(content().string(containsString("chart-y-label")))
                .andExpect(content().string(containsString("2\u00A0500 ₽")));
    }

    @Test
    void invalidInputReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/compound-interest/calculate")
                        .param("initialAmount", "-1")
                        .param("monthlyContribution", "0")
                        .param("annualInterestRate", "101")
                        .param("years", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("compound-interest"))
                .andExpect(model().attributeHasFieldErrors(
                        "compoundRequest", "initialAmount", "annualInterestRate", "years"))
                .andExpect(model().attributeDoesNotExist("result"))
                .andExpect(content().string(containsString("Начальная сумма не может быть отрицательной")))
                .andExpect(content().string(containsString("Срок должен быть не меньше 1 года")));
    }
}
