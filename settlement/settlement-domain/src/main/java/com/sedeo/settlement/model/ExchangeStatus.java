package com.sedeo.settlement.model;

public enum ExchangeStatus {
    PENDING,
    SETTLED(PENDING);

    private final ExchangeStatus allowedStatus;

    ExchangeStatus() {
        this.allowedStatus = null;
    }

    ExchangeStatus(ExchangeStatus allowedStatus) {
        this.allowedStatus = allowedStatus;
    }

    public boolean isStatusChangePossible(ExchangeStatus status) {
        return status.equals(this.allowedStatus);
    }
}
