package com.example.demo.controller;

import com.example.demo.date.Time;
import com.example.demo.model.*;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.example.demo.bank.LoginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.bank.LoginClient.getUserID;
import static com.example.demo.bank.LoginClient.isLogin;

@Controller
@RequestMapping("/createaccount")
public class CreateAccount {

    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    SignInDataRepository signInDataRepository;
    @Autowired
    SignOutDataRepository signOutDataRepository;


    @GetMapping
    public String createaccount() {

        Time time = new Time(signInDataRepository, signOutDataRepository);

        if (isLogin()) {
            createUserAccountAndSaveToDataBase(getUserID());;
        } else
            return "fail";

        if (time.isClientLoginTimeLessThan5Minute(getUserID())) {
            return "account_create_success";
        } else return "my_page_fail";

    }

    public void createUserAccountAndSaveToDataBase(String loginUserID) {
        AccountDataModel account = new AccountDataModel(accountDataRepository.count(), loginUserID);
        accountDataRepository.saveAndFlush(account);
    }
}