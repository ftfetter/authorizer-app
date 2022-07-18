package com.github.ftfetter.challenge.authorizer.controller;

import com.github.ftfetter.challenge.authorizer.strategy.OperationProcessor;
import com.github.ftfetter.challenge.authorizer.strategy.processor.AccountCreationProcessor;
import com.github.ftfetter.challenge.authorizer.strategy.processor.ProcessorStrategy;
import com.github.ftfetter.challenge.authorizer.strategy.processor.TransactionAuthorizationProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthorizerControllerTest {

    private AuthorizerController authorizerController;
    private AuthorizerIO authorizerIO;
    private OperationProcessor operationProcessor;
    private AccountCreationProcessor accountCreationProcessor;
    private TransactionAuthorizationProcessor transactionAuthorizationProcessor;

    @BeforeEach
    void setUp() {
        authorizerIO = mock(AuthorizerIO.class);
        operationProcessor = mock(OperationProcessor.class);
        authorizerController = new AuthorizerController(authorizerIO, operationProcessor);

        accountCreationProcessor = mock(AccountCreationProcessor.class);
        transactionAuthorizationProcessor = mock(TransactionAuthorizationProcessor.class);
    }

    @Test
    @DisplayName("When the operation is for account creation, then should only call the account creation process")
    void runTestWithAccountCreation() {
        when(authorizerIO.readInput())
                .thenReturn(buildInputJson());
        when(operationProcessor.getProcessors())
                .thenReturn(buildOperationIdentifiers());
        when(accountCreationProcessor.isEligible(anyString()))
                .thenReturn(true);
        when(transactionAuthorizationProcessor.isEligible(anyString()))
                .thenReturn(false);
        when(accountCreationProcessor.process())
                .thenReturn(buildProcess());
        when(authorizerIO.writeOutput())
                .thenReturn(buildWriteOutput());

        authorizerController.run();

        verify(authorizerIO).writeOutput();
        verify(authorizerIO).readInput();
        verify(accountCreationProcessor).process();
        verify(transactionAuthorizationProcessor, never()).process();
    }

    @Test
    @DisplayName("When the operation is for transaction authorization, then should only call the transaction authorization process")
    void runTestWithTransactionAuthorization() {
        when(authorizerIO.readInput())
                .thenReturn(buildInputJson());
        when(operationProcessor.getProcessors())
                .thenReturn(buildOperationIdentifiers());
        when(transactionAuthorizationProcessor.isEligible(anyString()))
                .thenReturn(true);
        when(accountCreationProcessor.isEligible(anyString()))
                .thenReturn(false);
        when(transactionAuthorizationProcessor.process())
                .thenReturn(buildProcess());
        when(authorizerIO.writeOutput())
                .thenReturn(buildWriteOutput());

        authorizerController.run();

        verify(authorizerIO).writeOutput();
        verify(authorizerIO).readInput();
        verify(transactionAuthorizationProcessor).process();
        verify(accountCreationProcessor, never()).process();
    }

    @Test
    @DisplayName("When the operation can't be identified, then should throw an exception")
    void runTestWithIdentifiedOperation() {
        when(authorizerIO.readInput())
                .thenReturn(buildInputJson());
        when(operationProcessor.getProcessors())
                .thenReturn(buildOperationIdentifiers());
        when(transactionAuthorizationProcessor.isEligible(anyString()))
                .thenReturn(false);
        when(accountCreationProcessor.isEligible(anyString()))
                .thenReturn(false);
        when(authorizerIO.writeOutput())
                .thenReturn(buildWriteOutput());

        assertThrows(RuntimeException.class, () -> authorizerController.run());

        verify(authorizerIO).writeOutput();
        verify(authorizerIO).readInput();
        verify(transactionAuthorizationProcessor, never()).process();
        verify(accountCreationProcessor, never()).process();
    }

    private Supplier<String> buildInputJson() {
        return () -> "{\"test\": true}";
    }

    private Stream<ProcessorStrategy> buildOperationIdentifiers() {
        return Stream.of(accountCreationProcessor, transactionAuthorizationProcessor);
    }

    private Function<String, String> buildProcess() {
        return string -> "SUCCESS TEST";
    }

    private Consumer<String> buildWriteOutput() {
        return System.out::println;
    }
}