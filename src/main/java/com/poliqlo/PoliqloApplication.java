package com.poliqlo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PoliqloApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoliqloApplication.class, args);
    }

}
