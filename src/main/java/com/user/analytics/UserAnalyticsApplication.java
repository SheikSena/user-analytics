package com.user.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAnalyticsApplication.class, args);
    }

}
