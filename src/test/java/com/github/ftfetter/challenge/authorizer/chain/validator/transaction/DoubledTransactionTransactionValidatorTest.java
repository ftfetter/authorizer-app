package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubledTransactionTransactionValidatorTest {

    private DoubledTransactionTransactionValidator doubledTransactionValidator;

    @BeforeEach
    void setUp() {
        doubledTransactionValidator = new DoubledTransactionTransactionValidator();
    }

    @Test
    @DisplayName("When there is no active account, then shouldn't return a violation")
    void validateTestWithoutActiveAccount() {
        List<String> actualViolations = doubledTransactionValidator.validate(null,null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account is not initialized, then shouldn't return a violation")
    void validateTestWithActiveAccountWithoutTransactions() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(null);

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has no transactions and no new transaction, then shouldn't return a violation")
    void validateTestWithActiveAccountWithEmptyTransactionsWithoutNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(Collections.emptyList());

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has no transactions, then shouldn't return a violation")
    void validateTestWithActiveAccountWithEmptyTransactionsWithNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(Collections.emptyList());
        Transaction newTransaction = buildTransaction();

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, newTransaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account has recent transactions but no new transaction, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithoutNewTransaction() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(List.of(buildTransaction()));

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the new transaction is from another merchant, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithNewTransactionFromOtherMerchant() {
        Transaction firstTransaction = buildTransaction();
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(List.of(firstTransaction));

        Transaction newTransaction = Transaction.builder()
                .withAmount(firstTransaction.getAmount())
                .withMerchant("OTHER MERCHANT")
                .withTime(firstTransaction.getTime())
                .build();

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, newTransaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the new transaction does not have the same amount, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithNewTransactionOfAnotherValue() {
        Transaction firstTransaction = buildTransaction();
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(List.of(firstTransaction));

        Transaction newTransaction = Transaction.builder()
                .withAmount(firstTransaction.getAmount() + 10)
                .withMerchant(firstTransaction.getMerchant())
                .withTime(firstTransaction.getTime())
                .build();

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, newTransaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the new transaction is doubled, but two minutes later, then shouldn't return a violation")
    void validateTestWithActiveAccountWithTransactionsWithNewTransactionAfterTwoMinutes() {
        Transaction firstTransaction = buildTransaction();
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(List.of(firstTransaction));

        Transaction newTransaction = Transaction.builder()
                .withAmount(firstTransaction.getAmount())
                .withMerchant(firstTransaction.getMerchant())
                .withTime(firstTransaction.getTime().plusMinutes(2))
                .build();

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, newTransaction);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the new transaction is doubled and is within two minutes from the last one, then should return a violation")
    void validateTestWithActiveAccountWithTransactionsWithDoubledNewTransaction() {
        Transaction firstTransaction = buildTransaction();
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(List.of(firstTransaction));

        Transaction newTransaction = Transaction.builder()
                .withAmount(firstTransaction.getAmount())
                .withMerchant(firstTransaction.getMerchant())
                .withTime(firstTransaction.getTime().plusMinutes(1))
                .build();

        List<String> actualViolations = doubledTransactionValidator.validate(activeAccount, newTransaction);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.DOUBLED_TRANSACTION.getViolation()));
    }

    private Transaction buildTransaction() {
        return Transaction.builder()
                .withAmount(42)
                .withMerchant("Restaurant at the end of Universe")
                .withTime(ZonedDateTime.parse("2012-12-21T21:00:00.000Z"))
                .build();
    }
}