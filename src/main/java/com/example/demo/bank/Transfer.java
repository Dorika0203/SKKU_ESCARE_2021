package com.example.demo.bank;

import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Transfer {


    private AccountDataRepository accountDataRepository;
    private BankStatementDataRepository bankStatementDataRepository;

    long receiverAccount = 0;
    long senderAccount = 0;
    long transferAmount = 0;
    long messageTimestamp = 0;

    public long getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(long receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public long getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(long senderAccount) {
        this.senderAccount = senderAccount;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransferInfoFromTransferRequest(String transferRequest) {
        String[] transferRequestParsedArray = transferRequest.split("\\s");
        setReceiverAccount(Integer.parseInt(transferRequestParsedArray[0]));
        setSenderAccount(Integer.parseInt(transferRequestParsedArray[1]));
        setTransferAmount(Integer.parseInt(transferRequestParsedArray[2]));
        setMessageTimestamp(Integer.parseInt(transferRequestParsedArray[3]));
    }


    public long getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(long messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public boolean isRequestArrivedLessThan10Seconds() {
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime - messageTimestamp < 10 && currentTime - messageTimestamp >= 0)
            return true;
        else
            return false;
    }

    public boolean isAccountsExistInAccountDatabase() {
        if (accountDataRepository.existsById(receiverAccount) && accountDataRepository.existsById(senderAccount))
            return true;
        else
            return false;
    }

    public Transfer(long receiverAccount, long senderAccount, long transferAmount, long messageTimestamp) {
        this.receiverAccount = receiverAccount;
        this.senderAccount = senderAccount;
        this.transferAmount = transferAmount;
        this.messageTimestamp = messageTimestamp;
    }

    @Autowired
    public Transfer(AccountDataRepository accountDataRepository, BankStatementDataRepository bankStatementDataRepository) {
        super();
        this.accountDataRepository = accountDataRepository;
        this.bankStatementDataRepository = bankStatementDataRepository;
    }
}
