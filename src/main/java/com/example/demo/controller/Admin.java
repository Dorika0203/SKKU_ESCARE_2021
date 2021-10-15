package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import com.example.demo.model.AdminDataModel;
import com.example.demo.repository.AdminDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.bank.LoginClient.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

import java.util.Arrays;

@Controller
public class Admin {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private AdminDataRepository adminDataRepository;

    // Login Page
    @RequestMapping(method = RequestMethod.GET, path = "/adminPage")
    public String adminHome(HttpSession session) {
        if(isAdminSessionAvailable(session)) {
            String adminID = getSessionUserID(session);
            Long level = adminDataRepository.getById(adminID).getLevel();

            // // token update test.
            // generateAESCipherByFortanixSDKMS("HI".getBytes(), getSessionApiClient(session));

            // super admin.
            if(level == 0) return "redirect:/adminPage/manageAdmin";
            else if (level == 1) return "redirect:/adminPage/manageClient";
            else return "fail"; 
        }
        return "admin_login";
    }


    // At Login Request
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/adminPage")
    public int adminLogin(String id, String pw, HttpSession session) {

        ApiClient client = generateFortanixSDKMSClientAndVerify(server, username, password);
        // if ID exist
        if(adminDataRepository.findById(id).isPresent())
        {
            AdminDataModel adminDataModel = adminDataRepository.getById(id);
            byte[] AESEncryptedPassword = adminDataModel.getPw();
            byte[] decryptedPassword = decryptAESCipherByFortanixSDKMS(AESEncryptedPassword, client);

            // PW check true.
            if(Arrays.equals(decryptedPassword, sha256(pw))) {
                setSessionUserID(id, session);
                setSessionFlag(1, session);
                setSessionApiClient(client, session);
                session.setMaxInactiveInterval(3600);
                System.out.println("HI");
                return 0;
            }
            return 2;
        }
        return 1;
    }
}
