package com.fosscut.util.load;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderFileLoader extends Loader {

    @Override
    public void validate(String orderPath) {
        File orderFile = new File(orderPath);

        if (orderFile.isDirectory()) {
            System.err.println("Order path points to a directory. Order can only be read from a file.");
            System.exit(1);
        }

        if (!orderFile.exists()) {
            System.err.println("Failed to load order file, because it does not exist.");
            System.exit(1);
        }
    }

    @Override
    public String load(String orderPath) {
        String orderString = null;

        try {
            orderString = Files.readString(Paths.get(orderPath));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        return orderString;
    }

}
