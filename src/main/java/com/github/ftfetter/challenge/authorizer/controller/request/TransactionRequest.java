package com.github.ftfetter.challenge.authorizer.controller.request;

import java.util.Objects;

public class TransactionRequest {

    private String merchant;
    private Integer amount;
    private String time;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRequest that = (TransactionRequest) o;
        return Objects.equals(merchant, that.merchant) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchant, amount, time);
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "merchant='" + merchant + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                '}';
    }
}
