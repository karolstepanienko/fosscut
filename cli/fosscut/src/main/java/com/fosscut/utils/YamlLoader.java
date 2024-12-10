package com.fosscut.utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fosscut.type.cutting.order.Order;

public class YamlLoader {
    private boolean quietModeRequested;

    public YamlLoader(boolean quietModeRequested) {
        this.quietModeRequested = quietModeRequested;
    }

    public Order loadOrder(File orderFile) {
        if(!quietModeRequested) System.out.println("Loading order...");

        if (orderFile.isDirectory()) {
            System.err.println("Order path points to a directory. Order can only be read from a file.");
            System.exit(1);
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Order order = new Order();
        try {
            order = yamlMapper.readValue(orderFile, Order.class);
        } catch (StreamReadException | DatabindException e) {
            System.err.println("Failed to load order file. Incorrect syntax.");
            System.err.println("Exception:");
            System.err.println(e.getClass().getCanonicalName());
            System.err.println(e.toString());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Failed to load order file, because it does not exist.");
            System.exit(1);
        }

        if(!quietModeRequested) System.out.println("Order loaded.");
        return order;
    }
}
