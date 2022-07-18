package com.github.ftfetter.challenge.authorizer.service;

import com.github.ftfetter.challenge.authorizer.chain.OperationValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Authorization;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final ActiveAccount activeAccount;
    private final OperationValidator operationValidator;

    public TransactionService(ActiveAccount activeAccount, OperationValidator operationValidator) {
        this.activeAccount = activeAccount;
        this.operationValidator = operationValidator;
    }

    public Authorization authorizeTransaction(Transaction transaction) {
        logger.info("Starting validations for authorization of transaction");
        List<String> violations = validateAll(activeAccount, transaction);
        if (violations.isEmpty()) {
            logger.info("No violations detected, authorizing transaction");
            List<Transaction> authorizedTransactions = activeAccount.getTransactions();
            authorizedTransactions.add(transaction);
            activeAccount.setAccount(discountAmountFromLimit(activeAccount.getAccount(), transaction));
            activeAccount.setTransactions(authorizedTransactions);
        }
        return new Authorization(activeAccount.getAccount(), violations);
    }

    private List<String> validateAll(ActiveAccount activeAccount, Transaction transaction) {
        List<String> accountViolations = operationValidator.getAccountValidatorsForTransactionAuthorization()
                .map(validator -> validator.validate(activeAccount))
                .orElse(List.of());
        List<String> transactionViolations = operationValidator.getTransactionValidatorsForTransactionAuthorization()
                .map(validator -> validator.validate(activeAccount, transaction))
                .orElse(List.of());

        return Stream.concat(accountViolations.stream(), transactionViolations.stream())
                .collect(Collectors.toList());
    }

    private Account discountAmountFromLimit(Account account, Transaction transaction) {
        return Account.AccountBuilder.anAccount()
                .withActiveCard(account.getActiveCard())
                .withAvailableLimit(account.getAvailableLimit() - transaction.getAmount())
                .build();
    }
}
