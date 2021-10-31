package com.example.demo.controller;

import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignOutDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpSession;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.bank.LoginClient.*;
import static com.example.demo.date.Time.*;

@Controller
@RequestMapping("logout")
public class SignOut
{

    @Autowired
    private SignOutDataRepository signOutDataRepository;

    @GetMapping
    public String connect(HttpSession session) {

        // 로그아웃을 시도하기 전에 이미 세션이 종료된 경우 -> 이미 로그아웃이 처리 되었으므로 로그아웃 페이지를 리턴
        if(!isSessionAvailable(session)) return "logout_page";

        ApiClient client = getSessionApiClient(session);
        
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        String ID = (String) session.getAttribute("userID");

        long tmp = signOutDataRepository.count();
        int iTmp = Long.valueOf(tmp).intValue();
        
        SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
        signOutDataRepository.saveAndFlush(signOutDataModel);
        // 세션 종료
        session.invalidate();
        return "logout_page";
    }
}