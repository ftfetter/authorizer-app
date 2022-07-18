package com.github.ftfetter.challenge.authorizer.chain;

import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;

import java.util.Optional;
import java.util.Set;

public class OperationValidator {

    private final Set<AccountValidator> accountCreationValidators;
    private final Set<AccountValidator> transactionAccountValidators;
    private final Set<TransactionValidator> transactionValidators;

    public OperationValidator(Set<AccountValidator> accountCreationValidators,
                              Set<AccountValidator> transactionAccountValidators,
                              Set<TransactionValidator> transactionValidators) {
        this.accountCreationValidators = accountCreationValidators;
        this.transactionAccountValidators = transactionAccountValidators;
        this.transactionValidators = transactionValidators;
    }

    public Optional<AccountValidator> getAccountValidatorsForAccountCreation() {
        return accountCreationValidators.stream()
                .reduce((validator1, validator2) -> validator2.linkWith(validator1));
    }

    public Optional<AccountValidator> getAccountValidatorsForTransactionAuthorization() {
        return transactionAccountValidators.stream()
                .reduce((validator1, validator2) -> validator2.linkWith(validator1));
    }

    public Optional<TransactionValidator> getTransactionValidatorsForTransactionAuthorization() {
        return transactionValidators.stream()
                        .reduce((validator1, validator2) -> validator2.linkWith(validator1));
    }
}
