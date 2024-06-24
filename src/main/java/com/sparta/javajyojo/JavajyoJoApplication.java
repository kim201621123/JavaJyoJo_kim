package com.sparta.javajyojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JavajyoJoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavajyoJoApplication.class, args);
    }

}
