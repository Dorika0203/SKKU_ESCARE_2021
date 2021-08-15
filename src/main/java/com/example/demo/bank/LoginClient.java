package com.example.demo.bank;

import com.fortanix.sdkms.v1.ApiClient;

public class LoginClient {
    
    private static ApiClient client = null;

    public static ApiClient getVerifiedFortanixClient() { return client; }

    public static void setVerifiedFortanixClient(ApiClient client) { LoginClient.client = client; }

}