package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.DOUBLED_TRANSACTION;

public class DoubledTransactionTransactionValidator extends TransactionValidator {

    private final Integer TIME_LIMIT_IN_MINUTES = 2;

    @Override
    public List<String> validate(ActiveAccount active, Transaction transaction) {
        return Optional.ofNullable(active)
                .map(ActiveAccount::getTransactions)
                .filter(transactions -> isDoubledTransaction(transactions, transaction))
                .map(transactions -> validateNextWithViolation(active, transaction, DOUBLED_TRANSACTION))
                .orElse(validateNext(active, transaction));
    }

    private Boolean isDoubledTransaction(List<Transaction> transactions, Transaction newTransaction) {
        if (newTransaction == null) {
            return false;
        }
        return transactions.stream()
                .filter(accountTransaction -> accountTransaction.getTime().isAfter(newTransaction.getTime().minusMinutes(TIME_LIMIT_IN_MINUTES)))
                .anyMatch(accountTransaction -> accountTransaction.equals(newTransaction));
    }
}
