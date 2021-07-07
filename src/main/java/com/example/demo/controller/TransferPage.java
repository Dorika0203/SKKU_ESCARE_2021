package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.user.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.user.LoginClient.getUserID;

@Controller
@RequestMapping("transferpage")
public class TransferPage
{
    @Autowired
    AccountDataRepository accountDataRepository;
    @GetMapping
    public String transferpage(Model model) {
        //add ID to model
        model.addAttribute("loginClientID", getUserID());
        return "transfer_page";
    }

    //needed to return value to ajax
    @ResponseBody
    @PostMapping("/transfer")
    public int transfer(@RequestParam Map<String, Object> transferDataMap) {
        String privateKey = (String) transferDataMap.get("privateKey");
        String publicKey = (String) transferDataMap.get("publicKey");
        String accountString = (String) transferDataMap.get("receiverAccount");
        String transactionAmountString = (String) transferDataMap.get("transactionAmount");
        long receiverAccount = 0;
        long transactionAmount = 0;

        //check if all information are correct
        try{
            //check input format
            receiverAccount = Integer.parseInt(accountString);
            transactionAmount = Integer.parseInt(transactionAmountString);
        } catch (NumberFormatException e) {
            //input format wrong
            return 1;
        }
        if (accountDataRepository.existsById(receiverAccount) && accountDataRepository.existsByUserId(getUserID())){
            //check whether each account exists
            AccountDataModel userAccountData = accountDataRepository.findByUserId(getUserID());
            AccountDataModel receiverAccountData = accountDataRepository.findById(receiverAccount).get();
            if (userAccountData.getBalance() >= transactionAmount){
                //transfer and edit balance
                receiverAccountData.setBalance(receiverAccountData.getBalance() + transactionAmount);
                userAccountData.setBalance(userAccountData.getBalance() - transactionAmount);
                accountDataRepository.saveAndFlush(receiverAccountData);
                accountDataRepository.saveAndFlush(userAccountData);
            } 
            else {
                //if balance is less than transfer amount
                return 3;
            }
        } 
        else {
            //if account not exists
            return 2;
        }
        //transfer success
        return 4;
    }
}