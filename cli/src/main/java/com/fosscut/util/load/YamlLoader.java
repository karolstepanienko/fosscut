package com.fosscut.util.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fosscut.type.cutting.order.Order;

public class YamlLoader {

    private static final Logger logger = LoggerFactory.getLogger(YamlLoader.class);

    public Order loadOrder(String orderString) {
        logger.info("Loading order...");

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Order order = new Order();
        try {
            order = yamlMapper.readValue(orderString, Order.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to load order file. Incorrect syntax.");
            logger.error("Exception:");
            logger.error(e.getClass().getCanonicalName());
            logger.error(e.toString());
            System.exit(1);
        }

        logger.info("Order loaded.");
        return order;
    }

}
