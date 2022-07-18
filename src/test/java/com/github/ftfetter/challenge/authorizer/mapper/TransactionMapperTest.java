package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.controller.request.TransactionRequest;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    @Test
    @DisplayName("When null is passed, then should return an empty Transaction")
    void fromRequestTestWhenNullIsPassed() {
        Transaction expectedTransaction = Transaction.builder().build();

        Transaction actualTransaction = TransactionMapper.fromRequest(null);

        assertEquals(expectedTransaction, actualTransaction);
    }

    @Test
    @DisplayName("When a TransactionRequest is passed, should return a Transaction with the TransactionRequest data")
    void fromRequestTestWhenTransactionRequestIsPassed() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(42);
        transactionRequest.setMerchant("Restaurant at the end of Universe");
        transactionRequest.setTime("2012-12-21T21:00:00.000Z");
        Transaction expectedTransaction = buildExpectedTransaction(transactionRequest);

        Transaction actualTransaction = TransactionMapper.fromRequest(transactionRequest);

        assertEquals(expectedTransaction, actualTransaction);
    }

    private Transaction buildExpectedTransaction(TransactionRequest request) {
        return Transaction.builder()
                .withAmount(request.getAmount())
                .withMerchant(request.getMerchant())
                .withTime(ZonedDateTime.parse(request.getTime()))
                .build();
    }
}