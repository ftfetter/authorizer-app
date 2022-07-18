package com.github.ftfetter.challenge.authorizer.chain.validator.transaction;

import com.github.ftfetter.challenge.authorizer.chain.validator.TransactionValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.INSUFFICIENT_LIMIT;

public class SufficientLimitTransactionValidator extends TransactionValidator {

    @Override
    public List<String> validate(ActiveAccount active, Transaction transaction) {
        return Optional.ofNullable(active)
                .map(ActiveAccount::getAccount)
                .filter(account -> isSufficientLimit(account, transaction))
                .map(account -> validateNextWithViolation(active, transaction, INSUFFICIENT_LIMIT))
                .orElse(validateNext(active, transaction));
    }

    private Boolean isSufficientLimit(Account account, Transaction transaction) {
        return Optional.ofNullable(transaction)
                .filter(nonNullTransaction -> nonNullTransaction.getAmount() != null)
                .map(nonNullTransaction -> account.getAvailableLimit())
                .map(availableLimit -> (availableLimit - transaction.getAmount()) < 0)
                .orElse(false);

    }
}
