package com.example.demo.fortanix;

import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.SecurityObjectsApi;
import com.fortanix.sdkms.v1.model.*;

import java.util.Arrays;

public class fortanixRestApi {
    public static ApiClient createClient(String server, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        return client;
    }

    private static String server = "https://sdkms.fortanix.com";
    private static String username = "a025eafd-5977-4924-8087-9b262315a974";
    private static String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    public static void genRSAKeyFromFortanixSDKMS(ApiClient client, String ID){
        SobjectRequest sobjectRequest = new SobjectRequest()
                .name(ID)
                .keySize(2048)
                .objType(ObjectType.RSA)
                .keyOps(Arrays.asList(KeyOperations.SIGN,
                        KeyOperations.VERIFY,
                        KeyOperations.EXPORT,
                        KeyOperations.UNWRAPKEY));
        SecurityObjectsApi securityObjectsApi = new
                SecurityObjectsApi(client);
        try{
            KeyObject keyObject =
                    securityObjectsApi.generateSecurityObject(sobjectRequest);
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }

    public static KeyObject getSecObj(ApiClient client, String ID) throws ApiException {
        SobjectDescriptor soDescriptor = new SobjectDescriptor()
                .name(ID);
        SecurityObjectsApi securityObjectsApi = new SecurityObjectsApi(client);
        KeyObject keyObject = securityObjectsApi.getSecurityObjectValueEx(soDescriptor);

        return keyObject;
    }

}
