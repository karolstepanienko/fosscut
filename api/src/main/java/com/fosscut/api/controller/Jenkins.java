package com.fosscut.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosscut.api.client.FosscutJenkinsClient;
import com.fosscut.api.type.JenkinsJobLogsDTO;
import com.fosscut.api.type.Settings;
import com.fosscut.api.util.ApiDefaults;
import com.fosscut.shared.SharedDefaults;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/jenkins")
public class Jenkins {

    @Autowired
    private FosscutJenkinsClient fosscutJenkinsClient;

    @PostMapping("/job/run")
    @ResponseBody
    public String triggerJenkinsJob(
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
            return fosscutJenkinsClient.triggerJob(settings);
        }
    }

    @GetMapping("/job/logs")
    @ResponseBody
    public JenkinsJobLogsDTO getJenkinsJobLogs(
        @CookieValue(ApiDefaults.COOKIE_QUEUE_ITEM_IDENTIFIER) Integer queueItemIdentifier,
        @CookieValue(ApiDefaults.COOKIE_JOB_NUMBER_IDENTIFIER) Integer jobNumberIdentifier,
        HttpServletResponse response
    ) {
        JenkinsJobLogsDTO jenkinsJobLogsDTO = fosscutJenkinsClient.getJobLogs(queueItemIdentifier, jobNumberIdentifier);
        response.setStatus(jenkinsJobLogsDTO.getHttpStatusCode());
        return jenkinsJobLogsDTO;
    }

}
