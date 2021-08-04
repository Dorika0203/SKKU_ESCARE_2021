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

}
