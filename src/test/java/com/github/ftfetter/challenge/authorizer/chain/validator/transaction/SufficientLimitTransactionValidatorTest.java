package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SufficientLimitTransactionValidatorTest {

    private SufficientLimitTransactionValidator sufficientLimitValidator;

    @BeforeEach
    void setUp() {
        sufficientLimitValidator = new SufficientLimitTransactionValidator();
    }

    @Test
    @DisplayName("When there is no active account, then shouldn't return a violation")
    void validateTestWithoutActiveAccount() {
        List<String> actualViolations = sufficientLimitValidator.validate(null, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account is not initialized, then shouldn't return a violation")
    void validateTestWithActiveAccountWithoutAccount() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(null);

        List<String> actualViolations = sufficientLimitValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When there is an account but no new transaction, then shouldn't return a violation")
    void validateTestWithActiveAccountWithAccountWithoutTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccount());
        activeAccount.setTransactions(null);

        List<String> actualViolations = sufficientLimitValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has enough limit, then shouldn't return a violation")
    void validateTestWithActiveAccountWithAccountWithValidTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccount());
        activeAccount.setTransactions(null);

        Transaction newTransaction = Transaction.builder()
                .withAmount(42)
                .withMerchant("")
                .withTime(ZonedDateTime.now())
                .build();

        List<String> actualViolations = sufficientLimitValidator.validate(activeAccount, newTransaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account doesn't have enough limit, then should return a violation")
    void validateTestWithActiveAccountWithAccountWithTooExpensiveTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccount());
        activeAccount.setTransactions(null);

        Transaction newTransaction = Transaction.builder()
                .withAmount(100)
                .withMerchant("")
                .withTime(ZonedDateTime.now())
                .build();

        List<String> actualViolations = sufficientLimitValidator.validate(activeAccount, newTransaction);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.INSUFFICIENT_LIMIT.getViolation()));
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setAvailableLimit(42);

        return account;
    }
}