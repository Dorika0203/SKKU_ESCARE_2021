package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AccountInfo")
public class AccountDataModel {
    @Id
    private String account;
    @Column
    private String userId;
    @Column
    private long balance;

    public String getAccount() { return account; }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public long getBalance() {return this.balance;}

    public void setBalance(long balance) {this.balance = balance;}

    public AccountDataModel(String account, String id) {
        this.account = account;
        this.userId = id;
        this.balance = 100000;
    }

    public AccountDataModel() {
        super();
    }
}