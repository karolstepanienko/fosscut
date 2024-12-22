package com.fosscut.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fosscut.api.util.PropertiesLoader;

@SpringBootApplication
public class FosscutApiApplication {

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    public static void main(String[] args) {
        new PropertiesLoader().run();
        SpringApplication.run(FosscutApiApplication.class, args);
    }

}
