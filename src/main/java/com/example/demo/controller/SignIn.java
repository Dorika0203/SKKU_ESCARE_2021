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
import com.example.demo.user.LoginClient;
import com.fortanix.sdkms.v1.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.user.LoginClient.setVerifiedFortanixClient;

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

        int flag = 0;
        // connect to SDKMS
        ApiClient client = createFortanixSDKMSClientAndVerify(server, username, password);
        setVerifiedFortanixClient(client);

        // try Login.
        if(userDataRepository.findById(ID_IN).isPresent()) {
            UserDataModel userDataModel = userDataRepository.getById(ID_IN);
            // login success.
            byte[] AESEncryptedPassword = userDataModel.getPw();

            try {
                if(Arrays.equals(sha256(PW_IN), decryptAESCipherByFortanixSDKMS((AESEncryptedPassword), client)))
                {
                    byte[] encryptedTimestamp = createAESEncryptedTimestampByFortanixSDKMS(client);
                    saveLoginClientInfo(ID_IN, encryptedTimestamp);
                    LoginClient.setUserID(ID_IN);
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

    public void saveLoginClientInfo(String loginClientID, byte[] encryptedTimestamp) {
        long count = signInDataRepository.count();
        SignInDataModel signInDataModel = new SignInDataModel((int) count, loginClientID, encryptedTimestamp);
        signInDataRepository.saveAndFlush(signInDataModel);
    }

    public byte[] createAESEncryptedTimestampByFortanixSDKMS(ApiClient client){
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] EncryptedTimestamp = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        return EncryptedTimestamp;
    }

}
