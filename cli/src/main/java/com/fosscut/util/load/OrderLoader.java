package com.fosscut.util.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.fosscut.type.OrderURI;

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

    public OrderURI getOrderUri(String orderPath) {
        OrderURI orderUri = null;
        if (redisConnectionSecretsFile != null && isURI(orderPath)) {
            orderUri = getOrderUriFromPath(orderPath);
        }
        return orderUri;
    }

    private boolean isURI(String orderPath) {
        if (getOrderUriFromPath(orderPath) == null) return false;
        else {
            if (!quietModeRequested) System.out.println("URI recognised. Attempting parsing...");
            return true;
        }
    }

    private OrderURI getOrderUriFromPath(String orderPath) {
        URI uri = null;
        try {
            uri = new URI(orderPath);
        } catch (URISyntaxException e) {
            System.err.println(e);
            System.err.println(e.getLocalizedMessage());
            if (!quietModeRequested) System.out.println("Not a URI. Parsing file path...");
        }
        return new OrderURI(uri);
    }

}
