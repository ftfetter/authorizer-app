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

class CardActiveValidatorTest {

    private CardActiveValidator cardActiveValidator;

    @BeforeEach
    void setUp() {
        cardActiveValidator = new CardActiveValidator();
    }

    @Test
    @DisplayName("When there is no active account, then should return a violation")
    void validateTestWithoutActiveAccount() {
        List<String> actualViolations = cardActiveValidator.validate(null);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.CARD_NOT_ACTIVE.getViolation()));
    }

    @Test
    @DisplayName("When the account is not initialized, then should return a violation")
    void validateTestWithActiveAccountWithoutAccount() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(null);
        activeAccount.setTransactions(null);

        List<String> actualViolations = cardActiveValidator.validate(activeAccount);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.CARD_NOT_ACTIVE.getViolation()));
    }

    @Test
    @DisplayName("When the account has no active card, then should return a violation")
    void validateTestWithActiveAccountWithAccountWithoutActiveCard() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccountWithoutActiveCard());
        activeAccount.setTransactions(null);

        List<String> actualViolations = cardActiveValidator.validate(activeAccount);

        assertEquals(1, actualViolations.size());
        assertTrue(actualViolations.contains(ViolationType.CARD_NOT_ACTIVE.getViolation()));
    }

    @Test
    @DisplayName("When the account has an active card, then shouldn't return a violation")
    void validateTestWithActiveAccountWithAccountWithActiveCard() {
        ActiveAccount activeAccount = ActiveAccount.getInstance();
        activeAccount.setAccount(buildAccountWithActiveCard());
        activeAccount.setTransactions(null);

        List<String> actualViolations = cardActiveValidator.validate(activeAccount);

        assertTrue(actualViolations.isEmpty());
    }

    private Account buildAccountWithoutActiveCard() {
        Account account = new Account();
        account.setActiveCard(false);

        return account;
    }

    private Account buildAccountWithActiveCard() {
        Account account = new Account();
        account.setActiveCard(true);

        return account;
    }
}