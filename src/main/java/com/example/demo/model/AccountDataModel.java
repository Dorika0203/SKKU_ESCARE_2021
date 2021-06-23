package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

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
    private byte[] accountPw;
    @Column
    private String id;
    @Column
    private String lastname;
    @Column
    private String firstname;



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) {this.lastname = lastname;}


    public AccountDataModel(String account, byte[] cipher, String id, String lastname, String firstname) {
        this.account = account;
        this.accountPw = cipher;
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public AccountDataModel() {
        super();
    }
}