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


        System.out.println("SERVER INIT START...");


        // 기본 수퍼 관리자 생성.
        String id = "admin";
        byte[] pw = id.getBytes();
        String name = "sudo";
        String number = "000-0000-0000";
        long level = 0;

        ApiClient client = generateFortanixSDKMSClientAndVerify(server, username, password);
        byte[] cipherPW = generateAESCipherByFortanixSDKMS(sha256(pw), client);

        AdminDataModel superAdmin = new AdminDataModel(id, cipherPW, name, number, level);
        adminDataRepository.saveAndFlush(superAdmin);

        // test general admin initialize
        AdminDataModel testAdmin1 = new AdminDataModel("ID_1", cipherPW, "name_1", "010-1111-1111", 1);
        adminDataRepository.saveAndFlush(testAdmin1);
        AdminDataModel testAdmin2 = new AdminDataModel("ID_2", cipherPW, "name_2", "010-2222-2222", 1);
        adminDataRepository.saveAndFlush(testAdmin2);
        AdminDataModel testAdmin3 = new AdminDataModel("ID_3", cipherPW, "name_3", "010-3333-3333", 1);
        adminDataRepository.saveAndFlush(testAdmin3);
        AdminDataModel testAdmin4 = new AdminDataModel("ID_4", cipherPW, "name_4", "010-4444-4444", 1);
        adminDataRepository.saveAndFlush(testAdmin4);
        //


        System.out.println("SERVER INIT FINISHED !");
    }

}