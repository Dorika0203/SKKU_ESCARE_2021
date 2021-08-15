package com.example.demo.bank;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignOutDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import static com.example.demo.date.Time.*;

import java.nio.charset.StandardCharsets;

import static com.example.demo.bank.LoginClient.*;
import static com.example.demo.fortanix.FortanixRestApi.*;


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

        // 의존성 주입
        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getSession().getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
    }
  
    // 세션 만료시 호출
    // 세션 만료 시나리오 1: 마이페이지 or 송금페이지에서 5분이상 무응답, 페이지 카운트다운으로 로그아웃 request를 보내기 전에 세션이 종료되어 로그아웃 controller 실행 전 session이 만료되는 경우.
    // 세션 만료 시나리오 2: 마이페이지, 송금페이지 외의 카운트다운 없는 페이지에서 (인증서 재발급 성공 페이지, 송금 성공 페이지 등) 5분 이상 무응답, 그 후 마이페이지로 복귀하는 경우.
    // 세션 만료 시나리오 3: 로그인하지 않은 사용자가 그냥 홈페이지에 접속 후 로그인을 하지 않고 종료, 그 이후 한달 후 홈페이지로 들어가는 경우. => 로그인된 세션이 아니라 정보가 전혀 없는 세션임. 예외처리 필요
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        System.out.println("[ Session Delete Start! ]");
        HttpSession session = event.getSession();

        System.out.println("current Time: " + getCurrentTime());
        System.out.println("Last Accessed Time: " + getTime(session.getLastAccessedTime()));
        System.out.println("session created Time: " + getTime(session.getCreationTime()));
        System.out.println("session ID: " + session.getId());



        // 시나리오 3을 제외한 경우 자동 로그아웃 처리.
        ApiClient client = getSessionApiClient(session);
        String ID = getSessionUserID(session);

        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] cipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);

        if (ID != null && client != null)
        {
            long tmp = signOutDataRepository.count();
            int iTmp = Long.valueOf(tmp).intValue();
            SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
            signOutDataRepository.saveAndFlush(signOutDataModel);
        }

        System.out.println("[ Session Delete Done .. ]");
    }
}
