package com.github.ftfetter.challenge.authorizer.model;

import java.util.List;
import java.util.Objects;

public class Authorization {

    private Account account;
    private List<String> violations;

    public Authorization(Account account, List<String> violations) {
        this.account = account;
        this.violations = violations;
    }

    public Account getAccount() {
        return account;
    }

    public List<String> getViolations() {
        return violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authorization that = (Authorization) o;
        return Objects.equals(account, that.account) &&
                Objects.equals(violations, that.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, violations);
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "account=" + account +
                ", violations=" + violations +
                '}';
    }
}
