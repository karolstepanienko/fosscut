package com.fosscut.util.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.fosscut.util.RedisClient;

import redis.clients.jedis.JedisPooled;

public class OrderRedisLoader extends Loader {

    private File redisConnectionSecretsFile;

    private static final String REDIS_STRING_KEY_PREFIX = "fosscut:";
    private static final String REDIS_STRING_ORDER_PREFIX = "order:";

    public OrderRedisLoader(File redisConnectionSecretsFile) {
        this.redisConnectionSecretsFile = redisConnectionSecretsFile;
    }

    @Override
    public void validate(String orderPath) {
        URI uri = null;
        try {
            uri = new URI(orderPath);
        } catch (URISyntaxException e) {}

        if (uri.getScheme() == null || !uri.getScheme().equals( "redis")) {
            System.err.println("Incorrect protocol. Must be 'redis'.");
            System.exit(1);
        }

        if (uri.getHost() == null) {
            System.err.println("Error: Unable to read hostname.");
            System.exit(1);
        }

        if (uri.getPort() <= 0) {
            System.err.println("Error: Unable to read port.");
            System.exit(1);
        }

        if (uri.getPath() == null) {
            System.err.println("Error: Unable to read identifier.");
            System.exit(1);
        }
    }

    @Override
    public String load(String orderPath) {
        URI uri = null;
        try {
            uri = new URI(orderPath);
        } catch (URISyntaxException e) {}

        RedisClient redisClient = new RedisClient(redisConnectionSecretsFile);
        JedisPooled jedis = redisClient.getClient(uri.getHost(), uri.getPort());
        String orderString = jedis.get(REDIS_STRING_KEY_PREFIX + REDIS_STRING_ORDER_PREFIX + getIdentifier(uri));
        jedis.close();
        return orderString;
    }

    private String getIdentifier(URI uri) {
        // remove first character from string
        return uri.getPath().substring(1);
    }

}
