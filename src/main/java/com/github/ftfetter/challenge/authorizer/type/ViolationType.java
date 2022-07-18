package com.github.ftfetter.challenge.authorizer.type;

public enum ViolationType {

    ACCOUNT_ALREADY_INITIALIZED ("account-already-initialized"),
    ACCOUNT_NOT_INITIALIZED ("account-not-initialized"),
    CARD_NOT_ACTIVE ("card-not-active"),
    INSUFFICIENT_LIMIT ("insufficient-limit"),
    HIGH_FREQUENCY_SMALL_INTERVAL ("high-frequency-small-interval"),
    DOUBLED_TRANSACTION ("double-transaction");

    private String violation;

    ViolationType(String violation) {
        this.violation = violation;
    }

    public String getViolation() {
        return violation;
    }
}
