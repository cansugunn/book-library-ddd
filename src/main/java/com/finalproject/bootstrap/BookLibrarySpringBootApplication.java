package com.finalproject.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.finalproject")
public class BookLibrarySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookLibrarySpringBootApplication.class, args);
    }
}
