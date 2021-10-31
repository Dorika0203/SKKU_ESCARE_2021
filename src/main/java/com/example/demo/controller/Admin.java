package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import com.example.demo.model.AdminDataModel;
import com.example.demo.model.UserDataModel;
import com.example.demo.repository.AdminDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static com.example.demo.bank.LoginClient.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

import java.util.Arrays;
import java.util.List;

@Controller
public class Admin {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private AdminDataRepository adminDataRepository;
    @Autowired
    private UserDataRepository userDataRepository;


    // 로그인 페이지
    @RequestMapping(method = RequestMethod.GET, path = "/adminPage")
    public String adminHome(HttpSession session) {
        if(isAdminSessionAvailable(session)) {
            String adminID = getSessionUserID(session);
            
            // DB에 해당 관리자 존재하지 않는 경우 (로그인 후에 수퍼관리자가 삭제하는 경우)
            if(!isAdminExist(adminID)) {
                session.invalidate();
                return "redirect:/adminPage";
            }
            Long level = adminDataRepository.getById(adminID).getLevel();

            // 수퍼 관리자
            if(level == 0) return "redirect:/adminPage/manageAdmin";
            // 일반 관리자
            else if (level == 1) return "redirect:/adminPage/manageClient";
            // 그 외 (오류)
            else
            {
                System.out.println("admin Level is out of bound...." + level);
                session.invalidate();
                return "redirect:/adminPage";
            } 
        }
        return "admin_login";
    }

    // 로그아웃
    @RequestMapping(method = RequestMethod.GET, path = "/adminPage/logout")
    public String adminLogOut(HttpSession session) {
        session.invalidate();
        return "redirect:/adminPage";
    }    


    // 로그인 요청 응답
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
                session.setMaxInactiveInterval(300); // 세션 수명
                return 0;
            }
            return 2;
        }
        return 1;
    }


    // 수퍼 관리자 기능 -----------------------------------------------------------------------------------
    // 수퍼 관리자 로그인 결과창 (관리자 리스트 조회 기능)
    @RequestMapping(method = RequestMethod.GET, path = "/adminPage/manageAdmin")
    public String superAdminHome(HttpSession session, Model model) {
        if(!isAdminSessionAvailable(session)) return "redirect:/adminPage";
        String currentAdminId = getSessionUserID(session);

        // DB에 해당 관리자 존재하지 않는 경우 (로그인 후에 수퍼관리자가 삭제하는 경우)
        if(!isAdminExist(currentAdminId)) {
            session.invalidate();
            return "redirect:/adminPage";
        }

        // 수퍼 관리자가 아닌데 시도하는 경우
        if(adminDataRepository.getById(currentAdminId).getLevel() != 0) return "redirect:/adminPage";

        List<AdminDataModel> adminList = adminDataRepository.findAll();
        JSONArray adminListInfo = new JSONArray();
        for(int i=0; i<adminList.size(); i++)
        {
            AdminDataModel ith_Admin = adminList.get(i);
            JSONObject ith_Info = new JSONObject();
            ith_Info.put("id", ith_Admin.getId());
            ith_Info.put("name", ith_Admin.getName());
            ith_Info.put("number", ith_Admin.getNumber());
            ith_Info.put("level", ith_Admin.getLevel());
            adminListInfo.put(ith_Info);
        }

        model.addAttribute("adminListInfo", adminListInfo);
        return "admin_home0";
    }


    // 관리자 생성 기능
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/adminPage/manageAdmin/create")
    public int createAdmin(HttpSession session, String id, String pw, String name, String number, String adminLevel)
    {
        // 세션이 사용 불가
        if(!isAdminSessionAvailable(session)) return 2;
        String currentAdminId = getSessionUserID(session);
        // DB에 해당 관리자 존재하지 않는 경우 (로그인 후에 수퍼관리자가 삭제하는 경우)
        if(!isAdminExist(currentAdminId)) {
            session.invalidate();
            return 2;
        }
        Long currentAdminLevel = adminDataRepository.getById(currentAdminId).getLevel();
        if(currentAdminLevel != 0) return 2;

        // 중복 아이디 존재
        if(isAdminExist(id)) return 1;


        // 입력 데이터타입 검사
        Long level;
        try {
            level = Long.parseLong(adminLevel);   
        } catch (NumberFormatException e) {
            System.out.println("not appropriate admin level");
            return 3;
        }
        
        ApiClient client = getSessionApiClient(session);
        byte[] cipher = generateAESCipherByFortanixSDKMS(sha256(pw), client);
        AdminDataModel newAdmin = new AdminDataModel(id, cipher, name, number, level);
        adminDataRepository.saveAndFlush(newAdmin);
        return 0;
    }

    // 관리자 제거 기능
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, path = "/adminPage/manageAdmin/remove")
    public int remove(HttpSession session, String id)
    {
        // 세션이 사용 불가
        if(!isAdminSessionAvailable(session)) return 2;
        String currentAdminId = getSessionUserID(session);

        // DB에 해당 관리자 존재하지 않는 경우 (로그인 후에 수퍼관리자가 삭제하는 경우)
        if(!isAdminExist(currentAdminId)) {
            session.invalidate();
            return 2;
        }
        Long currentAdminLevel = adminDataRepository.getById(currentAdminId).getLevel();
        if(currentAdminLevel != 0) return 2;

        // 아이디가 존재하지 않음
        if(!isAdminExist(id)) return 1;

        // 성공
        adminDataRepository.deleteById(id);
        return 0;
    }




    // 일반 관리자 기능 -----------------------------------------------------------------------------------
    
    // 일반 관리자 로그인 결과창 (유저 리스트 조회 기능)
    @RequestMapping(method = RequestMethod.GET, path = "/adminPage/manageClient")
    public String generalAdminHome(HttpSession session, Model model) {

        // 세션 검사
        if(!isAdminSessionAvailable(session)) return "redirect:/adminPage";
        String currentAdminId = getSessionUserID(session);

        // DB에 해당 관리자 존재하지 않는 경우 (로그인 후에 수퍼관리자가 삭제하는 경우)
        if(!isAdminExist(currentAdminId)) {
            session.invalidate();
            return "redirect:/adminPage";
        }

        // 일반 관리자가 아닌데 시도하는 경우
        if(adminDataRepository.getById(currentAdminId).getLevel() != 1) return "redirect:/adminPage";

        // 성공, 사용자 리스트 반환.
        List<UserDataModel> userList = userDataRepository.findAll();
        JSONArray userListInfo = new JSONArray();
        for(int i=0; i<userList.size(); i++)
        {
            UserDataModel ith_User = userList.get(i);
            JSONObject ith_Info = new JSONObject();
            ith_Info.put("id", ith_User.getId());
            ith_Info.put("lastName", ith_User.getLastname());
            ith_Info.put("firstName", ith_User.getFirstname());
            ith_Info.put("issuedTime", ith_User.getIssued_time());

            // // 전화 번호 토큰화 해제해서 보여주는 경우
            String tokenizedPhoneNumber = ith_User.getPhoneNumber();
            // String realPhoneNumber = new String(tokenDecryptByFortanixSDKMS(tokenizedPhoneNumber, getSessionApiClient(session)));
            // ith_Info.put("phoneNumber", realPhoneNumber);

            // 그냥 토큰화 결과로 보기
            String maskedPhoneNumber = tokenizedPhoneNumber.substring(0, 4) + "****" + tokenizedPhoneNumber.substring(8, 13);
            ith_Info.put("phoneNumber", maskedPhoneNumber);
            userListInfo.put(ith_Info);
        }

        model.addAttribute("userListInfo", userListInfo);
        return "admin_home1";
    }

    public boolean isAdminExist(String id) {
        return adminDataRepository.findById(id).isPresent();
    }
}
