package com.fosscut.util.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fosscut.type.cutting.order.Order;

public class YamlLoader {

    private static final Logger logger = LoggerFactory.getLogger(YamlLoader.class);

    public Order loadOrder(String orderString) throws JsonProcessingException {
        logger.info("Loading order...");
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Order order = new Order();
        order = yamlMapper.readValue(orderString, Order.class);
        logger.info("Order loaded.");
        return order;
    }

}
