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
import static com.example.demo.date.Time.getCurrentTime;

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
            String accountNumber = generateAccountNumber();
            saveToDataBase(accountNumber, userID);
            return "account_create_success";
        } else
            return "fail";
    }

    /* Account Number
    xxxx-xxx-xxxx-xx

    First digit: 1398

    Second digit: current time ( year % 10 / (month * second) % 10 / (day * minute + hour) % 10 )

    Third digit: current time ( day(1) / hour(1) / minute(1) / second(2) )

    Forth digit: (account count % 100) */
    public String generateAccountNumber() {
        // Set First part of AccountNumber
        String accountNumberFirst = "1398";

        // To Make Second, Third part Get each date and time
        String currentTime = getCurrentTime();

        // Get year, month, day
        String date = currentTime.split(" ")[0];
        int year, month, day;
        year = Integer.parseInt(date.split("-")[0]);
        month = Integer.parseInt(date.split("-")[1]);
        day = Integer.parseInt(date.split("-")[2]);

        // Get hour, minute, second
        String time = currentTime.split(" ")[1];
        int hour, minute, second;
        hour = Integer.parseInt(time.split(":")[0]);
        minute = Integer.parseInt(time.split(":")[1]);
        second = Integer.parseInt(time.split(":")[2]);

        // Set Second part of AccountNumber
        String accountNumberSecond;
        accountNumberSecond = Integer.toString((year % 10)) + Integer.toString((month * second) % 10) + Integer.toString((day * minute + hour) % 10);

        // Set Third part of AccountNumber
        String accountNumberThird;
        accountNumberThird = Integer.toString(day % 10) + Integer.toString(month % 10) + Integer.toString(second % 10);

        // Set Forth part of AccountNumber
        String accountNumberForth;
        int count = (int)accountDataRepository.count();
        if(count % 100 < 10) {
            accountNumberForth = "0" + Integer.toString(count % 100);
        }
        else {
            accountNumberForth = Integer.toString(count % 100);
        }

        // Generate AccountNumber
        String accountNumber = accountNumberFirst + "-" + accountNumberSecond + "-" + accountNumberThird + "-" + accountNumberForth;

        return accountNumber;
    }

    public void saveToDataBase(String accountNumber, String signInUserID) {
        AccountDataModel account = new AccountDataModel(accountNumber, signInUserID);
        accountDataRepository.saveAndFlush(account);
    }
}