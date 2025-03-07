package com.fosscut.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fosscut.api.util.JsonPayload;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class Debug {

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(Debug.class);

    @GetMapping("/health")
    @ResponseBody
    public String reportHealthy() {
        return "OK";
    }

    @GetMapping("/time")
    @ResponseBody
    public String track() {
        String time = LocalDate.now().toString() + ":" + LocalTime.now();
        logger.info(time);
        return time;
    }

    @RequestMapping(value = "/echo",
        consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
        method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<JsonPayload> echoBack(@RequestBody(required = false) byte[] rawBody) {

        final Map<String, String> headers  = Collections.list(request.getHeaderNames()).stream()
            .collect(Collectors.toMap(Function.identity(), request::getHeader));

        final JsonPayload response = new JsonPayload();
        response.set(JsonPayload.PROTOCOL, request.getProtocol());
        response.set(JsonPayload.METHOD, request.getMethod());
        response.set(JsonPayload.HEADERS, headers);
        response.set(JsonPayload.COOKIES, request.getCookies());
        response.set(JsonPayload.PARAMETERS, request.getParameterMap());
        response.set(JsonPayload.PATH, request.getServletPath());
        response.set(JsonPayload.BODY, rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null);
        logger.info("REQUEST: {}", request.getParameterMap());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
