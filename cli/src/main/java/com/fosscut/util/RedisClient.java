package com.fosscut.util;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

public class RedisClient {
    private RedisConnectionSecrets redisConnectionSecrets;

    public RedisClient(File redisConnectionSecretsFile) {
        this.redisConnectionSecrets = loadRedisConnectionSecrets(redisConnectionSecretsFile);
    }

    public void run() {
        System.out.println("Hello from redis.");

        HostAndPort address = new HostAndPort(
            redisConnectionSecrets.getHostname(),
            redisConnectionSecrets.getPort()
        );

        JedisClientConfig config = DefaultJedisClientConfig.builder()
            .password(redisConnectionSecrets.getPassword())
            .build();

        JedisPooled jedis = new JedisPooled(address, config);

        String value = jedis.get("welp");
        Integer intValue = Integer.parseInt(value);
        intValue += 1;
        jedis.set("welp", intValue.toString());

        System.out.println(value);
        jedis.close();
    }

    RedisConnectionSecrets loadRedisConnectionSecrets(File redisConnectionSecretsFile) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        RedisConnectionSecrets redisConnectionSecrets = new RedisConnectionSecrets();
        try {
            redisConnectionSecrets = yamlMapper.readValue(
                redisConnectionSecretsFile,
             RedisConnectionSecrets.class);
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return redisConnectionSecrets;
    }
}

class RedisConnectionSecrets {
    private String hostname;
    private Integer port;
    private String password;

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }
}
