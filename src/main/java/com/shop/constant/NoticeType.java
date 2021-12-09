package com.shop.constant;

public enum NoticeType {
    EMAIL("EMAIL"),
    SMS("SMS");

    private final String noticeTypeName;

    NoticeType(String noticeTypeName) {
        this.noticeTypeName = noticeTypeName;
    }

    @Override
    public String toString() {
        return noticeTypeName;
    }

}
