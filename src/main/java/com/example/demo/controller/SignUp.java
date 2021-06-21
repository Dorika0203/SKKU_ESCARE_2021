package com.example.demo.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.AuthResponse;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUp {
    private String server = "https://sdkms.fortanix.com";
    private String username = "02d20054-7a29-4511-9e73-f599da9f32a6";
    private String password = "4ztxSuaKpcIXtxiDYfgMxn0M2wyZaZSYnB_BqsOeswB_j7gGHqe5xGTqBvr-DjIZrFkrEJzgL5-YI8X1VExRjg";
    public final boolean SUCCESS = true;

    @PostMapping
    public String signUp(Model model,String ID, String PW, String lastName,String firstName,String phoneNumber) {
        System.out.println(ID + " " + PW + " " + lastName + " " + firstName + " " + phoneNumber);
        // open DB


        // connect to SDKMS
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
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }

        if(SUCCESS) return "sign_up_success";
        else return "sign_up_fail";
    }
    
}
