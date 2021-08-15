package com.example.demo.controller;

import com.example.demo.bank.Transfer;
import com.example.demo.model.AccountDataModel;
import com.example.demo.model.BankStatementDataModel;
import com.example.demo.model.SignInDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;

import static com.example.demo.date.Time.*;
import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.security.RSA.*;
import static com.example.demo.bank.LoginClient.*;

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
    public String transferPage(Model model, HttpSession session) {

        String userID = getSessionUserID(session);

        //add ID to model
        List<AccountDataModel> loginUserAccountList = accountDataRepository.findAllByUserId(userID);
        JSONArray myAccountsData = new JSONArray();

        for (int i = 0; i < loginUserAccountList.size(); i++) {

            AccountDataModel accountInfo = loginUserAccountList.get(i);
            JSONObject sendingData = new JSONObject();

            sendingData.put("accountID", accountInfo.getAccount());
            sendingData.put("balance", accountInfo.getBalance());

            myAccountsData.put(sendingData);
        }
        model.addAttribute("myAccountsData", myAccountsData.toString());
        model.addAttribute("loginClientID", userID);

        //check if user is login
        if (userID == null) return "fail";
        return "transfer_page";
    }

    //needed to return value to ajax
    @ResponseBody
    @PostMapping("/transfer")
    public int transfer(@RequestParam Map<String, Object> transferRequestMap, HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeyException, SignatureException {

        String ID_IN = getSessionUserID(session);
        ApiClient client = getSessionApiClient(session);
        int signInDataRepositoryCount = (int) signInDataRepository.count();
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] timestampCipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        SignInDataModel signInDataModel = new SignInDataModel(signInDataRepositoryCount, ID_IN, timestampCipher);
        signInDataRepository.saveAndFlush(signInDataModel);

        String transferRequest = (String) transferRequestMap.get("transferData");
        String signature = (String) transferRequestMap.get("signature");
        String base64PublicKey = (String) transferRequestMap.get("publicKey");
        PublicKey publicKey = getPublicKeyFromBase64String(base64PublicKey);

        Transfer transfer = new Transfer(accountDataRepository);
        long afterBalance = 0;
        long currentTime = System.currentTimeMillis() / 1000;

        if (isSignatureVerified(transferRequest, publicKey, Base64.getDecoder().decode(signature))) {
            //check if all information are correct
            try {
                transfer.setTransferInfoFromTransferRequest(transferRequest);
            } catch (NumberFormatException e) {
                //input format wrong
                return 1;
            }
            if (transfer.isRequestArrivedLessThan10Seconds()) {
                if (transfer.isAccountsExistInAccountDatabase()) {
                    //check if account exists
                    AccountDataModel senderUserAccount = accountDataRepository.findById(transfer.getSenderAccount()).get();
                    AccountDataModel receiverUserAccount = accountDataRepository.findById(transfer.getReceiverAccount()).get();
                    if (senderUserAccount.getBalance() >= transfer.getTransferAmount()) {
                        //transfer and edit balance
                        afterBalance = receiverUserAccount.getBalance() + transfer.getTransferAmount();
                        receiverUserAccount.setBalance(afterBalance);
                        senderUserAccount.setBalance(senderUserAccount.getBalance() - transfer.getTransferAmount());
                        accountDataRepository.saveAndFlush(senderUserAccount);
                        accountDataRepository.saveAndFlush(receiverUserAccount);
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
            long bankStatementDataRepositoryCount = bankStatementDataRepository.count();
            BankStatementDataModel transferLog = new BankStatementDataModel(bankStatementDataRepositoryCount, transfer.getSenderAccount(), currentTime, transfer.getTransferAmount(), afterBalance, transfer.getReceiverAccount());
            bankStatementDataRepository.saveAndFlush(transferLog);
            return 4;
        } else {
            //wrong signature
            return 0;
        }
    }

}