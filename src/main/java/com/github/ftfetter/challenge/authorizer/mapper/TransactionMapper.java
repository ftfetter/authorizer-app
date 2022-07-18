package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.controller.request.TransactionRequest;
import com.github.ftfetter.challenge.authorizer.model.Transaction;

import java.time.ZonedDateTime;
import java.util.Optional;

public final class TransactionMapper {

    private TransactionMapper() {
    }

    public static Transaction fromRequest(TransactionRequest request) {
        return Optional.ofNullable(request)
                .map(nonNullRequest -> Transaction.builder()
                        .withAmount(nonNullRequest.getAmount())
                        .withMerchant(nonNullRequest.getMerchant())
                        .withTime(ZonedDateTime.parse(nonNullRequest.getTime()))
                        .build())
                .orElse(Transaction.builder().build());
    }
}
