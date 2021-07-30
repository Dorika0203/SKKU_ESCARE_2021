package com.example.demo.security;

import com.fortanix.sdkms.v1.ApiClient;
import com.fortanix.sdkms.v1.ApiException;
import com.fortanix.sdkms.v1.api.AuthenticationApi;
import com.fortanix.sdkms.v1.auth.ApiKeyAuth;
import com.fortanix.sdkms.v1.model.AuthResponse;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class RSA {
    /**
     * 2048비트 RSA 키쌍을 생성합니다.
     */

    private static String server = "https://sdkms.fortanix.com";
    private static String username = "a025eafd-5977-4924-8087-9b262315a974";
    private static String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    //generate RSA key pair from sdkms and encrypt with PBE key
    public static ArrayList<String> genFortanixPBEKeyAndSalt(String password, PrivateKey privateKey) throws NoSuchAlgorithmException {

        byte[] encodedPrivKey = privateKey.getEncoded();

        String MYPBEALG = "PBEWithSHA1AndDESede";

        // hash iteration count
        int count = 20;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        String saltStr = Base64.getEncoder().encodeToString(salt);

        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);

        //password
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());

        //cipher mode
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);

        //encrypted password
        SecretKey pbeKey = null;
        try {
            //encrypt password
            pbeKey = keyFac.generateSecret(pbeKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        Cipher pbeCipher = null;
        try {
            pbeCipher = Cipher.getInstance(MYPBEALG);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        // Initialize PBE Cipher with key and parameters
        try {
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // Encrypt the encoded Private Key with the PBE key
        byte[] ciphertext = null;
        try {
            ciphertext = pbeCipher.doFinal(encodedPrivKey);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        String base64PrivateKey = Base64.getEncoder().encodeToString(ciphertext);

        ArrayList<String> keyInfo = new ArrayList<>(Arrays.asList(base64PrivateKey, saltStr));

        return keyInfo;
    }

    public static PrivateKey getPKCS8KeyFromPKCS1Key(byte[] privateKeyBytes) throws Exception {
        // Get the private key for PKCS # 1
        RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(privateKeyBytes));
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privKey = keyFactory.generatePrivate(rsaPrivKeySpec);
        return privKey;
    }

    public static String decRSAPrivKey(String password, String encPrivKey, byte[] salt) {
        String MYPBEALG = "PBEWithSHA1AndDESede";

        int count = 20;// hash iteration count

        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        //password
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        //cipher mode
        SecretKeyFactory keyFac = null;
        try {
            keyFac = SecretKeyFactory.getInstance(MYPBEALG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //encrypted password
        SecretKey pbeKey = null;
        try {
            //encrypt password
            pbeKey = keyFac.generateSecret(pbeKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        Cipher pbeCipher = null;
        try {
            pbeCipher = Cipher.getInstance(MYPBEALG);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Initialize PBE Cipher with key and parameters
        try {
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // Encrypt the encoded Private Key with the PBE key
        byte[] ciphertext = null;
        try {
            ciphertext = pbeCipher.doFinal(Base64.getDecoder().decode(encPrivKey));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(ciphertext);
    }

    public static String encryptRSA(String plain, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plain.getBytes());
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;
    }

    public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");
        return decrypted;
    }

    public static boolean signatureVerified(String msg, PublicKey publicKey, byte[] sign) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, SignatureException {
        String msgB = msg;
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(msgB.getBytes());
        boolean verify = signature.verify(sign);
        return verify;
    }

    public static PublicKey getPublicKeyFromBase64String(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyString =
                keyString.replace("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpecX509 =
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));

        return keyFactory.generatePublic(keySpecX509);
    }

    public static PrivateKey getPrivateKeyFromBase64String(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyString =
                keyString.replace("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));

        return keyFactory.generatePrivate(keySpecPKCS8);
    }

    //used only in demo testing
    public static void connectFortanixsdkms(ApiClient client) {
        AuthenticationApi authenticationApi = new AuthenticationApi(client);
        try {
            AuthResponse authResponse = authenticationApi.authorize();
            ApiKeyAuth bearerTokenAuth = (ApiKeyAuth) client.getAuthentication("bearerToken");
            bearerTokenAuth.setApiKey(authResponse.getAccessToken());
            bearerTokenAuth.setApiKeyPrefix("Bearer");
            System.out.println("success");
        } catch (ApiException e) {
            System.err.println("Unable to authenticate: " + e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {
        String plain = "Hi!";

        String B64PUB = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1K0v0d0EJLOmO8a2v6Q4EtujkBl0l56V2Vh+O23O2MPGW4a4biDgOlefaxZYBsWwASFr7cZylGQzojTQwYMDnuCBe8yoxuQnLiu9kZ9Jqd+8LoRSd6rkXTtWdh94LsVFoMHDjEWp6+G4zOq7qWUwC3QBofgEcGKN7qM/jrnVaIff2OsH8AuTaCSI96PqYjeZXN4BYS2YcoFBiTuHGCze/ov5m6EJtHWkVrvoGuxIz1FImxxwuXR8I0M+6OvolLM/Y/rJIHWDC14mRngerJvR20fboOL+SOBP4E8eajiiwikhwn1SQfqNk6yG0shjkuYgF07ZvioCNJ2tg48BKiprmQIDAQAB";
        String B64VAL = "VcM6uUo1au0wBytQjJXMyFP2Y4UPNxA6tgx5pcGo+aLtC+14o7sWLdkbnRXNZCEkiyGUpLrTgnI2FOiPhJLJl0tYItA4NYKzlzPYcuIu+PMq1NWAJWBBLDNGLcRTHjP34CBs6LoGEDwLoUxFPN4+mOEVe/9hZS8sBE8imoYAbC+yVau2/MXlXh1oPIK+AcjRUSvDANAj4ZBXo8IqxV5dv5RxYNsY+2F2bCXhr1SAJr7r16VQoUy5MA2C5f7LnnVpHtaeaEziAJ6UoB1mg9M+MgLPgzaaW/sOU7CErhO+LfP0nFye5WQ+VfZUOt2IbMXSaEGXYp4UEzn+JGB5WPlxs++sXLgaeFSpSMB357u/LkgfFxGcYZZcAF9aQi+vQxB9CJ+gSYUPVmYflJBEWvfpeqSjT4B4ieAuA+5H5KuNcZCvP3sjQyl0OO/9oJ5Ay7peLRUd1XnKo1GFbbeM+csSOYJ3C/D7Jbj0BpaDz8C3HpLJVQZKJ89KUgUX24Ciftvjcgf7vYq1qKp8OJXHZ9FFEkQnlzCgrsP88WljaETGkNWD/pWfX3hseUHvb8IBYJz+GaI2Seh6c9ZM1cvI501J/Uern13RQF5V+6Wy0sGqmhOgPP3LybYHfs0s0C3xsR9lxSUN0NPzx71osHWSFZ0cEypsZavPEt9bKzXu4iSUX4KJvBNdZvS4SpLoFeUyiQTKFFsMWEDXlgCLxogpymmJP2wQsdUSTEKrSXmEpgHlzVV4KP28q7b0wDK1dq37s7MW";
        byte[] BArrVal = Base64.getDecoder().decode(B64VAL);


        String decPBEKey = decRSAPrivKey("asd", B64VAL, Base64.getDecoder().decode("+ckLJtDzTfk="));

        PublicKey pubKey = RSA.getPublicKeyFromBase64String(B64PUB);
        PrivateKey privKey = getPrivateKeyFromBase64String(decPBEKey);
//        PrivateKey privKey = RSA.getPKCS8KeyFromPKCS1Key(BArrVal);

        String cipher = RSA.encryptRSA(plain, pubKey);
        String decText = RSA.decryptRSA(cipher, privKey);

        System.out.println("Plain text is " + plain + "\n");
        System.out.println("Cipher text is " + cipher + "\n");
        System.out.println("Decrypted cipher text is " + decText + "\n");


    }

}
