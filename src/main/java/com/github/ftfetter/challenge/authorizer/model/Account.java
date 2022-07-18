package com.github.ftfetter.challenge.authorizer.model;

import java.util.Objects;

public class Account {

    private Boolean activeCard;
    private Integer availableLimit;

    public Boolean getActiveCard() {
        return activeCard;
    }

    public void setActiveCard(Boolean activeCard) {
        this.activeCard = activeCard;
    }

    public Integer getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(Integer availableLimit) {
        this.availableLimit = availableLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(activeCard, account.activeCard) &&
                Objects.equals(availableLimit, account.availableLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeCard, availableLimit);
    }

    @Override
    public String toString() {
        return "Account{" +
                "activeCard=" + activeCard +
                ", availableLimit=" + availableLimit +
                '}';
    }

    public static final class AccountBuilder {
        private Boolean activeCard;
        private Integer availableLimit;

        private AccountBuilder() {
        }

        public static AccountBuilder anAccount() {
            return new AccountBuilder();
        }

        public AccountBuilder withActiveCard(Boolean activeCard) {
            this.activeCard = activeCard;
            return this;
        }

        public AccountBuilder withAvailableLimit(Integer availableLimit) {
            this.availableLimit = availableLimit;
            return this;
        }

        public Account build() {
            Account account = new Account();
            account.setActiveCard(activeCard);
            account.setAvailableLimit(availableLimit);
            return account;
        }
    }
}
