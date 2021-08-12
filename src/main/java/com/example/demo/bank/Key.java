package com.example.demo.bank;

import com.fortanix.sdkms.v1.model.KeyObject;

import java.util.Base64;

public class Key {
    private String userID;
    private String base64PublicKey;
    private String base64PrivateKey;
    private String password;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBase64PublicKey() {
        return base64PublicKey;
    }

    public void setBase64PublicKey(KeyObject keyPair) {
        byte[] publicKey = keyPair.getPubKey();
        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey);
        this.base64PublicKey = base64PublicKey;
    }

    public String getBase64PrivateKey() {

        return base64PrivateKey;
    }

    public void setBase64PrivateKey(KeyObject keyPair) {
        byte[] privateKey = keyPair.getValue();//pkcs1 priv key
        String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey);
        this.base64PrivateKey = base64PrivateKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Key(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }
}
