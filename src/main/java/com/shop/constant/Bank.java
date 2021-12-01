package com.shop.constant;

public enum Bank {
    IBK("기업"),
    NH("농협"),
    KB("국민"),
    KEB("하나"),
    WOORI("우리"),
    SHINHAN("신한");

    private final String bankName;

    Bank(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return bankName;
    }
}
