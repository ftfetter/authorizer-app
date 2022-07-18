package com.github.ftfetter.challenge.authorizer.controller.response;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AccountResponse {

    @SerializedName("active-card")
    private Boolean activeCard;
    @SerializedName("available-limit")
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
        AccountResponse that = (AccountResponse) o;
        return Objects.equals(activeCard, that.activeCard) &&
                Objects.equals(availableLimit, that.availableLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeCard, availableLimit);
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "activeCard=" + activeCard +
                ", availableLimit=" + availableLimit +
                '}';
    }

    public static final class AccountResponseBuilder {
        private Boolean activeCard;
        private Integer availableLimit;

        private AccountResponseBuilder() {
        }

        public static AccountResponseBuilder anAccountResponse() {
            return new AccountResponseBuilder();
        }

        public AccountResponseBuilder withActiveCard(Boolean activeCard) {
            this.activeCard = activeCard;
            return this;
        }

        public AccountResponseBuilder withAvailableLimit(Integer availableLimit) {
            this.availableLimit = availableLimit;
            return this;
        }

        public AccountResponse build() {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setActiveCard(activeCard);
            accountResponse.setAvailableLimit(availableLimit);
            return accountResponse;
        }
    }
}
