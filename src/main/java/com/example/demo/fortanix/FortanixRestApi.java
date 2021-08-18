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
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth = (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("Fortanix SDKMS Authentication success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
        return client;
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
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }

    public static KeyObject getSecurityObjectByID(ApiClient client, String ID) throws ApiException {
        SobjectDescriptor soDescriptor = new SobjectDescriptor()
                .name(ID);
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
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    public static String TokenEncrypt(String plain, ApiClient client){
        String ivStr = new String("ESCAREAAAAAAAAAA");
        byte[] bArrPlain = Base64.getDecoder().decode(plain);
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.alg(ObjectType.AES).mode(CryptMode.FPE).plain(bArrPlain);
        try{
            EncryptResponse encryptResponse = new EncryptionAndDecryptionApi(client)
                    .encrypt("2bf3feac-11b3-4a63-92e4-88f17d05bbfa", encryptRequest);
            return Base64.getEncoder().encodeToString(encryptResponse.getCipher());
        } catch (ApiException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String TokenDecrypt(String cipher, ApiClient client){
        String ivStr = new String("ESCAREAAAAAAAAAA");
        byte[] bArrCipher = Base64.getDecoder().decode(cipher);
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.alg(ObjectType.AES).mode(CryptMode.FPE).cipher(bArrCipher);
        try{
            DecryptResponse decryptResponse = new EncryptionAndDecryptionApi(client)
                    .decrypt("2bf3feac-11b3-4a63-92e4-88f17d05bbfa", decryptRequest);
            return Base64.getEncoder().encodeToString(decryptResponse.getPlain());
        } catch (ApiException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String plain = "0123456789123456";
        String B64Plain = Base64.getEncoder().encodeToString(plain.getBytes());
        ApiClient client;
        client = createClient("https://sdkms.fortanix.com", "a025eafd-5977-4924-8087-9b262315a974", "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ");

        String cipher = TokenEncrypt(B64Plain,client);
        System.out.println(cipher);

        plain = TokenDecrypt(cipher, client);
        System.out.println(plain);
    }

}
