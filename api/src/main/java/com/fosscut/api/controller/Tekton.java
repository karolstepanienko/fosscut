package com.fosscut.api.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fosscut.api.client.FosscutTektonClient;
import com.fosscut.api.type.IdentifierDTO;
import com.fosscut.api.type.TaskRunLogsDTO;

@RestController
@RequestMapping("/tekton")
public class Tekton {

    @Autowired
    private FosscutTektonClient ftkn;

    @GetMapping("/taskRun/create")
    public void taskRunCreate(@RequestBody(required = true) IdentifierDTO identifierDTO, HttpServletResponse response) {
        if (ftkn.taskRunExists(identifierDTO.getIdentifier())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            ftkn.createTaskRun(identifierDTO.getIdentifier());
        }
    }

    @GetMapping("/taskRun/delete")
    public void taskRunDelete(@RequestBody(required = true) IdentifierDTO identifierDTO, HttpServletResponse response) {
        if (!ftkn.taskRunExists(identifierDTO.getIdentifier())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            ftkn.deleteTaskRun(identifierDTO.getIdentifier());
        }
    }

    @GetMapping("/taskRun/get/logs")
    @ResponseBody
    public TaskRunLogsDTO getTaskRunLogs(@RequestBody(required = true) IdentifierDTO identifierDTO, HttpServletResponse response) {
        TaskRunLogsDTO taskRunLogsDTO = null;
        if (!ftkn.taskRunExists(identifierDTO.getIdentifier())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            taskRunLogsDTO = ftkn.getTaskRunLogs(identifierDTO.getIdentifier());
        }
        return taskRunLogsDTO;
    }

}
