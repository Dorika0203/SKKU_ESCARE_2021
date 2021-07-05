package com.example.demo.controller;

import com.example.demo.user.LoginClient;
import com.example.demo.model.UserDataModel;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.security.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Controller
public class ReIssuance {

    @Autowired
    UserDataRepository userDataRepository;

    private String signUpID = null;

    @GetMapping("reissuance")
    public String reissuance(){ return "reissuance"; }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model){
        System.out.println(signUpID);
        signUpID = LoginClient.getUserID();
        System.out.println(signUpID);
        if (signUpID!=null) {

            try {
                ArrayList<String> keyPair = RSA.genRSAKeyPair(PW);
                UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);
                saltBase64UpdatedModel.setSalt(keyPair.get(2));
                System.out.println(keyPair.get(0));
                System.out.println(keyPair.get(1));
                System.out.println(keyPair.get(2));
                model.addAttribute("ID", signUpID);
                model.addAttribute("public-key", keyPair.get(0));
                model.addAttribute("private-key", keyPair.get(1));
                userDataRepository.saveAndFlush(saltBase64UpdatedModel);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "sign_up_success";
        } else
            return "error";
    }
}
