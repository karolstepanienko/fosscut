package com.fosscut.util.load;

import java.io.File;
import java.io.IOException;

import com.fosscut.exception.EmptyOrderException;
import com.fosscut.exception.OrderFileDoesNotExistException;
import com.fosscut.exception.OrderFileIsADirectoryException;
import com.fosscut.exception.RedisOrderPathException;
import com.fosscut.util.RedisUriParser;

public class OrderLoader {

    private File redisConnectionSecretsFile;

    public OrderLoader(File redisConnectionSecretsFile) {
        this.redisConnectionSecretsFile = redisConnectionSecretsFile;
    }

    public String load(String orderPath)
        throws IOException, OrderFileIsADirectoryException,
        OrderFileDoesNotExistException, RedisOrderPathException,
        EmptyOrderException
    {
        Loader loader;

        if (redisConnectionSecretsFile != null && RedisUriParser.isURI(orderPath)) {
            loader = new OrderRedisLoader(redisConnectionSecretsFile);
        } else {
            loader = new OrderFileLoader();
        }

        loader.validate(orderPath);
        String orderString = loader.load(orderPath);

        if (orderString == null || orderString.isEmpty()) {
            throw new EmptyOrderException(orderPath);
        }

        return orderString;
    }

}
