package com.example.demo.controller;

import com.example.demo.bank.Key;
import com.example.demo.fortanix.FortanixRestApi;
import com.example.demo.model.SignInDataModel;
import com.example.demo.repository.*;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.model.KeyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;
import static com.example.demo.bank.LoginClient.*;
import static com.example.demo.fortanix.FortanixRestApi.*;

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

    @GetMapping("reissuance")
    public String reissuance(HttpSession session) {

        String userID = getSessionUserID(session);

        if (userID == null) return "fail";
        return "reissuance";
    }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model, HttpSession session) throws ApiException {

        String userID = getSessionUserID(session);
        if (userID == null) return "fail";

        ApiClient client = getSessionApiClient(session);
        saveLoginClientInfoToDatabase(userID, client);
        Key reissuedKey = new Key(userID, PW);
        KeyObject keyPair = FortanixRestApi.getSecurityObjectByID(client, userID);
        reissuedKey.setBase64PublicKey(keyPair);
        reissuedKey.setBase64PrivateKey(keyPair);
        sendUserPBEKeyAndPasswordToFrontend(model, reissuedKey);

        return "reissue_success";
    }

    // change sign in database to active user database
    public void saveLoginClientInfoToDatabase(String loginClientID, ApiClient client) {
        long count = signInDataRepository.count();

        // ApiClient client = getVerifiedFortanixClient();
        byte[] encryptedTimestamp = generateAESEncryptedTimestampByFortanixSDKMS(client);

        SignInDataModel signInDataModel = new SignInDataModel((int) count, loginClientID, encryptedTimestamp);
        signInDataRepository.saveAndFlush(signInDataModel);
    }

    public void sendUserPBEKeyAndPasswordToFrontend(Model model, Key key) {
        model.addAttribute("ID", key.getUserID());
        model.addAttribute("publicKey", key.getBase64PublicKey());
        model.addAttribute("privateKey", key.getBase64PrivateKey());
        model.addAttribute("password", key.getPassword());
    }

}
