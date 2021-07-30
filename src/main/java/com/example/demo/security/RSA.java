package com.example.demo.security;

import org.hibernate.mapping.Array;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
        //System.out.println("cipher" + Base64.getEncoder().encodeToString(ciphertext));
        // and here we have it! a DER encoded PKCS#8 encrypted key!
        byte[] encryptedPkcs8 = null;
        encryptedPkcs8 = encinfo.getEncryptedData();
        ArrayList<String> ret = new ArrayList<>(Arrays.asList(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()), Base64.getEncoder().encodeToString(encryptedPkcs8), saltStr, Base64.getEncoder().encodeToString(encodedprivkey)));
        return ret;
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

    public static String encryptRSA(String plain , PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
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
        String decrypted = new String(bytePlain,"utf-8");
        return decrypted;
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
        System.out.println(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));

        return keyFactory.generatePrivate(keySpecPKCS8);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        String plain = "Hi!";

//        ArrayList<String> keypair = RSA.genRSAKeyPair("1234");
//        System.out.println(keypair.get(0)+"\n");
//        System.out.println(keypair.get(1)+"\n");
//        System.out.println(keypair.get(2)+"\n");
//        System.out.println(keypair.get(3)+"\n");

        String B64PUB="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxGlEqRxAf43a2MuNB4YtFwz/LILN7PpAMFkuFTeVds/R/0PZ1JWK7ERnufePjajufkQ8hH2JAUuoMruY2UjSthQyoQysB7whEUyqnBBkJ+5EsbAjI4MXsyS49icq0Uue6ZB+p8JK9zxu2wAwc1r9R9lYB1iQmNgvbj9OYQPifvj+IGtek7cCR3B1SHwRxkx4xmGeWf1XQj9cphnNSkt55I5i2bcm2BQI61hbco8QnxavfTBk2xgrCB5fojyUqN7voR9MLFiqUFlLFXYhGVsAdczZMTwvFSNwP0LvAHfyaWeIhwi8BIzB0P0EeI5i4I4gEQKbX9TKuLDCYeLxXUaCTQIDAQAB";
        String B64VAL="MIIEowIBAAKCAQEAxGlEqRxAf43a2MuNB4YtFwz/LILN7PpAMFkuFTeVds/R/0PZ1JWK7ERnufePjajufkQ8hH2JAUuoMruY2UjSthQyoQysB7whEUyqnBBkJ+5EsbAjI4MXsyS49icq0Uue6ZB+p8JK9zxu2wAwc1r9R9lYB1iQmNgvbj9OYQPifvj+IGtek7cCR3B1SHwRxkx4xmGeWf1XQj9cphnNSkt55I5i2bcm2BQI61hbco8QnxavfTBk2xgrCB5fojyUqN7voR9MLFiqUFlLFXYhGVsAdczZMTwvFSNwP0LvAHfyaWeIhwi8BIzB0P0EeI5i4I4gEQKbX9TKuLDCYeLxXUaCTQIDAQABAoIBAASJL9I9+klX09GE5eViFmF9Zdk3zhymbNwQ+yz3QybixsU/4mRz7ajmLYUQcArD93oIBmPC6GqUvjYpue5urZsR3Vd1zPVCxXq6TAhdoop/iKuX1z/nM6Fp/DGHFwiNWAap1Rr3tDaAeUjqeLP5Snch0FQfGfvFplbD9IHuDomDGSwI+nAkLKM0Pe+iINP2urMt2DAJQVvpln1nXOuEz0rPUgOBYr9gbJWdTVHcVCTfdm7ec0iTzf+FUCfZmyoi7PjkBzn/fjnjNaEc3OukCs9v5Swq7fCl9Y5A0CBjWeFNfMhv6YG5H+XUd7qGnMJk8IIHj40GlZzQL7+hqkB8TXECgYEA8jaA/NcB06nyKlGvAQ6lp19tWeb09F8Rg6PWglPi6PH5LYrgSFYc+Xyfy8bjBr5+d+ep06I0+s3RgACBTxH45y8PhNGlj8husqj6pl9YA2yrXV23rFdGGwtNa/DarN9pccdNPI42MD3uIxmrCBi9r9KVIUCp/QgnY+sou4zG1MUCgYEAz5dZAbo8c07yN7PkwIcDT/AJxXEui8q/KMMHG3P1lHq5mqeoOiO4/apytzkNNhMQ7zgCL58JJgYms6BrLfPpprYOvnhDQuRdW2SIqsNSKAB4PHiV1GzciYRozd+UxOD0hIlRyssOqW52DGtDNhM4DlCDtLc0gYHjAUvHgf0PH+kCgYEAoTU7ho0HzzvmTNpO8xIvmQV/f32jfV4lBwutwtJYTRh38yLigeKmqDqVFIjMI3n4LJAIzS3Bu0fvbxVm+xyIID6HQqxSWC9b1hd3s/HzX1wddifhDpiLtYZBQl1s/fA4exzeQDaaxGMDs2LrHZlx7qrmzNoACcQ6i1tBogphb80CgYAFU+sS4ka+5t3MlC9gyZZKLOzzRk5dhByX3TmHeNhqsSOmVns40sXirMc48JvoMWET4qobssc3VJ0Vqx5VFZd/kdibBtcjFl1XBSOEznKDWE+9rZKa2xhl5yIRdpWI4AUmiaexvrhnnNAmzXHyJ1ge9e4La1BLqpu64skCCw6iUQKBgHQ7DG7qagHDzraj4K7oSlCJbXufA5QcIXjCYFzZS26L+n560I7JZLJ+UNDy8GNqYpbGjlXuDOX/DXGokCJ/LYuyvzYPU6iSISwtpYtEgCQcsvycX8HxnCOTngnIx2r6v3xOQ3qdCRDOVJGAf1XagQqQjlcc367fNalRh58CN8r3";

        //String decPBEKey = RSA.decRSAPrivKey("1234",keypair.get(1),Base64.getDecoder().decode(keypair.get(2).getBytes()));

        PublicKey pubKey = RSA.getPublicKeyFromBase64String(B64PUB);
        PrivateKey privKey = RSA.getPrivateKeyFromBase64String(B64VAL);

        String cipher = RSA.encryptRSA(plain, pubKey);
        String decText = RSA.decryptRSA(cipher, privKey);

        System.out.println("Plain text is " + plain + "\n");
        System.out.println("Cipher text is " + cipher + "\n");
        System.out.println("Decrypted cipher text is " + decText + "\n");
    }

}
