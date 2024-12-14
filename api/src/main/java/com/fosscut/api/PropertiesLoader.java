package com.fosscut.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesLoader {
    void run() {
        try {
            String propertiesFile = System.getProperty("properties.file");
            if (propertiesFile != null) {
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(propertiesFile).getAbsolutePath()));
                properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
            }
        } catch (Exception e) {
            System.err.println("Properties could not be loaded.");
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
}