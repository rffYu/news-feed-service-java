package com.newsfeed.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.newsfeed.listener", "common"})
@EnableAsync
public class NewsfeedApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsfeedApplication.class, args);
    }
}
