package com.fosscut.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosscut.api.client.FosscutAirflowClient;
import com.fosscut.api.type.AirflowDAGLogsDTO;
import com.fosscut.api.type.Settings;
import com.fosscut.api.util.ApiDefaults;
import com.fosscut.shared.SharedDefaults;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/airflow")
public class Airflow {

    @Autowired
    private FosscutAirflowClient fosscutAirflowClient;

    @PostMapping("/dag/run")
    @ResponseBody
    public String dagRun(
        @CookieValue(SharedDefaults.COOKIE_IDENTIFIER) String identifier,
        @CookieValue(ApiDefaults.COOKIE_SETTINGS_IDENTIFIER) String settingsString,
        HttpServletResponse response
    ) {
        Settings settings = Settings.getSettingsSafe(settingsString, identifier);
        if (settings == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "-1";
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return fosscutAirflowClient.runFosscutGenerateDAG(identifier, settings);
        }
    }

    @GetMapping("/dag/logs")
    @ResponseBody
    public AirflowDAGLogsDTO getDAGLogs(
        @CookieValue(ApiDefaults.COOKIE_DAG_RUN_ID_IDENTIFIER) String dagRunID
    ) {
        return fosscutAirflowClient.getDAGLogs(dagRunID);
    }

}
