package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TransferDB")
public class BankStatementDataModel {
    @Id
    private int count;

    @Column
    private String account;

    @Column
    private String transactionTime;

    @Column
    private String transactionAmount;

    @Column
    private String afterBalance;

    @Column
    private String depositAccount;


    public String getAccount() {return account;}
    public void setAccount(String account) {this.account = account;}
    public String getTransactionTime() { return transactionTime; }
    public void setTransactionTime(String transactionTime) { this.transactionTime = transactionTime;}
    public String getTransactionAmount() {return transactionAmount;}
    public void setTransactionAmount(String transactionAmount) {this.transactionAmount = transactionAmount;}
    public String getAfterBalance() {return afterBalance;}
    public void setAfterBalance(String afterBalance) {this.afterBalance = afterBalance;}
    public String getDepositAccount() {return depositAccount;}
    public void setDepositAccount(String depositAccount) {this.depositAccount = depositAccount;}

    public BankStatementDataModel(int count, String transactionTime, String transactionAmount, String afterBalance, String depositAccount) {
        this.count = count;
        this.transactionTime = transactionTime;
        this.transactionAmount = transactionAmount;
        this.afterBalance = afterBalance;
        this.depositAccount = depositAccount;
    }

    public BankStatementDataModel() {
        super();
    }
}
