package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.repository.AccountDataRepository;
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

        long account = 0;
        long transferAmount = 0;
        long messageTimestamp = 0;
        long currentTime = System.currentTimeMillis() / 1000;

        if (signatureVerified(transferData, publicKey, Base64.getDecoder().decode(signature))) {
            //check if all information are correct
            try {
                account = Integer.parseInt(transferDataArray[0]);
                transferAmount = Integer.parseInt(transferDataArray[1]);
                messageTimestamp = Integer.parseInt(transferDataArray[2]);
            } catch (NumberFormatException e) {
                //input format wrong
                return 1;
            }
            if (currentTime - messageTimestamp < 10 && currentTime - messageTimestamp >= 0) {
                if (accountDataRepository.existsById(account)) {
                    //check if account exists
                    AccountDataModel userAccount = accountDataRepository.findById(account).get();
                    if (userAccount.getBalance() > transferAmount) {
                        //transfer and edit balance
                        userAccount.setBalance(userAccount.getBalance() + transferAmount);
                        accountDataRepository.saveAndFlush(userAccount);
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
            return 4;
        } else {
            //wrong signature
            return 0;
        }
    }
}