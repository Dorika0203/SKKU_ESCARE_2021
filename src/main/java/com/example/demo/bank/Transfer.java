package com.example.demo.bank;

import com.example.demo.repository.AccountDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Transfer {


    private AccountDataRepository accountDataRepository;

    String receiverAccount;
    String senderAccount;
    long transferAmount = 0;
    long messageTimestamp = 0;

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
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
        setReceiverAccount(transferRequestParsedArray[0]);
        setSenderAccount(transferRequestParsedArray[1]);
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
        if (accountDataRepository.existsByAccount(receiverAccount) && accountDataRepository.existsByAccount(senderAccount))
            return true;
        else
            return false;
    }

    public Transfer(String receiverAccount, String senderAccount, long transferAmount, long messageTimestamp) {
        this.receiverAccount = receiverAccount;
        this.senderAccount = senderAccount;
        this.transferAmount = transferAmount;
        this.messageTimestamp = messageTimestamp;
    }

    @Autowired
    public Transfer(AccountDataRepository accountDataRepository) {
        super();
        this.accountDataRepository = accountDataRepository;
    }
}
