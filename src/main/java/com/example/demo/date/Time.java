package com.example.demo.date;

import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import com.fortanix.sdkms.v1.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.demo.fortanix.FortanixRestApi.decryptAESCipherByFortanixSDKMS;
import static com.example.demo.bank.LoginClient.getVerifiedFortanixClient;
import static com.example.demo.fortanix.FortanixRestApi.generateAESCipherByFortanixSDKMS;

@Service
public class Time {
    private SignInDataRepository signInDataRepository;
    private SignOutDataRepository signOutDataRepository;

    @Autowired
    public Time(SignInDataRepository signInDataRepository, SignOutDataRepository signOutDataRepository) {
        this.signInDataRepository = signInDataRepository;
        this.signOutDataRepository =signOutDataRepository;
    }

    public static Integer parseTimestampFormatToUNIXTime(String timestamp) {
        if (timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int) (epoch / 1000);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getCurrentTime() {
        // 현재시간을 가져와 Date형으로 저장한다
        Date date_now = new Date(System.currentTimeMillis());
        // 년월일시분초 14자리 포멧
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date_now); // 14자리 포멧으로 출력한다
    }

    public boolean isClientLoginTimeLessThan5Minute(String userID) {
        if(userID.isEmpty())
            return false;
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        if (signInDataModelList.isEmpty()) {
            return false;
        } else if (signOutDataModelList.isEmpty()) {
            return true;
        } else {
            SignInDataModel lastSignInDataModel = signInDataModelList.get(signInDataModelList.size() - 1);
            SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(signOutDataModelList.size() - 1);
            byte[] signInTimestampCipher = lastSignInDataModel.getSignIn_time();
            byte[] decryptedByteSignInTime = decryptAESCipherByFortanixSDKMS(signInTimestampCipher, getVerifiedFortanixClient());
            String signInTimestamp = new String(decryptedByteSignInTime, StandardCharsets.UTF_8);
            int UNIXSignInTime = parseTimestampFormatToUNIXTime(signInTimestamp);

            byte[] signOutCipher = lastSignOutDataModel.getSignOut_time();
            byte[] decryptedByteSignOutTime = decryptAESCipherByFortanixSDKMS(signOutCipher, getVerifiedFortanixClient());
            String signOutTimestamp = new String(decryptedByteSignOutTime, StandardCharsets.UTF_8);
            int UNIXSignOutTime = parseTimestampFormatToUNIXTime(signOutTimestamp);
            int signOutAndInTimeDiff = UNIXSignOutTime - UNIXSignInTime;
            if (signOutAndInTimeDiff > 0)
                return false;
            int signInAndCurrentTimeDiff = (int) (System.currentTimeMillis() / 1000) - UNIXSignInTime;
            if (0 <= signInAndCurrentTimeDiff && signInAndCurrentTimeDiff <= 300) {
                return true;
            } else
                return false;
        }
    }
}
