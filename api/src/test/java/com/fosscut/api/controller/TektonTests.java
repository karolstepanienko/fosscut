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
import com.fosscut.api.type.TektonTaskRunLogsDTO;
import com.fosscut.api.util.ApiDefaults;

import jakarta.servlet.http.Cookie;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TektonTests {

    @Autowired
    private Tekton controller;

    @Autowired
    private MockMvc mockMvc;

    // Given
    private static final Cookie cookie = new Cookie(
        ApiDefaults.COOKIE_IDENTIFIER,
        ApiDefaults.TEST_ORDER_IDENTIFIER
    );

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @Order(2)
    void taskRunCreate_Success() throws Exception {
        // Given
        Cookie cookieSettings = new Cookie(ApiDefaults.COOKIE_SETTINGS_IDENTIFIER, TestDefaults.getDefaultSettingsJson());

        // When & Then
        mockMvc.perform(post("/tekton/taskRun/create")
            .cookie(cookie)
            .cookie(cookieSettings))
            .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Order(3)
    void taskRunGetLogs_Success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        TektonTaskRunLogsDTO logsDTO = new TektonTaskRunLogsDTO();

        MvcResult result = null;
        for (int i = 0; i < 20; i++) {
            Thread.sleep(2000);

            // When & Then
            result = mockMvc.perform(get("/tekton/taskRun/logs")
                .cookie(cookie))
                .andReturn();

            String json = result.getResponse().getContentAsString();
            logsDTO = objectMapper.readValue(json, TektonTaskRunLogsDTO.class);

            if (!logsDTO.getStatus().equals("Unknown")) {
                break; // Exit loop if logs are available
            }
        }

        assertThat(logsDTO)
            .extracting(TektonTaskRunLogsDTO::getStatus)
            .isEqualTo("True");

        assertThat(logsDTO)
            .extracting(TektonTaskRunLogsDTO::getReason)
            .isEqualTo("Succeeded");

        assertThat(logsDTO.getLogs())
            .contains("Running fosscut with parameters");
    }

    @Test
    @Order(4)
    void taskRunCreate_Failure() throws Exception {
        // Given
        Cookie cookieSettings = new Cookie(ApiDefaults.COOKIE_SETTINGS_IDENTIFIER, TestDefaults.getDefaultSettingsJson());

        // When & Then
        mockMvc.perform(post("/tekton/taskRun/create")
            .cookie(cookie)
            .cookie(cookieSettings))
            .andExpect(status().isConflict());
    }

    @Test
    @Order(5)
    void taskRunDelete_Success() throws Exception {
        // When & Then
        mockMvc.perform(post("/tekton/taskRun/delete")
            .cookie(cookie))
            .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void taskRunDelete_Failure() throws Exception {
        // When & Then
        mockMvc.perform(post("/tekton/taskRun/delete")
            .cookie(cookie))
            .andExpect(status().isConflict());
    }

}
