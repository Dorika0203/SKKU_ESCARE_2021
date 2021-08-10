package com.example.demo.controller;

import com.example.demo.fortanix.FortanixRestApi;
import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.*;
import com.example.demo.user.LoginClient;
import com.example.demo.model.UserDataModel;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.model.KeyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;

import java.util.Date;
import java.util.List;

import static com.example.demo.date.Time.parseTimestampFormatToUNIXTime;
import static com.example.demo.fortanix.FortanixRestApi.decryptAESCipherByFortanixSDKMS;
import static com.example.demo.fortanix.FortanixRestApi.generateAESCipherByFortanixSDKMS;
import static com.example.demo.user.LoginClient.getVerifiedFortanixClient;
import static com.example.demo.user.LoginClient.getUserID;

@Controller
public class ReIssuance {

    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    SignInDataRepository signInDataRepository;
    @Autowired
    SignOutDataRepository signOutDataRepository;
    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    BankStatementDataRepository bankStatementDataRepository;

    private String signUpID = null;

    @GetMapping("reissuance")
    public String reissuance() {

        String userID = getUserID();
        if (userID == null) {return "fail";}

        // =================================Get Sign Time==================================================
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        int signInLogIndex = signInDataModelList.size();
        if (signInLogIndex == 0) {
            return "my_page_fail";
        }
        SignInDataModel lastSignInDataModel = signInDataModelList.get(signInLogIndex - 1);

        byte[] signInTimestampCipher = lastSignInDataModel.getSignIn_time();
        byte[] decryptedByteSignInTime = decryptAESCipherByFortanixSDKMS(signInTimestampCipher, getVerifiedFortanixClient());

        String signInTimestamp = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);

        int UNIXSignInTime = parseTimestampFormatToUNIXTime(signInTimestamp);
        //================================================================================================
        //===================================Get SignOut Time=============================================
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        int signOutLogIndex = signOutDataModelList.size();
        if (signOutLogIndex == 0) {
            return "my_page";
        }
        SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(signOutLogIndex - 1);

        byte[] signOutCipher = lastSignOutDataModel.getSignOut_time();
        byte[] decryptedByteSignOutTime = decryptAESCipherByFortanixSDKMS(signOutCipher, getVerifiedFortanixClient());

        String signOutTimestamp = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);

        int UNIXSignOutTime = parseTimestampFormatToUNIXTime(signOutTimestamp);
        //====================================================================================================
        //================================Compare SignIn & SignOut Time=======================================

        int signOutAndInTimeDiff = UNIXSignOutTime - UNIXSignInTime;

        if (signOutAndInTimeDiff > 0)
            return "my_page_fail";


        int signInAndCurrentTimeDiff = (int) (System.currentTimeMillis() / 1000) - UNIXSignInTime;


        // session checked. show my_page
        if (0 <= signInAndCurrentTimeDiff && signInAndCurrentTimeDiff <= 300)
        {
            return "reissuance";
        }
        else
            return "fail";
    }

    @PostMapping("reissue")
    public String Reissue(String PW, Model model) throws ApiException {

        String inputID = getUserID();
        ApiClient client = getVerifiedFortanixClient();
        byte[] encryptedTimestamp = createAESEncryptedTimestampByFortanixSDKMS(client);
        saveLoginClientInfo(inputID, encryptedTimestamp);

        signUpID = LoginClient.getUserID();

        if (signUpID != null) {

            KeyObject value = FortanixRestApi.getSecurityObjectByID(client, signUpID);
            byte[] pub = value.getPubKey();
            byte[] priv = value.getValue();//pkcs1 priv key

            String base64PublicKey = Base64.getEncoder().encodeToString(pub);
            String base64PrivateKey = Base64.getEncoder().encodeToString(priv);

            UserDataModel saltBase64UpdatedModel = userDataRepository.getById(signUpID);

            model.addAttribute("ID", signUpID);
            model.addAttribute("publicKey", base64PublicKey);
            model.addAttribute("privateKey", base64PrivateKey);
            model.addAttribute("password", PW);
            userDataRepository.saveAndFlush(saltBase64UpdatedModel);

            return "reissue_success";
        } else
            return "error";
    }

    public String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

    public void saveLoginClientInfo(String loginClientID, byte[] encryptedTimestamp) {
        long count = signInDataRepository.count();
        SignInDataModel signInDataModel = new SignInDataModel((int) count, loginClientID, encryptedTimestamp);
        signInDataRepository.saveAndFlush(signInDataModel);
    }

    public byte[] createAESEncryptedTimestampByFortanixSDKMS(ApiClient client){
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] EncryptedTimestamp = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        return EncryptedTimestamp;
    }
}
