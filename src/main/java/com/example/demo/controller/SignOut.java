package com.example.demo.controller;

import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.model.UserDataModel;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.api.EncryptionAndDecryptionApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.example.demo.user.LoginClient.getUserID;
import static com.example.demo.user.LoginClient.setUserID;

@Controller
@RequestMapping("logout")
public class SignOut
{
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";
    private String signUpID = null;

    @Autowired
    private SignOutDataRepository signOutDataRepository;

    @GetMapping
    public String connect() {

        int flag = 0;
        // connect to SDKMS
        ApiClient client = createClient(server, username, password);
        connectFortanixsdkms(client);

        long tmp = signOutDataRepository.count();
        int iTmp = Long.valueOf(tmp).intValue();
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateCipher(byteCurrentTime, client);
        String ID = getUserID();
        SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
        signOutDataRepository.saveAndFlush(signOutDataModel);
        setUserID(null);

        return "logout_page";
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

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

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