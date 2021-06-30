package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("transferpage")
public class TransferPage
{
    @GetMapping
    public String transferpage() {
        return "transfer_page";
    }

    @PostMapping("/transfer")
    public String transfer() {return "error";}
}