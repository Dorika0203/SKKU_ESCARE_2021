package com.example.demo.controller;

import com.example.demo.model.*;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.user.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/createaccount")
public class CreateAccount {

    @Autowired
    AccountDataRepository accountData;


    @GetMapping
    public String createaccount(Model model) {
       
        long Account = accountData.count();
        String id = LoginClient.getUserID();

        AccountDataModel account = new AccountDataModel(Account, id);
        accountData.saveAndFlush(account);
    
        return "account_create_success";
    }

}
