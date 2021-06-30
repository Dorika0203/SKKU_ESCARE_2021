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
    private String lastname;
    @Column
    private String firstname;
    @Column
    private String balance;

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

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getFirstname() {return firstname;}

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getBalance() {return this.balance;}

    public void setBalance(String balance) {this.balance = balance;}

    public AccountDataModel(String account, String id, String lastname, String firstname) {
        this.account = account;
        this.userId = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.balance = "0";
    }

    public AccountDataModel() {
        super();
    }
}