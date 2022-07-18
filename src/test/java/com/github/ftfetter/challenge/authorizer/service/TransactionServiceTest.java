package com.github.ftfetter.challenge.authorizer.service;

import com.github.ftfetter.challenge.authorizer.chain.OperationValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private TransactionService transactionService;
    private OperationValidator operationValidator;
    private ActiveAccount activeAccount;
    private AccountValidator accountValidator;
    private TransactionValidator transactionValidator;

    @BeforeEach
    void setUp() {
        operationValidator = mock(OperationValidator.class);
        activeAccount = mock(ActiveAccount.class);
        transactionService = new TransactionService(activeAccount, operationValidator);

        accountValidator = mock(AccountValidator.class);
        transactionValidator = mock(TransactionValidator.class);
    }

    @Test
    @DisplayName("When the validations don't return any violation, then should authorize the Transaction and return an Authorization without violations")
    void authorizeTransactionTestWhenValidationsDoesNotReturnViolations() {
        when(operationValidator.getAccountValidatorsForTransactionAuthorization())
                .thenReturn(Optional.of(accountValidator));
        when(operationValidator.getTransactionValidatorsForTransactionAuthorization())
                .thenReturn(Optional.of(transactionValidator));
        when(accountValidator.validate(any(ActiveAccount.class)))
                .thenReturn(Collections.emptyList());
        when(transactionValidator.validate(any(ActiveAccount.class), any(Transaction.class)))
                .thenReturn(Collections.emptyList());
        when(activeAccount.getAccount())
                .thenReturn(buildInitializedAccount());
        when(activeAccount.getTransactions())
                .thenReturn(new ArrayList<>());

        Authorization actualAuthorization = transactionService.authorizeTransaction(buildTransaction());

        verify(activeAccount).setAccount(any(Account.class));
        verify(activeAccount).setTransactions(anyList());
        assertEquals(buildExpectedAuthorizationWithoutViolations(), actualAuthorization);
    }

    @Test
    @DisplayName("When the validations return violations, then should not authorize the Transaction and return an Authorization with the violations")
    void createAccountTestWhenValidationsReturnViolations() {
        when(operationValidator.getAccountValidatorsForTransactionAuthorization())
                .thenReturn(Optional.of(accountValidator));
        when(operationValidator.getTransactionValidatorsForTransactionAuthorization())
                .thenReturn(Optional.of(transactionValidator));
        when(accountValidator.validate(any(ActiveAccount.class)))
                .thenReturn(Collections.singletonList("TEST-ACCOUNT-VIOLATION"));
        when(transactionValidator.validate(any(ActiveAccount.class), any(Transaction.class)))
                .thenReturn(Collections.singletonList("TEST-TRANSACTION-VIOLATION"));
        when(activeAccount.getAccount())
                .thenReturn(buildInitializedAccount());

        Authorization actualAuthorization = transactionService.authorizeTransaction(buildTransaction());

        verify(activeAccount, never()).setAccount(any(Account.class));
        verify(activeAccount, never()).setTransactions(anyList());
        assertEquals(buildExpectedAuthorizationWithViolations(), actualAuthorization);
    }

    private Account buildInitializedAccount() {
        return Account.AccountBuilder.anAccount()
                .withActiveCard(true)
                .withAvailableLimit(100)
                .build();
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                .withAmount(42)
                .withMerchant("Restaurant at the end of Universe")
                .withTime(ZonedDateTime.parse("2012-12-21T21:00:00.000Z"))
                .build();
    }

    private Authorization buildExpectedAuthorizationWithoutViolations() {
        return new Authorization(buildInitializedAccount(), Collections.emptyList());
    }

    private Authorization buildExpectedAuthorizationWithViolations() {
        return new Authorization(buildInitializedAccount(), Arrays.asList("TEST-ACCOUNT-VIOLATION", "TEST-TRANSACTION-VIOLATION"));
    }
}