package com.github.ftfetter.challenge.authorizer.strategy.processor;

import com.github.ftfetter.challenge.authorizer.controller.request.TransactionAuthorizationRequest;
import com.github.ftfetter.challenge.authorizer.controller.response.AuthorizationResponse;
import com.github.ftfetter.challenge.authorizer.mapper.AuthorizationMapper;
import com.github.ftfetter.challenge.authorizer.mapper.TransactionMapper;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.service.TransactionService;
import com.google.gson.Gson;

import java.util.Optional;
import java.util.function.Function;

public class TransactionAuthorizationProcessor implements ProcessorStrategy {

    private static final String IDENTIFIER = "transaction";

    private final Gson gson;
    private final TransactionService transactionService;

    public TransactionAuthorizationProcessor(Gson gson, TransactionService transactionService) {
        this.gson = gson;
        this.transactionService = transactionService;
    }

    @Override
    public Boolean isEligible(String input) {
        return Optional.ofNullable(input)
                .filter(inputString -> inputString.contains("{\"" + IDENTIFIER + "\":"))
                .isPresent();
    }

    @Override
    public Function<String, String> process() {
        return convertFromJson()
                .andThen(mapToModel()
                        .andThen(executeService()
                                .andThen(mapToResponse()
                                        .andThen(convertToJson()))));
    }

    private Function<String, TransactionAuthorizationRequest> convertFromJson() {
        return stringJson -> gson.fromJson(stringJson, TransactionAuthorizationRequest.class);
    }

    private Function<TransactionAuthorizationRequest, Transaction> mapToModel() {
        return transactionAuthorizationRequest -> TransactionMapper.fromRequest(transactionAuthorizationRequest.getTransaction());
    }

    private Function<Transaction, Authorization> executeService() {
        return transactionService::authorizeTransaction;
    }

    private Function<Authorization, AuthorizationResponse> mapToResponse() {
        return AuthorizationMapper::toResponse;
    }

    private Function<AuthorizationResponse, String> convertToJson() {
        return gson::toJson;
    }
}
