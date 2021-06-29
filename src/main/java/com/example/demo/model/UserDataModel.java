package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserInfo")
public class UserDataModel {
    @Id
    private String id;
    @Column
    private byte[] pw;
    @Column
    private String Lastname;
    @Column
    private String Firstname;



    @Column
    private String PhoneNumber;
    @Column
    private String issued_time;
    @Column
    private String salt;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getPw() {
        return pw;
    }

    public void setPw(byte[] pw) {
        this.pw = pw;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getIssued_time() {
        return issued_time;
    }

    public void setIssued_time(String issued_time) {
        this.issued_time = issued_time;
    }
    public void setSalt(String salt) { this.salt = salt; }
    public UserDataModel(String _id, byte[] _pw, String lastname, String firstname, String phonenumber) {
        this.id = _id;
        this.pw = _pw;
        this.Lastname = lastname;
        this.Firstname = firstname;
        this.PhoneNumber = phonenumber;
        this.issued_time = null;
    }

    public UserDataModel() {
        super();
    }
}
