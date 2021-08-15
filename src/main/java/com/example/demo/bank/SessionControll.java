package com.example.demo.bank;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignOutDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.fortanix.FortanixRestApi.*;

import java.nio.charset.StandardCharsets;

import static com.example.demo.bank.LoginClient.*;
import static com.example.demo.date.Time.*;


@WebListener
public class SessionControll implements HttpSessionListener{

    @Autowired
    private SignOutDataRepository signOutDataRepository;

    // 세션 생성시 호출
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println("[ Session Created.. ]");
        System.out.println("session created Time: " + getTime(session.getCreationTime()));
        System.out.println("session ID: " + session.getId());
    }
  
    // 세션 만료시 호출
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        System.out.println("[ Session Delete Start! ]");
        HttpSession session = event.getSession();

        System.out.println("current Time: " + getCurrentTime());
        System.out.println("Last Accessed Time: " + getTime(session.getLastAccessedTime()));
        System.out.println("session created Time: " + getTime(session.getCreationTime()));
        System.out.println("session ID: " + session.getId());

        // 세션 만료이므로 로그아웃 처리 해야함.
        // 현재 JPA repository를 Autowired 했을 때 null 처리가 됨.
        // 로그아웃 DB가 필요성이 없으므로 로그아웃 기록 자체를 남기지 않거나,
        // Autowired가 가능하도록 해야 할 듯.
        ApiClient client = getSessionApiClient(session);

        long tmp = signOutDataRepository.count(); // null pointer Exception !! Repository is null now.
        int iTmp = Long.valueOf(tmp).intValue();
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        String ID = (String) session.getAttribute("userID");
        SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
        signOutDataRepository.saveAndFlush(signOutDataModel);

        System.out.println("[ Session Delete Done .. ]");
    }
}
