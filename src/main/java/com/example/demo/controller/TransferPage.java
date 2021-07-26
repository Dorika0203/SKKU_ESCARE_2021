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
    public String transferpage(Model model) {
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
        String publicKey = (String) transferDataMap.get("publicKey");
        PublicKey publicKey1 = getPublicKeyFromBase64String(publicKey);
        System.out.println(transferData + "\n" + signature + "\n" + publicKey + "\n");

        signatureVerify(transferData, publicKey1, signature.getBytes(StandardCharsets.UTF_8));
        long account = 0;
        long transferAmount = 0;
        return 1;
//        //check if all information are correct
//        try{
//            //check input format
//            account = Integer.parseInt(accountString);
//            transferAmount = Integer.parseInt(transferAmountString);
//        } catch (NumberFormatException e) {
//            //input format wrong
//            return 1;
//        }
//        if(accountDataRepository.existsById(account)){
//            //check if account exists
//            AccountDataModel userAccount = accountDataRepository.findById(account).get();
//            if(userAccount.getBalance() > transferAmount){
//                //transfer and edit balance
//                userAccount.setBalance(userAccount.getBalance() + transferAmount);
//                accountDataRepository.saveAndFlush(userAccount);
//            } else {
//                //if balance is less than transfer amount
//                return 3;
//            }
//        } else {
//            //if account not exists
//            return 2;
//        }
//        //transfer success
//        return 4;
    }
}