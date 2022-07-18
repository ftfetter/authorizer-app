package com.github.ftfetter.challenge.authorizer.strategy.processor;

import com.github.ftfetter.challenge.authorizer.controller.request.AccountCreationRequest;
import com.github.ftfetter.challenge.authorizer.controller.response.AuthorizationResponse;
import com.github.ftfetter.challenge.authorizer.mapper.AccountMapper;
import com.github.ftfetter.challenge.authorizer.mapper.AuthorizationMapper;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.service.AccountService;
import com.google.gson.Gson;

import java.util.Optional;
import java.util.function.Function;

public class AccountCreationProcessor implements ProcessorStrategy {

    private static final String IDENTIFIER = "account";

    private final Gson gson;
    private final AccountService accountService;

    public AccountCreationProcessor(Gson gson, AccountService accountService) {
        this.gson = gson;
        this.accountService = accountService;
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

    private Function<String, AccountCreationRequest> convertFromJson() {
        return stringJson -> gson.fromJson(stringJson, AccountCreationRequest.class);
    }

    private Function<AccountCreationRequest, Account> mapToModel() {
        return accountCreationRequest -> AccountMapper.fromRequest(accountCreationRequest.getAccount());
    }

    private Function<Account, Authorization> executeService() {
        return accountService::createAccount;
    }

    private Function<Authorization, AuthorizationResponse> mapToResponse() {
        return AuthorizationMapper::toResponse;
    }

    private Function<AuthorizationResponse, String> convertToJson() {
        return gson::toJson;
    }
}
