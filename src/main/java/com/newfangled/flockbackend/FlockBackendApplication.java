package com.newfangled.flockbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FlockBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlockBackendApplication.class, args);
    }

}
