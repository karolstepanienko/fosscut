package com.fosscut.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosscut.api.client.FosscutAirflowClient;
import com.fosscut.api.util.ApiDefaults;

@Controller
@RequestMapping("/airflow")
public class Airflow {

    @Autowired
    private FosscutAirflowClient fosscutAirflowClient;

    @PostMapping("/dag/run")
    @ResponseBody
    public String dagRun(
        @CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier
    ) {
        return fosscutAirflowClient.runFosscutGenerateDAG(identifier);
    }

    @GetMapping("/dag/logs")
    @ResponseBody
    public String getDAGLogs(
        @CookieValue(ApiDefaults.COOKIE_DAG_RUN_ID_IDENTIFIER) String dagRunID
    ) {
        return fosscutAirflowClient.getDAGLogs(dagRunID);
    }

}
