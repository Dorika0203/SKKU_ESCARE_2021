package com.example.demo.controller;

import com.example.demo.model.*;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.example.demo.user.LoginClient;
import com.fortanix.sdkms.v1.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.demo.date.Time.parseTimestampFormatToUNIXTime;
import static com.example.demo.fortanix.FortanixRestApi.decryptAESCipherByFortanixSDKMS;
import static com.example.demo.fortanix.FortanixRestApi.generateAESCipherByFortanixSDKMS;
import static com.example.demo.user.LoginClient.getVerifiedFortanixClient;
import static com.example.demo.user.LoginClient.getUserID;

@Controller
@RequestMapping("/createaccount")
public class CreateAccount {

    @Autowired
    AccountDataRepository accountData;
    @Autowired
    SignInDataRepository signInDataRepository;
    @Autowired
    SignOutDataRepository signOutDataRepository;


    @GetMapping
    public String createaccount(Model model) {

        long Account = accountData.count();
        String id = LoginClient.getUserID();

        AccountDataModel account = new AccountDataModel(Account, id);
        accountData.saveAndFlush(account);
        String userID = getUserID();
        if (userID == null) {
            return "fail";
        }

        if (isClientLoginTimeLessThan5Minute(userID)) {
            return "account_create_success";
        } else return "my_page_fail";

    }

    public boolean isClientLoginTimeLessThan5Minute(String userID) {
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        SignInDataModel lastSignInDataModel = signInDataModelList.get(signInDataModelList.size() - 1);
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(signOutDataModelList.size() - 1);
        if (signInDataModelList.isEmpty()) {
            return false;
        } else if (signOutDataModelList.isEmpty()) {
            return true;
        } else {
            byte[] signInTimestampCipher = lastSignInDataModel.getSignIn_time();
            byte[] decryptedByteSignInTime = decryptAESCipherByFortanixSDKMS(signInTimestampCipher, getVerifiedFortanixClient());
            String signInTimestamp = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);
            int UNIXSignInTime = parseTimestampFormatToUNIXTime(signInTimestamp);

            byte[] signOutCipher = lastSignOutDataModel.getSignOut_time();
            byte[] decryptedByteSignOutTime = decryptAESCipherByFortanixSDKMS(signOutCipher, getVerifiedFortanixClient());
            String signOutTimestamp = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);
            int UNIXSignOutTime = parseTimestampFormatToUNIXTime(signOutTimestamp);
            int signOutAndInTimeDiff = UNIXSignOutTime - UNIXSignInTime;
            if (signOutAndInTimeDiff > 0)
                return false;
            int signInAndCurrentTimeDiff = (int) (System.currentTimeMillis() / 1000) - UNIXSignInTime;
            if (0 <= signInAndCurrentTimeDiff && signInAndCurrentTimeDiff <= 300) {
                return true;
            } else
                return false;
        }
    }
}