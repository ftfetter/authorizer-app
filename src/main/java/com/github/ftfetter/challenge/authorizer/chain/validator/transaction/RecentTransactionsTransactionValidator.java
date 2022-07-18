package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.HIGH_FREQUENCY_SMALL_INTERVAL;

public class RecentTransactionsTransactionValidator extends TransactionValidator {

    private static final Integer TIME_LIMIT_IN_MINUTES = 2;
    private static final Integer LIMIT_OF_TRANSACTIONS = 3;

    @Override
    public List<String> validate(ActiveAccount active, Transaction transaction) {
        return Optional.ofNullable(active)
                .map(ActiveAccount::getTransactions)
                .filter(transactions -> hasTooMuchRecentTransactions(transactions, transaction))
                .map(transactions -> validateNextWithViolation(active, transaction, HIGH_FREQUENCY_SMALL_INTERVAL))
                .orElse(validateNext(active, transaction));
    }

    private Boolean hasTooMuchRecentTransactions(List<Transaction> transactions, Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        return transactions.stream()
                .filter(accountTransaction -> accountTransaction.getTime().isAfter(transaction.getTime().minusMinutes(TIME_LIMIT_IN_MINUTES)))
                .count() >= LIMIT_OF_TRANSACTIONS;
    }
}
