package com.github.ftfetter.challenge.authorizer.service;

import com.github.ftfetter.challenge.authorizer.chain.OperationValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private AccountService accountService;
    private OperationValidator operationValidator;
    private ActiveAccount activeAccount;
    private AccountValidator accountValidator;

    @BeforeEach
    void setUp() {
        operationValidator = mock(OperationValidator.class);
        activeAccount = mock(ActiveAccount.class);
        accountService = new AccountService(activeAccount, operationValidator);

        accountValidator = mock(AccountValidator.class);
    }

    @Test
    @DisplayName("When the validations don't return any violation, then should setup the ActiveAccount and return an Authorization without violations")
    void createAccountTestWhenValidationsDoesNotReturnViolations() {
        when(operationValidator.getAccountValidatorsForAccountCreation())
                .thenReturn(Optional.of(accountValidator));
        when(accountValidator.validate(any(ActiveAccount.class)))
                .thenReturn(Collections.emptyList());
        when(activeAccount.getAccount())
                .thenReturn(new Account());

        Authorization actualAuthorization = accountService.createAccount(new Account());

        verify(activeAccount).setAccount(any(Account.class));
        verify(activeAccount).setTransactions(anyList());
        assertEquals(buildExpectedAuthorizationWithoutViolations(), actualAuthorization);
    }

    @Test
    @DisplayName("When the validations return violations, then should not setup the ActiveAccount and return an Authorization with the violations")
    void createAccountTestWhenValidationsReturnViolations() {
        when(operationValidator.getAccountValidatorsForAccountCreation())
                .thenReturn(Optional.of(accountValidator));
        when(accountValidator.validate(any(ActiveAccount.class)))
                .thenReturn(Collections.singletonList("TEST-VIOLATION"));
        when(activeAccount.getAccount())
                .thenReturn(new Account());

        Authorization actualAuthorization = accountService.createAccount(new Account());

        verify(activeAccount, never()).setAccount(any(Account.class));
        verify(activeAccount, never()).setTransactions(anyList());
        assertEquals(buildExpectedAuthorizationWithViolations(), actualAuthorization);
    }

    private Authorization buildExpectedAuthorizationWithoutViolations() {
        return new Authorization(new Account(), Collections.emptyList());
    }

    private Authorization buildExpectedAuthorizationWithViolations() {
        return new Authorization(new Account(), Collections.singletonList("TEST-VIOLATION"));
    }
}