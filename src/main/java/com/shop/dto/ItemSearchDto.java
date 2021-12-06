package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

    @Override
    public String toString() {
        return "ItemSearchDto{" +
                "searchDateType='" + searchDateType + '\'' +
                ", searchSellStatus=" + searchSellStatus +
                ", searchBy='" + searchBy + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                '}';
    }
}
