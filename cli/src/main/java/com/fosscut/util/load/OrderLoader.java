package com.fosscut.util.load;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.OrderFileDoesNotExistException;
import com.fosscut.exception.OrderFileIsADirectoryException;
import com.fosscut.type.OrderURI;

public class OrderLoader {

    private static final Logger logger = LoggerFactory.getLogger(OrderLoader.class);
    private File redisConnectionSecretsFile;

    public OrderLoader(File redisConnectionSecretsFile) {
        this.redisConnectionSecretsFile = redisConnectionSecretsFile;
    }

    public String load(String orderPath)
        throws IOException, OrderFileIsADirectoryException,
        OrderFileDoesNotExistException
    {
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
            logger.info("URI recognised. Attempting parsing...");
            return true;
        }
    }

    private OrderURI getOrderUriFromPath(String orderPath) {
        URI uri = null;
        try {
            uri = new URI(orderPath);
        } catch (URISyntaxException e) {
            logger.error(e.toString());
            logger.error(e.getLocalizedMessage());
            logger.info("Not a URI. Parsing file path...");
        }
        return new OrderURI(uri);
    }

}
