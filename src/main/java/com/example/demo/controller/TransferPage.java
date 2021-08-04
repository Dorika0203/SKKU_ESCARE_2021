package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.model.BankStatementDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
import com.example.demo.user.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.example.demo.security.RSA.*;

@Controller
@RequestMapping("transferpage")
public class TransferPage {
    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    BankStatementDataRepository bankStatementDataRepository;

    @GetMapping
    public String transferPage(Model model) {
        //add ID to model
        model.addAttribute("loginClientID", LoginClient.getUserID());
        return "transfer_page";
    }

    //needed to return value to ajax
    @ResponseBody
    @PostMapping("/transfer")
    public int transfer(@RequestParam Map<String, Object> transferDataMap) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeyException, SignatureException {
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
            BankStatementDataModel transferLog = new BankStatementDataModel(count, currentTime, transferAmount, afterBalance, senderAccount);
            bankStatementDataRepository.saveAndFlush(transferLog);
            return 4;
        } else {
            //wrong signature
            return 0;
        }
    }
}