package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecentTransactionsTransactionValidatorTest {

    private RecentTransactionsTransactionValidator recentTransactionsValidator;

    @BeforeEach
    void setUp() {
        recentTransactionsValidator = new RecentTransactionsTransactionValidator();
    }

    @Test
    @DisplayName("When there is no active account, then shouldn't return a violation")
    void validateTestWithoutActiveAccount() {
        List<String> actualViolations = recentTransactionsValidator.validate(null, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account is not initialized, then shouldn't return a violation")
    void validateTestWithActiveAccountWithoutTransactions() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(null);

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has no transactions and no new transaction, then shouldn't return a violation")
    void validateTestWithActiveAccountWithEmptyTransactionsWithoutNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(Collections.emptyList());

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has no transactions, then shouldn't return a violation")
    void validateTestWithActiveAccountWithEmptyTransactionsWithNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(Collections.emptyList());
        Transaction transaction = buildTransaction();

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, transaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has recent transactions but no new transaction, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithoutNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(buildAccountTransactions());

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has transactions but none recent, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithNewTransactionAndNoMuchRecentTransactions() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(buildAccountTransactions());
        Transaction transaction = buildTransaction();

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, transaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When there is three or more recent transactions, then should return a violation")
    void validateTestWithActiveAccountWithTransactionsWithNewTransactionAndTooMuchRecentTransactions() {
        List<Transaction> listWithTooMuchRecentTransactions = buildAccountTransactions();
        listWithTooMuchRecentTransactions.add(buildTransaction());
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(listWithTooMuchRecentTransactions);
        Transaction transaction = buildTransaction();

        List<String> actualViolations = recentTransactionsValidator.validate(activeAccount, transaction);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.HIGH_FREQUENCY_SMALL_INTERVAL.getViolation()));
    }

    private List<Transaction> buildAccountTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(buildTransaction());
        transactions.add(buildTransaction());

        return transactions;
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                .withAmount(42)
                .withMerchant("Restaurant at the end of Universe")
                .withTime(ZonedDateTime.parse("2012-12-21T21:00:00.000Z"))
                .build();
    }
}