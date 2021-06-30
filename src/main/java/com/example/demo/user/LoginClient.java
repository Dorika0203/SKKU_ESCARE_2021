package com.example.demo.user;

public class LoginClient {

    private static String userID = null;

    public static void setUserID(String id) {
        userID = id;
    }

    public static String getUserID() {
        return userID;
    }
}