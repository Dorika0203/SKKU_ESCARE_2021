package com.example.demo.controller;

import com.example.demo.date.Time;
import com.example.demo.fortanix.FortanixRestApi;
import com.example.demo.model.SignInDataModel;
import com.example.demo.repository.*;
import com.example.demo.bank.LoginClient;
import com.example.demo.model.UserDataModel;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.model.KeyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;

import java.util.Date;

import static com.example.demo.fortanix.FortanixRestApi.createAESEncryptedTimestampByFortanixSDKMS;
import static com.example.demo.fortanix.FortanixRestApi.generateAESCipherByFortanixSDKMS;
import static com.example.demo.bank.LoginClient.getVerifiedFortanixClient;
import static com.example.demo.bank.LoginClient.getUserID;

@Controller
public class ReIssuance {

    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    SignInDataRepository signInDataRepository;
    @Autowired
    SignOutDataRepository signOutDataRepository;
    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    BankStatementDataRepository bankStatementDataRepository;

    private String signUpID = null;

    @GetMapping("reissuance")
    public String reissuance() {

        Time time = new Time(signInDataRepository,signOutDataRepository);

        String userID = getUserID();
        if (userID == null) {
            return "fail";
        }

        if (time.isClientLoginTimeLessThan5Minute(userID)) {
            return "reissuance";
        } else
            return "my_page_fail";
    }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model) throws ApiException {

        String inputID = getUserID();
        ApiClient client = getVerifiedFortanixClient();
        byte[] encryptedTimestamp = createAESEncryptedTimestampByFortanixSDKMS(client);
        saveLoginClientInfo(inputID, encryptedTimestamp);

        signUpID = LoginClient.getUserID();

        if (signUpID != null) {

            KeyObject value = FortanixRestApi.getSecurityObjectByID(client, signUpID);
            byte[] pub = value.getPubKey();
            byte[] priv = value.getValue();//pkcs1 priv key

            String base64PublicKey = Base64.getEncoder().encodeToString(pub);
            String base64PrivateKey = Base64.getEncoder().encodeToString(priv);

            UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);

            model.addAttribute("ID", signUpID);
            model.addAttribute("publicKey", base64PublicKey);
            model.addAttribute("privateKey", base64PrivateKey);
            model.addAttribute("password", PW);
            userDataRepository.saveAndFlush(saltBase64UpdatedModel);

            return "reissue_success";
        } else
            return "error";
    }

    public void saveLoginClientInfo(String loginClientID, byte[] encryptedTimestamp) {
        long count = signInDataRepository.count();
        SignInDataModel signInDataModel = new SignInDataModel((int) count, loginClientID, encryptedTimestamp);
        signInDataRepository.saveAndFlush(signInDataModel);
    }

}
