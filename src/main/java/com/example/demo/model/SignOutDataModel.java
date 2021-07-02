package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SignOutLog")
public class SignOutDataModel {
    @Id
    private int count;

    @Column
    private String userId;

    @Column
    private byte[] encrytedSignOutTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getSignOut_time() {
        return encrytedSignOutTime;
    }

    public void setSignOut_time(byte[] encrytedSignOutTime) {
        this.encrytedSignOutTime = encrytedSignOutTime;
    }


    public SignOutDataModel(int count, String _id, byte[] encrytedSignOutTime) {
        this.count = count;
        this.userId = _id;
        this.encrytedSignOutTime = encrytedSignOutTime;
    }

    public SignOutDataModel() {
        super();
    }
}
