package com.example.simpleshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty( "https.proxyPort", "34996" );
        System.setProperty( "https.proxyHost", "109.87.48.66" );
        SpringApplication.run(Application.class, args);
    }
}
