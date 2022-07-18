package com.github.ftfetter.challenge.authorizer.chain.validator.account;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountNotCreatedValidatorTest {

    private AccountNotCreatedValidator accountNotCreatedValidator;

    @BeforeEach
    void setUp() {
        accountNotCreatedValidator = new AccountNotCreatedValidator();
    }

    @Test
    @DisplayName("When there is no active account, then shouldn't return a violation")
    void validateTestWithoutActiveAccount() {
        List<String> actualViolations = accountNotCreatedValidator.validate(null);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account is not initialized, then shouldn't return a violation")
    void validateTestWithActiveAccountWithoutInitializedAccount() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(null);

        List<String> actualViolations = accountNotCreatedValidator.validate(activeAccount);

        assertTrue(actualViolations.isEmpty());
    }

    @Test
    @DisplayName("When the account is initialized, then should return a violation")
    void validateTestWithActiveAccountWithInitializedAccount() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccount());
        activeAccount.setTransactions(null);

        List<String> actualViolations = accountNotCreatedValidator.validate(activeAccount);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getViolation()));
    }

    private Account buildAccount() {
        return Account.AccountBuilder.anAccount()
                .withActiveCard(true)
                .withAvailableLimit(100)
                .build();
    }
}