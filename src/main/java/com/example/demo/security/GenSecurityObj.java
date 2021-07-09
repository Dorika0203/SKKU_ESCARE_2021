package com.example.demo.security;

import com.example.demo.controller.SignUp;
import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.SecurityObjectsApi;
import com.fortanix.sdkms.v1.model.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class GenSecurityObj {

    private SobjectRequest sobjectRequest;

    public void GenerateRSA(ApiClient client, String userID){

         sobjectRequest = new SobjectRequest()
                .name(userID)
                .keySize(2048)
                .objType(ObjectType.RSA)
                .keyOps(Arrays.asList(KeyOperations.SIGN,
                                    KeyOperations.VERIFY,
                                    KeyOperations.EXPORT));
        SecurityObjectsApi securityObjectsApi = new
                SecurityObjectsApi(client);
        try{
            KeyObject keyObject =
                    securityObjectsApi.generateSecurityObject(sobjectRequest);
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }
}
