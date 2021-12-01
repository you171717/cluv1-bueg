package com.shop.constant;

public enum BidSearchSortColumn {
    REG_TIME("regTime"),
    NAME("name"),
    PRICE("price");

    private final String columnName;

    BidSearchSortColumn(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return columnName;
    }
}
