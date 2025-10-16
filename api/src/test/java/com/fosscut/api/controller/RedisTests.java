package com.fosscut.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fosscut.api.util.ApiDefaults;
import com.fosscut.shared.SharedDefaults;

import jakarta.servlet.http.Cookie;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class RedisTests {

    @Autowired
    private Redis controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void getIdentifier_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/redis/get/identifier"))
            .andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).hasSize(ApiDefaults.IDENTIFIER_LENGTH);
    }

    @Test
    void saveOrder_InvalidYaml() throws Exception {
        // Given
        String invalidOrderDTOString ="{"
            + "\"identifier\": \"" + ApiDefaults.TEST_ORDER_IDENTIFIER + "\","
            + "\"order\": \"invalid-yaml\""
            + "}";

        // When & Then
        mockMvc.perform(put("/redis/save/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidOrderDTOString))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    void saveOrder_Success() throws Exception  {
        // Given
        String orderDTOString ="{"
            + "\"identifier\": \"" + ApiDefaults.TEST_ORDER_IDENTIFIER + "\", "
            + "\"order\": \"inputs:\\n - length: 1000\\noutputs:\\n - length: 688 \\n   count: 11\""
            + "}";

        // When & Then
        mockMvc.perform(put("/redis/save/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderDTOString))
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void checkOrderSaved_Success() throws Exception {
        // Given
        Cookie cookie = new Cookie(SharedDefaults.COOKIE_IDENTIFIER, ApiDefaults.TEST_ORDER_IDENTIFIER);

        // When & Then
        mockMvc.perform(get("/redis/check/order/saved")
            .cookie(cookie))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }

    @Test
    void checkOrderSaved_Failure() throws Exception {
        // Given
        Cookie cookie = new Cookie(SharedDefaults.COOKIE_IDENTIFIER, "this order does not exist");

        // When & Then
        mockMvc.perform(get("/redis/check/order/saved")
            .cookie(cookie))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
    }

}
