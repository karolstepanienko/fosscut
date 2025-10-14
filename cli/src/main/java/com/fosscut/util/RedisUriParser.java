package com.fosscut.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.type.RedisURI;

public class RedisUriParser {

    private static final Logger logger = LoggerFactory.getLogger(RedisUriParser.class);

    public static RedisURI getOrderUri(String path) {
        RedisURI orderUri = null;
        if (path != null && isURI(path)) {
            orderUri = getOrderUriFromPath(path);
        }
        return orderUri;
    }

    public static boolean isURI(String path) {
        if (getOrderUriFromPath(path) == null) return false;
        else {
            logger.info("URI recognised. Attempting parsing...");
            return true;
        }
    }

    private static RedisURI getOrderUriFromPath(String path) {
        URI uri = null;
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            logger.error(e.toString());
            logger.error(e.getLocalizedMessage());
            logger.info("Not a URI. Parsing file path...");
        }
        return new RedisURI(uri);
    }

}
