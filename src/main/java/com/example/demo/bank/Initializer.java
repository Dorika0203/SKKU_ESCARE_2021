package com.example.demo.bank;

import com.example.demo.model.AdminDataModel;
import com.example.demo.repository.AdminDataRepository;
import com.fortanix.sdkms.v1.ApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static com.example.demo.fortanix.FortanixRestApi.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

@Component
public class Initializer implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    AdminDataRepository adminDataRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String server = "https://sdkms.fortanix.com";
        String username = "a025eafd-5977-4924-8087-9b262315a974";
        String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";


        System.out.println("SERVERINIT");
        String id = "admin";
        byte[] pw = id.getBytes();
        String name = "sudo";
        String number = "000-0000-0000";
        long level = 0;

        ApiClient client = generateFortanixSDKMSClientAndVerify(server, username, password);
        byte[] cipherPW = generateAESCipherByFortanixSDKMS(sha256(pw), client);

        AdminDataModel superAdmin = new AdminDataModel(id, cipherPW, name, number, level);
        adminDataRepository.saveAndFlush(superAdmin);
    }

}