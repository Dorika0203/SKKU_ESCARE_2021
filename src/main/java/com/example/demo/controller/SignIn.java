package com.example.demo.controller;

import com.example.demo.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/signin")
public class SignIn {

//    @Autowired
//    private UserDataInterface dbInterface;
//
//    @GetMapping
//    public String connect() {
//
//        UserDataModel temp = new UserDataModel("id_1", "pw_1", " ", " ", " ");
//        System.out.printf("HI\n");
//        saveData(temp);
//        System.out.printf("HI2\n");
//        return "connected";
//    }
//
//    public void saveData(UserDataModel svdt) {
//        dbInterface.save(svdt);
//        dbInterface.flush();
//    }
}
