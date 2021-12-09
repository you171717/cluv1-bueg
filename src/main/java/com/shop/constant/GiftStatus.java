package com.shop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum GiftStatus {
    BUY("BUY"),
    GIFT("GIFT");

    private final String statusType;

    GiftStatus(String statusType) {
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return statusType;
    }

}
