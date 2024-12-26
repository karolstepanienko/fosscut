package com.fosscut.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

import com.fosscut.api.util.PropertiesLoader;

@PropertySource("classpath:application.yml")
@SpringBootApplication
public class FosscutApiApplication {

    @Value("${corsUrl}")
    private String corsUrl;

    @Value("${corsMethods}")
    private String corsMethods;

    public static void main(String[] args) {
        new PropertiesLoader().run();
        SpringApplication.run(FosscutApiApplication.class, args);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (corsUrl != null) {
                    registry.addMapping("/**").allowedOrigins(corsUrl);
                }

                if (corsMethods != null) {
                    String[] methods = corsMethods.split(",");
                    for (String method : methods) {
                        registry.addMapping("/**").allowedMethods(method);
                    }
                }
            }
        };
    }
}
