package com.example.demo.controller;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import com.example.demo.model.*;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.fortanix.sdkms.v1.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.bank.LoginClient.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

@Controller
@RequestMapping("/signin")
public class SignIn {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private SignInDataRepository signInDataRepository;

    @PostMapping
    public String connect(Model model, String ID_IN, String PW_IN, HttpSession session) {
        
        // if session cookie available -> redirect to mypage.
        if(isSessionAvailable(session)) return "redirect:/mypage";

        int FLAG = 0;
        ApiClient client = generateFortanixSDKMSClientAndVerify(server, username, password);
        setSessionApiClient(client, session);


        if(userDataRepository.findById(ID_IN).isPresent()) {
            UserDataModel userDataModel = userDataRepository.getById(ID_IN);

            byte[] AESEncryptedPassword = userDataModel.getPw();
            byte[] decryptedPassword = decryptAESCipherByFortanixSDKMS((AESEncryptedPassword), client);

            if(isEqual(decryptedPassword, sha256(PW_IN))) {
                saveLoginClientInfoToDatabase(ID_IN, client);
                session.setMaxInactiveInterval(60);
                setSessionUserID(ID_IN, session);
                return "sign_in_success";
            }
            else FLAG = 1;
        }
        // id does not exist.
        else FLAG = 2;

        switch (FLAG)
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

    public void saveLoginClientInfoToDatabase(String loginClientID, ApiClient client) {
        long count = signInDataRepository.count();
        byte[] encryptedTimestamp = generateAESEncryptedTimestampByFortanixSDKMS(client);
        SignInDataModel signInDataModel = new SignInDataModel((int) count, loginClientID, encryptedTimestamp);
        signInDataRepository.saveAndFlush(signInDataModel);
    }

    public final boolean isEqual(byte[] byteArray1, byte[] byteArray2) {
        return Arrays.equals(byteArray1,byteArray2);
    }
}
