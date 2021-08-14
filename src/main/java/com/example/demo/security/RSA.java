package com.example.demo.security;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.*;

public class RSA {
    /**
     * 2048비트 RSA 키쌍을 생성합니다.
     */

    public static boolean isSignatureVerified(String msg, PublicKey publicKey, byte[] sign) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, SignatureException {
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
