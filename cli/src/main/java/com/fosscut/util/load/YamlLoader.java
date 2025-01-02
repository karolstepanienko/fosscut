package com.fosscut.util.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fosscut.type.cutting.order.Order;

public class YamlLoader {
    private boolean quietModeRequested;

    public YamlLoader(boolean quietModeRequested) {
        this.quietModeRequested = quietModeRequested;
    }

    public Order loadOrder(String orderString) {
        if(!quietModeRequested) System.out.println("Loading order...");

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Order order = new Order();
        try {
            order = yamlMapper.readValue(orderString, Order.class);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to load order file. Incorrect syntax.");
            System.err.println("Exception:");
            System.err.println(e.getClass().getCanonicalName());
            System.err.println(e.toString());
            System.exit(1);
        }

        if(!quietModeRequested) System.out.println("Order loaded.");
        return order;
    }
}
