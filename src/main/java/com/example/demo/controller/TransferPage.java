package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.model.BankStatementDataModel;
import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.example.demo.user.LoginClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fortanix.sdkms.v1.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.example.demo.fortanix.fortanixRestApi.DecryptAESCipher;
import static com.example.demo.fortanix.fortanixRestApi.generateAESCipher;
import static com.example.demo.security.RSA.*;
import static com.example.demo.user.LoginClient.getClient;
import static com.example.demo.user.LoginClient.getUserID;

@Controller
@RequestMapping("transferpage")
public class TransferPage {

    @Autowired
    SignInDataRepository signInDataRepository;
    @Autowired
    SignOutDataRepository signOutDataRepository;
    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    BankStatementDataRepository bankStatementDataRepository;

    @GetMapping
    public String transferPage(Model model) {
        //add ID to model
        List<AccountDataModel> loginUserAccountList = accountDataRepository.findAllByUserId(LoginClient.getUserID());
        JSONArray myAccountsData = new JSONArray();

        for (int i = 0; i < loginUserAccountList.size(); i++) {

            AccountDataModel accountInfo = loginUserAccountList.get(i);
            JSONObject sendingData = new JSONObject();

            sendingData.put("accountID", accountInfo.getAccount());
            sendingData.put("balance", accountInfo.getBalance());

            myAccountsData.put(sendingData);
        }
        model.addAttribute("myAccountsData", myAccountsData.toString());
        model.addAttribute("loginClientID", LoginClient.getUserID());

        //SignInSession signInSession = new SignInSession();
        //signInSession.SessionChecker("transfer_page");

        //check if user is login
        String userID = getUserID();
        if (userID == null) {return "fail";}

        // =================================Get Sign Time==================================================
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        int idx = signInDataModelList.size();
        if (idx == 0) {return "fail";}
        SignInDataModel lastSignInDataModel = signInDataModelList.get(idx-1);

        byte[] signIn_cipher = lastSignInDataModel.getSignIn_time();
        byte[] decryptedByteSignInTime = DecryptAESCipher(signIn_cipher, getClient());

        String signIn_time = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);

        String signInDate; String signInTime;
        signInDate = signIn_time.split(" ")[0];
        signInTime = signIn_time.split(" ")[1];

        int signInYear; int signInMonth; int signInDay; int signInHour; int signInMinute; int signInSecond;
        signInYear = Integer.parseInt(signInDate.split("-")[0]);
        signInMonth = Integer.parseInt(signInDate.split("-")[1]);
        signInDay = Integer.parseInt(signInDate.split("-")[2]);

        signInHour = Integer.parseInt(signInTime.split(":")[0]);
        signInMinute = Integer.parseInt(signInTime.split(":")[1]);
        signInSecond = Integer.parseInt(signInTime.split(":")[2]);
        //================================================================================================
        //===================================Get SignOut Time=============================================
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        idx = signOutDataModelList.size();
        if (idx != 0) {
            SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(idx-1);

            byte[] signOutcipher = lastSignOutDataModel.getSignOut_time();
            byte[] decryptedByteSignOutTime = DecryptAESCipher(signOutcipher, getClient());

            String signOut_time = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);

            String signOutDate; String signOutTime;
            signOutDate = signOut_time.split(" ")[0];
            signOutTime = signOut_time.split(" ")[1];

            int signOutYear; int signOutMonth; int signOutDay; int signOutHour; int signOutMinute; int signOutSecond;
            signOutYear = Integer.parseInt(signOutDate.split("-")[0]);
            signOutMonth = Integer.parseInt(signOutDate.split("-")[1]);
            signOutDay = Integer.parseInt(signOutDate.split("-")[2]);

            signOutHour = Integer.parseInt(signOutTime.split(":")[0]);
            signOutMinute = Integer.parseInt(signOutTime.split(":")[1]);
            signOutSecond = Integer.parseInt(signOutTime.split(":")[2]);
            //====================================================================================================
            //================================Compare SignIn & SignOut Time=======================================
            int signYearDiff = signOutYear - signInYear; int signMonthDiff = signOutMonth - signInMonth; int signDayDiff = signOutDay - signInDay;
            int signHourDiff = signOutHour - signInHour; int signMinuteDiff = signOutMinute - signInMinute; int signSecondDiff = signOutSecond - signInSecond;

            int signDiff = (((((signYearDiff * 12 + signMonthDiff) * 31 + signDayDiff) * 24 + signHourDiff) * 60 + signMinuteDiff) * 60 + signSecondDiff);

            if (signDiff > 0)
                return "fail";
        }

        String current_time = getCurrentTime();

        String curDate; String curTime;
        curDate = current_time.split(" ")[0];
        curTime = current_time.split(" ")[1];

        int curYear; int curMonth; int curDay; int curHour; int curMinute; int curSecond;
        curYear = Integer.parseInt( curDate.split("-")[0]);
        curMonth = Integer.parseInt(curDate.split("-")[1]);
        curDay = Integer.parseInt(curDate.split("-")[2]);

        curHour = Integer.parseInt(curTime.split(":")[0]);
        curMinute = Integer.parseInt(curTime.split(":")[1]);
        curSecond = Integer.parseInt(curTime.split(":")[2]);

        int yearDiff = curYear - signInYear; int monthDiff = curMonth - signInMonth; int dayDiff = curDay - signInDay;
        int hourDiff = curHour - signInHour; int minuteDiff = curMinute - signInMinute; int secondDiff = curSecond - signInSecond;
        int diff = (((((yearDiff * 12 + monthDiff) * 31 + dayDiff) * 24 + hourDiff) * 60 + minuteDiff) * 60 + secondDiff);


        // session checked. show my_page
        if (0 <= diff && diff <= 300)
        {
            String ID_IN = getUserID();
            ApiClient client = getClient();
            long tmp = signInDataRepository.count();
            int iTmp = Long.valueOf(tmp).intValue();
            byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
            byte[] cipher = generateAESCipher(byteCurrentTime, client);
            SignInDataModel signInDataModel = new SignInDataModel(iTmp, ID_IN, cipher);
            signInDataRepository.saveAndFlush(signInDataModel);
            LoginClient.setUserID(ID_IN);
            return "transfer_page";
        }
        else
            return "fail";
    }

    //needed to return value to ajax
    @ResponseBody
    @PostMapping("/transfer")
    public int transfer(@RequestParam Map<String, Object> transferDataMap) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeyException, SignatureException {

        String ID_IN = getUserID();
        ApiClient client = getClient();
        long tmp = signInDataRepository.count();
        int iTmp = Long.valueOf(tmp).intValue();
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateAESCipher(byteCurrentTime, client);
        SignInDataModel signInDataModel = new SignInDataModel(iTmp, ID_IN, cipher);
        signInDataRepository.saveAndFlush(signInDataModel);
        LoginClient.setUserID(ID_IN);

        String transferData = (String) transferDataMap.get("transferData");
        String signature = (String) transferDataMap.get("signature");
        String base64PublicKey = (String) transferDataMap.get("publicKey");
        PublicKey publicKey = getPublicKeyFromBase64String(base64PublicKey);
        String[] transferDataArray = transferData.split("\\s");

        long receiverAccount = 0;
        long senderAccount = 0;
        long transferAmount = 0;
        long afterBalance = 0;
        long messageTimestamp = 0;
        long currentTime = System.currentTimeMillis() / 1000;

        if (signatureVerified(transferData, publicKey, Base64.getDecoder().decode(signature))) {
            //check if all information are correct
            try {
                receiverAccount = Integer.parseInt(transferDataArray[0]);
                senderAccount = Integer.parseInt(transferDataArray[1]);
                transferAmount = Integer.parseInt(transferDataArray[2]);
                messageTimestamp = Integer.parseInt(transferDataArray[3]);
            } catch (NumberFormatException e) {
                //input format wrong
                return 1;
            }
            if (currentTime - messageTimestamp < 10 && currentTime - messageTimestamp >= 0) {
                if (accountDataRepository.existsById(receiverAccount) && accountDataRepository.existsById(senderAccount)) {
                    //check if account exists
                    AccountDataModel senderUserAccount = accountDataRepository.findById(senderAccount).get();
                    AccountDataModel recieverUserAccount = accountDataRepository.findById(receiverAccount).get();
                    if (senderUserAccount.getBalance() >= transferAmount) {
                        //transfer and edit balance
                        afterBalance = recieverUserAccount.getBalance() + transferAmount;
                        recieverUserAccount.setBalance(afterBalance);
                        senderUserAccount.setBalance(senderUserAccount.getBalance() - transferAmount);
                        accountDataRepository.saveAndFlush(senderUserAccount);
                        accountDataRepository.saveAndFlush(recieverUserAccount);
                    } else {
                        //if balance is less than transfer amount
                        return 3;
                    }
                } else {
                    //if account not exists
                    return 2;
                }
            } else {
                return 0;
            }
            //transfer success
            long count = bankStatementDataRepository.count();
            BankStatementDataModel transferLog = new BankStatementDataModel(count, senderAccount, currentTime, transferAmount, afterBalance, receiverAccount);
            bankStatementDataRepository.saveAndFlush(transferLog);
            return 4;
        } else {
            //wrong signature
            return 0;
        }
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }
}