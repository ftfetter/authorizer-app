package com.github.ftfetter.challenge.authorizer.service;

import com.github.ftfetter.challenge.authorizer.chain.OperationValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final ActiveAccount activeAccount;
    private final OperationValidator operationValidator;

    public AccountService(ActiveAccount activeAccount, OperationValidator operationValidator) {
        this.activeAccount = activeAccount;
        this.operationValidator = operationValidator;
    }

    public Authorization createAccount(Account account) {
        logger.info("Starting validations for creating account");
        List<String> violations = operationValidator.getAccountValidatorsForAccountCreation()
                .map(validation -> validation.validate(activeAccount))
                .orElse(List.of());
        if (violations.isEmpty()) {
            logger.info("No violations detected, creating new account");
            activeAccount.setAccount(account);
            activeAccount.setTransactions(new ArrayList<>());
        }
        return new Authorization(activeAccount.getAccount(), violations);
    }
}
