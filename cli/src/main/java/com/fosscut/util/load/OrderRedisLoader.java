package com.fosscut.util.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.shared.SharedDefaults;
import com.fosscut.type.OrderURI;
import com.fosscut.util.RedisClient;

import redis.clients.jedis.JedisPooled;

public class OrderRedisLoader extends Loader {

    private static final Logger logger = LoggerFactory.getLogger(OrderRedisLoader.class);

    private File redisConnectionSecretsFile;

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
            logger.error("Incorrect protocol. Must be 'redis'.");
            System.exit(1);
        }

        if (uri.getHost() == null) {
            logger.error("Error: Unable to read hostname.");
            System.exit(1);
        }

        if (uri.getPort() <= 0) {
            logger.error("Error: Unable to read port.");
            System.exit(1);
        }

        if (uri.getPath() == null) {
            logger.error("Error: Unable to read identifier.");
            System.exit(1);
        }
    }

    @Override
    public String load(String orderPath) {
        OrderURI orderUri = null;
        try {
            orderUri = new OrderURI(new URI(orderPath));
        } catch (URISyntaxException e) {}

        RedisClient redisClient = new RedisClient(redisConnectionSecretsFile);
        JedisPooled jedis = redisClient.getReadClient(orderUri.getHost(), orderUri.getPort());
        String orderString = jedis.get(
            SharedDefaults.REDIS_STRING_KEY_PREFIX
            + SharedDefaults.REDIS_STRING_ORDER_PREFIX
            + orderUri.getIdentifier()
        );
        jedis.close();
        return orderString;
    }

}
