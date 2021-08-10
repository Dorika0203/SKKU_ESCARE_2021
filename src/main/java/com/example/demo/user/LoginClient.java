package com.example.demo.user;

import com.fortanix.sdkms.v1.ApiClient;

public class LoginClient {

    private static String userID = null;

    private static ApiClient client = null;

    public static void setUserID(String id) {
        userID = id;
    }

    public static String getUserID() {
        return userID;
    }

    public static ApiClient getVerifiedFortanixClient() { return client; }

    public static void setVerifiedFortanixClient(ApiClient client) { LoginClient.client = client; }

}