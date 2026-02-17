package com.finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.finalproject")
@EnableJpaRepositories(basePackages = "com.finalproject.infrastructure.spring.persistence.jpa.repository")
@EntityScan(basePackages = "com.finalproject.infrastructure.spring.persistence.jpa.entity")
public class SpringApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringApiApplication.class, args);
    }
}
