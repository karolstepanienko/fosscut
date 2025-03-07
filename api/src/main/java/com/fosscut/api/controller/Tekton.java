package com.fosscut.api.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosscut.api.client.FosscutTektonClient;
import com.fosscut.api.type.TektonTaskRunLogsDTO;
import com.fosscut.api.util.ApiDefaults;

@Controller
@RequestMapping("/tekton")
public class Tekton {

    @Autowired
    private FosscutTektonClient ftkn;

    @GetMapping("/taskRun/create")
    public void taskRunCreate(
        @CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier,
        HttpServletResponse response
    ) {
        if (ftkn.taskRunExists(identifier)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            ftkn.createTaskRun(identifier);
        }
    }

    @GetMapping("/taskRun/delete")
    public void taskRunDelete(
        @CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier,
        HttpServletResponse response
    ) {
        if (!ftkn.taskRunExists(identifier)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            ftkn.deleteTaskRun(identifier);
        }
    }

    @GetMapping("/taskRun/get/logs")
    @ResponseBody
    public TektonTaskRunLogsDTO getTaskRunLogs(
        @CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier,
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
