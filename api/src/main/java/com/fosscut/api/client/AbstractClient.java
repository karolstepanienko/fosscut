package com.fosscut.api.client;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractClient {

    @Value("${redis.readHost}")
    protected String redisReadHost;

    @Value("${redis.readPort}")
    protected String redisReadPort;

}
