package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BankStatementInfo")
public class BankStatementDataModel {
    @Id
    private long count;

    @Column
    private String account;

    @Column
    private long transactionTime;

    @Column
    private long transactionAmount;

    @Column
    private long afterBalance;

    @Column
    private String depositAccount;


    public String getAccount() {return account;}

    public void setAccount(String account) {this.account = account;}

    public long getTransactionTime() { return transactionTime; }

    public void setTransactionTime(long transactionTime) { this.transactionTime = transactionTime;}

    public long getTransactionAmount() {return transactionAmount;}

    public void setTransactionAmount(long transactionAmount) {this.transactionAmount = transactionAmount;}

    public long getAfterBalance() {return afterBalance;}

    public void setAfterBalance(long afterBalance) {this.afterBalance = afterBalance;}

    public String getDepositAccount() {return depositAccount;}

    public void setDepositAccount(String depositAccount) {this.depositAccount = depositAccount;}

    public BankStatementDataModel(long count, String account, long transactionTime, long transactionAmount, long afterBalance, String depositAccount) {
        this.count = count;
        this.account = account;
        this.transactionTime = transactionTime;
        this.transactionAmount = transactionAmount;
        this.afterBalance = afterBalance;
        this.depositAccount = depositAccount;
    }

    public BankStatementDataModel() {
        super();
    }
}
