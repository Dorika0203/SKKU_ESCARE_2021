package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="Admin_info")
public class AdminDataModel {
    
    @Id
    private String id;
    @Column
    private byte[] pw;
    @Column
    private String name;
    @Column
    private String number;
    @Column
    private long level;

    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}
    public byte[] getPw() {return this.pw;}
    public void setPw(byte[] pw) {this.pw = pw;}
    public String getName(){return this.name;}
    public void setName(String name) {this.name = name;}
    public String getNumber() {return this.number;}
    public void setNumber(String number) {this.number = number;}
    public long getLevel() {return this.level;}
    public void setLevel(long level) {this.level = level;}

    public AdminDataModel(String id, byte[] pw, String name, String number, long level) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.number = number;
        this.level = level;
    }
    public AdminDataModel() {
        super();
    }
}
