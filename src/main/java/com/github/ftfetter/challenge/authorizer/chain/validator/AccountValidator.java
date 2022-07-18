package com.github.ftfetter.challenge.authorizer.chain.validator;

import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;
import com.github.ftfetter.challenge.authorizer.type.ViolationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AccountValidator {

    private AccountValidator next;

    public AccountValidator linkWith(AccountValidator next) {
        this.next = next;
        return next;
    }

    public abstract List<String> validate(ActiveAccount active);

    protected List<String> validateNext(ActiveAccount active) {
        return Optional.ofNullable(next)
                .map(validation -> validation.validate(active))
                .orElse(new ArrayList<>());
    }

    protected List<String> validateNextWithViolation(ActiveAccount active, ViolationType violation) {
        List<String> validations = validateNext(active);
        validations.add(violation.getViolation());
        return validations;
    }
}
