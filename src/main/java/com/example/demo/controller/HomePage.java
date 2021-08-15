package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.demo.bank.LoginClient.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("home")
public class HomePage
{ 
    @GetMapping
    public String hello(HttpSession session) {

        // if session cookie available -> redirect to mypage.
        if(isSessionAvailable(session)) return "redirect:/mypage";
        return "home_page"; 
    }
}