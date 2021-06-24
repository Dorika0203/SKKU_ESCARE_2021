package com.example.demo.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.example.demo.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/createaccount")
public class CreateAccount {

    @Autowired
    AccountDataInterface table;

    @GetMapping
    public String createAccount(/*String AccountPW String ID, String lastname, String firstname*/) {
       
        long tmp = table.count();
        String Account = GenerateAccount(tmp);
        String id = "1234"; String lastname = "Kim"; String firstname = "Minseo";

        AccountDataModel account = new AccountDataModel(Account, id, lastname, firstname);
        table.save(account);
        table.flush();
    
        return "account_create_success";
    }

    public String GenerateAccount(long tmp) {
        return Long.toString(++tmp);
    }
}
