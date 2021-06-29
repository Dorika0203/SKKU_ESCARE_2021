package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reissuance")
public class ReIssuance {

    @GetMapping
    public String reissuance(){ return "reissuance_page"; }
}
