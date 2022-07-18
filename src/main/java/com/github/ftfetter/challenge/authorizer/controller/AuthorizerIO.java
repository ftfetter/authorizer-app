package com.github.ftfetter.challenge.authorizer.controller;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class AuthorizerIO {

    private static final Logger logger = Logger.getLogger(AuthorizerIO.class.getName());

    private final Scanner scanner;

    public AuthorizerIO(Scanner scanner) {
        this.scanner = scanner;
    }

    public Supplier<String> readInput() {
        logger.info("Reading input");
        return scanner::nextLine;
    }

    public Consumer<String> writeOutput() {
        return System.out::println;
    }
}
