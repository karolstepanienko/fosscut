package com.fosscut.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.fosscut.shared.SharedDefaults;
import com.fosscut.shared.exception.OrderValidationException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.util.Validator;
import com.fosscut.shared.util.load.YamlLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fosscut.api.type.OrderDTO;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/redis")
public class Redis {

    private static final Logger logger = LoggerFactory.getLogger(Redis.class);

    @Autowired
    private RedisTemplate<String, String> template;

    @GetMapping("/get/identifier")
    @ResponseBody
    public String getNewIdentifier() {
        String id = "";
        String value = "";
        do {
            id = Utils.generateRandomLowerCaseAlphanumericString(ApiDefaults.IDENTIFIER_LENGTH);
            value = template.opsForValue().get(
                SharedDefaults.REDIS_STRING_KEY_PREFIX
                + SharedDefaults.REDIS_STRING_ORDER_PREFIX
                + id
            );
        } while (value != null);  // key has to be free
        return id;
    }

    @PutMapping("/save/order")
    public void saveOrderToRedis(@RequestBody(required = true) OrderDTO orderDto, HttpServletResponse response) {
        try {
            YamlLoader yamlLoader = new YamlLoader();
            Order order = yamlLoader.loadOrder(orderDto.getOrder());

            Validator validator = new Validator();
            validator.validateOrder(order);

            String key = SharedDefaults.REDIS_STRING_KEY_PREFIX + SharedDefaults.REDIS_STRING_ORDER_PREFIX + orderDto.getIdentifier();
            template.opsForValue().set(key, orderDto.getOrder());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException | OrderValidationException e) {
            logger.error(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @GetMapping("/check/order/saved")
    @ResponseBody
    public boolean checkOrderSavedInRedis(@CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier) {
        String key = SharedDefaults.REDIS_STRING_KEY_PREFIX + SharedDefaults.REDIS_STRING_ORDER_PREFIX + identifier;
        String value = template.opsForValue().get(key);
        return value != null;
    }

    @GetMapping("/get/plan")
    @ResponseBody
    public String getPlanFromRedis(@CookieValue(ApiDefaults.COOKIE_IDENTIFIER) String identifier) {
        if (identifier == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "identifier is null");
        }

        String key = SharedDefaults.REDIS_STRING_KEY_PREFIX + SharedDefaults.REDIS_STRING_PLAN_PREFIX + identifier;
        String value = template.opsForValue().get(key);

        if (value == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "key not found");
        }

        return value;
    }

}
