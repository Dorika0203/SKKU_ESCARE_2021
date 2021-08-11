package com.example.demo.controller;

import com.example.demo.date.Time;
import com.example.demo.model.*;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.example.demo.user.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        Time time = new Time(signInDataRepository,signOutDataRepository);

        long Account = accountData.count();
        String id = LoginClient.getUserID();

        AccountDataModel account = new AccountDataModel(Account, id);
        accountData.saveAndFlush(account);
        String userID = getUserID();
        if (userID == null) {
            return "fail";
        }

        if (time.isClientLoginTimeLessThan5Minute(userID)) {
            return "account_create_success";
        } else return "my_page_fail";

    }

}