package com.example.demo.bank;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static com.example.demo.date.Time.*;


@WebListener
public class SessionControll implements HttpSessionListener{

    // @Autowired
    // private SignOutDataRepository signOutDataRepository;

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


        // 세션 만료이므로 로그아웃 처리 해야함 (로그인 후에 생긴 5분 제한에 의한 세션 종료인 경우 => 그 외로 생긴 세션은 userID와 client를 참조했을 때 null이므로 예외 처리 해주면 됨).
        // 아래 주석 처리 된 코드는 null pointer exception이 발생함.
        // HttpSessionListener가 Autowired를 받지 못하기 때문 (https://jodu.tistory.com/20, https://okky.kr/article/157770 참고)
        // 로그아웃 DB가 필요성이 없으므로 로그아웃 DB를 없애는 방식으로 변경하는 것 제안.




        // ApiClient client = getSessionApiClient(session);
        // String ID = getSessionUserID(session);

        // byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        // byte[] cipher = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);

        // if (ID != null && client != null)
        // {
        //     long tmp = signOutDataRepository.count(); // null pointer Exception !! Repository is null now.
        //     int iTmp = Long.valueOf(tmp).intValue();
        //     SignOutDataModel signOutDataModel = new SignOutDataModel(iTmp, ID, cipher);
        //     signOutDataRepository.saveAndFlush(signOutDataModel);
        // }

        System.out.println("[ Session Delete Done .. ]");
    }
}
