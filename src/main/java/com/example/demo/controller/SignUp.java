package com.example.demo.controller;

import java.security.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import com.example.demo.fortanix.fortanixRestApi;
import com.example.demo.model.*;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.security.RSA;
import com.fortanix.sdkms.v1.*;
import com.fortanix.sdkms.v1.api.*;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.demo.fortanix.fortanixRestApi.createClient;
import static com.example.demo.security.RSA.genFortanixPBEKeyAndSalt;
import static com.example.demo.security.RSA.getPKCS8KeyFromPKCS1Key;

@Controller
public class SignUp {
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";
    private String signUpID = null;
    private ApiClient client = new ApiClient();
    public boolean SUCCESS = false;

    @Autowired
    private UserDataRepository userDataRepository;

    @PostMapping("/signup")
    public String signUp(String ID, String PW, String lastName, String firstName, String phoneNumber) {

        //set signing client's ID
        setSignUpID(ID);

        //change input PW string to byte
        byte[] byteArrPW = PW.getBytes();

        //create sdkms client
        client = createClient(server, username, password);
        connectFortanixsdkms(client);

        //hashing and encrypting pw
        byte[] cipher = null;

        try {
            cipher = generatedCipher(sha256(byteArrPW), client);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Insert Data in DB
        UserDataModel userDataModel = new UserDataModel(ID, cipher, lastName, firstName, phoneNumber);

        if (hasDuplicate(ID))
            return "sign_up_fail";
        else {
            SUCCESS = true;
            userDataRepository.save(userDataModel);
            userDataRepository.flush();
        }

        if (SUCCESS) {
            return "certification";
        } else
            return "sign_up_fail";
    }

    @PostMapping("certificate")
    public String certificate(String PW, Model model) throws Exception {
        //check DB if this user can receive certificate
        if (userDataRepository.existsById(signUpID)) {
            UserDataModel issuedTimeUpdatedModel = userDataRepository.getById(signUpID);
            issuedTimeUpdatedModel.setIssued_time(getCurrentTime());
            userDataRepository.saveAndFlush(issuedTimeUpdatedModel);

            //get RSA key pair from sdkms
            fortanixRestApi.genRSAKeyFromFortanixSDKMS(client, signUpID);
            KeyObject value = fortanixRestApi.getSecObj(client, signUpID);
            byte[] pub = value.getPubKey();
            byte[] priv = value.getValue();//pkcs1 priv key

            String B64Pub = Base64.getEncoder().encodeToString(pub);
            String B64Priv = Base64.getEncoder().encodeToString(priv);
            UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);

            //add data to DB
            saltBase64UpdatedModel.setSalt("0");
            model.addAttribute("ID", signUpID);
            model.addAttribute("publicKey", B64Pub);
            model.addAttribute("privateKey", B64Priv);
            model.addAttribute("password", PW);
            userDataRepository.saveAndFlush(saltBase64UpdatedModel);
            return "sign_up_success";
        } else
            return "error";
    }

    // class methods
    public Boolean hasDuplicate(String ID) {
        return userDataRepository.findById(ID).isPresent();
    }

    public byte[] generatedCipher(byte[] plain, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.alg(ObjectType.AES).plain(plain).mode(CryptMode.CBC).setIv(ivStr.getBytes());
        try {
            EncryptResponse encryptResponse = new EncryptionAndDecryptionApi(client)
                    .encrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", encryptRequest);
            return encryptResponse.getCipher();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] sha256(byte[] msg) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(msg);

        return md.digest();
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

    public void setSignUpID(String ID) {
        this.signUpID = ID;
    }

    //used only in demo testing
    public static void connectFortanixsdkms(ApiClient client) {
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth = (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }
}
