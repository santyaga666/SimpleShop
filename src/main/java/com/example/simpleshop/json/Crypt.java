package com.example.simpleshop.json;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Crypt {

    public static String generate(String str, byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        md.update(str.getBytes());
        byte[] digest = md.digest();
        String result = new BigInteger(1, digest).toString(16).toUpperCase();
        return result;
    }

}
