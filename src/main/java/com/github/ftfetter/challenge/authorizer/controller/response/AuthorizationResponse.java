package com.github.ftfetter.challenge.authorizer.controller.response;

import java.util.List;
import java.util.Objects;

public class AuthorizationResponse {

    private AccountResponse account;
    private List<String> violations;

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationResponse response = (AuthorizationResponse) o;
        return Objects.equals(account, response.account) &&
                Objects.equals(violations, response.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, violations);
    }

    @Override
    public String toString() {
        return "AuthorizationResponse{" +
                "account=" + account +
                ", violations=" + violations +
                '}';
    }

    public static final class AuthorizationResponseBuilder {
        private AccountResponse account;
        private List<String> violations;

        private AuthorizationResponseBuilder() {
        }

        public static AuthorizationResponseBuilder anAuthorizationResponse() {
            return new AuthorizationResponseBuilder();
        }

        public AuthorizationResponseBuilder withAccount(AccountResponse account) {
            this.account = account;
            return this;
        }

        public AuthorizationResponseBuilder withViolations(List<String> violations) {
            this.violations = violations;
            return this;
        }

        public AuthorizationResponse build() {
            AuthorizationResponse authorizationResponse = new AuthorizationResponse();
            authorizationResponse.setAccount(account);
            authorizationResponse.setViolations(violations);
            return authorizationResponse;
        }
    }
}
