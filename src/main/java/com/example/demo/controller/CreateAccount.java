package com.example.demo.controller;

import com.example.demo.model.*;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static com.example.demo.bank.LoginClient.*;

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
    public String createaccount(HttpSession session) {

        String userID = getSessionUserID(session);

        if (userID != null) {
            createUserAccountAndSaveToDataBase(userID);
            return "account_create_success";
        } else
            return "logout_page";
    }

    public void createUserAccountAndSaveToDataBase(String loginUserID) {
        AccountDataModel account = new AccountDataModel(accountDataRepository.count(), loginUserID);
        accountDataRepository.saveAndFlush(account);
    }
}