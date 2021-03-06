package com.example.demo.fortanix;

import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.api.EncryptionAndDecryptionApi;
import com.fortanix.sdkms.v1.api.SecurityObjectsApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static com.example.demo.date.Time.getCurrentTime;

public class FortanixRestApi {

    public static ApiClient createClient(String server, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        return client;
    }

    public static ApiClient generateFortanixSDKMSClientAndVerify(String server, String username, String password) {
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        updateBearerToken(client);
        return client;
    }


    // BearerToken update.
    public static boolean updateBearerToken(ApiClient client) {
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        AuthResponse authResponse;
        try {
            authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth = (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("Bearer Token authentication SUCCESS...");
            return true;
        } catch (ApiException e) {
            System.err.println("Bearer Token authentication FAIL..." + e.getMessage());
            return false;
        }
    }



    public static void generateRSAKeyFromFortanixSDKMS(ApiClient client, String ID){
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
            securityObjectsApi.generateSecurityObject(sobjectRequest);
        } catch (ApiException e) {
            System.out.println("Token dead.");
            if(updateBearerToken(client)) generateRSAKeyFromFortanixSDKMS(client, ID);
        }
    }

    public static KeyObject getSecurityObjectByID(ApiClient client, String ID) throws ApiException {
        SobjectDescriptor soDescriptor = new SobjectDescriptor().name(ID);
        SecurityObjectsApi securityObjectsApi = new SecurityObjectsApi(client);
        KeyObject keyObject = securityObjectsApi.getSecurityObjectValueEx(soDescriptor);
        return keyObject;
    }

    public static byte[] generateAESCipherByFortanixSDKMS(byte[] plain, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.alg(ObjectType.AES).plain(plain).mode(CryptMode.CBC).setIv(ivStr.getBytes());
        try {
            EncryptResponse encryptResponse = new EncryptionAndDecryptionApi(client)
                    .encrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", encryptRequest);
            return encryptResponse.getCipher();
        } catch (ApiException e) {
            System.out.println("Token dead.");
            if(updateBearerToken(client)) return generateAESCipherByFortanixSDKMS(plain, client);
            return null;
        }
    }

    public static byte[] generateAESEncryptedTimestampByFortanixSDKMS(ApiClient client){
        byte[] byteCurrentTime = getCurrentTime().getBytes(StandardCharsets.UTF_8);
        byte[] EncryptedTimestamp = generateAESCipherByFortanixSDKMS(byteCurrentTime, client);
        return EncryptedTimestamp;
    }

    public static byte[] decryptAESCipherByFortanixSDKMS(byte[] cipher, ApiClient client) {
        String ivStr = new String("ESCAREAAAAAAAAAA");
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.alg(ObjectType.AES).cipher(cipher).mode(CryptMode.CBC).iv(ivStr.getBytes());
        try {
            DecryptResponse decryptResponse = new EncryptionAndDecryptionApi(client).decrypt("72ea7189-a27e-4625-96b0-fc899e8a49ff", decryptRequest);
            return decryptResponse.getPlain();
        } catch (ApiException e) {
            System.out.println("Token dead.");
            if(updateBearerToken(client)) return decryptAESCipherByFortanixSDKMS(cipher, client);
            return null;
        }
    }

    public static byte[] tokenEncryptByFortanixSDKMS(String plain, ApiClient client){
        byte[] bArrPlain = Base64.getDecoder().decode(plain);
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.alg(ObjectType.AES).mode(CryptMode.FPE).plain(bArrPlain);
        try{
            EncryptResponse encryptResponse = new EncryptionAndDecryptionApi(client)
                    // .encrypt("470420aa-ebd7-4198-a8ad-09637f087f04", encryptRequest);
                    .encrypt("c253ae61-73d9-4182-a261-ef63dfd31644", encryptRequest);
            return encryptResponse.getCipher();
        } catch (ApiException e){
            System.out.println("Token dead.");
            if(updateBearerToken(client)) return tokenEncryptByFortanixSDKMS(plain, client);
            return null;
        }
    }

    public static byte[] tokenDecryptByFortanixSDKMS(String cipher, ApiClient client){
        byte[] bArrCipher = cipher.getBytes();
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.alg(ObjectType.AES).mode(CryptMode.FPE).cipher(bArrCipher);
        try{
            DecryptResponse decryptResponse = new EncryptionAndDecryptionApi(client)
                    // .decrypt("470420aa-ebd7-4198-a8ad-09637f087f04", decryptRequest);
                    .decrypt("c253ae61-73d9-4182-a261-ef63dfd31644", decryptRequest);
            return decryptResponse.getPlain();
        } catch (ApiException e){
            System.out.println("Token dead.");
            if(updateBearerToken(client)) return tokenDecryptByFortanixSDKMS(cipher, client);
            return null;
        }
    }

    public static void main(String[] args) {

    }

}
