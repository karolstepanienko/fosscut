package com.fosscut.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
import com.fosscut.api.type.JenkinsJobLogsDTO;
import com.fosscut.api.util.ApiDefaults;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class JenkinsTests {

    @Autowired
    private Jenkins controller;

    @Autowired
    private MockMvc mockMvc;

    public static String testQueueID = "";

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    @Order(2)
    void jobRun_Success() throws Exception {
        // When & Then
        MvcResult result = mockMvc.perform(post("/jenkins/job/run"))
            .andExpect(status().isOk()).andReturn();

        testQueueID = result.getResponse().getContentAsString();

        assertThat(testQueueID)
            .isNotNull()
            .isNotEmpty();

        assertThatCode(() -> Integer.parseInt(testQueueID))
            .doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    @SuppressWarnings("null")
    void jobLogs_Success() throws Exception {
        // Given
        Cookie cookieQueueItemID = new Cookie(ApiDefaults.COOKIE_QUEUE_ITEM_IDENTIFIER, testQueueID);
        Cookie cookieJobNumberID = new Cookie(ApiDefaults.COOKIE_JOB_NUMBER_IDENTIFIER, "");

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = null;
        for (int i = 0; i < 20; i++) {
            Thread.sleep(2000);
            // When & Then
            result = mockMvc.perform(get("/jenkins/job/logs")
                .cookie(cookieQueueItemID)
                .cookie(cookieJobNumberID))
                .andReturn();
            if (result.getResponse().getStatus() == HttpServletResponse.SC_OK) {
                break;
            }
        }

        String json = result.getResponse().getContentAsString();
        JenkinsJobLogsDTO logDTO = objectMapper.readValue(json, JenkinsJobLogsDTO.class);

        assertThat(logDTO).isNotNull();
        assertThat(logDTO.getHttpStatusCode()).isEqualTo(HttpServletResponse.SC_OK);
        assertThatCode(
            () -> Integer.parseInt(logDTO.getJobNumberIdentifier())
        ).doesNotThrowAnyException();
        assertThat(logDTO.getStatus()).isNull();
        assertThat(logDTO.getLogs()).contains("kind: \"Pod\"");
    }

}
