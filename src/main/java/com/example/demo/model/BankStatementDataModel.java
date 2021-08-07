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
    private long account;

    @Column
    private long transactionTime;

    @Column
    private long transactionAmount;

    @Column
    private long afterBalance;

    @Column
    private long depositAccount;


    public long getAccount() {return account;}

    public void setAccount(long account) {this.account = account;}

    public long getTransactionTime() { return transactionTime; }

    public void setTransactionTime(long transactionTime) { this.transactionTime = transactionTime;}

    public long getTransactionAmount() {return transactionAmount;}

    public void setTransactionAmount(long transactionAmount) {this.transactionAmount = transactionAmount;}

    public long getAfterBalance() {return afterBalance;}

    public void setAfterBalance(long afterBalance) {this.afterBalance = afterBalance;}

    public long getDepositAccount() {return depositAccount;}

    public void setDepositAccount(long depositAccount) {this.depositAccount = depositAccount;}

    public BankStatementDataModel(long count, long account, long transactionTime, long transactionAmount, long afterBalance, long depositAccount) {
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
