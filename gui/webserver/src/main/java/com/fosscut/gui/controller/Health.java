package com.fosscut.gui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Health {

    @GetMapping("/health")
    @ResponseBody
    public String reportHealthy() {
        return "OK";
    }

}
