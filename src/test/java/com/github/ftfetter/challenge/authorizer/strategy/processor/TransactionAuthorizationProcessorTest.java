package com.github.ftfetter.challenge.authorizer.strategy.processor;

import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.service.TransactionService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionAuthorizationProcessorTest {

    private TransactionAuthorizationProcessor transactionAuthorizationProcessor;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        transactionAuthorizationProcessor = new TransactionAuthorizationProcessor(new Gson(), transactionService);
    }

    @Test
    @DisplayName("When the string is null, then should return false")
    void isEligibleTestWhenStringIsNull() {
        Boolean actual = transactionAuthorizationProcessor.isEligible(null);

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is empty, then should return false")
    void isEligibleTestWhenStringIsEmpty() {
        Boolean actual = transactionAuthorizationProcessor.isEligible("");

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is not eligible, then should return false")
    void isEligibleTestWhenStringIsNotEligible() {
        Boolean actual = transactionAuthorizationProcessor.isEligible(buildNotEligibleJson());

        assertFalse(actual);
    }

    @Test
    @DisplayName("When the string is eligible, then should return true")
    void isEligibleTestWhenStringIsEligible() {
        Boolean actual = transactionAuthorizationProcessor.isEligible(buildTransactionAuthorizationJson());

        assertTrue(actual);
    }

    @Test
    @DisplayName("When JSON is valid and the service doesn't return any violation, then should return a JSON without violations")
    void processTestWhenServiceReturnsEmptyViolation() {
        when(transactionService.authorizeTransaction(buildTransactionConverted()))
                .thenReturn(buildAuthorizationWithoutViolations());

        String actualResponse = transactionAuthorizationProcessor.process().apply(buildTransactionAuthorizationJson());

        assertEquals(buildExpectedResponseJsonWithoutViolations(), actualResponse);
    }

    @Test
    @DisplayName("When JSON is valid and the service returns violations, then should return a JSON with the violations")
    void processTestWhenServiceReturnsViolations() {
        when(transactionService.authorizeTransaction(buildTransactionConverted()))
                .thenReturn(buildAuthorizationWithViolations());

        String actualResponse = transactionAuthorizationProcessor.process().apply(buildTransactionAuthorizationJson());

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

    private Transaction buildTransactionConverted() {
        return Transaction.builder()
                .withAmount(42)
                .withMerchant("Restaurant at the End of the Universe")
                .withTime(ZonedDateTime.parse("2012-12-21T12:00:00.000Z"))
                .build();
    }

    private String buildTransactionAuthorizationJson() {
        return "{\"transaction\":{\"merchant\":\"Restaurant at the End of the Universe\",\"amount\":42,\"time\":\"2012-12-21T12:00:00.000Z\"}}";
    }

    private String buildNotEligibleJson() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100}}";
    }

    private String buildExpectedResponseJsonWithoutViolations() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}";
    }

    private String buildExpectedResponseJsonWithViolations() {
        return "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[\"VIOLATION-TEST\"]}";
    }
}