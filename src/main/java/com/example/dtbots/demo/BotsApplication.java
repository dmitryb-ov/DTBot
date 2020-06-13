package com.example.dtbots.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableJpaRepositories("com.example.dtbots.demo.repositories")
@EnableConfigurationProperties
public class BotsApplication /*implements CommandLineRunner*/ {

    public static void main(String[] args) {
//        ApiContextInitializer.init();
        SpringApplication.run(BotsApplication.class, args);
    }
}
