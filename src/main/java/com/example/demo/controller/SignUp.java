package com.example.demo.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.demo.model.*;
import com.fortanix.sdkms.v1.*;
import com.fortanix.sdkms.v1.api.*;
import com.fortanix.sdkms.v1.model.*;
import com.fortanix.sdkms.v1.auth.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUp {
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";
    public boolean SUCCESS = false;

    @Autowired
    UserDataInterface table;

    @PostMapping
    public String signUp(String ID, String PW, String lastName, String firstName, String phoneNumber) {
        System.out.println(ID + " " + PW + " " + lastName + " " + firstName + " " + phoneNumber);

        //connect to SDKMS
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

        //hashing and encrypting pw
        String cipher = null;

        try {
            cipher = bytesToHex(generatedCipher(sha256(PW), client));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println(cipher);
        UserDataModel user = new UserDataModel(ID, cipher, lastName, firstName, phoneNumber);

        if (hasDuplicate(ID)) {
            SUCCESS = true;
            table.save(user);
            table.flush();
        }

        if (SUCCESS) return "sign_up_success";
        else return "sign_up_fail";
    }

    public Boolean hasDuplicate(String ID) {
        return !table.findById(ID).isPresent();
    }

    public byte[] generatedCipher(byte[] plain, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest
                .alg(ObjectType.AES)
                .plain(plain)
                .mode(CryptMode.CBC).setIv(ivStr.getBytes());
        try {
            EncryptResponse encryptResponse = new EncryptionAndDecryptionApi(client).encrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", encryptRequest);
            System.out.println(encryptResponse.getCipher().length);
            for(int i=0; i<encryptResponse.getCipher().length; i++) System.out.printf("%d: %d\n", i, encryptResponse.getCipher()[i]);
            return encryptResponse.getCipher();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(msg.getBytes());

        return md.digest();
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
