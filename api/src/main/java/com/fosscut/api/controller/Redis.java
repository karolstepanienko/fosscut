package com.fosscut.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.fosscut.api.util.ApiDefaults;
import com.fosscut.api.util.Utils;
import com.fosscut.api.type.OrderDTO;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/redis")
public class Redis {

    @Autowired
    private RedisTemplate<String, String> template;

    @GetMapping("/get/identifier")
    @ResponseBody
    public String getNewIdentifier() {
        String id = "";
        String value = "";
        do {
            id = Utils.generateRandomLowerCaseAlphanumericString(8);
            value = template.opsForValue().get(
                ApiDefaults.REDIS_STRING_KEY_PREFIX
                + ApiDefaults.REDIS_STRING_ORDER_PREFIX
                + id
            );
        } while (value != null);  // key has to be free
        return id;
    }

    @PutMapping("/save/order")
    public void saveOrderToRedis(@RequestBody(required = true) OrderDTO orderDto, HttpServletResponse response) {
        String key = ApiDefaults.REDIS_STRING_KEY_PREFIX + ApiDefaults.REDIS_STRING_ORDER_PREFIX + orderDto.getIdentifier();
        template.opsForValue().set(key, orderDto.getOrder());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @GetMapping("/check/order/saved")
    @ResponseBody
    public boolean checkOrderSavedInRedis(@CookieValue("fosscut_orderIdentifier") String identifier) {
        String key = ApiDefaults.REDIS_STRING_KEY_PREFIX + ApiDefaults.REDIS_STRING_ORDER_PREFIX + identifier;
        String value = template.opsForValue().get(key);
        return value != null;
    }

    @GetMapping("/get/plan")
    @ResponseBody
    public String getPlanFromRedis(@CookieValue("fosscut_orderIdentifier") String identifier) {
        if (identifier == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "identifier is null");
        }

        String key = ApiDefaults.REDIS_STRING_KEY_PREFIX + ApiDefaults.REDIS_STRING_PLAN_PREFIX + identifier;
        String value = template.opsForValue().get(key);

        if (value == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "key not found");
        }

        return value;
    }

}
