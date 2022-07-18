package com.github.ftfetter.challenge.authorizer.chain.validator.account;

import com.github.ftfetter.challenge.authorizer.chain.validator.AccountValidator;
import com.github.ftfetter.challenge.authorizer.model.Account;
import com.github.ftfetter.challenge.authorizer.singleton.ActiveAccount;

import java.util.List;
import java.util.Optional;

import static com.github.ftfetter.challenge.authorizer.type.ViolationType.CARD_NOT_ACTIVE;

public class CardActiveValidator extends AccountValidator {

    @Override
    public List<String> validate(ActiveAccount active) {
        return Optional.ofNullable(active)
                .map(ActiveAccount::getAccount)
                .filter(this::isAccountCardActive)
                .map(account -> validateNext(active))
                .orElse(validateNextWithViolation(active, CARD_NOT_ACTIVE));
    }

    private Boolean isAccountCardActive(Account account) {
        return Optional.ofNullable(account)
                .map(Account::getActiveCard)
                .orElse(false);
    }
}
