package com.fosscut.api.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosscut.api.client.FosscutTektonClient;
import com.fosscut.api.type.Settings;
import com.fosscut.api.type.TektonTaskRunLogsDTO;
import com.fosscut.api.util.ApiDefaults;
import com.fosscut.shared.SharedDefaults;

@Controller
@RequestMapping("/tekton")
public class Tekton {

    @Autowired
    private FosscutTektonClient ftkn;

    @PostMapping("/taskRun/create")
    public void taskRunCreate(
        @CookieValue(SharedDefaults.COOKIE_IDENTIFIER) String identifier,
        @CookieValue(ApiDefaults.COOKIE_SETTINGS_IDENTIFIER) String settingsString,
        HttpServletResponse response
    ) {
        Settings settings = Settings.getSettingsSafe(settingsString, identifier);
        if (settings == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            if (ftkn.taskRunExists(identifier)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                ftkn.createTaskRun(identifier, settings);
            }
        }
    }

    @PostMapping("/taskRun/delete")
    public void taskRunDelete(
        @CookieValue(SharedDefaults.COOKIE_IDENTIFIER) String identifier,
        HttpServletResponse response
    ) {
        if (!ftkn.taskRunExists(identifier)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            ftkn.deleteTaskRun(identifier);
        }
    }

    @GetMapping("/taskRun/logs")
    @ResponseBody
    public TektonTaskRunLogsDTO getTaskRunLogs(
        @CookieValue(SharedDefaults.COOKIE_IDENTIFIER) String identifier,
        HttpServletResponse response
    ) {
        TektonTaskRunLogsDTO taskRunLogsDTO = null;
        if (!ftkn.taskRunExists(identifier)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            taskRunLogsDTO = ftkn.getTaskRunLogs(identifier);
        }
        return taskRunLogsDTO;
    }

}
