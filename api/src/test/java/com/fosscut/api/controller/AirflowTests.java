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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fosscut.api.TestDefaults;
import com.fosscut.api.type.AirflowDAGLogsDTO;
import com.fosscut.api.util.ApiDefaults;
import com.fosscut.shared.SharedDefaults;

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
        Cookie cookie = new Cookie(SharedDefaults.COOKIE_IDENTIFIER, ApiDefaults.TEST_ORDER_IDENTIFIER);
        Cookie cookieSettings = new Cookie(ApiDefaults.COOKIE_SETTINGS_IDENTIFIER, TestDefaults.getDefaultSettingsJson());

        // When & Then
        MvcResult result = mockMvc.perform(post("/airflow/dag/run")
            .cookie(cookie)
            .cookie(cookieSettings))
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

        ObjectMapper objectMapper = new ObjectMapper();

        AirflowDAGLogsDTO logsDTO = new AirflowDAGLogsDTO();
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            // When & Then
            MvcResult result = mockMvc.perform(get("/airflow/dag/logs")
                .cookie(cookie))
                .andExpect(status().isOk()).andReturn();

            String json = result.getResponse().getContentAsString();
            logsDTO = objectMapper.readValue(json, AirflowDAGLogsDTO.class);

            if (logsDTO.getStatus().equals("success")) break;
        }

        assertThat(logsDTO.getStatus()).isEqualTo("success");
        assertThat(logsDTO.getLogs()).contains("Running cutting plan generation using a first-fit-decreasing algorithm...");
        assertThat(logsDTO.getLogs().lines().count() <= 154);
    }

}
