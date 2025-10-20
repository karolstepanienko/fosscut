package com.fosscut.shared.util.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fosscut.shared.type.cutting.order.Order;

public class YamlLoader {

    private static final Logger logger = LoggerFactory.getLogger(YamlLoader.class);

    public Order loadOrder(String orderString) throws JsonProcessingException {
        logger.info("Loading order...");
        Order order = loadClassFromYamlString(orderString, Order.class);
        logger.info("Order loaded.");
        return order;
    }

    public <T> T loadClassFromYamlString(String yamlString, Class<T> clazz)
        throws JsonProcessingException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        T object = yamlMapper.readValue(yamlString, clazz);
        return object;
    }

}
