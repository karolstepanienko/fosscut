package com.fosscut.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.fosscut.type.cutting.order.Order;

public class YamlLoader {
    private boolean quietModeRequested;

    public YamlLoader(boolean quietModeRequested) {
        this.quietModeRequested = quietModeRequested;
    }

    public Order loadOrder(File orderFile) {
        if(!quietModeRequested) System.out.println("Loading order...");
        Yaml yaml = new Yaml(new Constructor(Order.class, new LoaderOptions()));
        Order order = new Order();
        try {
            InputStream orderStream = new FileInputStream(orderFile);
            order = yaml.load(orderStream);
        } catch (FileNotFoundException e) {
            System.err.println("Failed to load order file, because it does not exist.");
            System.exit(1);
        } catch (Exception e) {
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
