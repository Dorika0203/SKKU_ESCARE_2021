package com.example.demo.controller;

import com.example.demo.model.*;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/signin")
public class SignIn {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private UserDataInterface dbInterface;

    @PostMapping
    public String connect(Model model, String ID_IN, String PW_IN) {

        System.out.println(ID_IN + " " + PW_IN);
        int flag = 0;
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
            System.out.println("SDKMS connect success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }

        // test : push userdata into DB.
        // UserDataModel temp = new UserDataModel("qwer@asdf.com", "zxcv");
        // dbInterface.save(temp);
        // dbInterface.flush();
        // System.out.printf("HI\n");

        // try Login.
        if(dbInterface.findById(ID_IN).isPresent()) {
            UserDataModel foundInfo = dbInterface.getById(ID_IN);
            // login success.
            if(foundInfo.getPw().equals(PW_IN)) return "sign_in_success";
            // password not match.
            else flag = 1;
        }
        // id does not exist.
        else flag = 2;

        switch (flag)
        {
            case 1:
                model.addAttribute("errorMessage", "Wrong Password");
                break;
            case 2:
                model.addAttribute("errorMessage", "No such ID");
                break;
            default:
                model.addAttribute("errorMessage", "FLAG VALUE IS WRONG!!");    
        }
        return "sign_in_fail";
    }
}
