package com.fosscut.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fosscut.api.client.FosscutTektonClient;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.tekton.client.DefaultTektonClient;
import io.fabric8.tekton.client.TektonClient;

@SpringBootApplication
public class FosscutApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FosscutApiApplication.class, args);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean(destroyMethod = "close")
    public KubernetesClient k8sClient() {
        return new KubernetesClientBuilder().build();
    }

    @Bean(destroyMethod = "close")
    public TektonClient tektonClient() {
        return new DefaultTektonClient();
    }

    @Bean(destroyMethod = "close")
    public FosscutTektonClient fosscutTektonClient() {
        return new FosscutTektonClient();
    }

}
