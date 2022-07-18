package com.github.ftfetter.challenge.authorizer.singleton;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ActiveAccount {

    private static ActiveAccount instance = new ActiveAccount();

    private Account account;
    private List<Transaction> transactions;

    private ActiveAccount() {
        this.account = new Account();
        this.transactions = new ArrayList<>();
    }

    public static ActiveAccount getInstance() {
        return instance;
    }

    public Account getAccount() {
        return account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "ActiveAccount{" +
                "account=" + account +
                ", transactions=" + transactions +
                '}';
    }
}
