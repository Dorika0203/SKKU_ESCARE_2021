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
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));

        return keyFactory.generatePrivate(keySpecPKCS8);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        String plain = "Hi!";

        ArrayList<String> keypair = RSA.genRSAKeyPair("1234");
        System.out.println(keypair.get(0)+"\n");
        System.out.println(keypair.get(1)+"\n");
        System.out.println(keypair.get(2)+"\n");
        System.out.println(keypair.get(3)+"\n");

        String B64PUB="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuwH7fTjU/75iv89wPAvI7XP487KRlqq+iht3h4+goxjKx0AAC1++DQLjglgoRJwuJnt5SXdfh4Yhm8bDGego0hO5waFTIyupKZNryy3B0yfnCA1IPgWQ3dCVvHNPXYjmWX0KTKI39a+Thdnkkcc0vaq5cgyYq1oCXSzPcERlCZDlklb3gb6eg60oA7aRWCg9PC5QUJ+lBjsM6KOvym9Fjzp8K+FoneWqqCdRVgI7gXsEzh7GzQ9K7RQRIEBcjFOLqQEHGDWzSz61pUNBQ07ET6B1K0EBuD2KDaycEWP00S9ILLY1I48PyJZ+YVWfQXK0vybQQtcIrtov42Pk7zWYqwIDAQAB";
        String B64VAL="MIIEowIBAAKCAQEAuwH7fTjU/75iv89wPAvI7XP487KRlqq+iht3h4+goxjKx0AAC1++DQLjglgoRJwuJnt5SXdfh4Yhm8bDGego0hO5waFTIyupKZNryy3B0yfnCA1IPgWQ3dCVvHNPXYjmWX0KTKI39a+Thdnkkcc0vaq5cgyYq1oCXSzPcERlCZDlklb3gb6eg60oA7aRWCg9PC5QUJ+lBjsM6KOvym9Fjzp8K+FoneWqqCdRVgI7gXsEzh7GzQ9K7RQRIEBcjFOLqQEHGDWzSz61pUNBQ07ET6B1K0EBuD2KDaycEWP00S9ILLY1I48PyJZ+YVWfQXK0vybQQtcIrtov42Pk7zWYqwIDAQABAoIBADwiTQ5dQhDi1bo8KCkG2RuSGVGz8CD00sRyRKNwygToKfycVedSaDii3ynA02IMnsJ9HelD25ImzZPb/EzOXKIA+dCL4cIDfigCYb05/4O45w+txbc77vOE6UFqCvFW3kuUa8VsvHXieZunD1rZJdp/lZZY+pbPIMd5a1L8i0jho8KHElnvoBm75wIs1HaSDwa4j2oEUNpWwbk+y8WG5TznodiK5ywNGuT59knCuHyPOW485D6HbiTUc9DYvDzMeTGe0v47onbt3ok/YacWpPb2ttUG+vZWzXo3YLdZa49rWv686wU+u/6M+2hGKSPHFQpaUDekZf6gPiq1DeIo6qECgYEA77+75jUDVgWrGsurhr0czcq0aEXG7Ru4fhSG+g2cKrMg49J59vNDoeHwwJIe0z4GvHlM42laFcdZzFmiKHZWJo9jYRbyoZ3IQfAb0jFjONF9kwq+t/K6ZMkMwGZfFOcfOh4PTYcXq1QnI59s3lC6IW+G+4DrwRM7TmzIpZ1VxTMCgYEAx68NMkfS/rcNrhoKVECJxqdw8JZaisqp3fVdvHamziMzrd9ur/8tZ7gvMEqw3rZHC3QoLe9DpJx52pblLmgNBFShBJ9YGbAuAoCIBW2baxAt8K7bjKP80aGRalJKcgpjgR5rsy4MCmBa7DLKZEEckKtxvGPKm12j7K0cP5i77qkCgYEAi5gZAOZXJeww/24dVlugfNaNCrWuKPreBlNPcCMijd40xVIU/8wO0iArPQUXe6n+5BjAwxzhfhLP32NFPsgAS31rwOlKpv2mz3XNzSpCep/HvSkheRuUmgBSM2in7hTQotdD08FX78MU8vHtwthAOB2m+6PKIWZgPr6qaXvdp+8CgYARkLeDdcf8uhOM/iNsd+TmwbHwp/k8/kjlCoF9Y56WwYf5Qo9VEghneE9GWzuly7kCK+yg5cw4fb9GWEG+zE5g1CT56B5y3AmgFLhiadrjFyBDbM5JV9+UfTbyeFyuHXXVVNy6fVF31DQLVQhyuzuNClfN76VR93HFDxFOEtxtSQKBgFL1MtXH1a1lgJyVnImhFbdeGkClkIHitf9gkjGv4ck2rF9Cwn9j/Mn7DT8AhNIQuc1aKRU4XwymcsRKa2g+VJW20GWhm4EMvql3ueXdxz4hp7dkpe+57wVsJ5zC5b7oyKMFcHVbKSjHr/D8dHWlC849+8V+OEf1m/Rn4drQ2zVf";


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
