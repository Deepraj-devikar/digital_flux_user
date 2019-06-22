package com.dolphintechno.dolphindigitalflux.model;

public class WalletHistoryTransaction {

    String walletHistoryId;
    String transaction;
    String doneBy;
    int amount;
    String reason;
    int balance;
    String dateTime;

    public WalletHistoryTransaction(String walletHistoryId, String transaction, String doneBy, int amount, String reason, int balance, String dateTime) {
        this.walletHistoryId = walletHistoryId;
        this.transaction = transaction;
        this.doneBy = doneBy;
        this.amount = amount;
        this.reason = reason;
        this.balance = balance;
        this.dateTime = dateTime;
    }

    public WalletHistoryTransaction(){

    }

    public void setWalletHistoryId(String walletHistoryId) {
        this.walletHistoryId = walletHistoryId;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getWalletHistoryId() {
        return walletHistoryId;
    }

    public String getTransaction() {
        return transaction;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public int getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public int getBalance() {
        return balance;
    }

    public String getDateTime() {
        return dateTime;
    }

}
