package com.fosscut.util.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.fosscut.exception.RedisOrderPathException;
import com.fosscut.shared.SharedDefaults;
import com.fosscut.type.OrderURI;
import com.fosscut.util.Messages;
import com.fosscut.util.RedisClient;

import redis.clients.jedis.JedisPooled;

public class OrderRedisLoader implements Loader {

    private File redisConnectionSecretsFile;

    public OrderRedisLoader(File redisConnectionSecretsFile) {
        this.redisConnectionSecretsFile = redisConnectionSecretsFile;
    }

    @Override
    public void validate(String orderPath) throws RedisOrderPathException {
        URI uri = null;
        try {
            uri = new URI(orderPath);
        } catch (URISyntaxException e) {}

        if (uri.getScheme() == null || !uri.getScheme().equals( "redis"))
            throw new RedisOrderPathException(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_PROTOCOL_EXCEPTION);

        if (uri.getHost() == null)
            throw new RedisOrderPathException(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_HOSTNAME_EXCEPTION);

        if (uri.getPort() <= 0)
            throw new RedisOrderPathException(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_PORT_EXCEPTION);

        if (uri.getPath() == null || uri.getPath().equals("/") || uri.getPath().equals(""))
            throw new RedisOrderPathException(Messages.REDIS_ORDER_PATH_ERROR + Messages.REDIS_ORDER_PATH_IDENTIFIER_EXCEPTION);
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
