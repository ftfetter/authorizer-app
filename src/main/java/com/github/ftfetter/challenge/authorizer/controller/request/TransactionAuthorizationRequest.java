package com.github.ftfetter.challenge.authorizer.controller.request;

import java.util.Objects;

public class TransactionAuthorizationRequest {

    private TransactionRequest transaction;

    public TransactionRequest getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionRequest transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionAuthorizationRequest that = (TransactionAuthorizationRequest) o;
        return Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction);
    }

    @Override
    public String toString() {
        return "TransactionCreationRequest{" +
                "transaction=" + transaction +
                '}';
    }
}
