package com.github.ftfetter.challenge.authorizer.chain.validator.account;

import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.ACCOUNT_NOT_INITIALIZED;

public class AccountInitializedValidator extends AccountValidator {

    @Override
    public List<String> validate(ActiveAccount active) {
        return Optional.ofNullable(active)
                .filter(this::isAccountInitialized)
                .map(account -> validateNext(active))
                .orElse(validateNextWithViolation(active, ACCOUNT_NOT_INITIALIZED));
    }

    private Boolean isAccountInitialized(ActiveAccount activeAccount) {
        return Optional.ofNullable(activeAccount.getAccount())
                .filter(account -> account.getAvailableLimit() != null)
                .map(Account::getActiveCard)
                .orElse(false);
    }
}
