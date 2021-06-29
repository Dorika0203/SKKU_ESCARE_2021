package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LoginSession")
public class LoginSessionModel {
    @Id
    private String id;

    @Column
    private String login_time;

    @Column
    private byte[] token;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public LoginSessionModel(String _id, String currentTime, byte[] token) {
        this.id = _id;
        this.login_time = currentTime;
        this.token = token;
    }

    public LoginSessionModel() {
        super();
    }
}
