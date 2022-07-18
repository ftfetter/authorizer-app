package com.github.ftfetter.challenge.authorizer.chain.validator.account;

import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.ACCOUNT_ALREADY_INITIALIZED;

public class AccountNotCreatedValidator extends AccountValidator {

    @Override
    public List<String> validate(ActiveAccount active) {
        return Optional.ofNullable(active)
                .filter(this::isAccountCreated)
                .map(account -> validateNextWithViolation(active, ACCOUNT_ALREADY_INITIALIZED))
                .orElse(validateNext(active));
    }

    private Boolean isAccountCreated(ActiveAccount activeAccount) {
        return Optional.ofNullable(activeAccount.getAccount())
                .filter(account -> account.getAvailableLimit() != null)
                .map(Account::getActiveCard)
                .orElse(false);
    }
}
