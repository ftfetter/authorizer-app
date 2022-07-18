package com.github.ftfetter.challenge.authorizer.controller.request;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AccountRequest {

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
        AccountRequest that = (AccountRequest) o;
        return Objects.equals(activeCard, that.activeCard) &&
                Objects.equals(availableLimit, that.availableLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeCard, availableLimit);
    }

    @Override
    public String toString() {
        return "AccountDataRequest{" +
                "activeCard=" + activeCard +
                ", availableLimit=" + availableLimit +
                '}';
    }
}
