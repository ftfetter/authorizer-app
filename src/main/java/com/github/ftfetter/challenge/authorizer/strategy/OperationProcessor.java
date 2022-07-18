package com.github.ftfetter.challenge.authorizer.strategy;

import com.github.ftfetter.challenge.authorizer.strategy.processor.AccountCreationProcessor;
import com.github.ftfetter.challenge.authorizer.strategy.processor.ProcessorStrategy;
import com.github.ftfetter.challenge.authorizer.strategy.processor.TransactionAuthorizationProcessor;

import java.util.stream.Stream;

public class OperationProcessor {

    private final AccountCreationProcessor accountCreationProcessor;
    private final TransactionAuthorizationProcessor transactionAuthorizationProcessor;

    public OperationProcessor(AccountCreationProcessor accountCreationProcessor, TransactionAuthorizationProcessor transactionAuthorizationProcessor) {
        this.accountCreationProcessor = accountCreationProcessor;
        this.transactionAuthorizationProcessor = transactionAuthorizationProcessor;
    }

    public Stream<ProcessorStrategy> getProcessors() {
        return Stream.of(accountCreationProcessor, transactionAuthorizationProcessor);
    }
}
