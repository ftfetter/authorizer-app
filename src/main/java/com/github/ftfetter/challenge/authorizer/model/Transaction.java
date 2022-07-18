package com.github.ftfetter.challenge.authorizer.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Transaction {

    private final String merchant;
    private final Integer amount;
    private final ZonedDateTime time;

    private Transaction(String merchant, Integer amount, ZonedDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public String getMerchant() {
        return merchant;
    }

    public Integer getAmount() {
        return amount;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(merchant, that.merchant) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(merchant, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "merchant='" + merchant + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                '}';
    }

    public static final class TransactionBuilder {
        private String merchant;
        private Integer amount;
        private ZonedDateTime time;

        private TransactionBuilder() {
        }

        public TransactionBuilder withMerchant(String merchant) {
            this.merchant = merchant;
            return this;
        }

        public TransactionBuilder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder withTime(ZonedDateTime time) {
            this.time = time;
            return this;
        }

        public Transaction build() {
            return new Transaction(merchant, amount, time);
        }
    }
}
