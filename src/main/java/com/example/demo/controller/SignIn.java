package com.example.demo.controller;

        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.Arrays;

        import com.example.demo.model.*;
        import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder;
        import com.fortanix.sdkms.v1.*;
        import com.fortanix.sdkms.v1.api.*;
        import com.fortanix.sdkms.v1.model.*;
        import com.fortanix.sdkms.v1.auth.*;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/signin")
public class SignIn {

    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private UserDataInterface dbInterface;

    @PostMapping
    public String connect(Model model, String ID_IN, String PW_IN) {

        System.out.println(ID_IN + " " + PW_IN);
        int flag = 0;
        // connect to SDKMS
        ApiClient client = new ApiClient();
        client.setBasePath(server);
        client.setUsername(username);
        client.setPassword(password);
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth =
                    (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("SDKMS connect success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }

        // try Login.
        if(dbInterface.findById(ID_IN).isPresent()) {
            UserDataModel foundInfo = dbInterface.getById(ID_IN);
            // login success.
            byte[] hex = foundInfo.getPw();

            try {
                System.out.println("GOT PW HASH");
                System.out.println(sha256(PW_IN).toString());
                System.out.println("GOT Encrypted Hash");
                System.out.println((hex));
                System.out.println((hex).length);
                System.out.println("GOT Decrypted Hash");
                System.out.println(DecryptCipher((hex), client));
                if(Arrays.equals(sha256(PW_IN), DecryptCipher((hex), client))) return "sign_in_success";
                else flag = 1;
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NO SUCH ALRGORITHM EXCEPTION!!!!!!!!!!!!!!!!!");
                e.printStackTrace();
            }
        }
        // id does not exist.
        else flag = 2;

        switch (flag)
        {
            case 1:
                model.addAttribute("errorMessage", "Wrong Password");
                break;
            case 2:
                model.addAttribute("errorMessage", "No such ID");
                break;
            default:
                model.addAttribute("errorMessage", "FLAG VALUE IS WRONG!!");
        }
        return "sign_in_fail";

    }

    public byte[] DecryptCipher(byte[] cipher, ApiClient client) {
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

    public byte[] sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(msg.getBytes());
        return md.digest();
    }

//    public byte[] hexToBytes(String hex) {
//        byte[] temp = new BigInteger(hex, 16).toByteArray();
//        //byte[] retval = Arrays.copyOfRange(temp, 1, temp.length);
//        for(int i=0; i<temp.length; i++) System.out.printf("%d: %d\n", i, temp[i]);
//        return temp;
//    }

}
