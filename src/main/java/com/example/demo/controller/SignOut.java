package com.example.demo.controller;

import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignOutDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.bank.LoginClient.*;

@Controller
@RequestMapping("logout")
public class SignOut
{

    @Autowired
    private SignOutDataRepository signOutDataRepository;

    @GetMapping
    public String connect(HttpSession session) {

        long tmp = signOutDataRepository.count();
        int iTmp = Long.valueOf(tmp).intValue();
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, getVerifiedFortanixClient());
        String ID = getUserID();
        SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
        signOutDataRepository.saveAndFlush(signOutDataModel);
        session.removeAttribute("userID");
        // setUserID(null);

        return "logout_page";
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

}