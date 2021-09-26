package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Admin {

    @RequestMapping(method = RequestMethod.GET, path = "/adminPage")
    public String adminHome(HttpSession session) {
        return "admin_login";
    }
}
