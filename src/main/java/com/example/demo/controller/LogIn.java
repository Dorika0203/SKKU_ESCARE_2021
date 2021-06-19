package com.example.demo.controller;

import com.fortanix.sdkms.v1.*;
import com.fortanix.sdkms.v1.api.*;
import com.fortanix.sdkms.v1.auth.*;
import com.fortanix.sdkms.v1.model.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("connect")
public class LogIn {
    private String server = "https://sdkms.fortanix.com";
    private String username = "02d20054-7a29-4511-9e73-f599da9f32a6";
    private String password = "4ztxSuaKpcIXtxiDYfgMxn0M2wyZaZSYnB_BqsOeswB_j7gGHqe5xGTqBvr-DjIZrFkrEJzgL5-YI8X1VExRjg";
    
    
    @GetMapping
    public String connect() {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);

        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth =
                    (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("success");
            return "connected";
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
            System.exit(1);
            return "fail";
        }
    }
}
