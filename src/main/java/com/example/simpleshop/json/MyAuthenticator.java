package com.example.simpleshop.json;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public  class MyAuthenticator extends Authenticator {


    protected PasswordAuthentication getPasswordAuthentication() {

        String username = "ZwnyZE";
        String password = "FEwjc1";
        return new PasswordAuthentication(username, password.toCharArray());

    }
    }