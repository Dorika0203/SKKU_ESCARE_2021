package com.example.demo.security;

import org.hibernate.mapping.Array;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    public static ArrayList<String> genRSAKeyPair(String password) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator gen;
        gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, secureRandom);
        KeyPair keyPair = gen.genKeyPair();

        // extract the encoded private key, this is an unencrypted PKCS#8 private key
        byte[] encodedprivkey = keyPair.getPrivate().getEncoded();

        // We must use a PasswordBasedEncryption algorithm in order to encrypt the private key, you may use any common algorithm supported by openssl, you can check them in the openssl documentation http://www.openssl.org/docs/apps/pkcs8.html
        String MYPBEALG = "PBEWithSHA1AndDESede";

        int count = 20;// hash iteration count
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);

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
            ciphertext = pbeCipher.doFinal(encodedprivkey);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
        AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
        try {
            algparms.init(pbeParamSpec);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

        // and here we have it! a DER encoded PKCS#8 encrypted key!
        byte[] encryptedPkcs8 = null;
        try {
            encryptedPkcs8 = encinfo.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String temp = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        ArrayList<String> ret = new ArrayList<>(Arrays.asList(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()), Base64.getEncoder().encodeToString(encryptedPkcs8)));
        return ret;
    }

    public static void decRSAPrivKey(String password, byte[] salt, String encPrivKey) {
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
    }

    public static void main(String[] args) {
        try {
            genRSAKeyPair("abcd").get(1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public Key로 RSA 암호화를 수행합니다. * @param plainText 암호화할 평문입니다. * @param publicKey 공개키 입니다. * @return
     */
    public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;
    }

    /**
     * Private Key로 RAS 복호화를 수행합니다. * * @param encrypted 암호화된 이진데이터를 base64 인코딩한 문자열 입니다. * @param privateKey 복호화를 위한 개인키 입니다. * @return * @throws Exception
     */
    public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");
        return decrypted;
    }

    /**
     * Base64 엔코딩된 개인키 문자열로부터 PrivateKey 객체를 얻는다. * @param keyString * @return * @throws NoSuchAlgorithmException * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String privateKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        try {
            return keyFactory.generatePrivate(keySpecPKCS8);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64 엔코딩된 공용키키 문자열로부터 PublicKey객체를 얻는다. * @param keyString * @return * @throws NoSuchAlgorithmException * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String publicKeyString = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        return keyFactory.generatePublic(keySpecX509);
    }

}
