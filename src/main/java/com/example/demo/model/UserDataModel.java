package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserInfo")
public class UserDataModel {
    @Id
    private String id;
    @Column
    private String pw;
    @Column
    private String Lastname;
    @Column
    private String Firstname;
    @Column
    private String PhoneNumber;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getLastname() { return Lastname; }

    public void setLastname(String lastname) {Lastname = lastname;}


    public UserDataModel(String _id, String _pw, String lastname, String firstname, String phonenumber) {
        this.id = _id;
        this.pw = _pw;
        this.Lastname = lastname;
        this.Firstname = firstname;
        this.PhoneNumber = phonenumber;
    }

    public UserDataModel() {
        super();
    }
}
