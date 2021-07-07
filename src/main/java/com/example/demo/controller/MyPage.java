package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.api.EncryptionAndDecryptionApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.user.LoginClient.getUserID;

@Controller
@RequestMapping("mypage")
public class MyPage {
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private SignInDataRepository signInDataRepository;
    @Autowired
    private SignOutDataRepository signOutDataRepository;
    @Autowired
    private AccountDataRepository accountDataRepository;

    @GetMapping
    public String mypage(Model model) {

        // connect to SDKMS
        ApiClient client = createClient(server, username, password);
        connectFortanixsdkms(client);

        String userID = getUserID();
        if (userID == null) {return "my_page_fail";}

        // =================================Get Sign Time==================================================
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        int idx = signInDataModelList.size();
        if (idx == 0) {return "my_page_fail";}
        SignInDataModel lastSignInDataModel = signInDataModelList.get(idx-1);

        byte[] signIn_cipher = lastSignInDataModel.getSignIn_time();
        byte[] decryptedByteSignInTime = DecryptCipher(signIn_cipher, client);
        System.out.println(decryptedByteSignInTime);

        String signIn_time = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);
        System.out.println(signIn_time);
        System.out.println("My_Page");

        String signInDate; String signInTime;
        signInDate = signIn_time.split(" ")[0];
        signInTime = signIn_time.split(" ")[1];

        int signInYear; int signInMonth; int signInDay; int signInHour; int signInMinute; int signInSecond;
        signInYear = Integer.parseInt(signInDate.split("-")[0]);
        signInMonth = Integer.parseInt(signInDate.split("-")[1]);
        signInDay = Integer.parseInt(signInDate.split("-")[2]);

        signInHour = Integer.parseInt(signInTime.split(":")[0]);
        signInMinute = Integer.parseInt(signInTime.split(":")[1]);
        signInSecond = Integer.parseInt(signInTime.split(":")[2]);
        //================================================================================================
        //===================================Get SignOut Time=============================================
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        idx = signOutDataModelList.size();
        if (idx == 0) {return "my_page";}
        SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(idx-1);

        byte[] signOutcipher = lastSignOutDataModel.getSignOut_time();
        byte[] decryptedByteSignOutTime = DecryptCipher(signOutcipher, client);
        System.out.println(decryptedByteSignOutTime);

        String signOut_time = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);
        System.out.println(signOut_time);
        System.out.println("My_Page");

        String signOutDate; String signOutTime;
        signOutDate = signOut_time.split(" ")[0];
        signOutTime = signOut_time.split(" ")[1];

        int signOutYear; int signOutMonth; int signOutDay; int signOutHour; int signOutMinute; int signOutSecond;
        signOutYear = Integer.parseInt(signOutDate.split("-")[0]);
        signOutMonth = Integer.parseInt(signOutDate.split("-")[1]);
        signOutDay = Integer.parseInt(signOutDate.split("-")[2]);

        signOutHour = Integer.parseInt(signOutTime.split(":")[0]);
        signOutMinute = Integer.parseInt(signOutTime.split(":")[1]);
        signOutSecond = Integer.parseInt(signOutTime.split(":")[2]);
        //====================================================================================================
        //================================Compare SignIn & SignOut Time=======================================
        int signYearDiff = signOutYear - signInYear; int signMonthDiff = signOutMonth - signInMonth; int signDayDiff = signOutDay - signInDay;
        int signHourDiff = signOutHour - signInHour; int signMinuteDiff = signOutMinute - signInMinute; int signSecondDiff = signOutSecond - signInSecond;

        int signDiff = (((((signYearDiff * 12 + signMonthDiff) * 31 + signDayDiff) * 24 + signHourDiff) * 60 + signMinuteDiff) * 60 + signSecondDiff);

        if (signDiff > 0)
            return "my_page_fail";

        String current_time = getCurrentTime();

        String curDate; String curTime;
        curDate = current_time.split(" ")[0];
        curTime = current_time.split(" ")[1];

        int curYear; int curMonth; int curDay; int curHour; int curMinute; int curSecond;
        curYear = Integer.parseInt( curDate.split("-")[0]);
        curMonth = Integer.parseInt(curDate.split("-")[1]);
        curDay = Integer.parseInt(curDate.split("-")[2]);

        curHour = Integer.parseInt(curTime.split(":")[0]);
        curMinute = Integer.parseInt(curTime.split(":")[1]);
        curSecond = Integer.parseInt(curTime.split(":")[2]);

        int yearDiff = curYear - signInYear; int monthDiff = curMonth - signInMonth; int dayDiff = curDay - signInDay;
        int hourDiff = curHour - signInHour; int minuteDiff = curMinute - signInMinute; int secondDiff = curSecond - signInSecond;
        int diff = (((((yearDiff * 12 + monthDiff) * 31 + dayDiff) * 24 + hourDiff) * 60 + minuteDiff) * 60 + secondDiff);


        if (0 <= diff && diff <= 300) {         //Connect to mypage Success.
            List<AccountDataModel> userAccountDataModelList = accountDataRepository.findAllByUserId(getUserID());
            List<List<String>> userAccountDataList = new ArrayList<List<String>>();
            for (int i = 0; i < userAccountDataModelList.size(); i++)
            {
                userAccountDataList.get(i).add(userAccountDataModelList.get(i).getAccount());
                userAccountDataList.get(i).add(userAccountDataModelList.get(i).getBalance());
            }

            model.addAttribute("userAccountNameList", userAccountDataList);

            return "my_page";
        }
        else
            return "my_page_fail";
    }

    public byte[] DecryptCipher(byte[] cipher, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.alg(ObjectType.AES).cipher(cipher).mode(CryptMode.CBC).iv(ivStr.getBytes());
        try {
            DecryptResponse decryptResponse = new EncryptionAndDecryptionApi(client).decrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", decryptRequest);
            return decryptResponse.getPlain();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

    // connect to SDKMS
    public ApiClient createClient(String server, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        return client;
    }

    public void connectFortanixsdkms(ApiClient client) {
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth = (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }
}