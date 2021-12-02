package com.shop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
// 구매/선물
public enum GiftStatus {

    BUY("0"),
    GIFT("1");

    private final String key;


}
