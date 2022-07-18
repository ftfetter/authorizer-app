package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.controller.request.AccountRequest;
import com.github.ftfetter.challenge.authorizer.controller.response.AccountResponse;

import java.util.Optional;

public final class AccountMapper {

    private AccountMapper() {
    }

    public static Account fromRequest(AccountRequest request) {
        return Optional.ofNullable(request)
                .map(nonNullRequest -> Account.AccountBuilder.anAccount()
                        .withActiveCard(request.getActiveCard())
                        .withAvailableLimit(request.getAvailableLimit())
                        .build())
                .orElse(new Account());
    }

    public static AccountResponse toResponse(Account account) {
        return Optional.ofNullable(account)
                .map(nonNullAccount -> AccountResponse.AccountResponseBuilder.anAccountResponse()
                        .withActiveCard(account.getActiveCard())
                        .withAvailableLimit(account.getAvailableLimit())
                        .build())
                .orElse(new AccountResponse());
    }
}
