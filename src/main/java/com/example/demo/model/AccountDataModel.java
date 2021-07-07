package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AccountInfo")
public class AccountDataModel {
    @Id
    private long account;
    @Column
    private String userId;
    @Column
    private String lastname;
    @Column
    private String firstname;
    @Column
    private long balance;

    public long getAccount() { return account; }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getFirstname() {return firstname;}

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public long getBalance() {return this.balance;}

    public void setBalance(long balance) {this.balance = balance;}

    public AccountDataModel(long account, String id, String lastname, String firstname) {
        this.account = account;
        this.userId = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.balance = 0;
    }

    public AccountDataModel() {
        super();
    }
}