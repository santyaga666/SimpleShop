package com.example.simpleshop;

import com.example.simpleshop.json.MyAuthenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Authenticator;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        Authenticator.setDefault(new MyAuthenticator());

        System.setProperty("https.proxyHost", "193.228.55.76");
        System.setProperty("https.proxyPort", "8000");
        System.setProperty("https.proxyUser", "ZwnyZE");
        System.setProperty("https.proxyPassword", "FEwjc1");

        SpringApplication.run(Application.class, args);
    }
}
