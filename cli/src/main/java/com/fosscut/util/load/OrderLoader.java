package com.fosscut.util.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class OrderLoader {

    private File redisConnectionSecretsFile;
    private boolean quietModeRequested;

    public OrderLoader(File redisConnectionSecretsFile, boolean quietModeRequested) {
        this.redisConnectionSecretsFile = redisConnectionSecretsFile;
        this.quietModeRequested = quietModeRequested;
    }

    public String load(String orderPath) {
        Loader loader;

        if (redisConnectionSecretsFile != null && isURI(orderPath)) {
            loader = new OrderRedisLoader(redisConnectionSecretsFile);
        } else {
            loader = new OrderFileLoader();
        }

        loader.validate(orderPath);
        return loader.load(orderPath);
    }

    boolean isURI(String orderPath) {
        try {
            new URI(orderPath);
        } catch (URISyntaxException e) {
            System.err.println(e);
            System.err.println(e.getLocalizedMessage());
            if (!quietModeRequested) System.out.println("Not a URI. Parsing file path...");
            return false;
        }

        if (!quietModeRequested) System.out.println("URI recognised. Attempting parsing...");
        return true;
    }

}
