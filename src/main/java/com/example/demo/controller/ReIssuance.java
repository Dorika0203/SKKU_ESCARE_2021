package com.example.demo.controller;

import com.example.demo.bank.Key;
import com.example.demo.fortanix.FortanixRestApi;
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

        // 세션 만료 시
        if(!isSessionAvailable(session)) return "fail";
        return "reissuance";
    }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model, HttpSession session) throws ApiException {

        // 세션 만료 시
        if(!isSessionAvailable(session)) return "fail";

        String userID = getSessionUserID(session);
        ApiClient client = getSessionApiClient(session);
        
        Key reissuedKey = new Key(userID, PW);
        KeyObject keyPair = FortanixRestApi.getSecurityObjectByID(client, userID);
        reissuedKey.setBase64PublicKey(keyPair);
        reissuedKey.setBase64PrivateKey(keyPair);
        sendUserPBEKeyAndPasswordToFrontend(model, reissuedKey);

        return "reissue_success";
    }

    public void sendUserPBEKeyAndPasswordToFrontend(Model model, Key key) {
        model.addAttribute("ID", key.getUserID());
        model.addAttribute("publicKey", key.getBase64PublicKey());
        model.addAttribute("privateKey", key.getBase64PrivateKey());
        model.addAttribute("password", key.getPassword());
    }

}
