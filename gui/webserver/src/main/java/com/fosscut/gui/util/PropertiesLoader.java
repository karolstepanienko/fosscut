package com.fosscut.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static final String DEFAULT_PROPERTIES_FILE_LOCATION = "/application.properties";

    public void run() {
        String propertiesFilePath = System.getProperty("properties.file");

        File propertiesFile;
        if (propertiesFilePath == null) {
            propertiesFile = new File(DEFAULT_PROPERTIES_FILE_LOCATION);
        } else {
            propertiesFile = new File(propertiesFilePath);
        }

        if (propertiesFilePath == null && !propertiesFile.exists()) {
            System.err.println("Skipping loading properties.");
        } else {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(propertiesFile.getAbsolutePath()));
                properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
            } catch (Exception e) {
                System.err.println("Properties could not be loaded.");
                System.err.println(e.getLocalizedMessage());
                System.exit(1);
            }
        }
    }
}
