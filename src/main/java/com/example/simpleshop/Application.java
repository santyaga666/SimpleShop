package com.example.simpleshop;

import com.example.simpleshop.json.MyAuthenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Authenticator;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        

        SpringApplication.run(Application.class, args);
    }
}
