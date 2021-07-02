package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SignInLog")
public class SignInDataModel {
    @Id
    private int count;

    @Column
    private String userId;

    @Column
    private byte[] encrytedSignInTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getSignIn_time() { return encrytedSignInTime; }

    public void setSignIn_time(byte[] encrytedSignInTime) {
        this.encrytedSignInTime = encrytedSignInTime;
    }


    public SignInDataModel(int count, String _id, byte[] encrytedSignInTime) {
        this.count = count;
        this.userId = _id;
        this.encrytedSignInTime = encrytedSignInTime;
    }

    public SignInDataModel() {
        super();
    }
}
