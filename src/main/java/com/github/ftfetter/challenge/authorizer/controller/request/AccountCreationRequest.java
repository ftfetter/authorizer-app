package com.github.ftfetter.challenge.authorizer.controller.request;

import java.util.Objects;

public class AccountCreationRequest {

    private AccountRequest account;

    public AccountRequest getAccount() {
        return account;
    }

    public void setAccount(AccountRequest account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCreationRequest that = (AccountCreationRequest) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account);
    }

    @Override
    public String toString() {
        return "AccountRequest{" +
                "account=" + account +
                '}';
    }
}
