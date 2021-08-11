package com.example.demo.date;

import com.example.demo.model.SignInDataModel;
import com.example.demo.model.SignOutDataModel;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.demo.fortanix.FortanixRestApi.decryptAESCipherByFortanixSDKMS;
import static com.example.demo.user.LoginClient.getVerifiedFortanixClient;

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

    public boolean isClientLoginTimeLessThan5Minute(String userID) {
        List<SignInDataModel> signInDataModelList = signInDataRepository.findAllByUserId(userID);
        SignInDataModel lastSignInDataModel = signInDataModelList.get(signInDataModelList.size() - 1);
        List<SignOutDataModel> signOutDataModelList = signOutDataRepository.findAllByUserId(userID);
        SignOutDataModel lastSignOutDataModel = signOutDataModelList.get(signOutDataModelList.size() - 1);
        if (signInDataModelList.isEmpty()) {
            return false;
        } else if (signOutDataModelList.isEmpty()) {
            return true;
        } else {
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
