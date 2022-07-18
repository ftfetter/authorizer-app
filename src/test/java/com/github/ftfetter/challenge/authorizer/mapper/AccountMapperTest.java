package com.github.ftfetter.challenge.authorizer.mapper;

import com.github.ftfetter.challenge.authorizer.controller.request.AccountRequest;
import com.github.ftfetter.challenge.authorizer.controller.response.AccountResponse;
import com.github.ftfetter.challenge.authorizer.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    @Test
    @DisplayName("When null is passed, then should return an empty Account")
    void fromRequestTestWhenNullIsPassed() {
        Account expectedAccount = new Account();

        Account actualAccount = AccountMapper.fromRequest(null);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    @DisplayName("When an AccountRequest is passed, then should return an Account with the AccountRequest data")
    void fromRequestTestWhenAccountRequestIsPassed() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setActiveCard(true);
        accountRequest.setAvailableLimit(100);
        Account expectedAccount = buildExpectedAccount(accountRequest);

        Account actualAccount = AccountMapper.fromRequest(accountRequest);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    @DisplayName("When null is passed, then should return an empty AccountResponse")
    void toResponseTestWhenNullIsPassed() {
        AccountResponse expectedAccountResponse = new AccountResponse();

        AccountResponse actualAccountResponse = AccountMapper.toResponse(null);

        assertEquals(expectedAccountResponse, actualAccountResponse);
    }

    @Test
    @DisplayName("When an Account is passed, then should return an AccountResponse with the Account data")
    void toResponseTestWhenAccountIsPassed() {
        Account account = new Account();
        account.setActiveCard(true);
        account.setAvailableLimit(100);
        AccountResponse expectedAccountResponse = buildExpectedAccountResponse(account);

        AccountResponse actualAccountResponse = AccountMapper.toResponse(account);

        assertEquals(expectedAccountResponse, actualAccountResponse);
    }

    private Account buildExpectedAccount(AccountRequest request) {
        return Account.AccountBuilder.anAccount()
                .withActiveCard(request.getActiveCard())
                .withAvailableLimit(request.getAvailableLimit())
                .build();
    }

    private AccountResponse buildExpectedAccountResponse(Account account) {
        return AccountResponse.AccountResponseBuilder.anAccountResponse()
                .withActiveCard(account.getActiveCard())
                .withAvailableLimit(account.getAvailableLimit())
                .build();
    }
}