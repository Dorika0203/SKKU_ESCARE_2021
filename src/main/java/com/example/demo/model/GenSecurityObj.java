package com.example.demo.model;

import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.api.SecurityObjectsApi;
import com.fortanix.sdkms.v1.model.KeyObject;
import com.fortanix.sdkms.v1.model.KeyOperations;
import com.fortanix.sdkms.v1.model.ObjectType;
import com.fortanix.sdkms.v1.model.SobjectRequest;

import java.util.Arrays;

public class GenSecurityObj {
    private SobjectRequest sobjectRequest;
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";


    public void Generate(){
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);

        sobjectRequest = new SobjectRequest()
                .name("Hi")
                .keySize(2048)
                .objType(ObjectType.RSA)
                .keyOps(Arrays.asList(KeyOperations.SIGN,
                                    KeyOperations.VERIFY,
                                    KeyOperations.EXPORT));
        SecurityObjectsApi securityObjectsApi = new
                SecurityObjectsApi(client);
        KeyObject keyObject =
                securityObjectsApi

    }
}
