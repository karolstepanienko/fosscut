package com.fosscut.util.load;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderFileLoader implements Loader {

    private static final Logger logger = LoggerFactory.getLogger(OrderFileLoader.class);

    @Override
    public void validate(String orderPath) {
        File orderFile = new File(orderPath);

        if (orderFile.isDirectory()) {
            logger.error("Order path points to a directory. Order can only be read from a file.");
            System.exit(1);
        }

        if (!orderFile.exists()) {
            logger.error("Failed to load order file, because it does not exist.");
            System.exit(1);
        }
    }

    @Override
    public String load(String orderPath) throws IOException {
        return Files.readString(Paths.get(orderPath));
    }

}
