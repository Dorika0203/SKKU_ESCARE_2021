package com.example.demo.controller;

import com.example.demo.user.LoginClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("transferpage")
public class TransferPage
{
    @GetMapping
    public String transferpage(Model model) {
        //add ID to model
        model.addAttribute("loginClientID", LoginClient.getUserID());
        return "transfer_page";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Map<String, Object> transferDataMap) {
        String privateKey = (String) transferDataMap.get("privateKey");
        System.out.println("privateKey" + privateKey);
        return "home_page";
    }
}