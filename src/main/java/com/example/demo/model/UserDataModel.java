package com.example.demo.model;

public class UserDataModel {
    private String id;
    private String pw;

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

    UserDataModel(String _id, String _pw) {
        this.id = _id;
        this.pw = _pw;
    }
}
