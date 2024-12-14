package com.fosscut.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FosscutApiApplication {

    public static void main(String[] args) {
        new PropertiesLoader().run();
        SpringApplication.run(FosscutApiApplication.class, args);
    }

}
