package com.example.demo.controller;

import com.example.demo.fortanix.fortanixRestApi;
import com.example.demo.user.LoginClient;
import com.example.demo.model.UserDataModel;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.security.RSA;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.model.KeyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import static com.example.demo.fortanix.fortanixRestApi.*;

@Controller
public class ReIssuance {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    UserDataRepository userDataRepository;

    private String signUpID = null;

    @GetMapping("reissuance")
    public String reissuance() {
        return "reissuance";
    }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model) throws ApiException {
        System.out.println(signUpID);
        signUpID = LoginClient.getUserID();
        System.out.println(signUpID);
        if (signUpID != null) {
            ApiClient client;
            client = createClient(server, username, password);
            KeyObject value = fortanixRestApi.getSecObj(client, signUpID);
            byte[] pub = value.getPubKey();
            byte[] priv = value.getValue();//pkcs1 priv key

            String B64Pub = Base64.getEncoder().encodeToString(pub);
            String B64Priv = Base64.getEncoder().encodeToString(priv);

            UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);

            model.addAttribute("ID", signUpID);
            model.addAttribute("publicKey", B64Pub);
            model.addAttribute("privateKey", B64Priv);
            model.addAttribute("password", PW);
            userDataRepository.saveAndFlush(saltBase64UpdatedModel);

            return "reissue_success";
        } else
            return "error";
    }
}
