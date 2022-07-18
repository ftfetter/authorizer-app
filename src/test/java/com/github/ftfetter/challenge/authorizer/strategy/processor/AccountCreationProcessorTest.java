package com.github.ftfetter.challenge.authorizer.strategy.processor;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.service.AccountService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountCreationProcessorTest {

    private AccountCreationProcessor accountCreationProcessor;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        accountCreationProcessor = new AccountCreationProcessor(new Gson(), accountService);
    }

    @Test
    @DisplayName("When the string is null, then should return false")
    void isEligibleTestWhenStringIsNull() {
        Boolean actual = accountCreationProcessor.isEligible(null);

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is empty, then should return false")
    void isEligibleTestWhenStringIsEmpty() {
        Boolean actual = accountCreationProcessor.isEligible("");

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is not eligible, then should return false")
    void isEligibleTestWhenStringIsNotEligible() {
        Boolean actual = accountCreationProcessor.isEligible(buildNotEligibleJson());

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is eligible, then should return true")
    void isEligibleTestWhenStringIsEligible() {
        Boolean actual = accountCreationProcessor.isEligible(buildAccountCreationJson());

        assertTrue(actual);
    }

    @Test
    @DisplayName("When JSON is valid and the service doesn't return any violation, then should return a JSON without violations")
    void processTestWhenServiceReturnsEmptyViolation() {
        when(accountService.createAccount(any(Account.class)))
                .thenReturn(buildAuthorizationWithoutViolations());

        String actualResponse = accountCreationProcessor.process().apply(buildAccountCreationJson());

        assertEquals(buildExpectedResponseJsonWithoutViolations(), actualResponse);
    }

    @Test
    @DisplayName("When JSON is valid and the service returns violations, then should return a JSON with violations")
    void processTestWhenServiceReturnsViolations() {
        when(accountService.createAccount(any(Account.class)))
                .thenReturn(buildAuthorizationWithViolations());

        String actualResponse = accountCreationProcessor.process().apply(buildExpectedResponseJsonWithViolations());

        assertEquals(buildExpectedResponseJsonWithViolations(), actualResponse);
    }

    private Authorization buildAuthorizationWithoutViolations() {
        return new Authorization(Account.AccountBuilder.anAccount()
                .withActiveCard(true)
                .withAvailableLimit(100)
                .build(),
                Collections.emptyList());
    }

    private Authorization buildAuthorizationWithViolations() {
        return new Authorization(Account.AccountBuilder.anAccount()
                .withActiveCard(true)
                .withAvailableLimit(100)
                .build(),
                Collections.singletonList("VIOLATION-TEST"));
    }

    private String buildAccountCreationJson() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100}}";
    }

    private String buildNotEligibleJson() {
        return "{\"transaction\":{\"merchant\":\"Burger King\",\"amount\":20,\"time\":\"2019-02-13T10:00:00.000Z\"}}";
    }

    private String buildExpectedResponseJsonWithoutViolations() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}";
    }

    private String buildExpectedResponseJsonWithViolations() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[\"VIOLATION-TEST\"]}";
    }
}