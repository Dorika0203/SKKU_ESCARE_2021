package com.example.demo.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.example.demo.model.*;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.fortanix.sdkms.v1.*;
import com.fortanix.sdkms.v1.api.*;
import com.fortanix.sdkms.v1.model.*;
import com.fortanix.sdkms.v1.auth.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.user.LoginClient.setUserID;


@Controller
@RequestMapping("/signin")
public class SignIn {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";
    private String signUpID = null;

    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private SignInDataRepository signInDataRepository;

    @PostMapping
    public String connect(Model model, String ID_IN, String PW_IN) {

        System.out.println(ID_IN + " " + PW_IN);
        int flag = 0;
        // connect to SDKMS
        ApiClient client = createClient(server, username, password);
        connectFortanixsdkms(client);

        // try Login.
        if(userDataRepository.findById(ID_IN).isPresent()) {
            UserDataModel userDataModel = userDataRepository.getById(ID_IN);
            // login success.
            byte[] hex = userDataModel.getPw();

            try {
                if(Arrays.equals(sha256(PW_IN), DecryptCipher((hex), client)))
                {
                    long tmp = signInDataRepository.count();
                    int iTmp = Long.valueOf(tmp).intValue();
                    byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
                    byte[] cipher = generateCipher(byteCurrentTime, client);
                    SignInDataModel signInDataModel = new SignInDataModel(iTmp, ID_IN, cipher);
                    signInDataRepository.saveAndFlush(signInDataModel);
                    setUserID(ID_IN);
                    return "sign_in_success";
                }
                else flag = 1;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
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

    public byte[] generateCipher(byte[] plain, ApiClient client) {
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

    public byte[] DecryptCipher(byte[] cipher, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.alg(ObjectType.AES).cipher(cipher).mode(CryptMode.CBC).iv(ivStr.getBytes());
        try {
            DecryptResponse decryptResponse = new EncryptionAndDecryptionApi(client).decrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", decryptRequest);
            return decryptResponse.getPlain();
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
            System.out.println("===================SHA-256 ERROR======================");
            return null;
        }
        md.update(msg.getBytes());
        return md.digest();
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

//    public byte[] hexToBytes(String hex) {
//        byte[] temp = new BigInteger(hex, 16).toByteArray();
//        //byte[] retval = Arrays.copyOfRange(temp, 1, temp.length);
//        for(int i=0; i<temp.length; i++) System.out.printf("%d: %d\n", i, temp[i]);
//        return temp;
//    }

    // connect to SDKMS
    public ApiClient createClient(String server, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        return client;
    }

    public void connectFortanixsdkms(ApiClient client) {
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
