package com.github.ftfetter.challenge.authorizer.chain.validator;

import com.github.ftfetter.challenge.authorizer.model.Transaction;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TransactionValidator {

    private TransactionValidator next;

    public TransactionValidator linkWith(TransactionValidator next) {
        this.next = next;
        return next;
    }

    public abstract List<String> validate(ActiveAccount active, Transaction transaction);

    protected List<String> validateNext(ActiveAccount active, Transaction transaction) {
        return Optional.ofNullable(next)
                .map(validation -> validation.validate(active, transaction))
                .orElse(new ArrayList<>());
    }

    protected List<String> validateNextWithViolation(ActiveAccount active, Transaction transaction, ViolationType violation) {
        List<String> validations = validateNext(active, transaction);
        validations.add(violation.getViolation());
        return validations;
    }
}
