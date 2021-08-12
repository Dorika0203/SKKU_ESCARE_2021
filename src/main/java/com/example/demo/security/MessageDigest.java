package com.example.demo.security;

import java.security.NoSuchAlgorithmException;

public class MessageDigest {
    public byte[] sha256(String msg) throws NoSuchAlgorithmException {
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("===================SHA-256 ERROR======================");
            return null;
        }
        md.update(msg.getBytes());
        return md.digest();
    }
}
