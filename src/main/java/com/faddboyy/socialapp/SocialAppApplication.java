package com.faddboyy.socialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // [TAMBAHKAN INI]

@SpringBootApplication
@EnableScheduling // [TAMBAHKAN INI]
public class SocialAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialAppApplication.class, args);
    }

}