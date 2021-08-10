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
        if (userID == null) {return "fail";}

        // =================================Get Sign Time==================================================
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        int signInLogIndex = signInDataModelList.size();
        if (signInLogIndex == 0) {
            return "my_page_fail";
        }
        SignInDataModel lastSignInDataModel = signInDataModelList.get(signInLogIndex - 1);

        byte[] signInTimestampCipher = lastSignInDataModel.getSignIn_time();
        byte[] decryptedByteSignInTime = decryptAESCipherByFortanixSDKMS(signInTimestampCipher, getVerifiedFortanixClient());

        String signInTimestamp = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);

        int UNIXSignInTime = parseTimestampFormatToUNIXTime(signInTimestamp);
        //================================================================================================
        //===================================Get SignOut Time=============================================
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        int signOutLogIndex = signOutDataModelList.size();
        if (signOutLogIndex == 0) {
            return "my_page";
        }
        SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(signOutLogIndex - 1);

        byte[] signOutCipher = lastSignOutDataModel.getSignOut_time();
        byte[] decryptedByteSignOutTime = decryptAESCipherByFortanixSDKMS(signOutCipher, getVerifiedFortanixClient());

        String signOutTimestamp = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);

        int UNIXSignOutTime = parseTimestampFormatToUNIXTime(signOutTimestamp);
        //====================================================================================================
        //================================Compare SignIn & SignOut Time=======================================

        int signOutAndInTimeDiff = UNIXSignOutTime - UNIXSignInTime;


        // session checked. show my_page
        if (0 <= signOutAndInTimeDiff && signOutAndInTimeDiff <= 300)
            return "account_create_success";
        else
            return "fail";
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }
}