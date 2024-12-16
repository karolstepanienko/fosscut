package com.fosscut.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fosscut.gui.util.PropertiesLoader;

@SpringBootApplication
public class FosscutGuiApplication {

    public static void main(String[] args) {
        new PropertiesLoader().run();
        SpringApplication.run(FosscutGuiApplication.class, args);
    }

}
