package com.github.ftfetter.challenge.authorizer.controller;

import com.github.ftfetter.challenge.authorizer.strategy.OperationProcessor;

import java.util.function.Function;

public class AuthorizerController {

    private final AuthorizerIO authorizerIO;
    private final OperationProcessor operationProcessor;

    public AuthorizerController(AuthorizerIO authorizerIO, OperationProcessor operationProcessor) {
        this.authorizerIO = authorizerIO;
        this.operationProcessor = operationProcessor;
    }

    public void run() {
        authorizerIO.writeOutput()
                .accept(executeProcess()
                        .apply(authorizerIO.readInput().get()));
    }

    private Function<String, String> executeProcess() {
        return jsonString -> operationProcessor.getProcessors()
                .filter(processor -> processor.isEligible(jsonString))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Not possible to identify the operation."))
                .process()
                .apply(jsonString);
    }
}
