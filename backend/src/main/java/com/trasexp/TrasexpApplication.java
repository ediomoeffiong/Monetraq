package com.trasexp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrasexpApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrasexpApplication.class, args);
    }

}