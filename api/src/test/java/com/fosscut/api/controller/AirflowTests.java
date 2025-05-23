package com.fosscut.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import jakarta.servlet.http.Cookie;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class AirflowTests {

    @Autowired
    private Airflow controller;

    @Autowired
    private MockMvc mockMvc;

    public static String testDagRunID = "";

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @Order(2)
    void dagRun_Success() throws Exception {
        // Given
        Cookie cookie = new Cookie(ApiDefaults.COOKIE_IDENTIFIER, ApiDefaults.TEST_ORDER_IDENTIFIER);

        // When & Then
        MvcResult result = mockMvc.perform(post("/airflow/dag/run")
            .cookie(cookie))
            .andExpect(status().isOk()).andReturn();

        testDagRunID = result.getResponse().getContentAsString();

        assertThat(testDagRunID)
           .isNotNull()
           .isNotEmpty()
           .contains(ApiDefaults.TEST_ORDER_IDENTIFIER);
    }

    @Test
    @Order(3)
    void dagLogs_Success() throws Exception {
        // Given
        Cookie cookie = new Cookie(ApiDefaults.COOKIE_DAG_RUN_ID_IDENTIFIER, testDagRunID);

        String logs = "";
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            // When & Then
            MvcResult result = mockMvc.perform(get("/airflow/dag/logs")
                .cookie(cookie))
                .andExpect(status().isOk()).andReturn();

            logs = result.getResponse().getContentAsString();
            // remove whitespace because default empty log output from airflow always contains empty spaces
            if (!logs.replaceAll("\\s+","").isEmpty() && logs.lines().count() > 100) break;
        }

        assertThat(logs).contains("Starting cutting plan generation...");
        assertThat(logs.lines().count() <= 154);
    }

}
