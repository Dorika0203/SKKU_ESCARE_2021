package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.example.demo.fortanix.FortanixRestApi;
import com.example.demo.model.*;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.fortanix.sdkms.v1.*;
import com.fortanix.sdkms.v1.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

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
    @Autowired
    AccountDataRepository accountData;

    @PostMapping("/signup")
    public String signUp(String ID, String PW, String lastName, String firstName, String phoneNumber) {

        //set signing client's ID
        setSignUpID(ID);

        //change input PW string to byte
        byte[] byteArrPW = PW.getBytes();

        //create sdkms client
        client = generateFortanixSDKMSClientAndVerify(server, username, password);

        //hashing and encrypting pw
        byte[] cipher = null;

        cipher = generateAESCipherByFortanixSDKMS(sha256(byteArrPW), client);

        //Tokenize phoneNumber
        String B64Plain = Base64.getEncoder().encodeToString(phoneNumber.getBytes());

        String TokenizedPhoneNumber = new String(tokenEncryptByFortanixSDKMS(B64Plain,client));

        //Insert Data in DB
        UserDataModel userDataModel = new UserDataModel(ID, cipher, lastName, firstName, TokenizedPhoneNumber);

        if (hasDuplicate(ID))
            return "sign_up_fail";
        else {
            SUCCESS = true;
            userDataRepository.save(userDataModel);
            userDataRepository.flush();
        }

        if (SUCCESS) {

            // create account, then make certification.
//            long Account = accountData.count();
//            AccountDataModel account = new AccountDataModel(Account, ID);
//            accountData.saveAndFlush(account);
//            System.out.println("creating account successed.");

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
            FortanixRestApi.generateRSAKeyFromFortanixSDKMS(client, signUpID);
            KeyObject value = FortanixRestApi.getSecurityObjectByID(client, signUpID);
            byte[] pub = value.getPubKey();
            byte[] priv = value.getValue();//pkcs1 priv key

            String B64Pub = Base64.getEncoder().encodeToString(pub);
            String B64Priv = Base64.getEncoder().encodeToString(priv);
            UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);

            //add data to DB
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

}
